package opensource;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Downloader implements Runnable{
	public BlockingDeque<String> urls;
	public Downloader(BlockingDeque<String> urls) {
		this.urls=urls;
	}

	public void run() {
		try {
			String url = urls.poll();
			/*
			 * 参考代码
			 * Document doc = Jsoup.connect("http://example.com").userAgent("Mozilla").data("name", "jsoup").get(); 
			   Document doc = Jsoup.connect("http://example.com").cookie("auth", "token").post(); 
			 */
			Document document = Jsoup.connect(url).userAgent("Mozilla").get();
			Elements links = document.select("a[href]");
			for(Element link:links) {

				String url_ = link.attr("abs:href");
				if (Util.isWantedUrl(url_)) {
					System.out.println(url_);
					urls.put(url_);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void main(String[] args) {
		
		
	}
}
