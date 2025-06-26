
public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T>{
  /**
   * Performs the rotation operation on the provided nodes within this tree.
   * When the provided child is a left child of the provided parent, this
   * method will perform a right rotation. When the provided child is a right
   * child of the provided parent, this method will perform a left rotation.
   * When the provided nodes are not related in one of these ways, this
   * method will either throw a NullPointerException: when either reference is
   * null, or otherwise will throw an IllegalArgumentException.
   *
   * @param child is the node being rotated from child to parent position 
   * @param parent is the node being rotated from parent to child position
   * @throws NullPointerException when either passed argument is null
   * @throws IllegalArgumentException when the provided child and parent
   *     nodes are not initially (pre-rotation) related that way
   */
  protected void rotate(BinaryTreeNode<T> child, BinaryTreeNode<T> parent)
      throws NullPointerException, IllegalArgumentException {
      //check if either of the provided refrence is null
      if(child == null || parent ==  null) {
        throw new NullPointerException("one of the nodes is null");
      }
      //check which type of rotation
      if(parent.left != null && parent.left.equals(child)) {
        rightRotation(child, parent);
      }else if(parent.right != null && parent.right.equals(child)){
        leftRotation(child, parent);
      }else {
        throw new IllegalArgumentException("nodes are not in parent-child relationship");
        //if there is no parent child relation throw exception
      }
  }
  /**
   * perform left rotation on a BST
   * @param child is the provided right child that will be rotated to be the parent
   * @param parent is the provided parent that will be rotated to be the child
   */
  private void leftRotation(BinaryTreeNode<T> child, BinaryTreeNode<T> parent) {
    child.up = null;
    //handles grandparent refrence
    if(parent.up != null) {
      child.up = parent.up;
      if(!parent.isRightChild()) {
        parent.up.left = child;
      }else {
        parent.up.right = child;
      }
    }
    parent.up = child;
    parent.right = null;
    //handle if child has left child
    if(child.left != null) {
      parent.right = child.left;
      child.left.up = parent;
    }
    child.left = parent;
    if(parent.equals(root)) {
      root = child;
    }
  }
  /**
   * performs right rotation on a BST
   * @param is the provided left child that will be rotated to be the parent
   * @param parent is the provided parent that will be rotated to be the child
   */
  private void rightRotation(BinaryTreeNode<T> child, BinaryTreeNode<T> parent) {
    child.up = null;
    //handles grandparent refrence
    if(parent.up != null) {
      child.up = parent.up;
      if(!parent.isRightChild()) {
        parent.up.left = child;
      }else {
        parent.up.right = child;
      }
      
    }
    parent.up = child;
    parent.left = null;
    //handles if child has a right child
    if(child.right != null) {
      parent.left = child.right;
      child.right.up = parent;
    }
    child.right = parent;
    if(parent.equals(root)) {
      root = child;
    }
    
  }
  /**
   * tests the left and right rotation of the rotate method
   * @return true if all tests pass, false other wise
   */
  public boolean test1() {
    //build the tree
    BSTRotation<Integer> tree = new BSTRotation<Integer>();    BinaryTreeNode<Integer> node = new BinaryTreeNode<Integer>(30);
    BinaryTreeNode<Integer> node1 = new BinaryTreeNode<Integer>(40);
    BinaryTreeNode<Integer> node2 = new BinaryTreeNode<Integer>(20);
    BinaryTreeNode<Integer> node3 = new BinaryTreeNode<Integer>(35);
    BinaryTreeNode<Integer> node4 = new BinaryTreeNode<Integer>(50);
    BinaryTreeNode<Integer> node5 = new BinaryTreeNode<Integer>(10);
    tree.root = node;
    node1.up = tree.root;
    tree.root.right = node1;
    node2.up = tree.root;
    tree.root.left = node2;  
    node1.left = node3; 
    node3.up = node1;
    node4.up = node1;
    node1.right = node4; 
    tree.rotate(tree.root.right, tree.root);
    //perform a left rotation
    if(!tree.root.equals(node1)) return false;
    if(!tree.root.left.equals(node)) return false;
    if(!tree.root.right.equals(node4)) return false;
    if(!tree.root.left.right.equals(node3)) return false;
    //verify everything is in expected position
    tree.root = null;
    node = new BinaryTreeNode<Integer>(30);
    node1 = new BinaryTreeNode<Integer>(40);
    node2 = new BinaryTreeNode<Integer>(20);
    node3 = new BinaryTreeNode<Integer>(35);
    node4 = new BinaryTreeNode<Integer>(50);
    node5 = new BinaryTreeNode<Integer>(10);
    //clears the tree
    tree.root = node3;
    tree.root.left =  node2;
    node2.up = tree.root;
    node2.left = node5;
    node5.up = node2;
    tree.root.right = node1;
    node1.up = tree.root;
    tree.rotate(tree.root.left,tree.root);
    //perform right rotation
    if(!tree.root.equals(node2)) return false;
    if(!tree.root.left.equals(node5)) return false;
    if(!tree.root.right.equals(node3)) return false;
    if(!tree.root.right.right.equals(node1)) return false;
    //verify everything is in expected position
    tree.root = null;
    node = new BinaryTreeNode<Integer>(30);
    node1 = new BinaryTreeNode<Integer>(40);
    node2 = new BinaryTreeNode<Integer>(20);
    node3 = new BinaryTreeNode<Integer>(35);
    node4 = new BinaryTreeNode<Integer>(50);
    node5 = new BinaryTreeNode<Integer>(10);
    //clears tree
    tree.root = node;
    node.right = node1;
    node1.up = node;
    node.left = node2;
    node2.up = node;

    // Perform a left rotation on node1 (40)
    tree.rotate(node1, node);

    // Verify parent pointers
    if (node1.up != null) return false;
    if (!node.up.equals(node1)) return false; 
    if (!node2.up.equals(node)) return false; 
    
    
    return true;
  }
  /** 
   * tests exception throwing of rotate() method and performs rotations not on the root node
   * @return true if all tests pass false otherwise
   */
  public boolean test2() {
    //build the tree
    BSTRotation<Character> tree = new BSTRotation<Character>();
    BinaryTreeNode<Character> node = new BinaryTreeNode<Character>('A');
    BinaryTreeNode<Character> node1 = new BinaryTreeNode<Character>('B');
    BinaryTreeNode<Character> node2 = new BinaryTreeNode<Character>('C');
    BinaryTreeNode<Character> node3 = new BinaryTreeNode<Character>('D');
    BinaryTreeNode<Character> node4 = new BinaryTreeNode<Character>('E');
    BinaryTreeNode<Character> node5 = new BinaryTreeNode<Character>('F');
    BinaryTreeNode<Character> node6 = new BinaryTreeNode<Character>('G');
    // tests that method throws NullPointerException properly
    try {
      tree.rotate(node, null);
      return false;
    }catch(NullPointerException e) {
      
    }catch(Exception e) {
      return false;
    }
    try {
      tree.rotate(null, node1);
      return false;
    }catch(NullPointerException e) {
      
    }catch(Exception e) {
      return false;
    }
    try {
      tree.rotate(null, null);
      return false;
    }catch(NullPointerException e) {
      
    }catch(Exception e) {
      return false;
    }
    //tests that method throws IllegalArgumentException properly
    try {
      tree.rotate(node, node1);
      return false;
    }catch(IllegalArgumentException e) {
      
    }catch(Exception e) {
      return false;
    }
    tree.root = node2;
    tree.root.left = node;
    node.up = tree.root;
    node.right = node1;
    node1.up = node;
    tree.rotate(node1, node);
    if(!tree.root.equals(node2)) return false;
    if(!tree.root.left.equals(node1)) return false;
    if(!tree.root.left.left.equals(node)) return false;
    //add new nodes to tree
    tree.root.right = node4;
    node4.up = tree.root;
    node4.left = node3;
    node3.up = node4;
    node4.right = node5;
    node5.up = node4;
    node5.right = node6;
    node6.up = node5;
    //do interior rotation
    tree.rotate(node5, node4);
    //verify positions of node
    if(!tree.root.equals(node2)) return false;
    if(!tree.root.right.equals(node5)) return false;
    if(!tree.root.right.left.equals(node4)) return false;
    if(!tree.root.right.right.equals(node6)) return false;
    if(!tree.root.right.left.left.equals(node3)) return false;
    tree.rotate(node3, node4);
    if(!tree.root.right.left.equals(node3)) return false;
    if(!tree.root.right.left.right.equals(node4)) return false;
    tree.rotate(node3, node5);
    if(!tree.root.right.equals(node3)) return false;
    if(!tree.root.right.right.equals(node5)) return false;
    if(!tree.root.right.right.right.equals(node6)) return false;
    if(!tree.root.right.right.left.equals(node4)) return false;
    
    
    return true;
  }
  /**
   * tests rotation with varying number of children 0,1,2,3
   * @return true if all tests pass, false otherwise
   */
  public boolean test3() {
    //build the tree
    BSTRotation<Character> tree = new BSTRotation<Character>();
    BinaryTreeNode<Character> node1 = new BinaryTreeNode<Character>('B');
    BinaryTreeNode<Character> node2 = new BinaryTreeNode<Character>('C');
    BinaryTreeNode<Character> node3 = new BinaryTreeNode<Character>('D');
    BinaryTreeNode<Character> node4 = new BinaryTreeNode<Character>('E');
    BinaryTreeNode<Character> node5 = new BinaryTreeNode<Character>('F');
    tree.root = node3;
    node3.left = node2;
    node2.up = tree.root;
    //do rotation with 0 children
    tree.rotate(tree.root.left, tree.root);
    //verify node positions
    if(tree.root.left != null) return false;
    if(!tree.root.equals(node2)) return false;
    if(!tree.root.right.equals(node3)) return false;
    //add new child to tree
    tree.root.left = node1;
    node1.up = tree.root;
    //do rotation with 1 child
    tree.rotate(tree.root.right, tree.root);
    if(!tree.root.equals(node3)) return false;
    if(!tree.root.left.equals(node2)) return false;
    if(!tree.root.left.left.equals(node1)) return false;
    //add another child node
    tree.root.right = node4;
    node4.up = tree.root;
    //do rotation with 2 children
    tree.rotate(tree.root.left, tree.root);
    //verify positions
    if(!tree.root.equals(node2)) return false;
    if(!tree.root.left.equals(node1)) return false;
    if(!tree.root.right.equals(node3)) return false;
    if(!tree.root.right.right.equals(node4)) return false;
    //rotate so tree is in valid shape
    tree.rotate(node4, node3);
    tree.root.right.right = node5;
    node5.up = tree.root.right.right;
    //do rotation with 3 children
    tree.rotate(node4, node2);
    //verify positions
    if(!tree.root.equals(node4)) return false;
    if(!tree.root.left.equals(node2)) return false;
    if(!tree.root.right.equals(node5)) return false;
    if(!tree.root.left.right.equals(node3)) return false;
    if(!tree.root.left.left.equals(node1)) return false;
    return true;
  }
  /**
   * main method to run all testers
   * @param args command line arguments, not used in method
   */
  public static void main(String[] args) {
    BSTRotation<Integer> tree = new BSTRotation<Integer>();
    System.out.println(tree.test1() ? " test 1 passed" : "test 1 failed");
    System.out.println(tree.test2() ? "test 2 passed" : "test 2 failed");
    System.out.println(tree.test3() ? "test 3 passed" : "test 3 failed");
  }
}
