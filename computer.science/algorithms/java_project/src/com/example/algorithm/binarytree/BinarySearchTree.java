package com.example.algorithm.binarytree;

public class BinarySearchTree {
    BinaryNode root;

    // for dump debug information
    private int currentLevel = -1;

    public BinaryNode getRoot() {
        return root;
    }

    void insert(int value) {
        if (root == null) {
            root = new BinaryNode(value);
            return;
        }
        insertInternal(root, value);
    }

    private void insertInternal(BinaryNode node, int value) {
        if (value < node.getValue()) {
            if(node.getLeft() == null) {
                node.setLeft(new BinaryNode(value));
                return;
            } else {
                insertInternal(node.getLeft(), value);
                return;
            }
        } else {
            if(node.getRight() == null) {
                node.setRight(new BinaryNode(value));
                return;
            } else {
                insertInternal(node.getRight(), value);
                return;
            }
        }
    }

    public BinaryNode delete(int value) {
        BinaryNode sentry = new BinaryNode(Integer.MIN_VALUE);
        sentry.setRight(root);

        BinaryNode ret =  deleteInternal(sentry, sentry.getRight(), value);
        root = sentry.getRight();
        return ret;
    }

    private BinaryNode deleteInternal(BinaryNode parent, BinaryNode node, int value) {
        if(node == null) return null;

        if(node.getValue() == value) {
            if (node.hasTwoChildren()) {
                return deleteNodeWithTwoChild(node);
            } else if (node.hasLeftChild() || node.hasRightChild()) {
                // node has one child
                BinaryNode child = null;
                if(node.hasLeftChild()) {
                    child = node.getLeft();
                } else {
                    child = node.getRight();
                }
                if (node == parent.getLeft()) {
                    parent.setLeft(child);
                } else if (node == parent.getRight()) {
                    parent.setRight(child);
                }
            } else {
                // node has no child
                if (node == parent.getLeft()) {
                    parent.setLeft(null);
                } else if (node == parent.getRight()) {
                    parent.setRight(null);
                }
            }
            return node;
        } else if (value < node.getValue()) {
            return deleteInternal(node, node.getLeft(), value);
        } else {
            // (value > node.getValue())
            return deleteInternal(node, node.getRight(), value);
        }
    }

    private BinaryNode deleteNodeWithTwoChild(BinaryNode node) {
        int ret = node.getValue();
        BinaryNode p = node;
        BinaryNode q = node.getRight();
        while (q.hasLeftChild()) {
            p = q;
            q = q.getLeft();
        }
        node.setValue(q.getValue());
        if(p.getLeft() == q) {
            p.setLeft(q.getRight());
        } else if (p.getRight() == q) {
            p.setRight(q.getRight());
        }
        return new BinaryNode(ret);
    }

    private void printTreeLevel(BinaryNode node, int level) {
        currentLevel++;
        try {
            if(node == null) return;
            if(currentLevel == level) {
                StringBuilder sb = new StringBuilder(node.getValue() + "(");
                if (node.getLeft() != null) {
                    sb.append(node.getLeft().getValue() + ",");
                } else {
                    sb.append("null,");
                }
                if (node.getRight() != null) {
                    sb.append(node.getRight().getValue() + ")  ");
                } else {
                    sb.append("null)  ");
                }
                System.out.print(sb.toString());
                return;
            }
            if (currentLevel > level) {
                return;
            }
            printTreeLevel(node.getLeft(), level);
            printTreeLevel(node.getRight(), level);
        } finally {
            currentLevel--;
        }
    }

    public void dump() {
        currentLevel = -1;
        System.out.println("tree dump:");

        for (int i = 0; i <= getHeight(); i++) {
            printTreeLevel(root, i);
            System.out.println();
        }
    }

    public int getHeight() {
        if (root == null) return 0;
        return height(root);
    }

    private int height(BinaryNode node) {
        return heightInternal(node) - 1;
    }

    private int heightInternal(BinaryNode node) {
        if (node == null)  return 0;
        return 1 + Math.max(heightInternal(node.getLeft()), heightInternal(node.getRight()));
    }

    public int getSize() {
        return size(root);
    }

    private int size(BinaryNode node) {
        if(node == null) return 0;
        return 1 + size(node.getLeft()) + size(node.getRight());
    }
}
