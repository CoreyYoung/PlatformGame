package platformgame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public abstract class WeatherSystem {

    public static final int NONE = 0;
    public static final int RAIN = 1;
    private static int weather = NONE;
    private static int rainX = 0;
    private static int rainY = 0;
    private static Image rainImage;
    private static Sound rainSound;

    public static void init() throws SlickException {
        rainImage = new Image("data/graphics/weather/Rain.png");
        rainSound = new Sound("data/sounds/Rain.ogg");
    }

    public static void update() {
        if (weather == RAIN) {
            rainX -= 2;
            rainY += 5;

            if (rainX < 0) {
                rainX += Game.width;
            }

            if (rainY > Game.height) {
                rainY -= Game.height;
            }
        }
    }

    public static void render() {
        if (weather == RAIN) {
            rainImage.setAlpha(0.3f);
            rainImage.draw(rainX - Game.width, rainY - Game.height);
            rainImage.draw(rainX, rainY - Game.height);
            rainImage.draw(rainX - Game.width, rainY);
            rainImage.draw(rainX, rainY);
        }
    }

    public static void setWeather(int weather) {
        WeatherSystem.weather = weather;

        if (weather == RAIN) {
            rainSound.loop();
        } else {
            rainSound.stop();
        }
    }
}
