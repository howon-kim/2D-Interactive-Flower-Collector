package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Menu {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static long SEED;
    private static final int MENUW = 40;
    private static final int MENUH = 60;

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
        int y = MENUH / 2;
        StdDraw.clear();
        StdDraw.clear(Color.black);
        Font bigFont = new Font("Comic Sans Ms", Font.BOLD, 25);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.red);
        StdDraw.text(x, y, string);
        StdDraw.show();
    }

    public static void makeHUD() {
        StdDraw.filledRectangle(0.0, HEIGHT, 10, 10);
        StdDraw.show();
    }
}
