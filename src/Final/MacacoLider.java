/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Final;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import robocode.*;
import robocode.util.Utils;

/**
 *
 * @author CFCanelas
 */
public class MacacoLider extends SuperDragao {

    private Map<String, Inimigo> inimigos;
    private HashMap<String, Point2D> teammates = new HashMap<>();
    private RobotStatus currentStatus;
    private boolean pleasure, arousal, dominance;
    private int btaken, topbtaken, bhit;
    private int emotion; // 1-Pride 2-Joy 3-Hope 4-Hate 5-Fear
    String fdp;

    
    
    public void run(){
        
        doScanner();
        
        while(true){
            

            checkdominance();
            checkpleasure();
            checkarousal();
            setemotion();

            
            switch(emotion){
                case 1: antiGravMoveNoEnemies();	
			doFirePower();
			doScanner();
			doGun();
			fire(firePower);
			execute();
                        
                case 2: antiGravMove();
                        doFirePower();
			doScanner();
			doGun();
			fire(firePower);
			execute();
                
                case 3: antiGravMove();
                        doFirePower();
			doScanner();
			doGun();
			fire(firePower * 2);
			execute();
                        
                case 4: target = inimigos.get(fdp);
                        goTo(target.x,target.y);
                        doFirePower();
			doScanner();
			doGun();
			fire(firePower);
			execute();
                
                case 5: 
                    
            }   
            

        }

    }

    
    public void report(){
        
        inimigos.forEach();
        
    }
    
    public void checkdominance(){
        
        if(inimigos.size() > teammates.size())
                dominance=false;
            else dominance=true;
        

    }

    public void checkpleasure() {

        if (btaken > bhit) {
            pleasure = false;
        } else {
            pleasure = true;
        }

    }

    public void checkarousal() {

        if (currentStatus.getEnergy() > 100) {
            arousal = true;
        } else {
            arousal = false;
        }
    }

    public void setemotion() {
        if (dominance) {
            if (arousal) {
                emotion = 1;
            } else {
                emotion = 2;
            }
        } else {
            if (pleasure) {
                emotion = 3;
            } else {
                if (arousal) {
                    emotion = 4;
                } else {
                    emotion = 5;
                }
            }
        }

    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        btaken++;
        inimigos.get(e.getName()).bhit();
        int x = inimigos.get(e.getName()).getBhit();
        if (x > topbtaken) {
            topbtaken = x;
            fdp = e.getName();
        }
    }

    public void onBulletHit(BulletHitEvent e) {
        bhit++;
    }

    
    public void onRobotDeath(RobotDeathEvent e){
        
        if (isTeammate(e.getName())) {
                teammates.remove(e.getName());
            } else{
            inimigos.remove(e.getName());
        }
        
    }
    

    public void onScannedRobot(ScannedRobotEvent e) {

        if (isTeammate(e.getName())) {
            Point2D target = getCoordinates(e);

            teammates.put(e.getName(), target);
        } else {
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

    public Point2D getCoordinates(ScannedRobotEvent e) {
        double angleToTarget = e.getBearing();
        // Calculate the angle to the scanned robot
        double angle = Math.toRadians((currentStatus.getHeading() + angleToTarget % 360));
        // Calculate the coordinates of the robot
        double targetX = (currentStatus.getX() + Math.sin(angle) * e.getDistance());
        double targetY = (currentStatus.getY() + Math.cos(angle) * e.getDistance());

        return new Point2D.Double(targetX, targetY);
    }

    public void goToEmptiestCorner() {
        ArrayList<Point2D> corners = new ArrayList<>();
        ArrayList<Integer> quadrant = new ArrayList<>();
        double bufferDistance = 18;
        corners.add(new Point2D.Double(bufferDistance, bufferDistance));
        corners.add(new Point2D.Double(getBattleFieldHeight() - bufferDistance, bufferDistance));
        corners.add(new Point2D.Double(bufferDistance, (getBattleFieldWidth()) - bufferDistance));
        corners.add(new Point2D.Double((getBattleFieldHeight() - bufferDistance), (getBattleFieldWidth()) - bufferDistance));

        Inimigo inimigo;
        for (String key : inimigos.keySet()) {
            inimigo = inimigos.get(key);
            if (inimigo.getX() < getBattleFieldHeight() / 2) {
                if (inimigo.getY() < getBattleFieldWidth() / 2) {
                    quadrant.add(0, quadrant.get(0) + 1);
                } else {
                    quadrant.add(1, quadrant.get(1) + 1);
                }
            } else {
                if (inimigo.getY() < getBattleFieldWidth() / 2) {
                    quadrant.add(2, quadrant.get(2) + 1);
                } else {
                    quadrant.add(3, quadrant.get(3) + 1);
                }
            }

            int i = Collections.min(quadrant);
            goTo(corners.get(i).getX(), corners.get(i).getY());
        }
    }

}
