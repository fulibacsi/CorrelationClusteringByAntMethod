import java.io.*;
import java.util.*;

public class Main {

	private static double alpha;
	private static double beta;
	private static double rho;
	private static double ratio;
	private static double zeros;
	private static int size; 
	private static int numberOfAnts;
	private static int maxIterations;
	private static boolean local = true;
	
	
	private static Cluster singleTest(boolean printresults) throws CloneNotSupportedException {
		// create a variable for storing the result
		Cluster solution;
		
		if(printresults) {
			// print preferences
			System.out.printf("Process started!\nProblem size: %d\nRatio: %.2f\nnumber of ants: %d\nAlpha: %.2f\nBeta: %.2f\nRho: %.2f\n",
								size, ratio, numberOfAnts, alpha, beta, rho);
				
			// Creating problem handler
			AntSystem hangyaboj = new AntSystem(size, ratio, zeros, numberOfAnts, alpha, beta, rho, maxIterations, local);
			//AntSystem hangyaboj = new AntSystem("bemenet.txt", numberOfAnts, alpha, beta, rho, maxIterations);
			
			// start time
			long startTime = System.currentTimeMillis();
			
			// problem solving
			solution = (Cluster)hangyaboj.solve();
			
			// print results
			System.out.printf("\nSolved!\nRequired time: %d ms\nError: %.2f\nSize of the largest cluster: %d\n", (System.currentTimeMillis() - startTime), solution.getValue(), solution.getLargestClusterSize());
			
			/*		
		 		// print solution
		   		System.out.println("Solution:\n");
				for(int i = 0; i < size; i++)
				System.out.printf("%3d", solution.getClustering()[i]);
		 	*/	
		}
		else {
			// Creating problem handler
			AntSystem hangyaboj = new AntSystem(size, ratio, zeros, numberOfAnts, alpha, beta, rho, maxIterations, local);
			//AntSystem hangyaboj = new AntSystem("bemenet.txt", numberOfAnts, alpha, beta, rho, maxIterations);
			
			// problem solving
			solution = (Cluster)hangyaboj.solve();
		}
		
		return solution;
	}
	
	private static void multiTest(int numberOfTests, String filename) throws CloneNotSupportedException {
		double[][][][][] AverageLargestClusterSizesAndError = new double[5][5][5][21][2];
		Cluster solution;
		// start time
		long startTime = System.currentTimeMillis();
		System.out.println("Process started!");
		
		for(int _alpha_ = 0; _alpha_ < 5; _alpha_++) {
			alpha = 0.0 + _alpha_ * 0.625;
			for(int _beta_ = 0; _beta_ < 5; _beta_++) { 
				beta = 0.0 + _beta_ * 0.5;
				for(int _rho_ = 0; _rho_ < 5; _rho_++) {
					rho = 0.0 + _rho_ * 0.25;
		
					System.out.print("\nalpha: " + _alpha_ + "/4 beta: " + _beta_ + "/4 rho: " + _rho_ + "/4 > ");
					for(int _ratio_ = 0; _ratio_ < 21; _ratio_++) {
						ratio = 0.0 + _ratio_ * 0.05;
						for(int j = 0; j < numberOfTests; j++) {
							solution = singleTest(false);
							AverageLargestClusterSizesAndError[_alpha_][_beta_][_rho_][_ratio_][0] += solution.getLargestClusterSize();
							AverageLargestClusterSizesAndError[_alpha_][_beta_][_rho_][_ratio_][1] += solution.getValue();
						}
						AverageLargestClusterSizesAndError[_alpha_][_beta_][_rho_][_ratio_][0] /= numberOfTests;
						AverageLargestClusterSizesAndError[_alpha_][_beta_][_rho_][_ratio_][1] /= numberOfTests;
						System.out.print(".");
					}
				}
			}
		}
		System.out.println("\nDone!\nRequired time: " + (System.currentTimeMillis() - startTime));
		saveToFile(filename, AverageLargestClusterSizesAndError);
	}

	
	private static void saveToFile(String filename, double[][][][][] AverageLargestClusterSizesAndError) {
		Calendar cal = Calendar.getInstance();
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		    // save the date of creation
		    out.write("# Created on " + cal.getTime() + "\n# Size of the problem: " + size);
		    out.write("\nratio;alpha;beta;rho;avgsize;error\n");
		    for(int _alpha_ = 0; _alpha_ < 5; _alpha_++)
		    	for(int _beta_ = 0; _beta_ < 5; _beta_++)
		    		for(int _rho_ = 0; _rho_ < 5; _rho_++)
		    			for(int _ratio_ = 0; _ratio_ < 21; _ratio_++)
		    				out.write(String.format("%.2f", (0.0 + _ratio_ * 0.05)) + ";" + (0.0 + _alpha_ * 0.625) + ";" + 
		    						(0.0 + _beta_ * 0.5) + ";" + (0.0 + _rho_ * 0.25) + ";" + 
		    						AverageLargestClusterSizesAndError[_alpha_][_beta_][_rho_][_ratio_][0] + ";" +
		    						AverageLargestClusterSizesAndError[_alpha_][_beta_][_rho_][_ratio_][1] + "\n");
		    out.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	
	/**
	 * @param args
	 * @throws CloneNotSupportedException 
	 */
	public static void main(String[] args) throws CloneNotSupportedException {
		// problem configuration
		alpha = 1.0;
		beta = 2.5;
		rho = 0.5;
		ratio = 0.7;
		zeros = 0.0;
		size = 200; 
		numberOfAnts = 5;
		maxIterations = 10;
		local = false;
		String filename = "/home/fuli/Asztal/results.csv";
		
		//multiTest(10, filename);
		singleTest(true);
	
	}



}
