package com.mosaic.schedulers;

/**
 *
 */
public interface AsyncSystem {

    public void start();
    public boolean isRunning();
    public void stop();

}
