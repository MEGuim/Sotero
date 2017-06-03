/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Final;

import java.util.Enumeration;
import robocode.Droid;
import robocode.MessageEvent;

/**
 *
 * @author meguim
 */
public class Orelhas extends AntiGravityBot implements Droid {
    
    
    public boolean hasMaster = true;
    public int counter = 0;
    
    public void run () {        
        while (true) {
            while(true) {
                if (counter > 10) {
                    hasMaster = false;
                    antiGravMoveNoEnemies();
                    doFirePower();
                    
                    out.println(target.distance);
                    fire(firePower);
                }
                else {
                    counter++;
                    antiGravMove();					//Move the bot
                    doFirePower();					//select the fire power to use
                    doGun();
                    out.println(target.distance);	//move the gun to predict where the enemy will be
                    fire(firePower);
                    execute();						//execute all commands
                }
            }
        }
    }    
    
    public void onMessageReceived(MessageEvent e) {
        target = (Inimigo) e.getMessage();
        hasMaster = true;
        counter = 0;
        
        
    }
}
