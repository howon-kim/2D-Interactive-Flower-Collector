package byow;
import byow.TileEngine.TETile;
import java.util.Random;
import byow.TileEngine.Tileset;
import java.util.ArrayList;

public class Room {

    public static final int maxSize = 10;
    public static final int minSize = 2;
    public static KDTree BSPTree;
    public static TETile[][] world;

    public int x1;
    public int x2;
    public int y1;
    public int y2;

    public int w;
    public int h;

    public Room(int x, int y, int w, int h) {
        this.x1 = x;
        this.x2 = x + w;
        this.y1 = y;
        this.y2 = y + h;

        this.w = w;
        this.h = h;
    }

    public int getX() { return this.x1; }
    public int getY() { return this.y1; }


    public static void generateRoom(Location loc) {
        // populates list of valid (not intersecting rooms) and adds room to TETile[][] world
        ArrayList<Room> rooms = new ArrayList<>();

        // generates random room dimensions
        // below values have to be within loc.x1, x2, y1, y2

        Random rand = new Random(123);

        int w = rand.nextInt(maxSize - minSize + 1) + minSize;
        int h = rand.nextInt(maxSize - minSize + 1) + minSize;
        int x = rand.nextInt(loc.x2- loc.x1) + loc.x1;
        int y = rand.nextInt(loc.y2 - loc.y1) + loc.y1;


        Room newRoom = new Room(x, y, w, h);

        // redundant if we're doing BSP
        boolean failed = false;
        for (Room r: rooms) {
            if (newRoom.overlaps(r)) {
                failed = true;
            }
        }

        BSPTree.insert(newRoom);

        if (!failed) {
            rooms.add(newRoom);
            putRoom(newRoom, world);
        }
    }

    public static void putRoom(Room room, TETile[][] world) {
        int w = room.w;
        int h = room.h;

        for (int x = 0; x < w; x += 1) {
            world[room.x1 + x][room.y1] = Tileset.WALL;
            world[room.x1 + x][room.y1 + h - 1] = Tileset.WALL;
        }
        for (int y = 0; y < h; y += 1) {
            world[room.x1][room.y1 + y] = Tileset.WALL;
            world[room.x1 + w - 1][room.y1 + y] = Tileset.WALL;
        }
        for (int x = 1; x < w - 1; x += 1) {
            for (int y = 1; y < h - 1; y += 1) {
                world[room.x1 + x][room.y1 + y] = Tileset.FLOOR;
            }
        }
    }

    private boolean overlaps(Room room) {
        return (x1 <= room.x2 && x2 >= room.x1 &&
                y1 <= room.y2 && room.y2 >= room.y1);
    }

    public static void connectRooms(Room room) {

        // using BSPTree would return itself-- abort
        Room nearestRoom = BSPTree.nearest(room);

        // put Vertical or Horizontal Hallways connecting room and nearestRoom
        // how to build combination of these hallways to connect diagonal rooms// offset rooms
        if (true) {
            Hallway.putVerticalHallway(world, room, nearestRoom);
        } else {
            Hallway.putHorizontalHallway( world, room, nearestRoom);
        }
    }

    /**
     * Returns the euclidean distance (L2 norm) squared between two points
     * (x1, y1) and (x2, y2). Note: This is the square of the Euclidean distance,
     * i.e. there's no square root.
     */
    private static double distance(double x1, double x2, double y1, double y2) {
        return Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
    }

    /**
     * Returns the euclidean distance (L2 norm) squared between two points.
     * Note: This is the square of the Euclidean distance, i.e.
     * there's no square root.
     */
    public static double distance(Room r1, Room r2) {
        return distance(r1.getX(), r2.getX(), r1.getY(), r2.getY());
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Room otherRoom = (Room) other;
        return getX() == otherRoom.getX() && getY() == otherRoom.getY();
    }

    public static void main(String[] args) {


    }

}
