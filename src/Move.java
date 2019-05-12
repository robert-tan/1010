public class Move implements Comparable<Move> {

  private int row;
  private int col;
  private TileID tile;
  private int moveScore;

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

  public int getMoveScore() {
    return moveScore;
  }

  public void setMoveScore(int moveScore) {
    this.moveScore = moveScore;
  }

  @Override
  public String toString() {
    return "Move{" +
        "row=" + row +
        ", col=" + col +
        ", tile=" + tile +
        '}';
  }

  @Override
  public int compareTo(Move o) {
    if (o.moveScore > moveScore) {
      return -1;
    } else if (o.moveScore == moveScore) {
      return 0;
    }
    return 1;
  }
}
