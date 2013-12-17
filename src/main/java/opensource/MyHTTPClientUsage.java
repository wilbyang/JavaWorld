package opensource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class MyHTTPClientUsage {

	public static void main(String[] args) throws Exception{
//		singleThread();
	multipleThread();
	}
	
	private static final int[] ids = {66,121,94,111,48,57,43,49,10,23,102,50,53,8,58,95,17,68,91,74,109,123,70,26,96,52,72,46,55,93,69,12,33,124,44,61,39,5,36,76,84,78,82,30,80,38,60,35,65,34,3,116,100,62,56,7,45,54,32,108,90,13,29,75,125,119,120,92,86,28,71,126,37,87,128,81,88,98,51,117,27,114,107,14,21,106,63,67,115,11,22,85,127};
	private static void singleThread() throws Exception {
		HttpClient httpClient = new HttpClient();
		long begin  = System.nanoTime();
		for (int i = 0; i < ids.length; i++) {
			long unixTime = System.currentTimeMillis() / 1000L;
			GetMethod get = new GetMethod("http://coin38.com/his/"+ids[i]+".do");
			httpClient.executeMethod(get);
			String bytes = get.getResponseBodyAsString();
			System.out.println(bytes);
		}
		long time  = System.nanoTime() - begin;
		System.out.printf("single thread Tasks took %.3f ms to run%n", time/1e6);
	}
	private static void multipleThread() throws Exception {
		HostConfiguration hostConf = new HostConfiguration(); 
		hostConf.setHost("coin38.com", 80, "http");

		HttpConnectionManagerParams connParam = new HttpConnectionManagerParams(); 
		int maxConnection = 40;
		connParam.setMaxConnectionsPerHost(hostConf, maxConnection); 
		connParam.setMaxTotalConnections(maxConnection); 
		MultiThreadedHttpConnectionManager conMgr =new MultiThreadedHttpConnectionManager(); 
		conMgr.setParams(connParam);
		final HttpClient httpClient = new HttpClient(conMgr);
		ExecutorService executor = Executors.newFixedThreadPool(40);
		long start = System.nanoTime();
		for (int i = 0; i < ids.length; i++) {
			final int j = i;
			executor.submit(new Runnable() {
				public void run() {
					long unixTime = System.currentTimeMillis();
					try {
						GetMethod get = new GetMethod("http://coin38.com/his/"+ids[j]+".do?_="+unixTime);
						httpClient.executeMethod(get);

						String bytes = get.getResponseBodyAsString();
						System.out.println(bytes);
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			});
		}
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.HOURS);     
		long time = System.nanoTime() - start;
		System.out.printf("Tasks took %.3f ms to run%n", time/1e6);
		/*
		long begin = System.currentTimeMillis();
		for (int i = 0; i < ids.length; i++) {
			long unixTime = System.currentTimeMillis() / 1000L;
			GetMethod get = new GetMethod("http://coin38.com/his/"+ids[i]+".do?_="+unixTime);
			GetThread getThread = new GetThread(httpClient, get);
			getThread.run();
		}
		long end = System.currentTimeMillis();
		System.out.println(end - begin);
		*/
		
		
	}

	/*
	static class GetThread implements Runnable {
		private HttpClient httpClient;
		private GetMethod method;
		public GetThread(HttpClient httpClient, GetMethod method) {
			this.httpClient = httpClient;
			this.method = method;
		}
		@Override
		public void run(){

			try {
				httpClient.executeMethod(method);

				String bytes = method.getResponseBodyAsString();
				System.out.println(bytes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	*/
}
