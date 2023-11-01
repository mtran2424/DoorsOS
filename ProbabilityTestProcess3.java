/*
 * Author: My Tran
 * Filename: ProbabilityTestProcess3.java
 * Description: Process used to test probabilistic model with all priority processes except background present.
 */
public class ProbabilityTestProcess3 extends UserlandProcess {
    /*
     * Run method required for runnable
     */
    @Override
    public void run() {
        /*
         * When there are real-time processes and the interactive or background process
         * queues are empty, their run time is given to the real-time processes. So
         * conducting the same test above with no background should result with
         * real-time processes occurring an extra 10% more than before. The average
         * percentages from 5 test runs gives 70.2% and 29.8% to real-time and
         * interactive processes respectively. This verifies the probabilistic model is
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
        OS.CreateProcess(new TestProcess("I_00"), Priority.INTERACTIVE);
        OS.CreateProcess(new TestProcess("I_01"), Priority.INTERACTIVE);
        OS.CreateProcess(new TestProcess("I_02"), Priority.INTERACTIVE);
        OS.CreateProcess(new TestProcess("I_03"), Priority.INTERACTIVE);
        OS.CreateProcess(new TestProcess("I_04"), Priority.INTERACTIVE);
        OS.CreateProcess(new TestProcess("I_05"), Priority.INTERACTIVE);
        OS.CreateProcess(new TestProcess("I_06"), Priority.INTERACTIVE);
        OS.CreateProcess(new TestProcess("I_07"), Priority.INTERACTIVE);
        OS.CreateProcess(new TestProcess("I_08"), Priority.INTERACTIVE);
        OS.CreateProcess(new TestProcess("I_09"), Priority.INTERACTIVE);
    }
}
