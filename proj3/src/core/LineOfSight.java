package core;

public class LineOfSight {
    private Avatar avatar;
    private int radius;

    // goal: line of site around a certain radius of the avatar
    public LineOfSight(Avatar avatar, int radius) {
        this.avatar = avatar;
    }

    // checks if a tile is within a line of sight
    public boolean tileIsLineOfSight(Position tilePos) {
        // first determine avatar's position
        Position avatarPos = new Position(avatar.getX(), avatar.getY());

        // use helper to calculate distance
        int distance = distanceCalc(avatarPos, tilePos);
        return distance <= radius;
    }
    private int distanceCalc(Position pos1, Position pos2) {
        int dis1 = Math.abs(pos1.x - pos2.x);
        int dis2 = Math.abs(pos1.y - pos2.y);
        return Math.max(dis1, dis2);
    }
}
