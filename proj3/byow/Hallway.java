package byow;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Hallway {

    public static void horizontalHallway(int x1, int x2, int y, TETile[][] world) {
        System.out.println("we're making a h hallway");
        for (int x = x1; x < x2; x += 1) {
            for (int a = x1+1; a < x2+1; a+=1)
            world[x][y] = Tileset.FLOOR;
        }
    }

    public static void verticalHallway(int y1, int y2, int x, TETile[][] world) {
        System.out.println("we're making a v hallway");
        for (int y = y1; y < y2; y += 1) {
            world[x][y] = Tileset.FLOOR;
        }
    }



}
