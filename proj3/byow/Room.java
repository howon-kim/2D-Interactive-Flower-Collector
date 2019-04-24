package byow;
import byow.Core.Engine;
import byow.TileEngine.TETile;
import java.util.Random;
import byow.TileEngine.Tileset;
import byow.lab12.BoringWorldDemo;

import java.util.ArrayList;

public class Room {

    private static final int maxSize = 10;
    private static final int minSize = 4;
    private static ArrayList<Room> rooms = new ArrayList<>();
    private static long SEED;
    private static Random RANDOM = new Random(SEED);

    public static TETile[][] world;
    Boolean connected = false;
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

    public static TETile[][] random(TETile[][] world, long s){
        int width = Engine.WIDTH;
        int height = Engine.HEIGHT;
        SEED = s;
        RANDOM = new Random(SEED);

        for (int i = 0; i <= 10000; i++){
            int w = RANDOM.nextInt((7 - 4) + 1) + 4;
            int h = RANDOM.nextInt((7 - 4) + 1) + 4;
            int x = RANDOM.nextInt(width);
            int y = RANDOM.nextInt(height);

            Room newRoom = new Room(x, y, w, h);
            if (outbound(newRoom)) {
            } else if (intersects(newRoom, world)) {
            } else{
                putRoom(newRoom, world);
                rooms.add(newRoom);
            }
        }
        for (Room room : rooms) {
            boolean detect = false;
            int goalX = 0;
            for (int x = room.getX2() + 1; x < width; x++){
                if(world[x][room.centerY] == Tileset.WALL) {

                    detect = true;
                    goalX = x;
                    break;

                }
            }
            if (detect) {
                for (int x = room.getX2(); x <= goalX + 1; x++){
                    if(world[x][room.centerY - 1] == Tileset.NOTHING) {
                        world[x][room.centerY - 1] = Tileset.WALL;
                    }
                    if(world[x][room.centerY] == Tileset.NOTHING || world[x][room.centerY] == Tileset.WALL) {
                        world[x][room.centerY] = Tileset.FLOOR;
                    }
                    if(world[x][room.centerY + 1] == Tileset.NOTHING) {
                        world[x][room.centerY + 1] = Tileset.WALL;
                    }
                }
            }
        }

        for (Room room : rooms) {
            boolean detect = false;
            int goalY = 0;
            for (int y = room.getY2() + 1; y < height; y++) {
                if (world[room.centerX][y] == Tileset.WALL) {
                    detect = true;
                    goalY = y;
                    break;
                }
            }
            if (detect) {
                for (int y = room.getY2(); y <= goalY; y++) {
                    if (world[room.centerX - 1][y] == Tileset.NOTHING) {
                        world[room.centerX - 1][y] = Tileset.WALL;
                    }
                    if (world[room.centerX][y] == Tileset.NOTHING || world[room.centerX][y] == Tileset.WALL && world[room.centerX ][y+1] != Tileset.NOTHING) {
                        world[room.centerX][y] = Tileset.FLOOR;
                    }
                    if (world[room.centerX + 1][y] == Tileset.NOTHING) {
                        world[room.centerX + 1][y] = Tileset.WALL;
                    }
                }
            }
        }
        /**
        for (Room room : rooms) {
            boolean detect = false;
            int goalY = 0;
            for (int y = room.getY1() - 1; y > 0; y--) {
                if (world[room.centerX][y] == Tileset.WALL) {
                    detect = true;
                    goalY = y;
                    break;
                }
            }
            if (detect) {
                for (int y = room.getY1(); y >= goalY; y--) {
                    world[room.centerX - 1][y] = Tileset.WALL;
                    world[room.centerX][y] = Tileset.FLOOR;
                    world[room.centerX + 1][y] = Tileset.WALL;
                }
            }
        }
         **/
        return world;
    }
    private static boolean outbound (Room room){
        if (room.getX2() >= Engine.WIDTH - 2|| room.getY2() >= Engine.HEIGHT - 2
        || room.getX1() <= 2 || room.getY1() <= 2) {
            return true;
        }
        return false;
    }

    private static boolean intersects (Room room, TETile[][] world){
        for (int i = room.getX1(); i <= room.getX2(); i++) {
            for (int j = room.getY1(); j <= room.getY2(); j++){
                if (world[i][j] != Tileset.NOTHING) {
                    return true;
                }
            }
        }
        return false;
        //return (x1 <= room.x2 && x2 >= room.x1 && y1 <= room.y2 && room.y2 >= room.y1);
    }

    public static void generateRoom(int x1, int x2, int w, int h, TETile[][] world) {
        Room newRoom = new Room(x1, x2, w, h);

        boolean failed = false;

            for (Room otherRoom : rooms) {
                if (newRoom.intersects(otherRoom, world)) {
                    failed = true;
                    return;
                }
                if (outbound(newRoom)) {
                    failed = true;
                    return;
                }
            }

            if (!failed) {
                System.out.println("room creation");
                putRoom(newRoom, world);

                System.out.println("hallway creation");
                int newCenterX = newRoom.centerX;
                int newCenterY = newRoom.centerY;

                if (rooms.size() != 0) {
                    Room prev = rooms.get(rooms.size() - 1);
                    //Hallway.horizontalHallway(prev, newRoom, world);
                    /**
                    if (prev.getX2() < newRoom.getX1() || newRoom.)
                    if (x1 + w1 < x2 || x2 + w2 < x1) then vertical
                    if (y1 + h1 < y2 || y2 + h2 < y1) then horizontal
                     **/
                }
            }
            /*

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
            }*/



            if (!failed) {
                rooms.add(newRoom);
            }
        }


        public static void putRoom (Room room, TETile[][]world){
            int x1 = room.x1;
            int x2 = room.x2;
            int y1 = room.y1;
            int y2 = room.y2;


            for (int x = x1; x <= x2; x++) {
                for (int y = y1; y <= y2; y++) {
                    if (x == x1 || x == x2  || y == y1 || y == y2 ) {
                        world[x][y] = Tileset.WALL;
                    } else
                        world[x][y] = Tileset.FLOOR;
                }
            }
        }

        public static void test(int screen_w, int screen_h, TETile[][] world){
            for (int i = 1; i <= screen_h; i++) {
                for (int j= 1; j <= screen_w; j+=5) {
                    world[i-1][j] = Tileset.FLOOR;
                    world[i][j] = Tileset.FLOOR;
                    world[i+1][j] = Tileset.FLOOR;


                }
            }

        }

        public static void randomRoom ( int screen_w, int screen_h, TETile[][] world){
            int w = RANDOM.nextInt((maxSize - minSize) + 1) + minSize;
            int h = RANDOM.nextInt((maxSize - minSize) + 1) + minSize;
            //int x = RANDOM.nextInt(screen_w);
            //int y = RANDOM.nextInt(screen_h);

            for(int i = 0; i <= screen_w; i += 8){
                for(int j = 0; j <= screen_h; j += 8){
                    w = RANDOM.nextInt((7 - 4) + 1) + 4;
                    h = RANDOM.nextInt((7 - 4) + 1) + 4;
                    generateRoom(j, i, w, h, world);
                }
            }
            //generateRoom(x, y, w, h, world);

        }






}