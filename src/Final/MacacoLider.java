/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Final;

import java.util.Map;
import robocode.*;
import robocode.util.Utils;


/**
 *
 * @author CFCanelas
 */
public class MacacoLider extends SuperDragao{
    
    private Map<String, Inimigo> inimigos;
    private RobotStatus currentStatus;
    
    
    
    
    
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
    
    
    
}
