package byow;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Hallway {

    public static TETile[][] world;

    public void horizontalHallway(int x1, int x2, int y) {

        for (int x = x1; x < x2; x += 1) {
            world[x][y] = Tileset.FLOOR;
        }
    }

    public void verticalHallway(int y1, int y2, int x) {

        for (int y = y1; y < y2; y += 1) {
            world[x][y] = Tileset.FLOOR;
        }
    }



}
