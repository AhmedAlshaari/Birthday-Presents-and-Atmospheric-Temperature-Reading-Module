import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

class ATRM
{
	public static Random rand = new Random();
	public static int n;
	
	public static class ATRMThread extends Thread
	{
		private static AtomicInteger counter = new AtomicInteger(0);
		public static int[] readings;
		public static int[] maxArr;
		public static int[] minArr;
		
		String name;
		
		public ATRMThread(String name) 
    {
      this.name = name;
    }

		@Override
		public void run()
		{
			int cur = 0;
			while ((cur = counter.getAndIncrement()) < n)
			{
				int reading = rand.nextInt(171) - 100;
				// System.out.println(this.name + " " + cur + " " + reading);
				readings[cur] = reading;

				int ind = (cur/60) * 5;
				if (reading > maxArr[ind + 0])
				{
					maxArr[ind + 0] = reading;
					Arrays.sort(maxArr, ind, ind+5);
				}

				if (reading < minArr[ind + 4])
				{
					minArr[ind + 4] = reading;
					Arrays.sort(minArr, ind, ind+5);
				}
			}
		}
	}
	public static void main(String[] args) {

		Scanner s = new Scanner(System.in);

		System.out.println("How many hours would you like to simulate?");
		int numHours = s.nextInt();
		n = numHours*60;
		
		ATRMThread.readings = new int[numHours*60];
		ATRMThread.minArr = new int[numHours*5];
		ATRMThread.maxArr = new int[numHours*5];

		int count = 0;

		ATRMThread t1 = new ATRMThread("Thread 1");
		ATRMThread t2 = new ATRMThread("Thread 2");
		ATRMThread t3 = new ATRMThread("Thread 3");
		ATRMThread t4 = new ATRMThread("Thread 4");
		ATRMThread t5 = new ATRMThread("Thread 5");
		ATRMThread t6 = new ATRMThread("Thread 6");
		ATRMThread t7 = new ATRMThread("Thread 7");
		ATRMThread t8 = new ATRMThread("Thread 8");

		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		t8.start();

		try
		{
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			t5.join();
			t6.join();
			t7.join();
			t8.join();
		}
		catch (Exception e)
		{      
		}

		while (count < numHours)
		{
			int minute = 0;
			int difference = 0;
			
			for (int i = count*60; i < (count*60) + 50; i++)
			{
				int temp = Math.abs(ATRMThread.readings[i]-ATRMThread.readings[i+9]);
				if (temp > difference)
				{
					minute = i-(count*60);
					difference = temp;
				}
			}

			System.out.println("Hour #" + (count+1) + " report:");
			System.out.println("10 minute interval with the greatest difference:" + minute + "-" + (minute+9) + " with a difference of " + difference + "F"); 
	
			System.out.println("Top 5 highest temperatures:");
			for (int i = 4 + (count*5); i >= (count*5); i--)
			{
				System.out.println((5-i-(count*5)) + ": " + ATRMThread.maxArr[i]);
			}
	
			System.out.println("Bottom 5 lowest temperatures:");
			
			for (int i = (count*5); i < 5 + (count*5); i++)
			{
				System.out.println((i+1 - (count*5)) + ": " + ATRMThread.minArr[i]);
			}
			System.out.println();
			count++;
		}
	}
	
}