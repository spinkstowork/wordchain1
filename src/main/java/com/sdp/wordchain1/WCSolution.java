package com.sdp.wordchain1;

/**
 * Written By: S. D. Pinkston Feb, 2018
 * Small value object class to house the solution.
 * Also has code to list out the solution chain.
 */
public class WCSolution {

    private String origin;
    private String lastWord;
    private boolean successFlag = false;

    private ChainList headNode; // first node of a doubly linked list

    public void setOrigin( String origin ) {
        this.origin = origin;
    }

    public void setLastWord( String lastWord ) {
        this.lastWord = lastWord;
    }

    public boolean isSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag( boolean successFlag ) {
        this.successFlag = successFlag;
    }

    public String getOrigin() {
        return origin;
    }

    public String getLastWord() {
        return lastWord;
    }

    public void setHeadNode( ChainList headNode ) {
        this.headNode = headNode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( '\n' );

        if( headNode == null ) {
            buildForNode( sb, 0, origin, lastWord );
        }
        else if( headNode.getProduct() == null ) {
            buildForNode( sb, headNode.getDepth(), origin, lastWord );
        }
        else {
            ChainList curNode = headNode.getProduct(); // skip first node
            ChainList lastNode = curNode;

            while( curNode != null ) {
                buildForNode( sb, curNode.getDepth(), curNode.getParent().getHeadKey(),
                    curNode.getHeadKey() );

                lastNode = curNode;
                curNode = curNode.getProduct();
            }

            if( successFlag ) {
                buildForNode( sb, lastNode.getDepth() + 1, lastNode.getHeadKey(), lastWord );
            }
        }

        return sb.toString();
    }

    private void buildForNode( StringBuilder sb, int depth, String key1, String key2 ) {
        sb.append( "Depth: " );
        sb.append( depth );
        sb.append( ' ' );
        sb.append( key1 );
        sb.append( " -> " );
        sb.append( key2 );
        sb.append( '\n' );
    }
}
