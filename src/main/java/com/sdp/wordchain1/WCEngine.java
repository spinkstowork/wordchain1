package com.sdp.wordchain1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Written By: S. D. Pinkston Feb, 2018
 * Engine that makes use of recursion to discover nodes in a word chain.
 * Implements a "depth first" node traversal.
 *
 * When new nodes are discovered, they are compared to prior nodes in the chain. Any redundant
 * nodes are pruned so that we dont attempt the same solutions twice.
 *
 */
public class WCEngine {

    // MAX recursion depth
    public static final int MAX_DEPTH = 200;

    private WCDictionary wcDictionary;
    private WCSolution wcSolution;

    private int depth = 0; // brake for recursion
    private int maxDepth = 0;

    // Its possible to refactor as to not need this flag. Yet the flag offers clarity
    // and to my best knowledge does no harm
    private boolean keepGoingFlag = true;

    // how many products (head nodes) have been processed
    private int productTraversalCounter = 0;

    // Record of head node keys
    private Map<String,String> headNodeKeys = new TreeMap<>( String.CASE_INSENSITIVE_ORDER );

    // Map containing keys already found to be terminal nodes
    private Map<String,String> terminalNodesMap = new TreeMap<>( String.CASE_INSENSITIVE_ORDER );

    protected WCEngine() {
    }

    /**
     * engine requires a dictionary and a potential wcSolution to begin processing.
     * @param wcDictionary
     * @param wcSolution
     */
    public WCEngine( WCDictionary wcDictionary, WCSolution wcSolution ) {
        this.wcDictionary = wcDictionary;
        this.wcSolution = wcSolution;
    }

    /**
     * Recursive method. Implements a depth first traversal of derivative words based
     * on a dictionary (word set).
     * @param product
     */
    public void traverseProduct( ChainList product ) {
        productTraversalCounter++; // ever increments

        if( maxDepth < depth ) {
            maxDepth = depth;
        }

        if( (depth+1) >= MAX_DEPTH ) { // limit recursion
            keepGoingFlag = false;
            System.out.println( "HIT MAX DEPTH PROCESSING: " + product.getHeadKey() );
        }

        if( keepGoingFlag && (depth++ <= MAX_DEPTH) ) {
            System.out.println( product.toString() );

            Iterator<String> itor = product.getNodeKeys().iterator();

            while( keepGoingFlag && itor.hasNext()) {
                String key = itor.next();

                List<String> derivativesList = wcDictionary.calcDerivatives( key );
                derivativesList.remove( product.getHeadKey() ); // todo: is it optimal to remove head after adding?

                if( derivativesList.isEmpty() ) {
                    // do not set keepGoingFlag to false, simply let the stack unwind
                    addTerminalNode( key );
//                    System.out.println( key + " further derivations not found. Terminal node." );
                    break; // exit while loop
                }
                else if( derivativesList.contains( wcSolution.getLastWord() ) ) {
                    // Problem was solved!
                    keepGoingFlag = false;
                    wcSolution.setSuccessFlag( true );

                    ChainList lastOne = new ChainList( key, depth );
                    lastOne.setParent( product );

                    for( String skey : derivativesList ) {
                        if( skey.equals( wcSolution.getLastWord() ) ) {
                            System.out.println( skey + " is the wcSolution found at depth: " + depth );
                        }
                        lastOne.addNodeKey( skey );
                    }
                    product.setProduct( lastOne );
                    System.out.println( lastOne.toString() );
                }
                else {
                    screenDerivativesAgainstTerminalNodes( derivativesList );
                    screenDerivativesAgainstPreviousHeadNodes( derivativesList );
                    product.screenDerivativesAgainstPreviousNodes( derivativesList );

                    if( derivativesList.isEmpty() ) {
                        if( terminalNodesMap.containsKey( key )) {
                            throw new RuntimeException( "Impossible. TerminalNodesMap contains key: " + key );
                        }
                        else {
                            addTerminalNode( key );
                        }
//                        System.out.println( key + " is a terminal node." );
                    }
                    else {
                        // if a node key was already traversed, then skip it. Unravel the stack

                        // Only pursue keys which have not been traversed before
                        if( !headNodeKeys.containsKey( key ) ) {
                            // keep track of which keys have been traversed
                            headNodeKeys.put( key, key );

                            // create the next link in the chain
                            ChainList newChainNode = new ChainList( key, depth );
                            newChainNode.setParent( product );

                            for( String derivativeKey : derivativesList ) {
                                newChainNode.addNodeKey( derivativeKey );
                            }

                            product.setProduct( newChainNode );
                            traverseProduct( newChainNode ); // note recursion
                        }
                    }
                }
            }
            depth--;
        }
    }

    private void screenDerivativesAgainstTerminalNodes( List<String> derivativesList ) {
        for( String key : terminalNodesMap.keySet() ) {
            if( derivativesList.contains( key )) {
                derivativesList.remove( key );
            }
        }
    }

    private void screenDerivativesAgainstPreviousHeadNodes( List<String> derivativesList ) {
        for( String key : headNodeKeys.keySet() ) {
            if( derivativesList.contains( key )) {
                derivativesList.remove( key );
            }
        }
    }

    public int headNodeKeysSize() {
        return headNodeKeys.size();
    }

    public int terminalNodesSize() {
        return terminalNodesMap.size();
    }

    public int getProductTraversalCounter() {
        return productTraversalCounter;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    /**
     * Attempt to find the first derivitive word set.
     * @return
     */
    public ChainList traverseInitialNode() {

        List<String> derivativesList = wcDictionary.calcDerivatives( wcSolution.getOrigin() );

        if( derivativesList.contains( wcSolution.getLastWord() )) {
            wcSolution.setSuccessFlag( true );
            System.out.println( wcSolution.getLastWord() + " is the wcSolution found at depth: " + depth );
            return null;
        }

        ChainList tmp = new ChainList( wcSolution.getOrigin(), depth );

        for( String skey : derivativesList ) {
            tmp.addNodeKey( skey );
        }

        return tmp;
    }

    private void addTerminalNode( String wreKey ) { // localize this operation
        terminalNodesMap.put( wreKey, wreKey );
    }

    /**
     * Provide some stats/feedback on the nodes traversed.
     * @param dictFilename
     * @param solution
     * @throws IOException
     */
    public void dropStats( String dictFilename, WCSolution solution ) throws IOException {
        dictFilename = dictFilename.replace( '.', '-' );

        String outfilename = String.format( "%s-%s-%s.out", solution.getOrigin(), solution.getLastWord(), dictFilename );

        FileWriter fw = new FileWriter( outfilename.toString() );

        fw.write( (new Date()).toString() );
        fw.write( "\nProduct head nodes:\n" );

        for( String key : headNodeKeys.keySet() ) {
            fw.write( key );
            fw.write( '\n' );
        }

        fw.write( "\nTerminal nodes:\n" );

        for( String key : terminalNodesMap.keySet() ) {
            fw.write( key );
            fw.write( '\n' );
        }
        fw.close();
    }

}
