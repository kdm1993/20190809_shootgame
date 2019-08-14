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
	int f_width = 1280, f_height=710;
	Toolkit tk = Toolkit.getDefaultToolkit();
	Image ironman = tk.getImage("D://images//ironman_up.png");
	Image bullet = tk.getImage("D://images//bullet.png");
	Image kill = tk.getImage("D://images//kill.png");
	Image map = tk.getImage("D://images//map.jpg");
	Image map2 = tk.getImage("D://images//map2.png");
	Image beam;
	Image buffImage = null;
	Graphics buffg = null;
	Thread th;
	ArrayList bullet_list = new ArrayList();
	ArrayList kill_list = new ArrayList();
	ArrayList explo_list = new ArrayList();
	Image explosion;
	Bullet b;
	Kill k;
	Explosion e;
	int bullet_count = 0, kill_count = 0, right_button_count = 0, beam_count = 0, beam_img=0;
	boolean left, right, up, down, left_button, left_button_count, right_button;
	boolean bullet_check_x, bullet_check_y, beam_check;
	boolean right_beam, char_check_x, char_check_y;
	
	public test02() {
		Dimension dim = new Dimension(1280, 720);
		setPreferredSize(dim);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(this);
		addMouseListener(this);
		setTitle("슈팅게임");
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
		Draw_Kill();
		Draw_Explosion();
		Draw_Beam();
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
				LaserProcess();
				Bullet_Check();
				Beam_Check();
				Char_Check();
				Map_Move();
				repaint();
				Thread.sleep(20);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void Draw() {
		buffg.clearRect(0, 30, f_width, f_height);
		buffg.drawImage(map, map_x_1, 0, this);
		buffg.drawImage(map2, map_x_2, 0, this);
		buffg.setColor(Color.blue);
		buffg.setFont(new Font("휴먼둥근헤드라인", Font.BOLD, 30));
		buffg.drawString("SCORE : " + new Score().score, 1000, 58);
		buffg.drawImage(ironman, x, y, this);
	}
	public void KeyProcess() {
		if(up == true && y>30) {
			y-=10;
		}
		if(down == true && y<580) {
			y+=10;
		}
		if(left == true && x>0) {
			x-=10;
		}
		if(right == true && x<1000){
			x+=10;
		}
	}
	public void BulletProcess() {
		bullet_count++;
		if(left_button == true && bullet_count>=7) {
			b = new Bullet(x+ironman.getWidth(null), y);
			bullet_list.add(b);
			bullet_count = 0;
		}
	}
	public void KillProcess() {
		kill_count++;
		if(kill_count>=20) {
			k = new Kill(f_width, (int)(Math.random()*((f_height-kill.getHeight(null)-100)))+30);
			kill_list.add(k);
			kill_count = 0;
		}
	}
	public void LaserProcess() {
		if(right_button == true && right_beam==false) {
			right_button_count++;
			if(right_button_count >= 50) {
				ironman = tk.getImage("D://images//laser1.gif");
			}
		} else {
			right_button_count = 0;
		}
	}
	public void Bullet_Check() { //미사일과 적 이미지 충돌체크 메소드
		try {
			for(int i=0; i<bullet_list.size(); i++) {
				for(int v=0; v<kill_list.size(); v++) {
					if(i!=bullet_list.size()) {
						b = (Bullet) bullet_list.get(i);
					}
					if(v!=kill_list.size()) {
						k = (Kill) kill_list.get(v);
					}
					for(int x1=b.pos.x; x1<b.pos.x+bullet.getWidth(null); x1++) {
						for(int x2=k.kill_pos.x; x2<k.kill_pos.x+kill.getWidth(null); x2++) {
							if(x1 == x2) {
								bullet_check_x = true;
							}
						}
					}
					for(int y1=b.pos.y; y1<b.pos.y+bullet.getHeight(null); y1++) {
						for(int y2=k.kill_pos.y; y2<k.kill_pos.y+kill.getHeight(null); y2++) {
							if(y1 == y2) {
								bullet_check_y = true;
							}
						}
					}
					if(bullet_check_x == true && bullet_check_y == true) {
						e = new Explosion(k.kill_pos.x, k.kill_pos.y);
						new Score().score_plus(10);
						explo_list.add(e);
						bullet_list.remove(i);
						kill_list.remove(v);
					}
					bullet_check_x = false;
					bullet_check_y = false;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public void Beam_Check() {
		if(right_beam == true) {
			try {
				for(int v=0; v<kill_list.size(); v++) {
					if(v!=kill_list.size()) {
						k = (Kill) kill_list.get(v);
					}
					for(int y1=(this.y-5+17); y1<(this.y-5+28); y1++) {
						for(int y2=k.kill_pos.y; y2<k.kill_pos.y+kill.getHeight(null); y2++) {
							if(y1 == y2) {
								beam_check = true;
								break;
							}
						}
					}
					if(beam_check == true) {
						e = new Explosion(k.kill_pos.x, k.kill_pos.y);
						new Score().score_plus(10);
						explo_list.add(e);
						kill_list.remove(v);
					}
					beam_check = false;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void Char_Check() { //캐릭터와 적 충돌체크
		try {
			for(int v=0; v<kill_list.size(); v++) {
				if(v!=kill_list.size()) {
					k = (Kill) kill_list.get(v);
				}
				for(int x1=x; x1<x+ironman.getWidth(null); x1++) {
					for(int x2=k.kill_pos.x; x2<k.kill_pos.x+kill.getWidth(null); x2++) {
						if(x1 == x2) {
							char_check_x = true;
							break;
						}
					}
				}
				for(int y1=y; y1<y+ironman.getHeight(null); y1++) {
					for(int y2=k.kill_pos.y; y2<k.kill_pos.y+kill.getHeight(null); y2++) {
						if(y1 == y2) {
							char_check_y = true;
							break;
						}
					}
				}
				if(char_check_x == true && char_check_y) {
					e = new Explosion(k.kill_pos.x, k.kill_pos.y);
					explo_list.add(e);
					kill_list.remove(v);
				}
				beam_check = false;
				char_check_x = false;
				char_check_y = false;
			}
		} catch(Exception e) {
			e.printStackTrace();
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
			explosion = tk.getImage("D://images//explosion//explosion"+e.effect_num+".png");
			buffg.drawImage(explosion, e.explo_pos.x, e.explo_pos.y, this);
			e.next_img();
			if(e.effect_num >= 19) {
				explo_list.remove(i);
			}
			e.move();
		}
	}
	public void Draw_Beam() { // 적 이미지 그리는 메소드
		if(right_beam == true) {
			beam = tk.getImage("D://images//beam//beam"+beam_img+".png");
			buffg.drawImage(beam, x+30, y-5, this);
			beam_img++;
			beam_count++;
			if(beam_count < 100 && beam_img==6) {
				beam_img = 0;
			}
			if(beam_count > 100 && beam_img==9) {
				right_beam = false;
				right_button = false;
				beam_count = 0;
				beam_img = 0;
				ironman = tk.getImage("D://images//ironman_up.png");
			}
		}
	}
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			up = true;
			if(right_button == false && right_beam == false) {
				ironman = tk.getImage("D://images//ironman_up.png");
			}
			break;
		case KeyEvent.VK_A:
			left = true;
			if(right_button == false && right_beam == false) {
				ironman = tk.getImage("D://images//ironman_left.png");
			}
			break;
		case KeyEvent.VK_S:
			down = true;
			if(right_button == false && right_beam == false) {
				ironman = tk.getImage("D://images//ironman_down.png");
			}
			break;
		case KeyEvent.VK_D:
			right = true;
			if(right_button == false && right_beam == false) {
				ironman = tk.getImage("D://images//ironman_right.png");
			}
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
		if(e.isMetaDown() && left_button == false && right_beam == false) {
			right_button = true;
			ironman = tk.getImage("D://images//laser.gif");
		} else if(right_button == false && right_beam == false) {
			left_button = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.isMetaDown()) {
			if(right_button_count < 50 && right_beam==false) {
				ironman = tk.getImage("D://images//ironman_up.png");
				right_button = false;
			} else if(right_button_count >= 50){
				right_beam = true;
				right_button_count = 0;
				ironman = tk.getImage("D://images//ironman_laser.png");
			}
		} else{
			left_button = false;
		}
	}
}
class Bullet {
	Point pos;
	
	Bullet(int x, int y) {
		pos = new Point(x, y);
	}
	public void move() {
		pos.x += 30;
	}
}
class Laser {
	Point pos;
	int laser_img = 0;
	
	Laser(int x, int y) {
		pos = new Point(x, y);
	}
	public void laser_move() {
		laser_img++;
		if(laser_img >= 6) {
			laser_img = 0;
		}
	}
}
class Score {
	static int score=0;
	
	public void score_plus(int x) {
		score += x;
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
	int effect_num = 0;
	
	Explosion(int x, int y) {
		explo_pos = new Point(x, y);
	}
	public void move() {
		explo_pos.x -= 5;
	}
	public void next_img() {
		effect_num++;
	}
}