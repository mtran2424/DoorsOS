/*
 * Author: My Tran
 * Filename: ProbabilityTestProcess2.java
 * Description: Process used to test probabilistic model with no real-time processes present.
 */
public class ProbabilityTestProcess2 extends UserlandProcess {
    /*
     * Run method required for runnable
     */
    @Override
    public void run() {
        /*
         * When there are no real-time processes in the real-time process queue,
         * interactive and background processes should run 75% and 25% of the time
         * respectively. Running the same test as Test 1 five times, counting up to
         * the 6th time the last of the initially interactive processes, 66 quantums at
         * that point are interactive (10 I_XX and 1 HelloWorld) and the rest are
         * background. Averaging the percentages, interactive and background process
         * occur on average 74.2% and 25.8% of the time. These statistics closely match
         * up to our probabilistic model.
         */
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
