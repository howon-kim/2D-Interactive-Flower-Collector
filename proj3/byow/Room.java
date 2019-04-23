package byow;
import java.util.Random;

import java.util.ArrayList;

public class Room {

    // 4 corners of the room
    public int x1;
    public int x2;
    public int y1;
    public int y2;

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

    public void putRooms() {
        ArrayList<Room> rooms = new ArrayList<>();

        Random random = new Random();
        int x = random.nextInt();
        int y = random.nextInt();
        int w = random.nextInt();
        int h = random.nextInt();

        Room newRoom = new Room(x, y, w, h);
        boolean failed = false;
        for (Room r: rooms) {
            if (newRoom.overlaps(r)) {
                failed = true;
            }
        }
        if (!failed) {
            rooms.add(newRoom);
        }

    }

    public boolean overlaps(Room room) {
        return (x1 <= room.x2 && x2 >= room.x1 &&
                y1 <= room.y2 && room.y2 >= room.y1);
    }


    /**
    public void makeRoom(int w, int h, TETile[][] world, Location loc) {
        for (int x = 0; x < w; x += 1) {
            world[loc.getX() + x][loc.getY()] = Tileset.WALL;
            world[loc.getX() + x][loc.getY() + h - 1] = Tileset.WALL;
        }
        for (int y = 0; y < h; y += 1) {
            world[loc.getX()][loc.getY() + y] = Tileset.WALL;
            world[loc.getX() + w - 1][loc.getY() + y] = Tileset.WALL;
        }
        for (int x = 1; x < w - 1; x += 1) {
            for (int y = 1; y < h - 1; y += 1) {
                world[loc.getX() + x][loc.getY() + y] = Tileset.FLOOR;
            }
        }
    }
    */


}
