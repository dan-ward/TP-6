import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class EventTest {

	@Before
	public void setUp() throws Exception {
	}

	
	@Test
	public void test_create_event() {
		FakeDB db = new FakeDB();		
		Copy copy = db.getCopy("C1");
		Patron patron = db.getPatron("P1");
		Worker worker = db.getWorker("W1");
		
		Event event = new Event.EventBuilder("Check Out")
				.worker(worker)
				.patron(patron)
				.copy(copy)
				.build();
		
		assertNotNull("event is null", event);
	}

	@Test
	public void test_get_event() {
		FakeDB db = new FakeDB();		
		Copy copy = db.getCopy("C1");
		Patron patron = db.getPatron("P1");
		Worker worker = db.getWorker("W1");
		
		Event event = new Event.EventBuilder("Check In")
				.worker(worker)
				.patron(patron)
				.copy(copy)
				.build();		
			
		assertEquals("event Patron not correct", patron, event.getPatron());
		assertEquals("event Copy not correct", copy, event.getCopy());
	}
	
	@Test
	public void test_event_to_string() {
		FakeDB db = new FakeDB();		
		Copy copy = db.getCopy("C1");
		Patron patron = db.getPatron("P1");
		Worker worker = db.getWorker("W1");
		String action = "Check Out";
		
		Event event = new Event.EventBuilder(action)
				.worker(worker)
				.patron(patron)
				.copy(copy)
				.build();
		
		assertEquals("event not as expected", event.getEventDateTime().getTime()
				+ "\n\t" + action
				+ "\n\t" + worker.toString()
				+ "\n\t" + patron.toString() 
				+ "\n\t" + copy.toString()
				, event.toString());
	}
	
	@Test
	public void test_event_to_string_without_copy() {
		FakeDB db = new FakeDB();		
		Patron patron = db.getPatron("P1");
		Worker worker = db.getWorker("W1");
		String action = "Check Out";
		
		Event event = new Event.EventBuilder(action)
				.worker(worker)
				.patron(patron)
				.build();
		
		assertEquals("event not as expected", event.getEventDateTime().getTime()
				+ "\n\t" + action
				+ "\n\t" + worker.toString()
				+ "\n\t" + patron.toString() 
				, event.toString());
	}	
}