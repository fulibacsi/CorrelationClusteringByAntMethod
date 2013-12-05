import java.util.Random;


public class Cluster {

	// the clustering
	private int[] clustering;
	// size of the problem
	private int size;
	// the goodness of the clustering
	private double value;
	
	// indicator variables
	private boolean uniform;
	private boolean computed;
	
	/**
	 * Constructor.
	 * @param size - size of the problem.
	 */
	public Cluster(int size) {
		clustering = new int[size];
		for(int i = 0; i < size; i++)
			clustering[i] = i;
		this.size = size;
		this.uniform = false;
		this.computed = false;
	}

	
	/**
	 * Get the clustering. 
	 * @return clustering.
	 */
	public int[] getClustering() {
		return clustering;
	}

	
	/**
	 * Set a new clustering.
	 * @param clustering - the new value.
	 */
	public void setClustering(int[] clustering) {
		this.clustering = clustering.clone();
		this.uniform = false;
		this.computed = false;
	}
	
	
	/**
	 * Get a specified element from the clustering.
	 * @param element - the position of the value in the clustering.
	 * @return the value on the specified position.
	 */
	public int getElement(int element){
		return this.clustering[element];
	}
	
	
	/**
	 * Modify the clustering by changing the value in a specified position
	 * @param position - the position of the value in the clustering.
	 * @param newLabel - the new value.
	 */
	public void modifyClustering(int position, int newLabel) {
		this.clustering[position] = newLabel;
		this.uniform = false;
		this.computed = false;
	}

	
	/**
	 * Get the size of the clustering.
	 * @return size.
	 */
	public int getSize() {
		return size;
	}


	/**
	 * Set a new size.
	 * @param size - the new size.
	 */
	public void setSize(int size) {
		this.size = size;
	}


	/**
	 * Return the goodness of the clustering.
	 * @return the goodness if it is already computed else return -1.0.
	 */
	public double getValue() {
		if(this.computed)
			return value;
		else
			return -1.0;
	}

	
	/**
	 * Set the goodness to a specified value.
	 * @param value 
	 */
	public void setValue(double value) {
		this.value = value;
		this.computed = true;
	}


	public boolean isUniform() {
		return uniform;
	}


	public void setUniform(boolean uniform) {
		this.uniform = uniform;
	}


	public boolean isComputed() {
		return computed;
	}


	public void setComputed(boolean computed) {
		this.computed = computed;
	}
	
	
	/**
	 * Get the neighbour elements.
	 * @param element - the index of the element of which we compute the neighbours
	 * @return the neighbours including the element itself [left neighbour, element, right neighbour]
	 */
	public int[] getNeighbourElements(int element, int numberOfNeighbours) {
		int[] neighbours;
		if(numberOfNeighbours == 3) {	
			neighbours = new int[3];
			neighbours[0] = neighbours[2] = -1; neighbours[1] = this.clustering[element];
			int max = 0;
			int min = this.size + 1;
	 		for(int i = 0; i < this.size; i++) {
	 			// get the max and min values
	 			if(clustering[i] > max)
	 				max = this.clustering[i];
	 			if(clustering[i] < min)
	 				min = this.clustering[i];
	 			
	 			/* get the initial neighbours:
	 			 * if the neighbour's value is still -1,
	 			 * set it to the first appropriate value
	 			 */
	 			if(neighbours[0] == -1 && this.clustering[i] < this.clustering[element])
	 				neighbours[0] = this.clustering[i];
	 			if(neighbours[2] == -1 && this.clustering[i] > this.clustering[element])
	 				neighbours[2] = this.clustering[i];
	 			
	 			/* search for the smallest element which is greater than the selected element 
	 			 * and for the greatest element which is smaller than the selected element
	 			 */
	 			if(this.clustering[i] < this.clustering[element] && this.clustering[i] > neighbours[0])
	 				neighbours[0] = clustering[i];
	 			if(this.clustering[i] > this.clustering[element] && this.clustering[i] < neighbours[2])
	 				neighbours[2] = clustering[i];
	 		}
	 		
	 		/* If the values still not appropriate, set it to min/max value.
	 		 */
	 		if(neighbours[0] == -1)
	 			neighbours[0] = max;
	 		if(neighbours[2] == -1)
	 			neighbours[2] = min;
		}	
		else {
			neighbours = new int[5]; 
			neighbours[0] =	neighbours[1] = neighbours[3] = neighbours[4] =-1; neighbours[2] = this.clustering[element];
			int dummy, max = 0, max2 = 0, min = this.size + 1, min2 = this.size + 1;
	 		for(int i = 0; i < this.size; i++) {
	 			
	 			// get the max, 2nd max, min and 2nd min values
	 			if(clustering[i] > max2)
	 				max2 = this.clustering[i];
	 			if(max < max2) {
	 				dummy = max2;
	 				max2 = max;
	 				max = dummy;
	 			}
	 				
	 			if(clustering[i] < min2)
	 				min2 = this.clustering[i];
	 			if(min > min2) {
	 				dummy = min2;
	 				min2 = min;
	 				min = dummy;
	 			}
	 			
	 			/* get the initial neighbours:
	 			 * if the neighbour's value is still -1,
	 			 * set it to the first appropriate value
	 			 */
	 			if(neighbours[1] == -1 && this.clustering[i] < this.clustering[element])
	 				neighbours[1] = this.clustering[i];
	 			if(neighbours[0] == -1 && this.clustering[i] < this.clustering[element] && this.clustering[i] < neighbours[1])
	 				neighbours[0] = this.clustering[i];
	 			
	 			if(neighbours[3] == -1 && this.clustering[i] > this.clustering[element])
	 				neighbours[3] = this.clustering[i];
	 			if(neighbours[4] == -1 && this.clustering[i] > this.clustering[element] && this.clustering[i] > neighbours[3])
	 				neighbours[4] = this.clustering[i];
	 			
	 			/* search for the smallest element which is greater than the selected element 
	 			 * and for the greatest element which is smaller than the selected element
	 			 */
	 			if(this.clustering[i] < this.clustering[element] && this.clustering[i] > neighbours[1]) {
	 				neighbours[0] = neighbours[1];
	 				neighbours[1] = clustering[i];
	 			}
	 			if(this.clustering[i] > this.clustering[element] && this.clustering[i] < neighbours[3]) {
	 				neighbours[4] = neighbours[3];
	 				neighbours[3] = clustering[i];
	 			}
	 		}
	 		
	 		/* If the values still not appropriate, set it to min/max value.
	 		 */
	 		if(neighbours[0] == -1 && neighbours[1] == -1) {
	 			neighbours[0] = max2;
	 			neighbours[1] = max;
	 		}
	 		else if(neighbours[0] == -1)
	 			neighbours[0] = max;
	 		
	 		if(neighbours[3] == -1 && neighbours[4] == -1) {
	 			neighbours[3] = min;
	 			neighbours[4] = min2;
	 		}
	 		else if(neighbours[4] == -1)
	 			neighbours[4] = max;
		}
		
		return neighbours;
	}
	
	
	/**
	 * Compute the size of the largest cluster in the actual clustering.
	 * @return the size of the largest cluster
	 */
	public int getLargestClusterSize() {
		// make sure that the labels are uniform 
		if(!(this.uniform))
			this.makeUniform();
		
		
		// get the number of the different labels
		int numberOfLabels = 0;
		for(int i = 0; i < this.size; i++)
			if(this.clustering[i] > numberOfLabels)
				numberOfLabels = this.clustering[i];
		
		int largestSize = 0;
		int counter;
		
		// check all of the different labels
		for(int i = 0; i <= numberOfLabels; i++) {
			// count their size
			counter = 0;
			for(int j = 0; j < this.size; j++)
				if(this.clustering[j] == i)
					counter++;
			// select the largest
			if(counter > largestSize)
				largestSize = counter;
		}
		
		return largestSize;
	}
	
	
	/**
	 * Generate a random clustering.
	 */
	public void generateRandomly() {
		this.uniform = false;
		this.computed = false;
		Random generator = new Random();
		for(int i = 0; i < this.size; i++)
			this.clustering[i] = generator.nextInt(this.size);
	}
	
	
	/**
	 * Make the cluster labels uniform.
	 */
	public void makeUniform() {
		// execute only if the clustering is not uniform.
		if(!(this.uniform)) {
			// to keep track of the changed labels, we create an array.
			int[] changed = new int[this.size];
			for(int i = 0; i < this.size; i++)
				changed[i] = 0;
			
			// store the new labels
			int newLabel = 0;
			// store the actual label
			int actLabel;
			
			// go through all of the labels
			for(int i = 0; i < this.size; i++) {
				// if the actual label has not been changed
				if(changed[i] == 0) {
					// save the actual label
					actLabel = this.clustering[i];
					// search through all of the labels
					for(int j = 0; j < this.size; j++)
						/* if the inspected label is the same as the actual label and
						 * it has not been changed,
						 */
						if((this.clustering[j] == actLabel) && (changed[j] == 0)) {
							// change the label, and set the label to changed.
							this.clustering[j] = newLabel;
							changed[j] = 1;
						}
					// create a new label
					newLabel++;
				}
			}
			
			this.uniform = true;
		}
	}
	
	
	/**
	 * Compute the goodness of the clustering in a given correlation matrix.
	 * @param matrix - the correlation matrix.
	 * @return the goodness of the clustering.
	 */
	public double computeValue(CorrelationMatrix matrix) {
		if(!(this.computed)) {
			double value = 0.0;
			
			for(int i = 0; i < this.size; i++)
				for(int j = i; j < this.size; j++) {
					// if they are in the same cluster
					if(this.clustering[i] == this.clustering[j]) {
						// and the connection between them is negative,
						if(matrix.getCorrelationMatrix()[i][j] == -1)
							// increase error
							value++;
					}
					// if they are not in the same cluster, but the connection
					// between them is positive,
					else if(matrix.getCorrelationMatrix()[i][j] == 1)
						// increase error
						value++;
				}
			
			// store the new value
			this.value = value;
			this.computed = true;
		}
		
		return this.value;
	}
	
	
	/**
	 * Change a label in the clustering and recompute the goodness of the clustering in a given correlation matrix.
	 * @param pos - position of the label to change
	 * @param newValue - the new label
	 * @param matrix - the correlation matrix
	 * @return - the recomputed goodness
	 */
	public double modifyClusteringAndComputeValue(int pos, int newValue, CorrelationMatrix matrix) {
		// if the error of the clustering is already computed,
		// remove the error values connected to the selected position
		if(this.computed && this.clustering[pos] == newValue)
			return this.value;
		if(this.computed) {
			for(int i = 0; i < this.size; i++) {
				// decrease error for the selected component
				if(this.clustering[pos] == this.clustering[i] && matrix.getCorrelationMatrix()[pos][i] == -1)
					this.value--;
				else if(this.clustering[pos] != this.clustering[i] && matrix.getCorrelationMatrix()[pos][i] == 1)
					this.value--;
				
				// increase error connected to the new value
				if(this.clustering[i] == newValue && matrix.getCorrelationMatrix()[pos][i] == -1)
					this.value++;
				else if(this.clustering[i] != newValue && matrix.getCorrelationMatrix()[pos][i] == 1)
					this.value++;
			}
			// change the clustering label to the new value
			this.clustering[pos] = newValue;
			this.computed = true;
		}
		else {
			this.clustering[pos] = newValue;
			this.computeValue(matrix);
		}
		
		return this.value;
	}
	
	
	/**
	 * Clone the clustering.
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Cluster clone = new Cluster(this.size);
		clone.setClustering(this.clustering.clone());
		clone.setUniform(this.uniform);
		clone.setComputed(this.computed);
		clone.setValue(this.value);
		return clone;
	}
	
}
