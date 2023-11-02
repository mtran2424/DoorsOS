/*
 * Author: My Tran
 * Filename: OS.java
 * Description: Represents the operating system acting as a bridge between 
 * userland and kerneland and has access to every publicly accessible
 * function between the two.
 */

import java.time.Clock;
import java.util.Random;

public class OS {
    private final static int MEMORY_BLOCK_SIZE = 1024;
    // Necessary to make static for random generation for get map. Instructions run too fast for value to change
    private static Random randomNumberGenerator = new Random(Clock.systemDefaultZone().millis());

    // One and only reference to the kernel
    private static Kernel kernel;

    // Descriptor for swapfile
    private static int swapFileDescriptor;

    private static int diskPage = 0;

    /*
     * Emulate OS start up instantiating the kernel and running the first
     * program via creating an initial process.
     */
    public static void StartUp(UserlandProcess init) {
        // One and only instance of kernel created
        kernel = new Kernel();
        CreateProcess(init);

        // Open a Swap file upon startup
        swapFileDescriptor = kernel.GetFakeFileSystem().Open("SwapFile.txt");
    }

    /*
     * Indirect access to kernel open for processes
     */
    public static int Open(String s) {
        // invoke kernel's open
        return kernel.Open(s);
    }

    /*
     * Indirect access to kernel read for processes
     */
    public static byte[] Read(int id, int size) {
        // invoke kernel read
        return kernel.Read(id, size);
    }

    /*
     * Indirect access to kernel write for processes
     */
    public static int Write(int id, byte[] data) {
        // invoke kernel write
        return kernel.Write(id, data);
    }

    /*
     * Indirect access to kernel Close for processes
     */
    public static void Close(int id) {
        // invoke kernel close
        kernel.Close(id);
    }

    /*
     * Indirect access to kernel Seek for processes
     */
    public static void Seek(int id, int to) {
        //invoke kernel seek
        kernel.Seek(id, to);
    }

    /*
     * Creates a process through the kernel with a given priority
     */
    public static int CreateProcess(UserlandProcess up, Priority tier) {
        return kernel.CreateProcess(up, tier);
    };

    /*
     * Creates a process through the kernel
     */
    public static int CreateProcess(UserlandProcess up) {
        return kernel.CreateProcess(up);
    };

    /*
     * Calls the kernels Sleep method
     */
    public static void Sleep(int milliseconds) {
        kernel.Sleep(milliseconds);
    }

    /*
     * Returns current process' pid
     */
    public static int GetPid() {
        return kernel.GetPid();
    }

    /*
     * Returns pid of process with provided name
     */
    public static int GetPidByName(String name) {
        return kernel.GetPidByName(name);
    }

    /*
     * Sends kernel messages between processes
     */
    public static void SendMessage(KernelMessage km) {
        kernel.SendMessage(km);
    }

    /*
     * Causes process to obtain or wait for message
     */
    public static KernelMessage WaitForMessage() {
        // invoke through caller to give ulp indirect access to kernel function
        return kernel.WaitForMessage();
    }

    /*
     * Place mapping of specified virtaul page into TLB
     */
    public static void GetMapping(int virtualPageNumber) {
        // Need to randomly generate number to determine where new mapping will go in TLB
        
        int randomNumber = randomNumberGenerator.nextInt(2);

        // Assign new mapping to TLB
        UserlandProcess.translationLookasideBuffer[randomNumber][0] = virtualPageNumber;
        
        // Kill if memory access is illegal
        if(kernel.GetPageMapping(virtualPageNumber) == null) {
            kernel.KillProcess();
        }
        
        // If there is no physical mapping to the virtual page
        if( kernel.GetPhyicalMapping(virtualPageNumber) == -1) {
            // Find a random process with a physical mapping to take page from
            KernelandProcess victimProcess = FindVictimProcess();

            // Get the first page with physical memory from victim
            int victimPage = 0;
            while(victimProcess.GetPhyicalPage(victimPage) == -1) {
                victimPage++;
            }

            // Victim physical page needs to be written to disk if not already written to disk
            if (victimProcess.GetPageMapping(victimPage).diskPage == -1) {
                // Calculate new disk page - put at end of swap file
                kernel.GetFakeFileSystem().Seek(swapFileDescriptor, diskPage * UserlandProcess.PAGE_SIZE);

                // Populate this array with page from physical memory from victims physical page
                byte[] pageData = new byte[UserlandProcess.PAGE_SIZE];

                // Loop over memory byte by byte copying physical page to array to be written to disk
                for(int i = victimProcess.GetPhyicalPage(victimPage) * UserlandProcess.PAGE_SIZE, j = 0; 
                i < (victimProcess.GetPhyicalPage(victimPage) * UserlandProcess.PAGE_SIZE) + UserlandProcess.PAGE_SIZE; 
                i++, j++) {
                    // Read bytes from memory
                    pageData[j] = UserlandProcess.memory[i];
                }

                // Write physical page bytes to disk
                kernel.GetFakeFileSystem().Seek(swapFileDescriptor, diskPage * UserlandProcess.PAGE_SIZE);
                // kernel.GetFakeFileSystem().Write(swapFileDescriptor, new byte[UserlandProcess.PAGE_SIZE]);
                kernel.GetFakeFileSystem().Write(swapFileDescriptor, pageData);

                // Set the disk mapping of the victim
                victimProcess.GetPageMapping(victimPage).diskPage = diskPage;
                diskPage++;
            }

            // Swap physical pages of current and victim process - unset victim physical page indicating no mapping
            kernel.GetPageMapping(virtualPageNumber).physicalPage = victimProcess.GetPageMapping(victimPage).physicalPage;
            victimProcess.GetPageMapping(victimPage).physicalPage = -1;

            // If there is a disk mapping for the current virtual page, read it back to memory
            if (kernel.GetPageMapping(virtualPageNumber).diskPage != -1) {
                // Find the stored disk page in the swap file
                kernel.GetFakeFileSystem().Seek(swapFileDescriptor, kernel.GetPageMapping(virtualPageNumber).diskPage * UserlandProcess.PAGE_SIZE);
                
                // Read the stored page from the swap file
                byte[] diskPageData = new byte[UserlandProcess.PAGE_SIZE];
                diskPageData = kernel.GetFakeFileSystem().Read(swapFileDescriptor, diskPageData.length);

                // Write the stored page to physical memory
                for(int i = kernel.GetPhyicalMapping(virtualPageNumber) * UserlandProcess.PAGE_SIZE, j = 0; 
                i < (kernel.GetPhyicalMapping(virtualPageNumber) * UserlandProcess.PAGE_SIZE) + UserlandProcess.PAGE_SIZE; 
                i++, j++) {
                    UserlandProcess.memory[i] = 0;
                    UserlandProcess.memory[i] = diskPageData[j];
                }
            }
            // If there is nothing to overwrite the bytes with, set it equal to 0
            else {
                // Wipe physical page before using physical page
                for(int i = kernel.GetPhyicalMapping(virtualPageNumber) * UserlandProcess.PAGE_SIZE; 
                i < (kernel.GetPhyicalMapping(virtualPageNumber) * UserlandProcess.PAGE_SIZE) + UserlandProcess.PAGE_SIZE; 
                i++) {
                    // Set physical bytes to 0
                    UserlandProcess.memory[i] = 0;
                }
            }
        }

        // Put mapping into TLB for caller process to reference
        UserlandProcess.translationLookasideBuffer[randomNumber][1] = kernel.GetPhyicalMapping(virtualPageNumber);
    }

    /*
     * Allocate memory amount equivalent to given size through the kernel.
     * Returns the start virtual address.
     */
    public static int AllocateMemory(int size) {
        // ensure that size is a multiple of 1024
        if(size % MEMORY_BLOCK_SIZE == 0) {
            // attemp to allocate memory through kernel
            return kernel.AllocateMemory(size);
        }
        // Indicate failure due to sizing proportion mismatch
        else {
            return -1;
        }
    }

    /*
     * Unmaps physical from virtual mapping in kernel.
     * Returns true if success. False otherwise.
     */
    public static boolean FreeMemory(int pointer, int size) {
        // ensure that size is a multiple of 1024
        if(size % MEMORY_BLOCK_SIZE == 0 && pointer % MEMORY_BLOCK_SIZE == 0) {
            // attemp to allocate memory through kernel
            return kernel.FreeMemory(pointer, size);
        }
        // Indicate failure due to sizing proportion mismatch
        else {
            return false;
        }
    }

    /*
     * Find a random process to take physical page from in page swap.
     * Helper for GetMapping.
     */
    private static KernelandProcess FindVictimProcess() {
        // Find a random process to be victim
        KernelandProcess victimProcess = kernel.GetRandomProcess(randomNumberGenerator);

        // Loop exits when a random process with a physical mapping is found
        while(!victimProcess.HasPhysicalMapping()) {
            // If the victim process has no physical mapping, find a different random process
            victimProcess = kernel.GetRandomProcess(randomNumberGenerator);
        }

        // Return victim candidate
        return victimProcess;
    }
}