import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PatronTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void test_create_patron() {
		Patron p = new Patron();
		assertNotNull("Patron is null", p);
	}
	
	@Test
	public void test_set_patron_id() {
		Patron p = new Patron();
		p.setId("123abc");
		assertEquals("patronId not set", "123abc", p.getId());
	}
	
	@Test
	public void test_set_parton_name() {
		Patron patron = new Patron("patron123", "p123");
		patron.setName("Billy");
		
		assertEquals("Patron name not set correctly", "Billy", patron.getName());
	}
	
	@Test
	public void test_patron_to_string() {
		FakeDB db = new FakeDB();		
		Patron patron = db.getPatron("P1");
		
		assertEquals("patron toString() failure", "P1", patron.toString());
	}
	
	@Test
	public void test_patron_check_in_copy() {
		FakeDB db = new FakeDB();
		Patron patron = db.getPatron("P1");
		Copy copy = db.getCopy("C1");
		
		try {
			patron.checkOutCopy(copy);
		} catch (HoldException e) {
			e.printStackTrace();
		}
		
		assertEquals("copy not checked out, in correctly", true, patron.checkInCopy(copy));
	}
	
	@Test
	public void test_get_checked_out_copy_count() {
		FakeDB db = new FakeDB();
		Patron patron = db.getPatron("P1");
		Copy copy1 = db.getCopy("C1");
		Copy copy2 = db.getCopy("C2");
		
		try {
			patron.checkOutCopy(copy1);
		} catch (HoldException e1) {
			e1.printStackTrace();
		}
		try {
			patron.checkOutCopy(copy1);
		} catch (HoldException e) {
			e.printStackTrace();
		}
		
		assertEquals("patron should have 2 copies checked out", 2, patron.getCheckedOutCopyCount());
	}

	@Test
	public void test_patron_hold() {
		Textbook textbook = new Textbook("Test hold title");
		Copy copy = new Copy("C3", textbook);
		Hold hold = new Hold(copy, "Overdue book");
		Patron patron = new Patron("P3", "Test hold patron");

		assertEquals("patron should have 0 holds", 0, patron.getHolds().size());
		
		patron.addHold(hold);		
		List<Hold> holds = new ArrayList<Hold>();
		holds.add(hold);

		assertEquals("patron should have 1 hold", 1, patron.getHolds().size());
		assertEquals("hold lists should match", holds, patron.getHolds());
		
		patron.removeHold(hold);
		holds.remove(hold);
		
		assertEquals("patron should have 0 holds", 0, patron.getHolds().size());
	}
	
	@Test
	public void test_patron_check_out_with_hold() {
		Textbook textbook = new Textbook("Test hold title");
		Copy copy1 = new Copy("C1", textbook);
		Copy copy2 = new Copy("C2", textbook);
		Hold hold = new Hold(copy1, "Overdue book");
		Patron patron = new Patron("P3", "Test hold patron");
		
		String holdMessages = "";
		
		try {
			patron.checkOutCopy(copy1);
		} catch (HoldException e) {
			holdMessages += e.toString();
			StdOut.println(e.toString());
		}
		patron.addHold(hold);
		try {			
			patron.checkOutCopy(copy2);
		} catch (HoldException e) {
			holdMessages += e.toString();
			StdOut.println(e.toString());
		}
		
		assertEquals("exception message should match", "HoldException: 1 hold!", holdMessages);
		assertEquals("patron should have 1 copy checked out", 1, patron.getCheckedOutCopyCount());
	}	
}