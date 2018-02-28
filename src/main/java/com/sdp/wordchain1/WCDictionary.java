package com.sdp.wordchain1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Written By: S. D. Pinkston Feb, 2018
 * Implementation of our "dictionary".
 */
public class WCDictionary {

    private String filename;

    // todo: overcome issue with this being declared static
    private static final SortedMap<String,String> dictionary = new TreeMap<>( String.CASE_INSENSITIVE_ORDER );

    private String charSet;

    protected WCDictionary() {
    }

    public WCDictionary( String filename ) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    /**
     * Assumes a "plain" text file as input.
     * @throws IOException
     */
    public void loadFromFile() throws IOException {
        if( dictionary.size() == 0 ) {
            Stream<String> stream = Files.lines( Paths.get( filename ) );
            stream.forEach( WCDictionary::addKeyToDict );

            // immediately calculate the charset. Yes, we might assume a-z, but there
            // are times with smaller dictionaries when there are less than a-z chars
            // in the set, so this step can sometimes limit the permutations used
            this.charSet = Utils.findAllPotentialCharsSorted( dictionary.values() );
        }
        else {
            throw new RuntimeException( "Refusing to load dictionary a second time." );
        }
    }

    public SortedMap<String,String> findFirstKey( String key ) {
        return dictionary.subMap( key, key + "Z" );
    }

    public boolean containsKey( String key ) {
        return dictionary.containsKey( key );
    }

    public String getCharSet() {
        return charSet;
    }

    /**
     * Given an input string and set of characters, construct all derivative strings
     * that do not match by one character that exist in this dictionary.
     * @param input
     * @return
     */
    public List<String> calcDerivatives( String input ) {
        List<String> output = new ArrayList<>();
        char[] inputChars = input.toCharArray(); // calculate this once, hoping to save cycles

        char[] buf; // hoping to save cycles verses a string builder

        for( int x=0; x < input.length(); x++ ) {
            buf = input.toCharArray(); // reset

            for( char tmp : charSet.toCharArray() ) {
                buf[x] = tmp;

                // only allow derivatives that exist in the dictionary
                // do not allow input string. Screen out input word
                if( !Arrays.equals( inputChars, buf ) && dictionary.containsKey( String.copyValueOf( buf ) ) ) {
                    output.add( new String( buf ) );
                }
            }
        }

        return output;
    }

    private static void addKeyToDict( String key ) {
        // fail silently instead of output to System.err
        if( key.indexOf( " " ) >= 0 ) {
//            System.err.println( "Key: [" + key + "] contains a space. Ignoring." );
        } else if( key.indexOf( "'" ) >= 0 ) {
//            System.err.println( "Key: [" + key + "] contains an apostraphe. Ignoring." );
        } else if( key.indexOf( "-" ) >= 0 ) {
//            System.err.println( "Key: [" + key + "] contains a hyphen. Ignoring." );
        } else if( !dictionary.containsKey( key ) ) {
            dictionary.put( key.toLowerCase(), key );
        }
    }

}
