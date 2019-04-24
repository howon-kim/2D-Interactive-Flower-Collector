package byow;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Hallway {
    public static void horizontalHallway(Room prev, Room cur, TETile[][] world){
        int temp = 0;
        if (cur.getX1() > prev.getX2()) {
            int width = Math.abs(cur.getX1() - prev.getX2());
            for (int i = prev.getX2() - 1; i <= prev.getX2() + width / 2; i++) {
                world[i][prev.centerY - 1] = Tileset.WALL;
                world[i][prev.centerY] = Tileset.FLOOR;
                world[i][prev.centerY + 1] = Tileset.WALL;

            }
            for (int i = cur.getX1(); i >= cur.getX1() - width / 2; i--) {
                world[i][cur.centerY - 1] = Tileset.WALL;
                world[i][cur.centerY] = Tileset.FLOOR;
                world[i][cur.centerY + 1] = Tileset.WALL;

                temp = i;
            }
        } else {
            int width = Math.abs(cur.getX1() - prev.getX2());
            for (int i = cur.getX2() - 1; i <= cur.getX2() + width / 2; i++) {
                world[i][cur.centerY - 1] = Tileset.WALL;
                world[i][cur.centerY] = Tileset.FLOWER;
                world[i][cur.centerY + 1] = Tileset.WALL;

            }
            for (int i = prev.getX1(); i >= prev.getX1() - width / 2; i--) {
                world[i][prev.centerY - 1] = Tileset.WALL;
                world[i][prev.centerY] = Tileset.FLOWER;
                world[i][prev.centerY + 1] = Tileset.WALL;

                temp = i;
            }
        }
            /**
            if (cur.centerY > prev.centerY){
                for (int i = prev.centerY; i <= cur.centerY; i++) {
                    world[temp-1][i+1] = Tileset.WALL;
                    world[temp][i] = Tileset.FLOWER;
                    world[temp+1][i-2] = Tileset.WALL;

                }
            } else {
                for (int i = cur.centerY; i <= prev.centerY; i++) {
                    world[temp-1][i+1] = Tileset.WALL;
                    world[temp][i] = Tileset.FLOOR;
                    world[temp+1][i-1] = Tileset.WALL;

                }
            }**/


    }

        /**
        public static void horizontalHallway(Room prev, Room cur, TETile[][] world){

            int prevX = 0; int curX = 0;
            if (prev.getX1() > cur.getX2()) {
                int width = Math.abs(prev.getX1() - cur.getX2());
                for (int i = prev.getX1(); i >= prev.getX1() - width / 2; i--) {
                    world[i][prev.centerY] = Tileset.FLOWER;
                    prevX = i;
                }
                for (int i = cur.getX2(); i <= cur.getX2() + width / 2; i++) {
                    world[i][cur.centerY] = Tileset.FLOWER;
                    curX = i;
                }
            } else {
                int width = Math.abs(prev.getX1() - cur.getX2());
                for (int i = prev.getX1(); i <= prev.getX1() + width / 2; i++) {
                    world[i][prev.centerY] = Tileset.FLOWER;
                    prevX = i;
                }
                for (int i = cur.getX2(); i <= cur.getX2() - width / 2; i--) {
                    world[i][cur.centerY] = Tileset.FLOWER;
                    curX = i;
                }
            }
            if (prev.centerY > cur.centerY) {
                for(int i = cur.centerY; i <= prev.centerY; i++) {
                    world[prevX][i] = Tileset.FLOWER;
                }
            } else {
                for(int i = prev.centerY; i <= cur.centerY; i++) {
                    world[curX][i] = Tileset.FLOWER;
                }
            }
        }**/
    public static void horizontalHallway(int x1, int x2, int y, TETile[][] world) {
        System.out.println("we're making a h hallway");

        if (x1 <= x2) {
            for (int x = x1; x < x2; x += 1) {
                //world[x][y - 1] = Tileset.WALL;
                world[x][y] = Tileset.FLOOR;
                //world[x][y + 1] = Tileset.WALL;
            }
        } else {
            for (int x = x2; x > x1; x -= 1) {
                //world[x][y - 1] = Tileset.WALL;
                world[x][y] = Tileset.FLOOR;
                //world[x][y + 1] = Tileset.WALL;
            }
        }

    }

    public static void verticalHallway(int y1, int y2, int x, TETile[][] world) {
        System.out.println("we're making a v hallway");
        if (y1 <= y2) {
            for (int y = y1; y < y2; y += 1) {
                world[x][y] = Tileset.FLOOR;
            }
        } else {
            for (int y = y2; y > y1; y -= 1) {
                world[x][y] = Tileset.FLOOR;
            }
        }
    }


}
