package core;

import saveload.SaveLoad;
import tileengine.TETile;
import tileengine.Tileset;

public class AutograderBuddy {

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) {
        // init the file name
        String fileName = "saveAndLoad.txt";
        World world = new World(12345L);
        char[] inputs = input.toCharArray();

        for (int i = 0; i < inputs.length; i++) {
            char currInput = inputs[i];

            if (currInput == 'n') {
                Long seed = getSeed(input);
                world = new World(seed);
            } else if (currInput == 'l') {
                world = SaveLoad.loadWorldFromFile(fileName);
            }

            switch (currInput) {
                case 'w', 'a', 's', 'd', 'g', 't' -> world.getAvatar().handleInputManual(currInput);
                default -> {
                }
            }

            // at the very end, check if it ends in a quit. if so, save then quit accordingly
            if (currInput == ':' && inputs[i + 1] == 'q') {
                SaveLoad.saveWorldToFile(world, fileName);
                return world.baseWorld;
            }
        }
        return world.baseWorld;
    }

    public static long getSeed(String input) {
        int startIndex = input.indexOf('n') + 1;
        int endIndex = input.indexOf('s');

        // Check that the seed starts with N and ends with S
        if (startIndex <= 0 || endIndex <= 0 || startIndex >= endIndex) {
            return 0;
        }

        // substring inclusive of start, exclusive of end
        String seedNumber = input.substring(startIndex, endIndex);

        // convert extracted number string to Long
        return Long.parseLong(seedNumber);
    }

    public static long inputString(String input) {
        int startIndex = input.indexOf('n') + 1;
        int endIndex = input.indexOf('s');

        // Check that the seed starts with N and ends with S
        if (startIndex <= 0 || endIndex <= 0 || startIndex >= endIndex) {
            return 0;
        }

        // substring inclusive of start, exclusive of end
        String seedNumber = input.substring(startIndex, endIndex);

        // convert extracted number string to Long
        return Long.parseLong(seedNumber);
    }

    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.FLOOR.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }
}
