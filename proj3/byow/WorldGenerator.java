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


    public static void main(String [] args) {
        TERenderer ter = new TERenderer();

        Engine engine = new Engine();
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
        Scanner input = new Scanner(System.in);
        System.out.print("Enter text: ");
        String userInput = input.next();
        System.out.println("Text entered = " +  userInput);

        userInput = userInput.substring(1, userInput.length() - 1);
        System.out.println(userInput);

        ter.renderFrame(engine.interactWithInputString(userInput));
    }
}
