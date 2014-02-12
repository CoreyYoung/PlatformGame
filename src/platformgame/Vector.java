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
        return (int) Math.toDegrees(Math.atan2(-(x1 - x2), y1 - y2)) + 90;
    }

    public static float getMagnitude(float x, float y) {
        return (float) Math.sqrt(x * x + y * y);
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public void setYMagnitude(float yMagnitude) {
        Vector tempVector = cartesianToVector(getXMagnitude(), yMagnitude);

        direction = tempVector.direction;
        magnitude = tempVector.magnitude;
    }

    public void setXMagnitude(float xMagnitude) {
        Vector tempVector = cartesianToVector(xMagnitude, getYMagnitude());

        direction = tempVector.direction;
        magnitude = tempVector.magnitude;
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
