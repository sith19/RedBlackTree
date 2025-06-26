/**
 * represents a binary search tree 
 * @param <T> type that the nodes in the tree hold must implement comparable interface
 * @param root - root of the tree object
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T>{
  protected BinaryTreeNode<T> root;
  /**
   * inserts a new value into the BST
   * @param data - data to be used for the new node to be inserted
   * @throws NUllPointerException if data is null
   */
  @Override
  public void insert(T data) throws NullPointerException {
    //check that data for node is valid
    if(data == null) {
      throw new NullPointerException("data is null");
    }
    BinaryTreeNode<T> newNode = new BinaryTreeNode<T>(data);
    //create the new node
    if(root == null) {
      root = newNode;
      return;
      //handles case where tree is empty
    }
    insertHelper(newNode, root);
    //call helper method for all other cases
    
    
    
  }
  /**
   * Performs the naive binary search tree insert algorithm to recursively
   * insert the provided newNode (which has already been initialized with a
   * data value) into the provided tree/subtree.  When the provided subtree
   * is null, this method does nothing. 
   * @param newNode - node to be inserted into the tree
   * @param subtree - node subtree is rooted at 
   */
  protected void insertHelper(BinaryTreeNode<T> newNode, BinaryTreeNode<T> subtree) {
    if(subtree == null) {
      return;
      //if given subtree is null do nothing
    }
    if(subtree.equals(newNode)) {
      if(subtree.left == null) {
        subtree.setChildLeft(newNode);
        newNode.setParent(subtree);
        //handle case where subtree is equal to node and left child is open
        
      }
    }
    else if(subtree.left == null && subtree.getData().compareTo(newNode.getData()) >= 0) {
      //check if left child is open and value is smaller or equal to root
      subtree.setChildLeft(newNode);
      newNode.setParent(subtree);
      //if it is insert it
      
    }
    else if(subtree.right == null && subtree.getData().compareTo(newNode.getData()) < 0) {
      //check if right child is open and value is bigger than root
      subtree.setChildRight(newNode);
      newNode.setParent(subtree);
      //if it is insert it
      
    }
    else if(subtree.getData().compareTo(newNode.getData()) >= 0) {
      insertHelper(newNode, subtree.left);
      //if value is smaller or equal to but left child isn't open repeat with left subtree 
    }
    else if(subtree.getData().compareTo(newNode.getData()) < 0) {
      insertHelper(newNode, subtree.right);
      //if value is bigger but right child isn't open repeat with right subtree
    }
  }
  /**
   * checks if a value appears in the tree at least once 
   * @param data - data that the node we look for contains
   * @return true if node with data exist, false otherwise
   */
  @Override
  public boolean contains(Comparable<T> data) {
    if(data == null) {
      return false;
      //prevents exceptions by providing null data
    }
    BinaryTreeNode<T> curr = root;
    //current node to check
    while(curr != null) {
      if(curr.getData().equals(data)) {
      //if data I look for is in the node return true
        return true;
      }
      else if(data.compareTo(curr.getData()) > 0) {
        curr = curr.right;
        //if data is bigger than current node go right
      }else {
        curr = curr.left;
        //if data is smaller or equal go left
      }
    }
    //if node is not found in search return false
    return false;
  }
  /**
   * counts number of nodes in the tree object
   * @return number of nodes in the tree
   */
  @Override
  public int size() {
    //call helper method
    return sizeHelper(root);
    
  }
  /**
   * counts the number of nodes in a subtree
   * @param root the root of the subtree
   * @return number of nodes in subtree
   */
  private int sizeHelper(BinaryTreeNode<T> root) {
    if(root == null) return 0;
    //if root of subtree is null return zero
    int leftTree = sizeHelper(root.left);
    //count all nodes on left subtree
    int rightTree = sizeHelper(root.right);
    //count all nodes on right subtree
    
    return 1 + rightTree + leftTree;
    //return 1 for the root + all nodes on left + all nodes on right
  }
  /**
   * Checks if the tree is empty
   * @returns true if tree is empty, false otherwise
   */
  @Override
  public boolean isEmpty() {
    
    if(root == null && this.size() == 0) {
    //the tree is only empty if root reference is null and size is 0
      return true;
    }
    return false;
  }
  /**
   * clears the BST of any data that is currently stored in it
   */
  @Override
  public void clear() {
    root.right = null;
    root.left = null;
    root = null;
    //sets all root parameters to null which in return removes all connections to other nodes
    
  }
  /**
   * Tests that the insert methods inserts data to the correct location in the tree 
   * uses an Integer tree
   * @return true if all tests pass, false otherwise
   */
  public boolean test1() {
    BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
    
    tree.insert(7);
    if(tree.root == null) return false;
    if(tree.root.getData() != 7) return false;
    //check that first node is added as the root
    
    tree.insert(8);
    if(tree.root.right == null) return false;
    if(tree.root.right.getData() != 8) return false;
    
    tree.insert(6);  
    if(tree.root.left == null) return false;
    if(tree.root.left.getData() != 6) return false;
    
    tree.insert(9);
    if(tree.root.right.right == null) return false;
    if(tree.root.right.right.getData() != 9) return false;
    
    tree.insert(7);
    if(tree.root.left.right == null) return false;
    if(tree.root.left.right.getData() != 7) return false;
    
    tree.insert(5);
    if(tree.root.left.left == null) return false;
    if(tree.root.left.left.getData() != 5) return false;
    
    //add different nodes and check that they are in the right position and hold the expected value
    return true;
  }
  /**
   * checks that the contains method works as intended and finds values that are both left and right
   * leaves as well as values stored in the middle of the tree and the root
   * uses a String tree
   * @return true if all tests pass, false otherwise
   */
  public boolean test2() {
    BinarySearchTree<String> tree = new BinarySearchTree<String>();
    //create the tree
    tree.insert("Bob");
    tree.insert("Ariel");
    tree.insert("Fred");
    tree.insert("Daisy");
    tree.insert("Calista");
    tree.insert("Luther");
    tree.insert("Luigi");
    tree.insert("Waluigi");
    //insert all values of the tree
    
    if(!tree.contains("Bob")) return false;
    //check that contains returns true for the root
    
    if(!tree.contains("Ariel")) return false;
    if(!tree.contains("Daisy")) return false;
    if(!tree.contains("Waluigi")) return false;
    if(!tree.contains("Luther")) return false;
    if(!tree.contains("Calista")) return false;
    //check leaves and values in the middle of the tree
    
    if(tree.contains("Mario")) return false;
    //check a value that is not in the tree
    
    return true;
  }
  /**
   * checks that the size() method returns the correct number of nodes as well as checking that
   * the clear() method removes all data in the tree using isEmpty()
   * uses a Character tree
   * @return true if all tests pass, false otherwise
   */
  public boolean test3() {
    BinarySearchTree<Character> tree = new BinarySearchTree<Character>();
    //create the new tree
    
    tree.insert('A');
    tree.insert('B');
    tree.insert('C');
    if(tree.size() != 3) return false;
    //check that size returns the correct value
    tree.clear();
    if(!tree.isEmpty()) return false;
    //use clear and check that the tree is empty
    
    tree.insert('D');
    tree.insert('F');
    tree.insert('H');
    tree.insert('L');
    if(tree.size() != 4) return false;
    //check that size returns the correct value
    tree.clear();
    if(!tree.isEmpty()) return false;
    //use clear and check that the tree is empty
    
    tree.insert('M');
    tree.insert('N');
    tree.insert('A');
    tree.insert('B');
    tree.insert('C');
    if(tree.size() != 5) return false;
    //check that size returns the correct value
    tree.clear();
    if(!tree.isEmpty()) return false;
    //use clear and check that the tree is empty
    
    
    return true;
  }
  /**
   * runs the test methods and outputs what value they return
   * @param args command line arguments Java uses to run the code
   */
  public static void main(String[] args) {
    BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
    //make a tree instance
    System.out.println(tree.test1() ? "Test 1 Passed": "Test 1 Fail");
    System.out.println(tree.test2() ? "Test 2 Passed": "Test 2 Fail");
    System.out.println(tree.test3() ? "Test 3 Passed": "Test 3 Fail");
    //call tester methods and print out their output
  }
}
