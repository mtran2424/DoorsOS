/*
 * Author: My Tran
 * Filename: GoodbyeWorld.java
 * Description: Representative of a process. Indefinitely prints out "Goodbye World" to
 * the console.
 */

import java.time.Clock;
import java.util.Random;

public class GoodbyeWorld extends UserlandProcess {
    /*
     * Run method required for runnable
     */
    @Override
    public void run() {
        int id = new Random(Clock.systemDefaultZone().millis()).nextInt(100);
        // infinite loop outputs same text
        while (true) {
            // Sleep the thread for more digestible output
            try {
                Thread.sleep(240);
            } catch (Exception e) {}

            System.out.println(id + ": Goodbye World");
        }
    }
}
