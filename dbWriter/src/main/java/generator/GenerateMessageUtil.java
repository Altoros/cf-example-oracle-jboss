package generator;

import java.util.Random;

/**
 * @author: Andrey Kozlov
 */
public final class GenerateMessageUtil {

    private static final Random random = new Random();

    private GenerateMessageUtil() {
    }

    public static int generateMessage(){
        return random.nextInt();
    }

}
