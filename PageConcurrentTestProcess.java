/*
 * Author: My Tran
 * Filename: PageConcurrentTestProcess.java
 * Description: Tests the implementation of paging on concurrent processes sharing physical memory.
 */
public class PageConcurrentTestProcess extends UserlandProcess{
    @Override
    public void run() {
        System.out.println("Test shows that physical memory is being mapped properly to virtual memory of processes:");
        
        // Process allocates and lingers
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                System.out.println("Process 1:");
                
                // Allocate 3K
                int basePointer1;
                if ((basePointer1 = OS.AllocateMemory(3072)) == -1) {
                    System.out.println("Allocate failed");
                }
                else {
                    // Allocate should've mapped from virtual page 0 in this process
                    System.out.println("Allocated memory pointer: " + basePointer1);
                    System.out.println("Should be 0 because new process memory.");
                    
                    // Write to update the TLB
                    Write(basePointer1, (byte) 2);
                    
                    // Printing TLB shows the physical page mapping
                    for(int i = 0; i < 2; i++) {
                        System.out.println("Virtual Page: " + i + ", Physical Page: " + i);
                        for(int j = 0; j < 2; j++) {
                            System.out.println(translationLookasideBuffer[i][j]);
                        }
                    }
                    System.out.println("TLB should show that page 0 maps to physical page 0 if process 1 ran first, 4 otherwise.");
                }

                // Have process linger
                while(true) {
                    try {
                        Thread.sleep(240);
                    } catch (Exception e) {}
                }
            }
            
        }, Priority.BACKGROUND);

        // Another process that lingers
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                System.out.println("Process 2:");

                // Allocate 4K worth
                int basePointer1;
                if ((basePointer1 = OS.AllocateMemory(4096)) == -1) {
                    System.out.println("Allocate failed");
                }
                else {
                    // Allocate should've mapped from virtual page 0 in this process
                    System.out.println("Allocated memory pointer: " + basePointer1);
                    System.out.println("Should be 0 because new process memory.");

                    // Write to update the TLB
                    Write(basePointer1, (byte) 2);
                    
                    // Printing TLB shows the physical page mapping
                    for(int i = 0; i < 2; i++) {
                        System.out.println("Virtual Page: " + i + ", Physical Page: " + i);
                        for(int j = 0; j < 2; j++) {
                            System.out.println(translationLookasideBuffer[i][j]);
                        }
                    }
                    System.out.println("TLB should show that page 0 maps to physical page 3 if process 1 ran first, 0 otherwise.");
                }

                // Make process linger
                while(true) {
                    try {
                        Thread.sleep(240);
                    } catch (Exception e) {}
                }
            }
            
        }, Priority.BACKGROUND);
        
    }

    
}
