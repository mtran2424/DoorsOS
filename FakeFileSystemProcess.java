/*
 * Author: My Tran
 * Filename: FakeFileSystemProcess.java
 * Description: Process to test the functionality of FFS device
 */
public class FakeFileSystemProcess extends UserlandProcess {
    @Override
    public void run () {
    
        // Testing Open
        //Opening 10 devices and printing the returned id values shows that the index returns are functioning
        int file0 = OS.Open("file file0.txt");
        int file1 = OS.Open("file file1.txt");
        int file2 = OS.Open("file file2.txt");
        int file3 = OS.Open("file file3.txt");
        int file4 = OS.Open("file file4.txt");
        int file5 = OS.Open("file file5.txt");
        int file6 = OS.Open("file file6.txt");
        int file7 = OS.Open("file file7.txt");
        int file8 = OS.Open("file file8.txt");
        int file9 = OS.Open("file file9.txt");

        System.out.println("FakeFileSystem Open Test 1: Checking open ids");
        System.out.println(file0 == 0 ? "PASS" : "FAIL");
        System.out.println(file1 == 1 ? "PASS" : "FAIL");
        System.out.println(file2 == 2 ? "PASS" : "FAIL");
        System.out.println(file3 == 3 ? "PASS" : "FAIL");
        System.out.println(file4 == 4 ? "PASS" : "FAIL");
        System.out.println(file5 == 5 ? "PASS" : "FAIL");
        System.out.println(file6 == 6 ? "PASS" : "FAIL");
        System.out.println(file7 == 7 ? "PASS" : "FAIL");
        System.out.println(file8 == 8 ? "PASS" : "FAIL");
        System.out.println(file9 == 9 ? "PASS" : "FAIL");
        System.out.println("Comparing id created per file and the order in which they're assigned\n");

        int write0 = OS.Write(file0, "Older than the trees,".getBytes());
        int write1 = OS.Write(file1, "Younger than the mountains".getBytes());
        int write2 = OS.Write(file2, "Growin like a breeze".getBytes());
        int write3 = OS.Write(file3, "Take me home.".getBytes());
        int write4 = OS.Write(file4, "Country roads.".getBytes());
        int write5 = OS.Write(file5, "To the place,".getBytes());
        int write6 = OS.Write(file6, "Where I belong.".getBytes());
        int write7 = OS.Write(file7, "West Virgina".getBytes());
        int write8 = OS.Write(file8, "Mountain mama".getBytes());
        int write9 = OS.Write(file9, "Take me home, country roads".getBytes());

        // Testing Write
        System.out.println("FakeFileSystem Write Test 1: Checking that write worked properly");
        System.out.println(write0 == "Older than the trees,".getBytes().length ? "PASS" : "FAIL");
        System.out.println(write1 == "Younger than the mountains".getBytes().length ? "PASS" : "FAIL");
        System.out.println(write2 == "Growin like a breeze".getBytes().length ? "PASS" : "FAIL");
        System.out.println(write3 == "Take me home.".getBytes().length ? "PASS" : "FAIL");
        System.out.println(write4 == "Country roads.".getBytes().length ? "PASS" : "FAIL");
        System.out.println(write5 == "To the place,".getBytes().length ? "PASS" : "FAIL");
        System.out.println(write6 == "Where I belong.".getBytes().length ? "PASS" : "FAIL");
        System.out.println(write7 == "West Virgina".getBytes().length ? "PASS" : "FAIL");
        System.out.println(write8 == "Mountain mama".getBytes().length ? "PASS" : "FAIL");
        System.out.println(write9 == "Take me home, country roads".getBytes().length ? "PASS" : "FAIL");
        System.out.println("Test compares the return byte count from write with the length of the what we wrote to it.\n");

        // Testing Seek and Read
        System.out.println("FakeFileSystem Seek and Read Test: Seek will put us at index 0 so read will read from beginning of each file.");
        OS.Seek(file0, 0);
        OS.Seek(file1, 0);
        OS.Seek(file2, 0);
        OS.Seek(file3, 0);
        OS.Seek(file4, 0);
        OS.Seek(file5, 0);
        OS.Seek(file6, 0);
        OS.Seek(file7, 0);
        OS.Seek(file8, 0);
        OS.Seek(file9, 0);
        
        // Reading twenty values and printing the byte output demonstrates that Read properly gets bytes as indicated by userland
        byte [] arr0 = OS.Read(file0, write0);
        byte [] arr1 = OS.Read(file1, write1);
        byte [] arr2 = OS.Read(file2, write2);
        byte [] arr3 = OS.Read(file3, write3);
        byte [] arr4 = OS.Read(file4, write4);
        byte [] arr5 = OS.Read(file5, write5);
        byte [] arr6 = OS.Read(file6, write6);
        byte [] arr7 = OS.Read(file7, write7);
        byte [] arr8 = OS.Read(file8, write8);
        byte [] arr9 = OS.Read(file9, write9);

        System.out.println("File_Device_0: " + new String(arr0));
        System.out.println("File_Device_1: " + new String(arr1));
        System.out.println("File_Device_2: " + new String(arr2));
        System.out.println("File_Device_3: " + new String(arr3));
        System.out.println("File_Device_4: " + new String(arr4));
        System.out.println("File_Device_5: " + new String(arr5));
        System.out.println("File_Device_6: " + new String(arr6));
        System.out.println("File_Device_7: " + new String(arr7));
        System.out.println("File_Device_8: " + new String(arr8));
        System.out.println("File_Device_9: " + new String(arr9));
        System.out.println("Should output whatever we wrote into each file showing the correct devices are being accessed.\n");
        
        // Closing device file three and then trying to read from it will throw an error because there is no device to read from.
        System.out.println("FakeFileSystem Read Test 2: Reading from a Device that doesn't exist");
        OS.Close(file3);

        // Read will cause an error message showing the device error is handled
        arr3 = OS.Read(file3, 10);
        System.out.println("Should output error message on Read because device was closed before reading\n");

        // Reopen device for later use
        file3 = OS.Open("file file3.txt");

        // Testing Close
        // Closing devices will free up their corresponding slots
        System.out.println("FakeFileSystem Close Test 1: Closing a device and checking if same slot was freed\n");
        OS.Close(file0);
        OS.Close(file7);

        // 0 and 7 should be free so opening rand7 first should return id 0 and give the other id 7
        file7 = OS.Open("file file7.txt");
        file0 = OS.Open("file file0.txt");

        System.out.println("file7_id: " + file7);
        System.out.println("file0_id: " + file0);
        System.out.println("file7_id should now be 0 and file0_id should now be 7 showing that finding empty device slot works\n");
        
        // Confirming that device has been closed by closing a device that has been closed. Error message
        // lets us know that there is no device where device was closed.
        System.out.println("FakeFileSystem Close Test 2: Try to close file device after int has already been closed");
        OS.Close(9);
        OS.Close(9);
        System.out.println("Should print out error message indicating Close call failed.\n");

        // Closing device rand three and then trying to seek it will throw an error because there is no device to seek in.
        System.out.println("FakeFileSystem Seek Test: Seeking from a Device that doesn't exist");
        OS.Close(file3);

        // Read will cause an error message showing the device error is handled
        OS.Seek(file3, 10);
        System.out.println("Should output error message on Seek because device was closed before seeking\n");
    }

}
