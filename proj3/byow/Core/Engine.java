package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.*;
import byow.WorldGenerator;
import byow.Location;
import java.util.Random;
import byow.Room;
import java.util.ArrayList;



import byow.WorldLocations;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static long SEED;
    private static final int MENUW = 40;
    private static final int MENUH = 60;
    private String keyboardInput;


    /* For "Game" Mechanics */
    private static boolean GAMEOVER = false;
    private int HEALTH;
    private String s;
    private int COUNTER;
    public static WorldLocations worldlocs;
    public static Location player;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        Menu.makeGUIBackground();
        Menu.makeGUI();
        StdDraw.show();
        StdDraw.enableDoubleBuffering();

        while (true) {
            keyboardInput = "";
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            keyboardInput += key;
            StdDraw.enableDoubleBuffering();
            StdDraw.clear(Color.BLACK);

            Menu.makeGUI();
            StdDraw.show();

            switch (key) {
                /* New Game Operations */
                case ('n'): {
                    String seed = "";
                    char c = 'l';
                    StdDraw.clear(Color.BLACK);
                    Menu.makeGUI();
                    StdDraw.text(MENUW / 2, MENUH / 4,
                            "Let's generate a new world! Input a seed, then press 's' to begin.");
                    StdDraw.show();

                    do {
                        if (!StdDraw.hasNextKeyTyped()) {
                            continue;
                        }
                        c = StdDraw.nextKeyTyped();
                        if (c >= 48 && c <= 57) {
                            seed += String.valueOf(c);
                        }
                        seed += String.valueOf(c);
                        if (c != 's') {
                            StdDraw.clear(Color.BLACK);
                            Menu.makeGUI();
                            StdDraw.text(MENUW / 2, MENUH / 4, "Your seed is: " + seed);
                            StdDraw.show();
                        }
                    } while (c != 's');

                    SEED = stringToInt(seed);

                    System.out.println("## SEED: " + SEED);
                    WorldGenerator.generateWorld();
                    player = makePlayer();
                    worldlocs = new WorldLocations(player, WorldGenerator.world);

                    System.out.println(player.getX());
                    System.out.println(player.getY());
                    playWorld(WorldGenerator.world);
                    break;
                }

                /* Load Operations */
                case ('l'): {
                    TETile[][] world = loadWorld();
                    GAMEOVER = false;
                    playWorld(world);
                    break;
                }

                case ('q'): {
                    /* Quit Operations */
                    GAMEOVER = true;
                    System.exit(0);
                    break;
                }

            }
        }
    }

    public static Location makePlayer() {
        Location p = getplayerEntry();
        WorldGenerator.world[p.getX()][p.getY()] = Tileset.FLOWER;
        System.out.println("Set Avatar");
        return p;
    }

    private static Location getplayerEntry() {
        Random rand = new Random(SEED);
        // int x = rand.nextInt(WIDTH/3);
        // int y = rand.nextInt(HEIGHT/3);

        int x = Room.rooms.get(Room.rooms.size() - 1).getCenterX();
        int y = Room.rooms.get(Room.rooms.size() - 1).getCenterY();
        return new Location(x, y);
    }


    private void playWorld(TETile[][] world) {

        /** new Thread(() -> {
            while (COUNTER > 0) {
                StdDraw.enableDoubleBuffering();

                timeCounter--;
                //long hh = timeCounter / 60 / 60 % 60;
                //long mm = timeCounter / 60 % 60;
                //long ss = timeCounter % 60;
                //System.out.println("left + hh + "hours" + mm + "minutes" + ss + "seconds");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
         */

        char key;
        String record = "";

        while (!GAMEOVER) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            key = StdDraw.nextKeyTyped();
            record += key;

            /**
             * FOR QUIT
            //System.out.println(record);
            for (int i = 0; i < record.length() - 1; i += 1) {
                if ((record.charAt(i) == ':' && record.charAt(i + 1) == 'q')
                        || (record.charAt(i) == ':' && record.charAt(i + 1) == 'Q')) {
                    saveWorld(world);
                    Menu.makeGUIBackground();
                    Menu.makeCustomMessageScreen("Your game has been saved!");
                    GAMEOVER = true;
                }
            }
             */
            worldlocs = move(worldlocs, key);
            System.out.println("moved");
        }
        Menu.makeGUIBackground();
        Menu.makeCustomMessageScreen("Have an another try next time!");
    }

    public long stringToInt(String str) {
        str = str.trim();
        String str2 = "";
        if (!"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    str2 = str2 + str.charAt(i);
                }
            }
        }
        return Long.parseLong(str2);
    }

     private WorldLocations move(WorldLocations worldlocs, char key) {
        switch (key) {
            case ('w'): {
                WorldGenerator.world[worldlocs.player().getX()][worldlocs.player().getY() + 1] = Tileset.AVATAR;
                WorldGenerator.world[worldlocs.player().getX()][worldlocs.player().getY()] = Tileset.FLOOR;
                Location newplayerlocation = new Location(worldlocs.player().getX(), worldlocs.player().getY() + 1);
                return new WorldLocations(newplayerlocation, WorldGenerator.world);
                }
            case ('s'): {
                WorldGenerator.world[worldlocs.player().getX()][worldlocs.player().getY() - 1] = Tileset.AVATAR;
                WorldGenerator.world[worldlocs.player().getX()][worldlocs.player().getY()] = Tileset.FLOOR;
                Location newplayerlocation = new Location(worldlocs.player().getX(), worldlocs.player().getY() - 1);
                return new WorldLocations(newplayerlocation, WorldGenerator.world);
                }
            case ('a'): {
                WorldGenerator.world[worldlocs.player().getX() - 1][worldlocs.player().getY()] = Tileset.AVATAR;
                WorldGenerator.world[worldlocs.player().getX()][worldlocs.player().getY()] = Tileset.FLOOR;
                Location newplayerlocation = new Location(worldlocs.player().getX() - 1, worldlocs.player().getY());
                return new WorldLocations(newplayerlocation, WorldGenerator.world);
                }

            case ('d'): {
                WorldGenerator.world[worldlocs.player().getX() + 1][worldlocs.player().getY()] = Tileset.AVATAR;
                WorldGenerator.world[worldlocs.player().getX()][worldlocs.player().getY()] = Tileset.FLOOR;
                Location newplayerlocation = new Location(worldlocs.player().getX() + 1, worldlocs.player().getY());
                return new WorldLocations(newplayerlocation, WorldGenerator.world);
            } default: return worldlocs;
        }
     }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        /** Initialize Tiles **/
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        /** String process **/
        String userInput = input.substring(1, input.length() - 1);
        /** World Generator Initiate **/
        WorldGenerator worldGenerator =
                new WorldGenerator(finalWorldFrame, Long.parseLong(userInput));
        /** Clear the world **/
        worldGenerator.clearWorld();
        /** Randomize world **/
        worldGenerator.randomizeWorld();
        return finalWorldFrame;
    }


    private static void saveWorld(TETile[][] world) {
        File file = new File("./crazyWorld.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(world);
        }  catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private static TETile[][] loadWorld() {
        File file = new File("./savedWorld.txt");
        if (file.exists()) {
            try {
                FileInputStream fs = new FileInputStream(file);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (TETile[][]) os.readObject();
            }
            /* Necessary for ObjectInputStream */
            catch (FileNotFoundException e) {
                System.out.println("File Not Found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("Class Not Found");
                System.exit(0);
            }
        }
        System.out.println("No World Saved Yet-- Random World Returned");
        return WorldGenerator.generateWorld();
    }

}
