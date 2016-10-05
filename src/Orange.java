public class Orange {
    //an enumeration that holds the different state of the orange process and the respective times for completion
    public enum State {
        Fetched(15),
        Peeled(38),
        Squeezed(29),
        Bottled(17),
        Processed(1);

        final int timeToComplete;

        private State(int timeToComplete) {
            this.timeToComplete = timeToComplete;
        }
        //checks if the current state of o is processed, if not it returns the next state in the enum
        public State nextState(){
            if (this == Processed){
                return this;
            }
            int nextIndex = this.ordinal()+1;
            return State.values()[nextIndex];
            
        }
    }
    private State state;

    //gives the state fetched and calls doWork
    public Orange() {
        state = State.Fetched;
        doWork();
    }

    //returns the current state
    public State getState() {
        return state;
    }

    //if the state is processed, throw an exception else do work and change the state
    public void runProcess() {
        // Don't attempt to process an already completed orange
        if (state == State.Processed) {
            throw new IllegalStateException("This orange has already been processed");
        }
        doWork();
        state = state.nextState();
    }
    //just sleeps for however long it takes to do the work 
    private void doWork() {
        // Sleep for the amount of time necessary to do the work
        try {
            Thread.sleep(state.timeToComplete);
        } catch (InterruptedException e) {
            System.err.println("Incomplete orange processing, juice may be bad");
        }
    }
    /*DISCLAIMER: The basis of this code was written by my operating systems professor Nate Williams,
    however a substantial portion of it was written by myself with help from both Nate and the almighty 
    information source that is Google. */
}
