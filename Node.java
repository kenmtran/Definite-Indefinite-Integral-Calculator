
public class Node<E extends Comparable<E>> implements Comparable<Node<E>> {
	E payload; // represents the object's data
	Node<E> left;  // represents the node to the left of the current node
	Node<E> right; // represents the node to the right of the current node
	// initializes the payload to null
	public Node() {
		payload = null;
	}
	// initializes the node's payload to the payload being passed in
	public Node(E payload) {
		this.payload = payload;
	}
	// returns the payload
	public E getPayload() {
		return payload;
	}
	// sets the payload
	public void setPayload(E payload){
		this.payload = payload;
	}
	// returns whatever the left node is
	public Node<E> getLeft() {
		return left;
	}
	// sets the node to the left of the current node
	public void left(Node<E> left) {
		this.left = left;
	}
	// returns whatever the right node is
	public Node<E> getRight() {
		return right;
	}
	// sets the node to the right of the current node
	public void right(Node<E> right) {
		this.right = right;
	}
	@Override
	// overridden compareTo function that calls the compareTo function of the payload's
	public int compareTo(Node<E> o) {

		return payload.compareTo(o.payload);
	}
	@Override
	// overridden toString function that calls the toString function of the payload's
	public String toString() {
		return payload.toString();
	}
}
