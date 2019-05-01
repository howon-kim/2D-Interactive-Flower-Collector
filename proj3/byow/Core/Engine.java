package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.*;
import byow.Location;
import byow.WorldGenerator;
import byow.WorldLocations;
import byow.Room;
import java.util.ArrayList;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Random;


public class Engine {

    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static long SEED;
    private Random RANDOM;

    private static final int MENUW = 40;
    private static final int MENUH = 60;
    private String keyboardInput;


    /* For "Game" Mechanics */
    private TETile[][] world;
    private Location player;
    private ArrayList<Location> keys;

    private static boolean GAMEOVER = false;
    private int HEALTH;
    private String s;
    private int COUNTER;
    private static WorldLocations worldlocs;
    //public static Location player;

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

            switch (key) {
                /* New Game Operations */
                case ('n'): {
                    String seed = "";
                    char c = 'l';

                    StdDraw.text(MENUW / 2, MENUH / 4,
                            "Let's generate a new world! Input a seed, then press 's' to begin.");
                    StdDraw.show();

                    while (c != 's') {
                        if (!StdDraw.hasNextKeyTyped()) {
                            continue;
                        }
                        c = StdDraw.nextKeyTyped();
                        seed += String.valueOf(c);
                        if (c != 's') {
                            StdDraw.clear(Color.BLACK);
                            Menu.makeGUI();
                            StdDraw.text(MENUW / 2, MENUH / 4, "Your seed is: " + seed);
                            StdDraw.show();
                        }
                    }

                    SEED = stringToInt(seed);
                    RANDOM = new Random(SEED);
                    System.out.println("## SEED: " + SEED);
                    world = WorldGenerator.generateWorld(SEED);
                    player = makePlayer();
                    keys = makeKeys();
                    displayKeys();
                    ter.initialize(Engine.WIDTH, Engine.HEIGHT);
                    ter.renderFrame(world);
                    worldlocs = new WorldLocations(player, world);

                    playWorld(world);
                    break;
                }

                /* Load Operations */
                case ('l'): {
                    ArrayList data = loadWorld();
                    world = (TETile[][]) data.get(0);
                    player = (Location) data.get(1);

                    ter = new TERenderer();
                    ter.initialize(Engine.WIDTH, Engine.HEIGHT);
                    ter.renderFrame(world);
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
                default:
                    return;
            }
        }
    }

    public Location makePlayer() {
        Location p = getplayerEntry();
        world[p.getX()][p.getY()] = Tileset.AVATAR;
        System.out.println("Set Avatar");
        return p;
    }

    private Location getplayerEntry() {
        int index = RANDOM.nextInt(Room.getRooms().size());
        Room room = (Room) Room.getRooms().get(index);
        int x = room.getCenterX();
        int y = room.getCenterY();
        return new Location(x, y);
    }

    public ArrayList<Location> makeKeys() {
        int numKeys = 5;
        int index;
        ArrayList<Location> keys = new ArrayList<>();
        for(int i = 1; i <= numKeys; i++) {
            index =  RANDOM.nextInt(Room.getRooms().size());
            Room room = (Room) Room.getRooms().get(index);
            int x = room.getCenterX();
            int y = room.getCenterY();
            keys.add(new Location(x, y));
        }
        return keys;
    }

    public void displayKeys() {
        for (Location loc: keys) {
            world[loc.getX()][loc.getY()] = Tileset.FLOWER;
        }
    }


    private void playWorld(TETile[][] w) {

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
        Menu.makeHUD();

        while (!GAMEOVER) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            key = StdDraw.nextKeyTyped();
            record += key;

            /* FOR QUIT */
            System.out.println(record);
            for (int i = 0; i < record.length() - 1; i += 1) {
                if ((record.charAt(i) == ':' && record.charAt(i + 1) == 'q')
                        || (record.charAt(i) == ':' && record.charAt(i + 1) == 'Q')) {
                    saveWorld(w, player);
                    Menu.makeGUIBackground();
                    Menu.makeCustomMessageScreen("Your game has been saved!");
                    StdDraw.pause(1500);
                    GAMEOVER = true;
                }
            }

            move(player, key);
            //System.out.println(player.getX() + " " + player.getY());
            ter.renderFrame(w);
        }
        Menu.makeGUIBackground();
        Menu.makeCustomMessageScreen("Do you want to start over (y/n)?");
        while(true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            key = StdDraw.nextKeyTyped();
            if (key == 'y' || key == 'Y') {
                GAMEOVER = false;
                interactWithKeyboard();
            } else {
                Menu.makeGUIBackground();
                Menu.makeCustomMessageScreen("Thank you for playing!");
                StdDraw.show();
                StdDraw.pause(500);
                System.exit(0);
            }
        }
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

    private Boolean moveHelper(Location obj) {
        if (world[obj.getX()][obj.getY()].character() == Tileset.WALL.character()) {
            return false;
        } else {
            world[obj.getX()][obj.getY()] = Tileset.AVATAR;
            return true;
        }
    }

    private void move(Location obj, char key) {
        switch (key) {
            case ('w'): {
                Location newplayerlocation = new Location(obj.getX(), obj.getY() + 1);
                if (moveHelper(newplayerlocation)) {
                    world[obj.getX()][obj.getY()] = Tileset.FLOOR;
                    player = newplayerlocation;
                }
                break;
            }
            case ('s'): {
                Location newplayerlocation = new Location(obj.getX(), obj.getY() - 1);
                if (moveHelper(newplayerlocation)) {
                    world[obj.getX()][obj.getY()] = Tileset.FLOOR;
                    player = newplayerlocation;
                }
                break;
            }
            case ('a'): {
                Location newplayerlocation = new Location(obj.getX() - 1, obj.getY());
                if (moveHelper(newplayerlocation)) {
                    world[obj.getX()][obj.getY()] = Tileset.FLOOR;
                    player = newplayerlocation;
                }
                break;
            }

            case ('d'): {
                Location newplayerlocation = new Location(obj.getX() + 1, obj.getY());
                if (moveHelper(newplayerlocation)) {
                    world[obj.getX()][obj.getY()] = Tileset.FLOOR;
                    player = newplayerlocation;
                }
                break;
            }
            default:
                return;
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
        input = input.toLowerCase();
        System.out.println(input);


        /** String process **/
        String move = "";
        int endSeed = 0;
        int save = input.indexOf(":q");
        char gameMode = input.charAt(0);

        //System.out.println(characterInput);
        //System.out.println(gameMode);
        if (gameMode == 'n') {

            endSeed = input.indexOf("s");
            SEED = Long.parseLong(input.substring(1, endSeed));
            RANDOM = new Random(SEED);

            /** World Generator Initiate **/
            world = WorldGenerator.generateWorld(SEED);
            player = makePlayer();

            /** Put Keys **/
            keys = makeKeys();
            displayKeys();

        } else if (gameMode == 'l') {
            world = (TETile[][]) loadWorld().get(0);
            player = (Location) loadWorld().get(1);

        } else {
            System.out.println("Incorrect Game Mode");
            return null;
        }

        if (save != -1) {
            move = input.substring(endSeed + 1, save);
        } else {
            move = input.substring(endSeed + 1, input.length());
        }


        /** Move Character **/
        if (!move.isEmpty()) {
            for (int i = 0; i < move.length(); i++) {
                move(player, move.charAt(i));
                //System.out.println(worldlocs.player().getX() + " " +  worldlocs.player().getY());
            }
        }

        if (save != -1) {
            saveWorld(world, player);
        }
        return world;
    }

    /**
     * private TETile[][] inputStringGame(char key, String input) {
     * switch (key) {
     * case ('n'): {
     * SEED = stringToInt(input);
     * <p>
     * // WorldGenerator.generateWorld();
     * // player = makePlayer();
     * // ter.renderFrame(WorldGenerator.getWorld());
     * // worldlocs = new WorldLocations(player, WorldGenerator.getWorld());
     * <p>
     * WorldGenerator.getWorld();
     * <p>
     * int start = 1;
     * for (int i = 0; i < input.length(); i += 1) {
     * if (input.charAt(i) == 's' || input.charAt(i) == 'S') {
     * start = i + 1;
     * break;
     * }
     * }
     * for (int i = start; i < input.length(); i += 1) {
     * //worldlocs = move(worldlocs, input.charAt(i));
     * if ((input.charAt(i) == ':' && input.charAt(i + 1) == 'q')
     * || (input.charAt(i) == ':' && input.charAt(i + 1) == 'Q')) {
     * GAMEOVER = true;
     * saveWorld(world, player);
     * System.out.println("Saved");
     * break;
     * }
     * }
     * return WorldGenerator.getWorld();
     * }
     * case ('l'): {
     * world = (TETile[][]) loadWorld().get(0);
     * worldlocs = new WorldLocations((Location) loadWorld().get(1), WorldGenerator.getWorld());
     * <p>
     * int start = 1;
     * for (int i = 0; i < input.length(); i += 1) {
     * if (input.charAt(i) == 's' || input.charAt(i) == 'S') {
     * start = i + 1;
     * break;
     * }
     * }
     * for (int i = start; i < input.length(); i += 1) {
     * if ((input.charAt(i) == ':' && input.charAt(i + 1) == 'q')
     * || (input.charAt(i) == ':' && input.charAt(i + 1) == 'Q')) {
     * GAMEOVER = true;
     * saveWorld(WorldGenerator.getWorld(), player);
     * System.out.println("Saved");
     * break;
     * }
     * //worldlocs = move(worldlocs, input.charAt(i));
     * }
     * return WorldGenerator.getWorld();
     * }
     * case ('q'): {
     * GAMEOVER = true;
     * world = new TETile[80][30];
     * for (TETile[] x : world) {
     * for (TETile y : x) {
     * y = Tileset.NOTHING;
     * }
     * }
     * return world;
     * }
     * default: {
     * GAMEOVER = true;
     * world = new TETile[80][30];
     * for (TETile[] x : world) {
     * for (TETile y : x) {
     * y = Tileset.NOTHING;
     * }
     * }
     * return world;
     * }
     * }
     * }
     **/
    private static ArrayList loadWorld() {
        File file = new File("./save_data.txt");
        if (file.exists()) {
            try {
                FileInputStream fs = new FileInputStream(file);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (ArrayList) os.readObject();
            } catch (FileNotFoundException e) {
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
        System.out.println("No World Saved Yet-- Returning Brand New World");
        ArrayList data = new ArrayList();
        data.add(WorldGenerator.generateWorld(SEED));
        return data;
    }

    private static void saveWorld(TETile[][] world, Location player) {
        File file = new File("./save_data.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            ArrayList data = new ArrayList<Object>();
            data.add(world);
            data.add(player);

            os.writeObject(data);
            System.out.println("saved");

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("wqrqwcasgwtqw");
            System.out.println(e);
            System.exit(0);
        }
    }

}