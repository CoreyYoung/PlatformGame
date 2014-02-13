package platformgame;

public class Vector {

    private float direction, magnitude;

    public Vector(float direction, float speed) {
        this.direction = direction;
        this.magnitude = speed;
    }

    public static Vector addVectors(Vector vector1, Vector vector2) {
        float x = vector1.getXMagnitude() + vector2.getXMagnitude();
        float y = vector1.getYMagnitude() + vector2.getYMagnitude();

        float speed = (float) Math.sqrt(x * x + y * y);

        float dir = (float) (Math.atan(y / x) * (180 / Math.PI));
        if (x < 0) {
            dir += 180;
        } else {
            if (y >= 0) {
                dir += 360;
            }
        }

        return new Vector(dir, speed);
    }

    public static Vector cartesianToVector(float x, float y) {
        float dir = Vector.getDir(x, y, 0, 0);
        float magnitude = Vector.getMagnitude(x, y);

        return new Vector(dir, magnitude);
    }

    public static float getDir(float x1, float y1, float x2, float y2) {
        return (float) Math.toDegrees(Math.atan2(-(x1 - x2), y1 - y2)) + 90;
    }

    public static float getMagnitude(float x, float y) {
        return (float) Math.sqrt(x * x + y * y);
    }

    public void addVector(Vector addedVector) {
        float x = getXMagnitude() + addedVector.getXMagnitude();
        float y = getYMagnitude() + addedVector.getYMagnitude();

        magnitude = (float) Math.sqrt(x * x + y * y);
        direction = (float) (Math.atan(y / x) * (180 / Math.PI));

        if (x < 0) {
            direction += 180;
        } else {
            if (y >= 0) {
                direction += 360;
            }
        }
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public void setXMagnitude(float xMagnitude) {
        float x = xMagnitude;
        float y = getYMagnitude();

        direction = Vector.getDir(x, y, 0, 0);
        magnitude = Vector.getMagnitude(x, y);
    }

    public void setYMagnitude(float yMagnitude) {
        float x = getXMagnitude();
        float y = yMagnitude;

        direction = Vector.getDir(x, y, 0, 0);
        magnitude = Vector.getMagnitude(x, y);
    }

    public float getDir() {
        return direction;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public float getXMagnitude() {
        return (float) (magnitude * Math.cos(direction * (Math.PI / 180)));
    }

    public float getYMagnitude() {
        return (float) (magnitude * Math.sin(direction * (Math.PI / 180)));
    }
}
