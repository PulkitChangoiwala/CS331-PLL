/*
Steps To Execute

- Save This File In Your System with name q3.java
- Open the command prompt in the folder where this file is stored
- Run Command "javac q3.java" (without quotes)
- Then Run "java q3 4" (without quotes), Note: you can replace 4 with Number Of thread which you want to create(4 to 16)

*/







import java.util.Random;
import java.lang.Math;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


class initialisation extends Thread
{
        //Every thread will start at i th term then i+n_thread, i+ 2*n_thread and so on
        public static final Random r = new Random();
        int startInd;
        int n_t; //number of threads
        double mat1[][];
        double mat2[][];
        
        //constructor
        initialisation(int input1, int input2, double[][] m1, double[][] m2){
                startInd = input1;
                n_t = input2;
                mat1 = m1;
                mat2 = m2;
                
        }



        public void run()
        {       
                try
                {   
                    //Using Row Major Order For Array Initailisation
                    //Treat input as single array of 10^3 * 10^3
                    //then in this thread initalise 2-D array with values at
                    // startInd, startInd + n_t, startInd + 2*n_t, ... so on from one-D array

                    //Conversion From 1-D array indices to 2-D array Indices
                    //row = oneD_Ind/1000; col = row = oneD_Ind%1000

                     int itr=startInd;
                     int length = 1000000;
                     int row; int col;
                     for(; itr<length; itr+=n_t){
                        row = itr/1000;
                        col = itr%1000;
                        mat1[row][col] = 10*r.nextDouble();
                        mat2[row][col] = 10*r.nextDouble();

                     }   



                }

                catch (Exception e){}

        }


}


class multiplication extends Thread{

    int startInd;
        int n_t; //number of threads
        double mat1[][];
        double mat2[][];
        double mat3[][];

        multiplication(int input1, int input2, double[][] m1, double[][] m2, double[][] m3){
                startInd = input1;
                n_t = input2;
                mat1 = m1;
                mat2 = m2;
                mat3 = m3;
                
        }
         public void run()
        {       
                try
                {   
                    //Used Row Major Order For Array Initailisation
                    //Treat input as single array of 10^3 * 10^3
                    //then in this thread initalise 2-D array with values at
                    // startInd, startInd + n_t, startInd + 2*n_t, ... so on from one-D array

                    //Conversion From 1-D array indices to 2-D array Indices
                    //row = oneD_Ind/1000; col = row = oneD_Ind%1000

                     int itr=startInd;
                     int length = 1000000;
                     int row; int col; double sum = 0;
                     for(; itr<length; itr+=n_t){
                        row = itr/1000;
                        col = itr%1000;
                        sum = 0;
                        for(int k=0;k<1000;++k){
                            sum = sum + mat1[row][k] * mat2[k][col];
                        }
                        mat3[row][col] = sum;


                        
                     }   



                }

                catch (Exception e){}

        }

}


public final class q3
{

        
        private static int row = 1000;
        private static int n_thread = 4;
        private static double matA[][] = new double[1000][1000];
        private static double matB[][] = new double[1000][1000];
        private static double matC[][] = new double[1000][1000];

        public static void main(String[] args)
        {
                long startTime = System.currentTimeMillis();

                if( args.length == 1) { n_thread = Integer.parseInt(args[0]);}
                
                if(n_thread>16 || n_thread < 4) {
                        System.out.println("Invalid Thread Count");
                }

                else{

                        //We need to create n_thread numbers of thread
                        
                        System.out.println("Starting with Initailisation \n");
                        initialisation threads[] = new initialisation[n_thread];
    
                        for(int i=0; i<n_thread; ++i){  

                            threads[i] = new initialisation(i,n_thread,matA,matB);
                            threads[i].start();

                        }

                        //We need to wait the main() till child threads are completed
                        //For that we will use join()

                        for(int i=0; i<n_thread; ++i){ try { threads[i].join();} catch(Exception e){}}
                        System.out.println("Initailisation Complete, writing to matrix A and matrix B to files \n");        
                        






                        /**************** Writing In File *************************/
                        try{
                            FileWriter writer = new FileWriter("Matrix_A.txt");
                            //writer.write("Try Text");
                            for(int i=0; i<row;++i){
                                for(int j=0; j<row;++j){
                                    writer.write(Double.toString(matA[i][j]));
                                    if(j==row-1) {writer.write("\n");}
                                    else {writer.write("    ");}
                                }
                            }
                            writer.close();
                        }
                        catch (IOException e){}

                        try{
                            FileWriter writer = new FileWriter("Matrix_B.txt");
                            //writer.write("Try Text");
                            for(int i=0; i<row;++i){
                                for(int j=0; j<row;++j){
                                    writer.write(Double.toString(matB[i][j]));
                                    if(j==row-1) {writer.write("\n");}
                                    else {writer.write("    ");}
                                }
                            }
                            writer.close();
                        }
                        catch (IOException e){}

                        /**************** Writing In File Ends Here*************************/







                        System.out.println("Finished With Writing In Files \n");
                        System.out.println("Starting with Multiplication \n");
                        multiplication threads_m[] = new multiplication[n_thread];
    
                        for(int i=0; i<n_thread; ++i){  

                            threads_m[i] = new multiplication(i,n_thread,matA,matB, matC);
                            threads_m[i].start();

                        }
                        for(int i=0; i<n_thread; ++i){ try { threads_m[i].join();} catch(Exception e){}}
        
                        System.out.println("Finished Multiplication \n");
                        System.out.println("Writing Output In A File \n");

                        try{
                            FileWriter writer = new FileWriter("Matrix_C.txt");
                            //writer.write("Try Text");
                            for(int i=0; i<row;++i){
                                for(int j=0; j<row;++j){
                                    writer.write(Double.toString(matC[i][j]));
                                    if(j==row-1) {writer.write("\n");}
                                    else {writer.write("    ");}
                                }
                            }
                            writer.close();
                        }
                        catch (IOException e){}

                        System.out.println("Matrix C is written in file. Thank You! \n");
                        
                        
                        


                        
                }
                long endTime = System.currentTimeMillis();
                //System.out.println(endTime - startTime);
                //System.out.println(totalPtsInsideCircle);
                
        } //end of main()



}