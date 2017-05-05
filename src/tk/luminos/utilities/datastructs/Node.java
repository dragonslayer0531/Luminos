package tk.luminos.utilities.datastructs;

class Node<T> {
	
	protected Node<T> next;
	protected Node<T> last;
	
	protected T data;
	
	Node(T data) {
		this.data = data;
	}
	
	Node(T data, Node<T> next) {
		this.data = data;
		this.next = next;
	}
	
	Node(T data, Node<T> last, Node<T> next) {
		this.data = data;
		this.last = last;
		this.next = next;
	}

}
