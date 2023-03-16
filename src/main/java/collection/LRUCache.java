package collection;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @Description Coding
 * @Author TLM
 * @Date 2023/3/16 16:01
 * @Version 1.0
 */
public class LRUCache<K,V> implements Iterable {
    private final int MAX = 3;
    private LinkedHashMap<K, V> cacheModel = new LinkedHashMap<>();

    public void cache(K key, V value) {
        if (cacheModel.containsKey(key)) {
            cacheModel.remove(key);
        } else if (cacheModel.size() >= MAX) {
            Iterator iterator = cacheModel.keySet().iterator();
            cacheModel.remove(iterator.next());
        }
        cacheModel.put(key, value);
    }

    @Override
    public Iterator<K> iterator() {
        var it = cacheModel.entrySet().iterator();
        return new Iterator<K>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public K next() {
                return it.next().getKey();
            }
        };
    }

    public static void main(String[] args) {
        LRUCache<String, Integer> cache = new LRUCache();
        cache.cache("A", 1);
        cache.cache("B", 1);
        cache.cache("E", 1);
        cache.cache("F", 1);

        var iterator = cache.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
        }
    }
}
