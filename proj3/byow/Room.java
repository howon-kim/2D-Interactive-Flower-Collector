package byow;
import byow.TileEngine.TETile;
import java.util.Random;
import byow.TileEngine.Tileset;
import java.util.ArrayList;

public class Room {

    private static final int maxSize = 10;
    private static final int minSize = 4;
    private static ArrayList<Room> rooms = new ArrayList<>();
    private static final long SEED = 2131242;
    private static final Random RANDOM = new Random(SEED);

    public static TETile[][] world;

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

    /**
    public static void randomRoom(int screen_w, int screen_h, TETile[][] world) {
        int w = RANDOM.nextInt((maxSize - minSize) + 1) + minSize;
        int h = RANDOM.nextInt((maxSize - minSize) + 1) + minSize;
        int x = RANDOM.nextInt(screen_w);
        int y = RANDOM.nextInt(screen_h);

        generateRoom(x, y, w, h, world);

    }
     */

    private static boolean overlaps(Room room) {
        for (Room r: rooms) {
            if (r.getX1() <= room.x2 && r.getX2() >= room.x1 && r.getY1() <= room.y2 && room.y2 >= room.y1){
                return true;
            }
        }
        return false;
    }

    private static boolean outbound(Room room) {
        if(room.getX2() > 60 || room.getY2() > 50){
            return true;
        }
        return false;
    }

/*
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
*/

}
