/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Canelas;

import static java.awt.Color.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import robocode.*;

/**
 *
 * @author CFCanelas
 */
public class MacacoLider extends SuperDragao {

    private HashMap<String, Point2D> teammates = new HashMap<>();
    private boolean pleasure = true, arousal = true, dominance = true;
    private int btaken = 0, topbtaken = 0, bhit = 0;
    private int emotion = 1; // 1-Pride 2-Joy 3-Hope 4-Hate 5-Fear
    String fdp = "";

    public void run() {


        while (true) {

            doScanner();
            checkdominance();
            checkpleasure();
            checkarousal();
            setemotion();
            selectTarget();
            report();
            System.out.println(inimigos);

            switch (emotion) {
                case 1:
                    setAllColors(BLUE);
                    antiGravMoveNoEnemies();
                    
                    if ( hasTarget ) {
                        doGun();
                        fire(firePower);
                    }
                    break;
                case 2:
                    setAllColors(BLACK);
                    antiGravMove();
                    if ( hasTarget ) {
                        doGun();
                        fire(firePower);
                    }
                    break;
                case 3:
                    setAllColors(WHITE);
                    antiGravMove();
                    if ( hasTarget ) {
                        doGun();
                        fire(firePower * 2);
                    }
                    break;
                case 4:
                    setAllColors(RED);
                    if ( !"".equals(fdp) ) {
                        target = inimigos.get(fdp);
                        goTo(target.x, target.y);
                        doGun();
                        fire(firePower);
                    }
                    break;
                case 5:
                    setAllColors(YELLOW);
                    goToEmptiestCorner();
                    if ( hasTarget ) {
                        doGun();
                        fire(firePower);
                    }
                    break;
            }
            execute();
        }

    }

    public void report() {
        try {
            Arbitros arbitros = new Arbitros(inimigos);
            broadcastMessage(arbitros);
        } catch (IOException ex) {
            Logger.getLogger(MacacoLider.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void checkdominance() {

        dominance = inimigos.size() <= teammates.size();

    }

    public void checkpleasure() {

        pleasure = btaken <= bhit;

    }

    public void checkarousal() {
        arousal = getEnergy() > 100;
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

    public void onRobotDeath(RobotDeathEvent e) {

        if (isTeammate(e.getName())) {
            teammates.remove(e.getName());
        } else {
            inimigos.remove(e.getName());
        }
    }
    
    public void onScannedRobot(ScannedRobotEvent e) {
        if (isTeammate(e.getName())) {
                double absbearing_rad = (getHeadingRadians()+e.getBearingRadians())%(2*PI);
                Point2D pos = new Point2D.Double(); 
                pos.setLocation( getX()+Math.sin(absbearing_rad)*e.getDistance(),  getY()+Math.cos(absbearing_rad)*e.getDistance());
                teammates.put(e.getName(), pos);
        } else {
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
        }
	}

    public Point2D getCoordinates(ScannedRobotEvent e) {
        double angleToTarget = e.getBearing();
        // Calculate the angle to the scanned robot
        double angle = Math.toRadians((getHeading() + angleToTarget % 360));
        // Calculate the coordinates of the robot
        double targetX = (getX() + Math.sin(angle) * e.getDistance());
        double targetY = (getY() + Math.cos(angle) * e.getDistance());

        return new Point2D.Double(targetX, targetY);
    }

    public void goToEmptiestCorner() {
        ArrayList<Point2D> corners = new ArrayList<>();
        ArrayList<Integer> quadrant = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            quadrant.add(i, 0);
        }
        double bufferDistance = 40;
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
    
     void doScanner() {
        setTurnRadarLeftRadians(2 * PI);
    }
}
