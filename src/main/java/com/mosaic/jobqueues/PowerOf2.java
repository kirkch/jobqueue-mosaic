package com.mosaic.jobqueues;

/**
 *
 */
class PowerOf2 {
    public static int roundUpToClosestPowerOf2( int v ) {
        int n = 2;

        while ( v > n ) {
            n *= 2;
        }

        return n;
    }

    public static boolean isPowerOf2( int v ) {
        return v == roundUpToClosestPowerOf2( v );
    }
}
