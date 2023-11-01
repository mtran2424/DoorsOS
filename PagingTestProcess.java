/*
 * Author: My Tran
 * Filename: PagingTestProcess.java
 * Description: Tests the implementation of paging in the OS
 */
public class PagingTestProcess extends UserlandProcess{
    @Override
    public void run() {
        // Run many processes to test processes working together in memory
        OS.CreateProcess(new PagingProcess("apples"));
        OS.CreateProcess(new PagingProcess("oranges"));
        OS.CreateProcess(new PagingProcess("mangos"));
        OS.CreateProcess(new PagingProcess("peaches"));
        OS.CreateProcess(new PagingProcess("pineapples"));

        // Infinite processes to run in background
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
    }
}
