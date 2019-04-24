package byow;


import byow.Core.Engine;
import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.RandomInputSource;
import byow.InputDemo.StringInputDevice;

public class WorldGenerator {


    public static void main(String [] args) {
        Engine engine = new Engine();
        InputSource inputSource = new KeyboardInputSource();
        String userInput = "";
        boolean start = false;
        while (inputSource.possibleNextInput())
        {
            char c = inputSource.getNextKey();
            if (c == 'N') {
                System.out.println("start to record number");
                start = true;
            }
            if(start){
                userInput += c;
            }
            if(c == 'S'){
                System.out.println(userInput);
                break;
            }
        }
        userInput = userInput.substring(1, userInput.length() - 1);
        System.out.println(userInput);

        engine.interactWithInputString(userInput);
    }
}
