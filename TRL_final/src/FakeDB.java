import java.util.ArrayList;
import java.util.Calendar;
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
		textbookStore.put("T2", new Textbook("Intro to Java"));
		textbookStore.put("T3", new Textbook("100 Uses for Bubble Gum"));
		textbookStore.put("T4", new Textbook("NoSQL"));
		textbookStore.put("T5", new Textbook("Going with GoLang"));
		textbookStore.put("T6", new Textbook("DBA's Guide to MSSQL Databases"));
		textbookStore.put("T7", new Textbook("Scrum for Masters"));
		
		
		workerStore = new HashMap<String, Worker>();
		workerStore.put("W1",  new Worker("W1", "Test Worker One"));
		workerStore.put("W2",  new Worker("W2", "Test Worker Two"));
		
		patronStore = new HashMap<String, Patron>();
		patronStore.put("P1", new Patron("P1", "Test Patron One"));
		patronStore.put("P2", new Patron("P2", "Test Patron Two"));
		patronStore.put("P3", new Patron("P3", "Test Patron Three"));
		patronStore.put("P4", new Patron("P4", "Test Patron Four"));
		patronStore.put("P5", new Patron("P5", "Test Patron Five"));
		patronStore.put("P6", new Patron("P6", "Test Patron Six"));
		patronStore.put("P7", new Patron("P7", "Test Patron Seven"));
		
		copyStore = new HashMap<String, Copy>();
		copyStore.put("C1", new Copy("C1", textbookStore.get("T1")));
		copyStore.put("C2", new Copy("C2", textbookStore.get("T1")));
		copyStore.put("C3", new Copy("C3", textbookStore.get("T2")));
		copyStore.put("C4", new Copy("C4", textbookStore.get("T2")));
		copyStore.put("C5", new Copy("C5", textbookStore.get("T3")));
		copyStore.put("C6", new Copy("C6", textbookStore.get("T3")));
		copyStore.put("C7", new Copy("C7", textbookStore.get("T4")));
		copyStore.put("C8", new Copy("C8", textbookStore.get("T4")));
		copyStore.put("C9", new Copy("C9", textbookStore.get("T5")));
		copyStore.put("C10", new Copy("C10", textbookStore.get("T5")));
		copyStore.put("C11", new Copy("C11", textbookStore.get("T6")));
		copyStore.put("C12", new Copy("C12", textbookStore.get("T6")));
		copyStore.put("C13", new Copy("C13", textbookStore.get("T7")));
		copyStore.put("C14", new Copy("C14", textbookStore.get("T7")));
		copyStore.put("C15", new Copy("C15", textbookStore.get("T1")));
		copyStore.put("C16", new Copy("C16", textbookStore.get("T2")));
		copyStore.put("C17", new Copy("C17", textbookStore.get("T3")));
	}

	public FakeDB() {
		
		try {
			
			
			Calendar tempDueDate = Calendar.getInstance();
			tempDueDate.set(2017, 11, 11, 11, 11, 11);
			
			if (!copyStore.get("C15").isCheckedOut()) {
				copyStore.get("C15").setDueDate(tempDueDate);
				copyStore.get("C15").setCheckedOut(true);
				copyStore.get("C16").setDueDate(tempDueDate);
				copyStore.get("C16").setCheckedOut(true);
				
				copyStore.get("C17").setDueDate(tempDueDate);
				copyStore.get("C17").setCheckedOut(true);
				
				patronStore.get("P6").checkOutCopy(copyStore.get("C15"));
				patronStore.get("P6").checkOutCopy(copyStore.get("C16"));
				patronStore.get("P7").checkOutCopy(copyStore.get("C17"));
				
				Hold p7Hold = new Hold(copyStore.get("C17"),"overdue");
				
				patronStore.get("P7").addHold(p7Hold);
				
			}
		} catch (HoldException e) {
			
		}
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
	
	public Map<String, Patron> getPatronStore() {
		return patronStore;
	}
}