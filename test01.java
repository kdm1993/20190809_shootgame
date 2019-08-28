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
	static int player_1_hp = 107, player_2_hp = 107;
	static int player_1_mana = 109, player_2_mana = 109;
	static int player_1_damage = 1, player_2_damage = 1;
	int map_x_1 = 0, map_x_2 = 1280;
	int f_width = 1280, f_height=710;
	static Toolkit tk = Toolkit.getDefaultToolkit();
	static Image ironman = tk.getImage("D://images//ironman_up.png");
	Image bullet = tk.getImage("D://images//bullet.png");
	Image kill = tk.getImage("D://images//kill//kill0_0.png");
	Image map = tk.getImage("D://images//map.jpg");
	Image map2 = tk.getImage("D://images//map2.jpg");
	Image empty_bar = tk.getImage("D://images//hp//EmptyBar.png");
	Image hp_bar = tk.getImage("D://images//hp//RedBar.png");
	Image coin = tk.getImage("D://images//coin.gif");
	Image kill_bullet = tk.getImage("D://images//kill_bullet.png");
	Image char_ui;
	Image beam;
	Image item_get = tk.getImage("D://images//item_get//item_get0.png");
	Image hit_explo;
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
	static ArrayList<Hit_Explosion> hit_list = new ArrayList<Hit_Explosion>();
	static ArrayList<Item_Effect> item_list = new ArrayList<Item_Effect>();
	static ArrayList<Item_Get_Effect> item_get_list = new ArrayList<Item_Get_Effect>();
	Image explosion;
	Bullet b;
	Bullet_Explosion be;
	Kill k;
	Kill_Bullet kb;
	Explosion e;
	Coin_Effect c;
	Hit_Explosion he;
	Item_Effect ie;
	Item_Get_Effect ige;
	static int bullet_count = 0, kill_count = 0, right_button_count = 0, beam_count = 0, beam_img=0;
	static boolean left, right, up, down, left_button, left_button_count, right_button;
	static boolean beam_check;
	static boolean right_beam;
	static int timer = 0, timer_count = 0;
	boolean test_count_1, test_count_2, test_count_3, test_count_4;

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
		Draw_Bullet();
		Draw_Kill();
		Draw_Hit_Explosion();
		Draw_Kill_Bullet();
		Draw_Explosion();
		Draw_Bullet_Explosion();
		Draw_Beam();
		Draw_Coin();
		Draw_Item();
		Draw_Game_UI();
		Draw_Score();
		Draw_Item_effect();
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
			bullet_explo_list.add(new Bullet_Explosion(-500, -500));
			be = bullet_explo_list.get(0);
			be.next_img();
			if(be.effect_num >= 37.0) {
				bullet_explo_list.remove(0);				
				test_count_1 = true;			
			}
		}
		if(test_count_2 == false) 
		{			
			explo_list.add(new Explosion(-500, -500));
			e = explo_list.get(0);
			e.next_img();
			if(e.effect_num >= 19.0) {
				bullet_explo_list.remove(0);
				test_count_2 = true;			
			}
		}
		if(test_count_3 == false) 
		{			
			kill_list.add(new Kill(-500, -500));
			k = kill_list.get(0);
			k.next_img();
			if(k.effect_num >= 5.0) {
				kill_list.remove(0);
				test_count_3 = true;			
			}
		}
		if(test_count_4 == false) 
		{			
			item_get_list.add(new Item_Get_Effect(-500, -500));
			ige = item_get_list.get(0);
			ige.next_img();
			if(ige.effect_num >= 19.0) {
				item_get_list.remove(0);
				test_count_4 = true;			
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
		buffg.drawImage(score, 300, 80, this);
		score = tk.getImage("D://images//number//"+num_2+".png");
		buffg.drawImage(score, 330, 80, this);
		score = tk.getImage("D://images//number//"+num_3+".png");
		buffg.drawImage(score, 360, 80, this);
		score = tk.getImage("D://images//number//"+num_4+".png");
		buffg.drawImage(score, 390, 80, this);
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
				kill = tk.getImage("D://images//kill//kill"+k.kill_num+"_"+(int)k.effect_num+".png");
				buffg.drawImage(kill, k.pos.x, k.pos.y, this);
				buffg.drawImage(empty_bar, k.pos.x, k.pos.y+kill.getHeight(null), this);
				buffg.drawImage(hp_bar, k.pos.x, k.pos.y+kill.getHeight(null),(int)k.width,17, this);
				k.move();
				if(k.cool == true) {
					k.effect_num+=0.1;
					if((int)k.effect_num == 3 && k.shoot == true) {
						kb = new Kill_Bullet(k.pos.x, k.pos.y+20);
						kill_bullet_list.add(kb);
						k.shoot = false;
					}
					if(k.effect_num >= 6.0) {
						k.effect_num = 0.0;
						k.cool = false;
						k.shoot = true;
						k.cooltime = timer;
					}
				}
				if(k.pos.x <= 0-kill.getWidth(this)) {
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
			explosion = tk.getImage("D://images//explosion//explosion"+(int)e.effect_num+".png");
			buffg.drawImage(explosion, e.pos.x, e.pos.y, this);
			e.next_img();
			e.move();
			if(e.effect_num >= 19.0) {
				explo_list.remove(i);
			}
		}
	}
	public void Draw_Bullet_Explosion()  { //폭발이펙트 출력메소드
		for(int i=0; i<bullet_explo_list.size(); i++) {
			be = (Bullet_Explosion) bullet_explo_list.get(i);
			bullet_explo = tk.getImage("D://images//bullet_explo//bullet_explo"+(int)be.effect_num+".png");
			buffg.drawImage(bullet_explo, be.pos.x, be.pos.y, this);
			be.move();
			be.next_img();
			if(be.effect_num >= 37) {
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
	public void Draw_Item() {
		for(int i=0; i<item_list.size(); i++) {
			ie = (Item_Effect) item_list.get(i);
			buffg.drawImage(ie.item, ie.pos.x, ie.pos.y, this);
			ie.move();
			if(ie.pos.y > f_height || ie.pos.x < 0) {
				item_list.remove(i);
			}
		}
	}
	public void Draw_Item_effect() {
		for(int i=0; i<item_get_list.size(); i++) {
			ige = (Item_Get_Effect) item_get_list.get(i);
			item_get = tk.getImage("D://images//item_get//item_get"+(int)ige.effect_num+".png");
			buffg.drawImage(item_get, ige.pos.x, ige.pos.y, this);
			ige.move();
			ige.next_img();
			if(ige.effect_num >= 19.0) {
				item_get_list.remove(i);
			}
		}
	}
	public void Draw_Hit_Explosion() {
		for(int i=0; i<hit_list.size(); i++) {
			he = (Hit_Explosion) hit_list.get(i);
			hit_explo = tk.getImage("D://images//hit//hit"+(int)he.effect_num+".png");
			buffg.drawImage(hit_explo, he.pos.x, he.pos.y, this);
			//he.move();
			he.next_img();
			if(he.effect_num >= 7.0) {
				hit_list.remove(i);
			}
		}
	}
	public void Draw_Game_UI() {
		char_ui = tk.getImage("D://images//ui//ironman_char_ui.png");
		buffg.drawImage(char_ui, 20, 40, this);
		char_ui = tk.getImage("D://images//ui//hp.png");
		buffg.drawImage(char_ui, 136, 96, player_1_hp, 9, this);
		char_ui = tk.getImage("D://images//ui//mana.png");
		buffg.drawImage(char_ui, 134, 110, this);
		/*
		int x = f_width/2;
		char_ui = tk.getImage("D://images//ui//warmachine_char_ui.png");
		buffg.drawImage(char_ui, 20+x, 40, this);
		char_ui = tk.getImage("D://images//ui//hp.png");
		buffg.drawImage(char_ui, 136+x, 96, this);
		char_ui = tk.getImage("D://images//ui//mana.png");
		buffg.drawImage(char_ui, 134+x, 110, this);
		*/
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
class Effect_Class {
	Point pos;
	double effect_num = 0;

	public void move() {
		pos.x -= 2;
	}
	public void next_img() {
		effect_num += 1;
	}
}
class Bullet extends Effect_Class {
	
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
class Kill extends Effect_Class {
	double max_hp = 3;
	double hp = 3;
	double width = 124.0;
	boolean cool;
	boolean shoot = true;
	int cooltime = test02.timer;
	int kill_num;
	
	Kill(int x, int y) {
		pos = new Point(x, y);
	}
	public void move() {
		pos.x -= 3;
		if(test02.timer - cooltime == 1) {
			cool = true;
		}
	}
	public void attack() {
		hp -= test02.player_1_damage;
		width = new test02().hp_bar.getWidth(null)*(hp/max_hp);
	}
}
class Kill_Bullet extends Effect_Class {

	boolean shoot = true;
	double shoot_x = 0, shoot_y = 0;
	double move_x=0, move_y=0;
	int damage = 1;
	
	Kill_Bullet(int x, int y) {
		pos = new Point(x, y);
		move_x = ((test02.x+test02.ironman.getWidth(null)/2)-pos.x)/4;
		move_y = ((test02.y+test02.ironman.getHeight(null)/2)-pos.y)/move_x;
		if(move_y > 4) {
			move_y = 4;
		}
		if(move_y < -4) {
			move_y = -4;
		}
		shoot_x = pos.x;
		shoot_y = pos.y;
	}
	public void move() {
		shoot_x -= 4;
		shoot_y += -move_y;
		pos.setLocation(shoot_x, shoot_y);
		pos.x = (int)shoot_x;
		pos.y = (int)shoot_y;
	}
}
class Explosion extends Effect_Class {
	Explosion(int x, int y) {
		pos = new Point(x, y);
	}
	public void next_img() {
		effect_num+=0.5;
	}
}
class Bullet_Explosion extends Effect_Class {
	Bullet_Explosion(int x, int y) {
		pos = new Point(x, y);
	}
}
class Coin_Effect extends Effect_Class {
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
class Hit_Explosion extends Effect_Class {
	
	Hit_Explosion(int x, int y) {
		pos = new Point(x, y);
	}
	public void next_img() {
		effect_num+=0.1;
	}
}
class Item_Effect extends Effect_Class {
	
	int item_num;
	String item_name;
	double move_y = 5;
	boolean move;
	Image item;
	
	Item_Effect(int x, int y) {
		pos = new Point(x, y);
		item_num = (int)(Math.random()*9);
		if(item_num == 0 || item_num == 1 || item_num == 7) {
			item_name = "heart";
		} else if(item_num == 2 || item_num == 3 || item_num == 8) {
			item_name = "energy";
		} else if(item_num == 4) {
			item_name = "damage";
		} else if(item_num == 5) {
			item_name = "attackspeed";
		} else if(item_num == 6) {
			item_name = "pierce";
		}
		item = test02.tk.getImage("D://images//item//"+item_name+".gif");
	}
	public void move() {
		pos.x -= 3;
		if(move == false) {
			pos.y--;
			move_y-=0.2;
		} else if(move == true) {
			pos.y++;
			move_y+=0.2;
		}
		if(move_y <= 0.0) {
			move = true;
		} else if(move_y >= 10.0) {
			move = false;
		}
	}
}
class Item_Get_Effect extends Effect_Class {
	
	Item_Get_Effect(int x, int y) {
		pos = new Point(x, y);
	}
	public void next_img() {
		effect_num+=0.5;
	}
}
class Sub_Thread extends Thread {
	
	test02 t = new test02();
	
	public void sub_start() {
		start();
	}
	public void run() {
		while(true) {
			BulletProcess();
			KillProcess();
			LaserProcess();
			Beam_Check();
			Char_Check();
			Coin_Check();
			Bullet_Check();
			Hit_Bullet_Check();
			Item_Check();
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
			if(t.timer==1) {
				KillProduce(t.f_width, 100, 3, 0);
				KillProduce(t.f_width+450, 100, 1, 1);
			}
			if(t.timer%5 == 0) {
				KillProduce(t.f_width, 100, 3 , 0);
				KillProduce(t.f_width+450, 100, 1, 1);
			} else if(t.timer%11 == 0) {
				KillProduce(t.f_width, 500, 3, 0);
				KillProduce(t.f_width+450, 100, 1, 1);
			}
		}
	}
	public void KillProduce(int x, int y, int num, int kill_num) {
		for(int p=0; p<num; p++) {
			t.k = new Kill(x+(p*150), y);
			t.kill_list.add(t.k);
			t.k.kill_num = kill_num;
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
	public void Hit_Bullet_Check() { //캐릭터와 적 미사일 충돌체크 메소드
		if(t.kill_bullet_list.size() != 0) {
			for(int i=0; i<t.kill_bullet_list.size(); i++) {
				if(i!=t.kill_bullet_list.size()) {
					t.kb = (Kill_Bullet) t.kill_bullet_list.get(i);
				}
				if(Crash(t.x+23, t.y+17, t.kb.pos.x, t.kb.pos.y, t.ironman, t.kill_bullet, -31, -45)) {
					if(t.kill_bullet_list.size() > 0) {
						t.kill_bullet_list.remove(i);
					}
					t.he = new Hit_Explosion(t.kb.pos.x-16, t.kb.pos.y-18);
					t.hit_list.add(t.he);
					t.player_1_hp -= t.kb.damage;
				}
			}
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
					if(Crash(t.b.pos.x, t.b.pos.y, t.k.pos.x, t.k.pos.y, t.bullet, t.kill)) {
						t.k.attack();
						if(t.bullet_list.size() > 0 && t.bullet_list.size()!=i) {
							t.bullet_list.remove(i);
						}
						t.be = new Bullet_Explosion(t.b.pos.x, t.b.pos.y);
						t.bullet_explo_list.add(t.be);
						if(t.k.hp == 0) {
							t.e = new Explosion(t.k.pos.x, t.k.pos.y-50);
							t.c = new Coin_Effect(t.k.pos.x, t.k.pos.y);
							new Score().score_plus(10);
							t.explo_list.add(t.e);
							t.coin_list.add(t.c);
							t.kill_list.remove(v);
							if(t.k.kill_num == 1) {
								item_drop();
							}
						}
					}
				}
			}
		}
	}
	public void item_drop() {
		t.ie = new Item_Effect(t.k.pos.x+27, t.k.pos.y+20);
		t.item_list.add(t.ie);
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
						for(int y2=t.k.pos.y; y2<t.k.pos.y+t.kill.getHeight(null); y2++) {
							if(y1 == y2) {
								t.beam_check = true;
								break exit_For;
							}
						}
					}
					if(t.beam_check == true) {
						t.c = new Coin_Effect(t.k.pos.x, t.k.pos.y);
						t.e = new Explosion(t.k.pos.x, t.k.pos.y);
						new Score().score_plus(10);
						t.explo_list.add(t.e);
						t.kill_list.remove(v);
						t.coin_list.add(t.c);
						if(t.k.kill_num == 1) {
							item_drop();
						}
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
				if(v!=t.kill_list.size() && t.kill_list.size() != 0) {
					t.k = (Kill) t.kill_list.get(v);
				}
				if(Crash(t.x+23, t.y+17, t.k.pos.x, t.k.pos.y, t.ironman, t.kill, -31, -45)) {
					t.e = new Explosion(t.k.pos.x, t.k.pos.y);
					t.explo_list.add(t.e);
					t.kill_list.remove(v);
				}
				t.beam_check = false;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void Coin_Check() { //캐릭터와 코인 충돌체크
		try {
			for(int v=0; v<t.coin_list.size(); v++) {
				if(v!=t.coin_list.size()) {
					t.c = (Coin_Effect) t.coin_list.get(v);
				}
				if(Crash(t.x, t.y, t.c.pos.x, t.c.pos.y, t.ironman, t.coin)) {
					t.coin_list.remove(v);
					new Score().score_plus(5);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void Item_Check() { //캐릭터와 아이템 충돌체크
		try {
			for(int v=0; v<t.item_list.size(); v++) {
				if(v!=t.item_list.size()) {
					t.ie = (Item_Effect) t.item_list.get(v);
				}
				if(Crash(t.x, t.y, t.ie.pos.x, t.ie.pos.y, t.ironman, t.ie.item)) {
					t.item_list.remove(v);
					t.ige = new Item_Get_Effect(t.ie.pos.x-t.item_get.getWidth(null)/2+5, t.ie.pos.y-t.item_get.getHeight(null)/2+5);
					t.item_get_list.add(t.ige);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	//충돌검사 메소드1 - 일반
	public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2) {
		
		boolean check = false;
		
		if((x1<x2+img2.getWidth(null)) &&
			(x1+img1.getWidth(null)>x2) &&
			(y1<y2+img2.getHeight(null)) &&
			(y1+img1.getHeight(null)>y2)) {
			check = true;
			return check;
		}
		return check;
	}
	//충돌검사 메소드2 - 이미지 크기 제어용
	//캐릭터 히트박스(+23,+17,-31,-45)
	public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2, int minus_x, int minus_y) {
		
		boolean check = false;
		
		if((x1<x2+img2.getWidth(null)) &&
			(x1+img1.getWidth(null)+minus_x>x2) &&
			(y1<y2+img2.getHeight(null)) &&
			(y1+img1.getHeight(null)+minus_y>y2)) {
			check = true;
			return check;
		}
		return check;
	}
}