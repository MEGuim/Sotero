package V2;

import robocode.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import robocode.util.Utils;

public class Leader extends TeamRobot {
    
    private final HashMap<String, Point2D> teammates = new HashMap<>();
    private final ArrayList <ScannedRobotEvent> enemies = new ArrayList<>();
    private final ArrayList <ScannedRobotEvent> leaders = new ArrayList<>();
    private final ArrayList <String> setTarget = new ArrayList<>();
    
    
    RobotStatus currentStatus;
    Message message;
    Double bufferDistance = Math.hypot(18, 18);
    boolean addLeaders = true;
    
    @Override
    public void run() {
        goToEmptiestCorner();
        
        turnRadarRight(360);
        
        
        addLeaders = false;
        while(true) {
            assignTargets();
        }
    }
    @Override
    public void onStatus(StatusEvent e) {
        currentStatus = e.getStatus();
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        
        if (isTeammate(e.getName())) {
            Point2D target = getCoordinates(e);
            
            teammates.put(e.getName(), target);
        } else {
            if (addLeaders && e.getEnergy() < 201d) {
                    leaders.add(e);
            } else {
                enemies.add(e);
            }
        }
    }
    
    @Override
    public void onHitByBullet(HitByBulletEvent event){
        goToEmptiestCorner();        
    }
    
    @Override
    public void onHitRobot(HitRobotEvent event){
        back(50);
        turnRight(90);
        ahead(100);
        goToEmptiestCorner();
    }
    
    private void moveTo(double destX, double destY) {
        double centerAngle = Math.atan2(destX - getX(), destY - getY());
        turnRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        turnRadarRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        double deltaX = destX - getX();
        double deltaY = destY - getY();
        ahead(Math.hypot(deltaX, deltaY));
    }
    
    public void goToEmptiestCorner() {
        ArrayList<Point2D> corners = new ArrayList<>();
        ArrayList<Integer> quadrant = new ArrayList<>();
        corners.add(new Point2D.Double(bufferDistance, bufferDistance));
        corners.add(new Point2D.Double(getBattleFieldHeight() - bufferDistance, bufferDistance));
        corners.add(new Point2D.Double(bufferDistance, (getBattleFieldWidth()) -  bufferDistance));
        corners.add(new Point2D.Double((getBattleFieldHeight() - bufferDistance), (getBattleFieldWidth()) - bufferDistance));

        message.getEnemiesPositions().forEach((enemy) -> {
            if (enemy.getX() < getBattleFieldHeight() / 2) {
                if (enemy.getY() < getBattleFieldWidth() / 2) {
                    quadrant.add(0, quadrant.get(0) + 1);
                } else {
                    quadrant.add(1, quadrant.get(1) + 1);
                }
            } else {
                if (enemy.getY() < getBattleFieldWidth() / 2) {
                    quadrant.add(2, quadrant.get(2) + 1);
                } else {
                    quadrant.add(3, quadrant.get(3) + 1);
                }
            }
        });
        int i = Collections.min(quadrant);
        
        moveTo(corners.get(i).getX(), corners.get(i).getY());
    }
    
    public void assignTargets() {
        
    }
    
    public void calcDistance(ScannedRobotEvent e, Point2D p) {
        
        for (ScannedRobotEvent enemy : enemies) {
            
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
}