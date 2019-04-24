package byow.lab12;

import byow.Location;
import byow.Room;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 *  Draws a world that is mostly empty except for a small region.
 */
public class BoringWorldDemo {
    public static final int WIDTH = 60;
    public static final int HEIGHT = 20;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        //Room.randomRoom(WIDTH,HEIGHT,world);

        Room.random(world, 1234);

        // fills in a block 14 tiles wide by 4 tiles tall
/*
        for (int i = 1; i < WIDTH - 1; i+=5) {
            for (int j = 1; j < HEIGHT - 1; j++) {
                world[i - 1][j] = Tileset.WALL;
                world[i][j] = Tileset.FLOOR;
                world[i + 1][j] = Tileset.WALL;
            }
        }

        for (int i = 1; i < WIDTH - 1; i+=5) {
            for (int j = 1; j < HEIGHT - 1; j++) {
                world[j - 1][i] = Tileset.WALL;
                world[j][i] = Tileset.FLOOR;
                world[j + 1][i] = Tileset.WALL;
            }
        }

 */
        // draws the world to the screen
        ter.renderFrame(world);
    }


}
