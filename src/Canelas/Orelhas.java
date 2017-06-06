package Canelas;

import static java.awt.Color.*;
import robocode.Droid;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;

/**
 *
 * @author CFCanelas
 */
public class Orelhas extends SuperDragao implements Droid {

    public boolean hasMaster = true;
    public int counter = 0;

    public void run() {
        setAllColors(GREEN);
        while (true) {
            if (counter > 10) {
                System.out.println("OPEN LOOOOOOOOP");
                hasMaster = false;
                antiGravMoveNoEnemies();
                setTurnGunLeftRadians(normaliseBearing(360));
                fire(firePower);
            } else {
                System.out.println("CLOSED LOOOOOOOP");
                counter++;
                antiGravMove();					//Move the bot
                selectTarget();
                if (hasTarget) {
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
}
