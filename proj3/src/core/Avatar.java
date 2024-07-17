package core;

import edu.princeton.cs.algs4.StdDraw;
import saveload.SaveLoad;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

public class Avatar {
    Position currPos;
    World refWorld;
    TETile[][] baseWorld;
    TETile tile = Tileset.AVATAR;
    String inputs;

    private boolean lineOfSightActive = false; // tracks if LineOfSightActive is being used or not
    private boolean lightsOn = false;
    private int grassTileCount;
    private int flowerTileCount;

    public Avatar(Room room, Random randomizer, World world) {
        // avatar generates in the middle of a random room
        initializePosition(room, randomizer);
        refWorld = world;
        baseWorld = refWorld.baseWorld;
        grassTileCount = WorldGenerator.NUM_GRASS_TILES;
    }

    private void initializePosition(Room room, Random randomizer) {
        // Set the initial position randomly in a room
        // subtracting 2 to avoid being placed too close to the walls
        int x = randomizer.nextInt(room.roomWidth - 2) + room.corners.get(0).x + 1;
        int y = randomizer.nextInt(room.roomHeight - 2) + room.corners.get(0).y + 1;

        // start off in spawned spot
        currPos = new Position(x, y);
    }

    private void grassGrowsToFlower() {
        // only can interact with grass tile if the avatar is standing one away from it
        // checks left, right, up, down
        if (isValidMove(currPos.x - 1, currPos.y) && baseWorld[currPos.x - 1][currPos.y] == Tileset.GRASS) {
            // changes the grass tile to a flower tile for the remainder of the game
            baseWorld[currPos.x - 1][currPos.y] = Tileset.FLOWER;
            grassTileCount--;
            flowerTileCount++;
        } else if (isValidMove(currPos.x + 1, currPos.y) && baseWorld[currPos.x + 1][currPos.y] == Tileset.GRASS) {
            baseWorld[currPos.x + 1][currPos.y] = Tileset.FLOWER;
            grassTileCount--;
            flowerTileCount++;
        } else if (isValidMove(currPos.x, currPos.y - 1) && baseWorld[currPos.x][currPos.y - 1] == Tileset.GRASS) {
            baseWorld[currPos.x][currPos.y - 1] = Tileset.FLOWER;
            grassTileCount--;
            flowerTileCount++;
        } else if (isValidMove(currPos.x, currPos.y + 1) && baseWorld[currPos.x][currPos.y + 1] == Tileset.GRASS) {
            baseWorld[currPos.x][currPos.y + 1] = Tileset.FLOWER;
            grassTileCount--;
            flowerTileCount++;
        }
    }

    public int getX() {
        return currPos.x;
    }

    public int getY() {
        return currPos.y;
    }

    public String getInputs() {
        return inputs;
    }

    public int getGrassTileCount() {
        return grassTileCount;
    }

    public int getFlowerTileCount() {
        return flowerTileCount;
    }

    public TETile getTile() {
        return tile;
    }

    public void moveUp() {
        if (isValidMove(currPos.x, currPos.y + 1)) {
            currPos.y++;
        }
    }

    public void moveDown() {
        if (isValidMove(currPos.x, currPos.y - 1)) {
            currPos.y--;
        }
    }

    public void moveLeft() {
        if (isValidMove(currPos.x - 1, currPos.y)) {
            // System.out.println("moving left");
            currPos.x--;
        }
    }

    public void moveRight() {
        if (isValidMove(currPos.x + 1, currPos.y)) {
            currPos.x++;
        }
    }

    public boolean isLineOfSightActive() {
        return lineOfSightActive;
    }

    public void setLineOfSightActive(boolean sightToggle) {
        this.lineOfSightActive = sightToggle;
    }

    public boolean isLightsOn() {
        return LightSources.lightsOn;
    }

    public void toggleLights() {
        LightSources.lightsOn = !LightSources.lightsOn;
    }

    public void setLightsOn(boolean lightToggle) {
        this.lineOfSightActive = lightToggle;
    }

    private boolean isValidMove(int x, int y) {
        // first check if move is within world bounds
        if (x >= 0 && x < WorldGenerator.WORLD_WIDTH && y >= 0 && y < WorldGenerator.WORLD_HEIGHT) {

            // then ensures that the next tile is not an invalid space (ex: wall, water, etc)
            if (baseWorld[x][y] != Tileset.WALL && baseWorld[x][y] != Tileset.WATER) {
                return baseWorld[x][y] != Tileset.LIGHT_ON && baseWorld[x][y] != Tileset.LIGHT_OFF;
            }
        }
        return false;
    }

    // toggling line of sight on and off
    public void toggleSight() {
        // equal to the opposite of itself
        // toggled when "g" is pressed"
        lineOfSightActive = !lineOfSightActive;
    }

    public void handleInput() {
        // user input for avatar movement using WASD
        // System.out.println("handling input");
        if (StdDraw.hasNextKeyTyped()) {
            char input = Character.toLowerCase(StdDraw.nextKeyTyped());
            inputs += input;
            if (inputs.endsWith(":q")) {
                quit();
            }

            System.out.println("input: " + input);
            // System.out.println();

            switch (input) {
                case 'a' -> moveLeft();
                case 's' -> moveDown();
                case 'd' -> moveRight();
                case 'w' -> moveUp();
                case 'g' -> toggleSight();
                case 't' -> toggleLights();
                case 'y' -> grassGrowsToFlower();
                default -> {
                }
            }
        }
    }

    public void handleInputManual(char input) {
        inputs += input;

        switch (input) {
            case 'a' -> moveLeft();
            case 's' -> moveDown();
            case 'd' -> moveRight();
            case 'w' -> moveUp();
            case 'g' -> toggleSight();
            case 't' -> toggleLights();
            case 'y' -> grassGrowsToFlower();
            default -> {
            }
        }
    }

    public void quit() {
        SaveLoad.saveWorldToFile(refWorld, "saveAndLoad.txt");
        System.exit(0);
    }
}
