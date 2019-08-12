package shootgame;

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
	
	int x = 100 , y = 300;
	int map_x_1 = 0, map_x_2 = 1280;
	int f_width = 1280, f_height=720;
	Toolkit tk = Toolkit.getDefaultToolkit();
	Image ironman = tk.getImage("D://images//ironman_up.png");
	Image bullet = tk.getImage("D://images//bullet.png");
	Image kill = tk.getImage("D://images//kill.png");
	Image map = tk.getImage("D://images//map.jpg");
	Image map2 = tk.getImage("D://images//map.jpg");
	Image buffImage = null;
	Graphics buffg = null;
	Thread th;
	ArrayList bullet_list = new ArrayList();
	ArrayList kill_list = new ArrayList();
	ArrayList explo_list = new ArrayList();
	ArrayList explo_img = new ArrayList();
	Bullet b;
	Kill k;
	Explosion e;
	int bullet_count = 0, kill_count = 0;
	boolean left, right, up, down, left_button, left_button_count;
	boolean bullet_check;
	
	public test02() {
		for(int i=0; i<20; i++) {
			explo_img.add(tk.getImage("D://images//.jpg"));
		}
		Dimension dim = new Dimension(f_width, f_height);
		setPreferredSize(dim);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(this);
		addMouseListener(this);
		setBackground(Color.white);
		setTitle("슈팅게임");
		setFocusable(true);
		setVisible(true);
		th = new Thread(this);
		th.start();
	}
	
	public void paint(Graphics g) {
		buffImage = createImage(f_width, f_height+15);
		buffg = buffImage.getGraphics();
		update(g);
	}
	public void update(Graphics g) {
		Draw();
		Draw_Bullet();
		Draw_Kill();
		Draw_Explosion();
		if(bullet_list.size() != 0) {
			Bullet_Check();
		}
		g.drawImage(buffImage, 0, 0, this);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true) {
				KeyProcess();
				BulletProcess();
				KillProcess();
				Map_Move();
				repaint();
				Thread.sleep(20);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void Draw() {
		buffg.clearRect(0, 15, f_width, f_height);
		buffg.drawImage(map, map_x_1, 0, this);
		buffg.drawImage(map2, map_x_2, 0, this);
		buffg.drawImage(ironman, x, y, this);
	}
	public void KeyProcess() {
		if(up == true && y>15) {
			y-=10;
		}
		if(down == true && y<600) {
			y+=10;
		}
		if(left == true && x>200) {
			x-=10;
		}
		if(right == true && x<1000){
			x+=10;
		}
	}
	public void BulletProcess() {
		bullet_count++;
		if(left_button == true && bullet_count>=7) {
			b = new Bullet(x+ironman.getWidth(this), y);
			bullet_list.add(b);
			bullet_count = 0;
		}
	}
	public void KillProcess() {
		kill_count++;
		if(kill_count>=20) {
			k = new Kill(f_width, (int)(Math.random()*(f_height-kill.getHeight(this)+16)));
			kill_list.add(k);
			kill_count = 0;
		}
	}
	public void Bullet_Check() { //미사일과 적 이미지 충돌체크 메소드
		try {
			for(int i=0; i<bullet_list.size(); i++) {
				for(int v=0; v<kill_list.size(); v++) {
					if(bullet_list.size() != 0) {
						b = (Bullet) bullet_list.get(i);
					}
					if(kill_list.size() != 0) {
						k = (Kill) kill_list.get(v);
					}
					if((b.pos.x >= k.kill_pos.x && b.pos.x <= k.kill_pos.x+kill.getWidth(this))
						&& (b.pos.y >= k.kill_pos.y && b.pos.y <= k.kill_pos.y+kill.getHeight(this))) {
						bullet_check = true;
					}
					if(bullet_check == true) {
						//e = new Explosion(k.kill_pos.x, k.kill_pos.y);
						//explo_list.add(e);
						bullet_list.remove(i);
						kill_list.remove(v);
					}
					bullet_check = false;
				}
			}
		} catch(Exception e) {
			
		}
		
	}
	public void Map_Move() { // 맵 이동 메소드
		map_x_1-=2;
		map_x_2-=2;
		if(map_x_1 == (f_width*-1)) {
			map_x_1 = f_width;
		}
		if(map_x_2 == (f_width*-1)) {
			map_x_2 = f_width;
		}
	}
	public void Draw_Bullet() { //미사일 그리는 메소드
		for(int i=0; i<bullet_list.size(); i++) {
			b = (Bullet) bullet_list.get(i);
			buffg.drawImage(bullet, b.pos.x, b.pos.y+20, this);
			b.move();
			if(b.pos.x > f_width) {
				bullet_list.remove(i);
			}
		}
	}
	public void Draw_Kill() { // 적 이미지 그리는 메소드 
		for(int i=0; i<kill_list.size(); i++) {
			k = (Kill) kill_list.get(i);
			buffg.drawImage(kill, k.kill_pos.x, k.kill_pos.y, this);
			k.move();
			if(k.kill_pos.x <= 0-kill.getWidth(this)) {
				kill_list.remove(i);
			}
		}
	}
	public void Draw_Explosion()  { //폭발이펙트 출력메소드
		for(int i=0; i<explo_list.size(); i++) {
			e = (Explosion) explo_list.get(i);
			Image explosion = tk.getImage("D://images//explosion.gif");
			buffg.drawImage(explosion, e.explo_pos.x, e.explo_pos.y, this);
			e.move();
			if(e.explo_pos.x <= 0-explosion.getWidth(this)) {
				explo_list.remove(i);
			}
		}
	}
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			up = true;
			ironman = tk.getImage("D://images//ironman_up.png");
			break;
		case KeyEvent.VK_A:
			left = true;
			ironman = tk.getImage("D://images//ironman_left.png");
			break;
		case KeyEvent.VK_S:
			down = true;
			ironman = tk.getImage("D://images//ironman_down.png");
			break;
		case KeyEvent.VK_D:
			right = true;
			ironman = tk.getImage("D://images//ironman_right.png");
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
class Kill {
	Point kill_pos;
	
	Kill(int x, int y) {
		kill_pos = new Point(x, y);
	}
	public void move() {
		kill_pos.x -= 5;
	}
}
class Explosion {
	Point explo_pos;
	
	Explosion(int x, int y) {
		explo_pos = new Point(x, y);
	}
	public void move() {
		explo_pos.x -= 5;
	}
}
class Explosion_Img {
	
	
	
	Explosion_Img() {
		
	}
}