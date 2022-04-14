import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

class Node
{
	int item;
	int key;
	Node next;
	ReentrantLock lock;

	public Node (Integer item)
	{
		this.item = item;
		this.key = item.hashCode();
		this.lock = new ReentrantLock();
	}
}

class Set
{
	int numAdd;
	static int numRemove;
	static Node head;

	public Set()
	{
		this.head = new Node(Integer.MIN_VALUE);
		numAdd = 0;
		numRemove = 0;
		head.next = new Node(Integer.MIN_VALUE+1);
	}

	public boolean add(Integer item) 
	{
		int key = item.hashCode();
		while (true)
		{
			Node pred = head;
			Node curr = pred.next;
			while (curr.key <= key && curr.next != null) 
			{
				pred = curr; curr = curr.next;
			}
			pred.lock.lock(); curr.lock.lock();
			try 
			{
				if (validate(pred, curr)) 
				{
					if (curr.key == key) 
					{
						return false;
					} 
					else 
					{
						Node node = new Node(item);
						node.next = curr;
						pred.next = node;
						numAdd++;
						return true;
					}
				}
			} 
			finally 
			{
				pred.lock.unlock(); curr.lock.unlock();
			}
		}
	}

	public boolean remove(Integer item) 
	{
		int key = item.hashCode();
		while (true) 
		{
			Node pred = head;
			Node curr = pred.next;
			while (curr.key < key && curr.next != null) 
			{
				pred = curr; curr = curr.next;
			}
			pred.lock.lock(); curr.lock.lock();
			try 
				{
				if (validate(pred, curr)) 
				{
					if (curr.key == key) 
					{
						pred.next = curr.next;
						numRemove++;
						return true;
					} 
					else 
					{
						return false;
					}
				}
			} 
			finally 
			{
				pred.lock.unlock(); curr.lock.unlock();
			}
		}
	}
	
	public boolean contains(Integer item) 
	{
		int key = item.hashCode();
		while (true) 
		{
			Node pred = this.head; // sentinel node;
			Node curr = pred.next;
			while (curr.key < key && curr.next != null) 
			{
				pred = curr; curr = curr.next;
			}
			try 
			{
				pred.lock.lock(); curr.lock.lock();
				if (validate(pred, curr)) 
				{
					return (curr.key == key);
				}
			} 
			finally 
			{ // always unlock
				pred.lock.unlock(); curr.lock.unlock();
			}
		}
	}

	private boolean validate(Node pred, Node curr) 
	{
		if (pred == null || curr == null)
			return false;
		Node node = head;

		while (node != null && node.key <= pred.key) 
		{
			if (node == pred)
				return pred.next == curr;
			node = node.next;
		}
		return false;
	}
}

class bpp
{
	static int goal = 500000;
	static Set LinkedList;
	
	public static class regThread extends Thread
	{
		private static AtomicInteger counter = new AtomicInteger(0);
		String name;

		public regThread(String name)
		{
			this.name = name;
		}
		
		@Override
		public void run()
		{
			int cur = 0;
			while (((cur = counter.getAndIncrement()) < goal))
			{
				Integer item = cur;
				LinkedList.add(item);
				LinkedList.remove(item);
			}
		}
	}

	public static void main(String[] args)
	{
		System.out.println("This is a simulation of the first problem of HW3, The Birthday Presents Party:");
		
		LinkedList = new Set();

		regThread t1 = new regThread("Thread 1");
		regThread t2 = new regThread("Thread 2");
		regThread t3 = new regThread("Thread 3");
		regThread t4 = new regThread("Thread 4");

		t1.start();
		t2.start();
		t3.start();
		t4.start();

		try
		{
			t1.join();
			t2.join();
			t3.join();
			t4.join();
		}
		catch (Exception e)
		{      
		}

		System.out.println(LinkedList.numAdd + " Gifts were hooked onto the special chain!");
		System.out.println(LinkedList.numRemove + " Thank you cards written!");

	}
	
}