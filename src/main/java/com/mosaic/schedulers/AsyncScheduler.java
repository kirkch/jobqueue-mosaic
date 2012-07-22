package com.mosaic.schedulers;

import com.mosaic.Future;

/**
 * Schedule a piece of work to execute at a later date.
 */
public interface AsyncScheduler {

    public <T> Future<T> schedule( AsyncJob<T> job );

}
