import java.util.Calendar;
import java.util.Date;

public class Copy {
	private String id;
	private Textbook textbook;
	private Boolean isCheckedOut;
	private Calendar dueDate;
		
	public Copy() {
		this.id = "";
		this.textbook = null;
		this.isCheckedOut = false;
		this.dueDate = null;
	}
	
	public Copy(String id, Textbook textbook) {
		this.id = id;
		this.textbook = textbook;
		this.isCheckedOut = false;
		this.dueDate = null;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Date getDueDate() {
		return this.dueDate.getTime();
	}
	
	public void setDueDate(Calendar calendar) {
		dueDate = calendar;
	}
	
	public void checkOut() {
		this.isCheckedOut = true;
		this.dueDate = Calendar.getInstance();
		this.dueDate.add(Calendar.DATE, 14);
	}
	
	public void checkIn() {
		this.isCheckedOut = false;
		this.dueDate = null;
	}
	
	public boolean isCheckedOut() {
		return this.isCheckedOut;
	}
	
	public String toString() {
		return this.id;
	}
	
	public String getTitle() {
		return textbook.getTitle();
	}
}
