/*
 * @(#)UTF8Decoder.java
 *
 * Summary: Read/decode UTF-8 encoded bytes, without using Java's built-in decoders to give a 16-bit String.
 *
 * Copyright: (c) 2009-2010 Roedy Green, Canadian Mind Products, http://mindprod.com
 *
 * Licence: This software may be copied and used freely for any purpose but military.
 *          http://mindprod.com/contact/nonmil.html
 *
 * Requires: JDK 1.6+
 *
 * Created with: IntelliJ IDEA IDE.
 *
 * Version History:
 *  1.0 2006-02-24
 */
// UTF8Decoder
package com.konuj.utils;

import java.io.UnsupportedEncodingException;

import static java.lang.System.out;

/**
 * Read/decode UTF-8 encoded bytes, without using Java's built-in decoders to give a 16-bit String.
 *
 * @author Roedy Green, Canadian Mind Products
 * @version 1.0 2006-02-24
 * @since 2006-02-24
 */
public final class UTF8Decoder
    {
    // ------------------------------ CONSTANTS ------------------------------

    /**
     * true if you want the TEST harness to ensure this code works.
     */
    private static final boolean DEBUGGING = true;

    /**
     * byte order mark as a character.
     */
    private static final char BOM = ( char ) 0xfeff;

    // -------------------------- STATIC METHODS --------------------------

    /**
     * decode a String from UTF-8 bytes.  We handle only 16-bit chars.
     * <p/>
     * UTF-8 is normally decoded simply with new String( byte[], "UTF-8" ) or with an InputStreamReader but this is
     * roughly what goes on under the hood, if you ever need to write your own decoder for some non-Java platform, or
     * you are just curious how it works.
     * <p/>
     * This works for 16-bit characters only. It does not handle 32-bit characters encoded with the contortionist use of
     * the low (0xdc00..0xdfff) and high(0xd800..0xdbff) bands of surrogate characters.
     *
     * @param input bytes encoded with UTF-8.
     *
     * @return decoded string
     */
    public static String decode( byte[] input )
        {
        char[] output = new char[input.length];
        // index input[]
        int i = 0;
        // index output[]
        int j = 0;
        while ( i < input.length )
            {
            // get next byte unsigned
            int b = input[ i++ ] & 0xff;
            // classify based on the high order 3 bits
            switch ( b >>> 5 )
                {
                default:
                    // one byte encoding
                    // 0xxxxxxx
                    // use just low order 7 bits
                    // 00000000 0xxxxxxx
                    output[ j++ ] = ( char ) ( b & 0x7f );
                    break;
                case 6:
                    // two byte encoding
                    // 110yyyyy 10xxxxxx
                    // use low order 6 bits
                    int y = b & 0x1f;
                    // use low order 6 bits of the next byte
                    // It should have high order bits 10, which we don't check.
                    int x = input[ i++ ] & 0x3f;
                    // 00000yyy yyxxxxxx
                    output[ j++ ] = ( char ) ( y << 6 | x );
                    break;
                case 7:
                    // three byte encoding
                    // 1110zzzz 10yyyyyy 10xxxxxx
                    assert ( b & 0x10 )
                           == 0 : "UTF8Decoder does not handle 32-bit characters";
                    // use low order 4 bits
                    int z = b & 0x0f;
                    // use low order 6 bits of the next byte
                    // It should have high order bits 10, which we don't check.
                    y = input[ i++ ] & 0x3f;
                    // use low order 6 bits of the next byte
                    // It should have high order bits 10, which we don't check.
                    x = input[ i++ ] & 0x3f;
                    // zzzzyyyy yyxxxxxx
                    int asint = ( z << 12 | y << 6 | x );
                    output[ j++ ] = ( char ) asint;
                    break;
                }// end switch
            }// end while
        return new String( output, 0/* offset */, j/* count */ );
        }

    // --------------------------- main() method ---------------------------

    /**
     * TEST harness to ensure UTF8Decoder works as advertised
     *
     * @param args not used
     *
     * @throws UnsupportedEncodingException if UTF-8 encoding not supported.
     */
    public static void main( String[] args ) throws UnsupportedEncodingException
        {
        if ( DEBUGGING )
            {
            String test =
                    BOM
                    + "Hello World"
                    + "\u0080\u007f\u0080\u0100\u0921\u30b0\u4e70\uffff";
            char[] oneOfAlmostEverything = new char[0xffff + 1];
            for ( int i = 0; i <= 0xffff; i++ )
                {
                oneOfAlmostEverything[ i ] = ( char ) i;
                }
            // avoid testing low band surrogates
            for ( int i = 0xdc00; i <= 0xdfff; i++ )
                {
                oneOfAlmostEverything[ i ] = 0;
                }

            // avoid testing high band surrogates
            for ( int i = 0xd800; i <= 0xdbff; i++ )
                {
                oneOfAlmostEverything[ i ] = 0;
                }

            // put one of almost every possible 16-bit Unicode character in our TEST too.
            test += new String( oneOfAlmostEverything );

            // convert to UTF-8 with built-in Java classes.
            byte[] encoded = test.getBytes( "UTF-8" );

            // reconstitute using our UTF8Decoder, instead of built-in classes.
            String reconstituted = UTF8Decoder.decode( encoded );
            if ( test.equals( reconstituted ) )
                {
                out.println( "UTF8Decoder worked" );
                }
            else
                {
                out.println( "UTF8Decoder failed" );
                out.println( test );
                out.println( reconstituted );

                for ( int i = 0; i < test.length(); i++ )
                    {
                    if ( reconstituted.charAt( i ) != test.charAt( i ) )
                        {
                        out.println( "oops "
                                     + test.charAt( i )
                                     + "["
                                     + Integer.toHexString( test.charAt( i ) )
                                     + "] "
                                     + reconstituted.charAt( i )
                                     + "["
                                     + Integer.toHexString( reconstituted.charAt(
                                i ) )
                                     + "]" );
                        }// end if
                    }// end for
                System.exit( 1 );
                }// end else
            }
        }// end main
    }// UTF8Decoder