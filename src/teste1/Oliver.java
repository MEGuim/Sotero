
import robocode.*;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import robocode.util.Utils;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Sistemas - a robot by (your name here)
 */
public class Oliver extends AdvancedRobot{


    /**
     * run: Teste1's default behavior
     */
	private DistanceThread counter;
	 
    public int cont =0;
    public double dist = 0;
    public double distance = 0;
    public static double total = 0;
    public static double fim = 0;
    public int timesHitWall = 0;
    public int timesHitOpponent = 0;
    public int timesMissedOpponent = 0;
    public int timesHitByOpponent = 0;
    public double walk;
    public double evasionDistance;
    public  ArrayList<Point> alvos;
    
    
    
    public void run() {
            // Initialization of the robot should be put here
            
            this.counter = new DistanceThread(this);
            alvos = new ArrayList();
            evasionDistance = Math.hypot(getHeight(), getWidth()) * 2;
	     
        
            setColors(Color.blue,Color.blue,Color.blue); // body,gun,radar
            setScanColor(Color.green);
            setAdjustRadarForRobotTurn(true);
        
            double x = 20;
            double y = 20;
            moveTo(x, y);
		
		
            if (getX() > (x - 5) && getX() < (x + 5) && getY() > (y - 5) && getY() < (y + 5)){
                    turnRadarRight(360);
                    this.counter.start();
            }
            
            turnRadarRight(360);
            System.out.println(alvos.get(0).toString());
            System.out.println(alvos.get(1).toString());
            movetofirst();
            
            // Robot main loop
            while(true) {             
                //scan();
            }
    }
    
    
    /**
     * moveTo: Moves Robot to position of coordinates (destX, destY)
     */
    private void moveTo(double destX, double destY) {
        double centerAngle = Math.atan2(destX-getX(), destY-getY());
        turnRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        turnRadarRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        double deltaX = destX - getX();
        double deltaY = destY - getY();
        ahead(Math.hypot(deltaX, deltaY));
    }
    
    private void movetofirst(){
        
        Point p = alvos.get(0);
        
        double destX = p.getX();
        double destY = p.getY();
        
        double centerAngle = Math.atan2(destX - (evasionDistance) - getX(), (destY + (evasionDistance) - getY()));
        turnRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        
        ahead(Math.hypot(((destX-getWidth())-getX()), ((destY+getHeight())-getY())));
        
        turnLeftRadians(getHeadingRadians());
        ahead(getHeight());
        turnRight(90);
        ahead(evasionDistance*1.35);
        
        
    }
    
    private void movetosecond(){
        
        Point p = alvos.get(1);
        
        double destX = p.getX();
        double destY = p.getY();
        
        double centerAngle = Math.atan2(destX - getX(), (destY + getHeight() - getY()));
        turnRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        
        ahead(Math.hypot((destX - getX()), ((destY+getHeight())-getY())));
        
        turnLeftRadians(getHeadingRadians()- 3.14159/2);
        ahead(getHeight()*1.5);
        turnRight(90);
        ahead(evasionDistance*1.35);
        
    }
    
    private void movetothird(){
        
        Point p = alvos.get(2);
        
        double destX = p.getX();
        double destY = p.getY();
        
        double centerAngle = Math.atan2(destX - getX(), (destY + getHeight() - getY()));
        turnRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        
        ahead(Math.hypot((destX - getX()), ((destY+getHeight())-getY())));
        
        turnLeftRadians(getHeadingRadians()- 3.14159/2);
        ahead(getHeight()*1.5);
        turnRight(90);
        ahead(evasionDistance*1.35);
        turnRight(90);
        ahead(evasionDistance*1.35);     
        
    }

    /**
     * onScannedRobot: What to do when you see another robot
     */
    public void onScannedRobot(ScannedRobotEvent e) {
            
            double angleToEnemy = e.getBearing();

            // Calculate the angle to the scanned robot
            double angle = Math.toRadians((getHeading() + angleToEnemy % 360));

            // Calculate the coordinates of the robot
            double targetX = (getX() + Math.sin(angle) * e.getDistance());
            double targetY = (getY() + Math.cos(angle) * e.getDistance());
            
            Point novo = new Point();
            novo.setLocation(targetX,targetY);
            alvos.add(novo);
    }

    
    public void onBulletHit(BulletHitEvent e){
        turnGunRight(720);
        timesHitOpponent++;
        }

   
    public void onBulletHit(BulletMissedEvent e) {
        timesMissedOpponent++;
    }
    
	
    public void onRoundEnded(RoundEndedEvent e){
       	double distance = counter.getDistance();
        total += distance;
	System.out.println("---------END OF ROUND---------");
       	System.out.println("Distance: " + distance + " pixels");
       	counter.kill();		
    }
	
    public void onDeath(DeathEvent e){
        System.out.println("--------I DIED--------");
        System.out.println("Walls hit: " +timesHitWall);
        System.out.println("Hits: " +timesHitOpponent);
        System.out.println("Misses: " +timesMissedOpponent);
        System.out.println("Hit by Enemy Bullets: " +timesHitByOpponent);
        System.out.println("Total distance: " +fim);
    }
	
    public void onBattleEnded(BattleEndedEvent e){
        System.out.println("--------END OF BATTLE---------");
        System.out.println("Total Distance: " +total);
    }

}