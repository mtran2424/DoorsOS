
/*
 * Author: My Tran
 * Filename: SleepProcess.java
 * Description: Process that can be slept for a specified period of time.
 */
import java.time.Clock;
import java.util.Random;

public class SleepProcess extends UserlandProcess {
    // Member for time to sleep to pass in to run
    int sleepTime;

    /*
     * Constructor
     * Creates object ot run with given amount of time to sleep
     */
    public SleepProcess(int time) {
        sleepTime = time;
    }

    /*
     * Run method required for runnable
     */
    @Override
    public void run() {
        // Randomly generated id to distinguish between running processes
        int id = new Random(Clock.systemDefaultZone().millis()).nextInt(100);
        int count = 0;

        // infinite loop outputs same text
        while (count++ < 30) {
            // Sleep the thread for more digestible output
            try {
                Thread.sleep(240);
            } catch (Exception e) {
            }

            System.out.println(id + ": Sleep");

            // Sleep after one print
            if (count == 1) {
                OS.Sleep(sleepTime);
            }
        }
    }
}
