/*
 * Author: My Tran
 * Filename: VirtualFileSystemDeviceLimitTestProcess.java
 * Description: Test process to test the VFS at the limit of its device capacity
 */
public class VirtualFileSystemDeviceLimitTestProcess extends UserlandProcess{
    @Override
    public void run() {
        // Testing that the process can not open more devices than it can map
        System.out.println("VFS Device Limit Test: Two Processes are created that open devices and never end so devices never close");
        System.out.println("Should output -1 and an error since there are too many devices for VFS to map.");

        // Process Creates 10 random devices within process and then runs forever so the devices never close
        OS.CreateProcess(new UserlandProcess() {
            @Override
            @SuppressWarnings("unused")
            public void run() {
                // Open 10 RandomDevices
                int rand0 = OS.Open("random 10000");
                int rand1 = OS.Open("random 1231232");
                int rand2 = OS.Open("random 124124");
                int rand3 = OS.Open("random 125123");
                int rand4 = OS.Open("random 125142312");
                int rand5 = OS.Open("random 10000");
                int rand6 = OS.Open("random 1231232");
                int rand7 = OS.Open("random 124124");
                int rand8 =OS.Open("random 125123");
                int rand9 = OS.Open("random 125142312");
                int rand10 = OS.Open("random 125142312");
                
                // Infinite loop to keep process running
                while(true) {
                    try {
                        Thread.sleep(240);
                    } catch (Exception e) {}
                }
            }
        });

         // Process Creates 11 FakeFileSystem devices in one process
        OS.CreateProcess(new UserlandProcess() {
            @Override
            @SuppressWarnings("unused")
            public void run() {
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
                int file10 = OS.Open("file example.txt");
                
                // Output where error will occur to indicate VFS checks for this issue
                System.out.println(file10);

                // Infinite loop to keep process running
                while(true) {
                    try {
                        Thread.sleep(240);
                    } catch (Exception e) {}
                }
            }
        });

        // With 20 devices already open, when another tries to open, the VFS will check for a slot and return -1 and output error
    }
    
}
