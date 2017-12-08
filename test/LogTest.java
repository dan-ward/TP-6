import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LogTest {

	@Before
	public void setUp() throws Exception {
	}

	
	@Test
	public void test_log_event_check_out() {
		Copy copy = new Copy("C1", new Textbook("Test Textbook"));
		Patron patron = new Patron("P1", "Test Patron");
		Worker worker = new Worker("W1", "Test Worker");
		String action = "Check Out";
		Event event = new Event.EventBuilder(action)
				.worker(worker)
				.patron(patron)
				.copy(copy)
				.build();
		Log log = new Log();
		
		int key = log.logEvent(event);
		
		assertEquals("event", event, log.getEvent(key));
	}
	
	@Test
	public void test_log_event_check_in() {
		Copy copy = new Copy("C1", new Textbook("Test Textbook"));
		Patron patron = new Patron("P1", "Test Patron");
		Worker worker = new Worker("W1", "Test Worker");
		String action = "Check In";
		Event event = new Event.EventBuilder(action)
				.worker(worker)
				.patron(patron)
				.copy(copy)
				.build();
		Log log = new Log();
		
		int key = log.logEvent(event);
		
		assertEquals("event", event, log.getEvent(key));
	}
}