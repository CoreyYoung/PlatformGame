package platformgame;

public class MovingEntity {

    public int JUMP_SPEED;
    public int width;
    public int height;
    public float x, y;
    public Vector velocity = new Vector(0, 0);

    public void update() {
        movement();
        collisions();
    }

    public void movement() {
    }

    public void collisions() {
        if (onSlope(World.LEFT_SLOPE)) {
            performLeftSlopeCollision();
        } else if (onSlope(World.RIGHT_SLOPE)) {
            performRightSlopeCollision();
        } else {
            performBlockCollisions();
        }
    }

    private boolean onSlope(int tileTypeNum) {
        final int x1 = (int) x + width - 1;
        final int x2 = (int) x + 1;

        final int tempY = (int) (y + height - 1);

        return (World.getTileAtPoint((int) x1 / 32, tempY / 32) == tileTypeNum
                || World.getTileAtPoint((int) x2 / 32, tempY / 32) == tileTypeNum);
    }

    private void performLeftSlopeCollision() {
        int gridPadding = (int) (Math.ceil((double) height / 32) * 32) - height;
        int tempX = (int) x + 1;
        int roundedY = (int) (Math.floor((y - 1 + 32 - gridPadding) / 32) * 32);

        int moveY = roundedY + (gridPadding - 32) + (int) (tempX - Math.floor(tempX / 32) * 32);

        performSlopeCollisions(World.LEFT_SLOPE, -1, tempX, moveY);
    }

    private void performRightSlopeCollision() {
        int gridPadding = (int) (Math.ceil((double) height / 32) * 32) - height;
        int tempX = (int) x + width - 1;
        int roundedY = (int) (Math.floor((y - 1 + 32 - gridPadding) / 32) * 32);

        int moveY = roundedY + (gridPadding - 32) + (int) (32 - (tempX - Math.floor(tempX / 32) * 32));

        performSlopeCollisions(World.RIGHT_SLOPE, 1, tempX, moveY);
    }

    private void performSlopeCollisions(int tileTypeNum, int dir, int tempX, int moveY) {
        int gridPadding = (int) (Math.ceil((double) height / 32) * 32) - height;
        int roundedY = (int) (Math.floor((y - 1) / 32) * 32);
        int slopeTop = roundedY + gridPadding;

        x += velocity.getXMagnitude();

        if (World.getTileAtPoint((int) (tempX + velocity.getXMagnitude()) / 32, (int) (y + height - 1) / 32) == tileTypeNum
                || Math.signum(velocity.getXMagnitude()) != dir) {
            if (y > moveY) {
                y = moveY;
            }
        } else {
            y = slopeTop;
        }

        if (y + velocity.getYMagnitude() <= moveY) {
            y += velocity.getYMagnitude();
        } else {
            velocity.setYMagnitude(0);
            y = moveY;
        }

        if (World.isBlockAtPoint((int) (x + 32) / 32, (int) y / 32)) {
            x = (int) (x / 32) * 32;
        } else if (World.isBlockAtPoint((int) (x) / 32, (int) y / 32)) {
            x = (int) ((x + 32) / 32) * 32;
        }
    }

    public void performBlockCollisions() {
        if (velocity.getXMagnitude() != 0) {
            int sign = (int) Math.signum(velocity.getXMagnitude());
            int tempX, pos, destX;

            if (sign == -1) {
                tempX = (int) Math.floor((x + velocity.getXMagnitude()) / 32);
                pos = (int) Math.floor(x / 32);
                destX = (int) Math.floor(x / 32) * 32;
            } else {
                tempX = (int) Math.floor((x + velocity.getXMagnitude() + width) / 32);
                pos = (int) (Math.ceil(x / 32) - Math.floor((32 - width) / 32) + 1);
                destX = (int) Math.floor(x / 32) * 32 + 32 - width;
            }

            if (!World.isBlockInLine(tempX, tempX, (int) Math.floor(y / 32), (int) Math.floor((y + height - 1) / 32))) {
                x += velocity.getXMagnitude();
            } else {
                velocity.setXMagnitude(0);

                if (!World.isBlockInLine(pos, pos, (int) Math.floor(y / 32), (int) Math.floor((y + height - 1) / 32))) {
                    x = destX;
                } else {
                    x = Math.round(x);
                }
            }
        }

        int pos = 0;
        if (velocity.getYMagnitude() < 0) {
            pos = (int) Math.floor(y / 32) * 32;
        } else if (velocity.getYMagnitude() > 0) {
            pos = (int) Math.round((y + height) / 32) * 32 - height;
        }

        if (!World.isBlockAtPoint((int) Math.floor(x / 32), (int) Math.floor((y + height + velocity.getYMagnitude()) / 32))
                && !World.isBlockAtPoint((int) Math.floor((x + width - 1.1) / 32), (int) Math.floor((y + height + velocity.getYMagnitude()) / 32))
                && !World.isBlockAtPoint((int) Math.floor(x / 32), (int) Math.floor((y + velocity.getYMagnitude()) / 32))
                && !World.isBlockAtPoint((int) Math.floor((x + width - 1.1) / 32), (int) Math.floor((y + velocity.getYMagnitude()) / 32))) {
            y += velocity.getYMagnitude();
        } else {
            if (velocity.getYMagnitude() != 0) {
                y = pos;
            }

            velocity.setYMagnitude(0);
        }
    }

    public void jump() {
        if (World.isBlockAtPoint((int) (x / 32), (int) ((y + height) / 32))
                || World.isBlockAtPoint((int) ((x + width - 1) / 32), (int) ((y + height) / 32))) {
            velocity.setYMagnitude(JUMP_SPEED);
        }
    }
}
