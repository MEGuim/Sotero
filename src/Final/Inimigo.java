/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Final;


/**
 *
 * @author goncalo
 */
public class Inimigo{
    
    private double x;
    private double y;
    private double bearing;
    private double heading;
    private double energy;

    public Inimigo(double x, double y, double bearing, double heading, double energy) {
        this.x = x;
        this.y = y;
        this.bearing = bearing;
        this.heading = heading;
        this.energy = energy;
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

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }    
}
