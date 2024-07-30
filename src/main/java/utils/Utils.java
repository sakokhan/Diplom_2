package utils;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
public class Utils {
    public static String randomString(int length){
        Random random = new Random();
        int leftLimit = 97;
        int rightLimit = 122;
        StringBuilder buffer = new StringBuilder(length);
        for (int i=0; i<length; i++){
            int randomLimitedInt = leftLimit + (int)(random.nextFloat()*(float)(rightLimit - leftLimit +1));
            buffer.append(Character.toChars(randomLimitedInt));
        }
        return buffer.toString();
    }


    final int min = 1;
    final int max = 10;
    final int rnd = rnd(min, max);

    public static int rnd(int min, int max){
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
    public static Instant timestamp() {
        return Instant.ofEpochSecond(ThreadLocalRandom.current().nextInt(5));
    }
}
