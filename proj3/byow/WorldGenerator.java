package byow;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.ArrayList;


public class WorldGenerator {

    public TETile[][] world;
    public int seed;
    public ArrayList<Location>  locations;
    public WeightedQuickUnionUF connectedUF;
    public ArrayList<Room> rooms;

    public WorldGenerator(int seed) {
        this.seed = seed;

        // partition the space randomly; output is list of Locations
        locations = BSP();

        // needs number of rooms from room array// how does world generator get this input?
        this.connectedUF = new WeightedQuickUnionUF(locations.size()  + 2);


        // for each location, generates room within location, puts into world
        for (Location loc: locations) {
            Room.generateRoom(loc);
        }

        for (Room room: rooms) {
            // hallway operations
            connectedUF.union();
        }
    }

    public int seed() {
        return this.seed;
    }


    public ArrayList<Location> BSP() {
        ArrayList<Location> locations = new ArrayList<>();
        // Binary Space Partitioning Mechanics //
        // should output specific range of x1, x2, y1, y2 values for Room to be Located






        return locations;
    }

    public boolean connected() {

        // is first room connected to last room
        return connectedUF.connected(rooms.get(0), rooms.get(rooms.size() - 1));
    }



}
