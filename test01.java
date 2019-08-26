package shootgame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import javax.swing.JFrame;

public class test01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new test02().start();
		new Sub_Thread().sub_start();
	}

}
class test02 extends JFrame implements KeyListener, Runnable, MouseListener {
	
	static int x = 100 , y = 300;
	int map_x_1 = 0, map_x_2 = 1280;
	int f_width = 1280, f_height=710;
	static Toolkit tk = Toolkit.getDefaultToolkit();
	static Image ironman = tk.getImage("D://images//ironman_up.png");
	Image bullet = tk.getImage("D://images//bullet.png");
	Image kill = tk.getImage("D://images//kill//kill0.png");
	Image map = tk.getImage("D://images//map.jpg");
	Image map2 = tk.getImage("D://images//map2.jpg");
	Image empty_bar = tk.getImage("D://images//hp//EmptyBar.png");
	Image hp_bar = tk.getImage("D://images//hp//RedBar.png");
	Image coin = tk.getImage("D://images//coin.gif");
	Image kill_bullet = tk.getImage("D://images//kill_bullet.png");
	Image beam;
	Image score;
	Image bullet_explo;
	Image buffImage = null;
	Graphics buffg = null;
	Thread th;
	static ArrayList<Bullet> bullet_list = new ArrayList<Bullet>();
	static ArrayList<Kill> kill_list = new ArrayList<Kill>();
	static ArrayList<Kill_Bullet> kill_bullet_list = new ArrayList<Kill_Bullet>();
	static ArrayList<Explosion> explo_list = new ArrayList<Explosion>();
	static ArrayList<Bullet_Explosion> bullet_explo_list = new ArrayList<Bullet_Explosion>();
	static ArrayList<Coin_Effect> coin_list = new ArrayList<Coin_Effect>();
	Image explosion;
	Bullet b;
	Bullet_Explosion be;
	Kill k;
	Kill_Bullet kb;
	Explosion e;
	Coin_Effect c;
	static int bullet_count = 0, kill_count = 0, right_button_count = 0, beam_count = 0, beam_img=0;
	static boolean left, right, up, down, left_button, left_button_count, right_button;
	static boolean beam_check;
	static boolean right_beam, crash_x, crash_y;
	boolean test_count_1, test_count_2;
	static int timer = 0, timer_count = 0;

	public void start() {
		hp_bar = tk.getImage("D://images//hp//RedBar.png");
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
		Draw_Score();
		Draw_Bullet();
		Draw_Kill();
		Draw_Kill_Bullet();
		Draw_Explosion();
		Draw_Bullet_Explosion();
		Draw_Beam();
		Draw_Coin();
		Map_Move();
		g.drawImage(buffImage, 0, 0, this);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true) {
				KeyProcess();
				TimerProcess();
				repaint();
				Thread.sleep(16);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void TimerProcess() {
		timer_count+=16;
		if(timer_count>=1000) {
			timer_count = 0;
			timer++;
			System.out.println("진행시간 : "+timer+"초");
		}
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
	public void Draw() {
		buffg.clearRect(0, 30, f_width, f_height);
		if(test_count_1 == false) 
		{			
			bullet_explo_list.add(new Bullet_Explosion(-100, -100));
			be = bullet_explo_list.get(0);
			be.next_img();
			if(be.bullet_effect_num >= 37) {
				bullet_explo_list.remove(0);				
				test_count_1 = true;			
			}
		}
		if(test_count_2 == false) 
		{			
			explo_list.add(new Explosion(-100, -100));
			e = explo_list.get(0);
			e.next_img();
			if(e.explo_effect_num >= 19) {
				bullet_explo_list.remove(0);				
				test_count_2 = true;			
			}
		}
		buffg.drawImage(map, map_x_1, 0, this);
		buffg.drawImage(map2, map_x_2, 0, this);
		buffg.drawImage(ironman, x, y, this);
	}
	public void Draw_Score() { //점수 그리는 메소드
		Score sc = new Score();
		
		int num_1 = sc.score/1000;
		int num_2 = (sc.score%1000)/100;
		int num_3 = (sc.score%100)/10;
		int num_4 = sc.score%10;
		
		score = tk.getImage("D://images//number//"+num_1+".png");
		buffg.drawImage(score, 1000, 40, this);
		score = tk.getImage("D://images//number//"+num_2+".png");
		buffg.drawImage(score, 1030, 40, this);
		score = tk.getImage("D://images//number//"+num_3+".png");
		buffg.drawImage(score, 1060, 40, this);
		score = tk.getImage("D://images//number//"+num_4+".png");
		buffg.drawImage(score, 1090, 40, this);
	}
	public void Draw_Bullet() { //미사일 그리는 메소드
		for(int i=0; i<bullet_list.size(); i++) {
			if(bullet_list.size() > 0) {
				b = (Bullet) bullet_list.get(i);
				buffg.drawImage(bullet, b.pos.x, b.pos.y+20, this);
				b.move();
				if(b.pos.x > f_width) {
					bullet_list.remove(i);
				}
			}
		}
	}
	public void Draw_Kill() { // 적 이미지 그리는 메소드 
		for(int i=0; i<kill_list.size(); i++) {
			if(kill_list.size() > 0) {
				k = (Kill) kill_list.get(i);
				kill = tk.getImage("D://images//kill//kill"+(int)k.effect_num+".png");
				buffg.drawImage(kill, k.kill_pos.x, k.kill_pos.y, this);
				buffg.drawImage(empty_bar, k.kill_pos.x, k.kill_pos.y+kill.getHeight(null), this);
				buffg.drawImage(hp_bar, k.kill_pos.x, k.kill_pos.y+kill.getHeight(null),(int)k.width,17, this);
				k.move();
				if(k.cool == true) {
					k.effect_num+=0.1;
					if((int)k.effect_num == 3 && k.shoot == true) {
						kb = new Kill_Bullet(k.kill_pos.x, k.kill_pos.y+20);
						kill_bullet_list.add(kb);
						k.shoot = false;
					}
					if(k.effect_num >= 6.0) {
						k.effect_num = 0.0;
						k.cool = false;
						k.cooltime = timer;
					}					
				}
				if(k.kill_pos.x <= 0-kill.getWidth(this)) {
					kill_list.remove(i);
				}
			}
		}
	}
	public void Draw_Kill_Bullet() { //적 미사일 이미지 그리는 메소드
		for(int v=0; v<kill_bullet_list.size(); v++) {
			kb = (Kill_Bullet)kill_bullet_list.get(v);
			buffg.drawImage(kill_bullet, kb.pos.x, kb.pos.y, this);
			kb.move();
		}
	}
	public void Draw_Explosion()  { //폭발이펙트 출력메소드
		for(int i=0; i<explo_list.size(); i++) {
			e = (Explosion) explo_list.get(i);
			explosion = tk.getImage("D://images//explosion//explosion"+e.explo_effect_num+".png");
			buffg.drawImage(explosion, e.explo_pos.x, e.explo_pos.y, this);
			e.next_img();
			e.move();
			if(e.explo_effect_num >= 19) {
				explo_list.remove(i);
			}
		}
	}
	public void Draw_Bullet_Explosion()  { //폭발이펙트 출력메소드
		for(int i=0; i<bullet_explo_list.size(); i++) {
			be = (Bullet_Explosion) bullet_explo_list.get(i);
			bullet_explo = tk.getImage("D://images//bullet_explo//bullet_explo"+be.bullet_effect_num+".png");
			buffg.drawImage(bullet_explo, be.explo_pos.x, be.explo_pos.y, this);
			be.move();
			be.next_img();
			if(be.bullet_effect_num >= 37) {
				bullet_explo_list.remove(i);
			}
		}
	}
	public void Draw_Beam() { // 빔 이미지 그리는 메소드
		if(right_beam == true) {
			beam = tk.getImage("D://images//beam//beam"+beam_img+".png");
			buffg.drawImage(beam, x+30, y-5, this);
			if(beam_count < 100) {
				beam_img++;				
			} else if(beam_count > 100) {
				beam_img--;
			}
			beam_count++;
			if(beam_count < 100 && beam_img==9) {
				beam_img = 3;
			}
			if(beam_count > 100 && beam_img==0) {
				right_beam = false;
				right_button = false;
				beam_count = 0;
				beam_img = 0;
				ironman = tk.getImage("D://images//ironman_up.png");
			}
		}
	}
	public void Draw_Coin() {
		for(int i=0; i<coin_list.size(); i++) {
			c = (Coin_Effect) coin_list.get(i);
			buffg.drawImage(coin, c.pos.x, c.pos.y, this);
			c.move();
			if(c.pos.y > f_height || c.pos.x < 0) {
				coin_list.remove(i);
			}
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

		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
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
class Score {
	static int score=0;
	
	public void score_plus(int x) {
		score += x;
	}
}
class Kill {
	Point kill_pos;
	double max_hp = 3;
	double hp = 3;
	int ironman_damage = 1;
	double width = 124.0;
	double effect_num = 0.0;
	boolean cool;
	boolean shoot;
	int cooltime = test02.timer;
	
	Kill(int x, int y) {
		kill_pos = new Point(x, y);
	}
	public void move() {
		kill_pos.x -= 3;
		if(test02.timer - cooltime == 1) {
			cool = true;
			shoot = true;
		}
	}
	public void attack() {
		hp -= ironman_damage;
		width = new test02().hp_bar.getWidth(null)*(hp/max_hp);
	}
}
class Kill_Bullet {
	Point pos;
	boolean shoot;
	
	Kill_Bullet(int x, int y) {
		pos = new Point(x, y);
	}
	public void move() {
		if(pos.x < test02.x) {
			pos.x+=2;
		} else if(pos.x > test02.x) {
			pos.x-=2;
		}
		if(pos.y < test02.y) {
			pos.y+=2;
		} else if(pos.y > test02.y) {
			pos.y-=2;
		}
	}
}
class Explosion {
	Point explo_pos;
	int explo_effect_num = 0;
	
	Explosion(int x, int y) {
		explo_pos = new Point(x, y);
	}
	public void move() {
		explo_pos.x -= 5;
	}
	public void next_img() {
		explo_effect_num++;
	}
}
class Bullet_Explosion {
	Point explo_pos;
	int bullet_effect_num = 0;
	
	Bullet_Explosion(int x, int y) {
		explo_pos = new Point(x, y);
	}
	public void move() {
		explo_pos.x -= 5;
	}
	public void next_img() {
		bullet_effect_num++;
	}
}
class Coin_Effect {
	Point pos;
	int coin_y = 0;
	
	Coin_Effect(int x, int y) {
		pos = new Point(x, y);
	}
	public void move() {
		if(coin_y < 30) {
			pos.y -= 2;	
			pos.x -= 7;
		} else {
			pos.y += 2;
			pos.x -= 3;
		}
		coin_y++;
	}
}
class Sub_Thread extends Thread {
	
	test02 t = new test02();
	
	public void sub_start() {
		Thread th2 = new Thread(this);
		th2.start();
	}
	public void run() {
		while(true) {
			BulletProcess();
			KillProcess();
			LaserProcess();
			Bullet_Check();
			Beam_Check();
			Char_Check();
			Coin_Check();
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void BulletProcess() {
		t.bullet_count++;
		if(t.left_button == true && t.bullet_count>=7) {
			t.b = new Bullet(t.x+t.ironman.getWidth(null), t.y);
			t.bullet_list.add(t.b);
			t.bullet_count = 0;
		}
	}
	public void KillProcess() {
		//t.k = new Kill(t.f_width, (int)(Math.random()*((t.f_height-t.kill.getHeight(null)-100)))+30);
		if(t.timer_count == 16 && t.timer > 0) {
			if(t.timer%3 == 0) {
				KillProduce(100,3);
			} else if(t.timer%7 == 0) {
				KillProduce(500,3);
			}
		}
	}
	public void KillProduce(int y, int num) {
		for(int p=0; p<num; p++) {
			t.k = new Kill(t.f_width+(p*150), y);
			t.kill_list.add(t.k);
		}
	}
	public void LaserProcess() {
		if(t.right_button == true && t.right_beam==false) {
			t.right_button_count++;
			if(t.right_button_count >= 50) {
				t.ironman = t.tk.getImage("D://images//laser1.gif");
			}
		} else {
			t.right_button_count = 0;
		}
	}
	public void Bullet_Check() { //미사일과 적 이미지 충돌체크 메소드
		if(t.bullet_list.size() != 0) {
			for(int i=0; i<t.bullet_list.size(); i++) {
				for(int v=0; v<t.kill_list.size(); v++) {
					if(i!=t.bullet_list.size()) {
						t.b = (Bullet) t.bullet_list.get(i);
					}
					if(v!=t.kill_list.size()) {
						t.k = (Kill) t.kill_list.get(v);
					}
					exit_For:
					for(int x1=t.b.pos.x; x1<=t.b.pos.x+t.bullet.getWidth(null); x1++) {
						for(int x2=t.k.kill_pos.x; x2<=t.k.kill_pos.x+t.kill.getWidth(null); x2++) {
							if(x1 == x2) {
								t.crash_x = true;
								break exit_For;
							}
						}
					}
					exit_For:
					for(int y1=t.b.pos.y; y1<=t.b.pos.y+t.bullet.getHeight(null); y1++) {
						for(int y2=t.k.kill_pos.y; y2<=t.k.kill_pos.y+t.kill.getHeight(null); y2++) {
							if(y1 == y2) {
								t.crash_y = true;
								break exit_For;
							}
						}
					}
					if(t.crash_x == true && t.crash_y == true) {
						t.k.attack();
						if(t.bullet_list.size() > 0 && t.bullet_list.size()!=i) {
							t.bullet_list.remove(i);
						}
						t.be = new Bullet_Explosion(t.b.pos.x, t.b.pos.y);
						t.bullet_explo_list.add(t.be);
						if(t.k.hp == 0) {
							t.e = new Explosion(t.k.kill_pos.x, t.k.kill_pos.y);
							t.c = new Coin_Effect(t.k.kill_pos.x, t.k.kill_pos.y);
							new Score().score_plus(10);
							t.explo_list.add(t.e);
							t.coin_list.add(t.c);
							t.kill_list.remove(v);
						}
					}
					t.crash_x = false;
					t.crash_y = false;
				}
			}
		}
	}
	public void Beam_Check() {
		if(t.right_beam == true) {
			try {
				for(int v=0; v<t.kill_list.size(); v++) {
					if(v!=t.kill_list.size()) {
						t.k = (Kill) t.kill_list.get(v);
					}
					exit_For:
					for(int y1=(this.t.y-5+17); y1<(this.t.y-5+28); y1++) {
						for(int y2=t.k.kill_pos.y; y2<t.k.kill_pos.y+t.kill.getHeight(null); y2++) {
							if(y1 == y2) {
								t.beam_check = true;
								break exit_For;
							}
						}
					}
					if(t.beam_check == true) {
						t.c = new Coin_Effect(t.k.kill_pos.x, t.k.kill_pos.y);
						t.e = new Explosion(t.k.kill_pos.x, t.k.kill_pos.y);
						new Score().score_plus(10);
						t.explo_list.add(t.e);
						t.kill_list.remove(v);
						t.coin_list.add(t.c);
					}
					t.beam_check = false;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void Char_Check() { //캐릭터와 적 충돌체크
		try {
			for(int v=0; v<t.kill_list.size(); v++) {
				if(v!=t.kill_list.size()) {
					t.k = (Kill) t.kill_list.get(v);
				}
				exit_For:
				for(int x1=t.x; x1<t.x+t.ironman.getWidth(null); x1++) {
					for(int x2=t.k.kill_pos.x; x2<t.k.kill_pos.x+t.kill.getWidth(null); x2++) {
						if(x1 == x2) {
							t.crash_x = true;
							break exit_For;
						}
					}
				}
				exit_For:
				for(int y1=t.y; y1<t.y+t.ironman.getHeight(null); y1++) {
					for(int y2=t.k.kill_pos.y; y2<t.k.kill_pos.y+t.kill.getHeight(null); y2++) {
						if(y1 == y2) {
							t.crash_y = true;
							break exit_For;
						}
					}
				}
				if(t.crash_x == true && t.crash_y == true) {
					t.e = new Explosion(t.k.kill_pos.x, t.k.kill_pos.y);
					t.explo_list.add(t.e);
					t.kill_list.remove(v);
				}
				t.beam_check = false;
				t.crash_x = false;
				t.crash_y = false;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void Coin_Check() { //캐릭터와 적 충돌체크
		try {
			for(int v=0; v<t.coin_list.size(); v++) {
				if(v!=t.coin_list.size()) {
					t.c = (Coin_Effect) t.coin_list.get(v);
				}
				exit_For:
				for(int x1=t.x; x1<t.x+t.ironman.getWidth(null); x1++) {
					for(int x2=t.c.pos.x; x2<t.c.pos.x+t.coin.getWidth(null); x2++) {
						if(x1 == x2) {
							t.crash_x = true;
							break exit_For;
						}
					}
				}
				exit_For:
				for(int y1=t.y; y1<t.y+t.ironman.getHeight(null); y1++) {
					for(int y2=t.c.pos.y; y2<t.c.pos.y+t.coin.getHeight(null); y2++) {
						if(y1 == y2) {
							t.crash_y = true;
							break exit_For;
						}
					}
				}
				if(t.crash_x == true && t.crash_y == true) {
					t.coin_list.remove(v);
					new Score().score_plus(5);
				}
				t.crash_x = false;
				t.crash_y = false;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}