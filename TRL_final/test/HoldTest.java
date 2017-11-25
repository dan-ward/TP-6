import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HoldTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_hold_copy_and_message() {
		Textbook textbook = new Textbook("Test hold title");
		Copy copy = new Copy("C3", textbook);
		Hold hold = new Hold(copy, "Overdue book");
		
		assertEquals("hold should return copy", copy, hold.getCopy());
		assertEquals("hold message not as expected", "Overdue book", hold.getMessage());
	}
	
	@Test
	public void test_set_copy_and_message() {
		Textbook textbook = new Textbook("Test hold title");
		Copy copy = new Copy("C3", textbook);
		Hold hold = new Hold(copy, "Overdue book");
		
		assertEquals("hold should return copy", copy, hold.getCopy());
		assertEquals("hold message not as expected", "Overdue book", hold.getMessage());		
		
		Textbook textbook2 = new Textbook("Wuthering Heights");
		Copy copy2 = new Copy("Copy123", textbook2);
		hold.setCopy(copy2);
		hold.setMessage("MIA");
		
		assertEquals("hold should return same copy", copy2, hold.getCopy());
		assertEquals("hold message not set correctly", "MIA", hold.getMessage());
	}
}