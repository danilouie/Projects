package core;
import edu.princeton.cs.algs4.StdDraw;
import saveload.SaveLoad;
import utils.FileUtils;

import java.awt.*;

public class Main {
    private static final String SAVE_FILENAME = "saveAndLoad.txt";
    private static String input;
    private static boolean saved;
    private static boolean gameRunning;

    public static void main(String[] args) {
        // display main menu and have interactivity
        StdDraw.setCanvasSize(1080, 540);  // Adjust the size as needed
        StdDraw.setXscale(0, 90);
        StdDraw.setYscale(0, 45);

        StdDraw.clear(Color.BLACK);

        displayMainMenu();
    }

    private static void displayMainMenu() {
        // displays main menu until one of the options is met
        renderMainMenu();
        World world = null;
        System.out.println("display mainMenu. saved? " + saved);

        while (!gameRunning) {
            if (StdDraw.hasNextKeyTyped()) {
                char userInput = Character.toLowerCase(StdDraw.nextKeyTyped());
                input += userInput;
                if (input.endsWith(":q")) {
                    quit(world);
                }

                switch (userInput) {
                    case 'n' -> {
                        world = generateNewWorld();
                    }
                    case 'l' -> handleLoadWorld();
                    case ':' -> { }
                    default -> {
                        StdDraw.clear(Color.BLACK);
                        renderMainMenuTemplate("Invalid option. Please try again.");

                        StdDraw.pause(300);
                        StdDraw.clear(Color.BLACK);

                        displayMainMenu();
                    }
                }
            }
        }
    }

    private static void handleLoadWorld() {
        // only load a world if there is one saved and available
        System.out.println("saved? " + saved);
        String[] save = FileUtils.readFile(SAVE_FILENAME).split(" ");
        System.out.println(save[0] + save[1] + save[2] + save[3]);

        if (!emptySave(save)) {
            StdDraw.clear(Color.BLACK);
            renderMainMenuLoad("Loading world with saved data!");
            StdDraw.pause(750);

            // load the world with the saved data and run it
            World world = SaveLoad.loadWorldFromFile(SAVE_FILENAME);
            world.runGame();

        } else {
            StdDraw.clear(Color.BLACK);
            renderMainMenuLoad("No save file. :'3");

            StdDraw.pause(750);
            StdDraw.clear(Color.BLACK);

            displayMainMenu();
        }
    }

    /** returns true if the save being checked repreents a viable world to load **/
    private static boolean emptySave(String[] save) {
        Long seed = Long.valueOf(save[0]);
        String inputs = (save[1]);
        boolean sightToggle = Boolean.parseBoolean((save[2]));
        boolean lightToggle = Boolean.parseBoolean((save[3]));

        return (seed == 0 && inputs == null && !sightToggle && !lightToggle);
    }

    private static World generateNewWorld() {

        // need to prompt user to enter seed starting with N and ending with S
        String prompt = "Enter a seed for the new world, followed by an 'S': n";
        StdDraw.clear(Color.black);
        renderMainMenuNew(prompt);

        String seedInput = "";
        World world = null;

        while (!gameRunning) {
            if (StdDraw.hasNextKeyTyped()) {
                char userInput = Character.toLowerCase(StdDraw.nextKeyTyped());
                seedInput += userInput;
                System.out.println("seedInput: " + seedInput);

                switch (userInput) {
                    case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                        prompt += userInput;
                        StdDraw.clear(Color.BLACK);
                        renderMainMenuNew(prompt);
                    }
                    case 's' -> {

                        StdDraw.clear(Color.BLACK);
                        renderMainMenuNew(prompt + "s");
                        StdDraw.pause(700);

                        // convert the string seed into a usable Long
                        Long seedInputConverted = readSeed(seedInput);

                        // generate the world
                        world = new World(seedInputConverted);

                        // save world progress starting from the moment it generates?
                        SaveLoad.saveWorldToFile(world, SAVE_FILENAME);
                        System.out.println("calling saveWorld");
                        saved = true;

                        prompt = "Generating world with seed: " + seedInputConverted;
                        StdDraw.clear(Color.black);
                        renderMainMenuNew(prompt);

                        // run the game
                        gameRunning = true;
                        runWorld(world);

                    }
                    default -> {
                        StdDraw.clear(Color.BLACK);
                        renderMainMenuTemplate("Invalid option. Please try again.");

                        StdDraw.pause(300);
                        StdDraw.clear(Color.BLACK);

                        displayMainMenu();
                    }
                }
            }
        }
        return world;
    }

    public static long readSeed(String seedString) {
        int startIndex = 0;
        int endIndex = seedString.indexOf('s');

        // substring inclusive of start, exclusive of end
        String seedNumber = seedString.substring(startIndex, endIndex);

        // convert extracted number string to Long
        return Long.parseLong(seedNumber);
    }

    private static void runWorld(World inputWorld) {
        // run the game
        inputWorld.runGame();
    }

    private static void quit(World world) {
        if (world == null) {
            SaveLoad.saveWorldToFile(new World(0L), SAVE_FILENAME);
        } else {
            SaveLoad.saveWorldToFile(world, SAVE_FILENAME);
        }
        gameRunning = false;
        saved = true;
        System.out.println("quitting. saved? " + saved);
        menuQuit();
    }

    private static void menuQuit() {
        StdDraw.clear(Color.BLACK);
        renderMainMenuTemplate("Quitting game");
        StdDraw.pause(400);

        StdDraw.clear(Color.BLACK);
        renderMainMenuTemplate("Quitting game.");
        StdDraw.pause(400);

        StdDraw.clear(Color.BLACK);
        renderMainMenuTemplate("Quitting game..");
        StdDraw.pause(300);

        StdDraw.clear(Color.BLACK);
        renderMainMenuTemplate("Quitting game...");
        StdDraw.pause(300);

        StdDraw.clear(Color.BLACK);
        renderMainMenuTemplate("Goodbye!");
        StdDraw.pause(400);

        System.exit(0);
    }

    private static void renderMainMenu() {
        // draws out the main menu
        Font titleFont = new Font("Arial", Font.BOLD, 50);
        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(45, 32, "CS61B: THE GAME");

        Font menuFont = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(menuFont);
        StdDraw.text(45, 22, "New Game (N)");
        StdDraw.text(45, 17, "Load Game (L)");
        StdDraw.text(45, 12, "Quit Game (:Q)");

        StdDraw.show();
    }

    private static void renderMainMenuNew(String prompt) {
        // draws out the main menu
        Font titleFont = new Font("Arial", Font.BOLD, 50);
        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(45, 32, "CS61B: THE GAME");

        Font menuFont = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(menuFont);

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.text(45, 22, "New Game (N)");

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(45, 17, prompt);

        StdDraw.show();
    }

    private static void renderMainMenuLoad(String prompt) {
        // draws out the main menu
        Font titleFont = new Font("Arial", Font.BOLD, 50);
        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(45, 32, "CS61B: THE GAME");

        Font menuFont = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(menuFont);

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.text(45, 22, "Load Game (L)");

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(45, 17, prompt);

        StdDraw.show();
    }

    private static void renderMainMenuTemplate(String prompt) {
        // draws out the main menu
        Font titleFont = new Font("Arial", Font.BOLD, 50);
        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(45, 32, "CS61B: THE GAME");

        Font menuFont = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(menuFont);
        StdDraw.text(45, 17, prompt);

        StdDraw.show();
    }
}
