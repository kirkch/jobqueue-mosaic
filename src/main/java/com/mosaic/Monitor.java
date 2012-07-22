package com.mosaic;

/**
 * Tidy wrapper around Java's notify/wait methods. Removes need to explicity acquire synchronized lock and place
 * noisey+redundant try/catch blocks for the sake of having them.
 */
public class Monitor {

    public synchronized void wake() {
        notify();
    }

    public synchronized void wakeAll() {
        notifyAll();
    }

    public synchronized void sleep() {
        try {
            wait();
        } catch ( InterruptedException e ) {

        }
    }

    public synchronized void sleep( int millis ) {
        try {
            wait( millis );
        } catch ( InterruptedException e ) {

        }
    }

    public synchronized void sleep( long millis ) {
        try {
            wait( millis );
        } catch ( InterruptedException e ) {

        }
    }

}
