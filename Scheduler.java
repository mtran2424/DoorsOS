/*
 * Author: My Tran
 * Filename: Scheduler.java
 * Description: Class simulates an OS process scheduler in the kerneland area.
 */
import java.time.Clock;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Collections;
import java.util.HashMap;

public class Scheduler {
    private List<KernelandProcess> realtimeProcesses, // list to store real-time processes
            interactiveProcesses, // list to store interactive processes
            backgroundProcesses; // list to store background processes
    private List<Pair<Long, KernelandProcess>> sleepingProcesses; // list to store sleeping processes
    private Timer systemClock; // acts as system clock
    private KernelandProcess currentlyRunning; // reference to the current process
    private Kernel kernel;
    private Map<Integer, KernelandProcess> processMap,  // Maps all processes to their pids
            waitingProcesses;
    /*
     * Default constructor for scheduler. Instantiates list of processes, timer and
     * sets initial schedule for
     * system clock.
     */
    public Scheduler(Kernel kernelReference) {
        kernel = kernelReference;

        // Create instances for the process queues to be synchronized for thread safety
        realtimeProcesses = Collections.synchronizedList(new LinkedList<>());
        interactiveProcesses = Collections.synchronizedList(new LinkedList<>());
        backgroundProcesses = Collections.synchronizedList(new LinkedList<>());
        sleepingProcesses = Collections.synchronizedList(new LinkedList<>());

        // Create instance of process map that maps pid to process
        processMap = Collections.synchronizedMap(new HashMap<>());

        // Do the same for waiting processes queue for same reasons as above
        waitingProcesses = Collections.synchronizedMap(new HashMap<>());
  
        // Create timer and schedule for initial interrupt
        systemClock = new Timer();
        systemClock.schedule(new Interrupt(), 250); // Interrupt scheduled to happen in 250ms
    }

    /*
     * Overloaded CreateProcess method that takes priority into consideration
     */
    public int CreateProcess(UserlandProcess up, Priority processPriority) {
        // Create new process instance
        KernelandProcess newProcess = new KernelandProcess(up, processPriority);

        // Add new process to tier appropriate queue
        EnqueueProcess(newProcess);

        // If the newly created process is the first in the list, then there is no
        // process currently running
        if (currentlyRunning == null) {
            SwitchProcess();
        }

        // Insert into process map upon creation to ensure that all processes are getting mapped
        processMap.put(newProcess.getpid(), newProcess);

        // Increment the static next pid and return the new pid process
        return newProcess.getpid();
    }

    /*
     * Adds new process to queue and starts process if one currently isn't running.
     * Defaulted with interactive priority to maintain previous functionality.
     */
    public int CreateProcess(UserlandProcess up) {
        // Create new process instance and add it to queue
        KernelandProcess newProcess = new KernelandProcess(up);
        interactiveProcesses.add(newProcess);

        // If currentProcess is null, no process has been run or there is not other
        // process running
        if (currentlyRunning == null) {
            SwitchProcess();
        }

        // Insert into process map upon creation to ensure that all processes are getting mapped
        processMap.put(newProcess.getpid(), newProcess);

        // Increment the static next pid and return the new pid process
        return newProcess.getpid();
    }

    /*
     * Performs task switch in kerneland
     */
    public void SwitchProcess() {
        // Stop current process

        // If current process is not null, then there is a currently running process
        if (currentlyRunning != null) {
            currentlyRunning.stop(); // Stop the currently running process


            // Remove the process completely from the appropriate if it is finished
            if (currentlyRunning.isDone()) {
                // Update process map when a device is done so ensure no finished processes in stored
                processMap.remove(currentlyRunning.getpid());
                //REMOVE ALL DEVICES
                CloseProcessDevices();
                
                // Free currently running process once process terminates
                FreeMemory();
                RemoveCurrentlyRunning();
            } 
            else { // Process must be placed back onto the queue if it is not finished

                // Temp process variable needed to return unfinished process to back of queue
                KernelandProcess temp = null;

                // Currently running process is going to be the first element in the queue
                switch (currentlyRunning.GetPriority()) {
                    case REALTIME:
                        temp = realtimeProcesses.get(0);
                        break;
                    case INTERACTIVE:
                        temp = interactiveProcesses.get(0);
                        break;
                    case BACKGROUND:
                        temp = backgroundProcesses.get(0);
                        break;
                    default:
                        break;
                }

                // Process that WAS currently running must be taken from the front of queue
                RemoveCurrentlyRunning();

                // Demotion must occur after the process is removed and before it is returned
                // to end up in the correct queue.
                switch(temp.GetPriority()) {
                    case REALTIME:
                    case INTERACTIVE:
                        if(temp.GetTimeout() > 5) {
                            temp.Demote();
                        }
                        break;
                    default:
                        break;
                }

                // Process is not done so it must be requeued up at the end
                EnqueueProcess(temp);
            }

        }
        // Clear the TLB on process switch
        UserlandProcess.translationLookasideBuffer[0][0] = -1;
        UserlandProcess.translationLookasideBuffer[0][1] = -1;
        UserlandProcess.translationLookasideBuffer[1][0] = -1;
        UserlandProcess.translationLookasideBuffer[1][1] = -1;

        // Wake up processes that should be added back to the queue
        WakeProcesses();

        // Get the next process to running based on probabilistic model
        GetNextProcess();

        // Run process chosen
        currentlyRunning.run();
    }

    /*
     * Close all devices opened in the process
     */
    private void CloseProcessDevices() {
        // Close VFS devices through the kernel
        for(int i = 0; i < KernelandProcess.MAX_ARRAY_SIZE; i++) {
            if(currentlyRunning.GetDeviceId(i) != -1) {
                kernel.Close(i);
                currentlyRunning.ResetDevice(i);
            }
        }
    }


    /*
     * Helper function to wake up processes from sleeping queue if appropriate
     */
    private void WakeProcesses() {
        // Wake up sleeping processes to give them a chance to run
        if (!sleepingProcesses.isEmpty()) {

            // Get current clock time to compare with process sleeping times
            Long currTime = Clock.systemDefaultZone().millis();

            // Wake up the eligible sleeping processes who's sleep time is over
            while (!sleepingProcesses.isEmpty() && currTime > sleepingProcesses.get(0).getKey()) {

                // Removes sleeping process from queue and add it to appropriate queue
                EnqueueProcess(sleepingProcesses.get(0).getValue());    
                sleepingProcesses.remove(0);
            }
        }
    }

    /*
     * Chooses next process to run using probabilistic model. Returns 0 if success. -1 if failure
     */
    private int GetNextProcess() {
        // Probabilistic model used for selecting the next process to run
        Random randomNumberGenerator = new Random(Clock.systemDefaultZone().millis());
        
        // Ideally, 6/10 real-time processes run, 3/10 interactive processes run, and
        // 1/10 background processes run
        if (!realtimeProcesses.isEmpty()) {

            switch (randomNumberGenerator.nextInt(10)) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    // There is a 6/10 chance of real-time with 0-5 running real-time proc.
                    currentlyRunning = realtimeProcesses.get(0);
                    break;
                case 6:
                case 7:
                case 8:
                    // There is a 3/10 chance of interactive with 6-8 running interactive proc.
                    // Run real-time if there is no interactive.
                    currentlyRunning = !interactiveProcesses.isEmpty() ? 
                        interactiveProcesses.get(0) : realtimeProcesses.get(0);
                    break;
                case 9:
                    // There is a 1/10 chance of background with 9 running background proc.
                    // Run real-time if there's no background.
                    currentlyRunning = !backgroundProcesses.isEmpty() ? 
                        backgroundProcesses.get(0) : realtimeProcesses.get(0);
                    break;
                default:
                    break;
            }

            return 0;
        } 
        else {
            // If there is no real-time processes then ideally 3/4 interactive processes
            // run and 1/4 background processes run
            if (!interactiveProcesses.isEmpty()) {

                switch (randomNumberGenerator.nextInt(4)) {
                    case 0:
                    case 1:
                    case 2:
                        // There is a 3/4 chance of interact with 0-2 running interactive proc.
                        currentlyRunning = interactiveProcesses.get(0);
                        break;
                    case 3:
                        // There is a 1/4 chance of interact with 3 running background proc.
                        // Run interactive if there's no background.
                        currentlyRunning = !backgroundProcesses.isEmpty() ? 
                            backgroundProcesses.get(0) : interactiveProcesses.get(0);
                    default:
                        break;
                }

                return 0;
            } 
            else {
                if(!backgroundProcesses.isEmpty()) {
                    // Run background if there is neither real-time or interactive
                    currentlyRunning = backgroundProcesses.get(0);

                    return 0;
                }
                else {
                    currentlyRunning = null;
                    return -1;
                }
            }
        }

    }


    /*
     * Sleeps the process for a givne duration of time. Processes are stopped and added to sleeping queue
     * to be later awakened when their minimum sleep time is passed
     */
    public void Sleep(int milliseconds) {
        if(currentlyRunning != null) {
            // Calculate the minimum time for the process to wake up
            Long minSleepTime = Clock.systemDefaultZone().millis() + (long) milliseconds;

            // Reset process timeout count if it is slept because it will not have been interrupted consecutively
            currentlyRunning.resetTimeout();

            // Temp process variable needed to insert slept proc in sleep queue and remove
            // from running queue
            KernelandProcess temp = currentlyRunning;

            // Remove process from queue of awake processes
            RemoveCurrentlyRunning();
            // Unreference currentlyRunning from the proc as it is not going to be running anymore
            currentlyRunning = null;

            // If there are no processes sleeping, target sleeping process can be added to
            // the queue at index 0
            if (sleepingProcesses.isEmpty()) {
                sleepingProcesses.add(new Pair<Long, KernelandProcess>(minSleepTime, temp));
            } 
            else // Add the element in incrementing order to wake processes up with greateer ease
            {
            // Find index to insert new process at
                int insertIndex = 0;
                while (insertIndex < sleepingProcesses.size() &&
                minSleepTime > sleepingProcesses.get(insertIndex).getKey()) {
                    insertIndex++;
                }   

                    // Insert process to sleeping queue at index for incrementing order
                    sleepingProcesses.add(insertIndex, new Pair<Long, KernelandProcess>(minSleepTime, temp));
            }

            SwitchProcess();

            temp.stop();
        }
        
    }

    /*
     * Adds target process to appropriate queue for eligible processes to run
     */
    private void EnqueueProcess(KernelandProcess target) {
        switch (target.GetPriority()) {
            case REALTIME:
                // REALTIME processes go to realtime queue
                realtimeProcesses.add(target);
                break;
            case INTERACTIVE:
                // INTERACTIVE processes go to interactive queue
                interactiveProcesses.add(target);
                break;
            case BACKGROUND:
                // BACKGROUND processes go to background queue
                backgroundProcesses.add(target);
                break;
            default:
                break;
        }
    }

    /*
     * Helper function to remove currently running process from the queue it
     * belonged to
     */
    private void RemoveCurrentlyRunning() {
        // If the currently running process has any of the existing priorities, it must be on the queue
        // because we had to have put it there and ran it
        switch (currentlyRunning.GetPriority()) {
            case REALTIME:
                realtimeProcesses.remove(0);
                break;
            case INTERACTIVE:
                interactiveProcesses.remove(0);
                break;
            case BACKGROUND:
                backgroundProcesses.remove(0);
                break;
            default:
                break;
        }

        currentlyRunning = null;
    }

    /*
     * Accessor for currently running process reference
     */
    public KernelandProcess GetCurrentlyRunning() {
        return currentlyRunning;
    }

    /*
     * Representative of an interrupt causing the "CPU" to switch processes
     */
    private class Interrupt extends TimerTask {
        @Override
        public void run() {

            // Every time an interrupt occurs, the next one needs to be scheduled
            systemClock.schedule(new Interrupt(), 250);
            // If a process is interrupted, it has timed out and count must increase
            if(currentlyRunning != null) {
                currentlyRunning.AddTimeout();
            }
            SwitchProcess();
        }
    }

    /*
     * Returns current process' pid
     */
    public int GetPid() {
        return currentlyRunning.getpid();
    }

    /*
     * Returns pid of process with provided name
     */
    public int GetPidByName(String name) {
        // Check through map of all processes until name is found
        for(Map.Entry<Integer, KernelandProcess> mapEntry : processMap.entrySet()) {
            if(mapEntry.getValue().GetName() == name){
                // return the pid of the process with that name if found
                return mapEntry.getValue().getpid();
            }
        }

        // return -1 if there is no process with that name to indicate error
        System.out.println("GetPidByName Failed: No Such Process by the name \"" + name + "\"");
        return -1;
    }

    /*
     * Return message from other process to current process.
     * Puts process into waiting state until there is a message to return if none.
     */
    public void WaitForMessage() {
        if(!currentlyRunning.HasMessage()) {
            // Temp process variable needed to insert waiting proc in waiting map and remove
            // from running queue
            KernelandProcess temp = currentlyRunning;

            // Remove process from queue of nonwaiting processes
            RemoveCurrentlyRunning();

            // Unreference currentlyRunning from the proc as it is not going to be running anymore
            currentlyRunning = null;

            // Insert process to waiting map to be awoken later on
            waitingProcesses.put(temp.getpid(), temp);

            // Switch to a different process while waiting
            SwitchProcess();

            // Process should stop running if it's still running
            temp.stop();
        }
    }

    /*
     * Send provided message to target indicated by provided message
     */
    public void SendMessage(KernelMessage km) {
        // Check if map is mapping the target of the message
        if(waitingProcesses.containsKey(km.GetTarget())) {
            // Retrieve the process to be put back on running queue for chance to run again
            KernelandProcess temp = waitingProcesses.get(km.GetTarget());

            // Add the message to the process' message queue to be returned by WaitForMessage
            temp.AddMessage(km);

            // put back on running queue for chance to run again and remove from waiting
            EnqueueProcess(temp);
            waitingProcesses.remove(temp.getpid());
        }
        else {
            // Message to indicate there was an error with sending
            System.out.println("SendMessage Failed: No Such Process With That Pid");
        }
    }

    /*
     * Returns target process 
     */
    public KernelandProcess GetProcess(int targetpid) {
        // Get targetpid from map
        return processMap.get(targetpid);
    }

    /*
     * Frees all memory mapped to process virtual memory
     */
    private void FreeMemory() {
        // Starting from virtual pointer 0, free all virual memory in currently running process
        kernel.FreeMemory(0, KernelandProcess.MAX_PAGE_AMOUNT * Kernel.PAGE_SIZE);
    }

    /*
     * Returns reference to a random process
     */
    public KernelandProcess GetRandomProcess(Random randomNumberGenerator) {
        // Get a list of keys from a set of all process keys (pid)
        List<Integer> processList = new ArrayList<>(processMap.keySet());

        // Grab a random key (pid) from a list of all processes
        int randomKey = processList.get(randomNumberGenerator.nextInt(processList.size()));

        // Return the Kerneland process in the process map stored with the random key
        return processMap.get(randomKey);
    }
}
