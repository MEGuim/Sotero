package V2;

import java.util.HashMap;
import java.util.Map;
import robocode.*;
import robocode.util.Utils;


public class Robo extends TeamRobot {
    
    private Map<String, Inimigo> inimigos;
    private RobotStatus currentStatus;
    
    @Override
    public void run() {
        inimigos = new HashMap<>();
        turnRadarRight(360);
        
        while (true) {
            
        }
    }
    
     @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        
         if (!isTeammate(e.getName())) {
            double enemyBearing = e.getBearing();
            double enemyHeading = e.getHeading();
            double enemyEnergy = e.getEnergy();
            
            // Calculate the angle to the scanned robot
            double angle = Math.toRadians((currentStatus.getHeading() + enemyBearing % 360));
            // Calculate the coordinates of the robot
            double enemyX = (currentStatus.getX() + Math.sin(angle) * e.getDistance());
            double enemyY = (currentStatus.getY() + Math.cos(angle) * e.getDistance());
            
            Inimigo enemy = new Inimigo(enemyX, enemyY, enemyBearing, enemyHeading, enemyEnergy);
            
            inimigos.put(e.getName(), enemy);
         }
    }
    
    @Override
    public void onStatus(StatusEvent e) {
        currentStatus = e.getStatus();
    }
    
    private void moveTo(double destX, double destY) {
        double centerAngle = Math.atan2(destX - getX(), destY - getY());
        turnRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        turnRadarRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        double deltaX = destX - getX();
        double deltaY = destY - getY();
        ahead(Math.hypot(deltaX, deltaY));
    }
    
    
}
