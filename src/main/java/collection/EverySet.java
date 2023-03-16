package collection;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * @Description Coding
 * @Author TLM
 * @Date 2023/3/16 15:33
 * @Version 1.0
 */
public class EverySet {
    public static void main(String[] args) {
        Set<Integer> hashSet = new HashSet<>(){
            {
                add(1);
                add(99);
                add(77);
                add(2);
                add(11);
            }
        };
        System.out.println(hashSet.stream().collect(Collectors.toList()));

        Set<Integer> treeSet = new TreeSet<>(){
            {
                add(1);
                add(99);
                add(77);
                add(2);
                add(11);
            }
        };
        System.out.println(treeSet.stream().collect(Collectors.toList()));

        Set<Integer> skipListSet = new ConcurrentSkipListSet<>(){{
            add(1);
            add(99);
            add(77);
            add(2);
            add(11);
        }};
        System.out.println(skipListSet.stream().collect(Collectors.toList()));
    }
}
