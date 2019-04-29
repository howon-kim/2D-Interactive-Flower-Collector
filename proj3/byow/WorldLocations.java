package byow;
import byow.TileEngine.TETile;

public class WorldLocations {
    private Location lockedDoor;
    private Location player;
    private TETile[][] world;

    public WorldLocations(Location doorloc, Location playerloc, TETile[][] world) {
        this.lockedDoor = doorloc;
        this.player = playerloc;
        this.world = world;
    }

    public Location lockedDoor() {
        return this.lockedDoor;
    }

    public Location player() {
        return this.player;
    }

    public TETile[][] world() {
        return this.world;
    }
}
