import java.util.Calendar;

public class Event {
	private final Calendar eventDateTime;
	private final String action;
	private final Worker worker;
	private final Patron patron;
	private final Copy copy;
	
	private Event(EventBuilder builder) {
		
		this.eventDateTime = Calendar.getInstance();
		this.worker = builder.worker;
		this.patron = builder.patron;
		this.copy = builder.copy;
		this.action = builder.action;
	}
	
	public static class EventBuilder {
		private String action;
		private Worker worker;
		private Patron patron;
		private Copy copy;
		
		
		public EventBuilder(String action) {
			this.action = action;
		}
		
		public EventBuilder worker(Worker worker) {
			this.worker = worker;
			return this;
		}
		
		public EventBuilder patron(Patron patron) {
			this.patron = patron;
			return this;
		}
		
		public EventBuilder copy(Copy copy) {
			this.copy = copy;
			return this;
		}
		
		public EventBuilder action(String action) {
			this.action = action;
			return this;
		}
		
		public Event build() {
			return new Event(this);
		}
	}
	
	public Calendar getEventDateTime() {
		return eventDateTime;
	}
	
	public Worker getWorker() {
		return worker;
	}

	public Patron getPatron() {
		return patron;
	}
	
	public Copy getCopy() {
		return copy;
	}

	public String getAction() {
		return action;
	}

	public String toString() {
		return eventDateTime.getTime()
				+ "\n\t" + action
				+ (worker == null ? "" : "\n\t" + worker.toString())
				+ (patron == null ? "" : "\n\t" + patron.toString()) 
				+ (copy == null ? "" : "\n\t" + copy.toString());
	}
}