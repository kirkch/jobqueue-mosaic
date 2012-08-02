package com.mosaic;

import com.mosaic.utils.SetUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 *
 */
public class Error_WithExceptionTests {

    @Test
    public void testGetDescription() {
        Error error = new Error(new IOException("smoke is pooring out of the sides"));

        assertEquals( "smoke is pooring out of the sides", error.getDescription() );
    }

    @Test
    public void getErrorType() {
        Error error = new Error(new IOException("smoke is pooring out of the sides"));

        assertEquals( "java.io.IOException", error.getErrorType() );
    }

    @Test
    public void getTags() {
        assertEquals( new HashSet(), new Error(new IOException("smoke is pooring out of the sides")).getTags() );
        assertEquals( SetUtils.asSet( "s1" ), new Error(new IOException("smoke is pooring out of the sides")).addTags("s1").getTags() );
        assertEquals( SetUtils.asSet("s1","s2"), new Error(new IOException("smoke is pooring out of the sides")).addTags("s1","s2").getTags() );
        assertEquals( 2, new Error(new IOException("smoke is pooring out of the sides")).addTags("s1","s2").getTags().size() );
    }

    @Test
    public void containsTag() {
        assertFalse( new Error( new IOException("smoke is pooring out of the sides") ).containsTag( "s1" ) );
        assertTrue( new Error( new IOException("smoke is pooring out of the sides")).addTags("s1" ).containsTag( "s1" ) );
        assertTrue( new Error( new IOException("smoke is pooring out of the sides")).addTags("s1", "s2" ).containsTag( "s1" ) );
    }

    @Test
    public void containsTags() {
        assertFalse( new Error(new IOException("smoke is pooring out of the sides")).containsTags("s1") );
        assertTrue(  new Error(new IOException("smoke is pooring out of the sides")).addTags("s1").containsTags("s1") );
        assertFalse( new Error( new IOException("smoke is pooring out of the sides")).addTags("s1" ).containsTags( "s1", "s2" ) );
        assertTrue( new Error( new IOException("smoke is pooring out of the sides")).addTags("s1", "s2" ).containsTags( "s1" ) );
        assertTrue( new Error( new IOException("smoke is pooring out of the sides")).addTags("s1", "s2" ).containsTags( "s1", "s2" ) );
    }

    @Test
    public void addTags_expectOriginalErrorToNotBeModified() {
        Error e1 = new Error(new IOException("smoke is pooring out of the sides"));
        Error e2 = e1.addTags("s1");

        assertFalse( e1.containsTag("s1") );
        assertTrue ( e2.containsTag("s1") );
    }


    @Test
    public void addTags_addingATagThatAlreadyExistsReturnsTheSameImmutableError() {
        Error e1 = new Error(new IOException("smoke is pooring out of the sides")).addTags("s1");
        Error e2 = e1.addTags("s1");

        assertTrue( e1 == e2 );
    }

    @Test
    public void removeTags() {
        Error e1 = new Error(new IOException("smoke is pooring out of the sides")).addTags("s1","s2","s3");
        Error e2 = e1.removeTags( "s1", "s3" );

        assertTrue( e1.containsTag("s1") );
        assertTrue( e1.containsTag("s2") );
        assertTrue( e1.containsTag("s3") );

        assertFalse ( e2.containsTag("s1") );
        assertFalse( e2.containsTag( "s3" ) );
    }

    @Test
    public void removeTags_removeTagsThatDoNoExistReturnsTheSameInstance() {
        Error e1 = new Error(new IOException("smoke is pooring out of the sides")).addTags("s1");
        Error e2 = e1.removeTags( "s2" );

        assertTrue( e1 == e2 );
    }

    @Test
    public void isExceptional() {
        assertTrue( new Error( new IOException("smoke is pooring out of the sides")).addTags("s1" ).isExceptional() );
    }

    @Test
    public void throwAsException() throws Throwable {
        try {
            new Error(new IOException("smoke is pooring out of the sides")).addTags("s1").throwAsException();
            fail( "expected exception" );
        } catch ( IOException ex ) {
            assertEquals( "smoke is pooring out of the sides", ex.getMessage() );
        }
    }

    @Test
    public void throwAsUncheckedException() throws Throwable {
        try {
            new Error(new NullPointerException("splat")).addTags("s1").throwAsUncheckedException();
            fail( "expected exception" );
        } catch ( NullPointerException ex ) {
            assertEquals( "splat", ex.getMessage() );
        }
    }

    @Test
    public void throwAsUncheckedException_usingACheckedException() throws Throwable {
        try {
            new Error(new IOException("smoke is pooring out of the sides")).addTags("s1").throwAsUncheckedException();
            fail( "expected exception" );
        } catch ( RuntimeException ex ) {
            assertEquals( "java.io.IOException: smoke is pooring out of the sides", ex.getMessage() );
        }
    }

}
