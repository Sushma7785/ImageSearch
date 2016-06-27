package com.example.sushma.imagesearch.SpellChecker;

/**
 * Created by sushma on 6/24/16.
 */
public class Trie {

    private Node root;

    public Trie() {
        root = new Node(' ');
    }

    /* Function to insert a String*/
    public void insertString(String word)
    {
        if (search(word) == true)
            return;
        Node current = root;
        for (char ch : word.toCharArray() )
        {
            Node child = current.subNode(ch);
            if (child != null)
                current = child;
            else
            {
                current.children.add(new Node(ch));
                current = current.subNode(ch);
            }
            current.count++;
        }
        current.isEnd = true;
    }
    /* Function to search for word */
    public boolean search(String word)
    {
        Node current = root;
        for (char ch : word.toCharArray() )
        {
            if (current.subNode(ch) == null)
                return false;
            else
                current = current.subNode(ch);
        }
        if (current.isEnd == true)
            return true;
        return false;
    }

    //main function which performs spell checking
    public String spellCheck(String str) {
        Node cur = root;
        char vowelArr[] = {'a', 'e', 'i', 'o', 'u'};
         StringBuffer sb = new StringBuffer(str);
        int  i = 0;
        while(i < sb.length()) {
            if(cur.subNode(sb.charAt(i)) == null) {
                if(isVowel(sb.charAt(i))) {
                    for(int j=0; j< vowelArr.length; j++) {
                        sb.setCharAt(i, vowelArr[j]);
                        if(search(sb.toString())) {
                            return sb.toString();
                        }
                        else if(cur.subNode(sb.charAt(i)) != null) {
                            Node present = cur.subNode(sb.charAt(i));
                            if(i+1 < sb.length() && present.subNode(sb.charAt(i+1)) != null) {
                                cur = cur.subNode(sb.charAt(i));
                                break;
                            }
                            else {
                                continue;
                            }
                        }
                    }
                }
            }
            else {
                Node present = cur.subNode(sb.charAt(i));
                if(i+1 < sb.length() && present.subNode(sb.charAt(i+1)) != null) {
                    cur = cur.subNode(sb.charAt(i));
                }
                else {
                    if(isVowel(sb.charAt(i))) {
                        for(int j=0; j< vowelArr.length; j++) {
                            sb.setCharAt(i, vowelArr[j]);
                            if(search(sb.toString())) {
                                return sb.toString();
                            }
                            else if(cur.subNode(sb.charAt(i)) != null) {
                                Node present1 = cur.subNode(sb.charAt(i));
                                if(i+1 < sb.length() && present1.subNode(sb.charAt(i+1)) != null) {
                                    cur = cur.subNode(sb.charAt(i));
                                    break;
                                }
                                else {
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
            i++;
        }

        return sb.toString();

    }

    private boolean isVowel(char c) {
        if(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')
                return true;
        else
            return false;
    }

}
