/*
 * Author: My Tran
 * Filename: PagingProcess.java
 * Description: Process runs logical testing on paging implementation
 */
public class PagingProcess extends UserlandProcess{
    // Member to store user input
    private String input;

    public PagingProcess(String in) {
        input = in;
    }
    @Override
    public void run() {
        System.out.println(input + " Process:\n");

        // Opening up a task, the TLB should be cleared out by the scheduler
        System.out.println("TLB Cleared upon SwitchProcess test 1: Should be all -1s");
        for(int i = 0; i < 2; i++) {
            System.out.println("Virtual Page: " + i + ", Physical Page: " + i);
            for(int j = 0; j < 2; j++) {
                System.out.println(translationLookasideBuffer[i][j]);
            }
        }

        System.out.println();
        
        System.out.println("Test Allocate Memory 1: Allocate, write, and read to ensure correct memory access");
        
        // pointer to the memory we just allocated
        int basePointer1;
        // Allocate memory to write to - 4 pages worth
        if ((basePointer1 = OS.AllocateMemory(4096)) == -1) {
            System.out.println("Allocate failed");
        }
        // Allocate was a success
        else {
            // Output the pointer returned from Allocate
            System.out.println("Start virtual address of memory allocated: " + basePointer1);
            
            System.out.println("Input to memory: " + input);
            // Get input from user input and write bytes to memory
            byte[] inputBytes = input.getBytes();
            for(int i = basePointer1; i < inputBytes.length; i++) {
                Write(i, inputBytes[i]);
            }
            
            // Read memory bytes at that virtual memory
            byte[] outputBytes = new byte[inputBytes.length];
            for(int i = basePointer1; i < inputBytes.length; i++) {
                outputBytes[i] = Read(i);
            }

            String output = new String(outputBytes);
            
            // Output the read string to show match
            System.out.println("Output from memory: " + output);
            
            System.out.println((output.equals(input)) ? "PASS" : "FAIL");
            
        }
        System.out.println();

        // Print out TLB at the end the show that it has received some mappings
        System.out.println("TLB Getting Mappings Test 2:");
        for(int i = 0; i < 2; i++) {
            System.out.println("Virtual Page: " + i + ", Physical Page: " + i);
            for(int j = 0; j < 2; j++) {
                System.out.println(translationLookasideBuffer[i][j]);
            }
        }
        System.out.println("Should have map of 0th page");

        System.out.println();

        System.out.println("Test Allocate Memory 2: Allocate more memory, writing and reading to non-zero pointer");
        int basePointer2;

        // Allocate memory new to write to - Another 4 pages
        if ((basePointer2 = OS.AllocateMemory(4096)) == -1) {
            System.out.println("Allocate failed");
        }
        // Allocate was a success
        else {
            // Allocated memory should have different pointer from first
            System.out.println("Start virtual address of memory allocated: " + basePointer2);
            
            // Write input String into memory in reverse to distinguish memory from beginning
            byte[] inputBytes = input.getBytes();
            for(int i = basePointer2, j = inputBytes.length - 1; i < basePointer2 + inputBytes.length; i++, j--) {
                Write(i, inputBytes[j]);
            }
            
            // Read sequential bytes in memory into output
            byte[] outputBytes = new byte[inputBytes.length];
            for(int i = basePointer2, j = 0; i < basePointer2 + inputBytes.length; i++, j++) {  
                outputBytes[j] = Read(i);
            }
            
            // Calculate reverse input string to match with read from memory output
            byte[] temp = new byte[inputBytes.length];
            for(int i = 0, j = inputBytes.length - 1; i < inputBytes.length; i++, j--) {
                temp[i] = inputBytes[j];
            }
            
            // Form comparison strings
            input = new String(temp);
            String output = new String(outputBytes);

            // Output the written and read string to show match
            System.out.println("Input to memory: " + input);
            System.out.println("Output from memory: " + output);

            System.out.println((output.equals(input)) ? "PASS" : "FAIL");
        }

        System.out.println();

        // Print out TLB at the end the show that it has received some mappings
        System.out.println("TLB Getting Mappings Test 3:");
        for(int i = 0; i < 2; i++) {
            System.out.println("Virtual Page: " + i + ", Physical Page: " + i);
            for(int j = 0; j < 2; j++) {
                System.out.println(translationLookasideBuffer[i][j]);
            }
        }
        System.out.println("Should have map page at index 4.\n");

        System.out.println("Free memory test:");
        // Free 2K from 0 making a hole from the beginning of virtual memory to 2 pages
        OS.FreeMemory(0, 2048);

        // pointer to the memory we just allocated
        int basePointer3;
        // Allocate memory to write to - 1 page meaning a 1K hole from in page 1
        if ((basePointer3 = OS.AllocateMemory(1024)) == -1) {
            System.out.println("Allocate failed");
        }
        // Allocate was a success
        else {
            // Output the pointer returned from Allocate
            System.out.println("Start virtual address of memory allocated: " + basePointer3);
            System.out.println("Should be 0 virtual address");
            
            input = "Free Test 1";
            System.out.println("Input to memory: " + input);
            // Get input from user input and write bytes to memory
            byte[] inputBytes = input.getBytes();
            for(int i = basePointer3; i < inputBytes.length; i++) {
                Write(i, inputBytes[i]);
            }
            
            // Read memory bytes at that virtual memory
            byte[] outputBytes = new byte[inputBytes.length];
            for(int i = basePointer3; i < inputBytes.length; i++) {
                outputBytes[i] = Read(i);
            }

            String output = new String(outputBytes);
            
            // Output the read string to show match
            System.out.println("Output from memory: " + output);
            
            System.out.println((output.equals(input)) ? "PASS" : "FAIL");
            
        }
        System.out.println();
        
        // Print out TLB at the end the show that it has received some mappings
        System.out.println("TLB Getting Mappings Test 4:");
        for(int i = 0; i < 2; i++) {
            System.out.println("Virtual Page: " + i + ", Physical Page: " + i);
            for(int j = 0; j < 2; j++) {
                System.out.println(translationLookasideBuffer[i][j]);
            }
        }
        System.out.println("Should have map of page at index 0 because memory was freed.\n");

        // pointer to the memory we just allocated
        int basePointer4;
        // Allocate memory to write to - 2 pages worth
        if ((basePointer4 = OS.AllocateMemory(2048)) == -1) {
            System.out.println("Allocate failed");
        }
        // Allocate was a success
        else {
            // Output the pointer returned from Allocate
            System.out.println("Start virtual address of memory allocated: " + basePointer4);
            System.out.println("2K won't fit in 1K hole so next virtual address should be 8192");
            
            input = "Free Test 2";
            System.out.println("Input to memory: " + input);
            // Get input from user input and write bytes to memory
            byte[] inputBytes = input.getBytes();
            for(int i = basePointer4; i < inputBytes.length + basePointer4; i++) {
                Write(i, inputBytes[i - basePointer4]);
            }
            
            // Read memory bytes at that virtual memory
            byte[] outputBytes = new byte[inputBytes.length];
            for(int i = basePointer4; i < inputBytes.length + basePointer4; i++) {
                outputBytes[i - basePointer4] = Read(i);
            }

            String output = new String(outputBytes);
            
            // Output the read string to show match
            System.out.println("Output from memory: " + output);
            
            System.out.println((output.equals(input)) ? "PASS" : "FAIL");
            
        }
        System.out.println();

        // Print out TLB at the end the show that it has received some mappings
        System.out.println("TLB Getting Mappings Test 5:");
        for(int i = 0; i < 2; i++) {
            System.out.println("Virtual Page: " + i + ", Physical Page: " + i);
            for(int j = 0; j < 2; j++) {
                System.out.println(translationLookasideBuffer[i][j]);
            }
        }
        System.out.println("Should have map of page at index 8 because thats where the next 2K block is.\n");

        // pointer to the memory we just allocated
        int basePointer5;
        // Allocate memory to write to - 1 page worth
        if ((basePointer5 = OS.AllocateMemory(1024)) == -1) {
            System.out.println("Allocate failed");
        }
        // Allocate was a success
        else {
            // Output the pointer returned from Allocate
            System.out.println("Start virtual address of memory allocated: " + basePointer5);
            System.out.println("1K should fit in hole so next virtual address should be 1024");
            
            input = "Free Test 3";
            System.out.println("Input to memory: " + input);
            // Get input from user input and write bytes to memory
            byte[] inputBytes = input.getBytes();
            for(int i = basePointer5; i < inputBytes.length + basePointer5; i++) {
                Write(i, inputBytes[i - basePointer5]);
            }
            
            // Read memory bytes at that virtual memory
            byte[] outputBytes = new byte[inputBytes.length];
            for(int i = basePointer5; i < inputBytes.length + basePointer5; i++) {
                outputBytes[i - basePointer5] = Read(i);
            }

            String output = new String(outputBytes);
            
            // Output the read string to show match
            System.out.println("Output from memory: " + output);
            
            System.out.println((output.equals(input)) ? "PASS" : "FAIL");
            
        }
        System.out.println();

        // Print out TLB at the end the show that it has received some mappings
        System.out.println("TLB Getting Mappings Test 6:");
        for(int i = 0; i < 2; i++) {
            System.out.println("Virtual Page: " + i + ", Physical Page: " + i);
            for(int j = 0; j < 2; j++) {
                System.out.println(translationLookasideBuffer[i][j]);
            }
        }
        System.out.println("Should have map of page at index 1 because thats where 1K hole is.\n");
    }
    
}
