import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import org.francisparker.mmaunu.gameengine.CollisionDetector;
import org.francisparker.mmaunu.gameengine.Drawable;
import org.francisparker.mmaunu.gameengine.DrawableGameObject;

import javax.imageio.ImageIO;
import javax.swing.event.MouseInputListener;

import org.francisparker.mmaunu.gameengine.GameFrame;

public class SmashMario extends SmashCharacter implements MouseInputListener {

	public SmashMario(int x, int y, Image img, int width, int height) {
		super(x, y, img, width, height, 4, 6, "Mario");
	}

	public void neutralAttack() { //punch
		if(attacklag==0){
			try
			{
				Image punchimg = ImageIO.read( this.getClass().getResource("/images/punchparticle.png") );
				int speed = 5;
				if(isMovingLeft() || isMovingRight()) speed+=getSpeed();
				SmashParticle p = new SmashParticle(getX(), getY()+25, punchimg, 50, 18, new Vector(getFacingDirection(),0), speed, 10);
				GameFrame.addDrawableObject(p);
				p.setNeutralToMario();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			endAttack(5,10);
		}
	}
	public void sideAttack(int direction) { //fire ball
		if(attacklag==0){
			try
			{
				Image fireballimg = ImageIO.read( this.getClass().getResource("/images/fireball.png") );
				SmashFireball b = new SmashFireball(getX()+getWidth()/2, getY()+getHeight()/2, 
						fireballimg, 50, 38, getSpeed()+3, direction);
				GameFrame.addDrawableObject(b);
				b.setNeutralToMario();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			endAttack(10,40);
		}
	}
	public void upAttack() { //punch jump
		if(super.getAttackJumpAble() && attacklag==0){
			try
			{
				Image punchimg = ImageIO.read( this.getClass().getResource("/images/punchparticleup.png") );
				int x = getX();
				if(getFacingDirection()==1) x+=35;
				SmashParticle p = new SmashParticle(x, getY()+25, punchimg, 50, 18, new Vector(0,-1), 14, 10);
				GameFrame.addDrawableObject(p);
				p.setNeutralToMario();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			super.setVelocity((int)super.getVelocity().getX(), -10);
			super.setAttackJumpAble(false);
			endAttack(10,30);
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
					fball.setNeutralToMario();
				}
				else {
					if(fball.isNeutralToMario()==false){

						Vector direction = new Vector(Math.cos(45),Math.sin(45));

						direction.setX(direction.getX()*fball.getDirection());

						takeDamage(1, 6, direction,30);

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
					if(particle.isNeutralToMario()==false){
						Vector direction = new Vector(Math.cos(45),Math.sin(45));
						direction.setX(direction.getX()*particle.getDirection());

						takeDamage(2, 5, direction, 5);

						GameFrame.removeDrawableObject(particle);
					}
				}
			}
		}
	}

	public void controllable() {}
	public void ai() {}

	@Override
	public void mouseClicked(MouseEvent e) {

	}
	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		GameFrame.setMessage("mouse pressed at (" + x + ", " + y + ").");

	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
}
