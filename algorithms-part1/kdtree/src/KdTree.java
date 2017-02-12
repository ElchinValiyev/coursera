import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;


public class KdTree {
    private Node root;
    private int size;
    private Comparator<Point2D> yOrder = Point2D.Y_ORDER;
    private Comparator<Point2D> xOrder = Point2D.X_ORDER;

    public KdTree() { // construct an empty set of points
    }

    public boolean isEmpty() { // is the set empty?
        return root == null;
    }

    public int size() { // number of points in the set
        return size;
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        root = put(root, p, true);
    }

    private Node put(Node node, Point2D value, boolean isVerticalSplit) {
        if (node == null) {
            size++;
            return new Node(value, isVerticalSplit);
        }
        if (node.point.equals(value)) return node;

        int cmp = node.isVerticalSplit ? xOrder.compare(value, node.point) :
                yOrder.compare(value, node.point);
        if (cmp < 0)
            node.left = put(node.left, value, !isVerticalSplit);
        else
            node.right = put(node.right, value, !isVerticalSplit);
        return node;
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        Node current = root;
        while (current != null) {
            if (p.equals(current.point)) return true;

            int cmp = current.isVerticalSplit ? xOrder.compare(p, current.point) :
                    yOrder.compare(p, current.point);
            if (cmp < 0) current = current.left;
            else current = current.right;
        }
        return false;
    }

    public void draw() { // draw all points to standard draw

    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle
        Stack<Point2D> points = new Stack<>();
        pointsInRange(points, rect, root);
        return points;
    }

    private void pointsInRange(Stack<Point2D> points, RectHV rect, Node node) {
        if (node == null) return;

        Point2D point = node.point;

        if (node.isVerticalSplit) {
            // if split line passes on the right, all points are on the left
            if (point.x() > rect.xmax())
                pointsInRange(points, rect, node.left);
                // if split line passes on the left, all points are on the right
            else if (point.x() < rect.xmin())
                pointsInRange(points, rect, node.right);
            else { // split line crosses the rectangle
                if (rect.contains(point))
                    points.push(point);
                pointsInRange(points, rect, node.left);
                pointsInRange(points, rect, node.right);
            }
        } else {
            // if split line passes above, all points are in the left subtree
            if (point.y() > rect.ymax())
                pointsInRange(points, rect, node.left);
                // if split line passes below, all points are in the right subtree
            else if (point.y() < rect.ymin())
                pointsInRange(points, rect, node.right);
            else { // split line crosses the rectangle
                if (rect.contains(point))
                    points.push(point);
                pointsInRange(points, rect, node.left);
                pointsInRange(points, rect, node.right);
            }
        }
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (isEmpty())
            return null;
        else {
            RectHV unitSquare = new RectHV(0.0, 0.0, 1.0, 1.0);
            return doNearest(root, unitSquare, p, null);
        }
    }


    private Point2D doNearest(Node node, RectHV nodeRect, Point2D queryPoint, Point2D nearestPoint) {
        // To find a closest point to a given query point, start at the root and recursively search in both subtrees
        // using the following pruning rule:
        // if the closest point discovered so far is closer than the distance between the query point and the rectangle
        // corresponding to a node, there is no need to explore that node (or its subtrees).
        // That is, a node is searched only if it might contain a point that is closer than the best one found so far.
        // The effectiveness of the pruning rule depends on quickly finding a nearby point.
        // To do this, organize your recursive method so that when there are two possible subtrees to go down,
        // you always choose the subtree that is on the same side of the splitting line as the query point as the first
        // subtree to explore — the closest point found while exploring the first subtree may enable pruning of the
        // second subtree.
        if (node == null) {
            return nearestPoint;
        }

        Point2D nearestPointCandidate = nearestPoint;
        double nearestDist = (nearestPointCandidate != null)
                ? queryPoint.distanceSquaredTo(nearestPointCandidate)
                : Double.MAX_VALUE;

        if (nearestDist > nodeRect.distanceSquaredTo(queryPoint)) {
            double dist = queryPoint.distanceSquaredTo(node.point);
            if (dist < nearestDist) {
                nearestPointCandidate = node.point;
            }

            RectHV leftNodeRect = leftNodeRect(node, nodeRect);
            RectHV rightNodeRect = rightNodeRect(node, nodeRect);

            if (isSmallerThanPointInNode(queryPoint, node)) {
                // explore left subtree first
                nearestPointCandidate = doNearest(node.left, leftNodeRect, queryPoint, nearestPointCandidate);
                nearestPointCandidate = doNearest(node.right, rightNodeRect, queryPoint, nearestPointCandidate);
            } else {
                // explore right subtree first
                nearestPointCandidate = doNearest(node.right, rightNodeRect, queryPoint, nearestPointCandidate);
                nearestPointCandidate = doNearest(node.left, leftNodeRect, queryPoint, nearestPointCandidate);
            }
        }

        return nearestPointCandidate;
    }

    private RectHV leftNodeRect(Node node, RectHV nodeRect) {
        return node.isVerticalSplit
                ? new RectHV(nodeRect.xmin(), nodeRect.ymin(), node.point.x(), nodeRect.ymax())
                : new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), node.point.y());
    }

    private RectHV rightNodeRect(Node node, RectHV nodeRect) {
        return node.isVerticalSplit
                ? new RectHV(node.point.x(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax())
                : new RectHV(nodeRect.xmin(), node.point.y(), nodeRect.xmax(), nodeRect.ymax());
    }

    private boolean isSmallerThanPointInNode(Point2D p, Node node) {
        int cmp = node.isVerticalSplit ? xOrder.compare(p, node.point) : yOrder.compare(p, node.point);
        return (cmp < 0);
    }

    public static void main(String[] args) { // unit testing of the methods (optional)
        KdTree tree = new KdTree();
        tree.insert(new Point2D(1, 1));
        tree.insert(new Point2D(1, 3));
        tree.insert(new Point2D(2, 8));
        tree.insert(new Point2D(3, 1));
        tree.insert(new Point2D(3, 6));
        tree.insert(new Point2D(5, 3));
        tree.insert(new Point2D(5, 7));
        tree.insert(new Point2D(7, 1));
        tree.insert(new Point2D(7, 7));

        StdOut.println("Find existing point: " + tree.contains(new Point2D(3, 6)));
        StdOut.println("Find non-existant p: " + tree.contains(new Point2D(0.2, 0.6)));
        StdOut.println("Count of nodes = 9 : " + tree.size());
        tree.insert(new Point2D(7, 7));

        StdOut.println("Cannot insert same : " + tree.size());
    }

    private class Node {
        private Point2D point;
        private boolean isVerticalSplit;
        private Node left;
        private Node right;

        Node(Point2D p, boolean isVerticalSplit) {
            this.isVerticalSplit = isVerticalSplit;
            this.point = p;
        }
    }
}
