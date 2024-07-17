package core;

public class Position {
    int x;
    int y;

    /** Position constructor **/
    public Position(int xPos, int yPos) {
        x = xPos;
        y = yPos;
    }

    public int posX() {
        return x;
    }

    public int posY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
