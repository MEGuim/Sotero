package SistemasAutomatos;

import robocode.*;
import java.awt.Color;

/**
 *
 * @author meguim
 */
public class Conta extends AdvancedRobot {
    
    private DistanceThread counter;
    
    private int cont =0;
    public int timesHitWall = 0;
    public int timesHitOpponent = 0;
    public int timesMissedOpponent = 0;
    public int timesHitByOpponent = 0;
    public static double total = 0;
    
    @Override
    public void run() {
        this.counter = new DistanceThread(this);
        this.counter.start();
       
        setColors(Color.blue,Color.blue,Color.blue); // body,gun,radar
        setScanColor(Color.green);
        setAdjustRadarForRobotTurn(true);
        
        while (true) {
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
  
    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        double distance = counter.getDistance();
        total += distance;
        System.out.println("Distance: " + distance + " pixels");
        counter.kill();
    }
    
    public void onBattleEnded(BattleEndedEvent e){
        System.out.println("--------END OF BATTLE---------");
        System.out.println("Total Distance: " + total);
    }
    
}
