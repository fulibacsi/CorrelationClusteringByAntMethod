import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CorrelationMatrix {
	
	/*
	 * The matrix itself. 
	 */
	private int[][] correlationMatrix;
	private int size;
	
	/**
	 * Constructor. Creates a random symmetric correlation matrix
	 * @param size - size of the matrix
	 * @param ratio - the ratio of the + and - edges.
	 */
	public CorrelationMatrix(int size, double ratio, double zeros)
	{
		this.size = size;
		this.correlationMatrix = new int[size][size];
		for(int i = 0; i < size; i ++)
			for(int j = i;j < size; j ++) {
				this.correlationMatrix[i][j] = this.correlationMatrix[j][i] = this.randomByRatio(ratio, zeros);
				if(i == j)
					this.correlationMatrix[i][j] = 0;
			}
	}
	
	/**
	 * Constructor for reading the matrix from a file.
	 * @param filename - the name of the file containing the matrix.
	 */
	public CorrelationMatrix(String filename) {
		File bemenet = new File(filename);
		try {
            Scanner fileScanner = new Scanner(bemenet);
            String line = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            
            // in the first line, search for the size parameter. 
            if(line.toCharArray()[0] == '#') {
            	lineScanner.useDelimiter(" ");
            	lineScanner.next();
        		this.size = lineScanner.nextInt();
            }
            
            this.correlationMatrix = new int[size][size];
            int i = 0;
    
            // read the values into the matrix
            while (fileScanner.hasNextLine() && i < this.size) {
                line = fileScanner.nextLine();
                lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
                for(int j = 0; j < this.size; j++)
                	correlationMatrix[i][j] = correlationMatrix[j][i] = lineScanner.nextInt();
                i++;
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Nincs meg a file!");
        }
	
	}
	
	
	/**
	 * Constructor for an empty correlation matrix.
	 * @param size - size of the matrix
	 */
	public CorrelationMatrix(int size) {
		this.size = size;
		this.correlationMatrix = new int[size][size];
	}
	
	
	/**
	 * Generates a random edge:	0 - not connected
	 * 							1 - positively connected
	 * 						   -1 - negatively connected
	 * @param ratio - the ratio of the positive and negative connections.
	 * @return the edge value.
	 */
	private int randomByRatio(double ratio, double zeros) {
		Random generator = new Random();
		if(generator.nextDouble() < zeros)
			return 0;
		else if(generator.nextDouble() <= ratio)
			return 1;
		else
			return -1;
	}

	
	/**
	 * Get the matrix.
	 * @return the matrix
	 */
	public int[][] getCorrelationMatrix() {
		return correlationMatrix;
	}

	
	/**
	 * Overrides actual matrix.
	 * @param correlationMatrix
	 */
	public void setCorrelationMatrix(int[][] correlationMatrix) {
		this.correlationMatrix = correlationMatrix.clone();
	}


	/**
	 * Get the size of the matrix.
	 * @return the size of the matrix
	 */
	public int getSize() {
		return size;
	}


	
	
}
