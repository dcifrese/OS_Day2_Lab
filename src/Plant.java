public class Plant implements Runnable {
    // How long do we want to run the juice processing
    public static final long PROCESSING_TIME = 5*1000;

    public static void main(String[] args) {
        // Creating two plant objects: p, p2
        Plant p = new Plant();
        Plant p2 = new Plant();

        //Starting both the plants and printing out a message indiciating the plants have started
        p.startPlant();
        p2.startPlant();
        System.out.print("Plants Starting, beginnning orange processing ");

        // Give the plants time to do work, per PROCESSING_TIME
        delay(PROCESSING_TIME, "Plant malfunction");

        // Stops both the plants and waits for them to shutdown 
        p.stopPlant();
        p2.stopPlant();
        p.waitToStop();
        p2.waitToStop();

        // Prints out the total oranges provided and processed as well as how many bottles were created and how many oranged wasted
        System.out.println("Total provided/processed = " + (p.getProvidedOranges()+p2.getProvidedOranges()) + "/" + (p.getProcessedOranges()+p2.getProcessedOranges()));
        System.out.println("Created " + (p.getBottles()+p2.getBottles()) +
                           ", wasted " + (p.getWaste()+p2.getWaste()) + " oranges");


    }

    //called after the plants are started, uses a try-catch to have the threads sleep or print out an error message
    private static void delay(long time, String errMsg) {
        long sleepTime = Math.max(1, time);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println(errMsg);
        }
    }

    // determines how many oranges constitutes a full bottle
    public final int ORANGES_PER_BOTTLE = 3;

    //Creates 5 thread objects per the assignment requirements
    private final Thread thread;
    private final Thread thread2;
    private final Thread thread3;
    private final Thread thread4;
    private final Thread thread5;

    // Creates two integers that will be used for statistics
    private int orangesProvided;
    private int orangesProcessed;

    // creates a boolean that will determine if the plant should run
    private volatile boolean timeToWork;

    // sets the two integers for stats to 0 and initializes the 5 threads
    Plant() {
        orangesProvided = 0;
        orangesProcessed = 0;
        thread = new Thread(this, "Plant");
        thread2 = new Thread(this, "Plant");
        thread3 = new Thread(this, "Plant");
        thread4 = new Thread(this, "Plant");
        thread5 = new Thread(this, "Plant");
    }

    //sets timeToWork to true and starts up the threads
    public void startPlant() {
        timeToWork = true;
        thread.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
    }

    //sets time to work to false
    public void stopPlant() {
        timeToWork = false;
    }

    //uses a try catch to have the threads wait to stop or throw a malfunction error
    public void waitToStop() {
        try {
            thread.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            System.err.println(thread.getName() + " stop malfunction");
            System.err.println(thread2.getName() + " stop malfunction");
            System.err.println(thread3.getName() + " stop malfunction");
            System.err.println(thread4.getName() + " stop malfunction");
            System.err.println(thread5.getName() + " stop malfunction");
        }

    }

    //Called once the threads are started. Checks time to work before processing the orange, incrementing the oranges provided, and printing out periods to show its working.
    public void run() {
        //System.out.print(Thread.currentThread().getName() + " Processing Oranges ");
        while (timeToWork) {
            processEntireOrange(new Orange());
            orangesProvided++;
            System.out.print(".");
        }
        System.out.println("");
    }

    //Passes in orange o, checks its state, if its not bottled
    public void processEntireOrange(Orange o) {
        while (o.getState() != Orange.State.Bottled) {
            o.runProcess();
        }
        orangesProcessed++;
    }

    //Passes in o, checjs if o is bottled, increments orangesProcessed. Dont know why it does this, it seems erroneus
    public void completeOrange(Orange o){
        if (o.getState() == Orange.State.Bottled){
            orangesProcessed++;
        }
    }

    //These last few I have deemed self explanatory
    public int getProvidedOranges() {
        return orangesProvided;
    }

    public int getProcessedOranges() {
        return orangesProcessed;
    }

    public int getBottles() {
        return orangesProcessed / ORANGES_PER_BOTTLE;
    }

    //This one might not be so self explanatory, I commented out the original code and checked for waste in my own way because it always seemed to be incorrect
    public int getWaste() {
        //return orangesProcessed % ORANGES_PER_BOTTLE;
        return orangesProvided - orangesProcessed;
    }

    /*DISCLAIMER: The basis of this code was written by my operating systems professor Nate Williams,
    however a substantial portion of it was written by myself with help from both Nate and the almighty 
    information source that is Google. */

}