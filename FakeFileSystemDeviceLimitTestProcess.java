/*
 * Author: My Tran
 * Filename: FakeFileSystemDeviceLimitTestProcess.java
 * Description: Test process to test the FFS at the limit of its device capacity
 */
public class FakeFileSystemDeviceLimitTestProcess extends UserlandProcess{
    @Override
    public void run() {
        // Testing that the process can not open more devices than it can map
        System.out.println("FFS Device Limit Test: Two Processes are created that Open devices and never end so devices never close");
        System.out.println("Should output an error since there are too many devices for FFS to map.\n");

        // Process Creates 10 FFS devices in one process
        OS.CreateProcess(new UserlandProcess() {
            @Override
            @SuppressWarnings("unused")
            public void run() {
                // Open 10 devices for this process
                int file0 = OS.Open("file example.txt");
                int file1 = OS.Open("file example.txt");
                int file2 = OS.Open("file example.txt");
                int file3 = OS.Open("file example.txt");
                int file4 = OS.Open("file example.txt");
                int file5 = OS.Open("file example.txt");
                int file6 = OS.Open("file example.txt");
                int file7 = OS.Open("file example.txt");
                int file8 = OS.Open("file example.txt");
                int file9 = OS.Open("file example.txt");

                // Infinite loop to make sure the process never finishes and devices never close
                while(true) {
                    try {
                        Thread.sleep(240);
                    } catch (Exception e) {}
                }
            }
        });

        // Process to create overflow FFS device
        OS.CreateProcess(new UserlandProcess() {
            @Override
            @SuppressWarnings("unused")
            public void run() {
                // This should put FFS capacity over the top
                int file10 = OS.Open("file example.txt");

                // Should be -1
                System.out.println(file10);

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
