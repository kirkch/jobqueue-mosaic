package com.mosaic.jobqueues;

import org.junit.Test;

import static org.junit.Assert.*;
import static com.mosaic.jobqueues.PowerOf2.*;

/**
 *
 */
public class PowerOf2Test {
    @Test
    public void testRoundUpToClosestPowerOf2() {
        assertEquals( 2, roundUpToClosestPowerOf2( -1 ) );
        assertEquals( 2, roundUpToClosestPowerOf2( 0 ) );
        assertEquals( 2, roundUpToClosestPowerOf2( 1 ) );
        assertEquals( 2, roundUpToClosestPowerOf2( 2 ) );
        assertEquals( 4, roundUpToClosestPowerOf2( 3 ) );
        assertEquals( 4, roundUpToClosestPowerOf2( 4 ) );
        assertEquals( 8, roundUpToClosestPowerOf2( 5 ) );
        assertEquals( 8, roundUpToClosestPowerOf2( 6 ) );
        assertEquals( 8, roundUpToClosestPowerOf2( 7 ) );
        assertEquals( 8, roundUpToClosestPowerOf2( 8 ) );
        assertEquals( 16, roundUpToClosestPowerOf2( 9 ) );
        assertEquals( 32, roundUpToClosestPowerOf2( 18 ) );
    }

    @Test
    public void testIsPowerOf2() {
        assertFalse( isPowerOf2( -1 ) );
        assertFalse( isPowerOf2( 0 ) );
        assertFalse( isPowerOf2( 1 ) );
        assertTrue( isPowerOf2( 2 ) );
        assertFalse( isPowerOf2( 3 ) );
        assertTrue( isPowerOf2( 4 ) );
        assertFalse( isPowerOf2( 5 ) );
        assertFalse( isPowerOf2( 6 ) );
        assertFalse( isPowerOf2( 7 ) );
        assertTrue( isPowerOf2( 8 ) );
        assertFalse( isPowerOf2( 9 ) );
        assertFalse( isPowerOf2( 18 ) );
    }
}
