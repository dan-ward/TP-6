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
}