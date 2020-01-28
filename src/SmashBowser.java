import java.awt.Image;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.francisparker.mmaunu.gameengine.AIObject;
import org.francisparker.mmaunu.gameengine.CollisionDetector;
import org.francisparker.mmaunu.gameengine.Drawable;
import org.francisparker.mmaunu.gameengine.DrawableGameObject;
import org.francisparker.mmaunu.gameengine.GameFrame;

public class SmashBowser extends SmashCharacter {
	public int difficulty; // 1-11
	public double chance = 0;
	int state = 0, passive = 1, aggressive = 2;
	int t = 150;
	int walkstate = 1, towards = 1, away = 2;

	public SmashBowser(int x, int y, Image img, int width, int height, int tempdifficulty) {
		super(x, y, img, width, height, 3, 7, "Bowser");
		if(tempdifficulty>0 && tempdifficulty<=10) difficulty = tempdifficulty;
		else difficulty = 4;
		chance = Logic.map(difficulty, 1, 11, 5, 99);
		System.out.println(chance);
	}

	public void neutralAttack() { //very slow punch
		if(attacklag==0){
			try
			{
				Image punchimg = ImageIO.read( this.getClass().getResource("/images/punchparticle.png") );
				int speed = 2;
				if(isMovingLeft() || isMovingRight()) speed+=getSpeed();
				SmashParticle p = new SmashParticle(getX(), getY()+40, punchimg, 50, 18, new Vector(getFacingDirection(),0), speed, 10);
				GameFrame.addDrawableObject(p);
				p.setNeutralToBowser();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			endAttack(10,50);
		}
	}
	public void sideAttack(int direction) { //fire breathing
		if(attacklag==0){
			try
			{
				Image fireballimg = ImageIO.read( this.getClass().getResource("/images/fireball.png") );
				SmashFireball b = new SmashFireball(getX(), getY(), 
						fireballimg, 50, 38, 2, direction);
				GameFrame.addDrawableObject(b);
				b.setNeutralToBowser();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			endAttack(10,20);
		}
	}
	public void upAttack() { //spin attack
		if(super.getAttackJumpAble() && attacklag==0){
			velocity.setY(-3);
			try
			{
				Image punchimg = ImageIO.read( this.getClass().getResource("/images/punchparticle.png") );
				int speed = 2;
				if(isMovingLeft() || isMovingRight()) speed+=getSpeed();
				SmashParticle p = new SmashParticle(getX(), getY()+40, punchimg, 50, 18, new Vector(getFacingDirection(),0), speed, 10);
				GameFrame.addDrawableObject(p);
				p.setNeutralToBowser();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			endAttack(20,30);
			super.setAttackJumpAble(false);
		}
	}

	public void collisionAttacks(){
		ArrayList<DrawableGameObject> listocollisions =  CollisionDetector.moveCollidesWithMultiple(this, getX(), getY());

		for(DrawableGameObject collision : listocollisions){

			if(collision instanceof SmashFireball){
				SmashFireball fball = (SmashFireball) collision;

				if(airdodge==true || spotdodge==true){
					fball.reverse();
					fball.reset();
					fball.setNeutralToBowser();
				}
				else {
					if(fball.isNeutralToBowser()==false){

						Vector direction = new Vector(Math.cos(45),Math.sin(45));

						direction.setX(direction.getX()*fball.getDirection());

						takeDamage(5, 2, direction,30);

						GameFrame.removeDrawableObject(fball);
					}
				}
			}
			if(collision instanceof SmashParticle){
				SmashParticle particle = (SmashParticle)collision;

				if(airdodge==true || spotdodge==true){
					GameFrame.removeDrawableObject(particle);
				}
				else {
					if(particle.isNeutralToBowser()==false){
						Vector direction = new Vector(Math.cos(45),Math.sin(45));
						direction.setX(direction.getX()*particle.getDirection());

						takeDamage(2, 5, direction, 30);

						GameFrame.removeDrawableObject(particle);
					}
				}
			}
		}
	}

	public void controllable() {}
	public void ai() {
		//determining whether to act correctly or not
		double random = Logic.random100();
		boolean act = false;
		if(random<=chance) act=true;
		//change state
		t++;
		if(t>=120){
			t=0;
			if(act) state = aggressive;
			else state = passive;
		}

		//SmashMario mario = (SmashMario) GameFrame.getPrimaryControllableObject();
		double marioX = GameFrame.getPrimaryControllableObject().getX();
		double marioY = GameFrame.getPrimaryControllableObject().getY();
		double distanceToMario = Logic.distanceFrom(getX(), 0, marioX, 0);

		//getting back on stage
		if(onPlatform==false && (pos.getX()<100 || pos.getX()>600)){
			state=aggressive;
			if(pos.getX()<100) moveRight();
			if(pos.getX()>600) moveLeft();
			if(pos.getY()>=300 && velocity.getY()>0 && onPlatform==false) {
				if(jumpNum>0) jump();
				else upAttack();
			}
		}
		//movement
		else {
			if(state==passive) {
				if(walkstate==towards){
					if(distanceToMario<=150) walkstate=away;
					if(marioX<getX()) moveLeft();
					if(marioX>getX()) moveRight();
				}
				else if(walkstate==away) {
					jump();
					if(distanceToMario>=300) walkstate=towards;
					if(marioX<getX()) moveRight();
					if(marioX>getX()) moveLeft();
				}
			}
			else if(state==aggressive && distanceToMario>100){
				if(marioX<getX()) moveLeft();
				if(marioX>getX()) moveRight();
				if(marioY<getY()) jump();
			}

			//attacking
			if(act && state==aggressive) {
				if(distanceToMario<=75) neutralAttack();
				else if(distanceToMario>75) sideAttack(getFacingDirection());
			}
		}

		//System.out.println(t);
		ArrayList<DrawableGameObject> list = CollisionDetector.moveCollidesWithMultiple(this, getX(), getY());
		for(DrawableGameObject collision : list){
			if((collision instanceof SmashFireball || collision instanceof SmashParticle) && act){
				if(onPlatform==false && airdodgetime>0) airdodge=true;
				if(onPlatform && spotdodgetime>0) spotdodge=true;
			}
		}

	}
}
