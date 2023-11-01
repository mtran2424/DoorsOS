/*
 * Author: My Tran
 * Filename: KernelMessage.java
 * Description: Object to represent a kernel message and its functionalities.
 */
class KernelMessage {
    private int senderpid, // Process id of message sender
            targetpid,  // Process id of message sender
            indicator;  // Indicates what the message is
    private byte[] data;    // array of data for whatever application needs

    /*
     * Copy Constructor for making a copy of another kernel message.
     */
    public KernelMessage(KernelMessage original) {
        // should pids change once the message reaches the target
        senderpid = -1; // Default to -1 because it is always set in SendMessage
        targetpid = original.targetpid;
        indicator = original.indicator;
        data = original.data.clone();   // deep copy
    }

    /*
     * Non Defualt constructor allowing user to create message with
     * arguments needed to send to another process to pass in
     */
    public KernelMessage(int target, int messageIndicator, byte[] messageData) {
        senderpid = -1; // Default to -1 because it is always set in SendMessage
        targetpid = target;
        indicator = messageIndicator;

        // Used by sender to create message.
        // Deep copy is made in SendMessage so shallow copy is fine here.
        data = messageData; // Shallow copy because it will be cloned in child process anyways
    }

    /*
     * Setter for senderpid
     */
    public void SetSender(int sender) {
        senderpid = sender;
    }

    /*
     * Setter for targetpid
     */
    public void SetTarget(int target) {
        targetpid = target;
    }

    /*
     * Sets the data in the message to be specified byte array
     */
    public void SetData(byte[] array) {
        data = array.clone();
    }

    /*
     * Getter for senderpid
     */
    public int GetSender() {
        return senderpid;
    }

    /*
     * Getter for target member
     */
    public int GetTarget() {
        return targetpid;
    }

    
    public byte[] GetData() {
        return data.clone();
    }


    /*
     * Print out byte array as a string
     */
    @Override
    public String toString() {
        return new String(data);
    }
}