package opensource;

import java.net.URI;
import java.net.URISyntaxException;

import com.google.common.net.InternetDomainName;

public class Util {
	public static boolean isWantedUrl(String url) {
		System.out.println(url);
		String host;
		try {
			host = new URI(url).getHost();
		} catch (URISyntaxException e) {
			return false;
		}
	    InternetDomainName domainName = InternetDomainName.fromLenient(host);
	    String domain =  domainName.topPrivateDomain().name();
		return "baidu.com".equals(domain);
	}

}
