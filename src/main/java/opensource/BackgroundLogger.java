package opensource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class BackgroundLogger
{
	
	private static final int ENTRIES = 64;

	private final ExecutorService executorService;
	private final Disruptor<LogEntry> disruptor;
	private final RingBuffer<LogEntry> ringBuffer;

	BackgroundLogger()
	{
		executorService = Executors.newCachedThreadPool();
		disruptor = new Disruptor<LogEntry>(LogEntry.FACTORY, ENTRIES, executorService);
		disruptor.handleEventsWith(new LogEntryHandler());
		disruptor.start();
		ringBuffer = disruptor.getRingBuffer();
	}

	public void log(String text)
	{
		final long sequence = ringBuffer.next();
		final LogEntry logEntry = ringBuffer.get(sequence);

		logEntry.time = System.currentTimeMillis();
		logEntry.level = 0;
		logEntry.text = text;

		ringBuffer.publish(sequence);
	}

	public void stop()
	{
		disruptor.shutdown();
		executorService.shutdownNow();
	}
	public static void main(String[] args) {
		BackgroundLogger backgroundLogger = new BackgroundLogger();
		for (int i = 0; i < 100; i++) {
			backgroundLogger.log("xxhh");
		}
	}
	
	
}