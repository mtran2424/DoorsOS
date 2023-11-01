/*
 * Author: My Tran
 * Filename: UserlandProcess
 * Description: Abstract class representing a process in the userland area
 * where traditional user side programs are run.
 */

abstract class UserlandProcess implements Runnable {
    // Array of memory size : 1024*1024 = 1048576 Bytes = 1 MB
    public final static int MEMORY_SIZE = 1048576;
    // 1024 bytes in 1 KB
    public final static int PAGE_SIZE = 1024;
    public static byte[] memory = new byte[MEMORY_SIZE];

    // Stores virtual to physical memory mapping on the page level
    public static int[/*V_Pages*/][/*P_Pages*/] translationLookasideBuffer = {{-1, -1}, {-1, -1}};  // Initial TLB holds no mapping. 

    /*
     * Given virtual address, finds and returns value in physical memory
     */
    public byte Read(int address) {
        // Need page number in virtual to access physical mapping of given page
        int offset = address % PAGE_SIZE;   
        // need offset from basepoint memory of page to find it in physical
        int pageNumber = address / PAGE_SIZE;

        // Match one of the mappings with the requested pages
        if(translationLookasideBuffer[0][0] == pageNumber) {
            // adding the offset puts us at where we wanted to like in the specific spot in the virtual paged we need to access
            // physical page mapped to virtual page times be page size gets us the address of the page
            int physicalAddress = (translationLookasideBuffer[0][1] * PAGE_SIZE) + offset;
            // byte at the physical address is the equivalent to virtual address we wanted to access in physcial memory
            return memory[physicalAddress];
        }
        else if(translationLookasideBuffer[1][0] == pageNumber) {
            // adding the offset puts us at where we wanted to like in the specific spot in the virtual paged we need to access
            // physical page mapped to virtual page times be page size gets us the address of the page
            int physicalAddress = (translationLookasideBuffer[1][1] * PAGE_SIZE) + offset;
            // byte at the physical address is the equivalent to virtual address we wanted to access in physcial memory
            return memory[physicalAddress];
        }
        // Mapping not found in current TLB
        else {
            // Get new mapping and retry read
            OS.GetMapping(pageNumber);
            return Read(address);
        }
    }

    /*
     * Given virtual address, finds and overwrites value in physical memory
     */
    public void Write(int address, byte value) {
        // Need page number in virtual to access physical mapping of given page
        int offset = address % PAGE_SIZE;   
        // need offset from basepoint memory of page to find it in physical
        int pageNumber = address / PAGE_SIZE;

        // Match one of the mappings with the requested pages
        if(translationLookasideBuffer[0][0] == pageNumber) {
            // adding the offset puts us at where we wanted to like in the specific spot in the virtual paged we need to access
            // physical page mapped to virtual page times be page size gets us the address of the page
            int physicalAddress = (translationLookasideBuffer[0][1] * PAGE_SIZE) + offset;
            // byte at the physical address is the equivalent to virtual address we wanted to access in physcial memory
            memory[physicalAddress] = value;
        }
        else if(translationLookasideBuffer[1][0] == pageNumber) {
            // adding the offset puts us at where we wanted to like in the specific spot in the virtual paged we need to access
            // physical page mapped to virtual page times be page size gets us the address of the page
            int physicalAddress = (translationLookasideBuffer[1][1] * PAGE_SIZE) + offset;
            // byte at the physical address is the equivalent to virtual address we wanted to access in physcial memory
            memory[physicalAddress] = value;
        }
        // Mapping not found in current TLB
        else {
            // Get new mapping and retry read
            OS.GetMapping(pageNumber);
            Write(address, value);
        }
    }
}