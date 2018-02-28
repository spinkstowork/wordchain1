package com.sdp.wordchain1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Written By: Scott D. Pinkston Feb, 2018
 * Misc. utilities.
 */
public class Utils {

    /**
     * Given an input string and set of characters, construct all derivative strings
     * that do not match by one character.
     * @param input
     * @param charSet
     * @return
     */
    public static List<String> calcAllDerivatives( String input, String charSet ) {
        List<String> output = new ArrayList<>();
        char[] inputChars = input.toCharArray(); // calculate this once, hoping to save cycles

        char[] buf; // hoping to save cycles verses a string builder
        // char[] buf = new char[ input.length() ];

        for( int x=0; x < input.length(); x++ ) {
            buf = input.toCharArray(); // reset

            for( char tmp : charSet.toCharArray() ) {
                buf[x] = tmp;

                if( !Arrays.equals( inputChars, buf ) ) { // no dups in output list allowed
                    output.add( new String( buf ) );
                }
            }
        }

        return output;
    }

    /**
     * Simple string sort.
     * @param inputKey
     * @return
     */
    public static String sortString( String inputKey ) {
        char[] buf = inputKey.toCharArray();
        Arrays.sort( buf );
        return new String( buf );
    }

    /**
     * Return count of chars 2 strings have in common.
     * @param org
     * @param cmp
     * @return
     */
    public static int countCommon( String org, String cmp ) {
        int retVal = 0;

        for( int x=0; x < org.length(); x++ ) {
            if( cmp.indexOf( org.charAt( x ) ) >= 0 ) {
                retVal++;
            }
        }
        return retVal;
    }

    /**
     * Returns a string the same length as org having '1' in positions where org and cmp have the
     * same characters by position. So given 'dog' and 'lag' result is '001'.
     * @param org
     * @param cmp
     * @return
     */
    public static String getCommonByPos( String org, String cmp ) {
        char[] buf = new char[ org.length() ];
        Arrays.fill( buf, '0' );

        for( int x=0; x < org.length(); x++ ) {
            if( org.charAt( x ) == cmp.charAt( x ) ) {
                buf[x] = '1';
            }
        }
        return new String( buf );
    }

    /**
     * Convenience method.
     * @param dictionary
     * @return
     */
    public static String findAllPotentialCharsSorted( Collection<String> dictionary ) {
        return sortString( findAllPotentialChars( dictionary ) );
    }

    /**
     * Given a set of dictionaryArray words, calculate total set of chars present, output in String form.
     * So given { "kid", "Rok" } output "dikor".
     * case-insensitive
     * @param dictionary
     * @return
     */
    public static String findAllPotentialChars( Collection<String> dictionary ) {
        StringBuffer sb = new StringBuffer();

        for( String word : dictionary ) {
            for( int x=0; x < word.length(); x++ ) {
                char searchFor = word.toLowerCase().charAt( x );

                // todo: the way in which the stringbuffer is searched may not be optimal, revisit solution
                if( sb.toString().indexOf( searchFor ) == -1 ) {
                    sb.append( searchFor );
                }
            }
        }

        return sb.toString();
    }
}
