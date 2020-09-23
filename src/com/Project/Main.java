package com.Project;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        if (args.length != 1) {
            quitError("Invalid number of arguments");
        }

        BinarySearchTree btree = new BinarySearchTree();

        try {
            BufferedReader input = new BufferedReader(new FileReader("dictionary.txt"));
            //Scans each word from the input and prints it out
            String data = input.readLine();

            while (data != null) {
                btree.insert(data);
                data = input.readLine();
            }
            System.out.println("Choose an operation :");
            System.out.println("1. Print tree using in order traversal\n" +
                    "2. Print tree using post order traversal\n" +
                    "3. Print tree using pre order traversal\n" +
                    "4. Print tree size (number of words loaded in the tree)\n" +
                    "5. Print tree using BFS Traversal\n" +
                    "6. Print tree height\n" +
                    "7. Search for a word\n" +
                    "8. Insert new word in the tree");
            int operation = scanner.nextInt();
            switch (operation) {
                case 1:
                    btree.printInorder();
                    break;
                case 2:
                    btree.printPostorder();
                    break;
                case 3:
                    btree.printPreorder();
                    break;
                case 4:
                    System.out.println(btree.size(btree.root));
                    break;
                case 5:
                    btree.breadthFirst();
                    break;
                case 6:
                    System.out.println(btree.calculateTreeHeight(btree.root));
                    break;
                case 7:
                    System.out.println("Write a word");
                    String searchedWord = scanner.next();
                    if (btree.iterativeSearch(btree.root, searchedWord))
                        System.out.println("The word is correct");
                    else {
                        System.out.println("The word does not exist");
                        System.out.print("The word in the leaf node is: ");
                        btree.leafNode(btree.root, searchedWord);
                        btree.successorPredecessor(btree.root, searchedWord);
                        System.out.println("The word in the inorder predecessor is: " + btree.predecessor);
                        System.out.println("The word in the inorder successor is: " + btree.successor);

                    }
                    break;
                case 8:
                    System.out.println("Write a word");
                    String insertedWord = scanner.next();
                    if (btree.iterativeSearch(btree.root, insertedWord))
                        System.out.println("The word is actually in the tree..!");
                    else {
                        btree.insert(insertedWord);
                        System.out.println("The word was inserted successfully..!");
                    }
                    break;
            }
            return;
        } catch (FileNotFoundException filenotfoundexception) //Catches file not found exception
        {
            System.out.println("File not found.");
        }
        //Catches input/output exception
        catch (IOException ioexception) {
            System.out.println("File input error occured!");
        }

    }

    //Displays an error message, program exits
    public static void quitError(String msg) {
        System.out.println(msg);
        System.out.println("Program will now quit.");
        System.exit(0);
    }
}

class BinarySearchTree {

    class Node {
        String key;
        Node left, right;

        public Node(String item) {
            key = item;
            left = right = null;
        }
    }

    Node root;

    BinarySearchTree() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    void insert(String key) {
        root = insertRec(root, key);
    }

    Node insertRec(Node root, String key) {

        if (root == null) {
            root = new Node(key);
            return root;
        }

        if (key.compareTo(root.key) < 0)
            root.left = insertRec(root.left, key);
        else if (key.compareTo(root.key) > 0)
            root.right = insertRec(root.right, key);

        return root;
    }

    public static int size(Node root) {
        if (root == null)
            return 0;
        int cnt = 0;
        cnt++;
        cnt += size(root.left);
        cnt += size(root.right);
        return cnt;
    }

    static boolean iterativeSearch(Node root, String key) {
        while (root != null) {

            if (key.compareTo(root.key) > 0)
                root = root.right;

            else if (key.compareTo(root.key) < 0)
                root = root.left;
            else
                return true;
        }
        return false;
    }

    public int calculateTreeHeight(Node root) {
        if (root == null) {
            return 0;
        } else {

            int lsh = calculateTreeHeight(root.left);

            int rsh = calculateTreeHeight(root.right);

            return Math.max(lsh, rsh) + 1;
        }
    }

    public void breadthFirst() {
        int height = calculateTreeHeight(root);
        for (int i = 0; i < height; i++) {
            levelOrderTraversal(root, i);
        }
    }

    public void levelOrderTraversal(Node node, int level) {
        if (node == null) {
            return;
        }
        if (level == 0) {
            System.out.println(node.key + " ");
        } else {
            levelOrderTraversal(node.left, level - 1);
            levelOrderTraversal(node.right, level - 1);
        }
    }


    static void leafNode(Node root, String key) {
        if (root == null)
            System.out.println();

        if (root.right == null && root.left == null)
            System.out.println(root.key);

        else {
            if (key.compareTo(root.key) > 0) {
                if (root.right == null) {
                    System.out.println(root.key);
                } else
                    leafNode(root.right, key);
            } else if (key.compareTo(root.key) < 0) {
                if (root.left == null) {
                    System.out.println(root.key);
                } else
                    leafNode(root.left, key);
            }
        }
    }

    static String successor, predecessor, leaf;

    public void successorPredecessor(Node root, String key) {
        if (root != null) {
            if (root.key == key) {
                if (root.left != null) {
                    Node t = root.left;
                    while (t.right != null) {
                        t = t.right;
                    }
                    predecessor = t.key;
                }
                if (root.right != null) {
                    Node t = root.right;
                    while (t.left != null) {
                        t = t.left;
                    }
                    successor = t.key;
                }
            } else if (0 > key.compareTo(root.key)) {
                successor = root.key;
                successorPredecessor(root.left, key);
            } else if (root.key.compareTo(key) < 0) {
                predecessor = root.key;
                successorPredecessor(root.right, key);
            }
        }
    }

    void printPostorder(Node node) {
        if (node == null)
            return;

        printPostorder(node.left);

        printPostorder(node.right);

        System.out.println(node.key + " ");
    }

    void printInorder(Node node) {
        if (node == null)
            return;

        printInorder(node.left);

        System.out.println(node.key + " ");

        printInorder(node.right);
    }

    void printPreorder(Node node) {
        if (node == null)
            return;

        System.out.println(node.key + " ");

        printPreorder(node.left);

        printPreorder(node.right);
    }

    void printPostorder() {
        printPostorder(root);
    }

    void printInorder() {
        printInorder(root);
    }

    void printPreorder() {
        printPreorder(root);
    }
}