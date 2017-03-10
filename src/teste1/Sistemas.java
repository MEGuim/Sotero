package sistemasintelige;
import robocode.*;
import java.awt.Color;
import robocode.util.Utils;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Sistemas - a robot by (your name here)
 */
public class Sistemas extends AdvancedRobot{


    /**
     * run: Teste1's default behavior
     */
	private DistanceThread counter;
	 
    public int cont =0;
    public double xAnt;
    public double yAnt;
    public double dist = 0;
	public double distance = 0;
	public static double total = 0;
    public static double fim = 0;
    public int timesHitWall = 0;
    public int timesHitOpponent = 0;
    public int timesMissedOpponent = 0;
    public int timesHitByOpponent = 0;
    public double walk;
    
    public void run() {
      // Initialization of the robot should be put here
	 	this.counter = new DistanceThread(this);
	   
      
        
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
        
        // Robot main loop
        while(true) {             
            scan();
        }
    }
    
    
    /**
     * moveTo: Moves Robot to position of coordinates (destX, destY)
     */
    private void moveTo(double destX, double destY) {
        double centerAngle = Math.atan2(destX-getX(), destY-getY());
        turnRightRadians(Utils.normalRelativeAngle(centerAngle - getHeadingRadians()));
        double deltaX = destX - getX();
        double deltaY = destY - getY();
        ahead(Math.hypot(deltaX, deltaY));
    }

    /**
     * onScannedRobot: What to do when you see another robot
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        if (getEnergy()>50) fire (3);
        else fire(0.5);
    }

    
    public void onBulletHit(BulletHitEvent e){
        turnGunRight(720);
        timesHitOpponent++;
        }

    public void onHitByBullet(HitByBulletEvent e) {

        if (cont == 0){
        setColors(Color.red,Color.red,Color.red);
        cont ++;
        }
        else if (cont == 1){
        setColors(Color.green,Color.green,Color.green);
        cont++;
        }
        else if (cont == 2) {
            setColors(Color.white,Color.white,Color.white);
            cont++;
        }
        else if (cont == 3){
            setColors(Color.yellow,Color.yellow,Color.yellow);
            cont++;
        }
        else if (cont == 4){
        setColors(Color.pink,Color.pink,Color.pink);
        cont =0;
        }
        
        timesHitByOpponent++;
    }
    
    /**
     * onHitWall: What to do when you hit a wall
     */
    public void onHitWall(HitWallEvent e) {
        back(200);
        timesHitWall++;
    }    
    
    public void onBulletHit(BulletMissedEvent e) {
        timesMissedOpponent++;
    }
    
	
    public void onRoundEnded(RoundEndedEvent e){
       	double distance = counter.getDistance();
		total += distance;
		System.out.println("---------END OF ROUND---------");
        System.out.println("Distance: " +dist);
        System.out.println("Walls hit: " +timesHitWall);
        System.out.println("Hits: " +timesHitOpponent);
        System.out.println("Misses: " +timesMissedOpponent);
        System.out.println("Hit by Enemy Bullets: " +timesHitByOpponent);
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