/*
Steps To Execute

- Save This File In Your System with name q2.java
- Open the command prompt in the folder where this file is stored
- Run Command "javac q2.java" (without quotes)
- Then Run "java q2 4" (without quotes), Note: you can replace 4 with Number Of thread which you want to create(4 to 16)

*/


import java.util.Random;
import java.lang.Math;



class simpson extends Thread
{
        //Every thread will start at i th term then i+n_thread, i+ 2*n_thread and so on
        
        int startInd;
        int n_t; //number of threads
        double deltaX;
        double sum_total = 0;
        simpson(int input1, int input2, double input3){
                startInd = input1;
                n_t = input2;
                deltaX = input3;
                sum_total = 0;
        }



        public void run()
        {       
            double x = -1; //starting at lower limit
            x += startInd*deltaX;

                int coeff;
                if(startInd%2 == 1) coeff = 4;
                else coeff = 2;
                
                try
                {   double power = 0.0, term = 0.0;
                    for(; x <=1; x = x + (n_t*deltaX)){
                        power = -1*x*x/2;
                        term = coeff*Math.exp(power)/(Math.sqrt(2*Math.PI));
                        sum_total+=term;

                        //Next term will be i+n_t, so if n_t is odd there will be a coeff change

                        if(n_t%2 == 1){
                            coeff = 6-coeff;
                        }

                    }

                }

                catch (Exception e){}

        }

        public double sumTotal(){
                return sum_total;
        }
}


public final class q2
{

        
        private static int N = 1000000;
        private static int n_thread = 4;
        private static double answer =  0.0;

        public static void main(String[] args)
        {
                long startTime = System.currentTimeMillis();

                if( args.length == 1) { n_thread = Integer.parseInt(args[0]);}
                
                if(n_thread>16 || n_thread < 4) {
                        System.out.println("Invalid Thread Count");
                }

                else{

                        //We need to create n_thread numbers of thread
                        simpson threads[] = new simpson[n_thread];

                        double x_delta = 0.0000001; //taking 2*10^7 points, and delta x = (b-a)/n

    
                        for(int i=0; i<n_thread; ++i){  


                            threads[i] = new simpson(i,n_thread,x_delta);
                            threads[i].start();


                        }

                        //We need to wait the main() till child threads are completed
                        //For that we will use join()

                        for(int i=0; i<n_thread; ++i){  

                            try { threads[i].join();} catch(Exception e){}

                            answer += threads[i].sumTotal(); 
                        }

                        answer = answer - 2*(Math.exp(-1/2))/(Math.sqrt(2*Math.PI));
                        answer = x_delta*answer/3;



                        
                }
                long endTime = System.currentTimeMillis();
                //System.out.println(endTime - startTime);
                System.out.println("Integral Value: "+ answer);
                
        } //end of main()



}