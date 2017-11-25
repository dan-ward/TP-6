import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeDB {
	
	private static Map<String, Textbook> textbookStore;
	private static Map<String, Worker> workerStore;
	private static Map<String, Patron> patronStore;
	private static Map<String, Copy> copyStore;
	
	static {
		textbookStore = new HashMap<String, Textbook>();
		textbookStore.put("T1", new Textbook("Test Title"));
		
		workerStore = new HashMap<String, Worker>();
		workerStore.put("W1",  new Worker("W1", "Test Worker"));
		workerStore.put("W2",  new Worker("W2", "Test Worker"));
		
		patronStore = new HashMap<String, Patron>();
		patronStore.put("P1", new Patron("P1", "Test Patron"));
		patronStore.put("P2", new Patron("P2", "Test Patron"));
		
		copyStore = new HashMap<String, Copy>();
		copyStore.put("C1", new Copy("C1", textbookStore.get("T1")));
		copyStore.put("C2", new Copy("C2", textbookStore.get("T1")));
	}
	
	public Textbook getTextbook(String key) {
		return textbookStore.get(key);
	}
	
	public boolean validateWorkerID(String key) {
		if (workerStore.get(key) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public Worker getWorker(String key) {
		return workerStore.get(key);
	}

	public boolean validatePatronID(String key) {
		if (patronStore.get(key) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public Patron getPatron(String key) {
		return patronStore.get(key);
	}

	public boolean validateCopyID(String key) {
		if (copyStore.get(key) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public Copy getCopy(String key) {
		return copyStore.get(key);
	}
	
}