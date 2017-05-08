/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package V2;

import robocode.*;
import java.awt.geom.Point2D;

/**
 * @author iiley Haiku Anti-Gravitation bot
 *
 * -Special thanks-*
 *
 * I would like to thank - DrLoco,Vuen, David Alves and Michael Dorgan for ideas
 * on tiny anti-gravity code that fits in haiku bots;) - and to all other
 * robocoders in this great community
 */
public class Escape extends AdvancedRobot {

    Point2D.Double gravitation = new Point2D.Double(0, 0),
            closest = new Point2D.Double(Double.POSITIVE_INFINITY, 0),
            lastFireTime = new Point2D.Double(0, 0);

    public void run() {
        while (true) {
            setAhead(180 * 12 / Math.abs(lastFireTime.y = Math.toDegrees(robocode.util.Utils.normalRelativeAngle(Math.atan2(gravitation.x + 1 / (getX() - 2d) - 1 / (getBattleFieldWidth() - getX() - 2d), gravitation.y + 1 / (getY() - 2d) - 1 / (getBattleFieldHeight() - getY() - 2d)) - getHeadingRadians()))));
            turnRight(Math.abs(lastFireTime.y) < 10d
                    ? lastFireTime.y
                    : (lastFireTime.y > 0d ? 10d : -10d));
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if (((gravitation.x = gravitation.x * 0.8 - (1 / e.getDistance() * Math.sin(e.getBearingRadians() + getHeadingRadians()))) + (gravitation.y = gravitation.y * 0.8 - (1 / e.getDistance() * Math.cos(e.getBearingRadians() + getHeadingRadians()))) != 0d || true)
                && e.getDistance() < closest.x + 180 && (closest.x = e.getDistance()) > 0 || getTime() - lastFireTime.x > 36) {
            if (((getEnergy() > 0.11d || e.getEnergy() == 0d)
                    && setFireBullet(Math.min(900d / e.getDistance(), Math.min(getEnergy() / 5d, Math.min(3d, e.getEnergy() / 6d)))) != null
                    && (lastFireTime.x = getTime()) > 0)
                    && (closest.x = Double.POSITIVE_INFINITY) > 0 || true) {
                setTurnGunRight(Double.POSITIVE_INFINITY);
            }
        }
    }
}
