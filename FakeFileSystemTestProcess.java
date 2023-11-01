/*
 * Author: My Tran
 * Filename: FakeFileSystemTestProcess.java
 * Description: Process to test the functionality on FakeFileSystem
 */
public class FakeFileSystemTestProcess extends UserlandProcess {
    @Override
    public void run () {
        // Create device that never ends to ensure OS keeps running
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

        // Run test process for FakeFileSystem
        OS.CreateProcess(new FakeFileSystemProcess());
    }
}
