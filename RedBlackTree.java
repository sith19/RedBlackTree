import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T>{
  /**
   * inserts a new node in to the red black tree, overrides BSTRotation insertion method
   * @param data - data provided for new node
   * @throws NullPointerException if data provided is null
   */
  @Override
  public void insert(T data) throws NullPointerException {
    if(data == null) {
       throw new NullPointerException("data is null");
    }
    //handles the case where data provided is null
    RBTNode<T> newNode = new RBTNode<T>(data);
    //create new node to insert
    newNode.isRed = true;
    //color it red
    if(this.isEmpty()) {
      this.root = newNode;
      ((RBTNode<T>)this.root).isRed = false;
      return;
      //handles case where tree is empty
    }
    super.insertHelper(newNode, root);
    //use normal BST algorithm
    ensureRedProperty(newNode);
    //use repair operation
    
  }
  /**
   * Checks if a new red node in the RedBlackTree causes a red property violation
   * by having a red parent. If this is not the case, the method terminates without
   * making any changes to the tree. If a red property violation is detected, then
   * the method repairs this violation and any additional red property violations
   * that are generated as a result of the applied repair operation.
   * @param newNode a newly inserted red node, or a node turned red by previous repair
   */
  protected void ensureRedProperty(RBTNode<T> newNode) {
      RBTNode<T> grandparent;
      RBTNode<T> parent;
      //create initial variables for parent and grandparent refrences
      if(newNode.parent() == null) {
        return;
      }
      //if recursive call to the root quit
      else if(!(newNode.parent().isRed) ) {
        return;
        //if there is no violation quit
      }else {
        if(newNode.parent().isRightChild()) {
          //check if parent is a right child
          if(newNode.parent().parent().childLeft() == null || !newNode.parent().parent().childLeft().isRed) {
          //if parent is a right child check if aunt is black
            if(!newNode.isRightChild()) {
              rotate(newNode, newNode.parent());
              //if parent and newNode don't have a right child relationship rotate to align them
              parent = newNode;
              grandparent = newNode.parent();
              //make references for next rotation considering new positioning
            }else {
              
              parent = newNode.parent();
              grandparent = newNode.parent().parent();
              //otherwise create references for rotation
            }
            boolean grandColor = grandparent.isRed;
            boolean parentColor = parent.isRed;
            //store the colors of the nodes to be rotated
            rotate(parent, grandparent);
            //rotate the nodes
            grandparent.isRed = parentColor;
            parent.isRed = grandColor;
            //swap the colors
            if(((RBTNode<T>)this.root).isRed) {
              ((RBTNode<T>)this.root).isRed = false;
            }
            //if root color is affected color it black
          }
          else if(newNode.parent().parent().childLeft().isRed) {
            //if aunt is red 
            grandparent = newNode.parent().parent();
            grandparent.isRed = true;
            grandparent.childLeft().isRed = false;
            grandparent.childRight().isRed = false;
            //recolor the grandparent and its two children
            if(((RBTNode<T>)this.root).isRed) {
              ((RBTNode<T>)this.root).isRed = false;
            }
            //if it affects the root color it black
            ensureRedProperty(grandparent);
            //recursive call with grandparent to solve violations that can be caused higher in the tree
            
          }
        }else {
            if(newNode.parent().parent().childRight() == null || !newNode.parent().parent().childRight().isRed) {
              //check if aunt is black
              if(newNode.isRightChild()) {
                rotate(newNode, newNode.parent());
                //if parent and new node don't have a left child relationship rotate to align them
                parent = newNode;
                grandparent = newNode.parent();
                //make references for next rotation considering new positioning
              }else {
                parent = newNode.parent();
                grandparent = newNode.parent().parent();
                //otherwise make references for rotation
                
              }
              boolean grandColor = grandparent.isRed;
              boolean parentColor = parent.isRed;
              //store the colors of the nodes to be rotated
              rotate(parent, grandparent);
              //rotate the nodes
              grandparent.isRed = parentColor;
              parent.isRed = grandColor;
              //swap the colors 
              if(((RBTNode<T>)this.root).isRed) {
                ((RBTNode<T>)this.root).isRed = false;
              }
              //if root color is affected color it black
            }
            else if(newNode.parent().parent().childRight().isRed) {
              //if aunt is red
              grandparent = newNode.parent().parent();
              grandparent.isRed = true;
              grandparent.childLeft().isRed = false;
              grandparent.childRight().isRed = false;
              //recolor grandparent and two children
              if(((RBTNode<T>)this.root).isRed) {
                ((RBTNode<T>)this.root).isRed = false;
              }
              //if root is recolored color it black
              ensureRedProperty(grandparent);
              //recursive call to solve violation that can be caused higher in the tree
              
            }
        }
      }
  }
  /**
  * Tests the rotation and color swap that is done if child has a black aunt
  */
  @Test
  public void testBlackAuntRepair() {
    RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
    //create a valid red black tree 
    tree.root = new RBTNode<Integer>(80);
    ((RBTNode<T>)tree.root).isRed = false;
    tree.root.right = new RBTNode<Integer>(100);
    tree.root.right.up = tree.root;
    ((RBTNode<T>)tree.root.right).isRed = false;
    tree.root.left = new RBTNode<Integer>(70);
    tree.root.left.up = tree.root;
    tree.root.left.right = new RBTNode<Integer>(75);
    tree.root.left.right.up = tree.root.left;
    ((RBTNode<T>)tree.root.left.right).isRed = false;
    tree.root.left.left = new RBTNode<Integer>(65);
    tree.root.left.left.up = tree.root.left;
    ((RBTNode<T>)tree.root.left.left).isRed = false;
    tree.root.right.right = new RBTNode<Integer>(110);
    tree.root.right.right.up = tree.root.right;
    //insert a node that will cause a violation with a black aunt
    tree.insert(105);
    //check that repair operation and rotations were done correctly and nodes kept their position and color
    if(!tree.root.getData().equals(80) || ((RBTNode<T>)tree.root).isRed) {
      Assertions.fail("root doesn't have correct data or color");
    }
    if(!tree.root.childRight().getData().equals(105) || ((RBTNode<T>)tree.root.childRight()).isRed) {
      Assertions.fail("root right child doesn't have correct data or color");
    }
    if(!tree.root.childLeft().getData().equals(70) || !((RBTNode<T>)tree.root.childLeft()).isRed) {
      Assertions.fail("root left child doesn't have correct data or color");
    }
    if(!tree.root.childRight().childRight().getData().equals(110) || !((RBTNode<T>)tree.root.childRight().childRight()).isRed) {
      Assertions.fail("right subtree right leaf doesn't have correct data or color");
    }
    if(!tree.root.childLeft().childRight().getData().equals(75) || ((RBTNode<T>)tree.root.childLeft().childRight()).isRed) {
      Assertions.fail("left subtree right leaf doesn't have correct data or color");
    }
    if(!tree.root.childRight().childLeft().getData().equals(100) || !((RBTNode<T>)tree.root.childRight().childLeft()).isRed) {
      Assertions.fail("right subtree left leaf doesn't have correct data or color");
    }
    if(!tree.root.childLeft().childLeft().getData().equals(65) || ((RBTNode<T>)tree.root.childLeft().childLeft()).isRed) {
      Assertions.fail("left subtree left leaf doesn't have correct data or color");
    }
    
  }
  /**
   * Tests the recolor repair that is used if red child has a red aunt
   */
  @Test
  public void testRedAuntRepair() {
    RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
    //generate a tree without violation and insert a node that causes violation with red aunt
    tree.insert(80);
    tree.insert(85);
    tree.insert(75);
    tree.insert(90);
    //check that positions and colors are appropriate to correct repair option
    if(!tree.root.getData().equals(80) || ((RBTNode<T>)tree.root).isRed) {
      Assertions.fail("root doesn't have correct data or color");
    }
    if(!tree.root.childRight().getData().equals(85) || ((RBTNode<T>)tree.root.childRight()).isRed) {
      Assertions.fail("root right child doesn't have correct data or color");
    }
    if(!tree.root.childLeft().getData().equals(75) || ((RBTNode<T>)tree.root.childLeft()).isRed) {
      Assertions.fail("root left child doesn't have correct data or color");
    }
    if(!tree.root.childRight().childRight().getData().equals(90) || !((RBTNode<T>)tree.root.childRight().childRight()).isRed) {
      Assertions.fail("right subtree right leaf doesn't have correct data or color");
    }
    //clear the tree
    tree.clear();
    //build a larger valid red black tree to test recolor
    tree.root = new RBTNode<>(80);
    ((RBTNode<T>)tree.root).isRed = false;
    tree.root.left = new RBTNode<>(70);
    tree.root.left.up = tree.root;
    ((RBTNode<T>)tree.root.left).isRed = true; 
    tree.root.right = new RBTNode<>(100);
    tree.root.right.up = tree.root;
    ((RBTNode<T>)tree.root.right).isRed = false; 
    tree.root.left.left = new RBTNode<>(65);
    tree.root.left.left.up = tree.root.left;
    ((RBTNode<T>)tree.root.left.left).isRed = false; 
    tree.root.left.right = new RBTNode<>(75);
    tree.root.left.right.up = tree.root.left;
    ((RBTNode<T>)tree.root.left.right).isRed = false; 
    tree.root.right.right = new RBTNode<>(110);
    tree.root.right.right.up = tree.root.right;
    ((RBTNode<T>)tree.root.right.right).isRed = true; 
    tree.root.right.left = new RBTNode<>(90);
    tree.root.right.left.up = tree.root.right;
    ((RBTNode<T>)tree.root.right.left).isRed = true;
    //insert node that will cause violation
    tree.insert(85);
    //check position and colors to make sure that recursive call did not change anything
    if (!tree.root.getData().equals(80) || ((RBTNode<Integer>)tree.root).isRed) {
      Assertions.fail("Root doesn't have correct data or color");
    }
    if (!tree.root.childLeft().getData().equals(70) || !((RBTNode<Integer>)tree.root.childLeft()).isRed) {
        Assertions.fail("Root left child doesn't have correct data or color");
    }
    if (!tree.root.childRight().getData().equals(100) || !((RBTNode<Integer>)tree.root.childRight()).isRed) {
        Assertions.fail("Root right child doesn't have correct data or color");
    }
    if (!tree.root.childLeft().childLeft().getData().equals(65) || ((RBTNode<Integer>)tree.root.childLeft().childLeft()).isRed) {
        Assertions.fail("Left subtree left child doesn't have correct data or color");
    }
    if (!tree.root.childLeft().childRight().getData().equals(75) || ((RBTNode<Integer>)tree.root.childLeft().childRight()).isRed) {
        Assertions.fail("Left subtree right child doesn't have correct data or color");
    }
    if (!tree.root.childRight().childLeft().getData().equals(90) || ((RBTNode<Integer>)tree.root.childRight().childLeft()).isRed) {
        Assertions.fail("Right subtree left child doesn't have correct data or color");
    }
    if (!tree.root.childRight().childRight().getData().equals(110) || ((RBTNode<Integer>)tree.root.childRight().childRight()).isRed) {
        Assertions.fail("Right subtree right child doesn't have correct data or color");
    }
    if (!tree.root.childRight().childLeft().childLeft().getData().equals(85) || !((RBTNode<Integer>)tree.root.childRight().childLeft().childLeft()).isRed) {
        Assertions.fail("Right subtree left child's left child doesn't have correct data or color");
    }
  }
  /**
   * tests Question 5 insertion from the RBT insertion quiz
   */
  @Test
  public void testQuizQuestion(){
    RedBlackTree<Character> tree = new RedBlackTree<Character>();
    //create a tree with the same insertion sequence as the quiz
    tree.insert('I');
    tree.insert('E');
    tree.insert('A');
    tree.insert('J');
    tree.insert('B');
    //check that ending tree has expected positions and colors
    if(!tree.root.getData().equals('E') || ((RBTNode<T>)tree.root).isRed) {
      Assertions.fail("root doesn't have correct data or color");
    }
    if(!tree.root.childRight().getData().equals('I') || ((RBTNode<T>)tree.root.childRight()).isRed) {
      Assertions.fail("root right child doesn't have correct data or color");
    }
    if(!tree.root.childLeft().getData().equals('A') || ((RBTNode<T>)tree.root.childLeft()).isRed) {
      Assertions.fail("root left child doesn't have correct data or color");
    }
    if(!tree.root.childRight().childRight().getData().equals('J') || !((RBTNode<T>)tree.root.childRight().childRight()).isRed) {
      Assertions.fail("right subtree leaf doesn't have correct data or color");
    }
    if(!tree.root.childLeft().childRight().getData().equals('B') || !((RBTNode<T>)tree.root.childLeft().childRight()).isRed) {
      Assertions.fail("left subtree leaf doesn't have correct data or color");
    }
  }
}
