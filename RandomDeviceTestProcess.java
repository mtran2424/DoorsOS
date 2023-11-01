/*
 * Author: My Tran
 * Filename: RandomDeviceTestProcess
 * Description: Process to test the functionality on RadnomDevices
 */
public class RandomDeviceTestProcess extends UserlandProcess{
    @Override
    public void run () {

        // Create a process that never ends so the OS always has processes to run
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(240);
                    } catch (Exception e) {}
                }
            }
            
        }, Priority.BACKGROUND);
        // Process runs the testing for RandomDevice
        OS.CreateProcess(new RandomDeviceProcess());
    }
}
