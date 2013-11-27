package opensource;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class CrawlerEntry {
	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		BlockingDeque<String> urlQueue = new LinkedBlockingDeque<String>(10);
		urlQueue.put("http://www.baidu.com");
	    for (int i = 0; i < 5; i++) {
	      Runnable worker = new Downloader(urlQueue);
	      executor.execute(worker);
	    }
	}

}
