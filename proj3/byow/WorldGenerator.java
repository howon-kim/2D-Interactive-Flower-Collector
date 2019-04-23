package byow;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import byow.Location;
import byow.Room;
import java.util.ArrayList;


public class WorldGenerator {

    public TETile[][] world;
    public int seed;
    public ArrayList<Location>  locations;
    private WeightedQuickUnionUF connectedUF;

    public WorldGenerator(int seed) {
        this.seed = seed;

        // needs number of rooms from room array// how does world generator get this input?
        this.connectedUF = new WeightedQuickUnionUF(NumberofRooms);

        // partition the space randomly; output is list of Locations
        locations = BSP();

        for (Location loc: locations) {
            generateRoom(loc); // generates room within location, puts into world
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
        return false;
    }



}
