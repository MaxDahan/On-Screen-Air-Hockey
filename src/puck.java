import java.awt.Color;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class puck{
	//FIELDS
	private JFrame f_puck = new JFrame();
	
	private int velX, velY;
	
	private int mass = 100;
	
	private JLabel puck = new JLabel();
	
	private Thread move, bounce;
	
	//METHODS
	public static void main(String[] args) {
		new BF_main();
	}
	
	public puck() {
		f_puck.setUndecorated(true);
		f_puck.setBackground(new Color(0, 0, 0, 0));
		f_puck.setSize(200, 200);
		f_puck.setLocation(500, 500);
		f_puck.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f_puck.setAlwaysOnTop(true);
		f_puck.setVisible(true);
		f_puck.addKeyListener(new KeyListener() {{}
		
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
			
				if (key == KeyEvent.VK_C || key == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});
		
		File file = new File("data\\puck.png");
		String path = file.getAbsolutePath();
		file = new File(path);
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch(IOException e1) {e1.printStackTrace();}
		puck = new JLabel(new ImageIcon(image));
		f_puck.getContentPane().add(puck);
		f_puck.pack();
		puck.setBounds(0, 0, 100, 100);
		
		move = new vel();
		move.start();
		
		bounce = new bounce();
		bounce.start();
	}
	
	//Getters
	public JFrame getFrame() {
		return f_puck;
	}
	public int getX() {
		return f_puck.getX();
	}
	public int getY() {
		return f_puck.getY();
	}
	public int getVelX() {
		return velX;
	}
	public int getVelY() {
		return velY;
	}
	public int getWidth() {
		return f_puck.getWidth();
	}
	public int getHeight() {
		return f_puck.getHeight();
	}	
	public int getMass() {
		return mass;
	}
	
	//Setters
	public void setVelX(int velx) {
		velX = velx;
	}
	public void setVelY(int vely) {
		velY = vely;
	}
	
	//bounces off wall
	public class bounce extends Thread {
		public void run() {
			while(true) {
				if(f_puck.getX() >= 1820 - (f_puck.getWidth() + 10)) {
					if(velX >= 0) {
						velX *= -1;
					}
				} else if(f_puck.getY() >= 980 - (f_puck.getHeight() + 10)) {
					if(velY >= 0) {
						velY *= -1;
					}
				} else if(f_puck.getY() <= 100) {
					velY = Math.abs(velY);
				} else if(f_puck.getX() <= 100) {
					velX = Math.abs(velX);
				}
				System.out.println();
			}
		}
	}
	
	//Adds velx to x and vely to y
	public class vel extends Thread{
		public void run() {
			while(true) {
				try {Thread.sleep(15);}catch(InterruptedException e) {e.printStackTrace();}
				f_puck.setLocation(f_puck.getX() + velX, f_puck.getY() + velY);
			}
		}
	}
}
