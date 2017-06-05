/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Final;


import robocode.Droid;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;

/**
 *
 * @author meguim
 */
public class Orelhas extends AntiGravityBot implements Droid {
    
    
    public boolean hasMaster = true;
    public int counter = 0;
    public boolean hasTarget = false;
    
    public void run () {        
        while (true) {
            while(true) {
                if (counter > 10) {
                    hasMaster = false;
                    antiGravMoveNoEnemies();
                    if ( hasTarget ) {
                        doGun();
                        out.println(target.distance);
                        fire(firePower);
                    }
                    execute();
                }
                else {
                    counter++;
                    antiGravMove();					//Move the bot
                    selectTarget();
                    if ( hasTarget ) {
                        doGun();
                        out.println(target.distance);	//move the gun to predict where the enemy will be
                        fire(firePower);
                    }
                    execute();
                }
            }
        }
    }    
    
    public void onMessageReceived(MessageEvent e) {
        Inimigo x = (Inimigo) e.getMessage();
        hasMaster = true;
        counter = 0;
        
        inimigos.put(x.getName(),x);
    }
    
    public void onRobotDeath(RobotDeathEvent e) {

        if (!isTeammate(e.getName())) {
            inimigos.remove(e.getName());
        }

    }
}
