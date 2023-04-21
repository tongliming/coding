package buffer;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

/**
 * @Description Coding
 * @Author TLM
 * @Date 2023/3/20 9:47
 * @Version 1.0
 */
public class BufferExample {

    @Test
    public void generate() throws IOException {
        var filename = "word";
        Random random = new Random();
        var bufferSize = 4 * 1024;
        try (var fileWriter = new BufferedOutputStream(new FileOutputStream(filename), bufferSize)) {
            var start = System.currentTimeMillis();
            for (int i = 0; i < 100000000; i++) {
                for (int j = 0; j < 5; j++) {
                    fileWriter.write(97 + random.nextInt(5));
                }
                fileWriter.write(32);
            }
            System.out.println("执行时间：" + (System.currentTimeMillis() - start));
        }
    }

    @Test
    public void read() throws IOException {
        var filename = "word";
        try (var fileReader = new FileInputStream(filename)) {
            int word;
            var start = System.currentTimeMillis();
            while ((word = fileReader.read()) != -1) {

            }
            System.out.println("执行时间：" + (System.currentTimeMillis() - start));
        }
    }

    @Test
    public void read_with_buffer() throws IOException {
        var filename = "word";
        var bufferSize = 4 * 1024;
        try (var fileReader = new BufferedInputStream(new FileInputStream(filename), bufferSize)) {
            int word;
            var start = System.currentTimeMillis();
            while ((word = fileReader.read()) != -1) {

            }
            System.out.println("执行时间：" + (System.currentTimeMillis() - start));
        }
    }


    @Test
    public void read_with_nio() throws IOException {
        var filename = "word";
        var buffer = ByteBuffer.allocate(4 * 1024);
        try (var channel = new FileInputStream(filename).getChannel()) {
            var start = System.currentTimeMillis();
            while (channel.read(buffer) != -1) {
                buffer.flip();  // 读写模式切换
                System.out.println(new String(buffer.array()));
                buffer.clear();
            }
            System.out.println("执行时间：" + (System.currentTimeMillis() - start));
        }
    }

    @Test
    public void test_chinese() {
        val str = "君不见，黄河之水天上来，奔流到海不复回。\n" +
                "君不见，高堂明镜悲白发，朝如青丝暮成雪。\n" +
                "人生得意须尽欢，莫使金樽空对月。\n" +
                "天生我材必有用，千金散尽还复来。\n" +
                "烹羊宰牛且为乐，会须一饮三百杯。\n" +
                "岑夫子，丹丘生，将进酒，杯莫停。\n" +
                "与君歌一曲，请君为我倾耳听。\n" +
                "钟鼓馔玉不足贵，但愿长醉不愿醒。\n" +
                "古来圣贤皆寂寞，惟有饮者留其名。\n" +
                "陈王昔时宴平乐，斗酒十千恣欢谑。\n" +
                "主人何为言少钱，径须沽取对君酌。\n" +
                "五花马，千金裘，呼儿将出换美酒，与尔同销万古愁。";
        val charset = StandardCharsets.UTF_8;
        val bytes = charset.encode(str).array();
        val bRange = Arrays.copyOfRange(bytes, 0, 11);
        val byteBuffer = ByteBuffer.allocate(12);
        val charBuffer = CharBuffer.allocate(12);
        byteBuffer.put(bRange);
        byteBuffer.flip();  // 读写模式切换
        charset.newDecoder().decode(byteBuffer, charBuffer, true);

        charBuffer.flip();
        val tmp = new char[charBuffer.length()];
        if (charBuffer.hasRemaining()) {
            charBuffer.get(tmp);
            System.out.println(new String(tmp));
        }
    }

}
