package tileengine;

import core.*;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class TERenderer {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;
    private Position hoveredPosition;

    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);      
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initialization method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *                     
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void renderFrame(World world) {
        int numXTiles = world.getBaseWorld().length;
        int numYTiles = world.getBaseWorld()[0].length;
        StdDraw.clear(new Color(0, 0, 0));

        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                // check if light should be illuminated (validity of spot)
                boolean illuminated = isIlluminated(world, new Position(x, y));

                // if tile is in valid illuminated spot and light is on -> should be the light on tile
                if (illuminated) {
                    lightOpacity(world, new Position(x, y));
                } else {
                    // tile is not in valid spot
                    world.getBaseWorld()[x][y].draw(x + xOffset, y + yOffset);
                }
            }
        }

        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world.getBaseWorld()[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }

                // draw the avatar on top
                if (world.getBaseWorld()[x][y].equals(Tileset.AVATAR)) {
                    world.getBaseWorld()[x][y].draw(x + xOffset, y + yOffset);
                }
            }
        }
        if (hoveredPosition != null) {
            drawTileInfo(world, hoveredPosition);
        }
        displayGrassAndFlowerCount();
        displayTimeAndDate();
        handleMouseHover();
        StdDraw.show();
    }

    // boolean for checking if the tile is [affected] by the light
    private boolean isIlluminated(World world, Position position) {
        // iterate through the static list of lights in the WorldGenerator class
        for (LightSources light : world.getLights()) {

            // calculates distance between light source and current position that it's checking
            int distance = LightSources.distanceCalc(light.getPosition(), position);

            // check if the tile is within the illuminated radius of the light source
            // if within radius and light is on -> return true
            if (distance <= light.getRadius() && light.isLightsOn()) {
                if (!checkWallOrWater(world.getBaseWorld(), position.posX(), position.posY())) {
                    return true;
                }
            }
        }
        // not within radius, or light is off
        return false;
    }

    private void lightOpacity(World world, Position position) {
//        TETile tile = Tileset.FLOOR;
        // iterate through the static list of lights in the WorldGenerator class
        for (LightSources light : world.getLights()) {

            // calculates distance between light source and current position that it's checking
            int distance = LightSources.distanceCalc(light.getPosition(), position);

            if (light.isLightsOn()) {

                // define gradient percentages based on distance - default is 100&
                double gradientPercentage = 1.0;

                // adjust gradient based on distance
                switch (distance) {
                    case 0 ->
                            Tileset.LIGHT_ON.draw(position.posX() + xOffset, position.posY() + yOffset);
                    case 1 ->
                            Tileset.LIGHT_CAST1.draw(position.posX() + xOffset, position.posY() + yOffset);
                    case 2 ->
                            Tileset.LIGHT_CAST2.draw(position.posX() + xOffset, position.posY() + yOffset);
                    case 3 ->
                            Tileset.LIGHT_CAST3.draw(position.posX() + xOffset, position.posY() + yOffset);
                    case 4 ->
                            Tileset.LIGHT_CAST4.draw(position.posX() + xOffset, position.posY() + yOffset);

                }
            }
        }
    }

    private boolean checkWallOrWater(TETile[][] world, int x, int y) {
        // check if the tile at the specified position is a wall or water
        // also need to check for sand tile, so it doesn't change it
        return world[x][y] == Tileset.WALL ||
                world[x][y] == Tileset.NOTHING ||
                world[x][y] == Tileset.GRASS ||
                world[x][y] == Tileset.FLOWER ||
                world[x][y] == Tileset.LOCKED_DOOR;
    }

    public void renderFrameLineOfSight(World world, int avatarX, int avatarY, int radius) {

        // only show tiles around the avatar (radius from the center)
        int startX = Math.max(0, avatarX - radius);
        int endX = Math.min(width, avatarX + radius + 1);
        int startY = Math.max(0, avatarY - radius);
        int endY = Math.min(height, avatarY + radius + 1);

        // black color to cover everything not in line of sight
        StdDraw.clear(new Color(0, 0, 0));

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                boolean illuminated = isIlluminated(world, new Position(x, y));

                // if tile is in valid illuminated spot and light is on -> should be the light on tile
                if (illuminated) {
                    lightOpacity(world, new Position(x, y));
                } else {
                    // tile is not in valid spot
                    world.getBaseWorld()[x][y].draw(x + xOffset, y + yOffset);
                }

                if (world.getBaseWorld()[x][y].equals(Tileset.AVATAR)) {
                    world.getBaseWorld()[x][y].draw(x + xOffset, y + yOffset);
                }
            }
        }
        if (hoveredPosition != null) {
            drawTileInfo(world, hoveredPosition);
        }
        displayTimeAndDate();
        displayGrassAndFlowerCount();
        handleMouseHover();
        StdDraw.show();
    }

    // handling when mouse hovers over the tile
    private void handleMouseHover() {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();

        // converting mouse coordinates to tile coordinates
        int tileX = mouseX - xOffset;
        int tileY = mouseY - yOffset;

        // makes sure that the mouse is within the world bounds
        if (tileX >= 0 && tileX < width && tileY >= 0 && tileY < height) {
            Position currHoveredPosition = new Position(tileX, tileY);

            if (!currHoveredPosition.equals(hoveredPosition)) {

                // update the hovered position
                hoveredPosition = currHoveredPosition;
            }
        }
    }

    private void drawTileInfo(World world, Position position) {
        TETile tile;

        // makes sure that tile type reflects the tile change when lights are turned on
        if (world.isLightsOn()) {

            // tile starts out as what it originally was
            TETile tempTile = world.getBaseWorld()[position.posX()][position.posY()];

            for (LightSources light : world.getLights()) {
                int distance = LightSources.distanceCalc(light.getPosition(), position);
                if (distance == 0) {
                    tempTile = Tileset.LIGHT_ON;
                    break;
                } else if (distance == 1) {
                    tempTile = Tileset.LIGHT_CAST1;
                    break;
                } else if (distance == 2) {
                    tempTile = Tileset.LIGHT_CAST2;
                    break;
                } else if (distance == 3) {
                    tempTile = Tileset.LIGHT_CAST3;
                    break;
                } else if (distance == 4) {
                    tempTile = Tileset.LIGHT_CAST4;
                    break;
                }
            }
            tile = tempTile;
        } else {
            tile = world.getBaseWorld()[position.posX()][position.posY()];
        }

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(0.5, height - 1, "Tile type: " + tile.description());
    }

    // display date and time on hud
    private void displayTimeAndDate() {

        // date
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE MMM dd, yyyy");
        String dateFormatted = date.format(dateFormatter);

        // time
        LocalTime time = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        String timeFormatted = time.format(timeFormatter);


        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(68, height - 1, "Date: " + dateFormatted);
        StdDraw.textLeft(82, height - 1, "Time: " + timeFormatted);
    }

    private void displayGrassAndFlowerCount() {
        int grassCount = World.getGrassTileCount();
        int flowerCount = World.getFlowerTileCount();

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.textLeft(40, height - 1, "Grass Count: " + grassCount);

        StdDraw.setPenColor(StdDraw.PINK);
        StdDraw.textLeft(50, height - 1, "Flower Count: " + flowerCount);
    }
}
