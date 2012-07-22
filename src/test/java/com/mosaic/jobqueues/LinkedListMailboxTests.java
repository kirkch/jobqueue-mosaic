package com.mosaic.jobqueues;

/**
 *
 */
public class LinkedListMailboxTests extends JobQueueInterfaceTestCases {

    public LinkedListMailboxTests() {
        super( new LinkedListJobQueue() );
    }

}
