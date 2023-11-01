/*
 * Author: My Tran
 * Filename: Device.java
 * Description: Provides interface for implementation of Devices
 */
public interface Device {
    int Open(String s) throws Exception;
    void Close(int id);
    byte[] Read(int id, int size);
    void Seek(int id, int to);
    int Write(int id, byte[] data);
}