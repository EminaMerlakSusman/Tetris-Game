import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Shape {
	/*
	 * Shapes are represented as a 3x2 matrix with 
	 * 0s on empty spaces and 1s where there is a block.
	 */
	
	private BufferedImage block;
	private int[][] coords;
	private Board board;
	private int deltaX = 0;
	private int x, y;
	
	private int color;
	
	private boolean collision = false, moveX = false; //collision detection, vertical & horizontal
	
	private int normalSpeed = 720, speedDown = 60, currentSpeed;
	
	private long time, lastTime;
	
	public Shape(BufferedImage block, int[][] coords, int color, Board board) {
		this.block = block;
		this.coords = coords;
		this.board = board;
		this.color = color;
		x = 3;
		y = 0;
		
		currentSpeed = normalSpeed;
		time = 0;
		lastTime = System.currentTimeMillis(); //last time the shape moved down by 1 square
	    }
	
		public int getColor() {
		return color;
		}

		public void update() {
		/*
		 * Update the location of the shape, and also make shape fall down
		 * (every 600 milliseconds, the shape moves down by 1)
		 */
			time += System.currentTimeMillis() - lastTime; //How much time has passed since last time
			lastTime = System.currentTimeMillis(); //last time the shape moved down by 1
		    
			if(collision) 
			{
				for(int row = 0; row < coords.length; row++) {
					for(int col=0; col < coords[row].length; col++) {
						if(coords[row][col] != 0) {
							board.getBoard()[y + row][x + col] = color;
						}
					   }	
					}
				checkLine();
				board.setNextShape();
			}
			
		    if (!(x + deltaX + coords[0].length > 10) && !(x + deltaX < 0)) 
		    { //left-right movement
		    	
				for(int row = 0; row < coords.length; row++)  
					for(int col=0; col < coords[row].length; col++) 
						if(coords[row][col] != 0) 
						{
						   	if (board.getBoard()[y + row][x + deltaX + col] != 0)
						   		/*this checks if the shape would collide with another
						   		shape when moving sideways. If not, then we
						   		we can move sideways. */
						   		moveX = false;
						}
		    	
		    	
		    	
		    	if (moveX) {
				     x += deltaX;
				     }
			}
		    
		    if (!(y + 1 + coords.length > 20)) 
		    {
		    	
				for(int row = 0; row < coords.length; row++)  
					//we need to stop falling when we collide with another shape
					for(int col=0; col < coords[row].length; col++) 
						if(coords[row][col] != 0) 
						{
							if (board.getBoard()[y + row + 1][col + x] != 0) 
								// if there is a 1 ahead of the shape, we stop moving 								  
								    collision = true;
								  
							
						}
				if(time > currentSpeed) 
			    { //downward movement
			    	y++;
			    	time = 0;
			    }
					
				
		    }else {
		    	 collision = true;
		     }
		    
		    deltaX = 0;
		    moveX = true;
		   }
	
		public BufferedImage getBlock() {
			return block;
		}

		public int[][] getCoords() {
			return coords;
		}

		public void render(Graphics g) { 
			/*
			 * Renders a shape
			 */
			for(int row = 0; row < coords.length; row++) {
				for(int col = 0; col < coords[row].length; col++) {
					if(coords[row][col] != 0) {
						g.drawImage(block, col*board.getBlockSize() + x*board.getBlockSize(), 
						row*board.getBlockSize() + y*board.getBlockSize(), null);
					}
				}
			}
		}
		
		private void checkLine() {
			/*
			 * Checks if all the squares are filled in.
			 * If yes, the line dissapears.
			 */
			int height = board.getBoard().length - 1;
			
			for (int i = height; i > 0; i--) { //rows
				int count = 0; //counts how many colored squares are in this row
				for(int j = 0; j < board.getBoard()[0].length; j++) { //columns
					if (board.getBoard()[i][j] != 0) {
						count += 1;	
					}
					board.getBoard()[height][j] = board.getBoard()[i][j];
					//if the row below was not fully filled out, 
					// height will be the same as i, and nothing will change.
					// If it was filled out, then
					//the row below this one is going to become row i.
				}
				if (count < board.getBoard()[0].length) { 
					height--;
				} else {
					board.someoneScored();
				}
			}
		}
		
		public void rotate() {
			//rotation of shapes
			
			if (collision) {
				//we don't want to rotate when we have already collided
				return;
			}
			
			
			int[][] rotatedMatrix = getTranspose(coords);
			
			rotatedMatrix = getReverse(rotatedMatrix);
			
			if (x + rotatedMatrix[0].length > 10  || y + rotatedMatrix.length > 20) {
				return;
			}
			
			for (int row = 0; row < rotatedMatrix.length; row++) {
				for (int col = 0; col <  rotatedMatrix[0].length; col++) {
					if (board.getBoard()[y + row][x + col] != 0) {
						//if the rotation would cause our shape to overlap 
						// with another shape, we can't rotate
						return;
					}
				}
			}
			coords = rotatedMatrix;
		}
		
		private int[][] getTranspose(int[][] matrix) {
			//turns the rows to columns in a matrix
			int[][] newMatrix = new int[matrix[0].length][matrix.length];
			for (int i=0; i < matrix.length; i++) { //rows
				for (int j=0; j < matrix[0].length; j++) { //columns
					int elt = matrix[i][j];
					newMatrix[j][i] = matrix[i][j];
				}
			}
			return newMatrix;
		}
		
		private int[][] getReverse(int[][] matrix) {
			//reverses the rows in a matrix
			int middle = matrix.length / 2;
			for (int i = 0; i < middle; i++) {
				int[] m = matrix[i]; // i-th row
				matrix[i] = matrix[matrix.length - i - 1];
				matrix[matrix.length - i - 1] = m;
			}
			return matrix;
		}
		
		public void setDeltaX(int deltaX) {
			this.deltaX = deltaX;
		}
		
		public void speedDown() {
			currentSpeed = speedDown;
		}
		
		public void normalSpeed() {
			currentSpeed = normalSpeed;
		}
	
}
