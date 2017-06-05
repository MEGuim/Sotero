
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
    
    /**
     * keep the scanner turning*
     */
    void doScanner() {
        setTurnRadarLeftRadians(2 * PI);
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
