/*
 * Author: My Tran
 * Filename: ProbabilityTestProcess1.java
 * Description: Process used to test probabilistic model with all priority processes present.
 */
public class ProbabilityTestProcess1 extends UserlandProcess {
    @Override
    public void run() {
        /*
         * When real-time processes are in the real-time process queue, the real-time,
         * interactive, and backgorund processes should run 60%, 30%, and 10% of the
         * time respectively. Knowing as much, a pattern and statistical analysis of the
         * running processes can be used to test the implemented model's functionality.
         * Creating 10 processes of each priority, we have enough processes to collect
         * an adequate amount of samples without worrying about demotion to a certain
         * point. Allowing the OS to run, we can numerate quantums by looking at the
         * switching of text output. I did 5 runs, allowing for each to run long enough
         * for the last in line real-time process to have occurred 6 times, as after the
         * 6th time, that process will increment to 6 and the next time it occurs, it
         * will be an interactive process due to demotion. Once the last initial
         * real-time quantum occurs the 6 times, 60 of the quantums at the point will be
         * real-time processes. The interactive and background quantums are then counted
         * and each are divided by the total to calculate the percent of time each
         * priority occupy. Averaging the percentages from the 5 runs, real-time,
         * interactive, and background processes averaged 59.1%, 30.8%, and 0.04%
         * respectively. These statistics closely match up to our probabilistic model.
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
