package com.sdp.wordchain1;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Written By: Scott D. Pinkston Feb, 2018
 * Coordinates components.
 */
public class SolutionCoordinator {

    private WCDictionary dictionary;
    private WCSolution solution;
    private WCEngine engine;

    public void init( ClassPathXmlApplicationContext ctx ) throws IOException {
        dictionary = ctx.getBean( "wcDictionary", WCDictionary.class );
        dictionary.loadFromFile();

        solution = ctx.getBean( "wcSolution", WCSolution.class );
        engine = ctx.getBean( "wcEngine", WCEngine.class );
    }

    public void solve( String origin, String lastWord ) throws IOException {
        if( origin == null || origin.isEmpty() ) {
            throw new RuntimeException( "Beginning term cannot be null or empty." );
        }
        if( lastWord == null || lastWord.isEmpty() ) {
            throw new RuntimeException( "Ending term cannot be null or empty." );
        }
        solution.setOrigin( origin );
        solution.setLastWord( lastWord );

        // todo: you might wish to call vett() here prior to calling the engine

        ChainList product = engine.traverseInitialNode();

        if( product == null ) {
            report();
        } else if( product.getNodeKeys().isEmpty() ) {
            System.err.println( "No derivations exist for [" + solution.getOrigin() + "] Exiting." );
        } else {
            solution.setHeadNode( product );
            engine.traverseProduct( product ); // where the work is done
            engine.dropStats( dictionary.getFilename(), solution );
            report();
        }
    }

    /**
     * Sometimes you may wish to know if the terms selected exist in the dictionary prior to solving.
     */
    public void vettTerms() {
        if( !dictionary.containsKey( solution.getOrigin() ) ) {
            throw new RuntimeException( "Origin word [" + solution.getOrigin() + "] not found in dictionary. Exiting." );
        } else if( !dictionary.containsKey( solution.getLastWord() ) ) {
            throw new RuntimeException( "Last word [" + solution.getLastWord() + "] not found in dictionary. Exiting." );
        }
    }

    private void report() {
        System.out.println( solution.toString() );

        System.out.println( "Ability to find solution for [" + solution.getLastWord() + "] was " + solution.isSuccessFlag() );
        System.out.println( "Product interations: " + engine.getProductTraversalCounter() );
        System.out.println( "Max recursion depth: " + engine.getMaxDepth() );
        System.out.println( "Terminal nodes discovered: " + engine.terminalNodesSize() );
        System.out.println( "Head node keys produced: " + engine.headNodeKeysSize() );
    }

}
