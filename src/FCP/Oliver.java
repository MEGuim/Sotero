package FCP;

import robocode.*;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import robocode.util.Utils;
import java.util.Collections;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html
/**
 * Sistemas - a robot by (your name here)
 */
public class Oliver extends AdvancedRobot {

    /**
     * run: Teste1's default behavior
     */
    private DistanceThread counter;
    public ArrayList<Point> alvos;
    public double dist = 0;
    public double distance = 0;
    public double botSize;
    public double realDistance = 0;
    public double walk;
    public double evasionDistance;
    public double margem = 40;
    public static double realDistanceTotal = 0;
    public static double total = 0;
    public static double fim = 0;
    public int timesHitWall = 0;
    public int timesHitOpponent = 0;
    public int timesMissedOpponent = 0;
    public int timesHitByOpponent = 0;
    public int cont = 0;
    public boolean foundObstacle;
    public boolean end = false;
    public boolean origin = false;
    public boolean scan = false;
    public boolean robo12Stuck = false;
    public boolean robo23Stuck = false;
    

    public void run() {
        // Initialization of the robot should be put here

        alvos = new ArrayList<Point>();
        botSize = Math.hypot(getHeight(), getWidth());
        evasionDistance = Math.hypot(getHeight(), getWidth()) * 1.25;

        setColors(Color.blue, Color.blue, Color.blue); // body,gun,radar
        setScanColor(Color.green);
        setAdjustRadarForRobotTurn(true);

        double x = 20;
        double y = 20;
        while (origin == false) {
            moveTo(x, y);
            if (getX() < 30 && getY() < 30) {
                origin = true;
            } else {
                back(evasionDistance * 1.35);
                turnRight(90);
                ahead(evasionDistance * 1.35);
            }
        }
        while (scan == false) {
            turnRadarRight(360);
        }
        System.out.println(alvos.get(0).toString());
        System.out.println(alvos.get(1).toString());
        System.out.println(alvos.get(2).toString());

        ordena();
        System.out.println(alvos.get(0).toString());
        System.out.println(alvos.get(1).toString());
        System.out.println(alvos.get(2).toString());
        
        calculateRealDistance();

        this.counter = new DistanceThread(this);
        this.counter.start();

        // Robot main loop
        while (!end) {
            Point p = alvos.get(0);
            Point p2 = alvos.get(1);
            Point p3 = alvos.get(2);
            double destX = p.getX();
            double destY = p.getY();
            double destX2 = p2.getX();
            double destY2 = p2.getY();
            double destX3 = p3.getX();
            double destY3 = p3.getY();
            
            
            moveTo(destX - margem, destY + margem);
        
            if (destY2 < destY) 
                moveTo(destX + margem, destY + margem);
            else 
                moveTo(destX2 - margem, destY2 + margem);
            
            moveTo(destX2 + margem, destY2 + margem);
            
            if (destX3 < destX2) 
                moveTo(destX2 + margem, destY2 - margem);
            else
                moveTo(destX3 + margem, destY3 + margem);
            
            moveTo(destX3 + margem, destY3 - margem);
            
            moveTo(x, y);
            end = true;
        }
    }

    /**
     * moveTo: Moves Robot to position of coordinates (destX, destY)
     */
    private void moveTo(double destX, double destY) {
        double centerAngle = Math.atan2(destX - getX(), destY - getY());
        turnRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        turnRadarRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        double deltaX = destX - getX();
        double deltaY = destY - getY();
        ahead(Math.hypot(deltaX, deltaY));
    }
    
    public void calculateRealDistance() {
        Point r1 = alvos.get(0);
        Point r2 = alvos.get(1);
        Point r3 = alvos.get(2);
        
        double destX1 = r1.getX();
        double destY1 = r1.getY();
        double destX2 = r2.getX();
        double destY2 = r2.getY();
        double destX3 = r3.getX();
        double destY3 = r3.getY();
        double deltaX;
        double deltaY;
        double d12;
        double d23;
        
        deltaX = destX2 - destX1;
        deltaY = destY2 - destY1;
        d12 = Math.hypot(deltaX, deltaY);
        if (d12 < 50)
            robo12Stuck = true;
        
        deltaX = destX3 - destX2;
        deltaY = destY3 - destY2;
        d23 = Math.hypot(deltaX, deltaY);
        if (d23 < 50)
            robo23Stuck = true;
        
        realDistance = Math.hypot(destX1 - 20, destY1 - 20) + Math.hypot(destX3 - 20, destY3 - 20) + d12 + d23;
    }

    
    public double distanceBetween(int frst, int scnd) {
        Point r1 = alvos.get(frst - 1);
        Point r2 = alvos.get(scnd - 1);
        
        double destX1 = r1.getX();
        double destY1 = r1.getY();
        double destX2 = r2.getX();
        double destY2 = r2.getY();
        
        double deltaX = destX2 - destX1;
        double deltaY = destY2 - destY1;
        double ret = Math.hypot(deltaX, deltaY);
        
        return ret;
    }
            
    public void ordena() {
        int n = 0;
        while (alvos.get(0).equals(alvos.get(1)) || alvos.get(0).equals(alvos.get(2)) || alvos.get(2).equals(alvos.get(1))) {
            System.out.println("Entrei no while");
            alvos.clear();
            if (n % 2 == 0) {
                moveTo(20, 600);
            } else {
                moveTo(600, 20);
            }
            turnRadarRight(360);
            moveTo(20, 20);
            n++;
            System.out.println(alvos.get(0).toString());
            System.out.println(alvos.get(1).toString());
            System.out.println(alvos.get(2).toString());

        }
        Collections.sort(alvos, new PointCompare());

        if (alvos.get(1).getY() < alvos.get(2).getY()) {

            Point temp = new Point();
            temp.setLocation(alvos.get(1));
            alvos.get(1).setLocation(alvos.get(2));
            alvos.get(2).setLocation(temp);
        }
    }

    /**
     * onScannedRobot: What to do when you see another robot
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        if (e.getVelocity() == 0) {
            double angleToEnemy = e.getBearing();

            // Calculate the angle to the scanned robot
            double angle = Math.toRadians((getHeading() + angleToEnemy % 360));

            // Calculate the coordinates of the robot
            double targetX = (getX() + Math.sin(angle) * e.getDistance());
            double targetY = (getY() + Math.cos(angle) * e.getDistance());

            Point novo = new Point();
            novo.setLocation(targetX, targetY);
            if (alvos.contains(novo)) {
            } else {
                alvos.add(novo);
            }
            if (alvos.size() >= 3) {
                scan = true;
            } else {
                scan = false;
            }
        } else {
            scan = false;
        }
    }

    public void onBulletHit(BulletHitEvent e) {
        turnGunRight(720);
        timesHitOpponent++;
    }

    public void onBulletHit(BulletMissedEvent e) {
        timesMissedOpponent++;
    }

    public void onRoundEnded(RoundEndedEvent e) {
        double distance = counter.getDistance();
        System.out.println("---------END OF ROUND---------");
        System.out.println("Distancia Real: " + realDistance);
        System.out.println("Distance: " + distance + " pixels");
        counter.kill();
    }

    public void onDeath(DeathEvent e) {
        double distance = counter.getDistance();
        total += distance;
        realDistanceTotal += realDistance;
        System.out.println("--------I DIED--------");
        System.out.println("Walls hit: " + timesHitWall);
        System.out.println("Hits: " + timesHitOpponent);
        System.out.println("Misses: " + timesMissedOpponent);
        System.out.println("Hit by Enemy Bullets: " + timesHitByOpponent);
        System.out.println("Distance: " + distance);
        System.out.println("Real Distance: " + realDistance);
        System.out.println("Ratio: " + distance / realDistance);
        System.out.println("Total distance: " + total);
        System.out.println("Total Real Distance: " + realDistanceTotal);
        System.out.println("Total Ratio: " + total / realDistanceTotal);
    }

    public void onBattleEnded(BattleEndedEvent e) {
        System.out.println("--------END OF BATTLE---------");
        System.out.println("Total Distance: " + total);
        System.out.println("Total Real Distance: " + realDistanceTotal);
        System.out.println("Ratio: " + total / realDistanceTotal);
    }

}
