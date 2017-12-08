import java.util.ArrayList;
import java.util.List;

public class OverdueNotice {
	private Patron patron;
	private List<Copy> copies = new ArrayList<Copy>();
	private String message;
	
	public OverdueNotice(Patron patron, String message) {
		this.patron = patron;
		this.message = message;
	}
	
	public Patron getPatron() {
		return patron;
	}

	public List<Copy> getCopies() {
		return copies;
	}

	public void setPatron(Patron patron) {
		this.patron = patron;
	}

	public void setCopy(Copy copy) {
		this.copies.add(copy);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
