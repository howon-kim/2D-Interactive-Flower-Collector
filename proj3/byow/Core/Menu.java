package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Menu {

    private static final int WIDTH = Engine.WIDTH;
    private static final int HEIGHT = Engine.HEIGHT;
    private static final int MENUW = Engine.MENUW;
    private static final int MENUH = Engine.MENUH;

    public static void initializeGUI() {
        makeGUIBackground();
        makeGUI();
        StdDraw.show();
        StdDraw.enableDoubleBuffering();
    }
    public static void makeGUIBackground() {
        StdDraw.clear();
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(MENUW * 16, MENUH * 16);
        Font font = new Font("Comic Sans Ms", Font.BOLD, 100);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, MENUW);
        StdDraw.setYscale(0, MENUH);
        StdDraw.clear(Color.BLACK);
    }

    public static void makeGUI() {
        Font title = new Font("Comic Sans Ms", Font.BOLD, 30);
        Font mainMenu = new Font("Comic Sans Ms", Font.PLAIN, 20);
        StdDraw.setFont(title);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(MENUW / 2, MENUH * 2 / 2.5, "Flower Collector");
        StdDraw.setFont(mainMenu);
        StdDraw.text(MENUW / 2, MENUH * 5.5 / 10, "New World (n)");
        StdDraw.text(MENUW / 2, MENUH * 4.5 / 10, "Load World (l)");
        StdDraw.text(MENUW / 2, MENUH * 3.5 / 10, "Quit (q)");
    }

    public static void makeCustomMessageScreen(String string) {
        int x = MENUW / 2;
        int y = MENUH * 2 / 3;
        StdDraw.clear();
        StdDraw.clear(Color.black);
        Font bigFont = new Font("Comic Sans Ms", Font.BOLD, 25);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(x, y, string);
        StdDraw.show();
    }

    public static void makeHUD() {
        StdDraw.filledRectangle(0.0, HEIGHT, 10, 10);
        StdDraw.show();
    }

    public static void enterSeedScreen() {
        StdDraw.text(MENUW / 2, MENUH / 4,
                "Let's generate a new world! Input a seed, then press 's' to begin.");
        StdDraw.show();
    }

    public static void displayEnteredSeed(String seed) {
        StdDraw.clear(Color.BLACK);
        Menu.makeGUI();
        StdDraw.text(MENUW / 2, MENUH / 4, "Your seed is: " + seed);
        StdDraw.show();
    }

    public static void lostScreen() {
        Menu.makeGUIBackground();
        Menu.makeCustomMessageScreen("You lost! You failed to collect all the flowers :(");
        StdDraw.pause(2000);
    }

    public static void winScreen() {
        Menu.makeGUIBackground();
        Menu.makeCustomMessageScreen("You did it! You collected all the flowers :)");
        StdDraw.pause(2000);
    }

    public static void collectedHeart(){
        StdDraw.textLeft(WIDTH / 2, HEIGHT - 1,
                "You've collected a heart and gained 5 seconds!");
        StdDraw.show();
    }

    public static void saveScreen(){
        Menu.makeGUIBackground();
        Menu.makeCustomMessageScreen("Your game has been saved!");
        StdDraw.pause(1500);
    }

    public static void gameOverScreen(){
        Menu.makeGUIBackground();
        Menu.makeCustomMessageScreen("Do you want to start over (y/n)?");
    }

    public static void endGameScreen(){
        Menu.makeGUIBackground();
        Menu.makeCustomMessageScreen("Thank you for playing!");
        StdDraw.show();
        StdDraw.pause(800);
        System.exit(0);
    }
}
