package FCP;

import java.awt.Point;
import java.util.Comparator;

public class PointCompare implements Comparator<Point> {
    public int compare(Point a, Point b) {
        if (a.getX() < b.getX()) {
            return -1;
        }
        else if (a.getX() > b.getX()) {
            return 1;
        }
        else {
            return 0;
        }
    }
}
