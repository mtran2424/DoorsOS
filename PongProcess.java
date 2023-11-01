/*
 * Author: My Tran
 * Filename: PongProcess.java
 * Description: Helper process to test passing kernel messages between processes. Acts as pong side of
 * ping pong test.
 */

public class PongProcess extends UserlandProcess{
    @Override
    public void run() {
        // Test for GetPidByName: If pings pid in this process matches pings pid in the ping process,
        // method works
        int ping = OS.GetPidByName("PingProcess");
        System.out.println("I am PONG, ping is = " + ping);

        byte[] messageArray = {0};
        // Create message to form to ping
        KernelMessage pingPongMessage = new KernelMessage(ping, 0, messageArray);
        // Test SendMessage: Send message to ping to is if it was received properly
        OS.SendMessage(pingPongMessage);

        while(true) {
            // Test WaitForMessage: Make a new message from ping's to pass back to ping. Works if message from ping is received
            pingPongMessage = new  KernelMessage(OS.WaitForMessage());
            messageArray = pingPongMessage.GetData();

            // Print output to compare pong pid in pong with pong pid in ping, ping pid in pong and ping pid in ping,
            // and message from ping with message received in pong. Should be consistent between two
            System.out.println("PONG: from:" + OS.GetPid() + " to: " + ping + " from: " + messageArray[0]);
            
            // Set the target back to ping and send back to keep loop going
            pingPongMessage.SetTarget(ping);
            OS.SendMessage(pingPongMessage);
        }
    }
}
