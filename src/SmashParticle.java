import java.awt.Image;
import javax.swing.JOptionPane;
import org.francisparker.mmaunu.gameengine.AIObject;
import org.francisparker.mmaunu.gameengine.CollisionDetector;
import org.francisparker.mmaunu.gameengine.ControllableObject;
import org.francisparker.mmaunu.gameengine.Drawable;
import org.francisparker.mmaunu.gameengine.GameFrame;

public class SmashParticle extends AIObject {
	int xdir = 0;
	int ydir = 0;
	private int fade;
	private boolean neutralToBowser = false;
	private boolean neutralToMario = false;

	public SmashParticle(int x, int y, Image img, int w, int h, Vector tempdir, int s, int tempfade){
		super(x,y,img,w,h,s);
		xdir = (int)tempdir.getX();
		ydir = (int)tempdir.getY();
		fade=tempfade;
	}

	public void act(){
		double x = getX();
		double y = getY();
		
		x+=getSpeed()*xdir;
		y+=getSpeed()*ydir;
		
		fade--;
		if(fade<=0){
			GameFrame.removeDrawableObject(this);
		}
		
		setX((int)x);
		setY((int)y);
	}
	
	public boolean isNeutralToBowser() {
		return neutralToBowser;
	}
	public int getDirection() {
		return xdir;
	}
	public boolean isNeutralToMario() {
		return neutralToMario;
	}
	
	public void setNeutralToBowser() {
		neutralToBowser=true;
	}
	public void setNeutralToMario() {
		neutralToMario=true;
	}
}
