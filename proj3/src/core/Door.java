package core;

import java.util.ArrayList;
import java.util.Random;

// no style issues
public class Door {
    Door destinationDoor;
    boolean hasHallway;
    Position location;
    Random randomizer;
    Room parentRoom;
    int side;

    public Door(Room assignedRoom, Random random, int assignedSide) {
        parentRoom = assignedRoom;
        randomizer = random;
        side = assignedSide;

        location = randomEdge(parentRoom.corners);

        destinationDoor = null;
        hasHallway = false;
    }

    /** based on the assigned SIDE, generates a random position along that edge of a room **/
    private Position randomEdge(ArrayList<Position> corners) {
        int startX;
        int endX;
        int startY;
        int endY;
        Position result = null;

        if (side == 0) { // Top
            startX = corners.get(1).x + 1; // top left x coordinate
            endX = corners.get(2).x - 1; // top right x coordinate
            result = new Position(randomizer.nextInt(endX - startX + 1) + startX, corners.get(1).y);
        } else if (side == 2) { // Bottom
            startX = corners.get(0).x + 1; // bottom left x coordinate
            endX = corners.get(3).x - 1; // bottom right x coordinate
            result = new Position(randomizer.nextInt(endX - startX + 1) + startX, corners.get(0).y);
        } else if (side == 1) { // Right
            startY = corners.get(3).y + 1; // bottom right y coordinate
            endY = corners.get(2).y - 1; // top right y coordinate
            result = new Position(corners.get(3).x, randomizer.nextInt(endY - startY + 1) + startY);
        } else if (side == 3) { // Left
            startY = corners.get(0).y + 1; // bottom left y coordinate
            endY = corners.get(1).y - 1; // top left y coordinate
            result = new Position(corners.get(0).x, randomizer.nextInt(endY - startY + 1) + startY);
        }
        return result;
    }
}
