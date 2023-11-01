/*
 * Author: My Tran
 * Filename: RandomDeviceProcess.java
 * Description: Process to test the functionality of RandomDevice
 */
public class RandomDeviceProcess extends UserlandProcess{

    @Override
    public void run () {
        // Testing Open
        //Opening 10 devices and printing the returned id values shows that the index returns are functioning
        int rand0 = OS.Open("random 10000");
        int rand1 = OS.Open("random 1231232");
        int rand2 = OS.Open("random 124124");
        int rand3 = OS.Open("random 125123");
        int rand4 = OS.Open("random 125142312");
        int rand5 = OS.Open("random 1251243123");
        int rand6 = OS.Open("random 125124312");
        int rand7 = OS.Open("random 1254123");
        int rand8 = OS.Open("random 16254");
        int rand9 = OS.Open("random 123");
        
        System.out.println("RandomDevice Open Test 1: Checking Open ids");
        System.out.println(rand0 == 0 ? "PASS" : "FAIL");
        System.out.println(rand1 == 0 ? "PASS" : "FAIL");
        System.out.println(rand2 == 0 ? "PASS" : "FAIL");
        System.out.println(rand3 == 0 ? "PASS" : "FAIL");
        System.out.println(rand4 == 0 ? "PASS" : "FAIL");
        System.out.println(rand5 == 0 ? "PASS" : "FAIL");
        System.out.println(rand6 == 0 ? "PASS" : "FAIL");
        System.out.println(rand7 == 0 ? "PASS" : "FAIL");
        System.out.println(rand8 == 0 ? "PASS" : "FAIL");
        System.out.println(rand9 == 0 ? "PASS" : "FAIL");
        System.out.println("Test compares the ids of the devices opened in the order in which they're created with 0-9 to indicate the VFS is assigning open slots properly.\n");

        // Testing Read
        // Reading twenty values and printing the byte output demonstrates that Read properly gets bytes as indicated by userland
        System.out.println("RandomDevice Read Test 1: ");
        byte [] arr0 = OS.Read(rand0, 20);
        byte [] arr1 = OS.Read(rand1, 20);
        byte [] arr2 = OS.Read(rand2, 20);
        byte [] arr3 = OS.Read(rand3, 20);
        byte [] arr4 = OS.Read(rand4, 20);
        byte [] arr5 = OS.Read(rand5, 20);
        byte [] arr6 = OS.Read(rand6, 20);
        byte [] arr7 = OS.Read(rand7, 20);
        byte [] arr8 = OS.Read(rand8, 20);
        byte [] arr9 = OS.Read(rand9, 20);
        System.out.println("Should output 20 bytes returned from Read for all 10 devices opened. They all have different seeds so should contain all different bytes.\n");

        // Each corresponding byte in each array is different showing that the devices being accessed are all different.
        // Different seeds per device returns different values per array, shows different devices per Read.
        for(int i = 0; i < arr0.length; i++) {
            System.out.println("Random Byte: " + i + "\n");
            System.out.println("Random_Device_0: " + arr0[i]);
            System.out.println("Random_Device_1: " + arr1[i]);
            System.out.println("Random_Device_2: " + arr2[i]);
            System.out.println("Random_Device_3: " + arr3[i]);
            System.out.println("Random_Device_4: " + arr4[i]);
            System.out.println("Random_Device_5: " + arr5[i]);
            System.out.println("Random_Device_6: " + arr6[i]);
            System.out.println("Random_Device_7: " + arr7[i]);
            System.out.println("Random_Device_8: " + arr8[i]);
            System.out.println("Random_Device_9: " + arr9[i]);
            System.out.println();
        }

        // Closing device rand three and then trying to read from it will throw an error because there is no device to read from.
        System.out.println("RandomDevice Read Test 2: Reading from a Device that doesn't exist");
        OS.Close(rand3);

        // Read will cause an error message showing the device error is handled
        arr3 = OS.Read(rand3, 10);
        System.out.println("Should output error message on Read because device was closed before reading.\n");

        // Reopen device for later use
        rand3 = OS.Open("random 213123124");

        // Testing Close
        // Closing devices will free up their corresponding slots
        System.out.println("RandomDevice Close Test 1: Closing a device and checking if same slot was freed");
        OS.Close(rand0);
        OS.Close(rand7);

        // 0 and 7 should be free so opening rand7 first should return id 0 and give the other id 7
        rand7 = OS.Open("random 124321");
        rand0 = OS.Open("random 1010");

        System.out.println("rand7_id: " + rand7);
        System.out.println("rand0_id: " + rand0);
        System.out.println("rand7 id should now be 0 and rand0 id should now be 7 since their slots were freed and rand7 was re-Opened before rand0\n");
        
        // Confirming that device has been closed by closing a device that has been closed. Error message
        // lets us know that there is no device where device was closed.
        System.out.println("RandomDevice Close Test 2: Try to close random device after rand device has already been closed");
        OS.Close(rand9);
        OS.Close(rand9);
        System.out.println("Should print out error message indicating Close call failed because rand9 was already closed.\n");

        // Testing Seek
        System.out.println("RandonDevice Seek Test 1: Testing seek functionality");
        OS.Close(rand2);
        OS.Close(rand3);

        // Opening 2 Randoms that have the same seed
        rand2 = OS.Open("random 100");
        rand3 = OS.Open("random 100");

        // Seeking in rand3 device and outputting the read values
        OS.Seek(rand3, 10);

        arr2 = OS.Read(rand2, 22);
        arr3 = OS.Read(rand3, 10);

        // Print contents of arrays to compare
        for(int i = 0; i < arr2.length; i++) {
            System.out.println( i + " Random_Device_2: " + arr2[i]);
        }
        System.out.println();
        for(int i = 0; i < arr3.length; i++) {
            System.out.println( i + " Random_Device_3: " + arr3[i]);
        }

        // Proves that seek works because arr3 appearing in 
        System.out.println("The last 10 values of the array from Random_Device_2 should be the 10 values in Random_Device_3 since they started on the same seed except seek was called on one.\n");

        // Closing device rand three and then trying to seek it will throw an error because there is no device to seek in.
        System.out.println("RandomDevice Seek Test 2: Reading from a Device that doesn't exist");
        OS.Close(rand3);

        // Read will cause an error message showing the device error is handled
        OS.Seek(rand3, 10);
        System.out.println("Should output error message on Seek because device was closed before seeking.\n");
    }

}
