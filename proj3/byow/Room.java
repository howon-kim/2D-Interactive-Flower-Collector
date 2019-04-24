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

    public int centerX;
    public int centerY;

    public Room(int x, int y, int w, int h) {
        this.x1 = x;
        this.x2 = x + w;
        this.y1 = y;
        this.y2 = y + h;
        this.w = w;
        this.h = h;
        this.centerX = (int) (x1 + x2) / 2;
        this.centerY = (int) (y1 + y2) / 2;
    }


    public int getX() {
        return this.x1;
    }

    public int getY() {
        return this.y1;
    }

    public int getX1() {
        return this.x1;
    }

    public int getY1() {
        return this.y1;
    }

    public int getX2() {
        return this.x2;
    }

    public int getY2() {
        return this.y2;
    }

    public static void generateRoom(int x1, int x2, int w, int h, TETile[][] world) {
        Room newRoom = new Room(x1, x2, w, h);

        boolean failed = false;

            for (Room otherRoom : rooms) {
                if (newRoom.intersects(otherRoom)) {
                    System.out.println("overlay");
                    failed = true;
                    break;
                }
                if (outbound(newRoom)) {
                    System.out.println("outbound");
                    failed = true;
                    break;
                }
            }

            if (!failed) {

                System.out.println("room creation");
                putRoom(newRoom, world);

                System.out.println("hallway creation");
                int newCenterX = newRoom.centerX;
                int newCenterY = newRoom.centerY;

                if (rooms.size() != 0) {
                    int prevCenterX = rooms.get(rooms.size() - 1).centerX;
                    int prevCenterY = rooms.get(rooms.size() - 1).centerY;

                    if (RANDOM.nextInt(2) == 1) {
                        Hallway.horizontalHallway(prevCenterX, newCenterX, prevCenterY, world);
                        Hallway.verticalHallway(prevCenterY, newCenterY, newCenterX, world);

                    } else {
                        Hallway.verticalHallway(prevCenterY, newCenterY, prevCenterX, world);
                        Hallway.horizontalHallway(prevCenterX, newCenterX, newCenterY, world);
                    }
                }
            }

            if (!failed) {
                rooms.add(newRoom);
            }
        }


        public static void putRoom (Room room, TETile[][]world){
            int x1 = room.x1;
            int x2 = room.x2;
            int y1 = room.y1;
            int y2 = room.y2;


            for (int x = x1; x < x2; x++) {
                for (int y = y1; y < y2; y++) {
                    if (x == x1 || x == x2 - 1 || y == y1 || y == y2 - 1) {
                        world[x][y] = Tileset.WALL;
                    } else
                        world[x][y] = Tileset.FLOOR;
                }
            }
        }


        public static void randomRoom ( int screen_w, int screen_h, TETile[][] world){
            int w = RANDOM.nextInt((maxSize - minSize) + 1) + minSize;
            int h = RANDOM.nextInt((maxSize - minSize) + 1) + minSize;
            int x = RANDOM.nextInt(screen_w);
            int y = RANDOM.nextInt(screen_h);

            generateRoom(x, y, w, h, world);

        }

        private boolean intersects (Room room){
            return (x1 <= room.x2 && x2 >= room.x1 &&
                    y1 <= room.y2 && room.y2 >= room.y1);
        }


        private static boolean outbound (Room room){
            if (room.getX2() > 60 || room.getY2() > 50) {
                return true;
            }
            return false;
        }


}