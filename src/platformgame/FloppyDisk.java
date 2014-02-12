package platformgame;

import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class FloppyDisk {

    private static Image image;

    public final int x;
    public final int y;
    public static ArrayList<FloppyDisk> floppyDiskList = new ArrayList<>();

    public FloppyDisk(int x, int y) {
        floppyDiskList.add(this);
        this.x = x;
        this.y = y;
    }

    public static void init() throws SlickException {
        image = new Image("data/graphics/world objects/FloppyDisk.png");
    }

    public static void render(int camX, int camY) {
        for (FloppyDisk floppyDisk : floppyDiskList) {
            image.draw(floppyDisk.x + camX, floppyDisk.y + camY);
        }
    }

    public void useFloppyDisk() {
        DataIO.saveGame();
        Player.health = Player.healthMAX;
        Hud.addMessage("The game has been saved, and the player's health restored! ");
    }
}
