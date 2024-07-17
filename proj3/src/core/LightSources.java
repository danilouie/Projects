package core;

import edu.princeton.cs.algs4.StdDraw;

public class LightSources {
    private Position position;
    static boolean lightsOn = false;

    int radius;

    // first testing without any cast
    private static final int LIGHT_CAST_RADIUS = 4;

    public LightSources(Position position) {
        this.position = position;
        this.radius = LIGHT_CAST_RADIUS;
    }

    public int getRadius() {
        return radius;
    }

    public Position getPosition() {
        return position;
    }
    public boolean isLightsOn() {
        return lightsOn;
    }

    public void toggleLights() {
        lightsOn = !lightsOn;
    }

    // used to calculate casting of light?
    public static int distanceCalc(Position pos1, Position pos2) {
        int dis1 = Math.abs(pos1.x - pos2.x);
        int dis2 = Math.abs(pos1.y - pos2.y);
        return Math.max(dis1, dis2);
    }

    @Override
    public String toString() {
        return "LightSource{" + "position=" + position + ", LightsOn=" + lightsOn + ", radius=" + radius + '}';
    }

    public void handleLightInput() {
        if (StdDraw.hasNextKeyTyped()) {
            char input = Character.toLowerCase(StdDraw.nextKeyTyped());
            System.out.println("input worked: " + input);
            if (input == 't') {
                toggleLights();
                System.out.println("Lights toggled");
            }
        }
    }
}
