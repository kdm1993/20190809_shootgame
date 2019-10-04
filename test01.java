package shootgame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;

public class test01 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test02 t = new test02();
		t.game_start();
		t.th.start();
		new Sub_Thread().start();
	}
}

class test02 extends JFrame implements KeyListener, Runnable, MouseListener {

	static boolean dual = true;
	static int player_1_state = 0;
	static int player_2_state = 0;
	double player_1_spark_count = 0;
	double player_2_spark_count = 0;
	//0 : 생존		1 : 죽음	 2 : 움직일수없음
	int player_1_die_x = 0, player_1_die_y = 0;
	int player_2_die_x = 0, player_2_die_y = 0;
	static boolean player_1_state_count;
	static boolean player_2_state_count;
	static int player_1_x = 100, player_1_y = 200;
	static int player_2_x = 100, player_2_y = 400;
	static int player_1_hp = 107, player_2_hp = 107;
	static int player_1_mana = 109, player_2_mana = 109;
	static int player_1_damage = 1, player_2_damage = 1;
	static int player_1_as = 1, player_2_as = 1;
	static int player_1_pierce = 0, player_2_pierce = 0;
	static int player_1_score = 0, player_2_score = 0;
	static String player_1_char = "warmachine", player_2_char = "ironman";
	int map_x_1 = 0, map_x_2 = 1280;
	int f_width = 1280, f_height = 710;
	static Toolkit tk = Toolkit.getDefaultToolkit();
	static Image player_1_char_img = tk.getImage("D://images//ironman_up.png");
	static Image player_2_char_img = tk.getImage("D://images//ironman_up.png");
	Image bullet = tk.getImage("D://images//bullet.png");
	Image gun_bullet = tk.getImage("D://images//bullet//bullet0.png");
	Image kill = tk.getImage("D://images//kill//kill0_0.png");
	Image trans = tk.getImage("D://images//trans.png");
	Image map = tk.getImage("D://images//map.jpg");
	Image map2 = tk.getImage("D://images//map2.jpg");
	Image empty_bar = tk.getImage("D://images//hp//EmptyBar.png");
	Image hp_bar = tk.getImage("D://images//hp//RedBar.png");
	Image coin = tk.getImage("D://images//coin.gif");
	static Image ironman_sub = tk.getImage("D://images//ironman_sub.png");
	Image booster = tk.getImage("D://images//booster//booster0.png");
	Image char_ui;
	Image beam;
	Image item_get = tk.getImage("D://images//item_get//item_get0.png");
	Image hit_explo;
	Image score;
	Image bullet_explo;
	Image buffImage = null;
	Graphics buffg = null;
	Thread th = new Thread(this);
	static ArrayList<Bullet> p_1_bullet_list = new ArrayList<Bullet>();
	static ArrayList<Bullet> p_2_bullet_list = new ArrayList<Bullet>();
	static ArrayList<Kill> kill_list = new ArrayList<Kill>();
	static ArrayList<Effect> effect_list = new ArrayList<Effect>();
	static ArrayList<Ironman_sub> ironman_sub_list = new ArrayList<Ironman_sub>();
	Image explosion;
	Bullet b;
	Effect effect;
	Kill k;
	static Ironman_sub sub;
	static int bullet_count_1 = 0, bullet_count_2 = 0, bullet_count_3 = 0, kill_count = 0, right_button_count = 0,
			beam_count = 0, beam_img = 0;
	static boolean player_1_left, player_1_right, player_1_up, player_1_down, player_1_left_button,
			player_1_left_button_count, player_1_right_button;
	static boolean player_2_left, player_2_right, player_2_up, player_2_down, player_2_left_button,
			player_2_left_button_count, player_2_right_button;
	static boolean beam_check;
	static boolean gun_check;
	static boolean right_beam;
	boolean charge_count;
	boolean warmachine_left_count, warmachine_right_count;
	static int timer = 0, timer_count = 0;
	boolean test_count;

	public void game_start() {
		hp_bar = tk.getImage("D://images//hp//RedBar.png");
		player_1_char_img = tk.getImage("D://images//" + player_1_char + "_up.png");
		player_2_char_img = tk.getImage("D://images//" + player_2_char + "_up.png");
		Dimension dim = new Dimension(1280, 720);
		setPreferredSize(dim);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(this);
		addMouseListener(this);
		setTitle("슈팅게임");
		setFocusable(true);
		setVisible(true);
		setResizable(false);
		//th.start();
	}

	public void paint(Graphics g) {
		buffImage = createImage(f_width, f_height);
		buffg = buffImage.getGraphics();
		update(g);
	}

	public void update(Graphics g) {
		Draw_Map();
		Draw_P1_Bullet();
		Draw_P2_Bullet();
		Draw_Kill();
		Draw_Diying();
		Draw_Beam();
		LaserProcess();
		Draw_Ironman_sub();
		Draw();
		Draw_Effect();
		Draw_Score();
		Draw_Game_UI();
		Map_Move();
		Player_state();
		g.drawImage(buffImage, 0, 0, this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (true) {
				long start = System.currentTimeMillis();
				for(int x=0; x<4; x++) {
					if (player_1_char.equals("ironman") && player_1_pierce < 4) {
						sub = new Ironman_sub(-100, -100);
						effect = new Effect(-100-booster.getWidth(null), -100, 10);
						ironman_sub_list.add(sub);
						effect_list.add(effect);
						player_1_pierce++;
					} else if(player_2_char.equals("ironman") && player_2_pierce < 4) {
						sub = new Ironman_sub(-100, -100);
						effect = new Effect(-100-booster.getWidth(null), -100, 10);
						ironman_sub_list.add(sub);
						effect_list.add(effect);
						player_2_pierce++;
					}
				}
				if(test_count == false) {
					test_count = true;
					for(int x=1; x<14; x++) {
						effect = new Effect(-5000, -5000, x, true);
						effect_list.add(effect);
					}
				}
				KeyProcess();
				//TimerProcess();
				repaint();
				long end = System.currentTimeMillis();
				if((end-start) > 1) {
					System.out.println("쓰레드 1 실행 시간 : " + (end-start) + "ms");					
				}
				if((end-start) < 16) {					
					Thread.sleep(16-(end-start));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void TimerProcess() {
		timer_count += 16;
		if (timer_count >= 1000) {
			timer_count = 0;
			timer++;
			System.out.println("진행시간 : " + timer + "초");
		}
	}

	public void KeyProcess() {
		if (player_1_up == true && player_1_y > 30) {
			player_1_y -= 10;
		}
		if (player_1_down == true && player_1_y < 580) {
			player_1_y += 10;
		}
		if (player_1_left == true && player_1_x > 0) {
			player_1_x -= 10;
		}
		if (player_1_right == true && player_1_x < 1000) {
			player_1_x += 10;
		}
		if (player_2_up == true && player_2_y > 30) {
			player_2_y -= 10;
		}
		if (player_2_down == true && player_2_y < 580) {
			player_2_y += 10;
		}
		if (player_2_left == true && player_2_x > 0) {
			player_2_x -= 10;
		}
		if (player_2_right == true && player_2_x < 1000) {
			player_2_x += 10;
		}
	}
	public void Draw_Map() {
		buffg.clearRect(0, 30, f_width, f_height);
		buffg.drawImage(map, map_x_1, 0, this);
		buffg.drawImage(map2, map_x_2, 0, this);
	}
	public void Draw() {
		Image warmachine_gun;
		warmachine_gun = tk.getImage("D://images//warmachine_gun.png");
		if (player_1_char.equals("warmachine") && player_1_state == 0) {
			buffg.drawImage(warmachine_gun, player_1_x + 20, player_1_y, this);
		} else if (player_2_char.equals("warmachine") && dual == true && player_1_state == 0) {
			buffg.drawImage(warmachine_gun, player_2_x + 20, player_2_y, this);
		}
		if(player_1_state == 0) {
			buffg.drawImage(player_1_char_img, player_1_x, player_1_y, this);			
		} else if(player_1_state == 2) {
			if(player_1_state_count == false) {
				buffg.drawImage(trans, player_1_x, player_1_y, this);
			} else if(player_1_state_count == true) {
				buffg.drawImage(player_1_char_img, player_1_x, player_1_y, this);
			}
			player_1_state_count = player_1_state_count == false ? true : false;
		}
		if(player_2_state == 0) {
			buffg.drawImage(player_2_char_img, player_2_x, player_2_y, this);
		} else if(player_2_state == 2) {
			if(player_2_state_count == false) {
				buffg.drawImage(trans, player_2_x, player_2_y, this);
			} else if(player_2_state_count == true) {
				buffg.drawImage(player_2_char_img, player_2_x, player_2_y, this);
			}
			player_2_state_count = player_2_state_count == false ? true : false;
		}
	}

	public void Draw_Score() { // 점수 그리는 메소드

		int num_1 = player_1_score / 1000;
		int num_2 = (player_1_score % 1000) / 100;
		int num_3 = (player_1_score % 100) / 10;
		int num_4 = player_1_score % 10;

		score = tk.getImage("D://images//number//" + num_1 + ".png");
		buffg.drawImage(score, 300, 80, this);
		score = tk.getImage("D://images//number//" + num_2 + ".png");
		buffg.drawImage(score, 330, 80, this);
		score = tk.getImage("D://images//number//" + num_3 + ".png");
		buffg.drawImage(score, 360, 80, this);
		score = tk.getImage("D://images//number//" + num_4 + ".png");
		buffg.drawImage(score, 390, 80, this);

		num_1 = player_2_score / 1000;
		num_2 = (player_2_score % 1000) / 100;
		num_3 = (player_2_score % 100) / 10;
		num_4 = player_2_score % 10;

		score = tk.getImage("D://images//number//" + num_1 + ".png");
		buffg.drawImage(score, 940, 80, this);
		score = tk.getImage("D://images//number//" + num_2 + ".png");
		buffg.drawImage(score, 970, 80, this);
		score = tk.getImage("D://images//number//" + num_3 + ".png");
		buffg.drawImage(score, 1000, 80, this);
		score = tk.getImage("D://images//number//" + num_4 + ".png");
		buffg.drawImage(score, 1030, 80, this);
	}

	public void Draw_P1_Bullet() { // 플레이어 1 미사일 그리는 메소드
		for (int i = 0; i < p_1_bullet_list.size(); i++) {
			if (p_1_bullet_list.size() != 0) {
				b = (Bullet) p_1_bullet_list.get(i);
				if (player_1_char.equals("ironman") && b != null) {
					buffg.drawImage(bullet, b.pos.x, b.pos.y + 20, this);
					b.move();
				}
				if (player_1_char.equals("warmachine") && b != null) {
					gun_bullet = tk.getImage("D://images//bullet//bullet"+b.gun_type+"_"+b.bullet_type+".png");
					buffg.drawImage(gun_bullet, b.pos.x, b.pos.y + 20, this);
					b.move();
				}
				if(b != null) {
					if (b.pos.x > f_width) {
						p_1_bullet_list.remove(i);
					}					
				}
			}
		}
	}

	public void Draw_P2_Bullet() { // 플레이어 2 미사일 그리는 메소드
		try {
			for (int i = 0; i < p_2_bullet_list.size(); i++) {
				if (p_2_bullet_list.size() > 0) {
					b = (Bullet) p_2_bullet_list.get(i);
					if (player_2_char.equals("ironman") && b != null) {
						buffg.drawImage(bullet, b.pos.x, b.pos.y + 20, this);
						b.move();
					}
					if (player_2_char.equals("warmachine") && b != null) {
						gun_bullet = tk.getImage("D://images//bullet//bullet"+b.gun_type+"_"+b.bullet_type+".png");
						buffg.drawImage(gun_bullet, b.pos.x, b.pos.y + 20, this);
						b.move();
					}
					if(b != null) {
						if (b.pos.x > f_width && b != null) {
							p_2_bullet_list.remove(i);
						}						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Draw_Kill() { // 적 이미지 그리는 메소드
		for (int i = 0; i < kill_list.size(); i++) {
			if (kill_list.size() > 0) {
				k = (Kill) kill_list.get(i);
				kill = tk.getImage("D://images//kill//kill" + k.kill_num + "_" + (int)k.effect_num + ".png");
				buffg.drawImage(kill, k.pos.x, k.pos.y, this);
				//buffg.drawImage(empty_bar, k.pos.x, k.pos.y + kill.getHeight(null), this);
				//buffg.drawImage(hp_bar, k.pos.x, k.pos.y + kill.getHeight(null), (int) k.width, 17, this);
				if(k.kill_num != 2 || k.cool == false) {
					k.move(k.kill_num);					
				}
				if(k.kill_num < 2) {
					k.effect_num += 0.1;
					if (k.cool == true && (int)k.effect_num == 3
						&& (player_1_state == 0 || player_2_state == 0)) {
						effect = new Effect(k.pos.x, k.pos.y + 20, 1);
						effect_list.add(effect);
						k.cool = false;
					}					
					if (k.effect_num >= 6.0) {
						k.effect_num = 0.0;
						k.cool = true;
						k.cooltime = timer;
					}
				} else if(k.kill_num == 2) {
					if(k.cool == true && k.effect_num < 10.0) {
						k.effect_num = 10.0;
					}
					k.effect_num += 0.1;
					if((int)k.effect_num == 10 && k.cool == false) {
						k.effect_num = 0.0;
					} else if((int)k.effect_num == 19 && k.cool == true && kill_list.size() != 0) {
						kill_list.remove(i);
					}
				}
				if (k.pos.x <= 0 - kill.getWidth(this)) {
					kill_list.remove(i);
				}
			}
		}
	}

	public void Draw_Effect() { // 이펙트 그리는 메소드
		for (int v = 0; v < effect_list.size(); v++) {
			effect = (Effect) effect_list.get(v);
			if(effect != null) {
				if(effect.effect_code == 1) {
					effect.effect_img = tk.getImage("D://images//kill_bullet.png");				
				} else if(effect.effect_code == 2) {
					effect.effect_img = tk.getImage("D://images//explosion//explosion"+(int)effect.effect_num+".png");
				} else if(effect.effect_code == 3) {
					effect.effect_img = tk.getImage("D://images//bullet_explo//bullet_explo"+(int)effect.effect_num+".png");
				} else if(effect.effect_code == 4) {
					effect.effect_img = tk.getImage("D://images//bullet_explo//bullet_explo2_"+(int)effect.effect_num+".png");
				} else if(effect.effect_code == 5) {
					effect.effect_img = tk.getImage("D://images//coin.gif");
				} else if(effect.effect_code == 6) {
					effect.effect_img = tk.getImage("D://images//hit//hit" + (int) effect.effect_num + ".png");
				} else if(effect.effect_code == 7) {
					effect.effect_img = tk.getImage("D://images//charge//charge" + (int) effect.effect_num + ".png");
				} else if(effect.effect_code == 8) {
					effect.effect_img = tk.getImage("D://images//hit//bullet_hit" + (int) effect.effect_num + ".png");
				} else if(effect.effect_code == 9) {
					effect.effect_img = tk.getImage("D://images//item_get//item_get" + (int) effect.effect_num + ".png");
				} else if(effect.effect_code == 10) {
					effect.effect_img = tk.getImage("D://images//booster//booster" + (int) effect.effect_num + ".png");
				} else if(effect.effect_code == 11) {
					effect.effect_img = tk.getImage("D://images//item//" + effect.item_name + ".gif");
				} else if(effect.effect_code == 12) {
					effect.effect_img = tk.getImage("D://images//spark//spark_"+ (int)effect.effect_num +".png");
				} else if(effect.effect_code == 13) {
					effect.effect_img = tk.getImage("D://images//char_explo//char_explo_"+ (int)effect.effect_num +".png");
				}
				buffg.drawImage(effect.effect_img, effect.pos.x, effect.pos.y, this);
				effect.move();
				effect.next_img();
				if (effect.effect_num >= 19.0 && effect.effect_code == 2) {
					effect_list.remove(v);
				} else if (effect.effect_num >= 37 && effect.effect_code == 3) {
					effect_list.remove(v);
				} else if (effect.effect_num >= 8 && effect.effect_code == 4) {
					effect_list.remove(v);
				} else if(effect.effect_code == 7) {
					if(effect.effect_num >= 12.0) {
						effect.effect_num = 0.0;
					}
					if(player_1_char.equals("ironman")) {
						if(player_1_right_button == true && right_beam == true) {
							effect_list.remove(v);
						} else if(player_1_right_button == false) {
							effect_list.remove(v);
						}
					} else if(player_2_char.equals("ironman")) {
						if(player_2_right_button == true && right_beam == true) {
							effect_list.remove(v);
						} else if(player_2_right_button == false) {
							effect_list.remove(v);
						}
					}
				} else if(effect.effect_code == 8) {
					if(effect.effect_num >= 7.0) {
						effect.effect_num = 0.0;						
					}
					if(player_1_char.equals("warmachine")) {
						if(player_1_left_button == false && effect.player_num == 1) {
							effect_list.remove(v);
						} else if(player_1_right_button == false && effect.player_num == 2) {
							effect_list.remove(v);
						}
					} else if(player_2_char.equals("warmachine")) {
						if(player_2_left_button == false && effect.player_num == 1) {
							effect_list.remove(v);
						} else if(player_2_right_button == false && effect.player_num == 2) {
							effect_list.remove(v);
						}
					}
				} else if(effect.effect_code == 9 && effect.effect_num >= 18.0) {
					effect_list.remove(v);
				} else if(effect.effect_code == 10 && effect.effect_num >= 18.0) {
					effect.effect_num = 0.0;
				} else if(effect.effect_code == 11 && (effect.pos.y > f_height || effect.pos.x < 0)) {
					effect_list.remove(v);
				} else if(effect.effect_code == 12 && effect.effect_num >= 3.0) {
					effect.effect_num = 0.0;
					if(effect.player_num == 1) {
						player_1_spark_count++;
						if(player_1_spark_count > 10) {
							player_1_spark_count = 0;
							effect_list.remove(v);
							effect = new Effect(player_1_die_x-480, player_1_die_y-200, 13, 1);
							effect_list.add(effect);
							player_1_die_x = 0;
							player_1_die_y = 0;
						}
					} else if(effect.player_num == 2) {
						player_2_spark_count++;
						if(player_2_spark_count > 10) {
							player_2_spark_count = 0;
							effect_list.remove(v);
							effect = new Effect(player_2_die_x-480, player_2_die_y-200, 13, 2);
							effect_list.add(effect);
							player_2_die_x = 0;
							player_2_die_y = 0;
						}
					}
				} else if(effect.effect_code == 13) {
					if(effect.effect_num >= 12.0 && effect.player_num == 1 && player_1_state == 2) {
						player_1_state = 1;
					}
					if(effect.effect_num >= 12.0 && effect.player_num == 1 && player_2_state == 2) {
						player_2_state = 1;
					}
					if(effect.effect_num >= 34.0) {
						effect_list.remove(v);
					}
				}
			}
		}
	}
	public void Draw_Beam() { // 빔 이미지 그리는 메소드
		if (right_beam == true && player_1_char.equals("ironman")) {
			charge_count = false;
			beam = tk.getImage("D://images//beam//beam" + beam_img + ".png");
			buffg.drawImage(beam, player_1_x + 30, player_1_y - 5, this);
			beam = tk.getImage("D://images//beam//beam.png");
			for (int s = 0; s < ironman_sub_list.size(); s++) {
				sub = ironman_sub_list.get(s);
				buffg.drawImage(beam, sub.pos.x + 12, sub.pos.y - 22, this);
			}
			if (beam_count < 100) {
				beam_img++;
			} else if (beam_count > 100) {
				beam_img--;
			}
			beam_count++;
			if (beam_count < 100 && beam_img == 9) {
				beam_img = 3;
			}
			if (beam_count > 100 && beam_img == 0) {
				right_beam = false;
				player_1_right_button = false;
				beam_count = 0;
				beam_img = 0;
				player_1_char_img = tk.getImage("D://images//ironman_up.png");
			}
		} else if(right_beam == true && player_2_char.equals("ironman")) {
			beam = tk.getImage("D://images//beam//beam" + beam_img + ".png");
			buffg.drawImage(beam, player_2_x + 30, player_2_y - 5, this);
			beam = tk.getImage("D://images//beam//beam.png");
			for (int s = 0; s < ironman_sub_list.size(); s++) {
				sub = ironman_sub_list.get(s);
				buffg.drawImage(beam, sub.pos.x + 12, sub.pos.y - 22, this);
			}
			if (beam_count < 100) {
				beam_img++;
			} else if (beam_count > 100) {
				beam_img--;
			}
			beam_count++;
			if (beam_count < 100 && beam_img == 9) {
				beam_img = 3;
			}
			if (beam_count > 100 && beam_img == 0) {
				right_beam = false;
				player_2_right_button = false;
				beam_count = 0;
				beam_img = 0;
				player_2_char_img = tk.getImage("D://images//ironman_up.png");
			}
		}
	}

	public void Draw_Ironman_sub() { // 아이언맨 보조무기 그리는 메소드
		if (player_1_char.equals("ironman") && player_1_state != 1) {
			for (int k = 0; k < ironman_sub_list.size(); k++) {
				sub = ironman_sub_list.get(k);
				buffg.drawImage(ironman_sub, sub.pos.x, sub.pos.y, this);
				sub.move(k);
				effect.pos.x = sub.pos.x-booster.getWidth(null);
				effect.pos.y = sub.pos.y + 3;
				effect.next_img();
			}
		} else if(player_2_char.equals("ironman") && player_1_state != 1){
			for (int k = 0; k < ironman_sub_list.size(); k++) {
				sub = ironman_sub_list.get(k);
				effect = effect_list.get(k);
				buffg.drawImage(ironman_sub, sub.pos.x, sub.pos.y, this);
				sub.move(k);
				effect.pos.x = sub.pos.x-booster.getWidth(null);
				effect.pos.y = sub.pos.y + 3;
				effect.next_img();
			}
		}
	}

	public void Draw_Game_UI() {
		char_ui = tk.getImage("D://images//ui//" + player_1_char + "_char_ui.png");
		buffg.drawImage(char_ui, 20, 40, this);
		char_ui = tk.getImage("D://images//ui//hp.png");
		buffg.drawImage(char_ui, 136, 96, player_1_hp, 9, this);
		char_ui = tk.getImage("D://images//ui//mana.png");
		buffg.drawImage(char_ui, 134, 110, player_1_mana, 9, this);
		if (dual == true) {
			int x = f_width / 2;
			char_ui = tk.getImage("D://images//ui//" + player_2_char + "_char_ui.png");
			buffg.drawImage(char_ui, 20 + x, 40, this);
			char_ui = tk.getImage("D://images//ui//hp.png");
			buffg.drawImage(char_ui, 136 + x, 96, player_2_hp, 9, this);
			char_ui = tk.getImage("D://images//ui//mana.png");
			buffg.drawImage(char_ui, 134 + x, 110, player_2_mana, 9, this);
		}
	}

	public void Draw_Diying() {
		if(player_1_die_x != 0 && player_1_state == 2) {
			player_1_char_img = tk.getImage("D://images//" + player_1_char + "_left.png");
			buffg.drawImage(player_1_char_img, player_1_die_x, player_1_die_y, this);	
		}
		if(player_2_die_x != 0 && player_2_state == 2) {
			player_2_char_img = tk.getImage("D://images//" + player_2_char + "_left.png");
			buffg.drawImage(player_2_char_img, player_2_die_x, player_2_die_y, this);
		}
	}
	public void Map_Move() { // 맵 이동 메소드
		map_x_1 -= 1;
		map_x_2 -= 1;
		if (map_x_1 == (f_width * -1)) {
			map_x_1 = f_width;
		}
		if (map_x_2 == (f_width * -1)) {
			map_x_2 = f_width;
		}
	}
	public void LaserProcess() {
		if (player_1_right_button == true && right_beam == false && player_1_char.equals("ironman")) {
			right_button_count++;
			if (right_button_count >= 50) {
				player_1_char_img = tk.getImage("D://images//laser1.gif");
			}
		} else if(player_2_right_button == true && right_beam == false && player_2_char.equals("ironman")){
			right_button_count++;
			if (right_button_count >= 50) {
				player_2_char_img = tk.getImage("D://images//laser1.gif");
			}
		} else {
			charge_count = false;
			right_button_count = 0;
		}
	}
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(player_1_state == 0) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				player_1_up = true;
				if (player_1_right_button == false && right_beam == false) {
					player_1_char_img = tk.getImage("D://images//" + player_1_char + "_up.png");
				}
				break;
			case KeyEvent.VK_A:
				player_1_left = true;
				break;
			case KeyEvent.VK_S:
				player_1_down = true;
				if (player_1_right_button == false && right_beam == false) {
					player_1_char_img = tk.getImage("D://images//" + player_1_char + "_down.png");
				}
				break;
			case KeyEvent.VK_D:
				player_1_right = true;
				break;
			case KeyEvent.VK_J:
				if (player_1_right_button == false && right_beam == false && player_1_char.equals("ironman")) {
					player_1_left_button = true;
				} else if (player_1_char.equals("warmachine")) {
					player_1_left_button = true;
					if(warmachine_left_count == false) {
						effect = new Effect(player_1_x + player_1_char_img.getWidth(null)-3, player_1_y+4+20, 8);
						effect.player_num = 1;
						effect_list.add(effect);
						warmachine_left_count = true;
					}
				}
				break;
			case KeyEvent.VK_K:
				if (player_1_left_button == false && right_beam == false && player_1_char.equals("ironman")) {
					if(charge_count == false) {
						effect = new Effect(player_1_x-85, player_1_y-100, 7);
						effect_list.add(effect);
						charge_count = true;
					}
					player_1_right_button = true;
					player_1_char_img = tk.getImage("D://images//laser.gif");
				} else if (player_1_char.equals("warmachine") && player_1_right_button == false) {
					player_1_right_button = true;
					if(warmachine_right_count == false) {
						effect = new Effect(player_1_x + player_1_char_img.getWidth(null)-3, player_1_y-5+20, 8);
						effect.player_num = 2;
						effect_list.add(effect);
						warmachine_right_count = true;
					}
				} else if (player_1_char.equals("warmachine") && player_1_right_button == true) {
					player_1_right_button = false;
					warmachine_right_count = false;
				}
				break;
			}
		}
		if (dual == true && player_2_state == 0) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				player_2_up = true;
				if (player_2_right_button == false && right_beam == false) {
					player_2_char_img = tk.getImage("D://images//" + player_2_char + "_up.png");
				}
				break;
			case KeyEvent.VK_LEFT:
				player_2_left = true;
				break;
			case KeyEvent.VK_DOWN:
				player_2_down = true;
				if (player_2_right_button == false && right_beam == false) {
					player_2_char_img = tk.getImage("D://images//" + player_2_char + "_down.png");
				}
				break;
			case KeyEvent.VK_RIGHT:
				player_2_right = true;
				break;
			case KeyEvent.VK_NUMPAD5:
				if (player_2_right_button == false && right_beam == false && player_2_char.equals("ironman")) {
					player_2_left_button = true;
				} else if (player_2_char.equals("warmachine")) {
					player_2_left_button = true;
					if(warmachine_left_count == false) {
						effect = new Effect(player_2_x + player_2_char_img.getWidth(null)-3, player_2_y+4+20, 8);
						effect.player_num = 1;
						effect_list.add(effect);
						warmachine_left_count = true;
					}
				}
				break;
			case KeyEvent.VK_NUMPAD6:
				if (player_2_left_button == false && right_beam == false && player_2_char.equals("ironman")) {
					if(charge_count == false) {
						effect = new Effect(player_2_x-85, player_2_y-100, 7);
						effect_list.add(effect);
						charge_count = true;
					}
					player_2_right_button = true;
					player_2_char_img = tk.getImage("D://images//laser.gif");
				} else if (player_2_char.equals("warmachine") && player_2_right_button == false) {
					player_2_right_button = true;
					if(warmachine_right_count == false) {
						effect = new Effect(player_2_x + player_2_char_img.getWidth(null)-3, player_2_y-5+20, 8);
						effect.player_num = 2;
						effect_list.add(effect);
						warmachine_right_count = true;
					}
				} else if (player_2_char.equals("warmachine") && player_2_right_button == true) {
					player_2_right_button = false;
				}
				break;
			}
		}
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
			switch(e.getKeyCode()) {
			case KeyEvent.VK_1:
				if(player_1_state == 1) {
					player_1_state = 2;
					player_1_x = -100;
					player_1_y = 200;
					player_1_hp = 107;
					player_1_mana = 109;
					player_1_char_img = tk.getImage("D://images//" + player_1_char + "_up.png");
				}
				break;
			case KeyEvent.VK_2:
				if(player_2_state == 1) {
					player_2_state = 2;
					player_2_x = -100;
					player_2_y = 400;
					player_2_hp = 107;
					player_2_mana = 109;
					player_2_char_img = tk.getImage("D://images//" + player_2_char + "_up.png");					
				}
				break;
			}
		if(player_1_state == 0) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				player_1_up = false;
				if (player_1_char.equals("ironman")) {
					if (player_1_right_button == false && right_beam == false) {
						player_1_char_img = tk.getImage("D://images//" + player_1_char + "_down.png");
					}
				} else {
					player_1_char_img = tk.getImage("D://images//" + player_1_char + "_down.png");
				}
				break;
			case KeyEvent.VK_A:
				player_1_left = false;
				break;
			case KeyEvent.VK_S:
				player_1_down = false;
				if (player_1_char.equals("ironman")) {
					if (player_1_right_button == false && right_beam == false) {
						player_1_char_img = tk.getImage("D://images//" + player_1_char + "_down.png");
					}
				} else {
					player_1_char_img = tk.getImage("D://images//" + player_1_char + "_down.png");
				}
				break;
			case KeyEvent.VK_D:
				player_1_right = false;
				break;
			case KeyEvent.VK_J:
				player_1_left_button = false;
				if (player_1_char.equals("warmachine")) {
					warmachine_left_count = false;
				}
				break;
			case KeyEvent.VK_K:
				if (player_1_char.equals("ironman")) {
					if (right_button_count < 50 && right_beam == false) {
						player_1_char_img = tk.getImage("D://images//ironman_up.png");
						player_1_right_button = false;
					} else if (right_button_count >= 50) {
						right_beam = true;
						right_button_count = 0;
						player_1_char_img = tk.getImage("D://images//ironman_laser.png");
					}
					break;
				}
			}
		}
		if (dual == true && player_2_state == 0) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				player_2_up = false;
				if (player_2_char.equals("ironman")) {
					if (player_2_right_button == false && right_beam == false) {
						player_2_char_img = tk.getImage("D://images//" + player_2_char + "_up.png");
					}
				} else {
					player_2_char_img = tk.getImage("D://images//" + player_2_char + "_up.png");
				}
				break;
			case KeyEvent.VK_LEFT:
				player_2_left = false;
				break;
			case KeyEvent.VK_DOWN:
				player_2_down = false;
				if (player_2_char.equals("ironman")) {
					if (player_2_right_button == false && right_beam == false) {
						player_2_char_img = tk.getImage("D://images//" + player_2_char + "_down.png");
					}
				} else {
					player_2_char_img = tk.getImage("D://images//" + player_2_char + "_down.png");
				}
				break;
			case KeyEvent.VK_RIGHT:
				player_2_right = false;
				break;
			case KeyEvent.VK_NUMPAD5:
				player_2_left_button = false;
				if (player_2_char.equals("warmachine")) {
					warmachine_left_count = false;
				}
				break;
			case KeyEvent.VK_NUMPAD6:
				if (player_2_char.equals("ironman")) {
					if (right_button_count < 50 && right_beam == false) {
						player_2_char_img = tk.getImage("D://images//ironman_up.png");
						player_2_right_button = false;
					} else if (right_button_count >= 50) {
						right_beam = true;
						right_button_count = 0;
						player_2_char_img = tk.getImage("D://images//ironman_laser.png");
					}
				}
				break;
			}
		}
		if(player_1_state == 1 || player_2_state == 1) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_1:
				if(player_1_state == 1) {
					player_1_state = 2;
					player_1_x = 100;
					player_1_y = 200;
					player_1_hp = 107;
					player_1_mana = 109;
				}
				break;
			case KeyEvent.VK_2:
				if(player_2_state == 1) {
					player_2_state = 2;
					player_2_x = 100;
					player_2_y = 400;
					player_2_hp = 107;
					player_2_mana = 109;
				}
				break;
			}
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

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	public void Player_state() {
		if(player_1_state == 2 && player_1_hp == 107 && player_1_x < 100) {
			player_1_x += 1;
			if(player_1_x == 100) {
				player_1_state = 0;
			}
		}
		if(player_2_state == 2 && player_2_hp == 107 && player_2_x < 100) {
			player_2_x += 1;
			if(player_2_x == 100) {
				player_2_state = 0;
			}
		}
		if(player_1_hp <= 0 && player_1_state == 0) {
			player_1_state = 2;
			player_1_die_x = player_1_x;
			player_1_die_y = player_1_y;
			player_1_x = -500;
			player_1_y = -500;
			player_1_pierce = 0;
			player_1_as = 0;
			player_1_hp = 0;
			player_1_state_count = false;
			player_1_left_button = false;
			player_1_right_button = false;
			player_1_left = false;
			player_1_right = false;
			player_1_up = false;
			player_1_down = false;
			effect = new Effect(player_1_die_x+10, player_1_die_y, 12, 1);
			effect_list.add(effect);
		}
		if(player_2_hp <= 0 && player_2_state == 0) {
			player_2_state = 2;
			player_2_die_x = player_2_x;
			player_2_die_y = player_2_y;
			player_2_x = -500;
			player_2_y = -500;
			player_2_pierce = 0;
			player_2_as = 0;
			player_2_hp = 0;
			player_2_state_count = false;
			player_2_left_button = false;
			player_2_right_button = false;
			player_2_left = false;
			player_2_right = false;
			player_2_up = false;
			player_2_down = false;
			effect = new Effect(player_2_die_x+10, player_2_die_y, 12, 2);
			effect_list.add(effect);
		}
	}
}
class Effect_Class {
	Point pos;
	double effect_num = 0;

	public void move() {
		pos.x -= 1;
	}

	public void next_img() {
		effect_num += 1;
	}
}
class Bullet extends Effect_Class {

	static int special = 0;
	int bullet_type;
	int gun_type;

	Bullet(int x, int y) {
		pos = new Point(x, y);
		bullet_type = 2;
	}

	Bullet(int x, int y, int t, int gt) {
		pos = new Point(x, y);
		bullet_type = t;
		gun_type = gt;
	}
	public void move() {
		pos.x += 30;
		if(bullet_type == 0) {
			pos.y -= 15;
		} else if(bullet_type == 1) {
			pos.y -= 5;
		} else if(bullet_type == 3) {
			pos.y += 5;
		} else if(bullet_type == 4) {
			pos.y += 15;
		}
	}
}

class Kill extends Effect_Class {
	double max_hp = 0;
	double hp = 0;
	double width = 124.0;
	boolean cool;
	boolean shoot = true;
	int cooltime = test02.timer;
	int kill_num;
	int player_num;
	test02 t;

	Kill(int x, int y) {
		pos = new Point(x, y);
		cooltime = test02.timer;
		if(t.dual == true) {
			if((pos.x+pos.y)-(t.player_1_x + t.player_1_y) 
					<= (pos.x+pos.y)-(t.player_2_x + t.player_2_y)) {
				player_num = 1;
			} else if((pos.x+pos.y)-(t.player_1_x + t.player_1_y) 
					>= (pos.x+pos.y)-(t.player_2_x + t.player_2_y)) {
				player_num = 2;
			}			
		} else {
			player_num = 1;
		}
	}

	public void move(int kill_num) {
		if(kill_num < 2) {
			pos.x -= 2;	
		} if(kill_num == 2) {
			int k = 30;
			if(player_num == 1) {
				if(pos.x > t.player_1_x-k) {
					pos.x -= 5;
				} else if(pos.x < t.player_1_x-k) {
					pos.x += 5;
				}
				if(pos.y > t.player_1_y-k) {
					pos.y -= 5;
				} else if(pos.y < t.player_1_y-k) {
					pos.y += 5;
				}
			} else if(player_num == 2) {
				if(pos.x > t.player_2_x-k) {
					pos.x -= 5;
				} else if(pos.x < t.player_2_x-k) {
					pos.x += 5;
				}
				if(pos.y > t.player_2_y-k) {
					pos.y -= 5;
				} else if(pos.y < t.player_2_y-k) {
					pos.y += 5;
				}
			}
		}
	}

	public void attack() {
		hp -= 1;
		width = new test02().hp_bar.getWidth(null) * (hp / max_hp);
	}
}
class Effect extends Effect_Class {
	Point pos;
	int effect_code = 0;
	double effect_num = 0;
	boolean shoot = true;
	boolean test = false;
	double shoot_x = 0, shoot_y = 0;
	double move_x = 0, move_y = 0;
	int damage = 1;
	int player_num;
	test02 t;
	Image effect_img;
	int coin_y = 0;
	String item_name;
	
	Effect(int x, int y, int effect_code) {
		pos = new Point(x, y);
		this.effect_code = effect_code;
		
		if(effect_code == 1) {
			effect_img = t.tk.getImage("D://images//kill_bullet.png");
			if(t.dual == true) {
				if((pos.x+pos.y)-(t.player_1_x + t.player_1_y) 
						<= (pos.x+pos.y)-(t.player_2_x + t.player_2_y)) {
					player_num = 1;
				} else if((pos.x+pos.y)-(t.player_1_x + t.player_1_y) 
						>= (pos.x+pos.y)-(t.player_2_x + t.player_2_y)) {
					player_num = 2;
				}			
			} else {
				player_num = 1;
			}
			if(player_num == 1) {
				move_x = ((test02.player_1_x-60 + test02.player_1_char_img.getWidth(null)+30 / 2) - pos.x) / 4;
				move_y = ((test02.player_1_y-90 + test02.player_1_char_img.getHeight(null)+17 / 2) - pos.y) / move_x;
				if (move_y > 4) {
					move_y = 4;
				}
				if (move_y < -4) {
					move_y = -4;
				}
			} else if(player_num == 2) {
				move_x = ((test02.player_2_x-60 + test02.player_2_char_img.getWidth(null)+30 / 2) - pos.x) / 4;
				move_y = ((test02.player_2_y-90 + test02.player_2_char_img.getHeight(null)+17 / 2) - pos.y) / move_x;
				if (move_y > 4) {
					move_y = 4;
				}
				if (move_y < -4) {
					move_y = -4;
				}
			}
			shoot_x = pos.x;
			shoot_y = pos.y;
		} else if(effect_code == 3) {
			effect_img = t.tk.getImage("D://images//bullet_explo//bullet_explo"+(int)effect_num+".png");
		} else if(effect_code == 4) {
			effect_img = t.tk.getImage("D://images//bullet_explo//bullet_explo2_"+(int)effect_num+".png");
		} else if(effect_code == 5) {
			effect_img = t.tk.getImage("D://images//coin.gif");
		} else if(effect_code == 6) {
			effect_img = t.tk.getImage("D://images//hit//hit" + (int) effect_num + ".png");
		} else if(effect_code == 10) {
			effect_img = t.tk.getImage("D://images//booster//booster0.png");
		} else if(effect_code == 11) {
			player_num = (int) (Math.random() * 8);
			effect_img = t.tk.getImage("D://images//item//"+ item_name +".gif");
		}
	}
	Effect(int x, int y, int effect_code, int player_num) {
		pos = new Point(x, y);
		this.effect_code = effect_code;
		this.player_num = player_num;
	}
	Effect(int x, int y, int effect_code, boolean test) {
		pos = new Point(x, y);
		this.effect_code = effect_code;
		this.test = test;
	}
	public void move() {
		if(test == false) {
			if(effect_code == 1) {
				shoot_x -= 4;
				shoot_y += -move_y;
				pos.setLocation(shoot_x, shoot_y);
				pos.x = (int) shoot_x;
				pos.y = (int) shoot_y;
			} else if(effect_code == 5) {
				if (coin_y < 30) {
					pos.y -= 2;
					pos.x -= 6;
				} else {
					pos.y += 1;
					pos.x -= 2;
				}
				coin_y++;
			} else if(effect_code == 7) {
				if(t.player_1_char.equals("ironman")) {
					pos.x = t.player_1_x-85;
					pos.y = t.player_1_y-100;			
				} else if(t.player_2_char.equals("ironman")) {
					pos.x = t.player_2_x-85;
					pos.y = t.player_2_y-100;		
				}
			} else if(effect_code == 8 && player_num == 1) {
				if (t.player_1_char.equals("warmachine")) {
					pos.x = t.player_1_x + t.player_1_char_img.getWidth(null)-3;
					pos.y = t.player_1_y + 4;
				} else {
					pos.x = t.player_2_x + t.player_2_char_img.getWidth(null)-3;
					pos.y = t.player_2_y + 4;
				}
			} else if(effect_code == 8 && player_num == 2) {
				if (t.player_1_char.equals("warmachine")) {
					pos.x = t.player_1_x + t.player_1_char_img.getWidth(null)-3;
					pos.y = t.player_1_y-5;
				} else {
					pos.x = t.player_2_x + t.player_2_char_img.getWidth(null)-3;
					pos.y = t.player_2_y-5;
				}
			} else if(effect_code == 10) {
				
			} else if(effect_code == 11) {
				pos.x -= 2;
				if (shoot == true) {
					pos.y--;
					move_y -= 0.2;
				} else if (shoot == false) {
					pos.y++;
					move_y += 0.2;
				}
				if (move_y <= 0.0) {
					shoot = false;
				} else if (move_y >= 10.0) {
					shoot = true;
				}
			} else if(effect_code == 12 || effect_code == 13) {
				
			} else {
				pos.x --;
			}
		}
	}
	public void next_img() {
		if(test == false) {
			if(effect_code == 2 || effect_code == 9 || effect_code == 12) {
				effect_num += 0.5;
			} else if(effect_code == 6) {
				effect_num += 0.1;
			} else if(effect_code == 7) {
				if(t.right_button_count < 50) {
					effect_num += 0.2;				
				} else {
					effect_num += 0.5;
				}
			} else if(effect_code == 13) {
				effect_num += 0.2;
			} else {
				effect_num++;
			}			
		} else if(test == true) {
			effect_num += 1;
		}
	}
}
class Ironman_sub extends Effect_Class {

	Ironman_sub(int x, int y) {
		pos = new Point(x, y);
	}

	public void move(int y) {
		if(test02.player_1_char.equals("ironman")) {
			if (pos.x > test02.player_1_x - test02.ironman_sub.getWidth(null)) {
				pos.x -= 2;
			} else if (pos.x < test02.player_1_x - test02.ironman_sub.getWidth(null)) {
				pos.x += 2;
			}
			if (y == 0) {
				if (pos.y > test02.player_1_y) {
					pos.y -= 2;
				} else if (pos.y < test02.player_1_y) {
					pos.y += 2;
				}
			} else if (y == 1) {
				if (pos.y > test02.player_1_y + test02.player_1_char_img.getHeight(null) / 4) {
					pos.y -= 2;
				} else if (pos.y < test02.player_1_y + test02.player_1_char_img.getHeight(null) / 4) {
					pos.y += 2;
				}
			} else if (y == 2) {
				if (pos.y > test02.player_1_y + test02.player_1_char_img.getHeight(null) / 2) {
					pos.y -= 2;
				} else if (pos.y < test02.player_1_y + test02.player_1_char_img.getHeight(null) / 2) {
					pos.y += 2;
				}
			} else if (y == 3) {
				if (pos.y > test02.player_1_y + test02.player_1_char_img.getHeight(null) / 4
						+ test02.player_1_char_img.getHeight(null) / 2) {
					pos.y -= 2;
				} else if (pos.y < test02.player_1_y + test02.player_1_char_img.getHeight(null) / 4
						+ test02.player_1_char_img.getHeight(null) / 2) {
					pos.y += 2;
				}
			}
		} else if(test02.player_2_char.equals("ironman")) {
			if (pos.x > test02.player_2_x - test02.ironman_sub.getWidth(null)) {
				pos.x -= 2;
			} else if (pos.x < test02.player_2_x - test02.ironman_sub.getWidth(null)) {
				pos.x += 2;
			}
			if (y == 0) {
				if (pos.y > test02.player_2_y) {
					pos.y -= 2;
				} else if (pos.y < test02.player_2_y) {
					pos.y += 2;
				}
			} else if (y == 1) {
				if (pos.y > test02.player_2_y + test02.player_2_char_img.getHeight(null) / 4) {
					pos.y -= 2;
				} else if (pos.y < test02.player_2_y + test02.player_2_char_img.getHeight(null) / 4) {
					pos.y += 2;
				}
			} else if (y == 2) {
				if (pos.y > test02.player_2_y + test02.player_2_char_img.getHeight(null) / 2) {
					pos.y -= 2;
				} else if (pos.y < test02.player_2_y + test02.player_2_char_img.getHeight(null) / 2) {
					pos.y += 2;
				}
			} else if (y == 3) {
				if (pos.y > test02.player_2_y + test02.player_2_char_img.getHeight(null) / 4
						+ test02.player_2_char_img.getHeight(null) / 2) {
					pos.y -= 2;
				} else if (pos.y < test02.player_2_y + test02.player_2_char_img.getHeight(null) / 4
						+ test02.player_2_char_img.getHeight(null) / 2) {
					pos.y += 2;
				}
			}
		}
	}
}

class Sub_Thread extends Thread {

	test02 t = new test02();
	/*
	public void sub_start() {
		start();
	}
	*/

	public void run() {
		while (true) {
			long start = System.currentTimeMillis();
			BulletProcess();
			KillProcess();
			Beam_Check();
			Coin_Check();
			//Char_Check();
			Hit_Bullet_Check();
			Bullet_Check();
			Bullet2_Check();
			Item_Check();
			long end = System.currentTimeMillis();
			if((end-start) > 1) {
				System.out.println("쓰레드 2 실행 시간 : " + (end-start) + "ms");					
			}
			try {
				if((end-start) < 16) {
					Thread.sleep(16-(end-start));					
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void BulletProcess() {
		t.bullet_count_1++;
		t.bullet_count_2++;
		t.bullet_count_3++;
		if (t.player_1_left_button == true && t.bullet_count_1 >= t.player_1_as) {
			t.bullet_count_1 = 0;
			if (t.player_1_char.equals("ironman")) {
				t.b = new Bullet(t.player_1_x + t.player_1_char_img.getWidth(null), t.player_1_y);
				t.p_1_bullet_list.add(t.b);
				if (t.ironman_sub_list.size() != 0) {
					for (int k = 0; k < t.ironman_sub_list.size(); k++) {
						t.sub = (Ironman_sub) t.ironman_sub_list.get(k);
						if (t.player_1_pierce > k) {
							t.b = new Bullet(t.sub.pos.x + t.ironman_sub.getWidth(null) + 20, t.sub.pos.y - 23);
							t.p_1_bullet_list.add(t.b);
						}
					}
				}
			} else if (t.player_1_char.equals("warmachine")) {
				t.b = new Bullet(t.player_1_x + t.player_1_char_img.getWidth(null)+30, t.player_1_y - 10, 2, 0);
				t.p_1_bullet_list.add(t.b);
				t.b.special = 4;
				if (t.b.special > 0) {
					t.b = new Bullet(t.player_1_x + t.player_1_char_img.getWidth(null)+30, t.player_1_y-5 - 10, 1, 0);
					t.p_1_bullet_list.add(t.b);
				}
				if (t.b.special > 1) {
					t.b = new Bullet(t.player_1_x + t.player_1_char_img.getWidth(null)+30, t.player_1_y+5 - 10, 3, 0);
					t.p_1_bullet_list.add(t.b);
				}
				if (t.b.special > 2) {
					t.b = new Bullet(t.player_1_x + t.player_1_char_img.getWidth(null)+30, t.player_1_y-15 - 10, 0, 0);
					t.p_1_bullet_list.add(t.b);
				}
				if (t.b.special > 3) {
					t.b = new Bullet(t.player_1_x + t.player_1_char_img.getWidth(null)+30, t.player_1_y+15 - 10, 4, 0);
					t.p_1_bullet_list.add(t.b);
				}
			}
		}
		if (t.player_1_right_button == true && t.bullet_count_3 >= t.player_1_as && t.player_1_char.equals("warmachine")) {
			t.b = new Bullet(t.player_1_x + t.player_1_char_img.getWidth(null)+30, t.player_1_y - 20, 2, 1);
			t.p_1_bullet_list.add(t.b);
			t.bullet_count_3 = 0;
			if (t.b.special > 0) {
				t.b = new Bullet(t.player_1_x + t.player_1_char_img.getWidth(null)+30, t.player_1_y-5 - 20, 1, 1);
				t.p_1_bullet_list.add(t.b);
			}
			if (t.b.special > 1) {
				t.b = new Bullet(t.player_1_x + t.player_1_char_img.getWidth(null)+30, t.player_1_y+5 - 20, 3, 1);
				t.p_1_bullet_list.add(t.b);
			}
			if (t.b.special > 2) {
				t.b = new Bullet(t.player_1_x + t.player_1_char_img.getWidth(null)+30, t.player_1_y-15 - 20, 0, 1);
				t.p_1_bullet_list.add(t.b);
			}
			if (t.b.special > 3) {
				t.b = new Bullet(t.player_1_x + t.player_1_char_img.getWidth(null)+30, t.player_1_y+15 - 20, 4, 1);
				t.p_1_bullet_list.add(t.b);
			}
		}
		if (t.dual == true) {
			if (t.player_2_left_button == true && t.bullet_count_2 >= t.player_2_as) {
				t.bullet_count_2 = 0;
				if (t.player_2_char.equals("ironman")) {
					t.b = new Bullet(t.player_2_x + t.player_2_char_img.getWidth(null), t.player_2_y);
					t.p_2_bullet_list.add(t.b);
					for (int k = 0; k < t.ironman_sub_list.size(); k++) {
						t.sub = (Ironman_sub) t.ironman_sub_list.get(k);
						if (t.player_2_pierce > k) {
							t.b = new Bullet(t.sub.pos.x + t.ironman_sub.getWidth(null) + 20, t.sub.pos.y - 23);
							t.p_2_bullet_list.add(t.b);
						}
					}
				} else if (t.player_2_char.equals("warmachine")) {
					t.b = new Bullet(t.player_2_x + t.player_2_char_img.getWidth(null)+30, t.player_2_y - 10, 2, 0);
					t.p_2_bullet_list.add(t.b);
					t.b.special = 4;
					if (t.b.special > 0) {
						t.b = new Bullet(t.player_2_x + t.player_2_char_img.getWidth(null)+30, t.player_2_y-5 - 10, 1, 0);
						t.p_2_bullet_list.add(t.b);
					}
					if (t.b.special > 1) {
						t.b = new Bullet(t.player_2_x + t.player_2_char_img.getWidth(null)+30, t.player_2_y+5 - 10, 3, 0);
						t.p_2_bullet_list.add(t.b);
					}
					if (t.b.special > 2) {
						t.b = new Bullet(t.player_2_x + t.player_2_char_img.getWidth(null)+30, t.player_2_y-15 - 10, 0, 0);
						t.p_2_bullet_list.add(t.b);
					}
					if (t.b.special > 3) {
						t.b = new Bullet(t.player_2_x + t.player_2_char_img.getWidth(null)+30, t.player_2_y+15 - 10, 4, 0);
						t.p_2_bullet_list.add(t.b);
					}
				}
			}
			if (t.player_2_right_button == true && t.bullet_count_3 >= t.player_2_as && t.player_2_char.equals("warmachine")) {
				t.b = new Bullet(t.player_2_x + t.player_2_char_img.getWidth(null)+30, t.player_2_y - 20, 2, 1);
				t.p_2_bullet_list.add(t.b);
				t.bullet_count_3 = 0;
				if (t.b.special > 0) {
					t.b = new Bullet(t.player_2_x + t.player_2_char_img.getWidth(null)+30, t.player_2_y-5 - 20, 1, 1);
					t.p_2_bullet_list.add(t.b);
				}
				if (t.b.special > 1) {
					t.b = new Bullet(t.player_2_x + t.player_2_char_img.getWidth(null)+30, t.player_2_y+5 - 20, 3, 1);
					t.p_2_bullet_list.add(t.b);
				}
				if (t.b.special > 2) {
					t.b = new Bullet(t.player_2_x + t.player_2_char_img.getWidth(null)+30, t.player_2_y-15 - 20, 0, 1);
					t.p_2_bullet_list.add(t.b);
				}
				if (t.b.special > 3) {
					t.b = new Bullet(t.player_2_x + t.player_2_char_img.getWidth(null)+30, t.player_2_y+15 - 20, 4, 1);
					t.p_2_bullet_list.add(t.b);
				}
			}
		}
	}

	public void KillProcess() {
		// t.k = new Kill(t.f_width,
		// (int)(Math.random()*((t.f_height-t.kill.getHeight(null)-100)))+30);
		try {
			if(t.timer_count % 32 == 0) {
				int height = (int)(Math.random()*600+1);
				int kill_num = (int)(Math.random()*2);
				KillProduce(t.f_width, height, 1, kill_num);			
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	// x좌표, y좌표, 몬스터마릿수, 몬스터코드
	public void KillProduce(int x, int y, int num, int kill_num) {
		for (int p = 0; p < num; p++) {
			t.k = new Kill(x + (p * 150), y);
			t.kill_list.add(t.k);
			t.k.kill_num = kill_num;
			if (t.k.kill_num == 0) {
				t.k.max_hp = 5;
				t.k.hp = 5;
			} else if (t.k.kill_num == 1) {
				t.k.max_hp = 10;
				t.k.hp = 10;
			} else if (t.k.kill_num == 2) {
				t.k.max_hp = 15;
				t.k.hp = 15;
			}
		}
	}

	public void Hit_Bullet_Check() { // 캐릭터와 적 미사일 충돌체크 메소드
		if (t.effect_list.size() != 0) {
			for (int i = 0; i < t.effect_list.size(); i++) {
				if (i != t.effect_list.size()) {
					t.effect = (Effect) t.effect_list.get(i);
				}
				if(t.effect != null) {
					if(t.effect.effect_code == 1 && t.effect.test == false) {
						if (Crash(t.player_1_x + 30, t.player_1_y + 17, t.effect.pos.x, t.effect.pos.y, t.player_1_char_img,
								t.effect.effect_img, -60, -90) && t.player_1_state == 0) {
							if (t.effect_list.size() > 0) {
								t.effect_list.remove(i);
							}
							t.effect = new Effect(t.effect.pos.x - 16, t.effect.pos.y - 18, 6);
							t.effect_list.add(t.effect);
							t.player_1_hp -= t.effect.damage;
						}
						if (t.dual == true) {
							if (Crash(t.player_2_x + 30, t.player_2_y + 17, t.effect.pos.x, t.effect.pos.y, t.player_2_char_img,
									t.effect.effect_img, -60, -90) && t.player_2_state == 0) {
								if (t.effect_list.size() > 0) {
									t.effect_list.remove(i);
								}
								t.effect = new Effect(t.effect.pos.x - 16, t.effect.pos.y - 18, 6);
								t.effect_list.add(t.effect);
								t.player_2_hp -= t.effect.damage;
							}
						}
					}					
				}
			}
		}
	}

	public void Bullet_Check() { // 플레이어 1 미사일과 적 이미지 충돌체크 메소드
		if (t.p_1_bullet_list.size() != 0) {
			for (int i = 0; i < t.p_1_bullet_list.size(); i++) {
				for (int v = 0; v < t.kill_list.size(); v++) {
					if (i < t.p_1_bullet_list.size()) {
						t.b = (Bullet) t.p_1_bullet_list.get(i);
					}
					if (v < t.kill_list.size()) {
						t.k = (Kill) t.kill_list.get(v);
					}
					if(t.k != null && t.b != null) {
						if (Crash(t.b.pos.x, t.b.pos.y, t.k.pos.x, t.k.pos.y, t.bullet, t.kill)) {
							t.k.attack();
							if (t.p_1_bullet_list.size() > 0 && t.p_1_bullet_list.size() != i) {
								t.p_1_bullet_list.remove(i);
							}
							if(t.player_1_char.equals("ironman")) {
								t.effect = new Effect(t.b.pos.x, t.b.pos.y, 3);								
							} else if(t.player_1_char.equals("warmachine")) {
								t.effect = new Effect(t.b.pos.x, t.b.pos.y, 4);
							}
							t.effect_list.add(t.effect);
							if (t.k.hp == 0) {
								t.effect = new Effect(t.k.pos.x, t.k.pos.y - 50, 2);
								t.effect_list.add(t.effect);
								t.effect = new Effect(t.k.pos.x, t.k.pos.y, 5);
								t.player_1_score += 10;
								t.effect_list.add(t.effect);
								t.kill_list.remove(v);
								if (t.k.kill_num == 1) {
									item_drop();
								}
							}
						}
					}
				}
			}
		}
	}

	public void Bullet2_Check() { // 플레이어 2 미사일과 적 이미지 충돌체크 메소드
		for (int i = 0; i < t.p_2_bullet_list.size(); i++) {
			for (int v = 0; v < t.kill_list.size(); v++) {
				if (t.p_2_bullet_list.size() != 0) {
					if (i < t.p_2_bullet_list.size()) {
						t.b = (Bullet) t.p_2_bullet_list.get(i);
					}
					if (v < t.kill_list.size()) {
						t.k = (Kill) t.kill_list.get(v);
					}
					if(t.k != null && t.b != null) {
						if (Crash(t.b.pos.x, t.b.pos.y, t.k.pos.x, t.k.pos.y, t.bullet, t.kill) && t.b != null & t.k != null) {
							t.k.attack();
							if (t.p_2_bullet_list.size() > 0 && t.p_2_bullet_list.size() != i) {
								t.p_2_bullet_list.remove(i);
							}
							if(t.player_2_char.equals("ironman")) {
								t.effect = new Effect(t.b.pos.x, t.b.pos.y, 3);								
							} else if(t.player_2_char.equals("warmachine")) {
								t.effect = new Effect(t.b.pos.x, t.b.pos.y, 4);
							}
							t.effect_list.add(t.effect);
							if (t.k.hp == 0) {
								t.effect = new Effect(t.k.pos.x, t.k.pos.y - 50, 2);
								t.effect_list.add(t.effect);
								t.effect = new Effect(t.k.pos.x, t.k.pos.y, 5);
								t.player_2_score += 10;
								t.effect_list.add(t.effect);
								t.kill_list.remove(v);
								if (t.k.kill_num == 1) {
									item_drop();
								}
							}
						}
					}
				}
			}
		}
	}
	public void item_drop() {
		t.effect = new Effect(t.k.pos.x + 27, t.k.pos.y + 20, 11);
		t.effect_list.add(t.effect);
	}

	public void Beam_Check() {
		if (t.right_beam == true) {
			try {
				for (int v = 0; v < t.kill_list.size(); v++) {
					if (v != t.kill_list.size()) {
						t.k = (Kill) t.kill_list.get(v);
					}
					if(t.player_1_char.equals("ironman")) {
						exit_For: for (int y1 = (this.t.player_1_y - 5 + 17); y1 < (this.t.player_1_y - 5 + 28); y1++) {
							for (int y2 = t.k.pos.y; y2 < t.k.pos.y + t.kill.getHeight(null); y2++) {
								if (y1 == y2) {
									t.beam_check = true;
									break exit_For;
								}
							}
						}
						exit_For: for (int s = 0; s < t.ironman_sub_list.size(); s++) {
							t.sub = t.ironman_sub_list.get(s);
							for (int y1 = t.sub.pos.y; y1 < (t.sub.pos.y + t.ironman_sub.getHeight(null)); y1++) {
								for (int y2 = t.k.pos.y; y2 < t.k.pos.y + t.kill.getHeight(null); y2++) {
									if (y1 == y2) {
										t.beam_check = true;
										break exit_For;
									}
								}
							}
						}
					} else if(t.player_2_char.equals("ironman") && t.dual == true) {
						exit_For: for (int y1 = (this.t.player_2_y - 5 + 17); y1 < (this.t.player_2_y - 5 + 28); y1++) {
							for (int y2 = t.k.pos.y; y2 < t.k.pos.y + t.kill.getHeight(null); y2++) {
								if (y1 == y2) {
									t.beam_check = true;
									break exit_For;
								}
							}
						}
						exit_For: for (int s = 0; s < t.ironman_sub_list.size(); s++) {
							t.sub = t.ironman_sub_list.get(s);
							for (int y1 = t.sub.pos.y; y1 < (t.sub.pos.y + t.ironman_sub.getHeight(null)); y1++) {
								for (int y2 = t.k.pos.y; y2 < t.k.pos.y + t.kill.getHeight(null); y2++) {
									if (y1 == y2) {
										t.beam_check = true;
										break exit_For;
									}
								}
							}
						}
					}
					
					if (t.beam_check == true) {
						t.effect = new Effect(t.k.pos.x, t.k.pos.y, 5);
						t.effect = new Effect(t.k.pos.x, t.k.pos.y, 2);
						t.player_1_score += 10;
						t.effect_list.add(t.effect);
						t.kill_list.remove(v);
						t.effect_list.add(t.effect);
						if (t.k.kill_num == 1) {
							item_drop();
						}
					}
					t.beam_check = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void Char_Check() { // 캐릭터와 적 충돌체크
		try {
			for (int v = 0; v < t.kill_list.size(); v++) {
				if (v != t.kill_list.size() && t.kill_list.size() != 0) {
					t.k = (Kill) t.kill_list.get(v);
				}
				if (Crash(t.player_1_x + 30, t.player_1_y + 17, t.k.pos.x, t.k.pos.y, t.player_1_char_img, t.kill, -60,
						-90) && t.player_1_state == 0) {
					if(t.k.kill_num < 2) {
						t.effect = new Effect(t.k.pos.x, t.k.pos.y, 2);
						t.effect_list.add(t.effect);
						if (t.kill_list.size() != 0) {
							t.kill_list.remove(v);
							t.player_1_hp -= 10;
						}
					} else if(t.k.kill_num == 2) {
						t.k.cool = true;
					}
				}
				if (Crash(t.player_2_x + 30, t.player_2_y + 17, t.k.pos.x, t.k.pos.y, t.player_2_char_img, t.kill, -60,
						-90) && t.player_2_state == 0) {
					if(t.k.kill_num < 2) {
						t.effect = new Effect(t.k.pos.x, t.k.pos.y, 2);
						t.effect_list.add(t.effect);
						if (t.kill_list.size() != 0) {
							t.kill_list.remove(v);
							t.player_2_hp -= 10;
						}
					} else if(t.k.kill_num == 2) {
						t.k.cool = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Coin_Check() { // 캐릭터와 코인 충돌체크
		try {
			for (int v = 0; v < t.effect_list.size(); v++) {
				if (v != t.effect_list.size()) {
					t.effect = (Effect) t.effect_list.get(v);
				}
				if(t.effect != null && t.effect.effect_code == 5) {
					if (Crash(t.player_1_x, t.player_1_y, t.effect.pos.x, t.effect.pos.y, t.player_1_char_img, t.coin)) {
						if(t.effect_list.size() != v) {
							t.effect_list.remove(v);
						}
						t.player_1_score += 5;
					}
					if (Crash(t.player_2_x, t.player_2_y, t.effect.pos.x, t.effect.pos.y, t.player_2_char_img, t.coin)) {
						if(t.effect_list.size() != v) {
							t.effect_list.remove(v);
						}
						t.player_2_score += 5;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Item_Check() { // 캐릭터와 아이템 충돌체크
		try {
			for (int v = 0; v < t.effect_list.size(); v++) {
				if (v != t.effect_list.size()) {
					t.effect = (Effect) t.effect_list.get(v);
				}
				if(t.effect != null) {
					if(t.effect.effect_code == 11 && t.effect.test == false) {
						String item_name = "";
						
						if (t.effect.player_num == 0 || t.effect.player_num == 1 || t.effect.player_num == 6) {
							item_name = "heart";
						} else if (t.effect.player_num == 2 || t.effect.player_num == 3 || t.effect.player_num == 7) {
							item_name = "energy";
						} else if (t.effect.player_num == 4) {
							item_name = "attackspeed";
						} else if (t.effect.player_num == 5) {
							item_name = "pierce";
						}
						t.effect.item_name = item_name;
						if (Crash(t.player_1_x, t.player_1_y, t.effect.pos.x, t.effect.pos.y, t.player_1_char_img, t.effect.effect_img)) {
							t.effect_list.remove(v);
							t.effect = new Effect(t.effect.pos.x - t.item_get.getWidth(null) / 2 + 5,
									t.effect.pos.y - t.item_get.getHeight(null) / 2 + 5, 9);
							t.effect_list.add(t.effect);
							if (t.player_1_char.equals("ironman") && t.player_1_pierce < 4 && t.effect.item_name.equals("pierce")) {
								if (t.player_1_pierce == 0) {
									t.sub = new Ironman_sub(t.player_1_x - t.ironman_sub.getWidth(null), t.player_1_y);
								} else if (t.player_1_pierce == 1) {
									t.sub = new Ironman_sub(t.player_1_x - t.ironman_sub.getWidth(null),
											t.player_1_y + t.player_1_char_img.getHeight(null) / 4);
								} else if (t.player_1_pierce == 2) {
									t.sub = new Ironman_sub(t.player_1_x - t.ironman_sub.getWidth(null),
											t.player_1_y + t.player_1_char_img.getHeight(null) / 2);
								} else if (t.player_1_pierce == 3) {
									t.sub = new Ironman_sub(t.player_1_x - t.ironman_sub.getWidth(null),
											t.player_1_y + t.player_1_char_img.getHeight(null) / 4
													+ t.player_1_char_img.getHeight(null) / 2);
								}
								t.ironman_sub_list.add(t.sub);
								t.player_1_pierce++;
							}
							if (t.player_1_char.equals("warmachine") && t.player_1_pierce < 4
									&& item_name.equals("pierce")) {
								t.player_1_pierce++;
								t.b.special++;
							}
							if (item_name.equals("attackspeed")) {
								t.player_1_as--;
							}
							if(item_name.equals("heart")) {
								if(t.player_1_hp <= 97) { 
									t.player_1_hp += 10;
								} else if(t.player_1_hp >= 98) {
									t.player_1_hp = 107;
								}
							}
							if(item_name.equals("energy")) {
								if(t.player_1_mana <= 99) { 
									t.player_1_mana += 10;
								} else if(t.player_1_mana >= 100) {
									t.player_1_mana = 109;
								}
							}
						} else if (Crash(t.player_2_x, t.player_2_y, t.effect.pos.x, t.effect.pos.y, t.player_2_char_img, t.effect.effect_img)) {
							t.effect_list.remove(v);
							t.effect = new Effect(t.effect.pos.x - t.item_get.getWidth(null) / 2 + 5,
									t.effect.pos.y - t.item_get.getHeight(null) / 2 + 5, 9);
							t.effect_list.add(t.effect);
							if (t.player_2_char.equals("ironman") && t.player_2_pierce < 4 && item_name.equals("pierce")) {
								if (t.player_2_pierce == 0) {
									t.sub = new Ironman_sub(t.player_2_x - t.ironman_sub.getWidth(null), t.player_2_y);
									t.effect = new Effect(t.player_2_x - t.ironman_sub.getWidth(null), t.player_2_y, 10);
								} else if (t.player_2_pierce == 1) {
									t.sub = new Ironman_sub(t.player_2_x - t.ironman_sub.getWidth(null),
											t.player_2_y + t.player_1_char_img.getHeight(null) / 4);
								} else if (t.player_2_pierce == 2) {
									t.sub = new Ironman_sub(t.player_2_x - t.ironman_sub.getWidth(null),
											t.player_2_y + t.player_1_char_img.getHeight(null) / 2);
								} else if (t.player_2_pierce == 3) {
									t.sub = new Ironman_sub(t.player_2_x - t.ironman_sub.getWidth(null),
											t.player_2_y + t.player_1_char_img.getHeight(null) / 4
													+ t.player_1_char_img.getHeight(null) / 2);
								}
								t.ironman_sub_list.add(t.sub);
								t.player_2_pierce++;
							}
							if (t.player_2_char.equals("warmachine") && t.player_2_pierce < 4
									&& t.effect.item_name.equals("pierce")) {
								t.player_2_pierce++;
								t.b.special++;
							}
							if (item_name.equals("attackspeed")) {
								t.player_2_as--;
							}
							if(item_name.equals("heart")) {
								if(t.player_2_hp <= 97) { 
									t.player_2_hp += 10;
								} else if(t.player_2_hp >= 98) {
									t.player_2_hp = 107;
								}
							}
							if(item_name.equals("energy")) {
								if(t.player_2_mana <= 99) { 
									t.player_2_mana += 10;
								} else if(t.player_2_mana >= 100) {
									t.player_2_mana = 109;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 충돌검사 메소드1 - 일반
	public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2) {

		boolean check = false;

		try {
			if ((x1 < x2 + img2.getWidth(null)) && (x1 + img1.getWidth(null) > x2) && (y1 < y2 + img2.getHeight(null))
					&& (y1 + img1.getHeight(null) > y2)) {
				check = true;
			}
			return check;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}

	// 충돌검사 메소드2 - 캐릭터 히트박스 충돌용
	// 캐릭터 히트박스(+30,+17,-60,-90)
	public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2, int minus_x, int minus_y) {

		boolean check = false;

		try {
			if ((x1 < x2 + img2.getWidth(null)) && (x1 + img1.getWidth(null) + minus_x > x2)
					&& (y1 < y2 + img2.getHeight(null)) && (y1 + img1.getHeight(null) + minus_y > y2)) {
				check = true;
				return check;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}
}