import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private static final int MIN_SEGMENT_LENGTH = 3;
    private ArrayList<LineSegment> lines;

    public FastCollinearPoints(Point[] points) { // finds all line segments containing 4 or more points
        lines = new ArrayList<>();
        Point[] orderedPoints = Arrays.copyOf(points, points.length);


        // Searching for duplicates
        Arrays.sort(orderedPoints);
        for (int i = 1; i < orderedPoints.length; i++)
            if (orderedPoints[i - 1].compareTo(orderedPoints[i]) == 0)
                throw new IllegalArgumentException("Duplicate detected");

        // if fewer than 4 points, no lines can be detected
        if (points.length <= MIN_SEGMENT_LENGTH) return;


        for (Point point : points) {
            Comparator<Point> order = point.slopeOrder();
            Arrays.sort(orderedPoints, order);

            int first = 1; // index of segment start
            int segmentLength = 1; // initial segment length
            double currentSlope = point.slopeTo(orderedPoints[1]);

            for (int i = 2; i < orderedPoints.length; i++) {

                // When the same, increment the count because points are collinearF
                if (Double.compare(point.slopeTo(orderedPoints[i]), currentSlope) == 0) {
                    segmentLength++;
                    if (i == orderedPoints.length - 1) // if end of array reached
                        addToLinesIfSatisfies(point, orderedPoints, first, segmentLength);
                }
                // Otherwise, reset the count and set the slope to compare
                else {
                    addToLinesIfSatisfies(point, orderedPoints, first, segmentLength);
                    currentSlope = point.slopeTo(orderedPoints[i]);
                    first = i;
                    segmentLength = 1;
                }
            }
        }
    }

    /**
     * Checks  if point is lower than any point on a segment (this helps
     * to eliminate duplicate lines, since there is only one line,
     * where point is minimal), if segment is long enough  and saves
     */
    private void addToLinesIfSatisfies(Point point, Point[] points, int first, int segmentLength) {
        if (segmentLength >= MIN_SEGMENT_LENGTH && isMin(point, points, first, first + segmentLength - 1)) {
            Arrays.sort(points, first, first + segmentLength);
            lines.add(new LineSegment(point, points[first + segmentLength - 1]));
        }
    }

    /**
     * Checks if point is minimal on given interval
     */
    private boolean isMin(Point p, Point[] array, int start, int end) {
        for (int i = start; i <= end; i++)
            if (p.compareTo(array[i]) > 0) return false;
        return true;
    }

    public int numberOfSegments() { // the number of line segments
        return lines.size();
    }

    public LineSegment[] segments() { // the line segments
        return lines.toArray(new LineSegment[lines.size()]);
    }

    public static void main(String[] args) {

        Point[] p = new Point[10];
        p[0] = new Point(4000, 30000);
        p[1] = new Point(3500, 28000);
        p[2] = new Point(3000, 26000);
        p[3] = new Point(2000, 22000);
        p[4] = new Point(1000, 18000);
        p[5] = new Point(13000, 21000);
        p[6] = new Point(23000, 16000);
        p[7] = new Point(28000, 13500);
        p[8] = new Point(28000, 5000);
        p[9] = new Point(28000, 1000);


        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32000);
        StdDraw.setYscale(0, 32000);
        for (Point pe : p) {
            pe.draw();
        }
        StdDraw.show();


        FastCollinearPoints collinear = new FastCollinearPoints(p);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}