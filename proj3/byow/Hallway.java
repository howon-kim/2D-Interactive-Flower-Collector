package byow;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Hallway {

    // What if you generate the other rooms by branching out of hallways, the way hallways are currently branching out of rooms? We did something similar
    // where you start with a room, which creates hallways, which then create more hallways and rooms and recursively fill up the world.

    public static TETile world;
/*
    public static final int maxSize = 10;
    public static final int minSize = 2;

    public static void generateHallway() {
        Random rand = new Random(123);

        int w = 1;
        int h = rand.nextInt(maxSize - minSize + 1) + minSize;

        // function that picks direction randomly-- left, right, up, down
        // then calculates midpoint
        // then builds and fills that specified hallway


    }
    /**
    public static void putVerticalHallway(TETile[][] world, Room room1, Room room2) {

      //  int h =
        // generating midpoints of room objects

        double midpoint1 = (room1.x1 - room1.x2) / 2;
        double midpoint2 = (room2.x1 - room2.x2) / 2;

        for (int y = 0; y < h; y += 1) {
            world[loc.x() - 1][loc.y() + y] = Tileset.WALL;
            world[loc.x() + 1][loc.y() + y] = Tileset.WALL;
        }

        for (int y = 0; y < h; y += 1) {
            world[loc.x()][loc.y() + y] = Tileset.FLOOR;
        }
    }

    public static void putHorizontalHallway(TETile[][] world, Room room1, Room room2) {

      //  int w =
        double midpoint1 = (room1.y1 - room1.y2) / 2;
        double midpoint2 = (room2.y1 - room2.y2) / 2;

        for (int x = 0; x < w; x += 1) {
            world[loc.x() + x][loc.y() - 1] = Tileset.WALL;
            world[loc.x() + x][loc.y() + 1] = Tileset.WALL;
        }
        for (int x = 0; x < w; x += 1) {
            world[loc.x() + x][loc.y()    ] = Tileset.FLOOR;
        }
    }
*/

}
