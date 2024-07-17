package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

public class Room {
    int roomHeight;
    int roomWidth;
    Random randomizer;
    TETile roomTile = Tileset.FLOOR;
    ArrayList<Position> corners;
    ArrayList<Door> doors;
    ArrayList<Integer> sides = new ArrayList<>(Arrays.asList(0, 1, 2, 3));

    /** Room constructor **/
    public Room(int height, int width, Position bottomLeft, Random random) {
        roomHeight = height;
        roomWidth = width;
        corners = new ArrayList<>();
        doors = new ArrayList<>();
        randomizer = random;

        corners.add(bottomLeft);
        corners.add(new Position(bottomLeft.x, bottomLeft.y + height)); // top left
        corners.add(new Position(bottomLeft.x + width, bottomLeft.y + height)); // top right
        corners.add(new Position(bottomLeft.x + width, bottomLeft.y)); // bottom right

        shuffle(sides);
    }

    // @source referenced and inspired by a Stack Overflow method to shuffle a list of items
    private void shuffle(ArrayList<Integer> inputSides) {
        for (int i = 0; i < inputSides.size(); i++) {
            int index = randomizer.nextInt(i + 1);
            // Swap elements at i and index
            int temp = inputSides.get(i);
            inputSides.set(i, inputSides.get(index));
            inputSides.set(index, temp);
        }
    }
}
