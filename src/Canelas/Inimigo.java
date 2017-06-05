/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Canelas;

import java.awt.geom.Point2D;
import java.io.Serializable;


public class Inimigo implements Serializable {

    public Inimigo() {
    }

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

    public double getBearing() {
        return bearing;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getChangehead() {
        return changehead;
    }

    public void setChangehead(double changehead) {
        this.changehead = changehead;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }
    
    public String toString() {
        return name;
    }
    
}
