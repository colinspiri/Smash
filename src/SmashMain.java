import java.awt.Color;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.francisparker.mmaunu.gameengine.GameFrame;

public class SmashMain {

	public static void main(String[] args) {
		new SmashMain();
	}

	@SuppressWarnings("static-access")
	public SmashMain() {
		Image marioimg = null, bowserimg = null, groundimg = null, backgroundimg = null;
		try
		{
			marioimg = ImageIO.read(this.getClass().getResource("/images/marioleft.png") );
			bowserimg = ImageIO.read(this.getClass().getResource("/images/bowser.png"));
			groundimg = ImageIO.read(this.getClass().getResource("/images/ground.png"));
			backgroundimg = ImageIO.read(this.getClass().getResource("/images/background.jpg"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		GameFrame mainWindow = new GameFrame("SUPER SMASH BROS 5",1000,750,false,false,true);

		SmashPlatform ground = new SmashPlatform(71,368,groundimg,612,231);
		
		String temp = JOptionPane.showInputDialog(null, "SET YOUR DIFFICULTY 1-11");
		int difficulty = Integer.parseInt(temp);
		
		SmashMario mario = new SmashMario(300,200,marioimg,65,79); //       	 		speed = 4, weight = 6
		SmashBowser bowser = new SmashBowser(400,200,bowserimg,115,100,difficulty); // 	speed = 3, weight = 8

		mainWindow.addDrawableObject(ground);
		mainWindow.addDrawableObject(bowser);
		mainWindow.setPrimaryControllableObject(mario);
		
		mainWindow.addKeyListener(mario);
		mainWindow.addMouseListener(mario);
		mainWindow.setGameBackground(backgroundimg);
		mainWindow.setFPS(60);
		
		mainWindow.setVisible(true);
	}

}
