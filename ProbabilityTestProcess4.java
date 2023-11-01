/*
 * Author: My Tran
 * Filename: ProbabilityTestProcess4.java
 * Description: Process used to test probabilistic model with all priority processes except interactive present.
 */
public class ProbabilityTestProcess4 extends UserlandProcess {
    /*
     * Run method required for runnable
     */
    @Override
    public void run() {
        /*
         * When there are real-time processes and the interactive or background process
         * queues are empty, their run time is given to the real-time processes. So
         * conducting the same test above with no interactive should result with
         * real-time processes occurring an extra 30% more than before. The average
         * percentages from 5 test runs gives 89.9% and 10.1% to real-time and
         * background processes respectively. This verifies the probabilistic model is
         * behaving as expected.
         */
        OS.CreateProcess(new TestProcess("Rt_00"), Priority.REALTIME);
        OS.CreateProcess(new TestProcess("Rt_01"), Priority.REALTIME);
        OS.CreateProcess(new TestProcess("Rt_02"), Priority.REALTIME);
        OS.CreateProcess(new TestProcess("Rt_03"), Priority.REALTIME);
        OS.CreateProcess(new TestProcess("Rt_04"), Priority.REALTIME);
        OS.CreateProcess(new TestProcess("Rt_05"), Priority.REALTIME);
        OS.CreateProcess(new TestProcess("Rt_06"), Priority.REALTIME);
        OS.CreateProcess(new TestProcess("Rt_07"), Priority.REALTIME);
        OS.CreateProcess(new TestProcess("Rt_08"), Priority.REALTIME);
        OS.CreateProcess(new TestProcess("Rt_09"), Priority.REALTIME);
        OS.CreateProcess(new TestProcess("B_00"), Priority.BACKGROUND);
        OS.CreateProcess(new TestProcess("B_01"), Priority.BACKGROUND);
        OS.CreateProcess(new TestProcess("B_02"), Priority.BACKGROUND);
        OS.CreateProcess(new TestProcess("B_03"), Priority.BACKGROUND);
        OS.CreateProcess(new TestProcess("B_04"), Priority.BACKGROUND);
        OS.CreateProcess(new TestProcess("B_05"), Priority.BACKGROUND);
        OS.CreateProcess(new TestProcess("B_06"), Priority.BACKGROUND);
        OS.CreateProcess(new TestProcess("B_07"), Priority.BACKGROUND);
        OS.CreateProcess(new TestProcess("B_08"), Priority.BACKGROUND);
        OS.CreateProcess(new TestProcess("B_09"), Priority.BACKGROUND);
    }
}
