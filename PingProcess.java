/*
 * Author: My Tran
 * Filename: PingProcess.java
 * Description: Helper process to test passing kernel messages between processes. Acts as ping side of
 * ping pong test.
 */

public class PingProcess extends UserlandProcess{
    @Override
    public void run() {
        // Test for GetPidByName: If pong pid in this process matches pong pid in the pong process,
        // method works
        int pong = OS.GetPidByName("PongProcess");
        System.out.println("I am PING, pong is = " + pong);
        byte[] messageArray = {0};

        // Test WaitForMessage: Make a new message from pong's to pass back to ping. Works if message from pong is received
        KernelMessage pingPongMessage = new KernelMessage(OS.WaitForMessage());

        // Set the target back to pong and send back to prime the loop
        pingPongMessage.SetData(messageArray);
        pingPongMessage.SetTarget(pong);
        OS.SendMessage(pingPongMessage);

        while(true) {
            // Test WaitForMessage: Make a new message from pong's to pass back to pong. Works if message from pong is received
            pingPongMessage = OS.WaitForMessage();
            messageArray = pingPongMessage.GetData();


            // Test GetPid
            // Print output to compare ping pid in ping with ping pid in pong, pong pid in ping and pong pid in pong,
            // and message from pong with message received in pong. Should be consistent between two
            System.out.println("PING: from:" + OS.GetPid() + " to: " + pong + " from: " + messageArray[0]++);
            pingPongMessage.SetData(messageArray);

            // Set the target back to pong and updated data and send back to keep loop going
            pingPongMessage.SetTarget(pong);
            OS.SendMessage(pingPongMessage);
        }
    }
}
