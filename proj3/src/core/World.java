package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class World {

    // build your own world!
    private WorldGenerator wg;
    public static long seed;
    TETile[][] baseWorld;
    Random randomizer;
    private static Avatar avatar;
    TETile tileUnder;
    private boolean isGameOver;
    private final TERenderer ter = new TERenderer();
    private static final int LINE_OF_SIGHT_RADIUS = 3;
    private final ArrayList<LightSources> lightPositions;
    boolean lightsOn = false;

    public World(Long seed) { // a placeholder for now until inputString methods are expanded
        wg = new WorldGenerator(seed);
        this.seed = seed;
        randomizer = wg.randomizer;
        baseWorld = wg.getWorld();

        initializeAvatar();
        lightPositions = wg.getLights();

        // set line of sight to be a certain size
        LineOfSight lineOfSight = new LineOfSight(avatar, LINE_OF_SIGHT_RADIUS);

        tileUnder = baseWorld[avatar.getX()][avatar.getY()];

        StdDraw.enableDoubleBuffering();
        // runGame();
    }

    public TETile[][] getBaseWorld() {
        return baseWorld;
    }

    public boolean isLightsOn() {
        return lightsOn;
    }

    public ArrayList<LightSources> getLights() {
        return wg.lightsArray;
    }

    public long getSeed() {
        return seed;
    }

    public static Avatar getAvatar() {
        return avatar;
    }

    // updates map based on avatar movement
    // initialize the avatar in the world
    private void initializeAvatar() {
        Room randomRoom = getRandomRoom();
        avatar = new Avatar(randomRoom, randomizer, this);
    }

    // gets the random room that the avatar will be initialized in
    private Room getRandomRoom() {
        ArrayList<Room> roomList = new ArrayList<>(wg.rooms);
        return roomList.get(randomizer.nextInt(roomList.size()));
    }


    // update avatar position as it moves throughout the board
    private void updateAvatarPosition() {
        // clear the previous avatar position
        baseWorld[avatar.getX()][avatar.getY()] = tileUnder;

        // update the avatar's position in the world based on movement
        avatar.handleInput();

        // store the tile that the avatar is going to "step" on
        tileUnder = baseWorld[avatar.getX()][avatar.getY()];

        // draw the new avatar position
        baseWorld[avatar.getX()][avatar.getY()] = avatar.getTile();
    }

    private void toggleLights() {
        // switch between tileset for lights on and off based on input
        for (LightSources light : lightPositions) {
            light.handleLightInput();
        }
    }

    public void runGame() {

        // initialize outside -> don't repeatedly initialize the world
        ter.initialize(baseWorld.length, baseWorld[0].length);

        toggleLights();

        // spawn avatar first
        updateAvatarPosition();


        // render after movement
        ter.renderFrame(this);
        StdDraw.show();
        StdDraw.pause(100);

        // when all the grass tiles have been changed to flower tiles the locked door unlocks
        // game ends when avatar stands on unlocked door tile
        while (!isGameOver()) {
            // also handles toggling of other things (line of sight and lights)
            updateAvatarPosition();

            // checks if door should open
            doorOpens();

            // if line of sight is active, should only be able to render the tiles nearby
            if (avatar.isLineOfSightActive()) {
                // Render with line of sight
                ter.renderFrameLineOfSight(this, avatar.getX(), avatar.getY(), LINE_OF_SIGHT_RADIUS);
            } else {
                // Render without line of sight
                ter.renderFrame(this);
            }

            // show the rendered frame (double buffering)
            StdDraw.show();

            // control the speed of the game
            StdDraw.pause(100);
        }
    }

    private void doorOpens() {
        if (avatar.getGrassTileCount() == 0) {
            for (int x = 0; x < baseWorld.length; x++) {
                for (int y = 0; y < baseWorld[0].length; y++) {
                    if (baseWorld[x][y] == Tileset.LOCKED_DOOR) {
                        baseWorld[x][y] = Tileset.UNLOCKED_DOOR;
                    }
                }
            }
        }
    }

    public static int getGrassTileCount() {
        return avatar.getGrassTileCount();
    }

    public static int getFlowerTileCount() {
        return avatar.getFlowerTileCount();
    }

    public boolean lineOfSightActivated() {
        return avatar.isLineOfSightActive();
    }

    private boolean isGameOver() {
        if (tileUnder == Tileset.UNLOCKED_DOOR) {
            isGameOver = true;
        }
        return isGameOver;
    }
}
