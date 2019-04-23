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
    private static ArrayList<Room> rooms = new ArrayList<>();

    private static final long SEED = 2131242;
    private static final Random RANDOM = new Random(SEED);


    private int x1;
    private int x2;
    private int y1;
    private int y2;

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

    public int getX1() { return this.x1; }
    public int getY1() { return this.y1; }
    public int getX2() { return this.x2; }
    public int getY2() { return this.y2; }

    public static void generateRoom(int x1, int x2, int w, int h, TETile[][] world) {
        Room newRoom = new Room(x1, x2, w, h);
        if(Room.overlaps(newRoom)){
            System.out.println("overlay!");
            return;
        } else if(Room.outbound(newRoom)) {
            System.out.println("outbound");
        } else{
            putRoom(newRoom, world);
            rooms.add(newRoom);
        }
    }

    public static void randomRoom(int screen_w, int screen_h, TETile[][] world) {
        // should there be max, min room sizes?
        // populates list of valid (not intersecting rooms) and adds room to TETile[][] world

        // generates random room dimensions
        // below values have to be within loc.x1, x2, y1, y2

        int minw = 4;
        int maxw = 6;
        int minh = 4;
        int maxh = 6;

        int w = RANDOM.nextInt((maxw - minw) + 1) + minw;
        int h = RANDOM.nextInt((maxh - minh) + 1) + minh;
        int x = RANDOM.nextInt(screen_w);
        int y = RANDOM.nextInt(screen_h);

        //int x = RANDOM.nextInt(((screen_w - w) - 0) + 1) + 0;
        //int y = RANDOM.nextInt(((screen_h - h) - 0) + 1) + 0;


        generateRoom(x, y, w, h, world);
/*
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
        */

    }
    public static void putRoom(Room room, TETile[][] world) {
        int x1 = room.x1;
        int x2 = room.x2;
        int y1 = room.y1;
        int y2 = room.y2;

        for(int x = x1; x < x2; x++) {
            for(int y = y1; y < y2; y++) {
                if (x == x1 || x == x2 - 1 || y == y1 || y == y2 - 1) {
                    world[x][y] = Tileset.WALL;
                } else
                    world[x][y] = Tileset.FLOOR;
            }
        }
    }
/*
    public static void putRoom(Room room, TETile[][] world) {
        int w = room.w;
        int h = room.h;

        System.out.println(room.getX() + " " + room.getY() + " " );

        for (int x = 0; x < w; x += 1) {
            world[room.x1 + x][room.y1 - 1] = Tileset.WALL;
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
*/
    private static boolean overlaps(Room room) {
        for (Room r: rooms) {
            if (r.getX1() <= room.x2 && r.getX2() >= room.x1 && r.getY1() <= room.y2 && room.y2 >= room.y1){
                return true;
            }
        }
        return false;
        /*return (x1 <= room.x2 && x2 >= room.x1 &&
                y1 <= room.y2 && room.y2 >= room.y1);

         */
    }
    private static boolean outbound(Room room) {
        if(room.getX2() > 60 || room.getY2() > 50){
            return true;
        }
        return false;
    }

/*
    public static void connectRooms(Room room) {
        Room nearestRoom = BSPTree.nearest(room);

        // put Vertical or Horizontal Hallways connecting room and nearestRoom
        // how to build combination of these hallways to connect diagonal rooms// offset rooms
        if (true) {
            Hallway.putVerticalHallway(world, room, nearestRoom);
        } else {
            Hallway.putHorizontalHallway( world, room, nearestRoom);
        }
    }
*/
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

}
