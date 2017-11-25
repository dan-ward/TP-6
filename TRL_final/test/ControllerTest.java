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
	public void test_start_transaction() {
		String patronId = "P1";
		Controller controller = new Controller();
		
		Patron patron = controller.startTransaction(patronId);
		
		assertEquals("patron name not as expected", "Test Patron", patron.getName());
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
		
		assertEquals("controller transaction type should be false", false, controller.setTransactionType("inout"));
	}
	
	@Test
	public void test_check_out_copy() {
		Controller controller = new Controller();
		Patron patron = controller.startTransaction("P2");
		controller.setTransactionType("out");
		Copy copy = controller.checkOutCopy("C1");
		
		try {
			controller.completeSession();
		} catch (HoldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		Worker worker = controller.loginWorker("W1");
		Patron patron = controller.startTransaction("P1");
		controller.setTransactionType("out");	
		Copy copy = controller.checkOutCopy("C1");
		
		try {
			controller.completeSession();
		} catch (HoldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public void test_validate_and_set_patron() {
		String patronId = "P1";
		Controller controller = new Controller();
		
		boolean isPatron = controller.validateAndSetPatron(patronId);
		
		assertEquals("patron is not valid", true, isPatron);
	}
	
	@Test
	public void test_validate_and_set_patron_fake_patronID() {
		String patronId = "P9";
		Controller controller = new Controller();
		
		boolean isPatron = controller.validateAndLoginWorker(patronId);
		
		assertEquals("patron is valid", false, isPatron);
	}
	
	
	@Test
	public void test_login_worker() {
		String workerId = "W1";
		Controller controller = new Controller();
		
		Worker worker = controller.loginWorker(workerId);
		
		assertEquals("worker name not as expected", "Test Worker", worker.getName());
	}
	
	@Test
	public void test_validate_and_login_worker() {
		String workerId = "W1";
		Controller controller = new Controller();
		
		boolean isWorker = controller.validateAndLoginWorker(workerId);
		
		assertEquals("worker is not valid", true, isWorker);
	}
	
	@Test
	public void test_validate_and_login_worker_fake_workerID() {
		String workerId = "W9";
		Controller controller = new Controller();
		
		boolean isWorker = controller.validateAndLoginWorker(workerId);
		
		assertEquals("worker is valid", false, isWorker);
	}


	@Test
	public void test_get_active_patron_string() {
		Controller controller = new Controller();
		Patron patron = controller.startTransaction("P1");
		
		assertEquals("Patron string doesn't match", controller.getActivePatronString(), "Patron ID: P1 Patron Name: Test Patron");
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
		Worker worker = controller.loginWorker("W2");
		Patron patron = controller.startTransaction("P2");
		controller.setTransactionType("out");
		Copy copy = controller.checkOutCopy("C2");
		
		Queue<Copy> checkOutQueue = new LinkedList<Copy>();
		checkOutQueue.add(copy);
		
		assertEquals("check out queue does not match expected value", checkOutQueue, controller.getCheckOutQueue());
		copy.checkIn();
		patron.checkInCopy(copy);
	}

	@Test
	public void test_validate_and_checkout_copy() {
		Controller controller = new Controller();
		
		assertEquals("copy is not valid", true, controller.validateAndCheckOutCopy("C1"));
	}
	
	@Test
	public void test_validate_and_checkout_copy_fail() {
		Controller controller = new Controller();
		
		assertEquals("copy is valid", false, controller.validateAndCheckOutCopy("C9"));
	}	
	
	@Test
	public void test_complete_session() {
		Controller controller = new Controller();
		Worker worker = controller.loginWorker("W2");
		Patron patron = controller.startTransaction("P2");
		controller.setTransactionType("out");
		Queue<Copy> checkOutQueue = new LinkedList<Copy>();
		Copy copy1 = controller.checkOutCopy("C1");
		checkOutQueue.add(copy1);
		Copy copy2 = controller.checkOutCopy("C2");
		checkOutQueue.add(copy2);
		
		assertEquals("check out queue should have 2 copies", 2, controller.getCheckOutQueue().size());
		assertEquals("copy 1 shouldn't be checked out yet", false, copy1.isCheckedOut());

		try {
			controller.completeSession();
		} catch (HoldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		Worker worker = controller.loginWorker("W2");
		Patron patron = controller.startTransaction("P2");
		controller.setTransactionType("out");
		Queue<Copy> checkOutQueue = new LinkedList<Copy>();
		Copy copy1 = controller.checkOutCopy("C1");
		String action = "Check Out";
		log.logEvent(new Event.EventBuilder(action)
				.worker(worker)
				.patron(patron)
				.copy(copy1)
				.build());
		
		checkOutQueue.add(copy1);
		Copy copy2 = controller.checkOutCopy("C2");
		log.logEvent(new Event.EventBuilder(action)
				.worker(worker)
				.patron(patron)
				.copy(copy2)
				.build());		
		checkOutQueue.add(copy2);
		try {
			controller.completeSession();
		} catch (HoldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		Worker worker = controller.loginWorker("W1");
		Patron patron = controller.startTransaction("P1");
		controller.setTransactionType("out");
		
		assertEquals("active patrons should match", patron, controller.getActivePatron());
	}
	
	@Test
	public void test_validate_and_set_patron_fail() {
		Controller controller = new Controller();
		
		assertEquals("patron does not exist in DB, should fail", false, controller.validateAndSetPatron("Patron"));
	}
}