package byow;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Hallway {

    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private int w;
    private int h;

    public static void horizontalHallway(int x1, int x2, int y, TETile[][] world) {
        System.out.println("h hallway");
        if (x1 > x2) {
            for (int x = x2; x > x1; x -= 1) {
                world[x][y] = Tileset.FLOOR;
            }
        } else {
            for (int x = x1; x < x2; x += 1) {
                world[x][y] = Tileset.FLOOR;
            }
        }
    }

    public static void verticalHallway(int y1, int y2, int x, TETile[][] world) {
        System.out.println("v hallway");
        if (y1 > y2) {
            for (int y = y2; y > y1; y -= 1) {
                world[x][y] = Tileset.FLOOR;
            }
        } else {
            for (int y = y1; y < y2; y += 1) {
                world[x][y] = Tileset.FLOOR;
            }
        }
    }


}
