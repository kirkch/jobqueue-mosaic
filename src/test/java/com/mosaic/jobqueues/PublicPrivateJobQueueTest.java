package com.mosaic.jobqueues;

/**
 *
 */
public class PublicPrivateJobQueueTest extends JobQueueInterfaceTestCases {

    public PublicPrivateJobQueueTest() {
        super( new PublicPrivateJobQueue(new SynchronizedJobQueueWrapper(new LinkedListJobQueue()), new LinkedListJobQueue()) );

        bulkPopMayReturnLessThanAllJobs = true;
    }

}
