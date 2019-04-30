package byow;
import byow.TileEngine.TETile;

public class WorldLocations {
    private Location player;

    public WorldLocations(Location playerloc, TETile[][] world) {
        this.player = playerloc;
    }

    public Location player() {
        return this.player;
    }

}
