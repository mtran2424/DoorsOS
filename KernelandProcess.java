/*
 * Author: My Tran
 * Filename: KernelandProcess.java
 * Description: Manages process from kernel's perspective.
 */

import java.util.LinkedList;

public class KernelandProcess {
    // Max number of devices per process
    final static int MAX_ARRAY_SIZE = 10;   
    // Max number of virtual memory pages
    final static int MAX_PAGE_AMOUNT = 100; 
    private static int nextpid = 1; // holds next generated process id. Initial is 1
    private int pid; // holds process id
    private boolean started; // indicator for if the thread as been started yet
    private Thread processThread; // thread that will allow us to run or code
    private Priority priority; // priority tier of the process object instance
    private int timeouts;   // counter member for process timeouts
    private int idArray[];  // Stores all the VFS ids for opened process devices
    private String name;    // Name of the process of the ULP this KLP was created from
    private LinkedList<KernelMessage> messageQueue; // Queue of messages from other processes
    // Table of virtual to physical page mappings for process
    private VirtualToPhysicalMapping[] pageTable;    // index: virtual page and value: physical page

    /*
     * Constructor
     * Initializes default process. Interactive on default.
     */
    public KernelandProcess(UserlandProcess up) {
        // Initialize idArray to all -1 to indicate all devices are empty
        idArray = new int[MAX_ARRAY_SIZE];
        for(int i = 0; i < MAX_ARRAY_SIZE; i++){idArray[i] = -1;}

        // Initialize page table to have no mappings
        pageTable = new VirtualToPhysicalMapping[MAX_PAGE_AMOUNT];
        // for(int i = 0; i < MAX_PAGE_AMOUNT; i++){pageTable[i] = new VirtualToPhysicalMapping();}

        pid = nextpid++; // set pid for process from static member incremented every time one is created
        processThread = new Thread(up); // create instance of thread
        priority = Priority.INTERACTIVE; // priority is set as default to interactive
        timeouts = 0; // Initialize timeout counter to 0

        name = up.getClass().getSimpleName(); // Name of the ULP stored in KLP
        messageQueue = new LinkedList<>();  // Instantiate the message queue
    }

    /*
     * Constructor
     * Creates process with specified priority
     */
    public KernelandProcess(UserlandProcess up, Priority tier) {
        // Initialize idArray to all -1 to indicate all devices are empty
        idArray = new int[MAX_ARRAY_SIZE];
        for(int i = 0; i < MAX_ARRAY_SIZE; i++){idArray[i] = -1;}

        // Initialize page table to have no mappings
        pageTable = new VirtualToPhysicalMapping[MAX_PAGE_AMOUNT];
        // for(int i = 0; i < MAX_PAGE_AMOUNT; i++){pageTable[i] = new VirtualToPhysicalMapping();}

        pid = nextpid++; // set pid for process from static member incremented every time one is created
        processThread = new Thread(up); // create instance of thread
        priority = tier; // priority is set as default to interactive
        timeouts = 0; // Initialize timeout counter to 0

        name = up.getClass().getSimpleName(); // Name of the ULP stored in KLP
        messageQueue = new LinkedList<>();  // Instantiate the message queue
    }

    /*
     * Stops the process
     */
    @SuppressWarnings("deprecation")
    public void stop() {
        // If the thread has started, suspend it
        if (started) 
        {
            processThread.suspend();
        }
    }

    /*
     * Getter for if the process is finished running
     */
    public boolean isDone() {
        // If the process has been started, but the thread is not alive, then the
        // process is finsished
        return (started && !processThread.isAlive());
    }

    /*
     * Accessor indicating if a process has started.
     */
    public boolean isStarted() {
        return started;
    }

    /*
     * Runs the current process
     */
    @SuppressWarnings("deprecation")
    public void run() {

        // If the process has already been started, resume it
        if (started) 
        {
            processThread.resume();
        } 
        else 
        {
            // if it has not been started, before, the flag must be set and the thread must
            // start
            started = true;
            processThread.start();
        }
    }

    /*
     * Getter for the current process' id
     */
    public int getpid() {
        return pid;
    }

    /*
     * Setter for the process priority level
     */
    public void SetPriority(Priority tier) {
        priority = tier;
    }

    /*
     * Return the priority of process
     */
    public Priority GetPriority() {
        return priority;
    }

    /*
     * Demote the process priority by one tier
     */
    public void Demote() {
        switch(priority) {
            case REALTIME:
                // Real-time demotes to interactive.
                priority = Priority.INTERACTIVE;
                break;
            case INTERACTIVE:
                // Interactive demotes to background.
                priority = Priority.BACKGROUND;
                break;
            default:
                // Background doesn't demote any lower.
                break;
        }

        // Reset the timeout count after demotion in case of another demotion.
        timeouts = 0;
    }

    /*
     * Getter for timeout count
     */
    public int GetTimeout(){
        return timeouts;
    }

    /*
     * Modifier for timeout count. Increments by 1 when called.
     */
    public void AddTimeout() {
        timeouts++;
    }

    /*
     * Modifier for timeout count. Sets it back to 0.
     */
    public void resetTimeout() {
        timeouts = 0;
    }

    /*
     * Gets an empty index in idArray and retuns it to caller.
     * Returns index of empty slot or -1 if none are available.
     */
    public int GetEmptyDeviceEntry() {
        // Find empty spot in the array
        int index = 0;
        while(index < MAX_ARRAY_SIZE && idArray[index] != -1) {
            index++;
        }

        // Check if there is an available slot to assign the device
        if(index >= MAX_ARRAY_SIZE) {
            // Error occurred, no spaces available
            return -1;
        }
        else {
            // Return calculated index to caller
            return index;
        }
    }

    /*
     * Adds opened VFS device id to indicated index in idArray.
     */
    public void AddDevice(int id, int index) {
        idArray[index] = id;
    }

    /*
     * Returns the VFS device id at given process device id.
     */
    public int GetDeviceId(int id) {
        return idArray[id];
    }

    /*
     * Returns idArray mapping process devices' ids to their VFS device ids.
     */
    public void ResetDevice(int id) {
        idArray[id] = -1;
    }

    /*
     * Return name of the process
     */
    public String GetName() {
        return name;
    }

    /*
     * Inserts kernel message into kernel message queue
     */
    public void AddMessage(KernelMessage message) {
        messageQueue.add(message);
    }

    /*
     * Checks if the processes has messages
     */
    public boolean HasMessage() {
        return !messageQueue.isEmpty();
    }

    /*
     * Remove the first first message in the queue and return it if there is one.
     * Null otherwise
     */
    public KernelMessage GetNextMessage() {
        return messageQueue.isEmpty() ? null : messageQueue.removeFirst();
    }

    /*
     * Finds base page where inclusive range of pages are contiguously free.
     * Returns base page if found. -1 otherwise.
     */
    public int FindNPages(int pages) {
        // Base point page from which to start looking for contiguous free pages
        int basePage = 0;
        boolean found = false;

        // Exits if continuously free pages are found or pages cannot be found
        while(!found && (basePage < MAX_ARRAY_SIZE)) {
            //Find next unused page
            while(basePage < MAX_ARRAY_SIZE && (pageTable[basePage] != null)) {
                basePage++;
            }

            // After loop, basePage should be index of unused page. Still needs to be within acceptable amount
            if(basePage + pages < MAX_ARRAY_SIZE) {
                // Loop over page range looking for initialized memory - Exit loop if found and move base page
                for(int i = 0; i < pages; i++) {
                    // Success case - loop has successfully completed and number of pages from base is contiguously free
                    if((i == pages - 1) && (pageTable[basePage + i] == null)) {
                        // meet exit condition on main loop
                        found = true;
                        break;
                    }

                    // Not large enough hole
                    if(pageTable[basePage + i] != null) {
                        // Update base point to the allocated page
                        basePage = basePage + i;
                        // Meet inner loop exit condition
                        break;
                    }
                }
            }
            // Number of pages of free contiguous virtual memory can not exist if there aren't enough virtual pages left
            else {
                // meet the loop condition to exit
                // basePage = basePage + pages;
                break;
            }
        }

        // If all virtual pages have been checked, basePage will not be in range - indicate failure
        if(basePage > MAX_ARRAY_SIZE) {
            return -1;
        }

        // Only reaches this point when if connecutive free pages are sucessfully found.
        return basePage;
    }

    /*
     * Map physical page to virtual page
     */
    public void MapPage(int virtualPage, int physicalPage) {
        // Create new mapping
        pageTable[virtualPage] = new VirtualToPhysicalMapping();

        // Map physical to virtual page
        pageTable[virtualPage].physicalPage = physicalPage;
    }

    /*
     * Overloaded map page that allocates virtual to physical mapping with no physical page
     */
    public void MapPage(int virtualPage) {
        // Create new mapping
        pageTable[virtualPage] = new VirtualToPhysicalMapping();
    }

    /*
     * Unmap page entry from virtual page
     */
    public void UnmapPage(int virtualPage) {
        // Null out entry to remove reference and let garbage collection deal with it
        pageTable[virtualPage] = null;
    }

    /*
     * Return physical page mapping for virtual page
     */
    public int GetPhyicalPage(int virtualPage) {
        // Get and return physical page stored from virtual page table
        return pageTable[virtualPage].physicalPage;
    }

    /*
     * Return page mapping, either virtual or physical
     */
    public VirtualToPhysicalMapping GetPageMapping(int virtualPage) {
        // Return mapping at given virtual page for process
        return pageTable[virtualPage];
    }

    /*
     * Checks process for a physical mapping
     */
    public boolean HasPhysicalMapping() {
        // Start from beginning
        int mappings = 0;
        // Iterate through the page table
        while(mappings < pageTable.length) {
            // A virtual page mapping with a physical mapping has been found if there is a page with a non -1 physical mapping
            if(pageTable[mappings] != null) {
                if(pageTable[mappings].physicalPage != -1) {
                    // Return true
                    return true;
                }
            }
        }
        // If the loop exits, no mapping was found
        return false;
    }
}
