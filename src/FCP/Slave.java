
package FCP;

import java.awt.geom.Point2D;
import robocode.Droid;
import robocode.MessageEvent;
import robocode.RobotStatus;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;


public class Slave extends TeamRobot implements Droid {
    
    public ScannedRobotEvent target;
    RobotStatus currentStatus;

    @Override
    public void run() {
    }
    
    public void onMessageReceived(MessageEvent e) {
        target = (ScannedRobotEvent) e.getMessage();
        pointAndShoot();
        moveToTarget();
    }
    
    public void pointAndShoot() {
        double absoluteBearing = getHeadingRadians() + target.getBearingRadians();
        setTurnGunRightRadians(Utils.normalRelativeAngle(absoluteBearing - getGunHeadingRadians() + (target.getVelocity() * Math.sin(target.getHeadingRadians() - absoluteBearing) / 13.0)));
        setFire(3.0);
    }
    
    private void moveToTarget() {
        double angleToTarget = target.getBearing();
        // Calculate the angle to the scanned robot
        double angle = Math.toRadians((currentStatus.getHeading() + angleToTarget % 360));
        
        double destX = (currentStatus.getX() + Math.sin(angle) * target.getDistance());
        double destY = (currentStatus.getY() + Math.cos(angle) * target.getDistance());
        
        double centerAngle = Math.atan2(destX - getX(), destY - getY());
        turnRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        turnRadarRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        
        double deltaX = destX - getX();
        double deltaY = destY - getY();
        ahead(Math.hypot(deltaX, deltaY));
    }
    
    public Point2D getCoordinates(ScannedRobotEvent e) {
        double angleToTarget = e.getBearing();
        // Calculate the angle to the scanned robot
        double angle = Math.toRadians((currentStatus.getHeading() + angleToTarget % 360));
        // Calculate the coordinates of the robot
        double targetX = (currentStatus.getX() + Math.sin(angle) * e.getDistance());
        double targetY = (currentStatus.getY() + Math.cos(angle) * e.getDistance());


        return new Point2D.Double(targetX, targetY); 
    }
}
