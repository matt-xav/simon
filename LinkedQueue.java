package queue;

public class LinkedQueue<T> implements QueueInterface<T> 
{
	
	private Node frontNode;
	private Node backNode;
	
	public LinkedQueue()
	{
		frontNode = null;
		backNode = null;
	}
	
	@Override
	public void enqueue(T newEntry) 
	{
		Node newNode = new Node(newEntry,null);
		if(isEmpty())
			frontNode = newNode;
		else
			backNode.setNextNode(newNode);
		backNode = newNode;
	}

	@Override
	public T dequeue() 
	{
		T front = getFront();
		frontNode.setData(null);
		frontNode = frontNode.getNextNode();
		if(frontNode == null)
			backNode = null;
		return front;
	}

	@Override
	public T getFront() 
	{
		if(isEmpty())
			throw new EmptyQueueException();
		else
			return frontNode.getData();
	}

	@Override
	public boolean isEmpty() 
	{
		return frontNode == null;
	}

	@Override
	public void clear() 
	{
		while(!isEmpty())
			dequeue();
	}
	
	public T[] toArray()
	{
		@SuppressWarnings("unchecked")
		T[] tempQueue = (T[]) new Object[getSize()];
		Node currentNode = frontNode;
		int i = 0;
		while(currentNode != null)
		{
			tempQueue[i] = currentNode.getData();
			currentNode = currentNode.getNextNode();
			i++;
		}
		return tempQueue;
	}
	
	public int getSize()
	{
		Node currentNode = frontNode;
		int counter = 0;
		while(currentNode != null)
		{
			counter++;
			currentNode = currentNode.getNextNode();
		}
		return counter;
	}
	
	@Override
	public boolean equals(QueueInterface<T> comparison) {
		if(this.getSize()==comparison.getSize()){
			T[] temp = this.toArray();
			T[] compTemp = comparison.toArray();
			for(int i=0; i < temp.length; i++){
				if(!temp[i].equals(compTemp[i])){
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}
	
	private class Node
	{
		private T data;
		private Node next;
		
		public Node()
		{
			this(null);
		}
		
		public Node(T data)
		{
			this.data = data;
			this.next = null;
		}
		
		public Node(T data, Node next)
		{
			this.data = data;
			this.next = next;
		}
		
		public T getData()
		{
			return data;
		}
		
		public void setData(T newData)
		{
			data = newData;
		}
		
		public Node getNextNode()
		{
			return next;
		}
		
		public void setNextNode(Node newNext)
		{
			next = newNext;
		}
	}

	@Override
	public QueueInterface<T> copy() {
		QueueInterface<T> myQueue = new LinkedQueue<T>();
		T[] temp = this.toArray();
		for(int i=0; i < temp.length; i++){
			myQueue.enqueue(temp[i]);
		}
		return myQueue;
	}

	
}