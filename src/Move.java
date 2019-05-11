public class Move {

  private int row;
  private int col;
  private TileID tile;

  public Move(int row, int col, TileID tile) {
    this.row = row;
    this.col = col;
    this.tile = tile;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public TileID getTile() {
    return tile;
  }

  @Override
  public String toString() {
    return "Move{" +
        "row=" + row +
        ", col=" + col +
        ", tile=" + tile +
        '}';
  }
}
