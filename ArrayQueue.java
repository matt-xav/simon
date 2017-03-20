package queue;

import java.util.Arrays;

public class ArrayQueue<T> implements QueueInterface<T>
{
	private T[] queue;
	private int frontIndex;
	private int backIndex;
	private boolean initialized = false;
	private static final int DEFAULT_CAPACITY = 50;
	private static final int MAX_CAPACITY = 10000;
	
	public ArrayQueue()
	{
		this(DEFAULT_CAPACITY);
	}
	
	public ArrayQueue(int capacity)
	{
		checkCapacity(capacity);
		
		@SuppressWarnings("unchecked")
		T[] tempQueue = (T[]) new Object[capacity + 1];
		queue = tempQueue;
		frontIndex = 0;
		backIndex = capacity;
		initialized = true;
	}
	
	private void checkCapacity(int capacity)
	{
		if(capacity > MAX_CAPACITY)
			throw new IllegalStateException("Attempt to create a queue whose " + 
											"capacity exceeds allowed maximum of " + MAX_CAPACITY);										
	}
	
	private void checkInitialization()
	{
		if (!initialized)
			throw new SecurityException("ArrayQueue object is not initialized properly.");
	}
	
	private void ensureCapacity()
	{
		if(frontIndex == ((backIndex + 2) % queue.length))
		{
			T[] oldQueue = queue;
			int oldSize = oldQueue.length;
			int newSize = oldSize * 2;
			checkCapacity(newSize);
			
			@SuppressWarnings("unchecked")
			T[] tempQueue = (T[]) new Object[newSize];
			queue = tempQueue;
			for( int i = 0; i < oldSize - 1; i++)
			{
				queue[i] = oldQueue[frontIndex];
				frontIndex = (frontIndex + 1) % oldSize;
			}
			
			frontIndex = 0;
			backIndex = oldSize - 2;
		}
	}
	
	@Override
	public void enqueue(T newEntry) 
	{
		checkInitialization();
		ensureCapacity();
		backIndex = (backIndex + 1) % queue.length;
		queue[backIndex] = newEntry;
	}

	@Override
	public T dequeue() 
	{
		checkInitialization();
		if(isEmpty())
			throw new EmptyQueueException();
		else
		{
			T front = queue[frontIndex];
			queue[frontIndex]=null;
			frontIndex = (frontIndex + 1) % queue.length;
			return front;
		}
	}

	@Override
	public T getFront() 
	{
		if(isEmpty())
			throw new EmptyQueueException();
		else
		{
			return queue[frontIndex];
		}
	}

	@Override
	public boolean isEmpty() 
	{
		return frontIndex == ((backIndex + 1) % queue.length);
	}

	@Override
	public void clear() 
	{
		while(!isEmpty())
			dequeue();
		
	}
	
	public T[] toArray()
	{
		int size = (backIndex - frontIndex)%queue.length + 1;
		int currentIndex = frontIndex;
		@SuppressWarnings("unchecked")
		T[] tempQueue = (T[]) new Object[size];
		for(int i = 0; i < size ; i++)
		{
			tempQueue[i] = queue[(currentIndex + i)%queue.length];
		}
		return tempQueue;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(QueueInterface<T> comparison) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public QueueInterface<T> copy() {
		// TODO Auto-generated method stub
		return null;
	}
}