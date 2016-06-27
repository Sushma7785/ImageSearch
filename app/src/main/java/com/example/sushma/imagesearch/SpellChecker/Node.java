package com.example.sushma.imagesearch.SpellChecker;

import java.util.LinkedList;

/**
 * Created by sushma on 6/24/16.
 */
public class Node {

    char character;
    LinkedList<Node> children;
    boolean isEnd;
    int count;

    public Node(char character) {
        this.character = character;
        this.children = new LinkedList<Node>();
        isEnd = false;
        count = 0;
    }


    public Node subNode(char c)
    {
        if (children != null) {
            for (Node eachChild : children) {
                if (eachChild.character == c)
                    return eachChild;
            }

        }
        return null;
    }


}
