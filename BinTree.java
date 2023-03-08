
public class BinTree<E> {
Node root = null;
private String printString = "";
	// Accessor
    public Node getRoot() {  
    	return root;   
    }
    // Mutator
    public void setRoot(Node node) {
    	root = node; 
    }
    // search function that is used to be called from main
    public boolean search(Node node) {
    	return search(root, node);
    }
    // searches if the node being passed in has already existed within the BST
    public boolean search(Node root, Node node) {
    	// if the root is currently not null
        if (root != null) {
        	// if the root is equal to the node
        	if(root.compareTo(node) == 0) {
        		// return true
                return true;
        	}
        	// if the node is less than the root
            else if(node.compareTo(root) < 0) {
            	// continue to search through the left 
        		return search(root.getLeft(),node);
            }
            else { // if the node is greater than the root
            	// continue to search through the right
        		return search(root.getRight(),node);
            }
        	
        }
        return false;	            
    }
    
    // insert function to be used from main, as well as to set the root to a node if it was null
    public void insert(Node node) {
    	root = insert(root, node);
    }
    
    // inserts the node being passed into the binary search tree
    public Node insert(Node root, Node node)
    {
    	// if the root is null, make the node the root
        if (root == null)
        {
            root = node;
            return root;
        }
           
       	// if the node is less than the root
	    if(node.compareTo(root) < 0)
	    {
	    	// insert the node to the left of the root
	    	root.left(insert(root.getLeft(), node));
	            	
	    }
	    // if the node is greater than the root
	    else if(node.compareTo(root) > 0)
	    {
	    	// insert the node to the right of the root
	    	root.right(insert(root.getRight(), node));
	    }
        	
        
	    return root;
    }
    
    // No clue on how to make the delete function recursive
    
    
    public Node delete(Node node)
    {
        Node current = root, parent = root;

        while (current != null)
        {	
		//check if node is to the left
                if (node.compareTo(current) < 0)
                {
                        parent = current;
                        current = current.getLeft();
                }
		//check if node is to the right
                else if (node.compareTo(current) > 0)
                {
                        parent = current;
                        current = current.getRight();
                }
		//break loop when node found
                else
                        break;
        }

	//check if node found - loop could end when cur == null
        if (current != null)
        {
                //check if cur has 0 children
		if (current.getLeft() == null && current.getRight() == null)
                    //make link from parent = null
		    if (parent.getLeft() == current)
                            parent.left(null);
                    else
                            parent.right(null);
		//check if cur has 1 child on right
                else if(current.getLeft() == null)
                {
		    //link proper parent link to child of cur
                    if (parent.getLeft() == current)
                        parent.left(current.getRight());
                    else
                        parent.right(current.getRight());
		    //disconnect cur from tree
                    current.right(null);
                }
		//check if cur has 1 child on left
                else if (current.getRight() == null)
                {
		    //link proper parent link to child of cur
                    if (parent.getRight() == current)
                        parent.right(current.getLeft());
                    else
                        parent.left(current.getLeft());
		    //disconnect cur from tree
                    current.left(null);
                }
		//cur has 2 children
                else
                {
		    //parent will hold position where node will be copied
                    parent = current;
		    //move cur to left child
                    
                    
                    current = current.getLeft();
                    Node pre = null;
		    //move cur down right branch of left subtree
                    while (current.getRight() != null)
                    {
                        pre = current;
                        current = current.getRight();
                    }
                    
		    //copy content from node to node
                    parent.setPayload(current.getPayload());
		    //call delete again to delete the node that was copied
                    pre.right(current.getLeft());
                    current.left(null);
                }
                        

        }
        return current;
    }
    
    // Inorder traversal from the root
    public void inorder() {
    	inorder(root);
    }
    // Inorder traversal from a subtree
    public void inorder(Node root) {
    	 if (root == null) return;
    	 inorder(root.left);
    	 printString += root.toString() + " ";
    	 inorder(root.right);
    }
    // returns the string
    public String getString() {
    	return printString;
    }
    
}
