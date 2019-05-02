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

    /** GLOBAL SCREEN SIZE COMPONENT **/
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int MENUW = 40;
    public static final int MENUH = 60;
    private long SEED;
    private Random RANDOM;


    /* For "Game" Mechanics */
    private TETile[][] world;
    private Location player;
    private ArrayList<Location> keys;

    /* Default Setting */
    private static boolean GAMEOVER = false;
    private int HEALTH = 0;
    private int FLOWERS;
    private int TIMELEFT = 60;
    private String message = null;

    /** FILE SAVING COMPONENT **/
    private String input = "";

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {

        Menu.initializeGUI();

        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            switch (Character.toLowerCase(StdDraw.nextKeyTyped())) {
                /** NEW GAME OPERATION **/
                case ('n'): {
                    playTheNewWorld();
                    break;
                }

                /** LOAD OPERATION **/
                case ('l'): {
                    loadTheWorld();
                    break;
                }

                /** QUIT OPERATION **/
                case ('q'): {
                    /* Quit Operations */
                    GAMEOVER = true;
                    System.exit(0);
                    break;
                }
                //default:
                //   return;
            }
        }
    }

    /** INITIALIZE THE WORLD **/
    public void initializeWorld() {
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);

        GAMEOVER = false;
        HEALTH = 0;
        FLOWERS = 0;
        TIMELEFT = 30;
        message = null;

        world = WorldGenerator.generateWorld(SEED);
        player = makePlayer();
        putHearts();
        putNotes();
        keys = makeKeys();
        displayKeys();
        ter.renderFrame(world);
    }

    /** PLAY NEW WORLD **/
    public void playTheNewWorld(){
        Menu.enterSeedScreen();
        String seed = "";
        while(true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (key == 's') {
                setupSeed(seed);
                input = SEED + " ";
                initializeWorld();
                break;
            } else if (Character.isDigit(key)) {
                seed += key;
            } else {
                // Ignore the input
            }
            Menu.displayEnteredSeed(seed);
        }
        System.out.println("Let's play the game");
        playWorld(world);
    }

    public void loadTheWorld(){
        input = loadWorld();
        setupSeed(input.substring(0, input.indexOf(" ")));
        String move = input.substring(input.indexOf(" ") + 1, input.length());

        /* DEBUG PURPOSE **
        System.out.println(input);
        System.out.println("Seed :" + SEED);
        System.out.println("MOVE :" + move);
        **/

        initializeWorld();
        replay(move);
        playWorld(world);
    }

    /** SETUP THE SEED **/
    public void setupSeed(String seed) {
        SEED = Long.parseLong(seed);
        RANDOM = new Random(SEED);
        System.out.println("## SEED: " + SEED);
    }

    /** REPLAY **/
    public void replay(String move) {
        for (int i = 0; i < move.length(); i++) {
            move(move.charAt(i));
            /* DEBUG PURPOSE **
            System.out.println(player.getX() + " " + player.getY());
            **/
            ter.renderFrame(world);
            StdDraw.pause(100);
        }
        HEALTH = 0;
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
                world[l.getX()][l.getY()] = Tileset.HEART;
            }
        }
    }

    public void putNotes() {
        int numNotes = 0;
        int index;
        ArrayList<Location> notes = new ArrayList<>();
        for (Room room: Room.rooms) {
            if (numNotes < 3) {
                int x = room.getCenterX();
                int y = room.getCenterY();
                Location loc = new Location(x, y);
                notes.add(loc);
                numNotes += 1;
            }
        }
        for (Location l: notes) {
            world[l.getX()][l.getY()] = Tileset.NOTE;
        }
    }


    public Location makePlayer() {
        int index = RANDOM.nextInt(Room.getRooms().size());
        Room room = (Room) Room.getRooms().get(index);
        int x = room.getCenterX();
        int y = room.getCenterY();
        Location p = new Location(x, y);
        world[p.getX()][p.getY()] = Tileset.AVATAR;
        System.out.println("Set Avatar");
        return p;
    }


    public ArrayList<Location> makeKeys() {
        int index;
        int numRoom = Room.getRooms().size();

        if(numRoom <= 10){
            FLOWERS = 1;
        } else {
            FLOWERS = numRoom / 10;
        }
        ArrayList<Location> keys = new ArrayList<>();
        for(int i = 1; i <= FLOWERS; i++) {
            index = RANDOM.nextInt(Room.getRooms().size());
            Room room = (Room) Room.getRooms().get(index);
            int x = room.getCenterX();
            int y = room.getCenterY();
            Location loc = new Location(x, y);

            if (world[x][y] != Tileset.AVATAR && world[x][y] != Tileset.KEY) {
                keys.add(loc);
            } else {
                i -= 1;
            }
        }
        return keys;
    }

    public void displayKeys() {
        for (Location loc: keys) {
            world[loc.getX()][loc.getY()] = Tileset.KEY;
        }
    }

    private void playWorld(TETile[][] w) {
        Thread worker = new Thread(() -> {
            while (TIMELEFT > 0) {
                StdDraw.enableDoubleBuffering();
                TIMELEFT--;
                if (TIMELEFT == 0) {
                    GAMEOVER = true;
                }
                if (TIMELEFT == 20) {
                    world[player.getX()][player.getY()] = Tileset.NEWAVATAR;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }}});

        worker.start();
        char key;
        String move = "";

        while (!GAMEOVER) {
            if (message != null) {
                StdDraw.text(WIDTH / 5, 1, "Zen Message:" +message);
                StdDraw.show();
            }
            mouseHover();
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            key = Character.toLowerCase(StdDraw.nextKeyTyped());
            move += key;

            if (TIMELEFT == 0) {
                GAMEOVER = true;
                Menu.lostScreen();
            }

            if (FLOWERS == 0) {
                Menu.winScreen();
                GAMEOVER = true;
                break;
            }

            if (HEALTH == 1) {
                TIMELEFT += 5;
                HEALTH = 0;
                Menu.collectedHeart();
            }




            /** FOR QUIT **/
            /* DEBUG PURPOSE **
            System.out.println(move);
            **/
            for (int i = 0; i < move.length() - 1; i += 1) {
                if (move.charAt(i) == ':' && move.charAt(i + 1) == 'q') {
                    input += move.substring(0, i);
                    saveWorld(input);
                    GAMEOVER = true;
                }
            }

            move(key);
        }

        if(GAMEOVER) {
            Menu.gameOverScreen();
            while (true) {
                if (!StdDraw.hasNextKeyTyped()) {
                    continue;
                }
                char input = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (input == 'y') {
                    worker.stop();
                    interactWithKeyboard();
                } else if(input == 'n') {
                    //Menu.lostScreen();
                    Menu.endGameScreen();
                } else {
                    // Nothing happens
                }
            }
        }
    }



    private void eatFood(Location obj_e) {
        if (world[obj_e.getX()][obj_e.getY()] == Tileset.HEART) {
            HEALTH += 1;
        }
        if (world[obj_e.getX()][obj_e.getY()] == Tileset.KEY) {
            FLOWERS -= 1;
        }
        if (world[obj_e.getX()][obj_e.getY()] == Tileset.NOTE) {
            message = chooseMessage();
        }
        if (world[obj_e.getX()][obj_e.getY()] != Tileset.WALL) {
            if (TIMELEFT <= 20) {
                world[obj_e.getX()][obj_e.getY()] = Tileset.NEWAVATAR;
            } else {
                world[obj_e.getX()][obj_e.getY()] = Tileset.AVATAR;
            }
            world[player.getX()][player.getY()] = Tileset.FLOOR;
            player = obj_e;
        }
    }

    private String chooseMessage() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("Look at you, working those keys!");
        strings.add("You smell great today, you know that?");
        strings.add("Keep your eyes on the prize!");
        strings.add("What a time it is to be alive!");
        strings.add("You should get boba after this! You deserve it.");
        strings.add("Keep it up, soldier!");
        return strings.get(RANDOM.nextInt(5));
    }


    private void move(char key) {
        Location newplayerlocation = new Location(player.getX(), player.getY());
        switch (key) {
            case ('w'): {
                newplayerlocation = new Location(player.getX(), player.getY() + 1);
                break;
            }
            case ('s'): {
                newplayerlocation = new Location(player.getX(), player.getY() - 1);
                break;
            }
            case ('a'): {
                newplayerlocation = new Location(player.getX() - 1, player.getY());
                break;
            }
            case ('d'): {
                newplayerlocation = new Location(player.getX() + 1, player.getY());
                break;
            }
            default:
                return;
        }
        eatFood(newplayerlocation);
    }

    private void mouseHover() {
        //System.out.println("Before coordinates");
        int mx = (int) StdDraw.mouseX();
        int my = (int) StdDraw.mouseY();
        //System.out.println("After coordinates");

        // check if loc in image!!!! important
        Location loc = new Location(mx, my);
        if ((loc.getX() >= 0 && loc.getX() < WIDTH) && (loc.getY() >= 0 && loc.getY() < HEIGHT)) {
            showDescriptions(loc);
        }
        StdDraw.textLeft(5, HEIGHT - 1,
                "Collect all the flowers before the time runs out!");
        StdDraw.textLeft(5, HEIGHT - 2,
                "Collect hearts to extend your time!");
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
            StdDraw.text(WIDTH * 4 / 5, 1, "wall");
            StdDraw.text(WIDTH / 2, 1, "A wall! Nothing interesting there.");
        } else if (world[mx][my].equals(Tileset.AVATAR)) {
            ter.renderFrame(world);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH * 4 / 5, 1, "you");
            StdDraw.text(WIDTH / 2, 1, "That's you! And you're running out of time!");
        } else if (world[mx][my].equals(Tileset.NEWAVATAR)) {
            ter.renderFrame(world);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH * 4 / 5, 1, "you");
            StdDraw.text(WIDTH / 2, 1, "That's you! Look at you go!");
        } else if (world[mx][my].equals(Tileset.FLOOR)) {
            ter.renderFrame(world);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH * 4 / 5, 1, "floor");
            StdDraw.text(WIDTH / 2, 1, "The floor! Nothing interesting there.");
        } else if (world[mx][my].equals(Tileset.HEART)) {
            ter.renderFrame(world);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH * 4 / 5, 1, "heart");
            StdDraw.text(WIDTH / 2, 1, "A heart! Collect it to extend your time!");
        } else if (world[mx][my].equals(Tileset.KEY)) {
            ter.renderFrame(world);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH * 4 / 5, 1, "flower");
            StdDraw.text(WIDTH / 2, 1, "A flower! Collect it!");
        } else if (world[mx][my].equals(Tileset.NOTE)) {
            ter.renderFrame(world);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH * 4 / 5, 1, "zen message");
            StdDraw.text(WIDTH / 2, 1, "A calming message for you!");
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
            input = loadWorld();

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
                move(move.charAt(i));
                //System.out.println(worldlocs.player().getX() + " " +  worldlocs.player().getY());
            }
        }

        if (save != -1) {
            saveWorld(input);
        }
        return world;
    }

    private String loadWorld() {
        File file = new File("./save_data.txt");
        if (file.exists()) {
            try {
                FileInputStream fs = new FileInputStream(file);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (String) os.readObject();
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
        playTheNewWorld();
        return null;
    }

    private void saveWorld(String data) {
        File file = new File("./save_data.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(data);
            Menu.saveScreen();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}

/** TRASH CAN
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
 **/