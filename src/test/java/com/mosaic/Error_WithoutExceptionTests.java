package com.mosaic;

import com.mosaic.utils.SetUtils;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

/**
 *
 */
public class Error_WithoutExceptionTests {

    @Test
    public void testGetDescription() {
        Error error = new Error("desc", "t1");

        assertEquals( "desc", error.getDescription() );
    }

    @Test
    public void getErrorType() {
        Error error = new Error("desc", "t1");

        assertEquals( "t1", error.getErrorType() );
    }

    @Test
    public void getTags() {
        assertEquals( new HashSet(), new Error("desc", "t1").getTags() );
        assertEquals( SetUtils.asSet("s1"), new Error("desc", "t1", "s1").getTags() );
        assertEquals( SetUtils.asSet("s1","s2"), new Error("desc", "t1", "s1","s2").getTags() );
        assertEquals( 2, new Error("desc", "t1", "s1","s2").getTags().size() );
    }

    @Test
    public void containsTag() {
        assertFalse( new Error( "desc", "t1" ).containsTag( "s1" ) );
        assertTrue( new Error( "desc", "t1", "s1" ).containsTag( "s1" ) );
        assertTrue( new Error( "desc", "t1", "s1", "s2" ).containsTag( "s1" ) );
    }

    @Test
    public void containsTags() {
        assertFalse( new Error("desc", "t1").containsTags("s1") );
        assertTrue(  new Error("desc", "t1", "s1").containsTags("s1") );
        assertFalse( new Error( "desc", "t1", "s1" ).containsTags( "s1", "s2" ) );
        assertTrue( new Error( "desc", "t1", "s1", "s2" ).containsTags( "s1" ) );
        assertTrue( new Error( "desc", "t1", "s1", "s2" ).containsTags( "s1", "s2" ) );
    }

    @Test
    public void addTags_expectOriginalErrorToNotBeModified() {
        Error e1 = new Error("desc", "t1");
        Error e2 = e1.addTags("s1");

        assertFalse( e1.containsTag("s1") );
        assertTrue ( e2.containsTag("s1") );
    }


    @Test
    public void addTags_addingATagThatAlreadyExistsReturnsTheSameImmutableError() {
        Error e1 = new Error("desc", "t1", "s1");
        Error e2 = e1.addTags("s1");

        assertTrue( e1 == e2 );
    }

    @Test
    public void removeTags() {
        Error e1 = new Error("desc", "t1", "s1","s2","s3");
        Error e2 = e1.removeTags( "s1", "s3" );

        assertTrue( e1.containsTag("s1") );
        assertTrue( e1.containsTag("s2") );
        assertTrue( e1.containsTag("s3") );

        assertFalse ( e2.containsTag("s1") );
        assertFalse( e2.containsTag( "s3" ) );
    }

    @Test
    public void removeTags_removeTagsThatDoNoExistReturnsTheSameInstance() {
        Error e1 = new Error("desc", "t1", "s1");
        Error e2 = e1.removeTags( "s2" );

        assertTrue( e1 == e2 );
    }

    @Test
    public void isExceptional() {
        assertFalse( new Error( "desc", "t1", "s1" ).isExceptional() );
    }

    @Test
    public void throwAsException() throws Throwable {
        try {
            new Error("desc", "t1", "s1").throwAsException();
            fail( "expected exception" );
        } catch ( RuntimeException ex ) {
            assertEquals( "t1(desc, tags=[s1])", ex.getMessage() );
        }
    }

    @Test
    public void throwAsUncheckedException() throws Throwable {
        try {
            new Error("desc", "t1", "s1").throwAsUncheckedException();
            fail( "expected exception" );
        } catch ( RuntimeException ex ) {
            assertEquals( "t1(desc, tags=[s1])", ex.getMessage() );
        }
    }

}
