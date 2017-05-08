package FCP;

import java.awt.geom.Point2D;
import java.util.ArrayList;


public class Message {
    private final ArrayList<Point2D> obstacles = new ArrayList<>();
    private final ArrayList<Point2D> enemies = new ArrayList<>();
    private final ArrayList<Point2D> teammates = new ArrayList<>();
    private final Point2D.Double target = new Point2D.Double();
    private double targetHP;
    
    public void addObstaclePosition(Point2D p) {
        if (!obstacles.contains(p)) {
            obstacles.add(p);
        }
    }
    
    public void addEnemyPosition(Point2D p) {
        if (!enemies.contains(p)) {
            enemies.add(p);
        }
    }
    
    public void addTeammatesPosition(Point2D p) {
        if (!teammates.contains(p)) {
            teammates.add(p);
        }
    }
    
    public ArrayList<Point2D> getObstaclesPositions(){
        return obstacles;
    }
    
    public ArrayList<Point2D> getEnemiesPositions(){
        return enemies;
    }
    
    public ArrayList<Point2D> getTeammatesPositions(){
        return teammates;
    }
    
    public void setTarget(Point2D p){
        target.setLocation(p);
    }
    
    public Point2D getTarget(){
        return target;
    }
    
    public void reset(){
        enemies.clear();
        teammates.clear();
        obstacles.clear();
    }
    
    public void replace(Message message){
        reset();
        enemies.addAll(message.getEnemiesPositions());
        teammates.addAll(message.getTeammatesPositions());
        obstacles.addAll(message.getObstaclesPositions());
        target.setLocation(message.getTarget());
    }
}
