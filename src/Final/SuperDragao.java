
package Final;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Hashtable;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

/**
 *
 * @author CFCanelas
 */
public class SuperDragao extends AntiGravityBot {

    double firePower;				//the power of the shot we will be using

    public void run() {
		inimigos = new Hashtable();
		target = new Inimigo();
		target.distance = 100000;						//initialise the distance so that we can select a target
		setColors(Color.red,Color.blue,Color.green);	//sets the colours of the robot
		//the next two lines mean that the turns of the robot, gun and radar are independant
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		turnRadarRightRadians(2*PI);					//turns the radar right around to get a view of the field
		while(true) {
			antiGravMove();					//Move the bot
			doFirePower();					//select the fire power to use
			doScanner();					//Oscillate the scanner over the bot
			doGun();
			out.println(target.distance);	//move the gun to predict where the enemy will be
			fire(firePower);
			execute();						//execute all commands
		}
	}
    
    /**
     * keep the scanner turning*
     */
    void doScanner() {
        setTurnRadarLeftRadians(2 * PI);
    }
    
    void doFirePower() {
		firePower = 400/target.distance;//selects a bullet power based on our distance away from the target
		if (firePower > 3) {
			firePower = 3;
		}
	}
    
    /**Move the gun to the predicted next bearing of the enemy**/
	void doGun() {
		long time = getTime() + (int)Math.round((getRange(getX(),getY(),target.x,target.y)/(20-(3*firePower))));
		Point2D.Double p = target.guessPosition(time);
		
		//offsets the gun by the angle to the next shot based on linear targeting provided by the enemy class
		double gunOffset = getGunHeadingRadians() - (Math.PI/2 - Math.atan2(p.y - getY(), p.x - getX()));
		setTurnGunLeftRadians(normaliseBearing(gunOffset));
	}
    
    /**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		Inimigo en;
		if (inimigos.containsKey(e.getName())) {
			en = (Inimigo)inimigos.get(e.getName());
		} else {
			en = new Inimigo();
			inimigos.put(e.getName(),en);
		}
		//the next line gets the absolute bearing to the point where the bot is
		double absbearing_rad = (getHeadingRadians()+e.getBearingRadians())%(2*PI);
		//this section sets all the information about our target
		en.name = e.getName();
		double h = normaliseBearing(e.getHeadingRadians() - en.heading);
		h = h/(getTime() - en.ctime);
		en.changehead = h;
		en.x = getX()+Math.sin(absbearing_rad)*e.getDistance(); //works out the x coordinate of where the target is
		en.y = getY()+Math.cos(absbearing_rad)*e.getDistance(); //works out the y coordinate of where the target is
		en.bearing = e.getBearingRadians();
		en.heading = e.getHeadingRadians();
		en.ctime = getTime();				//game time at which this scan was produced
		en.speed = e.getVelocity();
		en.distance = e.getDistance();	
		en.live = true;
		if ((en.distance < target.distance)||(target.live == false)) {
			target = en;
		}
	}
  
    
}
