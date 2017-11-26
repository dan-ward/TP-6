import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Controller {
	FakeDB db = new FakeDB();
	Log log = new Log();
	int lastEventKey;
	Worker activeWorker;
	Patron activePatron;
	String transactionType;
	Copy activeCopy;
	Queue<Copy> checkOutQueue = new LinkedList<Copy>();
	
	public Controller() {
		this.db = new FakeDB();
	}
	
	public Patron startTransaction(String patronId) {
		this.activePatron = this.db.getPatron(patronId);
		return this.activePatron;
	}
	
	public Boolean setTransactionType(String transactionType) {
		Boolean returnValue;
		String action = "Transaction Type of " + transactionType;

		if(transactionType.equalsIgnoreCase("out") || transactionType.equalsIgnoreCase("in")) {
			this.transactionType = transactionType;
			action = action + " successfully set";
			returnValue = true;
		} else {
			action = action + " failed to set";
			returnValue = false;
		}

		Event setTransactionType = new Event.EventBuilder(action)
				.worker(this.activeWorker)
				.patron(this.activePatron)
				.build();
		log.logEvent(setTransactionType);

		return returnValue;
		
	}
	
	public String getTransactionType() {
		return this.transactionType;
	}
	
	public Patron getActivePatron() {
		return this.activePatron;
	}

	public void setActivePatron(Patron activePatron) {
		this.activePatron = activePatron;
	}

	public String getActivePatronString() {
		return "Patron ID: " + this.activePatron.getId() + " Patron Name: " + this.activePatron.getName();
	}  

	public String getActiveCopyString() {
		return "Copy ID: " + this.activeCopy.getId() + " Title Name: " + this.activeCopy.getTitle();
	}  

	
	public boolean validateAndSetPatron(String patronID) {
		Boolean returnValue;
		String action;
		
		if (this.db.validatePatronID(patronID)) {
			this.setActivePatron(startTransaction(patronID));
			action = "Validated and Set Patron: " + patronID;
			returnValue = true;
		} else {
			action = "Failed to Validated and Set Patron: " + patronID;
			returnValue = false;
		}

		Event validatePatron = new Event.EventBuilder(action)
				.worker(this.activeWorker)
				.patron(this.activePatron)
				.build();
		log.logEvent(validatePatron);

		return returnValue;
	
	}

	public Copy checkOutCopy(String copyId) {
		this.activeCopy = this.db.getCopy(copyId);
		checkOutQueue.add(this.activeCopy);
		return this.activeCopy;
	}

	public boolean validateAndCheckOutCopy(String copyID) {
		Boolean returnValue;
		String action;
				
		if (this.db.validateCopyID(copyID)) {
			this.activeCopy = this.checkOutCopy(copyID);
			action = "Copy Validated and Successfully Set, Copy ID: " + copyID; 
			returnValue = true; 
		} else {
			action = "Copy Failed to Validate, Copy ID: " + copyID;
			returnValue = false;
		}

		Event checkOutCopy = new Event.EventBuilder(action)
				.worker(this.activeWorker)
				.patron(this.activePatron)
				.copy(this.activeCopy)
				.build();
		log.logEvent(checkOutCopy);
		
		return returnValue;
	}
	
	public Event getLastEvent() {
		return log.getEvent(lastEventKey);
	}
	
	public boolean validateAndLoginWorker(String workerID) {

		Boolean returnValue;
		String action;

		if (this.db.validateWorkerID(workerID)) {
			this.activeWorker = this.loginWorker(workerID);
			
			action = "Worker Login Successful for WorkerID: " + workerID;
			returnValue = true;
			
		} else {
			action = "Worker Login Unsuccessful for WorkerID: " + workerID;
			returnValue = false;
		}

		Event workerLogin = new Event.EventBuilder(action)
				.worker(this.activeWorker)
				.build();
		log.logEvent(workerLogin);

		return returnValue;
	}
	
	public Worker loginWorker(String workerId) {
		this.activeWorker = this.db.getWorker(workerId);
		return this.activeWorker;
	}
	
	public Queue<Copy> getCheckOutQueue() {
		return this.checkOutQueue;
	}
	
	public void completeSession() throws HoldException {
		while (checkOutQueue.size() > 0) {
			Copy c = checkOutQueue.poll();
			c.checkOut();
			this.activeCopy = c;
			this.activePatron.checkOutCopy(this.activeCopy);
			String action = "Complete Session - Check Out";
			Event completeSession = new Event.EventBuilder(action)
					.worker(this.activeWorker)
					.patron(this.activePatron)
					.copy(this.activeCopy)
					.build();
			lastEventKey = this.log.logEvent(completeSession);
			checkOutQueue.remove(this.activeCopy);
		}
	}
	
	public Log getLog() {
		return this.log;
	}
	
}