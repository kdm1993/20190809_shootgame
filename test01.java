import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;

public class test01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new test02();
	}

}
class test02 extends JFrame implements KeyListener, Runnable, MouseListener {
	
	int x = 60 , y = 100;
	int f_width = 1280, f_height=720;
	Toolkit tk = Toolkit.getDefaultToolkit();
	Image ironman = tk.getImage("D://images//ironman.png");
	Image bullet = tk.getImage("D://images//bullet.gif");
	Image buffImage = null;
	Graphics buffg = null;
	Thread th;
	ArrayList bullet_list = new ArrayList();
	Bullet b;
	int bullet_count = 0;
	boolean left, right, up, down, left_button, left_button_count;
	
	public test02() {
		Dimension dim = new Dimension(f_width, f_height);
		setPreferredSize(dim);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(this);
		addMouseListener(this);
		setTitle("½´ÆÃ°ÔÀÓ");
		setFocusable(true);
		setVisible(true);
		th = new Thread(this);
		th.start();
	}
	
	public void paint(Graphics g) {
		buffImage = createImage(f_width, f_height);
		buffg = buffImage.getGraphics();
		
		update(g);
	}
	public void update(Graphics g) {
		Draw();
		Draw_Bullet();
		g.drawImage(buffImage, x, y, this);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true) {
				KeyProcess();
				BulletProcess();
				repaint();
				Thread.sleep(20);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void Draw() {
		buffg.clearRect(0, 0, 1280, 720);
		buffg.drawImage(ironman, x, y, this);
	}
	public void KeyProcess() {
		if(up == true && y>15) {
			y-=5;
		}
		if(down == true && y<300) {
			y+=5;
		}
		if(left == true && x>59) {
			x-=5;
		}
		if(right == true && x<500){
			x+=5;
		}
	}
	public void BulletProcess() {
		if(left_button == true) {
			b = new Bullet(x, y);
			bullet_list.add(b);
		}
	}
	public void Draw_Bullet() {
		for(int i=0; i<bullet_list.size(); i++) {
			b = (Bullet) bullet_list.get(i);
			buffg.drawImage(bullet, b.pos.x+100, b.pos.y+40, this);
			b.move();
			if(b.pos.x > f_width) {
				bullet_list.remove(i);
			}
		}
	}
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			up = true;
			break;
		case KeyEvent.VK_A:
			left = true;
			break;
		case KeyEvent.VK_S:
			down = true;
			break;
		case KeyEvent.VK_D:
			right = true;
			break;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			up = false;
			break;
		case KeyEvent.VK_A:
			left = false;
			break;
		case KeyEvent.VK_S:
			down = false;
			break;
		case KeyEvent.VK_D:
			right = false;
			break;
		}
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		left_button = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		left_button = false;
	}
}
class Bullet {
	Point pos;
	
	Bullet(int x, int y) {
		pos = new Point(x, y);
	}
	public void move() {
		pos.x += 10;
	}
}