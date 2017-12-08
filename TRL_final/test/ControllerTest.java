import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

public class ControllerTest {

	@Before
	public void setUp() throws Exception {	
	}


	@Test
	public void test_login_worker() {
		Controller controller = new Controller();
		controller.loginWorker("W1");
		
		assertEquals("Worker doesn't match", "W1" , controller.activeWorker.getId());
		
	}
	
	@Test
	public void test_initializePatronTransaction_valid_patron() {
		Controller controller = new Controller();
		controller.initializePatronTransaction("P1");
		
		assertEquals("Patrons don't match", "P1", controller.activePatron.getId());
	}
	

	@Test
	public void test_checkoutCopy_valid() {
		Controller controller = new Controller();
		controller.processCheckoutCopy("C1");
		
		assertEquals("Copy doesn't match", "C1", controller.activeCopy.getId());
	}
	
	@Test
	public void test_set_patron_valid() {
		Controller controller = new Controller();
		Patron validPatron = controller.startTransaction("P2");
		
		controller.setActivePatron(validPatron);
		
		assertEquals("Patrons don't match", validPatron, controller.getActivePatron());
		
	}

	@Test
	public void test_set_patron_invalid() {
		Controller controller = new Controller();
		Patron validPatron = controller.startTransaction("W2");
		
		controller.setActivePatron(validPatron);
		
		assertEquals("Patrons don't match", validPatron, controller.getActivePatron());
		
	}
	
	@Test
	public void test_start_transaction() {
		String patronId = "P1";
		Controller controller = new Controller();
		
		Patron patron = controller.startTransaction(patronId);
		
		assertEquals("patron name not as expected", "Test Patron One", patron.getName());
	}
	
	@Test
	public void test_start_transaction_patronID_retained() {
		String patronId = "P1";
		Controller controller = new Controller();
		
		Patron patron = controller.startTransaction(patronId);
		
		assertEquals("patron name not as expected", "P1", patron.getId());
	}
	
	@Test
	public void test_set_transaction_type() {
		Controller controller = new Controller();
		
		controller.setTransactionType("out");
		assertEquals("controller transaction type != out", "out", controller.getTransactionType());
		
		controller.setTransactionType("in");
		assertEquals("controller transaction type != in", "in", controller.getTransactionType());
		
		controller.setTransactionType("lookup");
		assertEquals("controller transcation type != lookup", "lookup", controller.getTransactionType());
		
		assertEquals("controller transaction type should be false", false, controller.setTransactionType("inout"));
	}
	
	@Test
	public void test_check_out_copy() {
		Controller controller = new Controller();
		Patron patron = controller.startTransaction("P2");
		controller.setTransactionType("out");
		Copy copy = controller.addCopyToCheckoutList("C1");
		
		try {
			controller.completeSession();
		} catch (HoldException e) {
			// do nothing
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 14);
		
		assertEquals("check out failure", "Test Title", copy.getTitle());
		assertEquals("check out should set copy's isCheckedOut", true, copy.isCheckedOut());
		assertEquals("copy should be due in 14 days", calendar.getTime().toString(), copy.getDueDate().toString());
		assertEquals("patron should have 1 copy checked out", 1, patron.getCheckedOutCopyCount());
		copy.checkIn();
		patron.checkInCopy(copy);
	}
		
	@Test
	public void test_log_event() {
		Controller controller = new Controller();
		Worker worker = controller.getWorkerObject("W1");
		Patron patron = controller.startTransaction("P1");
		controller.setTransactionType("out");	
		Copy copy = controller.addCopyToCheckoutList("C1");
		
		try {
			controller.completeSession();
		} catch (HoldException e) {
			// do nothing
		}
		
		Event event = new Event.EventBuilder("Complete Session - Check Out")
				.worker(worker)
				.patron(patron)
				.copy(copy)
				.build();
		
		assertEquals("event should contain worker, patron, copy", event.toString(), controller.getLastEvent().toString());
		copy.checkIn();
		patron.checkInCopy(copy);
	}

	@Test
	public void test_validate_patron() {
		String patronId = "P1";
		Controller controller = new Controller();
		
		boolean isPatron = controller.validatePatron(patronId);
		
		assertEquals("patron is not valid", true, isPatron);
	}
	
	@Test
	public void test_validate_and_set_patron_fake_patronID() {
		String patronId = "W9";
		Controller controller = new Controller();
		
		boolean isPatron = controller.validatePatron(patronId);
		
		assertEquals("patron is valid", false, isPatron);
	}
	
	
	@Test
	public void test_get_worker_object() {
		String workerId = "W1";
		Controller controller = new Controller();
		
		Worker worker = controller.getWorkerObject(workerId);
		
		assertEquals("worker name not as expected", "Test Worker One", worker.getName());
	}
	
	@Test
	public void test_validate_worker() {
		String workerId = "W1";
		Controller controller = new Controller();
		
		boolean isWorker = controller.validateWorker(workerId);
				
		assertEquals("worker is not valid", true, isWorker);
	}
	
	@Test
	public void test_validate_and_login_worker_fake_workerID() {
		String workerId = "P9";
		Controller controller = new Controller();
		
		boolean isWorker = controller.validateWorker(workerId);
		
		assertEquals("worker is valid", false, isWorker);
	}

	@Test
	public void test_get_active_patron_string_null_patron() {

		Controller controller = new Controller();
		controller.initializePatronTransaction("NULL");
		
		assertEquals("Patron string doesn't match", "", controller.getActivePatronString());	
		
	}
	
	@Test
	public void test_get_active_patron_string_no_holds() {
		Controller controller = new Controller();
		controller.initializePatronTransaction("P6");
		
		assertEquals("Patron string doesn't match", "%nPatron ID: P6%nPatron Name: Test Patron Six%n2 " + 
					 "copies checked out:%nCopyID: C15  Title: Test Title  Due: Mon Dec 11 11:11:11 " +
					 "CST 2017%nCopyID: C16  Title: Intro to Java  Due: Mon Dec 11 11:11:11 CST " + 
					 "2017%n%n0 holds:%n", controller.getActivePatronString());	
	}

	@Test
	public void test_get_active_patron_string_no_copies() {

		Controller controller = new Controller();
		controller.initializePatronTransaction("P2");
		
		assertEquals("Patron string doesn't match", "%nPatron ID: P2%nPatron Name: Test Patron Two%n0 copies checked out:%n0 holds:%n", controller.getActivePatronString());	
		
	}

	@Test
	public void test_get_active_patron_string_with_hold() {

		Controller controller = new Controller();
		controller.initializePatronTransaction("P7");
		
		assertEquals("%nPatron ID: P7%nPatron Name: Test Patron Seven%n1 copies checked out:%nCopyID: " + 
					 "C17  Title: 100 Uses for Bubble Gum  Due: Mon Dec 11 11:11:11 CST 2017%n%n1 holds:" +
					 "%nCopyID: C17  Title: 100 Uses for Bubble Gum  Due: Mon Dec 11 11:11:11 CST 2017 " +
					 "Message: overdue%n", controller.getActivePatronString());	
		
	}

	@Test
	public void test_get_active_patron_string_bad_ID() {
		Controller controller = new Controller();
		Patron patron = controller.startTransaction("P9");
		
		assertEquals("Patron isn't null", patron == null, true);
	}
	
	@Test
	public void test_get_active_copy_string() {
		Controller controller = new Controller();
		FakeDB db = new FakeDB();
		controller.activeCopy = db.getCopy("C1");
		
		assertEquals("Copy string doesn't match", controller.getActiveCopyString(), "Copy ID: C1 Title Name: Test Title");
	}	

	
	@Test
	public void test_add_copy_to_check_out_queue() {
		Controller controller = new Controller();
		Worker worker = controller.getWorkerObject("W2");
		Patron patron = controller.startTransaction("P2");
		controller.setTransactionType("out");
		Copy copy = controller.addCopyToCheckoutList("C2");
		
		Queue<Copy> checkOutQueue = new LinkedList<Copy>();
		checkOutQueue.add(copy);
		
		assertEquals("check out queue does not match expected value", checkOutQueue, controller.getCheckOutQueue());
		copy.checkIn();
		patron.checkInCopy(copy);
	}

	@Test
	public void test_validate_copy() {
		Controller controller = new Controller();
		
		assertEquals("copy is not valid", true, controller.validateCopy("C1"));
	}
	
	@Test
	public void test_validate_and_checkout_copy_fail() {
		Controller controller = new Controller();
		
		assertEquals("copy is valid", false, controller.validateCopy("W9"));
	}	
	
	@Test
	public void test_complete_check_out_session() {
		Controller controller = new Controller();
		Worker worker = controller.getWorkerObject("W2");
		Patron patron = controller.startTransaction("P2");
		controller.setTransactionType("out");
		Queue<Copy> checkOutQueue = new LinkedList<Copy>();
		Copy copy1 = controller.addCopyToCheckoutList("C1");
		checkOutQueue.add(copy1);
		Copy copy2 = controller.addCopyToCheckoutList("C2");
		checkOutQueue.add(copy2);
		
		assertEquals("check out queue should have 2 copies", 2, controller.getCheckOutQueue().size());
		assertEquals("copy 1 shouldn't be checked out yet", false, copy1.isCheckedOut());

		try {
			controller.completeSession();
		} catch (HoldException e) {
			// do nothing
		}
		
		assertEquals("copy 1 should be checked out", true, copy1.isCheckedOut());
		assertEquals("copy 2 should be checked out", true, copy2.isCheckedOut());
		checkOutQueue.poll();
		checkOutQueue.poll();
		assertEquals("the check out queue should be empty", checkOutQueue, controller.getCheckOutQueue());
		copy1.checkIn();
		patron.checkInCopy(copy1);
		copy2.checkIn();
		patron.checkInCopy(copy2);
	}
	
	@Test
	public void test_get_log() {
		Log log = new Log();
		
		Controller controller = new Controller();
		Worker worker = controller.getWorkerObject("W2");
		Patron patron = controller.startTransaction("P2");
		controller.setTransactionType("out");
		Queue<Copy> checkOutQueue = new LinkedList<Copy>();
		Copy copy1 = controller.addCopyToCheckoutList("C1");
		String action = "Check Out";
		log.logEvent(new Event.EventBuilder(action)
				.worker(worker)
				.patron(patron)
				.copy(copy1)
				.build());
		
		checkOutQueue.add(copy1);
		Copy copy2 = controller.addCopyToCheckoutList("C2");
		log.logEvent(new Event.EventBuilder(action)
				.worker(worker)
				.patron(patron)
				.copy(copy2)
				.build());		
		checkOutQueue.add(copy2);
		try {
			controller.completeSession();
		} catch (HoldException e) {
			// do nothing
		}		
		
		assertEquals("logs should match", log.toString(), controller.getLog().toString());
		copy1.checkIn();
		patron.checkInCopy(copy1);
		copy2.checkIn();
		patron.checkInCopy(copy2);
	}	
	
	@Test
	public void test_get_active_patron() {
		Controller controller = new Controller();
		Worker worker = controller.getWorkerObject("W1");
		Patron patron = controller.startTransaction("P1");
		controller.setTransactionType("out");
		
		assertEquals("active patrons should match", patron, controller.getActivePatron());
	}
	
	@Test
	public void test_validate_and_set_patron_fail() {
		Controller controller = new Controller();
		
		assertEquals("patron does not exist in DB, should fail", false, controller.validatePatron("Patron"));
	}

	
	@Test
	public void test_set_transaction_type_to_in() {
		Controller controller = new Controller();
		Patron patron = controller.startTransaction("P1");
		controller.setTransactionType("in");
		
		assertEquals("transaction type should be 'in'", "in", controller.getTransactionType());
	}
	
	@Test
	public void test_add_copy_to_check_in_queue() {
		Controller controller = new Controller();
		Worker worker = controller.getWorkerObject("W1");
		Patron patron = controller.startTransaction("P1");
		controller.setTransactionType("in");
		Copy copy = controller.addCopyToCheckInList("C1");
		
		Queue<Copy> checkInQueue = new LinkedList<Copy>();
		checkInQueue.add(copy);
		
		assertEquals("check in queue does not match expected value", checkInQueue, controller.getCheckInQueue());
//		copy.checkIn();
//		patron.checkInCopy(copy);
	}

	@Test
	public void test_check_in_copy() {
		Controller controller = new Controller();
		Worker worker = controller.getWorkerObject("W2");
		Patron patron = controller.startTransaction("P2");
		
		controller.setTransactionType("out");
		Copy copy = controller.addCopyToCheckoutList("C2");
		try {
			controller.completeSession();
		} catch (HoldException e1) {
			// do nothing
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 14);
		
		assertEquals("check out failure", "Test Title", copy.getTitle());
		assertEquals("check out should set copy's isCheckedOut", true, copy.isCheckedOut());
		assertEquals("copy should be due in 14 days", calendar.getTime().toString(), copy.getDueDate().toString());
		assertEquals("patron should have 1 copy checked out", 1, patron.getCheckedOutCopyCount());
		
		controller.setTransactionType("in");
		copy = controller.addCopyToCheckInList("C2");
		
		try {
			controller.completeSession();
		} catch (HoldException e) {
			// do nothing
		}
		
		assertEquals("check in failure", "Test Title", copy.getTitle());
		assertEquals("check in should set copy's isCheckedOut to false", false, copy.isCheckedOut());
		assertEquals("patron should have 0 copies checked out", 0, patron.getCheckedOutCopyCount());
		
//		copy.checkIn();
//		patron.checkInCopy(copy);
	}
	
	@Test
	public void test_clear_session() {
		Controller controller = new Controller();
		Worker worker = controller.getWorkerObject("W2");
		Patron patron = controller.startTransaction("P2");
		
		controller.setTransactionType("out");
		Copy copy = controller.addCopyToCheckoutList("C2");
		try {
			controller.completeSession();
		} catch (HoldException e1) {
			// do nothing
		}
		
		controller.clearSession();
		assertEquals("active patron should be null", null, controller.getActivePatron());
		
	}
	
	@Test
	public void test_initializeCopyTransaction_valid_copy() {
		Controller controller = new Controller();
		controller.initializeCopyTransaction("C1");
		
		assertEquals("Copy does not match expected value", "C1", controller.activeCopy.getId());
	}
	
}