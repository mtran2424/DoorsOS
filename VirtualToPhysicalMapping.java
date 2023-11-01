/*
 * Author: My Tran
 * Filename: VirtualToPhysicalMapping.java
 * Description: Provides data structure mapping physical page in memory to disk page.
 */
public class VirtualToPhysicalMapping {
    // Members represent mapping between physical and virtual page
    public int physicalPage;
    public int diskPage;

    /*
     * Defualt constructor sets pages both to -1 indicating unmapped
     */
    public VirtualToPhysicalMapping() {
        physicalPage = -1;
        diskPage = -1;   
    }
}
