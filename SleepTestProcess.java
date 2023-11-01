/*
 * Author: My Tran
 * Filename: SleepTestProcess.java
 * Description: Test process for sleep functionality.
 */
import java.util.Timer;
import java.util.TimerTask;

public class SleepTestProcess extends UserlandProcess {
    /*
     * Run method required for runnable
     */
    @Override
    public void run() {
        /*
         * During the test process, 6 processes that are set to sleep in groups of two,
         * soon after running. Three timers are created to indicate when each set of
         * processes should be waking up. The slept processes will not occur in print
         * rotation until they're alotted sleep times have been passed. Three clusters
         * of processes are slept for different amounts of time to make more obvious the
         * order in which they are awakened. It can be clearly seen while running that
         * processes are being put into the sleep queue and being removed from the sleep
         * queue since we can see the correct number of processes returning to the print
         * rotation that timer indicates that a certain amount of time has passed.
         */
        OS.CreateProcess(new SleepProcess(10000), Priority.REALTIME);
        OS.CreateProcess(new SleepProcess(10000), Priority.REALTIME);
        OS.CreateProcess(new SleepProcess(20000), Priority.REALTIME);
        OS.CreateProcess(new SleepProcess(20000), Priority.REALTIME);
        OS.CreateProcess(new SleepProcess(30000), Priority.REALTIME);
        OS.CreateProcess(new SleepProcess(30000), Priority.REALTIME);
        OS.CreateProcess(new TestProcess("B_00"), Priority.BACKGROUND);
        OS.CreateProcess(new TestProcess("B_01"), Priority.BACKGROUND);
        OS.CreateProcess(new TestProcess("B_02"), Priority.BACKGROUND);
        OS.CreateProcess(new TestProcess("B_03"), Priority.BACKGROUND);
        OS.CreateProcess(new TestProcess("B_04"), Priority.BACKGROUND);

        // Timer to indicate times where processes should be waking up
        Timer alarm = new Timer();

        // Wake up alert indicates time for the first group of slept processes to wake
        // up
        alarm.schedule(new TimerTask() {
            public void run() {
                System.out.println("10000ms elapsed - Time to wake up...");
            }
        }, 10000);

        // Wake up alert indicates time for the second group of slept processes to wake
        // up
        alarm.schedule(new TimerTask() {
            public void run() {
                System.out.println("20000ms elapsed - Time to wake up...");
            }
        }, 20000);

        // Wake up alert indicates time for the third group of slept processes to wake
        // up
        alarm.schedule(new TimerTask() {
            public void run() {
                System.out.println("30000ms elapsed - Time to wake up...");
            }
        }, 30000);

    }
}
