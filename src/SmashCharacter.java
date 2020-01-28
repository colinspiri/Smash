import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.francisparker.mmaunu.gameengine.CollisionDetector;
import org.francisparker.mmaunu.gameengine.ControllableObject;
import org.francisparker.mmaunu.gameengine.Drawable;
import org.francisparker.mmaunu.gameengine.DrawableGameObject;
import org.francisparker.mmaunu.gameengine.GameFrame;

public abstract class SmashCharacter extends ControllableObject implements KeyListener {
	public String name;
	public int facingdirection;
	public int xdirection;
	public int ydirection;
	public int left = -1, right = 1, up = -1, down = 1, neutral = 0;
	public int tdirectionx = 0;
	public int tdirectiony = 0;
	public boolean crouch;

	public int jumpNum = 2;
	public int jumptime = 0;
	public double gravity = 9.8;
	public double ogweight;
	public double weight;
	public double normalforce = 0;
	
	public Vector pos = new Vector(getX(),getY());
	public Vector velocity = new Vector(0,0);
	public Vector acceleration = new Vector(0,0);
	public Vector appliedforce = new Vector(0,0);
	public Vector resistance = new Vector(0,0);
	int resisttime = 0;

	public int landtime = 0;
	public boolean onPlatform = false;
	public boolean touchingSide = false;

	public boolean spotdodge = false;
	public int spotdodgetime = 10;
	public int spotdodgecooldown = 0;
	public boolean airdodge = false;
	public int airdodgetime = 0;
	public boolean stun = false;
	public double lagframes = 0;

	public int width;
	public int height;
	public Image regimg;

	public int currpercent = 0;
	public boolean neutralattack = false;
	public boolean sideattack = false;
	public boolean upattack = false;
	public boolean attackjumpable = false;
	public int attacklag = 0;

	public SmashCharacter(int x, int y, Image img, int tempwidth, int tempheight, int speed, double tempweight, String tempname){
		super(x,y,img,tempwidth,tempheight,speed);
		weight = tempweight/-32;
		weight*=gravity;
		ogweight=weight;
		width=tempwidth;
		height=tempheight;
		xdirection=right;
		ydirection=neutral;
		name=tempname;
		regimg=getImage();
		facingdirection=1;
	}

	public void act() {
		//variables
		pos.setComponents(getX(), getY());

		//onPlatform/off screen
		offScreen();
		collisionPlatforms();
		onPlatform();

		//dodge
		dodge();

		//lag
		lag();

		//direction
		direction();

		//crouch
		crouch();
		
		//input
		input();

		//subclass methods
		controllable();
		ai();
		collisionAttacks();
		attack();

		resistance();
		move();
		
		//changeImage();
	}

	public abstract void controllable();
	public abstract void ai();
	
	public abstract void neutralAttack();
	public abstract void sideAttack(int direction);
	public abstract void upAttack();
	public void endAttack(double templag, double tempattacklag) {
		neutralattack=false;
		sideattack=false;
		upattack=false;
		attacklag+=tempattacklag;
		addLag(templag);
	}
	public abstract void collisionAttacks();
	public void attack() {
		attacklag-=1;
		if(attacklag<=0) {
			attacklag=0;
		}
	}
	public boolean getAttackJumpAble() {
		return attackjumpable;
	}
	public void setAttackJumpAble(boolean temp){
		attackjumpable=temp;
	}

	public void offScreen() {
	}
	public void collisionPlatforms() {
		double leftside = getX();
		double rightside = getX() + width;
		double topside = getY();
		double bottomside = getY() + height;

		// collision detection with platform
		if(bottomside>=389 && bottomside<=420 && rightside>=105 && leftside<=650 && velocity.getY()>=0) onPlatform=true;
		else onPlatform=false;

		if(bottomside>420 && rightside>=120 && leftside<=635) touchingSide=true;
		else touchingSide=false;

		if(topside>=550) {
			JOptionPane.showMessageDialog(null, "GAME OVER. " + name + " lost.");
			System.exit(0);
		}
	}
	public void onPlatform() {
		if(onPlatform){
			//landing
			if(landtime==0){
				if(airdodge || stun) lagframes+=22;
				else if(jumpNum<=0) lagframes+=3;
				else lagframes+=1;

				velocity.setY(0);
			}

			attackjumpable=true;
			weight=ogweight;
			normalforce=-1*weight;
			jumpNum=2;
			airdodge=false;
			airdodgetime=10;
			landtime+=1;
		}
		else {
			normalforce=0;
			landtime=0;
		}
	}
	public void dodge() {
		//air dodge
		if(airdodge){
			velocity.setY(0);
			System.out.println("airdodge " + airdodgetime);
			airdodgetime--;
			if(airdodgetime<=0){
				airdodge=false;
				addLag(20);
			}
		}

		//spot dodge
		if(spotdodge){
			System.out.println("spot dodge " + (int)spotdodgetime);
			spotdodgetime--;
			spotdodgecooldown=0;
			if(spotdodgetime<=0) {
				spotdodge=false;
				addLag(10);
			}
		}
		else {
			spotdodgecooldown++;
			if(spotdodgecooldown>=30){
				spotdodgetime=10;
			}
		}
	}
	public void lag() {
		//lag frames
		if(lagframes>0){
//			if(lagframes>60) System.out.println("stun " + ((int)(lagframes/60))*1.0);
//			else System.out.println("stun " + (int)lagframes);


			spotdodge=false;
			stun=true;
			lagframes--;
		}
		else {
			stun=false;
			lagframes=0;
		}
	}
	public void direction() {
		tdirectionx++;
		if(tdirectionx>=10) {
			tdirectionx=0;
			xdirection=neutral;
		}
		if(xdirection==neutral) tdirectionx=0;
		tdirectiony++;
		if(tdirectiony>=10) {
			tdirectiony=0;
			ydirection=neutral;
		}
		if(ydirection==neutral) tdirectiony=0;
	}
	public void crouch() {
		if(crouch) {
			System.out.println("crouch");
		}
		else {
			setImage(regimg);
			setWidth(65);
			setHeight(79);
		}
		crouch=false;
	}
	
	public void moveLeft() {
		if(touchingSide==false && spotdodge==false && crouch==false) {
			facingdirection=left;
			xdirection=left;
			ydirection=neutral;
			tdirectionx=0;
			pos.sub(getSpeed(),0);
		}
	}
	public void moveRight() {
		if(touchingSide==false && spotdodge==false && crouch==false){
			facingdirection=right;
			xdirection=right;
			ydirection=neutral;
			tdirectionx=0;
			pos.add(getSpeed(), 0);
		}
	}
	public void jump() {
		if(jumpNum>0 && spotdodge==false && crouch==false){
			ydirection=up;
			xdirection=neutral;
			tdirectiony=0;
			velocity.setY(-10);
			jumpNum -= 1;
		}
	}
	public void input() {
		if(stun==false && airdodge==false){
			//jump
			if(isMovingUp()) {
				jumptime++;
				if(jumptime==1) jump();
			}
			else jumptime=0;

			//fast fall and crouch
			if(isMovingDown()){
				if(onPlatform) crouch=true;
				else if(velocity.getY()>0 && velocity.getY()<3) {
					weight=ogweight*1.9;
					velocity.setY(3);
				}
				ydirection=down;
				xdirection=neutral;
				tdirectiony=0;
			}

			//left and right
			if(isMovingLeft()) moveLeft();
			if(isMovingRight()) moveRight();
		}
		else {
			crouch=false;
		}
	}
	
	public void resistance() {
		//resistance
		if(velocity.getX()>=1) {
			resistance.setX(0.4);
		}
		else if(velocity.getX()<0) {
			resistance.setX(-0.4);
		}
		else {
			resistance.setComponents(0,0);
		}
	}
	public void move() {
		//F=ma
		acceleration.setComponents( 0.2*(appliedforce.getX()-resistance.getX()) , -0.2*(weight + normalforce + appliedforce.getY()) );

		//move
		velocity.add(acceleration);
		pos.add(velocity);

		//update position
		setX((int)pos.getX());
		setY((int)pos.getY());
	}
	public void changeImage() {
		if(facingdirection==-1 && xdirection==neutral){
			try
			{
				Image left = ImageIO.read(this.getClass().getResource("/images/marioleft.png") );
				setImage(left);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else if(facingdirection==1 && xdirection==neutral){
			try
			{
				Image right = ImageIO.read(this.getClass().getResource("/images/marioright.png") );
				setImage(right);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else if(facingdirection==-1 && xdirection==left){
			try
			{
				Image left = ImageIO.read(this.getClass().getResource("/images/mariowalkingleft.png") );
				setImage(left);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else if(facingdirection==1 && xdirection==right){
			try
			{
				Image right = ImageIO.read(this.getClass().getResource("/images/mariowalkingright.png") );
				setImage(right);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void takeDamage(double damage, double baseknockback, Vector direction, double templagframes){
		currpercent+=damage;
		System.out.println(name + " took " + (int)damage + " damage and has " + currpercent + "% currently");

		double knockbackamt = 0;
		knockbackamt += currpercent/10;
		knockbackamt += baseknockback;
		knockbackamt *= 0.8;
		jumpNum=2;

		direction.mult(knockbackamt);
		direction.setY(Math.abs(direction.getY())*-1);

		velocity.set(direction);

		addLag(templagframes);
	}
	public void addLag(double frames) {
		lagframes=frames;
	}
	public void setVelocity(int x, int y){
		velocity.setComponents(x, y);
	}

	public int getFacingDirection() {
		return facingdirection;
	}
	public int getXDirection(){
		return xdirection;
	}
	public int getYDirection(){
		return ydirection;
	}
	public Vector getVelocity() {
		return velocity;
	}
	
	public void keyPressed(KeyEvent evt){
		int key = evt.getKeyCode();
		if(key == KeyEvent.VK_1) {
			System.exit(0);
		}
		if(key == KeyEvent.VK_SPACE){
			if(onPlatform==false && airdodgetime>0) airdodge=true;
			if(onPlatform && spotdodgetime>0) spotdodge=true;
		}
		if(key == KeyEvent.VK_DOWN){
			neutralAttack();
		}
		if(key == KeyEvent.VK_LEFT){
			sideAttack(-1);
		}
		if(key == KeyEvent.VK_RIGHT){
			sideAttack(1);
		}
		if(key == KeyEvent.VK_UP){
			upAttack();
		}
	}
	public void keyReleased(KeyEvent evt){
	}
	public void keyTyped(KeyEvent evt){

	}
}
