import java.util.HashMap;
import java.util.Map;

public class Log {

	private static Map<Integer, Event> log;
	private Integer nextKey = 1;
	
	static {
		log = new HashMap<Integer, Event>();
	}
	
	public Integer logEvent(Event event) {
		log.put(nextKey, event);
		return nextKey++;
	}
	
	public Event getEvent(int key) {
		return log.get(key);
	}
	
	public String toString() {
		String logString = "";
		
		for(int i = 1; i < log.size(); i++) {
			logString += this.getEvent(i).toString() + '\n';
		}
		return logString;
	}
}