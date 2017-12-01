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
		String activePatronInformation;
		
		activePatronInformation = "%nPatron ID: " + this.activePatron.getId() + "%nPatron Name: " + 
				this.activePatron.getName() +
				"%n" + this.activePatron.getCheckedOutCopyCount() + " copies checked out:%n" +
				this.activePatron.getCheckedOutString() + "%n" + this.activePatron.getHolds().size() + 
				" holds:%n" + this.activePatron.getHoldString();
		
		return activePatronInformation;
		
	}  

	public String getActiveCopyString() {
				
		return "Copy ID: " + this.activeCopy.getId() + " Title Name: " + this.activeCopy.getTitle();
	}  

	
	public boolean validatePatron(String patronID) {
		Boolean isValidPatron;
		String action;
		
		if (this.db.validatePatronID(patronID)) {
			action = "Validated Patron: " + patronID;
			isValidPatron = true;
		} else {
			action = "Failed to Validated Patron: " + patronID;
			isValidPatron = false;
		}

		Event validatePatron = new Event.EventBuilder(action)
				.worker(this.activeWorker)
				.patron(this.activePatron)
				.build();
		log.logEvent(validatePatron);

		return isValidPatron;
		
	}
	
	public void initializePatronTransaction(String patronID) {
		String action;
		
		this.setActivePatron(startTransaction(patronID));
		action = "Initialize Transaction for Patron: " + patronID;

		Event validatePatron = new Event.EventBuilder(action)
				.worker(this.activeWorker)
				.patron(this.activePatron)
				.build();
		log.logEvent(validatePatron);
		
	}
	

	public Copy addCopyToCheckoutList(String copyId) {
		this.activeCopy = this.db.getCopy(copyId);
		checkOutQueue.add(this.activeCopy);
		return this.activeCopy;
	}

	public boolean validateCopy(String copyID) {
		Boolean isValidCopy;
		String action;
				
		if (this.db.validateCopyID(copyID)) {
			action = "Copy Validated, Copy ID: " + copyID; 
			isValidCopy = true; 
		} else {
			action = "Copy Failed to Validate, Copy ID: " + copyID;
			isValidCopy = false;
		}

		Event validateCopy = new Event.EventBuilder(action)
				.worker(this.activeWorker)
				.patron(this.activePatron)
				.copy(this.activeCopy)
				.build();
		log.logEvent(validateCopy);
		
		return isValidCopy;
		
	}
	
	public void processCheckoutCopy(String copyID) {
		String action;
		
		this.activeCopy = this.addCopyToCheckoutList(copyID);
		action = "Copy Successfully Checked Out, Copy ID: " + copyID; 

		Event checkOutCopy = new Event.EventBuilder(action)
				.worker(this.activeWorker)
				.patron(this.activePatron)
				.copy(this.activeCopy)
				.build();
		log.logEvent(checkOutCopy);
		
	}
	
	
	public Event getLastEvent() {
		return log.getEvent(lastEventKey);
	}
	
	public boolean validateWorker(String workerID) {
		Boolean isValidWorker;
		String action;

		if (this.db.validateWorkerID(workerID)) {			
			action = "Worker Validation Successful for WorkerID: " + workerID;
			isValidWorker = true;
			
		} else {
			action = "Worker Validation Unsuccessful for WorkerID: " + workerID;
			isValidWorker = false;
		}

		Event workerLogin = new Event.EventBuilder(action)
				.worker(this.activeWorker)
				.build();
		log.logEvent(workerLogin);

		return isValidWorker;
		
	}
	
	public void loginWorker(String workerID) {
		String action;
		
		this.activeWorker = this.getWorkerObject(workerID);
			
		action = "Worker Login Successful for WorkerID: " + workerID;

		Event workerLogin = new Event.EventBuilder(action)
				.worker(this.activeWorker)
				.build();
		log.logEvent(workerLogin);

	}
	
		
	public Worker getWorkerObject(String workerId) {
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