/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Final;

import static java.awt.Color.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;
import robocode.*;

/**
 *
 * @author CFCanelas
 */
public class MacacoLider extends SuperDragao {

    private HashMap<String, Point2D> teammates = new HashMap<>();
    private RobotStatus currentStatus;
    private boolean pleasure, arousal, dominance;
    private int btaken, topbtaken, bhit;
    private int emotion; // 1-Pride 2-Joy 3-Hope 4-Hate 5-Fear
    String fdp;

    public void run() {

        doScanner();

        while (true) {

            checkdominance();
            checkpleasure();
            checkarousal();
            setemotion();
            doScanner();
            selectTarget();

            switch (emotion) {
                case 1:
                    setAllColors(BLUE);
                    report();
                    antiGravMoveNoEnemies();
                    
                    if ( hasTarget ) {
                        doGun();
                        fire(firePower);
                    }

                case 2:
                    setAllColors(BLACK);
                    report();
                    antiGravMove();
                    if ( hasTarget ) {
                        doGun();
                        fire(firePower);
                    }

                case 3:
                    setAllColors(WHITE);
                    report();
                    antiGravMove();
                    if ( hasTarget ) {
                        doGun();
                        fire(firePower * 2);
                    }

                case 4:
                    setAllColors(RED);
                    report();
                    if ( hasTarget ) {
                        target = inimigos.get(fdp);
                        goTo(target.x, target.y);
                        doGun();
                        fire(firePower);
                    }

                case 5:
                    setAllColors(YELLOW);
                    report();
                    goToEmptiestCorner();
                    if ( hasTarget ) {
                        doGun();
                        fire(firePower);
                    }
                    

            }
            execute();

        }

    }

    public void report() {
        try {
            for (String key : inimigos.keySet()) {
                broadcastMessage(inimigos.get(key));
            }
        } catch (IOException e) {
            printStackTrace(e);
        }

    }

    public void checkdominance() {

        if (inimigos.size() > teammates.size()) {
            dominance = false;
        } else {
            dominance = true;
        }

    }

    public void checkpleasure() {

        if (btaken > bhit) {
            pleasure = false;
        } else {
            pleasure = true;
        }

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
            Point2D target = getCoordinates(e);

            teammates.put(e.getName(), target);
        } else {
            double enemyBearing = e.getBearing();
            double enemyHeading = e.getHeading();
            double enemyEnergy = e.getEnergy();

            // Calculate the angle to the scanned robot
            double angle = Math.toRadians((getHeading() + enemyBearing % 360));
            // Calculate the coordinates of the robot
            double enemyX = (getX() + Math.sin(angle) * e.getDistance());
            double enemyY = (getY() + Math.cos(angle) * e.getDistance());

            Inimigo enemy = new Inimigo(enemyX, enemyY, enemyBearing, enemyHeading, enemyEnergy);

            inimigos.put(e.getName(), enemy);
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
