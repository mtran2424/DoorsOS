/*
 * Author: My Tran
 * Filename: RandomDevice.java
 * Description: Device maps devices used to generate random byte values
 */
import java.util.Random;

public class RandomDevice implements Device {

    // Max number of devices that can be mapped in device
    final int ARRAY_SIZE = 10;

    // Array of 10 randomizer items
    private Random internalDataArray[];

    /*
     * Default Constructor for random device
     */
    public RandomDevice() {
        // Assign ten array items
        internalDataArray = new Random[ARRAY_SIZE];
    }

    /*
     * Opens a new randomizer.
     * Returns id if success, -1 otherwise.
     */
    @Override
    public int Open(String s) {
        Random newRandom;
        // Used passed in string to create seed
        if (!s.isEmpty() && s != null) {
            try {
                // Uses string input as seed for random. String intended to be integer value of some sort
                newRandom = new Random(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                // indicate to user that open failed
                System.out.println("RandomDevice Open Failed: Invalid Input Error");
                return -1;
            }
        }
        else {// Use default seed otherwise
            newRandom = new Random();
        }

        // Find empty spot in the array
        int index = 0;
        while(index < ARRAY_SIZE && internalDataArray[index] != null) {
            index++;
        }

        // Check if there is an available slot to assign the device
        if(index >= ARRAY_SIZE) {
            // Error occurred, no spaces available
            System.out.println("RandomDevice Open Failed: Device Capacity Limited");
            return -1;
        }
        else {
            internalDataArray[index] = newRandom;
            // return device "id"
            return index;
        }
    }

    /*
     * Closes device with indicated id by nulling out entry
     */
    @Override
    public void Close(int id) {

        // nulls device to close if device exists
        internalDataArray[id] = null;
    }

    /*
     * Read will create/fil array with random values and return it to the user.
     */
    @Override
    public byte[] Read(int id, int size) {
        // Create byte array to return, read in random values, and return array
        byte randomValues[] = new byte[size];
        internalDataArray[id].nextBytes(randomValues);
        return randomValues;
    }

    /*
     * Set offset in Random
     */
    @Override
    public void Seek(int id, int to) {
        // Moves the randomizer along by reading without outputting
        Read(id, to);
    }

    /*
     * Return 0 length and do nothing (since it makes no sense)
     */
    @Override
    public int Write(int id, byte[] data) {
        return 0;
    }

    /*
     * Closes out all devices in internal data array
     */
    // public void Close() {
    //     for(int i = 0; i < internalDataArray.length; i++) {
    //        internalDataArray[i] = null;
    //     }
    // }
}
