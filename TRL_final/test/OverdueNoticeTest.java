import static org.junit.Assert.*;

import org.junit.Test;

public class OverdueNoticeTest {

	@Test
	public void test_get_patron() {
		FakeDB db = new FakeDB();
		Patron p = db.getPatron("P1");
		OverdueNotice odn = new OverdueNotice(p,"test overdue notice");
		
		assertEquals("Test get patron","P1", odn.getPatron().getId());
		
	}

	@Test
	public void test_set_patron() {
		FakeDB db = new FakeDB();
		Patron p = db.getPatron("P1");
		OverdueNotice odn = new OverdueNotice(p,"test overdue notice");
		
		odn.setPatron(db.getPatron("P2"));
		
		assertEquals("Test get patron","P2", odn.getPatron().getId());
		
	}
	
	@Test
	public void test_get_message() {
		FakeDB db = new FakeDB();
		Patron p = db.getPatron("P1");
		OverdueNotice odn = new OverdueNotice(p,"test overdue notice");
		
		assertEquals("Test get message","test overdue notice", odn.getMessage());
		
	}
	
	@Test
	public void test_set_message() {
		FakeDB db = new FakeDB();
		Patron p = db.getPatron("P1");
		OverdueNotice odn = new OverdueNotice(p,"test overdue notice");
		
		odn.setMessage("new message");
		
		assertEquals("Test set message","new message", odn.getMessage());
		
	}
	
	@Test
	public void test_get_copy() {
		FakeDB db = new FakeDB();
		Patron p = db.getPatron("P1");
		Copy c = db.getCopy("C1");
		OverdueNotice odn = new OverdueNotice(p,"test overdue notice");
		odn.setCopy(c);
		
		assertEquals("Test get copy","C1", odn.getCopies().get(0).getId());
		
	}
	
	
}
