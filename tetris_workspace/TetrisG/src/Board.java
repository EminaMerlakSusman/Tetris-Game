import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class Board extends JPanel implements KeyListener {
	
	private BufferedImage blocks;
	
	private final int blockSize = 30;
	
	private final int boardWidth = 10, boardHeight = 20; //20 blocks high & 10 blocks wide
	
	private int[][] board = new int[boardHeight][boardWidth];
	
	private Shape[] shapes = new Shape[7]; //array of shapes with 7 elements
	
	private Shape currentShape; //the shape we are currently playing with
	
	private Timer timer;
	
	private final int FPS = 60; //we're running our game @ 60 frames per second
	
	private final int delay = 1000/60;
	
	private boolean gameOver = false;
	
	private int score = 0;
	

	
	public Board() {
		try {
			blocks = ImageIO.read(Board.class.getResource("tiles.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		timer = new Timer(delay, new ActionListener() {

			@Override
			/*
			 * When key is pressed, update location of shape
			 */
			public void actionPerformed(ActionEvent e) {
				
				update();
				repaint();
			}
			
		});
	   
		
		
		timer.start();
		
		
		/*
		 * We fill the shapes array with the different tetris shapes
		 */
		
		shapes[0] = new Shape(blocks.getSubimage(0, 0, blockSize, blockSize), new int[][] {
			{1, 1, 1, 1} //the long shape (I-shape)	
		}, 1, this);
		
		shapes[1] = new Shape(blocks.getSubimage(blockSize, 0, blockSize, blockSize), new int[][] {
			{1, 1, 0},
			{0, 1, 1}  //the Z-shape	
		}, 2, this);
		
		shapes[2] = new Shape(blocks.getSubimage(blockSize*2, 0, blockSize, blockSize), new int[][] {
			{0, 1, 1},
			{1, 1, 0}  //the S-shape	
		}, 3, this);
		
		shapes[3] = new Shape(blocks.getSubimage(blockSize*3, 0, blockSize, blockSize), new int[][] {
			{1, 1, 1},
			{0, 0, 1}  //the J-shape	
		}, 4, this);
		
		shapes[4] = new Shape(blocks.getSubimage(blockSize*4, 0, blockSize, blockSize), new int[][] {
			{1, 1, 1},
			{1, 0, 0}  //the L-shape	
		}, 5, this);
		
		shapes[5] = new Shape(blocks.getSubimage(blockSize*5, 0, blockSize, blockSize), new int[][] {
			{1, 1, 1},
			{0, 1, 0}  //the T-shape	
		}, 6, this);
		
		shapes[6] = new Shape(blocks.getSubimage(blockSize*6, 0, blockSize, blockSize), new int[][] {
			{1, 1},
			{1, 1}  //the square	
		}, 7, this);
		
		setNextShape(); 
	}
	
	public void update() {
		currentShape.update();
		if (gameOver) {
			timer.stop();
		}
	}
	
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //parent
		
		currentShape.render(g);
		/*
		 * Makes shapes pile up on the bottom of the board
		 */
		for(int row = 0; row < board.length; row++) {
			for(int col=0; col < board[row].length; col++) {
				if (board[row][col] != 0) {
				g.drawImage(blocks.getSubimage((board[row][col] - 1)*blockSize, 0, blockSize, blockSize), col*blockSize, row*blockSize, null);
				  }
				}
		}
		
		/*
		 * Draws the horizontal and vertical lines representing the playing area
		 */
		
		for (int i = 0; i < boardHeight; i++) {
			g.drawLine(0, i*blockSize, boardWidth*blockSize, i*blockSize);
		}
		for (int j = 0; j < boardWidth; j++) {
			g.drawLine(j*blockSize, 0, j*blockSize, boardHeight*blockSize);
		}
		
		
	}
	
	public void setNextShape() {
		//randomly generates a shape
		int index = (int) (Math.random()*shapes.length);
		
		Shape newShape = new Shape(shapes[index].getBlock(), shapes[index].getCoords(), shapes[index].getColor(), this);
		
		currentShape = newShape;
		
		for(int row = 0; row < currentShape.getCoords().length; row++) { 
			for(int col=0; col < currentShape.getCoords()[row].length; col++) {
				if(currentShape.getCoords()[row][col] != 0)
					{
						if(board[row][col + 3] != 0)
							{
								gameOver = true;							
						    }
					}
				}
	         }
		}
	
	public void someoneScored()
	{
	    score++;

	    // scoreLabel.setText("Score: " + score);
	}
	
	public int getBlockSize() {
		return this.blockSize;
	}
     
	public int[][] getBoard() {
		return board;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		/*
		 * Decides what to do when certain keys are pressed
		 */
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			currentShape.setDeltaX(-1); //if we press left key, we move one position to the left
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			currentShape.setDeltaX(1); //if we press right key, we move one position to the right
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentShape.speedDown(); //if we press down key, we fall down faster
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			currentShape.rotate(); //if we press up key, we rotate the shape
		}
		
		if(e.getKeyCode() == KeyEvent.VK_T) {
			; //if we press up key, we rotate the shape
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Resume moving in normal speed when down key is released
		
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentShape.normalSpeed(); //if we press down key, we fall down faster
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
