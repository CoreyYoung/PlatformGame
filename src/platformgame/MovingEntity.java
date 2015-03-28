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

        return (World.getTileAtPoint(x1 / World.BLOCK_WIDTH, tempY / World.BLOCK_WIDTH) == tileTypeNum
                || World.getTileAtPoint(x2 / World.BLOCK_WIDTH, tempY / World.BLOCK_WIDTH) == tileTypeNum);
    }

    private void performLeftSlopeCollision() {
        int gridPadding = (int) (Math.ceil((double) height / World.BLOCK_WIDTH) * World.BLOCK_WIDTH) - height;
        int tempX = (int) x + 1;
        int roundedY = (int) (Math.floor((y - 1 + World.BLOCK_WIDTH - gridPadding) / World.BLOCK_WIDTH) * World.BLOCK_WIDTH);

        int moveY = roundedY + (gridPadding - World.BLOCK_WIDTH) + (int) (tempX - Math.floor(tempX / World.BLOCK_WIDTH) * World.BLOCK_WIDTH);

        performSlopeCollisions(World.LEFT_SLOPE, -1, tempX, moveY);
    }

    private void performRightSlopeCollision() {
        int gridPadding = (int) (Math.ceil((double) height / World.BLOCK_WIDTH) * World.BLOCK_WIDTH) - height;
        int tempX = (int) x + width - 1;
        int roundedY = (int) (Math.floor((y - 1 + World.BLOCK_WIDTH - gridPadding) / World.BLOCK_WIDTH) * World.BLOCK_WIDTH);

        int moveY = roundedY + (gridPadding - World.BLOCK_WIDTH) + (int) (World.BLOCK_WIDTH - (tempX - Math.floor(tempX / World.BLOCK_WIDTH) * World.BLOCK_WIDTH));

        performSlopeCollisions(World.RIGHT_SLOPE, 1, tempX, moveY);
    }

    private void performSlopeCollisions(int tileTypeNum, int dir, int tempX, int moveY) {
        int gridPadding = (int) (Math.ceil((double) height / World.BLOCK_WIDTH) * World.BLOCK_WIDTH) - height;
        int roundedY = (int) (Math.floor((y - 1) / World.BLOCK_WIDTH) * World.BLOCK_WIDTH);
        int slopeTop = roundedY + gridPadding;

        x += velocity.getXMagnitude();

        if (World.getTileAtPoint((int) (tempX + velocity.getXMagnitude()) / World.BLOCK_WIDTH, (int) (y + height - 1) / World.BLOCK_WIDTH) == tileTypeNum
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

        if (World.isBlockAtPoint((int) (x + World.BLOCK_WIDTH) / World.BLOCK_WIDTH, (int) y / World.BLOCK_WIDTH)) {
            x = (int) (x / World.BLOCK_WIDTH) * World.BLOCK_WIDTH;
        } else if (World.isBlockAtPoint((int) (x) / World.BLOCK_WIDTH, (int) y / World.BLOCK_WIDTH)) {
            x = (int) ((x + World.BLOCK_WIDTH) / World.BLOCK_WIDTH) * World.BLOCK_WIDTH;
        }
    }

    public void performBlockCollisions() {
        if (velocity.getXMagnitude() != 0) {
            int sign = (int) Math.signum(velocity.getXMagnitude());
            int tempX, pos, destX;

            if (sign == -1) {
                tempX = (int) Math.floor((x + velocity.getXMagnitude()) / World.BLOCK_WIDTH);
                pos = (int) Math.floor(x / World.BLOCK_WIDTH);
                destX = (int) Math.floor(x / World.BLOCK_WIDTH) * World.BLOCK_WIDTH;
            } else {
                tempX = (int) Math.floor((x + velocity.getXMagnitude() + width) / World.BLOCK_WIDTH);
                pos = (int) (Math.ceil(x / World.BLOCK_WIDTH) - Math.floor((World.BLOCK_WIDTH - width) / World.BLOCK_WIDTH) + 1);
                destX = (int) Math.floor(x / World.BLOCK_WIDTH) * World.BLOCK_WIDTH + World.BLOCK_WIDTH - width;
            }

            if (!World.isBlockInLine(tempX, tempX, (int) Math.floor(y / World.BLOCK_WIDTH), (int) Math.floor((y + height - 1) / World.BLOCK_WIDTH))) {
                x += velocity.getXMagnitude();
            } else {
                velocity.setXMagnitude(0);

                if (!World.isBlockInLine(pos, pos, (int) Math.floor(y / World.BLOCK_WIDTH), (int) Math.floor((y + height - 1) / World.BLOCK_WIDTH))) {
                    x = destX;
                } else {
                    x = Math.round(x);
                }
            }
        }

        int pos = 0;
        if (velocity.getYMagnitude() < 0) {
            pos = (int) Math.floor(y / World.BLOCK_WIDTH) * World.BLOCK_WIDTH;
        } else if (velocity.getYMagnitude() > 0) {
            pos = Math.round((y + height) / World.BLOCK_WIDTH) * World.BLOCK_WIDTH - height;
        }

        if (!World.isBlockAtPoint((int) Math.floor(x / World.BLOCK_WIDTH), (int) Math.floor((y + height + velocity.getYMagnitude()) / World.BLOCK_WIDTH))
                && !World.isBlockAtPoint((int) Math.floor((x + width - 1.1) / World.BLOCK_WIDTH), (int) Math.floor((y + height + velocity.getYMagnitude()) / World.BLOCK_WIDTH))
                && !World.isBlockAtPoint((int) Math.floor(x / World.BLOCK_WIDTH), (int) Math.floor((y + velocity.getYMagnitude()) / World.BLOCK_WIDTH))
                && !World.isBlockAtPoint((int) Math.floor((x + width - 1.1) / World.BLOCK_WIDTH), (int) Math.floor((y + velocity.getYMagnitude()) / World.BLOCK_WIDTH))) {
            y += velocity.getYMagnitude();
        } else {
            if (velocity.getYMagnitude() != 0) {
                y = pos;
            }

            velocity.setYMagnitude(0);
        }
    }

    public void jump() {
        if (World.isBlockAtPoint((int) (x / World.BLOCK_WIDTH), (int) ((y + height) / World.BLOCK_WIDTH))
                || World.isBlockAtPoint((int) ((x + width - 1) / World.BLOCK_WIDTH), (int) ((y + height) / World.BLOCK_WIDTH))) {
            velocity.setYMagnitude(JUMP_SPEED);
        }
    }
}
