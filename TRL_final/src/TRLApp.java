

public class TRLApp {

	public static Controller TRLController = new Controller();
	
	private static void welcomeMessage() {
		StdOut.println("Welcome to the TRL");
	}
	private static String getWorkerId() {
		String workerID;

		StdOut.print("Please enter your WorkerID (e.g., W1 or W2): ");
		workerID = StdIn.readString();
		
		while (!TRLController.validateWorker(workerID)) {
			StdOut.println("The worker ID of: " + workerID + " is not valid.");
			StdOut.print("Please enter your WorkerID: ");
			workerID = StdIn.readString();			
		}
		
		return workerID;
		
	}
		
	private static void loginWorker(String workerID) {
		TRLController.loginWorker(workerID);
	}
	
	private static String getPatronID() {
		String patronID;
		StdOut.print("Please enter the PatronID (e.g., P1 or P2): ");
		patronID = StdIn.readString();

		while (!TRLController.validatePatron(patronID)) {
			StdOut.println("The patron ID of: " + patronID + " is not valid.");
			StdOut.print("Please enter the PatronID: ");
			patronID = StdIn.readString();
		}
		
		return patronID;
	}
	
	private static void initializePatronTransaction(String patronID) {
		TRLController.initializePatronTransaction(patronID);
	}
	
	private static void printPatronInformation() {
		StdOut.println(TRLController.getActivePatronString());
	}
	
	private static String setTransactionType() {
		
		String transactionType;

		StdOut.print("Please enter the transaction type (out/in): ");
		transactionType = StdIn.readString();

		while (!TRLController.setTransactionType(transactionType)) {
			StdOut.println("The transaction type of: " + transactionType + " is not valid.");
			StdOut.print("Please enter the transaction type (out/in): ");
			transactionType = StdIn.readString();
		}

		return transactionType;	
	}

	private static void checkoutCopy() {
		
		String copyID;
		
		StdOut.print("Please enter the copy ID (e.g., C1 or C2): ");
		copyID = StdIn.readString();

		while (!TRLController.validateCopy(copyID)) {
			StdOut.println("The copy ID of: " + copyID + " is not valid.");
			StdOut.print("Please enter a valid copy ID: ");
			copyID = StdIn.readString();
		}
		
		TRLController.checkOutCopy(copyID);

		StdOut.println(TRLController.getActiveCopyString());
		
		StdOut.println("Copy " + copyID + " was successfully added to the checkout queue");	
	}

	
	private static void checkoutCopies() {
		String checkOutAnother = "y";
		while (checkOutAnother.equalsIgnoreCase("y")) {
			checkoutCopy();
			StdOut.print("Would you like to checkout another y/n? ");
			checkOutAnother = StdIn.readString();
		}
		
		while (TRLController.checkOutQueue.size() > 0) {
			Copy c = TRLController.checkOutQueue.poll();
			StdOut.println("Copy ID: " + c.getId() + " Title: " + c.getTitle() + " is being checked out.");
		}
		
		try {
			TRLController.completeSession();
			StdOut.println("All of your copies have been checked out.  Thank you!");
		} catch (HoldException e) {
			StdOut.println("Sorry your copies failed to checkout, please try again later.");
		}
	}
	
	public static void main(String[] args) {
		String transactionType;
				
		welcomeMessage();
		
		loginWorker(getWorkerId());

		initializePatronTransaction(getPatronID());
		
		printPatronInformation();
		
		transactionType = setTransactionType();
		
		if (transactionType.equals("out")) {
			checkoutCopies();
		} else {
			StdOut.println("Sorry you entered an invalid transaction type");
		}
	}
}