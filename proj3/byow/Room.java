package byow;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Room {

    private int width;
    private int height;

    public Room(int w, int h) {
        this.width = w;
        this.height = h;
    }

    public int height() {
        return this.height;
    }

    public int width() {
        return this.width;
    }

    public void addRoom(TETile[][] world, Location loc, int w, int h) {
        for (int x = 0; x < w; x += 1) {
            world[loc.xPos() + x][loc.yPos()] = Tileset.WALL;
            world[loc.xPos() + x][loc.yPos() + h - 1] = Tileset.WALL;
        }
        for (int y = 0; y < h; y += 1) {
            world[loc.xPos()][loc.yPos() + y] = Tileset.WALL;
            world[loc.xPos() + w - 1][loc.yPos() + y] = Tileset.WALL;
        }
    }


}
