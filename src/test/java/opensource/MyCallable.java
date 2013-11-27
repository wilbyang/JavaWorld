package opensource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
public class MyCallable implements Callable<String>{

	public String call() throws Exception {
		return Thread.currentThread().getName();
	}
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<String>> list = new ArrayList<Future<String>>();
		Callable<String> callable = new MyCallable();
		for(int i=0; i< 100; i++){
			//submit Callable tasks to be executed by thread pool
			Future<String> future = executor.submit(callable);
			//add Future to the list, we can get return value using Future
			list.add(future);
		}
		for(Future<String> fut : list){
			try {
				//print the return value of Future, notice the output delay in console
				// because Future.get() waits for task to get completed
				System.out.println(new Date()+ "::"+fut.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		executor.shutdown();
		
	}

}
