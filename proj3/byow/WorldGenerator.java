package byow;
import byow.TileEngine.TETile;


public class WorldGenerator {

    public TETile[][] world;
    public int seed;

    public WorldGenerator(int seed) {
        this.seed = seed;
    }

    public int seed() {
        return this.seed;
    }

    // Binary Space Partitioning Mechanics //

    // should output specific range of x1, x2, y1, y2 values for Room to be Located

}
