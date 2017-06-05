/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Canelas;

import robocode.Droid;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;

/**
 *
 * @author meguim
 */
public class Orelhas extends SuperDragao implements Droid {
    
    public boolean hasMaster = true;
    public int counter = 0;
    
    public void run () {        
        while (true) {
            if (counter > 10) {
                System.out.println("OPEN LOOOOOOOOP");
                hasMaster = false;
                antiGravMoveNoEnemies();
                setTurnGunLeftRadians(normaliseBearing(360));
                out.println(target.distance);
                fire(firePower);
            }
            else {
                System.out.println("CLOSED LOOOOOOOP");
                counter++;
                antiGravMove();					//Move the bot
                selectTarget();
                if ( hasTarget ) {
                    System.out.println("Traget " + target.name);
                    doGun();
                    out.println(target.distance);	//move the gun to predict where the enemy will be
                    fire(firePower);
                }
            }
            execute();
        }
    }    
    
    public void onMessageReceived(MessageEvent e) {
        counter = 0;
        hasMaster = true;
        Arbitros arbitros = (Arbitros) e.getMessage();  
        for (Inimigo inimigo : arbitros.inimigos.values()) {
            inimigos.put(inimigo.name, inimigo);
        }
    }
    
    public void onRobotDeath(RobotDeathEvent e) {

        if (!isTeammate(e.getName())) {
            inimigos.remove(e.getName());
        }

    }
}
