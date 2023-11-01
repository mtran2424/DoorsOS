/*
 * Author: My Tran
 * Filename: TestProcess.java
 * Description: Representative of a process. Indefinitely prints out passed in input to
 * the console.
 */
import java.time.Clock;
import java.util.Random;

public class TestProcess extends UserlandProcess {
    // Holds string to be printed in process
    private String text;

    /*
     * Constructor
     * Allows caller to pass in input text
     */
    TestProcess(String input) { 
        text = input;
    }
    
    /*
     * Run method required for runnable
     */
    @Override
    public void run() {
        
        //Randomly generated id to distinguish between running processes
        int id = new Random(Clock.systemDefaultZone().millis()).nextInt(100);

        // infinite loop outputs same text
        while (true) {
            
            // Sleep the thread for more digestible output
            try {
                Thread.sleep(240);
            } catch (Exception e) {}

            System.out.println(id + ": " + text);
        }
    }
}
