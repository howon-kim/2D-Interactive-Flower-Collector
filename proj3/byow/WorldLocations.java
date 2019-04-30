package byow;
import byow.TileEngine.TETile;

public class WorldLocations {
    private Location player;
    private TETile[][] world;

    public WorldLocations(Location playerloc, TETile[][] world) {
        this.player = playerloc;
        this.world = world;
    }

    public Location player() {
        return this.player;
    }

    public TETile[][] world() {
        return this.world;
    }
}
