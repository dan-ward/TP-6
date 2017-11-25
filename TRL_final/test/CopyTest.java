import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class CopyTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_create_copy() {
		Copy c = new Copy();
		assertNotNull("Copy is null", c);
	}
	
	@Test
	public void test_get_copy_Id() {
		Copy c = new Copy();
		assertNotNull("copy id is null", c.getId());
	}
	
	@Test
	public void test_set_copy_id() {
		Copy c = new Copy();
		c.setId("abc123");
		assertEquals("copy id not set", "abc123", c.getId());
	}
			
	@Test
	public void test_is_checked_out() {
		Copy c = new Copy();
		assertNotNull("checkedOut is null", c.isCheckedOut());
	}
	
	@Test
	public void test_set_is_checked_out() {
		Copy c = new Copy();
		c.checkOut();
		assertEquals("not checked out", true, c.isCheckedOut());
	}
	
	@Test
	public void test_check_out_copy() {
		Copy c = new Copy();
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 14); 
		
		c.checkOut();
		assertEquals("copy should be due in 14 days", calendar.getTime().toString(), c.getDueDate().toString());
		
		calendar.add(Calendar.DAY_OF_MONTH, 14);
		c.setDueDate(calendar);
		
		assertEquals("copy should be due in 28 days", calendar.getTime().toString(), c.getDueDate().toString());
	}
	
	@Test
	public void test_copy_constructor() {
		FakeDB db = new FakeDB();
		Textbook textbook = db.getTextbook("T1");
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 14);		
		
		Copy c = new Copy("C1", textbook);
		
		assertEquals("copy constructor failure - id", "C1", c.getId());
		assertEquals("copy constructor failure - isCheckedOut", false, c.isCheckedOut());
	}

	@Test
	public void test_copy_to_string() {
		FakeDB db = new FakeDB();		
		Copy copy = db.getCopy("C1");
		
		assertEquals("copy toString() failure", "C1", copy.toString());
	}
}