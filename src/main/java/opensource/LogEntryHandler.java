package opensource;
import com.lmax.disruptor.EventHandler;
public class LogEntryHandler implements EventHandler<LogEntry>
{
 
    public LogEntryHandler()
    {
    }

	public void onEvent(LogEntry logEntry, long arg1, boolean arg2) throws Exception {
		System.out.println(logEntry.text);
	}
 
    
 
}
