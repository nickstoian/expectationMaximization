//Nicolas Stoian
//CS780 Artificial Intelligence
//Programming Assignment #2 - Expectation Maximization

//This program needs 2 command line arguments
//args[0] "input.txt" for text file representing the dataset
//args[1] "output.txt" to write out the iteration, log likelihood, and conditional probabilities

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Class containing main function and helper functions
 * The main function requires 2 arguments, args[0] for the input file and args[1] for the output file
 * The output is also sent to the console
 *
 * @author Nicolas Stoian
 *
 */
public class ExpectationMaximization {
	public static void main(String args[]){
		try{
			Scanner inFile = new Scanner(new FileReader(args[0]));
			PrintWriter outFile = new PrintWriter(args[1]);
			int[] countArray = new int [12];
			double [] probArray = new double [12];
			for(int i = 0; i < 12; i++){
				countArray[i] = 0;
				probArray[i] = 0.0;
			}
			loadCountArray(countArray, inFile);
			double [] theta = new double [5];
			for(int i = 0; i < 5; i++){
				theta[i] = 0.0;
			}
			// Set the starting parameters below, using the estimateThetasFromData function will discard these values
			theta[0] = 0.7;		// P(G)
			theta[1] = 0.8;		// P(W|G)
			theta[2] = 0.4;		// P(W|-G)
			theta[3] = 0.7;		// P(H|G)
			theta[4] = 0.3;		// P(H|-G)
			//estimateThetasFromData(countArray, theta);   // Uncomment this line to estimate the initial parameters from the given data
			computeProbArray(probArray, theta);
			double previousLogLikelihood = 0.0;
			double logLikelihood = computeLogLikelihood(countArray, probArray);
			int iteration = 0;
			System.out.println("Data file = " + args[0]);
			outFile.println("Data file = " + args[0]);
			System.out.println();
			outFile.println();
			System.out.println("Iteration = " + iteration);
			outFile.println("Iteration = " + iteration++);
			System.out.println("Log Likelihood = " + logLikelihood);
			outFile.println("Log Likelihood = " + logLikelihood);
			System.out.println("P(G)    = " + theta[0]);
			System.out.println("P(W|G)  = " + theta[1]);
			System.out.println("P(W|-G) = " + theta[2]);
			System.out.println("P(H|G)  = " + theta[3]);
			System.out.println("P(H|-G) = " + theta[4]);
			outFile.println("P(G)    = " + theta[0]);
			outFile.println("P(W|G)  = " + theta[1]);
			outFile.println("P(W|-G) = " + theta[2]);
			outFile.println("P(H|G)  = " + theta[3]);
			outFile.println("P(H|-G) = " + theta[4]);
			System.out.println();
			outFile.println();
			while(Math.abs(logLikelihood - previousLogLikelihood) > 0.001){
				previousLogLikelihood = logLikelihood;
				computeThetas(countArray, probArray, theta);
				computeProbArray(probArray, theta);
				logLikelihood = computeLogLikelihood(countArray, probArray);
				System.out.println("Iteration = " + iteration);
				outFile.println("Iteration = " + iteration++);
				System.out.println("Log Likelihood = " + logLikelihood);
				outFile.println("Log Likelihood = " + logLikelihood);
				System.out.println("P(G)    = " + theta[0]);
				System.out.println("P(W|G)  = " + theta[1]);
				System.out.println("P(W|-G) = " + theta[2]);
				System.out.println("P(H|G)  = " + theta[3]);
				System.out.println("P(H|-G) = " + theta[4]);
				outFile.println("P(G)    = " + theta[0]);
				outFile.println("P(W|G)  = " + theta[1]);
				outFile.println("P(W|-G) = " + theta[2]);
				outFile.println("P(H|G)  = " + theta[3]);
				outFile.println("P(H|-G) = " + theta[4]);
				System.out.println();
				outFile.println();
			}
			inFile.close();
			outFile.close();
		}
		catch(FileNotFoundException e){
			System.err.println("File not found exception, check arguements and try again.");
            return;
		}
	}

	/**
	 * Function to take in the input and store the relevant information in an array
	 *
	 * @param countArray
	 * @param inFile
	 */
	public static void loadCountArray(int[] countArray, Scanner inFile){
		String gender;
		int weight;
		int height;
		inFile.nextLine();
		while(inFile.hasNext()){
			gender = inFile.next();
			weight = inFile.nextInt();
			height = inFile.nextInt();
	        if(gender.equals("0")){
	        	if(weight == 0 && height == 0){
	        		countArray[0]++;
	        	}
	        	if(weight == 0 && height == 1){
	        		countArray[1]++;
	        	}
	        	if(weight == 1 && height == 0){
	        		countArray[2]++;
	        	}
	        	if(weight == 1 && height == 1){
	        		countArray[3]++;
	        	}
	        }
	        if(gender.equals("1")){
	        	if(weight == 0 && height == 0){
	        		countArray[4]++;
	        	}
	        	if(weight == 0 && height == 1){
	        		countArray[5]++;
	        	}
	        	if(weight == 1 && height == 0){
	        		countArray[6]++;
	        	}
	        	if(weight == 1 && height == 1){
	        		countArray[7]++;
	        	}
	        }
	        if(gender.equals("-")){
	        	if(weight == 0 && height == 0){
	        		countArray[8]++;
	        	}
	        	if(weight == 0 && height == 1){
	        		countArray[9]++;
	        	}
	        	if(weight == 1 && height == 0){
	        		countArray[10]++;
	        	}
	        	if(weight == 1 && height == 1){
	        		countArray[11]++;
	        	}
	        }
	    }
	}

	/**
	 * Function to estimate the initial parameters from the given complete data
	 * Must have some hard data for this to work
	 *
	 * @param countArray
	 * @param theta
	 */
	public static void estimateThetasFromData(int[] countArray, double[] theta){
		double newTheta0 = (double)(countArray[0] + countArray[1] + countArray[2] + countArray[3])
				   		   /
				   		   (countArray[0] + countArray[1] + countArray[2] + countArray[3] + countArray[4] + countArray[5] + countArray[6] + countArray[7]);
		double newTheta1 = (double)(countArray[0] + countArray[1])
		   		   		   /
		   		   		   (countArray[0] + countArray[1] + countArray[2] + countArray[3]);
		double newTheta2 = (double)(countArray[4] + countArray[5])
		   		   		   /
		   		   		   (countArray[4] + countArray[5] + countArray[6] + countArray[7]);
		double newTheta3 = (double)(countArray[0] + countArray[2])
		   		   		   /
		   		   		   (countArray[0] + countArray[1] + countArray[2] + countArray[3]);
		double newTheta4 = (double)(countArray[4] + countArray[6])
		   		   		   /
		   		   		   (countArray[4] + countArray[5] + countArray[6] + countArray[7]);
		theta[0] = newTheta0;
		theta[1] = newTheta1;
		theta[2] = newTheta2;
		theta[3] = newTheta3;
		theta[4] = newTheta4;
	}


	/**
	 * Function to compute the probabilities for each data case and store the values in an array
	 *
	 * @param probArray
	 * @param theta
	 */
	public static void computeProbArray(double[] probArray, double[] theta){
		probArray[0] = theta[1] * theta[3] * theta[0];
		probArray[1] = theta[1] * (1 - theta[3]) * theta[0];
		probArray[2] = (1 - theta[1]) * theta[3] * theta[0];
		probArray[3] = (1 - theta[1]) * (1 - theta[3]) * theta[0];
		probArray[4] = theta[2] * theta[4] * (1 - theta[0]);
		probArray[5] = theta[2] * (1 - theta[4]) * (1 - theta[0]);
		probArray[6] = (1 - theta[2]) * theta[4] * (1 - theta[0]);
		probArray[7] = (1 - theta[2]) * (1 - theta[4]) * (1 - theta[0]);
		probArray[8] = (theta[1] * theta[3] * theta[0]) + (theta[2] * theta[4] * (1 - theta[0]));
		probArray[9] = (theta[1] * (1 - theta[3]) * theta[0]) + (theta[2] * (1 - theta[4]) * (1 - theta[0]));
		probArray[10] = ((1 - theta[1]) * theta[3] * theta[0]) + ((1 - theta[2]) * theta[4] * (1 - theta[0]));
		probArray[11] = ((1 - theta[1]) * (1 - theta[3]) * theta[0]) + ((1 - theta[2]) * (1 - theta[4]) * (1 - theta[0]));
	}

	/**
	 * Function to compute the log likelihood
	 *
	 * @param countArray
	 * @param probArray
	 * @return the log likelihood
	 */
	public static double computeLogLikelihood(int[] countArray, double[] probArray){
		double logLikelyhood = 0.0;
		for(int i = 0; i < 12; i++){
			logLikelyhood+= (Math.log(probArray[i]) * countArray[i]);
		}
		return logLikelyhood;
	}

	/**
	 * Function to compute the values for the parameters
	 *
	 * @param countArray
	 * @param probArray
	 * @param theta
	 */
	public static void computeThetas(int[] countArray, double[] probArray, double[] theta){
		double newTheta0 = (double)(countArray[0] + countArray[1] + countArray[2] + countArray[3] +
						   ((probArray[0]/probArray[8])*countArray[8]) + ((probArray[1]/probArray[9])*countArray[9]) +
						   ((probArray[2]/probArray[10])*countArray[10]) + ((probArray[3]/probArray[11])*countArray[11]))
						   /
						   (countArray[0] + countArray[1] + countArray[2] + countArray[3] + countArray[4] + countArray[5] + countArray[6] +
						   countArray[7] + countArray[8] + countArray[9] + countArray[10] + countArray[11]);
		double newTheta1 = (double)(countArray[0] + countArray[1] +
				   		   ((probArray[0]/probArray[8])*countArray[8]) + ((probArray[1]/probArray[9])*countArray[9]))
				   		   /
				   		   (countArray[0] + countArray[1] + countArray[2] + countArray[3] +
				   		   ((probArray[0]/probArray[8])*countArray[8]) + ((probArray[1]/probArray[9])*countArray[9]) +
						   ((probArray[2]/probArray[10])*countArray[10]) + ((probArray[3]/probArray[11])*countArray[11]));
		double newTheta2 = (double)(countArray[4] + countArray[5] +
		   		   		   ((probArray[4]/probArray[8])*countArray[8]) + ((probArray[5]/probArray[9])*countArray[9]))
		   		   		   /
		   		   		   (countArray[4] + countArray[5] + countArray[6] + countArray[7] +
		   		   		   ((probArray[4]/probArray[8])*countArray[8]) + ((probArray[5]/probArray[9])*countArray[9]) +
		   		   		   ((probArray[6]/probArray[10])*countArray[10]) + ((probArray[7]/probArray[11])*countArray[11]));
		double newTheta3 = (double)(countArray[0] + countArray[2] +
		   		   		   ((probArray[0]/probArray[8])*countArray[8]) + ((probArray[2]/probArray[10])*countArray[10]))
		   		   		   /
		   		   		   (countArray[0] + countArray[1] + countArray[2] + countArray[3] +
		   		   		   ((probArray[0]/probArray[8])*countArray[8]) + ((probArray[1]/probArray[9])*countArray[9]) +
		   		   		   ((probArray[2]/probArray[10])*countArray[10]) + ((probArray[3]/probArray[11])*countArray[11]));
		double newTheta4 = (double)(countArray[4] + countArray[6] +
		   		   		   ((probArray[4]/probArray[8])*countArray[8]) + ((probArray[6]/probArray[10])*countArray[10]))
		   		   		   /
		   		   		   (countArray[4] + countArray[5] + countArray[6] + countArray[7] +
		   		   		   ((probArray[4]/probArray[8])*countArray[8]) + ((probArray[5]/probArray[9])*countArray[9]) +
		   		   		   ((probArray[6]/probArray[10])*countArray[10]) + ((probArray[7]/probArray[11])*countArray[11]));
		theta[0] = newTheta0;
		theta[1] = newTheta1;
		theta[2] = newTheta2;
		theta[3] = newTheta3;
		theta[4] = newTheta4;
	}
}
