import java.util.*;

public class AntSystem {
	
	private CorrelationMatrix matrix;
	private double[][] pheromone;
	private int size, numberOfAnts, maxIterations;
	private double ratio, alpha, beta, rho;
	private ArrayList<Ant> anti;
	private Cluster solution;
	private boolean isSolved;
	private boolean local;
	
	
	/**
	 * Constructor. Creates an Ant System with a given sized correlation matrix. 
	 * @param size - size of the problem
	 * @param ratio - +/- element ratio
	 * @param number - number of ants
	 * @param alpha - history coefficient
	 * @param beta - heuristic coefficient
	 * @param rho - decay factor
	 * @param maxIterations - maximum number of iterations
	 */
	public AntSystem(int size, double ratio, double zeros, int number, double alpha, double beta, double rho, int maxIterations, boolean local) {
		this.size = size;
		this.ratio = ratio;
		this.alpha = alpha;
		this.beta = beta;
		this.rho = rho;
		this.numberOfAnts = number;
		this.maxIterations = maxIterations;
		this.local = local;
		this.matrix = new CorrelationMatrix(this.size, this.ratio, zeros);
		this.pheromone = new double[this.size][this.size];
		for(int i = 0; i < this.size; i++)
			for(int j = 0; j < this.size; j++)
				this.pheromone[i][j] = 1.0;
		
		this.anti = new ArrayList<Ant>();
		for(int i = 0; i < this.numberOfAnts; i++)
			this.anti.add(new Ant(this.size, this.matrix, this.alpha, this.beta));
		this.isSolved = false;
	}
	
	
	/**
	 * Constructor. Creates an Ant System by reading the correlation matrix from a specified file.
	 * @param filename - the file containing the correlation matrix
	 * @param number - number of ants
	 * @param alpha - history coefficient
	 * @param beta - heuristic coefficient
	 * @param rho - decay factor
	 * @param maxIterations - maximum number of iterations
	 */
	public AntSystem(String filename, int number, double alpha, double beta, double rho, int maxIterations) {
		this.matrix = new CorrelationMatrix(filename);
		this.size = this.matrix.getSize();
		this.alpha = alpha;
		this.beta = beta;
		this.rho = rho;
		this.numberOfAnts = number;
		this.maxIterations = maxIterations;
		this.pheromone = new double[this.size][this.size];
		for(int i = 0; i < this.size; i++)
			for(int j = 0; j < this.size; j++)
				this.pheromone[i][j] = 1.0;
		
		this.anti = new ArrayList<Ant>();
		for(int i = 0; i < this.numberOfAnts; i++)
			this.anti.add(new Ant(this.size, this.matrix, this.alpha, this.beta));
		this.isSolved = false;
	}
		
	
	/**
	 * Solve the problem
	 * @return - a Cluster containing the results
	 * @throws CloneNotSupportedException
	 */
	public Cluster solve() throws CloneNotSupportedException{
		Cluster solution = new Cluster(this.size);
		solution.generateRandomly();
		double bestValue = solution.computeValue(this.matrix);
		
		for(int iteration = 0; iteration < this.maxIterations; iteration++) {
			//System.out.println("\n" + iteration + ": " + bestValue);
			for(int i = 0; i < this.numberOfAnts; i++) {
				if(this.local)
					this.anti.get(i).createLocalSolutionImproved(this.pheromone);
				else
					this.anti.get(i).createSolutionImproved(this.pheromone);
				if(this.anti.get(i).getValue() < bestValue) {
					solution = (Cluster)this.anti.get(i).getClustering().clone();
					bestValue = this.anti.get(i).getValue();
				}
			}
			this.updatePheromone();
			//System.out.printf("Solutions: ");
			for(int i = 0; i < this.numberOfAnts; i++) {
				//System.out.printf("%.2f ", this.anti.get(i).getValue());
				this.anti.get(i).randomize();
			}
			/*
			for(int i = 0; i < this.size; i++) {
				for(int j = 0; j< this.size; j++)
					System.out.printf("%.1f ", this.pheromone[i][j]);
				System.out.println();
			}
			*/
			System.out.printf(".");
			
		}
		
		this.solution = (Cluster)solution.clone();
		this.isSolved = true;
		return (Cluster)solution.clone();
	}
	
	
	/**
	 * Update pheromone levels.
	 */
	public void updatePheromone() {
		for(int i = 0; i < this.size; i++)
			for(int j = 0; j< this.size; j++){
				this.pheromone[i][j] *= (1.0 - this.rho);
				for(int k = 0; k < this.numberOfAnts; k++)
					if(this.anti.get(k).getClustering().getElement(i) == j)
						this.pheromone[i][j] += 1.0 / this.anti.get(k).getValue();
				
		}
	}
	
	
	/**
	 * Get the solution. If the problem is not solved yet it solves it first.
	 * @return a Cluster containing the results 
	 * @throws CloneNotSupportedException
	 */
	public Cluster getSolution() throws CloneNotSupportedException {
		if(isSolved)
			return this.solution;
		else {
			this.solve();
			return this.solution;
		}
	}
	
	
	/**
	 * Get the correlation matrix
	 * @return correlation matrix.
	 */
	public CorrelationMatrix getMatrix() {
		return matrix;
	}

}
