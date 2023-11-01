/*
 * Author: My Tran
 * Filename: FakeFileSystem.java
 * Description: Device maps devices used to files
 */
import java.io.RandomAccessFile;

public class FakeFileSystem implements Device {

    // Indicates max number of RandomAccessFiles
    final int MAX_ARRAY_SIZE = 10;

    // Maps files to ids in device
    private RandomAccessFile internalDataArray[];

    /*
     * Constructor creates instance of device
     */
    public FakeFileSystem() {
        internalDataArray = new RandomAccessFile[MAX_ARRAY_SIZE];
    }

    /*
     * Opens RandomAccessFile device given filename
     */
    @Override
    public int Open(String s){
        if(s.isEmpty() || s == null) {
            // Return -1 to indicate error and output error message because of invalid input
            System.out.println("Open Failed: Invalid File Name");
            return -1;
        }
        else {
            // Find empty spot in the array
            int emptySpot = 0;
            while(emptySpot < MAX_ARRAY_SIZE && internalDataArray[emptySpot] != null) {
                emptySpot++;
            }

            // Check if there is an available slot to assign the device
            if(emptySpot >= MAX_ARRAY_SIZE) {
                // Error occurred, no spaces available
                System.out.println("FakeFileSystem Open Failed: Device Capacity at Limit");
                return -1;
            }
            else {
                // Create RandomAccessFile in the array at the nearest empty slot
                try {
                    internalDataArray[emptySpot] = new RandomAccessFile(s, "rw");
                }
                catch (Exception e) {
                    
                    System.out.println("Open Failed: Invalid Device ID");
                }
                
                // return device "id"
                return emptySpot;
            }

        }
    }

    /*
     * Closes device in indicated slot
     */
    @Override
    public void Close(int id){
        // Close the RandomAccessFile
        try {
            internalDataArray[id].close();

            // null out entry to indicate closed
            internalDataArray[id] = null;
        }catch(Exception e) {
            
            System.out.println("Write Failed: Error has occurred");
        }        
    }

    /*
     * Read from RandomAccessFile device with index id in array
     */
    @Override
    public byte[] Read(int id, int size) {
        // Array to store bytes and return in
        byte dataArray[] = new byte[size];
        try {
            // Read bytes from the file attached to the RandomAccessFile
            internalDataArray[id].read(dataArray, 0, size);
            return dataArray;

        } catch (Exception e) {
            // Ouptut error message and return null to indicate error occurred in read
            System.out.println("FakeFileSystem Read Failed: Error occurred");
            return null;
        }

        
    }

    /*
     * Seek in RandomAccessFile device with index id in array
     */
    @Override
    public void Seek(int id, int to) {
        try {
            // Call RandomAccessFile's seek to perform device function
            internalDataArray[id].seek(to);
        } catch (Exception e) {
            // Output seek error message
            System.out.println("Seek Failed: Error Occurred");
        }
    }

    /*
     * Write to RandomAccessFile device with index id in array
     */
    @Override
    public int Write(int id, byte[] data) {
        try {
            // Call RandomAccessFile's write to perform device function
            internalDataArray[id].write(data, 0, data.length);

            // Seek returns the number of bytes written
            return data.length;
        } catch (Exception e) {
            // Error message and -1 indicates error occurred
            System.out.println("Write Failed: Invalid Device ID");
            return -1;
        }
    }

    // public long GetOffset(int id) {
    //     try {
    //         return internalDataArray[id].getFilePointer();
    //     } catch (Exception e) {
    //         // Error message and -1 indicates error occurred
    //         System.out.println("Write Failed: Invalid Device ID");
    //         return -1;
    //     }
    // }

    // public long GetFileSize(int id) {
    //     try {
    //         return internalDataArray[id].length();
    //     } catch (Exception e) {
    //         // Error message and -1 indicates error occurred
    //         System.out.println("Write Failed: Invalid Device ID");
    //         return -1;
    //     }
    // }

}
