import java.util.List;

public class Board {

  private int board[][];
  private int score;

  public Board() {
    board = new int[10][10];
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        board[i][j] = 0;
      }
    }
    score = 0;
  }

  public Board(Board board) {
    this.board = new int[10][10];
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        this.board[i][j] = board.getEntry(i, j);
      }
    }
    score = 0;
  }

  private int getEntry(int row, int col) {
    return board[row][col];
  }

  public int getScore() {
    return score;
  }

  private void clearCol(int col) {
    for (int i = 0; i < 10; i++) {
      board[i][col] = 0;
    }
    score += 10;
  }

  private void clearRow(int row) {
    for (int i = 0; i < 10; i++) {
      board[row][i] = 0;
    }
    score += 10;
  }

  private void checkLineClear(int row, int col) {
    boolean clearRow = true;
    boolean clearCol = true;
    for (int i = 0; i < 10 ; i++) {
      if (board[i][col] == 0) {
        clearCol = false;
      }
      if (board[row][i] == 0) {
        clearRow = false;
      }
    }
    if (clearRow) {
      clearRow(row);
    }
    if (clearCol) {
      clearCol(col);
    }
  }

  public boolean canBePlaced(TileID tile) {
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (checkPlacePiece(i, j, tile)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean checkPlacePiece(int row, int col, TileID tile) {
    List<Coordinate> offsets = tile.getOffsets();
    for (Coordinate c : offsets) {
      if (row + c.getRow() >= 10 || col + c.getCol() >= 10 ||
          board[row + c.getRow()][col + c.getCol()] == 1) {
        return false;
      }
    }
    return true;
  }

  public boolean placePiece(int row, int col, TileID tile) {
    List<Coordinate> offsets = tile.getOffsets();
    if (checkPlacePiece(row, col, tile)) {
      for (Coordinate c : offsets) {
        board[row + c.getRow()][col + c.getCol()] = 1;
        checkLineClear(row + c.getRow(), col + c.getCol());
        score += 1;
      }
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    String result = "";
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] == 0) {
          result += ". ";
        } else {
          result += "x ";
        }
      }
      result += "\n";
    }
    result += "Score: " + score + "\n";
    return result;
  }

}
