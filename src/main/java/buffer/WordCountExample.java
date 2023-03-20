package buffer;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * @Description Coding
 * @Author TLM
 * @Date 2023/3/20 11:15
 * @Version 1.0
 */
public class WordCountExample {
    @Test
    public void single_count() throws IOException {
        try (val inputStream = new BufferedInputStream(new FileInputStream("word"))) {
            val start = System.currentTimeMillis();
            val buf = new byte[6 * 1024];
            int len = 0;
            val totalMap = new HashMap<String, Integer>();
            while ((len = inputStream.read(buf)) != -1) {
                val copy = Arrays.copyOfRange(buf, 0, len);
                String str = new String(copy);
                Map<String, Integer> countMap = countByString(str);
                for (val entry : countMap.entrySet()) {
                    increaseKey(entry.getKey(), entry.getValue(), totalMap);
                }
            }
            System.out.println("time:" + (System.currentTimeMillis() - start) + "ms");
            System.out.println("total:" + totalMap.size());
            for (val entry : totalMap.entrySet()) {
                System.out.println("key:" + entry.getKey() + ", count:" + entry.getValue());
            }
        }
    }

    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    class CountTask implements Callable<Map<String, Integer>> {

        private final long start;
        private final long end;
        private final String filename;

        public CountTask(long start, long end, String filename) {
            this.start = start;
            this.end = end;
            this.filename = filename;
        }

        @Override
        public Map<String, Integer> call() throws Exception {
            val map = new HashMap<String, Integer>();
            val accessFile = new RandomAccessFile(filename, "rw");
            val channel = accessFile.getChannel();
            val mBuffer = channel.map(
                    FileChannel.MapMode.READ_ONLY,
                    this.start,
                    this.end - this.start
            );
            val charBuffer = StandardCharsets.US_ASCII.decode(mBuffer);
            return countByString(charBuffer.toString());
        }
    }

    private void run(String filename, int chunkSize) throws ExecutionException, InterruptedException {
        val start = System.currentTimeMillis();
        val file = new File(filename);
        long fileLength = file.length();
        long position = 0;
        val futures = new ArrayList<Future<Map<String, Integer>>>();
        while (position < fileLength) {
            val next = Math.min(position + chunkSize, fileLength);
            val countTask = new CountTask(position, next, filename);
            position = next;
            val future = forkJoinPool.submit(countTask);
            futures.add(future);
        }
        val totalMap = new HashMap<String, Integer>();
        for (var future : futures) {
            val map = future.get();
            for (val entry : map.entrySet()) {
                increaseKey(entry.getKey(), entry.getValue(), totalMap);
            }
        }
        System.out.println("cpu:" + Runtime.getRuntime().availableProcessors());
        System.out.println("task size:" + futures.size());
        System.out.println("time:" + (System.currentTimeMillis() - start) + "ms");
        System.out.println("total:" + totalMap.size());
        for (val entry : totalMap.entrySet()) {
            System.out.println("key:" + entry.getKey() + ", count:" + entry.getValue());
        }
    }

    @Test
    public void multi_count() throws Exception {
        val countExample = new WordCountExample();
        countExample.run("word", 3 * 1024 * 1024);
    }

    private void increaseKey(String key, Integer count, Map<String, Integer> totalMap) {
        if (totalMap.containsKey(key)) {
            totalMap.put(key, totalMap.get(key) + count);
        } else {
            totalMap.put(key, count);
        }
    }

    private Map<String, Integer> countByString(String str) {
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        StringTokenizer tokenizer = new StringTokenizer(str);
        while (tokenizer.hasMoreTokens()) {
            val word = tokenizer.nextToken();
            increaseKey(word, 1, countMap);
        }
        return countMap;
    }
}
