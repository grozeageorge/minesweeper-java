public class Tile {
    private TileState state = TileState.UNKNOWN;
    private boolean mine;
    private int total;

    public boolean isMine() {
        return mine;
    }

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public void setSurrounding(int total) {
        this.total = total;
    }

    public int getSurrounding() {
        return total;
    }
}
