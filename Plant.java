public class Plant implements Runnable {
    // How long do we want to run the juice processing
    public static final long PROCESSING_TIME = 5*1000;

    public static void main(String[] args) {
        // Startup a single plant
        Plant p = new Plant();
        Plant p2 = new Plant();

        p.startPlant();
        p2.startPlant();
        // Give the plants time to do work
        
        delay(PROCESSING_TIME, "Plant malfunction");

        // Stop the plant, and wait for it to shutdown
        
        
        p.stopPlant();
        p2.stopPlant();
        p.waitToStop();
        p2.waitToStop();

        // Summarize the results
        System.out.println("Total provided/processed = " + (p.getProvidedOranges()+p2.getProvidedOranges()) + "/" + (p.getProcessedOranges()+p2.getProcessedOranges()));
        System.out.println("Created " + (p.getBottles()+p2.getBottles()) +
                           ", wasted " + (p.getWaste()+p2.getWaste()) + " oranges");


    }

    private static void delay(long time, String errMsg) {
        long sleepTime = Math.max(1, time);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println(errMsg);
        }
    }

    public final int ORANGES_PER_BOTTLE = 3;

    private final Thread thread;
    private final Thread thread2;
    private int orangesProvided;
    private int orangesProcessed;
    private volatile boolean timeToWork;

    Plant() {
        orangesProvided = 0;
        orangesProcessed = 0;
        thread = new Thread(this, "Plant");
        thread2 = new Thread(this, "Plant");
    }

    public void startPlant() {
        timeToWork = true;
        thread.start();
        //thread2.start();
    }

    public void stopPlant() {
        timeToWork = false;
    }

    public void waitToStop() {
        try {
            thread.join();
            //thread2.join();
        } catch (InterruptedException e) {
            System.err.println(thread.getName() + " stop malfunction");
            //System.err.println(thread2.getName() + " stop malfunction");
        }

    }

    public void run() {
        System.out.print(Thread.currentThread().getName() + " Processing oranges");
        while (timeToWork) {
            processEntireOrange(new Orange());
            orangesProvided++;
            System.out.print(".");
        }
        System.out.println("");
    }

    public void processEntireOrange(Orange o) {
        while (o.getState() != Orange.State.Bottled) {
            o.runProcess();
        }
        orangesProcessed++;
    }

    


    public int getProvidedOranges() {
        return orangesProvided;
    }

    public int getProcessedOranges() {
        return orangesProcessed;
    }

    public int getBottles() {
        return orangesProcessed / ORANGES_PER_BOTTLE;
    }

    public int getWaste() {
        return orangesProcessed % ORANGES_PER_BOTTLE;
    }
}