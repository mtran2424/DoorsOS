/*
 * Author: My Tran
 * Filename: main.java
 * Description: Driver class to test the OS
 */
public class Main {
    public static void main(String[] args) {
        // Uncomment to test
        // PrioritySchedulerTests();

        // Uncomment to test
        // DevicesTest();

        // Uncomment to test
        // PingPongTest();

        // Uncomment to test
        // PageTest();

        // Uncomment to test
        VirtualMemory();
    }

    /*
     * Run tests on priority scheduler functionality
     */
    public static void PrioritySchedulerTests() {
        // Uncomment startup on each test to run each

        // Probabilistic model testing
        // Real-time process existing probability test
        //OS.StartUp(new ProbabilityTestProcess1());

        // Test 2: No real-time process probability test
        // OS.StartUp(new ProbabilityTestProcess2());

        // Test 3: Real-time and Interactive Processes w/ no Background processes
        // OS.StartUp(new ProbabilityTestProcess3());

        // Test 4: Real-time and Background Processes w/ no Interactive processes
        // OS.StartUp(new ProbabilityTestProcess4());

        // Test 5: Sleep and awakeup functionality
        //OS.StartUp(new SleepTestProcess());

        // Test 6: Testing demotion of real-time processes and interactive processes
        //OS.StartUp(new DemotionTestProcess());
    }


    public static void DevicesTest() {
        // Uncomment startup on each test to run each
        
        // OS.StartUp(new RandomDeviceTestProcess());
        // OS.StartUp(new RandomDeviceLimitTestProcess());

        // OS.StartUp(new FakeFileSystemTestProcess());
        // OS.StartUp(new FakeFileSystemDeviceLimitTestProcess());
        //OS.StartUp(new DevicesTestProcess());
        // OS.StartUp(new VirtualFileSystemDeviceLimitTestProcess());
    }

    public static void PingPongTest() {
        OS.StartUp(new PingPongTestProcess());
    }

    public static void PageTest() {
        // Uncomment startup on each test to run
        OS.StartUp(new PagingTestProcess());
        // OS.StartUp(new PageConcurrentTestProcess());
    }

    public static void VirtualMemory() {
        // Uncomment startup on each test to run
        OS.StartUp(new VirtualMemoryTestProcess1());
        // OS.StartUp(new VirtualMemoryTestProcess2());
    }
}