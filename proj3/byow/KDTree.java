package byow;

// @https://www.youtube.com/watch?v=irkJ4gczM0I
import java.util.List;

/**  KDTree class should be immutable.
 *  Also note that while k-d trees can theoretically work
 *  for any number of dimensions, your implementation only
 *  has to work for the 2-dimensional case,
 *  i.e. when our points have only x and y coordinates.
 *
 *  REMINDER: A K-D Tree(also called as K-Dimensional Tree) is a binary search tree
 *  where data in each node is a K-Dimensional point in space!!!
 *
 * omg om g
 * omg
 * ogm g
 *
 * */

public class KDTree implements PointSet {

    private Node root;

    // You can assume points has at least size 1.
    public KDTree(List<Point> points) {
        for (Point p: points) {
            insert(p);
        }
    }

    private class Node {
        Node left; //also down
        Node right; // also up
        Point point;

        Node(Point point) {
            this.point = point;
        }
    }

    @Override
    public Point nearest(double x, double y) {
        Point tmp = new Point(x, y);
        return nearest(tmp);
    }

    public Point nearest(Point point) {
        return nearestHelper(root, point, root.point, true);
    }

    private Point nearestHelper(Node n, Point point, Point bestCandidate, boolean even) {
        // If everything's good until the end of the tree-
        if (n == null) {
            return bestCandidate;
        }
        // If it's the same point overlapping
        if (n.point.equals(point)) {
            return point;
        }
        // If the current node's point is better, replace
        if (Point.distance(n.point, point) < Point.distance(bestCandidate, point)) {
            bestCandidate = n.point;
        }
        double kDSpaceLine = comparePoints(point, n, even);
        if (kDSpaceLine < 0) {
            bestCandidate = nearestHelper(n.left, point, bestCandidate, !even);

            if (Point.distance(bestCandidate, point)
                    >= kDSpaceLine * kDSpaceLine) {
                bestCandidate = nearestHelper(n.right, point, bestCandidate, !even);
            }
        } else {
            bestCandidate = nearestHelper(n.right, point, bestCandidate, !even);

            if (Point.distance(bestCandidate, point)
                    >= kDSpaceLine * kDSpaceLine) {
                bestCandidate = nearestHelper(n.left, point, bestCandidate, !even);
            }
        }
        return bestCandidate;
    }

    private double comparePoints(Point point, Node n, boolean even) {
        if (even) { // If right up
            return point.getX() - n.point.getX();
        } else { // If left down
            return point.getY() - n.point.getY();
        }
    }

    public void insert(Point point) {
        root = insertHelper(root, point, true);
    }

    private Node insertHelper(Node n, Point point, boolean even) {
        if (n == null) {
            return new Node(point);
        }
        double decider = comparePoints(point, n, even);
        // Gotta go Left Node
        if (decider < 0 && even) {
            n.left = insertHelper(n.left, point, !even);
        } else if (decider < 0 && !even) { // Bottom Nodes
            n.left = insertHelper(n.left, point, !even);
        } else if (decider > 0 && even) { // Gotta go  Right Nodes
            n.right = insertHelper(n.right, point, !even);
        } else if (decider > 0 && !even) {  // Gotta go Top Nodes
            n.right = insertHelper(n.right, point, !even);
        } else if (!n.point.equals(point)) {
            n.right = insertHelper(n.right, point, !even);
        }
        return n;
    }
}
