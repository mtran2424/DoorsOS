/*
 * Author: My Tran
 * Filename: Kernel.java
 * Description: Models the functionality of a kernel for an OS on our virtual OS
 */

import java.util.Random;

public class Kernel implements Device{
    final static int MEMORY_PAGES = 1024;  // Number of pages in physical memory
    final static int PAGE_SIZE = 1024;     // Size of a page

    // Scheduler of processes in the kernel
    private Scheduler scheduler;
    private VirtualFileSystem virtualFileSytem;
    private boolean[] freeList;

    /*
     * Default constructor for Kernel to create
     */
    public Kernel() {
        // instantiate scheduler to ensure single instance
        scheduler = new Scheduler(this);
        virtualFileSytem = new VirtualFileSystem();

        // Boolean arrays are initialized to false - reflects blocks of memory uninitialized on start
        freeList = new boolean[MEMORY_PAGES];
    }

    /*
     * Creates process through scheduler
     */
    public int CreateProcess(UserlandProcess up, Priority tier) {
        return scheduler.CreateProcess(up, tier);
    }

    /*
     * Creates process through scheduler
     */
    public int CreateProcess(UserlandProcess up) {
        return scheduler.CreateProcess(up);
    }

    /*
     * Calls the schedulers sleep method
     */
    public void Sleep(int milliseconds) {
        scheduler.Sleep(milliseconds);
    }

    /*
     * Opens a new device and maps it to a process
     */
    @Override
    public int Open(String s) {
        // Get an empty device slot from process to map device to
        int emptyEntry = scheduler.GetCurrentlyRunning().GetEmptyDeviceEntry();
        
        // GetEmptyDeviceEntry returns -1 if there are no more empty slots
        if(emptyEntry == -1) {
            // return -1 to indicate error occurred
            System.out.println("Kernel Open Failed: Device Capacity Limited");
            return emptyEntry;
        }
        else {
            // Open a device and store the id to map to process
            int id = virtualFileSytem.Open(s);

            if(id == -1) {
                // return -1 to indicate error in opening device
                return id;
            }
            else {
                // put id from VFS into kerneland process array at empty slot
                scheduler.GetCurrentlyRunning().AddDevice(id, emptyEntry);

                // Return the index in which the index in the process array
                return emptyEntry;
            }
        }
    }

    /*
     * Closes process device by invoking VFS's Close function given index of device 
     * in process array
     */
    @Override
    public void Close(int id) {
        // Convert id from userland to VFS id
        int virtualFileSytemID = scheduler.GetCurrentlyRunning().GetDeviceId(id);

        if(virtualFileSytemID == -1) {
            System.out.println("Kernel Close Failed: Device Does Not Exist");
        }
        else {
            // Use VFS device's close given userland input
            virtualFileSytem.Close(virtualFileSytemID);

            // Set id in IdArray in process to -1 to indicate closed
            scheduler.GetCurrentlyRunning().ResetDevice(id);
        }
        
    }

    /*
     * Closes process device by invoking VFS's Close function given index of device 
     * in process array
     */
    @Override
    public byte[] Read(int id, int size) {
        // Convert id from userland to VFS id
        int virtualFileSytemID = scheduler.GetCurrentlyRunning().GetDeviceId(id);

        if(virtualFileSytemID == -1) {
            System.out.println("Kernel Read Failed: Device Does Not Exist");
            return null;
        }
        // Use VFS device's read given userland input
        return virtualFileSytem.Read(virtualFileSytemID, size);
    }

    /*
     * Run process device's Seek by invoking VFS's Seek function given index of device 
     * in process array
     */
    @Override
    public void Seek(int id, int to) {
        // Convert id from userland to VFS id
        int virtualFileSytemID = scheduler.GetCurrentlyRunning().GetDeviceId(id);
        
        if(virtualFileSytemID == -1) {
            System.out.println("Kernel Seek Failed: Device Does Not Exist");
        }
        else {
            // Use VFS device's Seek given userland input
            virtualFileSytem.Seek(virtualFileSytemID, to);
        }
    }

    /*
     * Run process device's Write by invoking VFS's Seek function given index of device 
     * in process array
     */
    @Override
    public int Write(int id, byte[] data) {
        // Convert id from userland to VFS id
        int virtualFileSytemID = scheduler.GetCurrentlyRunning().GetDeviceId(id);
        
        // Use VFS device's Write given userland input
        return virtualFileSytem.Write(virtualFileSytemID, data);
    }

    /*
     * Returns current process' pid
     */
    public int GetPid() {
        return scheduler.GetPid();
    }
    /*
     * Returns pid of process with provided name
     */
    public int GetPidByName(String name) {
        return scheduler.GetPidByName(name);
    }

    /*
     * Sends kernel messages between processes
     */
    public void SendMessage(KernelMessage km) {
        // make a new message to be sent to host for km
        KernelMessage message = new KernelMessage(km);

        // sets the sender's pid so processes can't lie about their identity
        message.SetSender(GetPid());

        // Must invoke schedulers send message in order to dequeue waitng processes
        scheduler.SendMessage(message);
    }

    /*
     * Return message from other process to current process.
     * Puts process into waiting state until there is a message to return if none.
     */
    public KernelMessage WaitForMessage() {
        // Check first if there are messages already in the current procs queue to
        // know if we need to wait
        if(scheduler.GetCurrentlyRunning().HasMessage()) {
            // No need to wait if there are messages already
            return scheduler.GetCurrentlyRunning().GetNextMessage();
        }
        else {
            // This wait will stop the process and it will resum when ran again
            scheduler.WaitForMessage();
            
            // SendMessage will wake this process up to resume here when there is
            // a message to return
            return scheduler.GetCurrentlyRunning().GetNextMessage();
        }
    }

    /*
     * Finds contiguous virtual memory in process, and maps it to physical memory.
     * Returns start virtual address if success. -1 otherwise
     */
    public int AllocateMemory(int size) {
        // Requested amount of memory to allocate
        int neededPages = size / PAGE_SIZE;
        // Find next available page in process
        int startVirtualPage = scheduler.GetCurrentlyRunning().FindNPages(neededPages);

        // Indicate error if memory cannot be allocated - no holes large enough for allocated memory
        if(startVirtualPage == -1) {
            return -1;
        }
        
        // Array of free physical pages to map to virtual
        int[] physicalPageArray = new int[neededPages];
        
        int physicalPage = 0;
        int foundPages = 0;
        // Find physical blocks to map to virtual
        while((physicalPage < MEMORY_PAGES) && (foundPages < neededPages)) {
            // If entry in free list is false, page is free
            if(!freeList[physicalPage]) {
                // Mark page for mapping if one is found
                physicalPageArray[foundPages] = physicalPage;
                foundPages++;
            }

            physicalPage++;
        }

        // Number of contiguous pages have already been identified at this point
        for(int i = 0; i < foundPages; i++) {
            // Map physical page to virtual
            scheduler.GetCurrentlyRunning().MapPage(startVirtualPage + i, physicalPageArray[i]);

            // Indicate that physical page is in use in free list
            freeList[physicalPageArray[i]] = true;
        }

        // If there aren't enough physical pages, assign mappings with no physical pages
        for(int i = foundPages; i < neededPages; i++) {
            scheduler.GetCurrentlyRunning().MapPage(startVirtualPage + i);
        }

        // Method returns the first virtual memory address of newly allocated memory in process
        return startVirtualPage * PAGE_SIZE;
    }

    /*
     * Takes virtual address and amount to free
     */
    public boolean FreeMemory(int pointer, int size) {
        // Calculate number of pages to free
        int freePages = size / PAGE_SIZE;

        int virtualPage = pointer / PAGE_SIZE;

        // If the amount to free exceeds the program memory limit, memory cannot be freed - paging fault
        if(virtualPage + freePages > KernelandProcess.MAX_PAGE_AMOUNT) {
            return false;
        }

        // Start from pointer and unmap consecutive pages equivalent to size
        for(int i = 0; i < freePages; i++) {
            // Check for mapping of physical page
            if(scheduler.GetCurrentlyRunning().GetPageMapping(virtualPage + i) != null) {    
                // If there is a physical mapping, it needs to be freed in the free list
                if(scheduler.GetCurrentlyRunning().GetPhyicalPage(virtualPage + i) != -1) {
                    // Mark the physical page as free again
                    freeList[scheduler.GetCurrentlyRunning().GetPhyicalPage(virtualPage + i)] = false;

                    // Physical memory must be cleared
                    for(int j = scheduler.GetCurrentlyRunning().GetPhyicalPage(virtualPage + i) * UserlandProcess.PAGE_SIZE;
                    j < (scheduler.GetCurrentlyRunning().GetPhyicalPage(virtualPage + i) * UserlandProcess.PAGE_SIZE) + UserlandProcess.PAGE_SIZE;
                    j++) {
                        UserlandProcess.memory[j] = 0;
                    }
                }
            }
            // Unmap the page in virtual memory - Sets mapping to null through mutator
            scheduler.GetCurrentlyRunning().UnmapPage(virtualPage + i);
        }

        // indicate successfully freed memory
        return true;
    }

    /*
     * Gets physical page number from mapping in process memory, given virtual page number
     */
    public int GetPhyicalMapping(int virtualPageNumber) {
        return scheduler.GetCurrentlyRunning().GetPhyicalPage(virtualPageNumber);
    }

    /*
     * Gets mapping object for virtual page
     */
    public VirtualToPhysicalMapping GetPageMapping(int virtualPageNumber) {
        return scheduler.GetCurrentlyRunning().GetPageMapping(virtualPageNumber);
    }

    /*
     * Get randomly selected process using scheduler's GetRandomProcess()
     */
    public KernelandProcess GetRandomProcess(Random randomNumberGenerator) {
        return scheduler.GetRandomProcess(randomNumberGenerator);
    }

    /*
     * Getter for the FFS object from VFS
     */
    public FakeFileSystem GetFakeFileSystem() {
        return virtualFileSytem.GetFakeFileSystem();
    }
}
