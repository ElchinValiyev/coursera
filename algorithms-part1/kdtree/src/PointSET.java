import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> points;

    public PointSET() { // construct an empty set of points
        points = new TreeSet<>();
    }

    public boolean isEmpty() { // is the set empty?
        return points.isEmpty();
    }

    public int size() { // number of points in the set
        return points.size();
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (!points.contains(p))
            points.add(p);
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        return points.contains(p);
    }

    public void draw() { // draw all points to standard draw
        points.forEach(Point2D::draw);
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle
        Stack<Point2D> filteredPoints = new Stack<>();
        for (Point2D p : points)
            if (rect.contains(p)) filteredPoints.push(p);
        return filteredPoints;
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        Point2D closest = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Point2D point : points)
            if (point.distanceTo(p) < minDistance) {
                minDistance = point.distanceTo(p);
                closest = point;
            }
        return closest;
    }

    public static void main(String[] args) { // unit testing of the methods (optional)
    }
}
