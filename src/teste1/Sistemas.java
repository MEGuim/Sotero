package teste1;
import robocode.*;
import java.awt.Color;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Sistemas - a robot by (your name here)
 */
public class Sistemas extends AdvancedRobot{


    /**
     * run: Teste1's default behavior
     */
    public int cont =0;
    public double xAnt;
    public double yAnt;
    public double dist = 0;
    public static double fim = 0;
    public int timesHitWall = 0;
    public int timesHitOpponent = 0;
    public int timesMissedOpponent = 0;
    public int timesHitByOpponent = 0;
    
    public void run() {
      // Initialization of the robot should be put here
        xAnt = getX();
        yAnt = getY();
        
        
        addCustomEvent(new Condition("TurnStartedEvent", 99){
            public boolean test() {
                return true;
            }
        });
        
        setColors(Color.blue,Color.blue,Color.blue); // body,gun,radar
        setScanColor(Color.green);
        setAdjustRadarForRobotTurn(true);

        // Robot main loop
        while(true) {
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunLeft(360);
            turnRadarRight(360);             
            scan();
        }
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
    
    public void onCustomEvent(CustomEvent e) {
        if (e.getCondition().getName().equals("TurnStartedEvent")) {
            double delta = sqrt(pow(getX()-xAnt,2) + pow(getY()-yAnt,2));
            dist += delta;
            xAnt = getX();
            yAnt = getY();
            fim += delta;
        
        }
    }
	
    public void onRoundEnded(RoundEndedEvent e){
        System.out.println("---------END OF ROUND---------");
        System.out.println("Distance: " +dist);
        System.out.println("Walls hit: " +timesHitWall);
        System.out.println("Hits: " +timesHitOpponent);
        System.out.println("Misses: " +timesMissedOpponent);
        System.out.println("Hit by Enemy Bullets: " +timesHitByOpponent);
        System.out.println("Total distance: " +fim);		
    }
	
    public void onDeath(DeathEvent e){
        System.out.println("--------I DIED--------");
        System.out.println("Distance: " +dist);
        System.out.println("Walls hit: " +timesHitWall);
        System.out.println("Hits: " +timesHitOpponent);
        System.out.println("Misses: " +timesMissedOpponent);
        System.out.println("Hit by Enemy Bullets: " +timesHitByOpponent);
        System.out.println("Total distance: " +fim);
    }
	
    public void onBattleEnded(BattleEndedEvent e){
        System.out.println("--------END OF BATTLE---------");
        System.out.println("Total Distance: " +fim);
    }

}