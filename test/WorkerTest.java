import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WorkerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_worker_create() {
		Worker worker = new Worker();
		
		assertNotNull("worker is null", worker);
	}
	
	@Test
	public void test_worker_constructor() {
		Worker worker = new Worker("W1", "Bob");
		
		assertEquals("worker id not retrieved", "W1", worker.getId());
		assertEquals("worker id not retrieved", "Bob", worker.getName());
	}

	@Test
	public void test_set_worker_id() {
		Worker worker = new Worker();
		
		worker.setId("W1");
		assertEquals("workerId not set", "W1", worker.getId());
	}
	

	@Test
	public void test_worker_set_worker_name() {
		Worker worker = new Worker();
		
		worker.setName("Bob");
		assertEquals("workerName not set", "Bob", worker.getName());
	}
	
	@Test
	public void test_worker_to_string() {
		FakeDB db = new FakeDB();		
		Worker worker = db.getWorker("W1");
		
		assertEquals("worker toString() failure", "W1", worker.toString());
	}
}