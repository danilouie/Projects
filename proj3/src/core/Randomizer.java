package core;

import java.util.Random;

public class Randomizer {
    int inputSeed; // this value is extracted from the N#####S input
    Random randomizer;

    /** constructor that takes in a string, extracts the seed, and sets it for generation **/
    public Randomizer(String seed) {
        inputSeed = inputString(seed);
        randomizer = new Random(inputSeed);
    }

    // input seed N***S -> parse to read numbers (inputted as string)
    public int inputString(String input) {
        int startIndex = input.indexOf('N') + 1;
        int endIndex = input.indexOf('S');

        // Check that the seed starts with N and ends with S
        if (startIndex <= 0 || endIndex <= 0 || startIndex >= endIndex) {
            return 0;
        }

        // substring inclusive of start, exclusive of end
        String seedNumber = input.substring(startIndex, endIndex);

        // convert extracted number string to integer
        return Integer.parseInt(seedNumber);
    }

    /** a method that generates a random int that is within a given bound.
     Used for generating random dimensions like height, width, and length
     for rooms and hallways **/
    public int randomizeDimensions(int bound) {
        return randomizer.nextInt(bound);
    }

    public Position randomizePos(int xBound, int yBound) {
        int x = randomizer.nextInt(xBound);
        int y = randomizer.nextInt(yBound);
        return new Position(x, y);
    }

    public int randomizeInstances(int bound) {
        return randomizer.nextInt(bound);
    }
}

