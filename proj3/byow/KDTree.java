package byow;
import java.util.List;

public class KDTree implements RoomSet {

    private Node root;

    // You can assume rooms has at least size 1.
    public KDTree(List<Room> rooms) {
        for (Room room: rooms) {
            insert(room);
        }
    }

    private class Node {
        Node left; //also down
        Node right; // also up
        Room room;

        Node(Room room) {
            this.room = room;
        }
    }

    public Room nearest(int x, int y, int w, int h) {
        Room tmp = new Room(x, y, w, h );
        return nearest(tmp);
    }

    public Room nearest(Room room) {
        return nearestHelper(root, room, root.room, true);
    }

    private Room nearestHelper(Node n, Room room, Room bestCandidate, boolean even) {
        // If everything's good until the end of the tree-
        if (n == null) {
            return bestCandidate;
        }
        // If it's the same room overlapping
        if (n.room.equals(room)) {
            return room;
        }
        // If the current node's room is better, replace
        if (room.distance(n.room, room) < room.distance(bestCandidate, room)) {
            bestCandidate = n.room;
        }
        double kDSpaceLine = comparerooms(room, n, even);
        if (kDSpaceLine < 0) {
            bestCandidate = nearestHelper(n.left, room, bestCandidate, !even);

            if (room.distance(bestCandidate, room)
                    >= kDSpaceLine * kDSpaceLine) {
                bestCandidate = nearestHelper(n.right, room, bestCandidate, !even);
            }
        } else {
            bestCandidate = nearestHelper(n.right, room, bestCandidate, !even);

            if (room.distance(bestCandidate, room)
                    >= kDSpaceLine * kDSpaceLine) {
                bestCandidate = nearestHelper(n.left, room, bestCandidate, !even);
            }
        }
        return bestCandidate;
    }

    private double comparerooms(Room room, Node n, boolean even) {
        if (even) { // If right up
            return room.getX() - n.room.getX();
        } else { // If left down
            return room.getY() - n.room.getY();
        }
    }

    public void insert(Room room) {
        root = insertHelper(root, room, true);
    }

    private Node insertHelper(Node n, Room room, boolean even) {
        if (n == null) {
            return new Node(room);
        }
        double decider = comparerooms(room, n, even);
        // Gotta go Left Node
        if (decider < 0 && even) {
            n.left = insertHelper(n.left, room, !even);
        } else if (decider < 0 && !even) { // Bottom Nodes
            n.left = insertHelper(n.left, room, !even);
        } else if (decider > 0 && even) { // Gotta go  Right Nodes
            n.right = insertHelper(n.right, room, !even);
        } else if (decider > 0 && !even) {  // Gotta go Top Nodes
            n.right = insertHelper(n.right, room, !even);
        } else if (!n.room.equals(room)) {
            n.right = insertHelper(n.right, room, !even);
        }
        return n;
    }
}
