/*
 * Author: My Tran
 * Filename: PingPongTestProcess
 * Description: Test process to test the functionality of messages between processes
 */
public class PingPongTestProcess extends UserlandProcess{
    @Override
    public void run() {
        // Ping is created first since it sends the first message
        OS.CreateProcess(new PingProcess(), Priority.INTERACTIVE);

        // Run with other processes to show can run with other processes
        OS.CreateProcess(new HelloWorld(), Priority.REALTIME);

        // Pong is after ping since it receives the first message
        OS.CreateProcess(new PongProcess(), Priority.INTERACTIVE);
        
        // Run with other processes to show can run with other processes
        OS.CreateProcess(new GoodbyeWorld(), Priority.REALTIME);
    }

}
