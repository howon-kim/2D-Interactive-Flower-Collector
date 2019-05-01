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
    private int HEALTH = 0;
    private int FLOWERS = 0;
    private String s;
    private int TIMELEFT = 60;
    private static WorldLocations worldlocs;

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
                    putHearts();
                    keys = makeKeys();
                    displayKeys();
                    // added +10
                    ter.initialize(Engine.WIDTH, Engine.HEIGHT + 3);
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

    public void putHearts() {
        ArrayList<Location> heartlocs = new ArrayList<>();
        for (Room room: Room.rooms) {
            int x = room.getCenterX();
            int y = room.getCenterY();
            if (world[x][y] == Tileset.FLOOR) {
                Location loc = new Location(x, y);
                heartlocs.add(loc);
            }
            for (Location l: heartlocs) {
                if (world[l.getX()][l.getY()] != Tileset.AVATAR) {
                    world[l.getX()][l.getY()] = Tileset.HEART;
                }
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
            world[loc.getX()][loc.getY()] = Tileset.KEY;
        }
    }


    private void playWorld(TETile[][] w) {

        new Thread(() -> {
         while (TIMELEFT > 0) {
         StdDraw.enableDoubleBuffering();
         TIMELEFT--;

         try {
             Thread.sleep(1000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }}}).start();

        char key;
        String record = "";

        while (!GAMEOVER) {
            mouseHover();
            // mouseHover2();
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            key = StdDraw.nextKeyTyped();
            record += key;

            if (TIMELEFT == 0) {
                GAMEOVER = true;
                Menu.makeGUIBackground();
                Menu.makeCustomMessageScreen("You lost! You failed to collect all the flowers :(");
                StdDraw.pause(2000);
                break;
            }

            if (FLOWERS == 5) {
                Menu.makeGUIBackground();
                Menu.makeCustomMessageScreen("You did it! You collected all the flowers :)");
                StdDraw.pause(2000);
                break;
            }
            if (HEALTH == 3) {
                TIMELEFT += 20;
                HEALTH = 0;
                StdDraw.text(WIDTH / 2, HEIGHT - 1,
                        "You've collected 3 hearts and gained 20 seconds!");
                StdDraw.show();
            }


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
            // System.out.println(player.getX() + " " + player.getY());
            // ter.renderFrame(w);
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
                StdDraw.pause(800);
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
                if (world[newplayerlocation.getX()][newplayerlocation.getY()] == Tileset.HEART) {
                    HEALTH += 1;
                }
                if (world[newplayerlocation.getX()][newplayerlocation.getY()] == Tileset.KEY) {
                    FLOWERS += 1;
                }
                if (moveHelper(newplayerlocation)) {
                    world[obj.getX()][obj.getY()] = Tileset.FLOOR;
                    player = newplayerlocation;
                }
                break;
            }
            case ('s'): {
                Location newplayerlocation = new Location(obj.getX(), obj.getY() - 1);
                if (world[newplayerlocation.getX()][newplayerlocation.getY()] == Tileset.HEART) {
                    HEALTH += 1;
                }
                if (world[newplayerlocation.getX()][newplayerlocation.getY()] == Tileset.KEY) {
                    FLOWERS += 1;
                }
                if (moveHelper(newplayerlocation)) {
                    world[obj.getX()][obj.getY()] = Tileset.FLOOR;
                    player = newplayerlocation;
                }
                break;
            }
            case ('a'): {
                Location newplayerlocation = new Location(obj.getX() - 1, obj.getY());
                if (world[newplayerlocation.getX()][newplayerlocation.getY()] == Tileset.HEART) {
                    HEALTH += 1;
                }
                if (world[newplayerlocation.getX()][newplayerlocation.getY()] == Tileset.KEY) {
                    FLOWERS += 1;
                }
                if (moveHelper(newplayerlocation)) {
                    world[obj.getX()][obj.getY()] = Tileset.FLOOR;
                    player = newplayerlocation;
                }
                break;
            }

            case ('d'): {
                Location newplayerlocation = new Location(obj.getX() + 1, obj.getY());
                if (world[newplayerlocation.getX()][newplayerlocation.getY()] == Tileset.HEART) {
                    HEALTH += 1;
                }
                if (world[newplayerlocation.getX()][newplayerlocation.getY()] == Tileset.KEY) {
                    FLOWERS += 1;
                }
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

    private void mouseHover() {
        System.out.println("Before coordinates");
        int mx = (int) StdDraw.mouseX();
        int my = (int) StdDraw.mouseY();
        System.out.println("After coordinates");

        // check if loc in image!!!! important
        Location loc = new Location(mx, my);
        if ((loc.getX() >= 0 && loc.getX() < WIDTH) && (loc.getY() >= 0 && loc.getY() < HEIGHT)) {
            showDescriptions(loc);
        }
        StdDraw.text(WIDTH / 5, HEIGHT,
                    "Collect all the flowers before the time runs out!");
        StdDraw.text(WIDTH / 5, HEIGHT - 1,
                "Collect 3 hearts to extend your time!");
        StdDraw.text(WIDTH * 4 / 5, HEIGHT - 1,
                    "Health: " + HEALTH);
        StdDraw.text(WIDTH / 2, HEIGHT - 1,
                    "Time Left: " + TIMELEFT);
        StdDraw.show();
        }

    private void showDescriptions(Location loc) {
        int mx = loc.getX();
        int my = loc.getY();

        if (world[mx][my].equals(Tileset.WALL)) {
            ter.renderFrame(world);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2, 1, "A wall! Nothing interesting there.");
        } else if (world[mx][my].equals(Tileset.AVATAR)) {
            ter.renderFrame(world);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2, 1, "That's you! Look at you go!");
        } else if (world[mx][my].equals(Tileset.FLOOR)) {
            ter.renderFrame(world);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2, 1, "The floor! Nothing interesting there.");
        } else if (world[mx][my].equals(Tileset.HEART)) {
            ter.renderFrame(world);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2, 1, "A heart! Collect it to gain health!");
        } else if (world[mx][my].equals(Tileset.KEY)) {
            ter.renderFrame(world);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2, 1, "A flower! Collect it!");
        } else {
            ter.renderFrame(world);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2, 1, "Absolutely nothing.");
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