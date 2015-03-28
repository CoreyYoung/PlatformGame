package platformgame;

public class HitBox {
    private final int width, height, xOffset, yOffset;

    public HitBox(int width, int height, int xOffset, int yOffset) {
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public int getYForBottom(int bottom) {
        return bottom - height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }
}
