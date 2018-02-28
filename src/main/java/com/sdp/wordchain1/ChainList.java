package com.sdp.wordchain1;

import java.util.ArrayList;
import java.util.List;

/**
 * Written By: S. D. Pinkston Feb, 2018
 * Implements a doubly linked, linked list.
 */
public class ChainList {

    private String headKey; // the word/key we are working with currently
    private int depth; // which step of the solution (how deep)

    private ChainList parent; // previous node in the linked list

    // words/terms we found while processing this step
    private final List<String> nodeKeys = new ArrayList<>();

    private ChainList product; // forward link

    public ChainList( String headKey, int depth ) {
        this.headKey = headKey;
        this.depth = depth;
    }

    public ChainList getParent() {
        return parent;
    }

    public void setParent( ChainList parent ) {
        this.parent = parent;
    }

    public int getDepth() {
        return depth;
    }

    public void addNodeKey( String key ) {
        nodeKeys.add( key );
    }

    public List<String> getNodeKeys() {
        return nodeKeys;
    }

    public String getHeadKey() {
        return headKey;
    }

    public ChainList getProduct() {
        return product;
    }

    public void setProduct( ChainList product ) {
        this.product = product;
    }

    /**
     * Ensure no derivatives exist back up the chain (include this instance).
     * @param derivativesList
     */
    public void screenDerivativesAgainstPreviousNodes( List<String> derivativesList ) {

        for( String key : getNodeKeys() ) { // screen against this instance first
            if( derivativesList.contains( key ) ) {
                derivativesList.remove( key );
            }
        }

        if( parent != null ) {
            if( derivativesList.contains( parent.getHeadKey() )) {
                derivativesList.remove( parent.getHeadKey() );
            }
            // also remove any derivatives that exist in the parents nodeKeys
            for( String parentKey : parent.getNodeKeys() ) {
                if( derivativesList.contains( parentKey ) ) {
                    derivativesList.remove( parentKey );
                }
            }
            parent.screenDerivativesAgainstPreviousNodes( derivativesList );
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( "Head: " );
        sb.append( headKey );
        sb.append( " Depth: " );
        sb.append( depth );
        sb.append( "\n  " );

        for( String keys : nodeKeys ) {
            sb.append( keys );
            sb.append( ',' );
        }
        sb.append( '\n' );

        return sb.toString();
    }
}
