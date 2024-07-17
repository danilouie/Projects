package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayList;
import java.util.Random;
public class WorldGenerator {

    Random randomizer;
    TETile worldTile = Tileset.NOTHING;
    static final int WORLD_WIDTH = 90;
    static final int WORLD_HEIGHT = 45;
    TETile[][] world = new TETile[WORLD_WIDTH][WORLD_HEIGHT];
    static final int NUM_GRASS_TILES = 20;

    ArrayList<Room> rooms;
    int minRoomSize = 4;
    int maxRoomSize = 10;
    int minRooms = 13;
    int maxRooms = 18;

    ArrayList<Door> doors;
    ArrayList<LightSources> lightsArray;

    /** the constructor **/
    public WorldGenerator(Long seed) {
        randomizer = new Random(seed);
        rooms = new ArrayList<>();
        doors = new ArrayList<>();
        lightsArray = new ArrayList<>();

        // change the appropriate world tiles
        for (int x = 0; x < WORLD_WIDTH; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                world[x][y] = worldTile;
            }
        }
        generateRooms();
        drawRooms();
        drawDoors();
        connectDoors();
        generateHallways();
        drawWalls();
        drawExit();
        drawLights();
        drawGrass();
    }

    public TETile[][] getWorld() {
        return world;
    }

    private void drawGrass() {
        int i = 0;
        while (i < NUM_GRASS_TILES) {
            int x = randomizer.nextInt(WORLD_WIDTH);
            int y = randomizer.nextInt(WORLD_HEIGHT);

            // makes sure that the position is valid for generating grass
            if (world[x][y] == Tileset.FLOOR) {
                if (world[x][y] == Tileset.FLOOR) {
                    world[x][y] = Tileset.GRASS;
                    i++;
                } else {
                    i--;
                }
            }
        }
    }

    private void drawLights() {
        // for simplicity of testing purposes, only change the tiles rn
        for (LightSources light : lightsArray) {
            // draw lights as off first
            world[light.getPosition().x][light.getPosition().y] = Tileset.LIGHT_OFF;
        }
    }

    public ArrayList<LightSources> getLights() {
        return lightsArray;
    }

    private void generateLightSources() {
        // generate one light source per room in a random spot
        for (Room room : rooms) {
            int x = room.corners.get(0).x + randomizer.nextInt(room.roomWidth - 1);
            int y = room.corners.get(0).y + randomizer.nextInt(room.roomHeight - 1);

            Position position = new Position(x, y);
            // add the position of the lights into the array
            lightsArray.add(new LightSources(position));
        }
    }

    private void drawExit() {
        Room room = rooms.get(rooms.size() - 1);
        Position bottomLeft = room.corners.get(0);

        int randomX = randomizer.nextInt(room.roomWidth);
        int randomY = randomizer.nextInt(room.roomHeight);

        Position pos = new Position(bottomLeft.x + randomX, bottomLeft.y + randomY);

        world[pos.x][pos.y] = Tileset.LOCKED_DOOR;
    }

    /** draws all the walls **/
    private void drawWalls() {
        for (int x = 1; x < WORLD_WIDTH - 1; x++) {
            for (int y = 1; y < WORLD_HEIGHT - 1; y++) {
                if (checkBlock(x, y) && world[x][y] == worldTile) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
    }

    /** checks if a block is on the edge of a room or hallway **/
    private boolean checkBlock(int x, int y) {

        Position topLeft = new Position(x - 1, y + 1);
        Position topMiddle = new Position(x, y + 1);
        Position topRight = new Position(x + 1, y + 1);

        Position left = new Position(x - 1, y);
        Position right = new Position(x + 1, y);

        Position bottomLeft = new Position(x - 1, y - 1);
        Position bottomMiddle = new Position(x, y - 1);
        Position bottomRight = new Position(x + 1, y - 1);

        if (world[topLeft.x][topLeft.y] == Tileset.FLOOR
                || world[topMiddle.x][topMiddle.y] == Tileset.FLOOR
                || world[topRight.x][topRight.y] == Tileset.FLOOR) {
            return true;
        }
        if (world[left.x][left.y] == Tileset.FLOOR || world[right.x][right.y] == Tileset.FLOOR) {
            return true;
        }
        return world[bottomLeft.x][bottomLeft.y] == Tileset.FLOOR
                || world[bottomMiddle.x][bottomMiddle.y] == Tileset.FLOOR
                || world[bottomRight.x][bottomRight.y] == Tileset.FLOOR;
    }

    /** draws all the hallways and changes the appropriate tiles **/
    private void generateHallways() {
        for (Door door : doors) {
            makeHallway(door);
        }
    }

    private void makeHallway(Door door) {
        Position start = door.location;
        Position end = door.destinationDoor.location;

        int dx = end.x - start.x;
        int dy = end.y - start.y;

        // if end is to the right of start
        if (dx > 0) {
            for (int x = start.x; x <= end.x; x++) {
                world[x][start.y] = Tileset.FLOOR;
            }
        } else if (dx < 0) {
            for (int x = start.x; x >= end.x; x--) {
                world[x][start.y] = Tileset.FLOOR;
            }
        }

        // if start is above end
        if (dy > 0) {
            for (int y = start.y; y <= end.y; y++) {
                world[end.x][y] = Tileset.FLOOR;
            }
        } else if (dy < 0) {
            for (int y = start.y; y >= end.y; y--) {
                world[end.x][y] = Tileset.FLOOR;
            }
        }

        door.hasHallway = true;
        door.destinationDoor.hasHallway = true;
    }

    /** connects all the doors **/
    private void connectDoors() {
        for (Door door : doors) {
            nearestDoor(door);
        }
    }

    private void nearestDoor(Door currDoor) {
        Door closest = doors.get(doors.size() - 1);
        double prev = closer(new Position(0, 0), currDoor.location);
        double curr;

        for (Door door : doors) {
            if (door != currDoor && door.destinationDoor == null) {
                curr = closer(currDoor.location, door.location);
                if (curr < prev) {
                    prev = curr;
                    closest = door;
                }
            }
        }
        currDoor.destinationDoor = closest;
        currDoor.destinationDoor.destinationDoor = currDoor;
    }

    private double closer(Position current, Position compare) {
        int dx = Math.abs(current.x - compare.x);
        int dy = Math.abs(current.y - compare.y);

        return Math.pow(dx, 2) + Math.pow(dy, 2);
    }


    /** method that draws the rooms by first generating a random # of rooms, then drawing each one **/
    private void drawRooms() {
        for (Room room : rooms) {
            Position currBottomLeft = room.corners.get(0);

            for (int x = currBottomLeft.x; x < currBottomLeft.x + room.roomWidth - 1; x++) {
                for (int y = currBottomLeft.y; y < currBottomLeft.y + room.roomHeight - 1; y++) {

                    // only change the tile if it is within the bounds of the world
                    if (x < WORLD_WIDTH - 2 && y < WORLD_HEIGHT - 2 && x > 2 && y > 2) {
                        world[x][y] = room.roomTile;
                    }
                }
            }
            room.corners.get(1).y = room.corners.get(1).y - 2; // reassigning top left corner
            room.corners.get(2).x = room.corners.get(2).x - 2;
            room.corners.get(2).y = room.corners.get(2).y - 2;
            room.corners.get(3).x = room.corners.get(3).x - 2;
        }
        drawLights();
    }

    private void generateRooms() {
        int numRooms = randomInstanceRooms();
        int i = 0;

        // make and add new valid rooms until the number of rooms is met
        while (i <= numRooms) {
            Room newRoom = makeRoom();
            boolean canAdd = true;

            // if the hashSet is empty, add the first valid room (no need to check for overlap)
            if (rooms.isEmpty()) {
                rooms.add(newRoom);
                i++;

                // otherwise, iterate through the entire hashSet and check if there are any rooms overlap
            } else {
                for (Room room : rooms) {
                    if (overlap(newRoom, room)) {
                        canAdd = false;
                    }
                }
                // given that there's no overlap, add the room to the hashSet
                if (canAdd) {
                    rooms.add(newRoom);
                    i++;
                }
            }
        }
        generateLightSources();
    }

    /** makes a single new room that is within world bounds **/
    private Room makeRoom() {
        Room newRoom = new Room(randomDimension(), randomDimension(), randomPos(), randomizer);
        if (withinBoundsRooms(newRoom)) {
            return newRoom;
        }

        // if not within bounds, call method recursively until a room within bounds is made
        return makeRoom();
    }
    private void drawDoors() {
        generateDoors();
        for (Door door : doors) {
            world[door.location.x][door.location.y] = Tileset.SAND;
        }
    }

    /** generates a random # of doors for a single room **/
    private void generateDoors() {
        // for each room, make a random # of doors
        for (Room room : rooms) {
            makeDoor(room);
        }
    }

    private void makeDoor(Room room) {
        // while not all the doors are generated yet
        int i = 0;
        while (i < 2) {
            Door newDoor = new Door(room, randomizer, room.sides.get(i));

            i++;
            if (!room.doors.contains(newDoor) && withinBounds(newDoor.location)) {
                room.doors.add(newDoor);
                doors.add(newDoor);
            }
        }
    }

    private boolean withinBoundsRooms(Room room) {
        // need to check if bottom left and top right corner is within bounds as well

        Position bottomLeft = room.corners.get(0);
        Position topRight = room.corners.get(2);

        return (withinBounds(bottomLeft) && withinBounds(topRight));
    }

    /** if within world bounds, returns true. otherwise, returns false **/
    private boolean withinBounds(Position position) {
        return position.x < WORLD_WIDTH - 2 && position.y < WORLD_HEIGHT - 2 && position.x > 2 && position.y > 2;
    }

    /** checks if room1 overlaps with room2. Returns true if there is overlap **/
    private boolean overlap(Room room1, Room room2) {
        Position bottomLeft1 = room1.corners.get(0);
        Position topRight1 = room1.corners.get(2);
        Position bottomLeft2 = room2.corners.get(0);
        Position topRight2 = room2.corners.get(2);

        for (int x = bottomLeft1.x; x <= topRight1.x; x++) {
            for (int y = bottomLeft1.y; y <= topRight1.y; y++) {
                if (x >= bottomLeft2.x - 1 && x <= topRight2.x + 1 && y >= bottomLeft2.y - 1 && y <= topRight2.y + 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /** generates dimensions for a room within the minSize and maxSize **/
    private int randomDimension() {
        return randomizer.nextInt(maxRoomSize + minRoomSize) + minRoomSize;
    }

    /** returns a randomized position within the world bounds **/
    private Position randomPos() {
        int randomX = randomizer.nextInt(WORLD_WIDTH);
        int randomY = randomizer.nextInt(WORLD_HEIGHT);
        return new Position(randomX, randomY);
    }

    /** returns a random number of rooms within the max and min range **/
    private int randomInstanceRooms() {
        return randomizer.nextInt(maxRooms - minRooms) + minRooms;
    }
}
