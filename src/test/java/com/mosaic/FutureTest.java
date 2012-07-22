package com.mosaic;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class FutureTest {

    @Test
    public void initialFutureHasNoResultOrException() {
        Future f = new Future();

        assertFalse( f.hasCompleted() );
        assertFalse( f.hasResult() );
        assertFalse( f.hasException() );
    }

    @Test
    public void givenIncompleteFuture_getResultErrors() {
        Future f = new Future();

        try {
            f.getResult();
            fail( "Expected ISE");
        } catch ( IllegalStateException e ) {
            assertEquals( "future has not been completed, call isCompleted() first", e.getMessage() );
        }
    }

    @Test
    public void givenIncompleteFuture_getThrowableErrors() {
        Future f = new Future();

        try {
            f.getException();
            fail( "Expected ISE");
        } catch ( IllegalStateException e ) {
            assertEquals( "future has not been completed, call isCompleted() first", e.getMessage() );
        }
    }

    @Test
    public void givenIncompleteFuture_completeWithResult_expectToBeAbleToRetrieveResult() {
        Future<String> f = new Future<String>();
        f.completeWithResult( "hello" );

        assertEquals( "hello", f.getResult() );
    }

    @Test
    public void givenFutureWithResult_expectHasCompletedToReturnTrue() {
        Future<String> f = new Future<String>();
        f.completeWithResult( "hello" );

        assertTrue( f.hasCompleted() );
    }

    @Test
    public void givenFutureWithResult_expectHasResultToReturnTrue() {
        Future<String> f = new Future<String>();
        f.completeWithResult( "hello" );

        assertTrue( f.hasResult() );
    }

    @Test
    public void givenFutureWithResult_expectHasExceptionToReturnFalse() {
        Future<String> f = new Future<String>();
        f.completeWithResult( "hello" );

        assertFalse( f.hasException() );
    }

    @Test
    public void givenFutureWithResult_expectCompletingWithResultAgainWillBeIgnored() {
        Future<String> f = new Future<String>();
        f.completeWithResult( "hello" );

        f.completeWithResult( "goodbye" ); // expect goodbye to be ignored
        assertEquals( "hello", f.getResult() );
    }

    @Test
    public void givenFutureWithResult_expectCompletingWithThrowableWillBeIgnored() {
        Future<String> f = new Future<String>();
        f.completeWithResult( "hello" );

        f.completeWithException( new RuntimeException() ); // expect exception to be ignored
        assertEquals( "hello", f.getResult() );
    }

    @Test
    public void givenIncompleteFuture_completeWithThrowable_expectToBeAbleToRetrieveThrowable() {
        Throwable ex = new RuntimeException( "test exception" );

        Future<String> f = new Future<String>();
        f.completeWithException( ex );

        assertEquals( ex, f.getException() );
    }

    @Test
    public void givenFutureCompletedWithException_expectHasCompletedToReturnTrue() {
        Throwable ex = new RuntimeException( "test exception" );

        Future<String> f = new Future<String>();
        f.completeWithException( ex );

        assertTrue( f.hasCompleted() );
    }

    @Test
    public void givenFutureCompletedWithException_expectHasResultToReturnFalse() {
        Throwable ex = new RuntimeException( "test exception" );

        Future<String> f = new Future<String>();
        f.completeWithException( ex );

        assertFalse( f.hasResult() );
    }

    @Test
    public void givenFutureCompletedWithException_expectHasExceptionToReturnTrue() {
        Throwable ex = new RuntimeException( "test exception" );

        Future<String> f = new Future<String>();
        f.completeWithException( ex );

        assertTrue( f.hasException() );
    }

    @Test
    public void givenIncompleteFuture_completeWithResult_expectToNotBeAbleToRetrieveException() {
        Future<String> f = new Future<String>();
        f.completeWithResult( "maddie" );

        try {
            f.getException();
            fail( "Expected ISE");
        } catch ( IllegalStateException e ) {
            assertEquals( "future contains a result, not an exception", e.getMessage() );
        }
    }

    @Test
    public void givenIncompleteFuture_completeWithException_expectToNotBeAbleToRetrieveResult() {
        Throwable ex = new RuntimeException( "test exception" );

        Future<String> f = new Future<String>();
        f.completeWithException( ex );

        try {
            f.getResult();
            fail( "Expected ISE");
        } catch ( IllegalStateException e ) {
            assertEquals( "future contains an exception, not a result", e.getMessage() );
            assertEquals( ex, e.getCause() );
        }
    }


// givenIncompleteFuture_completeWithException_expectToNotBeAbleToRetrieveResult
// givenIncompleteFuture_completeWithException_expectToNotBeAbleToCompleteWithResult
// givenIncompleteFuture_completeWithException_expectToNotBeAbleToCompleteWithException
// givenIncompleteFuture_completeWithResult_expectToNotBeAbleToCompleteWithResult
// givenIncompleteFuture_completeWithResult_expectToNotBeAbleToCompleteWithException
}
