/*
 * Author: My Tran
 * Filename: VirtualFileSystem.java
 * Description: Device that maps calls to other devices and ids
 */
public class VirtualFileSystem implements Device{
    final int DEVICE_ARRAY_SIZE = 20;
    // Maps VFS id to a Device/id combination
    private Pair<Integer, Device>[] deviceArray;
    private RandomDevice randomDevice;
    private FakeFileSystem fakeFileSystem;

    /*
     * Constructor creates instance of VirtualFileSystem with array set to size 
     * DEVICE_ARRAY_SIZE defined in class definition
     */
    @SuppressWarnings("unchecked")
    public VirtualFileSystem() {
        deviceArray = new Pair[DEVICE_ARRAY_SIZE];
        randomDevice = new RandomDevice();
        fakeFileSystem = new FakeFileSystem();
    }

    /*
     * Opens up a new device
     */
    @Override
    public int Open(String s) {
        // Invalid string passed in so device cannot be created
        if(s.isEmpty() || s == null) {
            // -1 return indicates error in input for open
            System.out.println("VirtualFileSytem Open Failed: Invalid Input Error");
            return -1;
        }
        else {

            String deviceType = "", deviceInput = "";

            int i = 0;

            // Skip over whitespace
            while(s.charAt(i) == ' ') {
                i++;
            }
            // Accumulate first word
            while(s.charAt(i) != ' ') {
                deviceType = deviceType + s.charAt(i);
                i++;
            }

            // Skip over whitespace
            while(s.charAt(i) == ' ') {
                i++;
            }

            //Get the rest of the input
            while(i < s.length()) {
                deviceInput = deviceInput + s.charAt(i);
                i++;
            }
            

            // Find empty spot in the array
            int index = 0;
            while(index < DEVICE_ARRAY_SIZE && deviceArray[index] != null) {
                index++;
            }

            // Check if there is an available slot to assign the device
            if(index >= DEVICE_ARRAY_SIZE) {
                // Error occurred, no spaces available
                System.out.println("VirtualFileSystem Open Failed: Device Capacity Limited");
                return -1;
            }
            else {
                // deviceID and newDevice must exist and are TBD
                int deviceID = -1;
                Pair<Integer, Device> newDevice = null;

                // Need to determine the type of device to create
                switch(deviceType) {
                    case "random":
                        // Get the id from device's Open function to store with device later
                        deviceID = randomDevice.Open(deviceInput);

                        // Indicate failure if Open failed
                        if(deviceID == -1) {
                            return -1;
                        }
                        // Create a new array entry from input above so that we can store it
                        newDevice = new Pair<>(deviceID, randomDevice);
                        deviceArray[index] = newDevice;
                        break;
                    case "file":
                        // Get the id from device's Open function to store with device later
                        deviceID = fakeFileSystem.Open(deviceInput);

                        // Indicate failure if Open failed
                        if(deviceID == -1) {
                            return -1;
                        }
                        // Create a new array entry from input above so that we can store it
                        newDevice = new Pair<>(deviceID, fakeFileSystem);
                        deviceArray[index] = newDevice;
                        break;
                    default:
                        //Indicate error if device doesn't exist
                        System.out.println("VirtualFileSystem Open Failed: Device Type Does Not Exist");
                        return -1;
                }
                // Return the id for the current class
                return index;
            }

            
        }
    }

    /*
     * Invokes given device's Close function
     */
    @Override
    public void Close(int id) {
        try {
            // Check to make sure that the device to close actually exists
            if(deviceArray[id] == null) {
                System.out.println("VirtualFileSystem Close Faled: Device Does Not Exist");
            }
            else {
                // Close out the device in within device stored before nulling out in VFS to ensure the slot is freed
                deviceArray[id].getValue().Close(deviceArray[id].getKey());
                deviceArray[id] = null;
            }
        }
        catch (Exception e) {
            // Any potential occurred errors should be indicated to user in close
            System.out.println("VirtualFileSystem Close Failed: Error Occurred");
        }
    }

    /*
     * Invokes given device's Read function
     */
    @Override
    public byte[] Read(int id, int size) {
        // Reading from out of bounds index is an error. Prevent user from reading from device that cannot exist
        if(id < 0 || id > DEVICE_ARRAY_SIZE)
        {
            System.out.println("VirtualFileSystem Read Failed: Invalid Input Error");
            return null;
        }

        // Prevent user from reading from device that does not exist
        if(deviceArray == null) {
            System.out.println("VirtualFileSystem Read Failed: Device Does Not Exist");
            return null;
        }

        // Get the read value from device stored in VFS
        return deviceArray[id].getValue().Read(deviceArray[id].getKey(), size);
    }

    /*
     * Invokes given device's Seek function
     */
    @Override
    public void Seek(int id, int to) {
        // Seeking in out of bounds index is an error. Prevent user from reading from device that cannot exist
        if(id < 0 || id > DEVICE_ARRAY_SIZE)
        {
            System.out.println("VirtualFileSystem Seek Failed: Invalid Input Error");
        }
        // Prevent user from seeking in device that does not exist
        else if(deviceArray[id] == null) {
            System.out.println("VirtualFileSystem Seek Faled: Device Does Not Exist");
        }
        // Prevent user from reading from device that does not exist
        else {
            deviceArray[id].getValue().Seek(deviceArray[id].getKey(), to);
        }
    }

    /*
     * Invokes given device's Write function 
     */
    @Override
    public int Write(int id, byte[] data) {
        // Write return is device dependent
        return deviceArray[id].getValue().Write(deviceArray[id].getKey(), data);
    }

    public FakeFileSystem GetFakeFileSystem() {
        return fakeFileSystem;
    }
}
