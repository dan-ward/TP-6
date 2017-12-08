import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TextbookTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_get_title() {
		Textbook textbook = new Textbook("This is a Test Title");
		assertEquals("title failure", "This is a Test Title", textbook.getTitle());
	}
}