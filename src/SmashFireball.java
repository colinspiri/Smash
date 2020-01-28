import java.awt.Image;

import javax.swing.JOptionPane;

import org.francisparker.mmaunu.gameengine.AIObject;
import org.francisparker.mmaunu.gameengine.CollisionDetector;
import org.francisparker.mmaunu.gameengine.ControllableObject;
import org.francisparker.mmaunu.gameengine.Drawable;
import org.francisparker.mmaunu.gameengine.GameFrame;

public class SmashFireball extends AIObject {
	private int direction;
	private int left = -1, right = 1;
	
	private Vector ogposition = new Vector(0,0);
	private Vector pos = new Vector(0,0);
	private double x = 0;
	private double y = 0;
	private Vector velocity = new Vector(0,0);
	private Vector acceleration = new Vector(0,0);
	private double energy = 0;
	
	private int distancetraveled = 0;
	private int weight;
	private boolean onPlatform;
	private boolean touchingSide;
	
	private boolean neutralToBowser = false;
	private boolean neutralToMario = false;


	public SmashFireball(int x, int y, Image img, int w, int h, int s, int d){
		super(x,y,img,w,h,s);
		ogposition.setComponents(x, y);
		direction = d;
		weight = 2;
		energy = Math.abs((389-getY())/2);
	}

	public void act(){
		System.out.println("energy " + energy);
		//set up variables
		pos.setComponents(getX(), getY());
		x = pos.getX();
		y = pos.getY();
		int speed = getSpeed();
		int width = getWidth();
		int height = getHeight();
		double leftside = x;
		double rightside = x + width;
		double topside = y;
		double bottomside = y + height;
		
		//remove fireball
		distancetraveled = (int)Math.abs(ogposition.getX()-pos.getX());
		if(distancetraveled>=300){
			GameFrame.removeDrawableObject(this);
		}
		
		//moving
		velocity.setX(speed*direction);
		
		//bounce off platform
		if(bottomside>=389 && bottomside<=409 && rightside>=105 && leftside<=670 && velocity.getY()>0 || onPlatform && bottomside>=380) onPlatform=true;
		else onPlatform=false;

		if(bottomside>=409 && rightside>=85 && leftside<=650) touchingSide=true;
		else touchingSide=false;
		
		if(onPlatform){
			velocity.setY(-1*Math.sqrt(2*energy));
		}
		if(touchingSide){
			velocity.setX(velocity.getX()*-1);
		}
		
		//F=ma
		acceleration.setComponents(0, 0.2*weight);
		
		//move
		velocity.add(acceleration);
		x+=velocity.getX();
		y+=velocity.getY();
		
		setX((int)x);
		setY((int)y);
	}
	
	public int getDirection(){
		return direction;
	}
	public Vector getVelocity() {
		return velocity;
	}
	public Vector getPos() {
		return pos;
	}
	public boolean isNeutralToBowser() {
		return neutralToBowser;
	}
	public boolean isNeutralToMario() {
		return neutralToMario;
	}
	
	public void reverse() {
		direction = direction*-1;
		velocity.setY(-5);
	}
	public void move(double xamt, double yamt){
		setX((int)(getX()+xamt*direction));
		setY((int)(getY()+yamt));
	}
	public void setPos(double tempx, double tempy) {
		x=tempx;
		y=tempy;
	}
	public void reset() {
		ogposition.setComponents(getX(), getY());
		neutralToBowser=false;
		neutralToMario=false;
	}
	public void setNeutralToBowser() {
		neutralToBowser=true;
	}
	public void setNeutralToMario() {
		neutralToMario=true;
	}
	


}
