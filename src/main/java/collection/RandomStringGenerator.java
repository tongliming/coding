package collection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @Description Coding
 * @Author TLM
 * @Date 2023/3/16 15:04
 * @Version 1.0
 */
public class RandomStringGenerator<T> implements Iterable<T> {
    private final List<T> strList;

    public RandomStringGenerator(List<T> strList) {
        this.strList = strList;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return strList.get((int) (strList.size() * Math.random()));
            }
        };
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("List", "Tree", "Array");
        var generator = new RandomStringGenerator<String>(list);
        for (String str : generator) {
            System.out.println(str);
        }

    }
}
