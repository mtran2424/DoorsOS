/*
 * Author: My Tran
 * Filename: VirtualMemoryTestProcess2.java
 * Description: Test for reloading virtual memory from disk
*/
public class VirtualMemoryTestProcess2 extends UserlandProcess{
    @Override
    public void run() {
        System.out.println("Reload From Disk Test:");
        String input = "Sausage Egg and Cheese on a Bagel";
        int basePointer1;
        // Allocate memory to write to - 100 pages worth
        if ((basePointer1 = OS.AllocateMemory(102400)) == -1) {
            System.out.println("Allocate failed");
        }
        // Allocate was a success
        else {
            // Output the pointer returned from Allocate
            System.out.println("Start virtual address of memory allocated: " + basePointer1);
            
            System.out.println("Input to memory: " + input);
            // Get input from user input and write bytes to memory
            byte[] inputBytes = input.getBytes();
            for(int j = basePointer1; j < 102400; j+=1024){
                for(int i = j; i < inputBytes.length; i++) {
                    Write(i, inputBytes[i]);
                }
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

        // Allocate all pages in physical memory, write to the pages, sleep the process for at least 1 cycle to get chance of loading memory back from disk
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                // Allocate a page
                int memory = OS.AllocateMemory(102400);

                // Write to multiple pages to make page swaps more obvious
                byte[] inputBytes = "Pancakes".getBytes();
                // Write to beginning of all pages
                for(int j = memory; j < 102400; j += 1024) {
                    for(int i = memory; i < inputBytes.length; i++) {
                        Write(i, inputBytes[i]);
                    }
                }

                // Put Process at end of queue
                //OS.Sleep(100);

                // Read start of all pages and check to ensure matches
                for(int j = basePointer1; j < 102400; j+=1024) {
                    byte[] outputBytes = new byte[inputBytes.length];
                    for(int i = basePointer1; i < inputBytes.length; i++) {
                        outputBytes[i] = Read(i);
                    }
            
                    String output = new String(outputBytes);
                    
                    // Output the read string to show match
                    System.out.println("Output from memory: " + output);
                    String input = new String(inputBytes);
                    System.out.println((output.equals(input)) ? "PASS" : "FAIL");
                }
                
                // Make process hang so memory isn't freed
                while(true) {
                    try {
                        Thread.sleep(240);
                    }
                    catch (Exception e) {}
                }
            }
        });
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                // Allocate a page
                int memory = OS.AllocateMemory(102400);

                // Write to memory
                byte[] inputBytes = "Waffles".getBytes();
                // Write to beginning of all pages
                for(int j = memory; j < 102400; j += 1024) {
                    for(int i = memory; i < inputBytes.length; i++) {
                        Write(i, inputBytes[i]);
                    }
                }

                // Put Process at end of queue
                OS.Sleep(100);

                // Read start of all pages and check to ensure matches
                for(int j = basePointer1; j < 102400; j+=1024) {
                    byte[] outputBytes = new byte[inputBytes.length];
                    for(int i = basePointer1; i < inputBytes.length; i++) {
                        outputBytes[i] = Read(i);
                    }
            
                    String output = new String(outputBytes);
                    
                    // Output the read string to show match
                    System.out.println("Output from memory: " + output);
                    String input = new String(inputBytes);
                    System.out.println((output.equals(input)) ? "PASS" : "FAIL");
                }

                // Make process hang so memory isn't freed
                while(true) {
                    try {
                        Thread.sleep(240);
                    }
                    catch (Exception e) {}
                }
            }
        });
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                // Allocate a page
                int memory = OS.AllocateMemory(102400);

                // Write to memory
                byte[] inputBytes = "Fries".getBytes();
                // Write to beginning of all pages
                for(int j = memory; j < 102400; j += 1024) {
                    for(int i = memory; i < inputBytes.length; i++) {
                        Write(i, inputBytes[i]);
                    }
                }

                // Put Process at end of queue
                OS.Sleep(100);

                // Read start of all pages and check to ensure matches
                for(int j = basePointer1; j < 102400; j+=1024) {
                    byte[] outputBytes = new byte[inputBytes.length];
                    for(int i = basePointer1; i < inputBytes.length; i++) {
                        outputBytes[i] = Read(i);
                    }
            
                    String output = new String(outputBytes);
                    
                    // Output the read string to show match
                    System.out.println("Output from memory: " + output);
                    String input = new String(inputBytes);
                    System.out.println((output.equals(input)) ? "PASS" : "FAIL");
                }

                // Make process hang so memory isn't freed
                while(true) {
                    try {
                        Thread.sleep(240);
                    }
                    catch (Exception e) {}
                }
            }
        });
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                // Allocate a page
                int memory = OS.AllocateMemory(102400);

                // Write to memory
                byte[] inputBytes = "Fries".getBytes();
                // Write to beginning of all pages
                for(int j = memory; j < 102400; j += 1024) {
                    for(int i = memory; i < inputBytes.length; i++) {
                        Write(i, inputBytes[i]);
                    }
                }

                // Put Process at end of queue
                OS.Sleep(100);

                // Read start of all pages and check to ensure matches
                for(int j = basePointer1; j < 102400; j+=1024) {
                    byte[] outputBytes = new byte[inputBytes.length];
                    for(int i = basePointer1; i < inputBytes.length; i++) {
                        outputBytes[i] = Read(i);
                    }
            
                    String output = new String(outputBytes);
                    
                    // Output the read string to show match
                    System.out.println("Output from memory: " + output);
                    String input = new String(inputBytes);
                    System.out.println((output.equals(input)) ? "PASS" : "FAIL");
                }

                // Make process hang so memory isn't freed
                while(true) {
                    try {
                        Thread.sleep(240);
                    }
                    catch (Exception e) {}
                }
            }
        });
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                // Allocate a page
                int memory = OS.AllocateMemory(102400);

                // Write to memory
                byte[] inputBytes = "Potato salad".getBytes();
                // Write to beginning of all pages
                for(int j = memory; j < 102400; j += 1024) {
                    for(int i = memory; i < inputBytes.length; i++) {
                        Write(i, inputBytes[i]);
                    }
                }

                // Put Process at end of queue
                OS.Sleep(100);

                // Read start of all pages and check to ensure matches
                for(int j = basePointer1; j < 102400; j+=1024) {
                    byte[] outputBytes = new byte[inputBytes.length];
                    for(int i = basePointer1; i < inputBytes.length; i++) {
                        outputBytes[i] = Read(i);
                    }
            
                    String output = new String(outputBytes);
                    
                    // Output the read string to show match
                    System.out.println("Output from memory: " + output);
                    String input = new String(inputBytes);
                    System.out.println((output.equals(input)) ? "PASS" : "FAIL");
                }

                // Make process hang so memory isn't freed
                while(true) {
                    try {
                        Thread.sleep(100);
                    }
                    catch (Exception e) {}
                }
            }
        });
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                // Allocate a page
                int memory = OS.AllocateMemory(102400);

                // Write to memory
                byte[] inputBytes = "Gritz".getBytes();
                // Write to beginning of all pages
                for(int j = memory; j < 102400; j += 1024) {
                    for(int i = memory; i < inputBytes.length; i++) {
                        Write(i, inputBytes[i]);
                    }
                }

                // Put Process at end of queue
                OS.Sleep(100);

                // Read start of all pages and check to ensure matches
                for(int j = basePointer1; j < 102400; j+=1024) {
                    byte[] outputBytes = new byte[inputBytes.length];
                    for(int i = basePointer1; i < inputBytes.length; i++) {
                        outputBytes[i] = Read(i);
                    }
            
                    String output = new String(outputBytes);
                    
                    // Output the read string to show match
                    System.out.println("Output from memory: " + output);
                    String input = new String(inputBytes);
                    System.out.println((output.equals(input)) ? "PASS" : "FAIL");
                }

                // Make process hang so memory isn't freed
                while(true) {
                    try {
                        Thread.sleep(240);
                    }
                    catch (Exception e) {}
                }
            }
        });
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                // Allocate a page
                int memory = OS.AllocateMemory(102400);

                // Write to memory
                byte[] inputBytes = "Fruit cup".getBytes();
                // Write to beginning of all pages
                for(int j = memory; j < 102400; j += 1024) {
                    for(int i = memory; i < inputBytes.length; i++) {
                        Write(i, inputBytes[i]);
                    }
                }

                // Put Process at end of queue
                OS.Sleep(100);

                // Read start of all pages and check to ensure matches
                for(int j = basePointer1; j < 102400; j+=1024) {
                    byte[] outputBytes = new byte[inputBytes.length];
                    for(int i = basePointer1; i < inputBytes.length; i++) {
                        outputBytes[i] = Read(i);
                    }
            
                    String output = new String(outputBytes);
                    
                    // Output the read string to show match
                    System.out.println("Output from memory: " + output);
                    String input = new String(inputBytes);
                    System.out.println((output.equals(input)) ? "PASS" : "FAIL");
                }

                // Make process hang so memory isn't freed
                while(true) {
                    try {
                        Thread.sleep(240);
                    }
                    catch (Exception e) {}
                }
            }
        });
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                // Allocate a page
                int memory = OS.AllocateMemory(102400);

                // Write to memory
                byte[] inputBytes = "Soda".getBytes();
                // Write to beginning of all pages
                for(int j = memory; j < 102400; j += 1024) {
                    for(int i = memory; i < inputBytes.length; i++) {
                        Write(i, inputBytes[i]);
                    }
                }

                // Put Process at end of queue
                OS.Sleep(100);

                // Read start of all pages and check to ensure matches
                for(int j = basePointer1; j < 102400; j+=1024) {
                    byte[] outputBytes = new byte[inputBytes.length];
                    for(int i = basePointer1; i < inputBytes.length; i++) {
                        outputBytes[i] = Read(i);
                    }
            
                    String output = new String(outputBytes);
                    
                    // Output the read string to show match
                    System.out.println("Output from memory: " + output);
                    String input = new String(inputBytes);
                    System.out.println((output.equals(input)) ? "PASS" : "FAIL");
                }

                // Make process hang so memory isn't freed
                while(true) {
                    try {
                        Thread.sleep(240);
                    }
                    catch (Exception e) {}
                }
            }
        });
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                // Allocate a page
                int memory = OS.AllocateMemory(102400);

                // Write to memory
                byte[] inputBytes = "Coleslaw".getBytes();
                // Write to beginning of all pages
                for(int j = memory; j < 102400; j += 1024) {
                    for(int i = memory; i < inputBytes.length; i++) {
                        Write(i, inputBytes[i]);
                    }
                }

                // Put Process at end of queue
                OS.Sleep(100);

                // Read start of all pages and check to ensure matches
                for(int j = basePointer1; j < 102400; j+=1024) {
                    byte[] outputBytes = new byte[inputBytes.length];
                    for(int i = basePointer1; i < inputBytes.length; i++) {
                        outputBytes[i] = Read(i);
                    }
            
                    String output = new String(outputBytes);
                    
                    // Output the read string to show match
                    System.out.println("Output from memory: " + output);
                    String input = new String(inputBytes);
                    System.out.println((output.equals(input)) ? "PASS" : "FAIL");
                }

                // Make process hang so memory isn't freed
                while(true) {
                    try {
                        Thread.sleep(240);
                    }
                    catch (Exception e) {}
                }
            }
        });
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                // Allocate a page
                int memory = OS.AllocateMemory(102400);

                // Write to memory
                byte[] inputBytes = "Cookie".getBytes();
                // Write to beginning of all pages
                for(int j = memory; j < 102400; j += 1024) {
                    for(int i = memory; i < inputBytes.length; i++) {
                        Write(i, inputBytes[i]);
                    }
                }

                // Put Process at end of queue
                OS.Sleep(100);

                // Read start of all pages and check to ensure matches
                for(int j = basePointer1; j < 102400; j+=1024) {
                    byte[] outputBytes = new byte[inputBytes.length];
                    for(int i = basePointer1; i < inputBytes.length; i++) {
                        outputBytes[i] = Read(i);
                    }
            
                    String output = new String(outputBytes);
                    
                    // Output the read string to show match
                    System.out.println("Output from memory: " + output);
                    String input = new String(inputBytes);
                    System.out.println((output.equals(input)) ? "PASS" : "FAIL");
                }

                // Make process hang so memory isn't freed
                while(true) {
                    try {
                        Thread.sleep(240);
                    }
                    catch (Exception e) {}
                }
            }
        });
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run() {
                // Allocate a page
                int memory = OS.AllocateMemory(102400);

                // Write to memory
                byte[] inputBytes = "More Chicken Tenders".getBytes();
                // Write to beginning of all pages
                for(int j = memory; j < 102400; j += 1024) {
                    for(int i = memory; i < inputBytes.length; i++) {
                        Write(i, inputBytes[i]);
                    }
                }

                // Put Process at end of queue
                OS.Sleep(100);

                // Read start of all pages and check to ensure matches
                for(int j = basePointer1; j < 102400; j+=1024) {
                    byte[] outputBytes = new byte[inputBytes.length];
                    for(int i = basePointer1; i < inputBytes.length; i++) {
                        outputBytes[i] = Read(i);
                    }
            
                    String output = new String(outputBytes);
                    
                    // Output the read string to show match
                    System.out.println("Output from memory: " + output);
                    String input = new String(inputBytes);
                    System.out.println((output.equals(input)) ? "PASS" : "FAIL");
                }
                // Make process hang so memory isn't freed
                while(true) {
                    try {
                        Thread.sleep(240);
                    }
                    catch (Exception e) {}
                }
            }
        });

        // Sleep process, putting current process at the end of the queue so that reloading page can be tested
        OS.Sleep(2500);

        System.out.println("Read all pages for matches...");
        byte[] inputBytes = input.getBytes();
        // Read memory bytes at that virtual memory
        for(int j = basePointer1; j < 102400; j+=1024) {
            byte[] outputBytes = new byte[inputBytes.length];
            for(int i = basePointer1; i < inputBytes.length; i++) {
                outputBytes[i] = Read(i);
            }
    
            String output = new String(outputBytes);
            
            // Output the read string to show match
            System.out.println("Output from memory: " + output);
            
            System.out.println((output.equals(input)) ? "PASS" : "FAIL");
        }

        System.out.println("If all pass, at some point, a physical page was written away. If all reads matched the initial writes, then reload had to have worked properly.");
    }
    
}
