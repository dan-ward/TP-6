
public class Worker {
	private String id;
	private String name;
	
	public Worker() {
		this.id = "";
		this.name = "";
	}
	
	public Worker(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
}