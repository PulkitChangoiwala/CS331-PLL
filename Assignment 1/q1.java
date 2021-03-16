/*
Steps To Execute

- Save This File In Your System with name q1.java
- Open the command prompt in the folder where this file is stored
- Run Command "javac q1.java" (without quotes)
- Then Run "java q1 4" (without quotes), Note: you can replace 4 with Number Of thread which you want to create(4 to 16)

*/

import java.util.Random;




class monteCarlo extends Thread
{
	public static final Random r = new Random();
	
	private int circlePoints;
	private int iterations; //Number of time we have to simulate the monte carlo algorithm
	
	monteCarlo(int input){
		circlePoints=0;
		iterations = input;

	}

	public void run()
	{	

		
		try
		{
			for(int trial = 1; trial <= iterations; trial++){
			Double x = r.nextDouble();
			Double y = r.nextDouble();
			//System.out.println(x); System.out.println(y);
			if((x*x)+(y*y) <=1){
				circlePoints++;
			}
		}
		}

		catch (Exception e){}

	}

	public int circlepts(){
		return circlePoints;
	}
}


public final class q1
{

	//private static final Random r = new Random();

	private static int N = 1000000;
	private static int n_thread = 4;
	private static int totalPtsInsideCircle = 0;
	private static double pi_approx = 0.000000;

	public static void main(String[] args)
	{
		long startTime = System.currentTimeMillis();

		if( args.length == 1) { n_thread = Integer.parseInt(args[0]);}
		
		if(n_thread>16 || n_thread < 4) {
			System.out.println("Invalid Thread Count");
		}

		else{

			//We need to create n_thread numbers of thread
			monteCarlo threads[] = new monteCarlo[n_thread];

			int ptsPerThread = N/n_thread;
			int rem = N%n_thread;


			for(int i=0; i<n_thread; ++i){	

				if(i<rem){
					threads[i] = new monteCarlo(ptsPerThread+1);
					threads[i].start();

				}
				else{
					threads[i] = new monteCarlo(ptsPerThread);
					threads[i].start();

				}



			}

			//We need to wait the main() till child threads are completed
			//For that we will use join()

			for(int i=0; i<n_thread; ++i){	

				try { threads[i].join();} catch(Exception e){}

				totalPtsInsideCircle = totalPtsInsideCircle + threads[i].circlepts(); 
            }



			
		}
		long endTime = System.currentTimeMillis();
		//System.out.println(endTime - startTime);
		//System.out.println(totalPtsInsideCircle);
		pi_approx = 4.000000*totalPtsInsideCircle/N;
		System.out.println(pi_approx);
	} //end of main()



}