package byow;


import byow.Core.Engine;
import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.RandomInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Scanner;

public class WorldGenerator {

    static Engine engine = new Engine();

    public static void main(String [] args) {

        /**
        InputSource inputSource = new KeyboardInputSource();
        String userInput = "";
        boolean start = false;
        while (inputSource.possibleNextInput())
        {
            char c = inputSource.getNextKey();
            if (c == 'N' || c == 'n') {
                System.out.println("start to record number");
                start = true;
            }
            if(start){
                userInput += c;
            }
            if(c == 'S' || c == 's'){
                System.out.println(userInput);
                break;
            }
        }**/
        /*
        Scanner input = new Scanner(System.in);
        //System.out.print("Enter text: ");
        String userInput = input.next();
        //System.out.println("Text entered = " +  userInput);

        userInput = userInput.substring(1, userInput.length() - 1);
        //System.out.println(userInput);
        TETile[][] world = engine.interactWithInputString(userInput);

         */
        TETile[][] world = engine.interactWithInputString("n5197880843569031643s");

        TERenderer ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
        ter.renderFrame(world);


    }
}
