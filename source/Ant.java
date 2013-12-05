
public class Ant {
	
	private int size;
	private Cluster clustering;
	private CorrelationMatrix correlationMatrix;
	private double alpha;
	private double beta;
	private double value;
	

	/**
	 * Constructor.
	 * @param size - size of the correlation matrix
	 * @param matrix - the correlation matrix
	 * @param alpha - history coefficient
	 * @param beta - heuristic coefficient
	 */
	public Ant(int size, CorrelationMatrix matrix, double alpha, double beta) {
		this.size = size;
		this.clustering = new Cluster(this.size);
		this.clustering.generateRandomly();
		this.correlationMatrix = matrix;
		this.alpha = alpha;
		this.beta = beta;
		this.value = this.clustering.computeValue(this.correlationMatrix);
	}
	
	
	/**
	 * Get the clustering.
	 * @return the clustering.
	 */
	public Cluster getClustering() {
		return clustering;
	}
	
	
	/**
	 * Get the goodness of the Ant.
	 * @return the goodness.
	 */
	public double getValue() {
		return value;
	}

	
	/**
	 * Set the clustering to a random clustering.
	 */
	public void randomize() {
		this.clustering.generateRandomly();
		this.value = this.clustering.computeValue(this.correlationMatrix);
	}

	
	/**
	 * Search for the best element in a clustering at the given index.
	 * @param pheromone - the pheromone matrix
	 * @param element - the index in the clustering 
	 * @return - the best value for the given index
	 * @throws CloneNotSupportedException
	 */
	public int selectBest(double[][] pheromone, int element) throws CloneNotSupportedException{
		
		int bestElement = this.clustering.getElement(element);
		double actProb, mostProb = 0.0, sumProb = 0.0;
		double[] prob = new double[this.size]; 
		
		// actual solution
		Cluster actual = (Cluster)this.clustering.clone();
				
		// get the probabilities of the different solutions
		for(int i = 0; i < this.size; i++) {
			// modify the selected element... 
			actual.modifyClustering(element, i);
			// and compute&store the probability of the solution
			prob[i] = Math.pow(pheromone[element][i], this.alpha) * Math.pow((1.0 / actual.computeValue(this.correlationMatrix)), this.beta);
			// increase the sum
			sumProb += prob[i];
		}
		
		// reset the actual solution
		actual.setClustering(this.clustering.getClustering());
		

		// search for the most probable solution
		for(int i = 0; i < this.size; i++) {
			actProb = prob[i]/sumProb;
			if(actProb >= mostProb) {
				bestElement = i;
				mostProb = actProb;
			}
		}
		
		return bestElement;
	}
	
	/**
	 * Search for the best element in a clustering at the given index.
	 * @param pheromone - the pheromone matrix
	 * @param element - the index in the clustering 
	 * @return - the best value for the given index
	 * @throws CloneNotSupportedException
	 */
	public int selectBestImproved(double[][] pheromone, int element) throws CloneNotSupportedException{
		
		int bestElement = this.clustering.getElement(element);
		double actProb, mostProb = 0.0, sumProb = 0.0, temp;
		double[] prob = new double[this.size]; 
		
		// actual solution
		Cluster actual = (Cluster)this.clustering.clone();
				
		// get the probabilities of the different solutions
		for(int i = 0; i < this.size; i++) {
			// modify the selected element... 
			temp = actual.modifyClusteringAndComputeValue(element, i, this.correlationMatrix);
			// and compute&store the probability of the solution
			prob[i] = Math.pow(pheromone[element][i], this.alpha) * Math.pow((1.0 / temp), this.beta);
			// increase the sum
			sumProb += prob[i];
		}
		
		// reset the actual solution
		actual.setClustering(this.clustering.getClustering());
		

		// search for the most probable solution
		for(int i = 0; i < this.size; i++) {
			actProb = prob[i]/sumProb;
			if(actProb >= mostProb) {
				bestElement = i;
				mostProb = actProb;
			}
		}
		
		return bestElement;
	}
	
	
	
	public int selectBestLocal(double[][] pheromone, int element) throws CloneNotSupportedException{
		int bestLocal = this.clustering.getElement(element);
		double actProb, mostProb = 0.0, sumProb = 0.0;
		int[] neighbours;
		
		// if the problem is small, use only 2 neighbours
		int numberOfNeighbours;
		if(this.size < 5) {
			numberOfNeighbours = 3;
			neighbours = this.clustering.getNeighbourElements(element, numberOfNeighbours);
		}
		else {
			numberOfNeighbours = 5;
			neighbours = this.clustering.getNeighbourElements(element, numberOfNeighbours);
		}
		
		double[] prob = new double[numberOfNeighbours];
		
		// create a temporary cluster
		Cluster actual = (Cluster)this.clustering.clone();
		
		for(int i = 0; i < numberOfNeighbours; i++) {
			// modify the selected element... 
			actual.modifyClustering(element, neighbours[i]);
			// and compute&store the probability of the solution
			prob[i] = Math.pow(pheromone[element][neighbours[i]], this.alpha) * Math.pow((1.0 / actual.computeValue(this.correlationMatrix)), this.beta);
			// increase the sum
			sumProb += prob[i];
		}
		
		// reset the actual solution
		actual.setClustering(this.clustering.getClustering());

		// search for the most probable solution
		for(int i = 0; i < numberOfNeighbours; i++) {
			actProb = prob[i]/sumProb;
			if(actProb >= mostProb) {
				bestLocal = neighbours[i];
				mostProb = actProb;
			}
		}
	
		return bestLocal;
	}
	
	
	public int selectBestLocalImproved(double[][] pheromone, int element) throws CloneNotSupportedException{
		int bestLocal = this.clustering.getElement(element);
		double actProb, mostProb = 0.0, sumProb = 0.0, temp;
		int[] neighbours;
		
		// if the problem is small, use only 2 neighbours
		int numberOfNeighbours;
		if(this.size < 5) {
			numberOfNeighbours = 3;
			neighbours = this.clustering.getNeighbourElements(element, numberOfNeighbours);
		}
		else {
			numberOfNeighbours = 5;
			neighbours = this.clustering.getNeighbourElements(element, numberOfNeighbours);
		}
		
		double[] prob = new double[numberOfNeighbours];
		
		// create a temporary cluster
		Cluster actual = (Cluster)this.clustering.clone();
		
		for(int i = 0; i < numberOfNeighbours; i++) {
			// modify the selected element... 
			temp = actual.modifyClusteringAndComputeValue(element, neighbours[i], this.correlationMatrix);
			// and compute&store the probability of the solution
			prob[i] = Math.pow(pheromone[element][neighbours[i]], this.alpha) * Math.pow((1.0 / temp), this.beta);
			// increase the sum
			sumProb += prob[i];
		}
		
		// reset the actual solution
		actual.setClustering(this.clustering.getClustering());

		// search for the most probable solution
		for(int i = 0; i < numberOfNeighbours; i++) {
			actProb = prob[i]/sumProb;
			if(actProb >= mostProb) {
				bestLocal = neighbours[i];
				mostProb = actProb;
			}
		}
	
		return bestLocal;
	}
	
	
	
	
	/**
	 * Create a solution by selecting the best local elements. For multiple agent purposes
	 * make the solution to uniform.
	 * @param pheromone - the pheromone matrix.
	 * @throws CloneNotSupportedException
	 */
	public void createSolution(double[][] pheromone) throws CloneNotSupportedException{
		for(int i = 0; i < this.size; i++)
			this.clustering.modifyClustering(i, this.selectBest(pheromone, i));
		this.clustering.makeUniform();
		this.value = this.clustering.computeValue(this.correlationMatrix);
	}
	

	
	/**
	 * Create a solution by selecting the best local elements. For multiple agent purposes
	 * make the solution to uniform.
	 * @param pheromone - the pheromone matrix.
	 * @throws CloneNotSupportedException
	 */
	public void createSolutionImproved(double[][] pheromone) throws CloneNotSupportedException{
		for(int i = 0; i < this.size; i++)
			this.clustering.modifyClustering(i, this.selectBestImproved(pheromone, i));
		this.clustering.makeUniform();
		this.value = this.clustering.computeValue(this.correlationMatrix);
	}	
	
	
	
	
	
	/**
	 * Create a solution by selecting the best local elements. This method only check the neighbour elements.
	 * For multiple agent purposes make the solution to uniform.
	 * @param pheromone - the pheromone matrix.
	 * @throws CloneNotSupportedException
	 */
	public void createLocalSolution(double[][] pheromone) throws CloneNotSupportedException{
		for(int i = 0; i < this.size; i++)
			this.clustering.modifyClustering(i, this.selectBestLocal(pheromone, i));
		this.clustering.makeUniform();
		this.value = this.clustering.computeValue(this.correlationMatrix);
	}
	
	
	/**
	 * Create a solution by selecting the best local elements. This method only check the neighbour elements.
	 * For multiple agent purposes make the solution to uniform.
	 * @param pheromone - the pheromone matrix.
	 * @throws CloneNotSupportedException
	 */
	public void createLocalSolutionImproved(double[][] pheromone) throws CloneNotSupportedException{
		for(int i = 0; i < this.size; i++)
			this.clustering.modifyClustering(i, this.selectBestLocalImproved(pheromone, i));
		this.clustering.makeUniform();
		this.value = this.clustering.computeValue(this.correlationMatrix);
	}
	
	
}
