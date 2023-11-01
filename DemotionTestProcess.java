/*
 * Author: My Tran
 * Filename: DemotionTestProcess.java
 * Description: Test process for the functionality of demotion.
 */
public class DemotionTestProcess extends UserlandProcess{
    /*
     * Run method required for runnable
     */
    @Override
    public void run(){
        /*
         * Now looking past the 6th occurence of the last inital real-time quantum, and
         * switching focus to the pattern of the print rotation, we can determine
         * whether demotion is behaving is it should. Past the 6th occurence of the last
         * intial real-time process, we can visible see the change in frequency of Rt_xx
         * quantums. This follows as at this point all Rt processes should be demoted to
         * interactive. This 3:1 ratio of Rt_xx to B_xx processes is indicative of the
         * probabilistic model implemented and tested earlier. After another 6
         * occurences of the last initial Rt process, the print rotation pattern
         * consistently repeats in line with the background processes. So all the
         * background quantums will occur, followed by all the Rt processes. This is
         * indicative that they have all been demoted to background processes.
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
