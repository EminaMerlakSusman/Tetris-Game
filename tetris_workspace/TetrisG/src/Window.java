import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
public class Window {
	/*
	 * Class describing the properties of the window
	 */
	public static final int WIDTH = 318, HEIGHT = 640;
	private JFrame window;
	private Board board;
	public Window() {
		window = new JFrame("Tetris Game");
		window.setSize(318, 640);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		
		board = new Board(); //initialize board
		window.setVisible(true);
		
		window.add(board); //add board component to window
		window.addKeyListener(board);
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Creates a new window object
		new Window();
	}

}
