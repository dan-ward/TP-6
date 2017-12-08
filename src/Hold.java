
public class Hold {
	private Copy copy;
	private String message;
	
	public Hold(Copy copy, String message) {
		this.copy = copy;
		this.message = message;
	}
	
	public Copy getCopy() {
		return copy;
	}

	public void setCopy(Copy copy) {
		this.copy = copy;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}