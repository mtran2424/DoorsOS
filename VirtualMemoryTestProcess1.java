/*
 * Author: My Tran
 * Filename: VirtualMemoryTestProcess.java
 * Description: Test functionality of virtual memory
 */
public class VirtualMemoryTestProcess1 extends UserlandProcess{
    @Override
    public void run() {
        // Test to make sure paging still works as expected
        OS.CreateProcess(new PagingProcess("Chicken tenders"));

        // Allocate all pages in physical memory
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
                byte[] inputBytes = "Gritz".getBytes();
                // Write to beginning of all pages
                for(int j = memory; j < 102400; j += 1024) {
                    for(int i = memory; i < inputBytes.length; i++) {
                        Write(i, inputBytes[i]);
                    }
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

                // Make process hang so memory isn't freed
                while(true) {
                    try {
                        Thread.sleep(240);
                    }
                    catch (Exception e) {}
                }
            }
        });

        
        // Idle process to prevent program stoppage
        while(true) {
            // Run paging test to see if memory references are the same        
            OS.CreateProcess(new UserlandProcess() {
                @Override
                public void run () {
                    String input = "Sausage Egg and Cheese on a Bagel";
                    int basePointer1;
                    // Allocate memory to write to - 4 pages worth
                    if ((basePointer1 = OS.AllocateMemory(1024)) == -1) {
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

                    while(true) {
                        try {
                            Thread.sleep(10);
                        }
                        catch (Exception e) {}
                    }
                }
            });
            try {
                Thread.sleep(10);
            }
            catch (Exception e) {}
        }
    }
}
