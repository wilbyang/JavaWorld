package opensource;
import java.io.IOException;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.google.common.base.CharMatcher;

public class LearningTest {

	@Test
	public void test() {
		Pattern p = Pattern.compile(".xx.");
		Matcher matcher = p.matcher("yxxh");
		System.out.println(matcher.matches());
	}
	@Test
	public void testUnicodeRegularExpression() {
		// 要匹配的字符串     
		String source = "<span title='5 星级酒店' class='dx dx5'>";  
		// 将上面要匹配的字符串转换成小写     
		// source = source.toLowerCase();     
		// 匹配的字符串的正则表达式     
		String reg_charset = "<span[^>]*?title=\'([0-9]*[\\s|\\S]*[\u4E00-\u9FA5]*)\'[\\s|\\S]*class=\'[a-z]*[\\s|\\S]*[a-z]*[0-9]*\'";       
		Pattern p = Pattern.compile(reg_charset);     
		Matcher m = p.matcher(source);     
		while (m.find()) {
			System.out.println(m.group(1));
		}  
	}
	@Test
	public void guavaStringTest() {
		String phoneNumber = CharMatcher.DIGIT.retainFrom("my phone number is 123456789");
		System.out.println(phoneNumber);
	}
	@Test
	public void jsoupConnectTest() throws IOException {
		Document doc = Jsoup.connect("http://www.baidu.com").timeout(6000).get();
		Elements links = doc.select("a[href]");
		for (Element link : links) {
		    System.out.println(link.attr("abs:href"));
		}
	}
	@Test
	public void velocityTeset() {
		/*  first, get and initialize an engine  */
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        /*  next, get the Template  */
        Template t = ve.getTemplate( "helloworld.vm" );
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("name", "World");
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        /* show the World */
        System.out.println( writer.toString() );   
	}
}
