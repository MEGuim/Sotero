/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Final;

import java.awt.geom.Point2D;


public class Inimigo {

    public Inimigo(double x, double y, double bearing, double heading, double speed) {
        this.bearing = bearing;
        this.heading = heading;
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    String name;
    public double bearing, heading, speed, x, y, distance, changehead;
    public long ctime; 		//game time that the scan was produced
    public boolean live; 	//is the enemy alive?
    public int bhit;
    
    
    public String getName() {
        return name;
    }
    
    public int getBhit() {
        return bhit;
    }

    public void bhit() {
        this.bhit += bhit;
    }

    public Point2D.Double guessPosition(long when) {
        double diff = when - ctime;
        double newY = y + Math.cos(heading) * speed * diff;
        double newX = x + Math.sin(heading) * speed * diff;

        return new Point2D.Double(newX, newY);
    }
}