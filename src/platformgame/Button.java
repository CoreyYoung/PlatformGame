package platformgame;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Button {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Image normalImage;
    private final Image hoverImage;

    public Button(int x, int y, String normalImagePath) throws SlickException {
        this.x = x;
        this.y = y;
        this.normalImage = new Image(normalImagePath);
        hoverImage = null;
        width = normalImage.getWidth();
        height = normalImage.getHeight();
    }

    public Button(int x, int y, String normalImagePath, String hoverImagePath) throws SlickException {
        this.x = x;
        this.y = y;
        this.normalImage = new Image(normalImagePath);
        this.hoverImage = new Image(hoverImagePath);
        width = normalImage.getWidth();
        height = normalImage.getHeight();
    }

    public void render(Input input) {
        if (isMouseOver(input) && hoverImage != null) {
            hoverImage.draw(x, y);
        } else {
            normalImage.draw(x, y);
        }
    }

    private boolean isMouseOver(Input input) {
        return (input.getMouseX() >= x && input.getMouseX() <= x + width
                && input.getMouseY() >= y && input.getMouseY() <= y + height);
    }

    public boolean isPressed(Input input) {
        return (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && isMouseOver(input));
    }
}
