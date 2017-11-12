import java.util.ArrayList;
import java.util.List;

public class Patron {

	private String id;
	private String name;
	private List<Copy> checkedOutCopies = new ArrayList<Copy>();
	private List<Hold> holds = new ArrayList<Hold>();
	
	public Patron() {
		this.id = "";
		this.name = "";
	}
	
	public Patron(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public String toString() {
		return this.id;
	}
	
	public void checkOutCopy(Copy copy) throws HoldException {
		if(holds.size() > 0) {
			throw new HoldException(holds.size() + " hold" + (holds.size() == 1 ? "!" : "s!"));
		}
		checkedOutCopies.add(copy);
	}
	
	public boolean checkInCopy(Copy copy) {
		return checkedOutCopies.remove(copy);
	}
	
	public int getCheckedOutCopyCount() {
		return checkedOutCopies.size();
	}
	
	public List<Copy> getCheckedOutCopies() {
		return checkedOutCopies;
	}
	
	public void addHold(Hold hold) {
		holds.add(hold);
	}
	
	public List<Hold> getHolds() {
		return this.holds;
	}
	
	public void removeHold(Hold hold) {
		holds.remove(hold);
	}
}