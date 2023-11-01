/*
 * Author: My Tran
 * Filename: RandomDeviceLimitTestProcess.java
 * Description: Test process to test the RandomDevice at the limit of its device capacity
 */
public class RandomDeviceLimitTestProcess extends UserlandProcess{
    @Override
    public void run() {
        // Testing that the process can not open more devices than it can map
        System.out.println("RandomDevice Device Limit Test: Two Processes are created that open devices and never end so devices never close");
        System.out.println("Should output -1 and an error since there are too many devices for RandomDevice to map.\n");

        // Process Creates 10 random devices in within process and then runs forever so the devices never close
        OS.CreateProcess(new UserlandProcess() {
            @Override
            @SuppressWarnings("unused")
            public void run() {
                // Open 10 RandomDevices within this process
                int rand0 = OS.Open("random 10000");
                int rand1 = OS.Open("random 1231232");
                int rand2 = OS.Open("random 124124");
                int rand3 = OS.Open("random 125123");
                int rand4 = OS.Open("random 125142312");
                int rand5 = OS.Open("random 10000");
                int rand6 = OS.Open("random 1231232");
                int rand7 = OS.Open("random 124124");
                int rand8 = OS.Open("random 125123");
                int rand9 = OS.Open("random 125142312");

                // Infinite loop to make sure the process never finishes and devices never close
                while(true) {
                    try {
                        Thread.sleep(240);
                    } catch (Exception e) {}
                }
            }
        });

        // Process to open overflow RandomDevice device
        OS.CreateProcess(new UserlandProcess() {
            @Override
            @SuppressWarnings("unused")
            public void run() {
                // Open 11th RandomDevice device
                int rand10 = OS.Open("random 124124");
                
                // Should be -1
                System.out.println(rand10);

                // Infinite loop to make sure the process never finishes and devices never close
                while(true) {
                    try {
                        Thread.sleep(240);
                    } catch (Exception e) {}
                }
            }
        });
    }
    
}
