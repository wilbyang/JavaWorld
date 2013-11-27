package opensource;

import java.util.HashSet;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.common.primitives.Ints;

@WebService
public class WebServiceAdd {
	@WebMethod
	public int add(int a, int b) {
		return a+b;
	}

	public static void main(String[] args) {

		WebServiceAdd calculator = new WebServiceAdd();
		String sequence = "";
		Splitter.on(",").omitEmptyStrings().trimResults().split(sequence );
        Endpoint endpoint = Endpoint.publish("http://localhost:8080/calculator", calculator);  
        
        
        //Ints
        int[] array = { 1, 2, 3, 4, 5 };int[] array2 = { 1, 2, 3, 4, 5 };
        int a = 4;
        boolean contains = Ints.contains(array, a);
        int indexOf = Ints.indexOf(array, a);
        int max = Ints.max(array);
        int min = Ints.min(array);
        int[] concat = Ints.concat(array, array2);
        
        //一个key对应多个value的Map
        
        //函数的闭包功能
        Function<String, Integer> strlen = new Function<String, Integer>() {
            public Integer apply(String from) {
                Preconditions.checkNotNull(from);
                return from.length();
            }
        };
        List<String> from = Lists.newArrayList("abc", "defg", "hijkl");
        List<Integer> to = Lists.transform(from, strlen);
        for (int i = 0; i < from.size(); i++) {
            System.out.printf("%s has length %d\n", from.get(i), to.get(i));
        }
        
        
        //支持key有多个值
        Multimap<Integer, String> siblings = Multimaps.newHashMultimap<Integer, String>();
        siblings.put(0, "Kenneth");
        siblings.put(1, "Joe");
        siblings.put(2, "John");
        siblings.put(3, "Jerry");
        siblings.put(3, "Jay");
        siblings.put(5, "Janet");

        for (int i = 0; i < 6; i++) {
            int freq = siblings.get(i).size();
            System.out.printf("%d siblings frequency %d\n", i, freq);
        }
        //双向映射
        private static final BiMap<Integer,String> NUMBER_TO_NAME_BIMAP;
        
          NUMBER_TO_NAME_BIMAP = Maps.newHashBiMap();
          NUMBER_TO_NAME_BIMAP.put(1, "Hydrogen");
          NUMBER_TO_NAME_BIMAP.put(2, "Helium");
          NUMBER_TO_NAME_BIMAP.put(3, "Lithium");
          NUMBER_TO_NAME_BIMAP.inverse().get(elementName);
        
        //set的交集和并集
        HashSet<Integer> setA = new HashSet<Integer>(1, 2, 3, 4, 5);
        HashSet<Integer> setB = new HashSet<Integer>(4, 5, 6, 7, 8);
         
        SetView<Integer> union = Sets.union(setA, setB);
        for (Integer integer : union)
            System.out.println(integer);       
         
        SetView difference = Sets.difference(setA, setB);
        for (Integer integer : difference)
            System.out.println(integer);      
         
        SetView intersection = Sets.intersection(setA, setB);
        for (Integer integer : intersection)
            System.out.println(integer);
	}

}
