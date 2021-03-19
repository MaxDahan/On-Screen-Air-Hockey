import java.awt.Color;
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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class BF_main {
	
	//FIELDS
	private JFrame f_bouncer = new JFrame();
	
	private int mouseLasty, mouseLastx, plus_hold_timer;
	
	private puck puck = new puck();
	
	private int mass = 10;
	
	private int velX, velY, lastX, lastY;
	
	private Thread collide, getVel, slowDown;
	
	private mouseAdapt ma = new mouseAdapt();
	
	private JLabel hitter = new JLabel();
	
	//METHODS
	public static void main(String[] args) {
		new BF_main();
	}
	
	public BF_main() {
		f_bouncer.setUndecorated(true);
		f_bouncer.setBackground(new Color(0, 0, 0, 0));
		f_bouncer.setSize(200, 200);
		f_bouncer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f_bouncer.setAlwaysOnTop(true);
		f_bouncer.setVisible(true);
		f_bouncer.addMouseListener(ma);
		f_bouncer.addKeyListener(new KeyListener() {{}
		
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_C || key == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			}
		}
		
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
		});
		
		File file = new File("data\\hitter.png");
		String path = file.getAbsolutePath();
		file = new File(path);
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch(IOException e1) {e1.printStackTrace();}
		hitter = new JLabel(new ImageIcon(image));
		f_bouncer.getContentPane().add(hitter);
		f_bouncer.pack();
		hitter.setBounds(0, 0, 155, 155);

		collide = new collide();
		collide.start();
		
		getVel = new getVel();
		getVel.start();
	}
	
	public void bounceOff() {
		int newVelX2 = ((puck.getVelX()) * (puck.getMass() - mass) + (2 * mass * velX)) / (mass + puck.getMass());
		int newVelY2 = ((puck.getVelY()) * (puck.getMass() - mass) + (2 * mass * velY)) / (mass + puck.getMass());
		puck.setVelX(newVelX2); puck.setVelY(newVelY2);
	}
	
	public class getVel extends Thread {
		private boolean first = true;
		public void run() {
			while(true) {
				if(first) {
					lastX = f_bouncer.getX();
					lastY = f_bouncer.getY();
					first = false;
				} else if(!first) {
					velX = (f_bouncer.getX() - lastX);
					velY = (f_bouncer.getY() - lastY);
					first = true;
				}
				try {Thread.sleep(40);} catch(InterruptedException e) {e.printStackTrace();}
			}
		}
	}
	
	public class slowDown extends Thread {
		public void run() {
			while(true) {
				if(puck.getVelX() > 0) {
					puck.setVelX(puck.getVelX() - 1);
				}
				if(puck.getVelY() > 0) {
					puck.setVelY(puck.getVelY() - 1);
				}
				if(puck.getVelX() < 0) {
					puck.setVelX(puck.getVelX() + 1);
				}
				if(puck.getVelY() < 0) {
					puck.setVelY(puck.getVelY() + 1);
				}
				try {Thread.sleep(5);} catch(InterruptedException e) {e.printStackTrace();}
			}
		}
	}
	
	public class collide extends Thread {
		private boolean added = false;
		public void run() {
			while(true) {
				double xDif = (f_bouncer.getX() + f_bouncer.getWidth()/2) - (puck.getX() + puck.getWidth()/2);
				double yDif = (f_bouncer.getY() + f_bouncer.getHeight()/2) - (puck.getY() + puck.getHeight()/2);
				
			    double distance = Math.sqrt((Math.pow(xDif, 2) + Math.pow(yDif, 2)));
			    
			    //like a thread.sleep but quicker
			    System.out.println();
			    
				if(distance < (f_bouncer.getWidth()/2 + puck.getWidth()/2)) {
					if(!added) {
						slowDown = new slowDown();
						slowDown.start();
						added = true;
					} else if(added) {
						added = false;
						slowDown.stop();
					}
					bounceOff();
				}
			}
		}
	}
	
	public class mouseAdapt extends MouseAdapter {
		private Thread move_plus = new Thread();
    	private Thread plus_follow = new Thread();
        public void mousePressed(MouseEvent e) {
        	move_plus = new move_plus();
        	plus_follow = new plus_follow();
        	move_plus.start();
        	plus_follow.start();
        }
        public void mouseReleased(MouseEvent e) {
        	plus_follow.stop();
        	move_plus.stop();
        	plus_hold_timer = 0;
        }
	}
	
	private class plus_follow extends Thread {
		private boolean first = true;
		public void run() {
			while(true) {
				try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
				Point mousePos = MouseInfo.getPointerInfo().getLocation();
				if(!first) {
					mouseLastx = (int)mousePos.getX() - mouseLastx;
					mouseLasty = (int)mousePos.getY() - mouseLasty;
					f_bouncer.setLocation(f_bouncer.getX() + mouseLastx, f_bouncer.getY() + mouseLasty);
				}
				first = false;
				mouseLastx = (int)mousePos.getX();
				mouseLasty = (int)mousePos.getY();
			}
		}
	}
	
	//Helper class for plus_follow
	private class move_plus extends Thread {
		public void run() {
			while(true) {
				plus_hold_timer++;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
