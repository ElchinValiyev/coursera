import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lines;

    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
        lines = new ArrayList<>();

        // has to copy, mutation of arguments is forbidden
        Point[] p = Arrays.copyOf(points, points.length);
        Arrays.sort(p); // for natural order of result

        // Searching for duplicates
        for (int i = 1; i < p.length; i++) {
            if (p[i - 1].compareTo(p[i]) == 0)
                throw new IllegalArgumentException("Duplicate detected");
        }

        for (int i = 0; i < p.length - 3; i++) {

            for (int j = i + 1; j < p.length - 2; j++) {
                double slope1 = p[i].slopeTo(p[j]); // computing slope i -> j

                for (int k = j + 1; k < p.length - 1; k++) {
                    double slope2 = p[j].slopeTo(p[k]); // computing slope j -> k

                    for (int l = k + 1; l < p.length; l++) {
                        double slope3 = p[k].slopeTo(p[l]); // computing slope k -> l

                        if (Double.compare(slope1, slope2) == 0 && Double.compare(slope2, slope3) == 0)
                            lines.add(new LineSegment(p[i], p[l]));
                    }
                }
            }
        }
    }

    public int numberOfSegments() { // the number of line segments
        return lines.size();
    }

    public LineSegment[] segments() { // the line segments
        return lines.toArray(new LineSegment[lines.size()]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}