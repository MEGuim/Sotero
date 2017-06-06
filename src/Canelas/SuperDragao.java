package Canelas;

import robocode.*;
import java.awt.geom.*;
import java.util.*;

/**
 *
 * @author CFCanelas
 */
public class SuperDragao extends TeamRobot {

    Inimigo target;					//our current enemy
    public Map<String, Inimigo> inimigos = new HashMap<String, Inimigo>();
    boolean hasTarget = false;

    double firePower;				//the power of the shot we will be using
    final double PI = Math.PI;		//just a constant
    int direction = 1;				//direction we are heading... 1 = forward, -1 = backwards
    double midpointstrength = 0;	//The strength of the gravity point in the middle of the field
    int midpointcount = 0;			//Number of turns since that strength was changed

    void antiGravMove() {
        double xforce = 0;
        double yforce = 0;
        double force;
        double ang;
        GravPoint p;
        
        for (Inimigo inimigo : inimigos.values()) {
            if (inimigo.live) {
                p = new GravPoint(inimigo.x, inimigo.y, -1000);
                force = p.power / Math.pow(getRange(getX(), getY(), p.x, p.y), 2);
                //Find the bearing from the point to us
                ang = normaliseBearing(Math.PI / 2 - Math.atan2(getY() - p.y, getX() - p.x));
                //Add the components of this force to the total force in their respective directions
                xforce += Math.sin(ang) * force;
                yforce += Math.cos(ang) * force;
            }
        }

        midpointcount++;
        if (midpointcount > 5) {
            midpointcount = 0;
            midpointstrength = (Math.random() * 2000) - 1000;
        }
        p = new GravPoint(getBattleFieldWidth() / 2, getBattleFieldHeight() / 2, midpointstrength);
        force = p.power / Math.pow(getRange(getX(), getY(), p.x, p.y), 1.5);
        ang = normaliseBearing(Math.PI / 2 - Math.atan2(getY() - p.y, getX() - p.x));
        xforce += Math.sin(ang) * force;
        yforce += Math.cos(ang) * force;

        xforce += 5000 / Math.pow(getRange(getX(), getY(), getBattleFieldWidth(), getY()), 3);
        xforce -= 5000 / Math.pow(getRange(getX(), getY(), 0, getY()), 3);
        yforce += 5000 / Math.pow(getRange(getX(), getY(), getX(), getBattleFieldHeight()), 3);
        yforce -= 5000 / Math.pow(getRange(getX(), getY(), getX(), 0), 3);

        //Move in the direction of our resolved force.
        goTo(getX() - xforce, getY() - yforce);
    }

    void antiGravMoveNoEnemies() {
        double xforce = 0;
        double yforce = 0;
        double force;
        double ang;
        GravPoint p;

        midpointcount++;
        if (midpointcount > 5) {
            midpointcount = 0;
            midpointstrength = (Math.random() * 2000) - 1000;
        }

        p = new GravPoint(getBattleFieldWidth() / 2, getBattleFieldHeight() / 2, midpointstrength);
        force = p.power / Math.pow(getRange(getX(), getY(), p.x, p.y), 1.5);
        ang = normaliseBearing(Math.PI / 2 - Math.atan2(getY() - p.y, getX() - p.x));
        xforce += Math.sin(ang) * force;
        yforce += Math.cos(ang) * force;

        xforce += 5000 / Math.pow(getRange(getX(), getY(), getBattleFieldWidth(), getY()), 3);
        xforce -= 5000 / Math.pow(getRange(getX(), getY(), 0, getY()), 3);
        yforce += 5000 / Math.pow(getRange(getX(), getY(), getX(), getBattleFieldHeight()), 3);
        yforce -= 5000 / Math.pow(getRange(getX(), getY(), getX(), 0), 3);

        //Move in the direction of our resolved force.
        goTo(getX() - xforce, getY() - yforce);
    }

    /**
     * Move towards an x and y coordinate*
     */
    void goTo(double x, double y) {
        double dist = 20;
        double angle = Math.toDegrees(absbearing(getX(), getY(), x, y));
        double r = turnTo(angle);
        setAhead(dist * r);
    }

    /**
     * Turns the shortest angle possible to come to a heading, then returns the
     * direction the the bot needs to move in.*
     */
    int turnTo(double angle) {
        double ang;
        int dir;
        ang = normaliseBearing(getHeading() - angle);
        if (ang > 90) {
            ang -= 180;
            dir = -1;
        } else if (ang < -90) {
            ang += 180;
            dir = -1;
        } else {
            dir = 1;
        }
        setTurnLeft(ang);
        return dir;
    }

    //if a bearing is not within the -pi to pi range, alters it to provide the shortest angle
    double normaliseBearing(double ang) {
        if (ang > PI) {
            ang -= 2 * PI;
        }
        if (ang < -PI) {
            ang += 2 * PI;
        }
        return ang;
    }

    //if a heading is not within the 0 to 2pi range, alters it to provide the shortest angle
    double normaliseHeading(double ang) {
        if (ang > 2 * PI) {
            ang -= 2 * PI;
        }
        if (ang < 0) {
            ang += 2 * PI;
        }
        return ang;
    }

    //returns the distance between two x,y coordinates
    public double getRange(double x1, double y1, double x2, double y2) {
        double xo = x2 - x1;
        double yo = y2 - y1;
        double h = Math.sqrt(xo * xo + yo * yo);
        return h;
    }

    //gets the absolute bearing between to x,y coordinates
    public double absbearing(double x1, double y1, double x2, double y2) {
        double xo = x2 - x1;
        double yo = y2 - y1;
        double h = getRange(x1, y1, x2, y2);
        if (xo > 0 && yo > 0) {
            return Math.asin(xo / h);
        }
        if (xo > 0 && yo < 0) {
            return Math.PI - Math.asin(xo / h);
        }
        if (xo < 0 && yo < 0) {
            return Math.PI + Math.asin(-xo / h);
        }
        if (xo < 0 && yo > 0) {
            return 2.0 * Math.PI - Math.asin(-xo / h);
        }
        return 0;
    }

    void doGun() {
        firePower = 400 / target.distance; //selects a bullet power based on our distance away from the target
        if (firePower > 3) {
            firePower = 3;
        }

        long time = getTime() + (int) Math.round((getRange(getX(), getY(), target.x, target.y) / (20 - (3 * firePower))));
        Point2D.Double p = target.guessPosition(time);

        //offsets the gun by the angle to the next shot based on linear targeting provided by the enemy class
        double gunOffset = getGunHeadingRadians() - (Math.PI / 2 - Math.atan2(p.y - getY(), p.x - getX()));
        setTurnGunLeftRadians(normaliseBearing(gunOffset));
    }

    public void selectTarget() {
        double distance = 999999999;
        double currY = getY();
        double currX = getX();
        for (Inimigo inimigo : inimigos.values()) {
            double aux = getRange(currX, currY, inimigo.x, inimigo.y);
            if (aux < distance) {
                System.out.println(inimigo);
                distance = aux;
                target = inimigo;
                System.out.println("Joelhada neste arbitro: " + target);
                hasTarget = true;
            }
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        if (inimigos.containsKey(event.getName())) {
            back(50);
            ahead(100);
        } else {
            back(50);
            turnRight(90);
            ahead(100);
        }
    }

    public void onHitWall(HitWallEvent event) {
        double angle = Math.toDegrees(absbearing(getX(), getY(), getBattleFieldHeight(), getBattleFieldWidth()));
        turnTo(angle);
        ahead(5);
    }
    
    public void onRobotDeath(RobotDeathEvent e) {
        if (!isTeammate(e.getName())) {
            inimigos.remove(e.getName());
        }
    }
}
