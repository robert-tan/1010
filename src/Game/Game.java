package Game;

import java.util.ArrayList;
import java.util.List;

public class Game {

  private int[][] board;
  private int score;
  TileID[] nextThree;

  private boolean[] hasNextThree;

  public Game() {
    board = new int[10][10];
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        board[i][j] = 0;
      }
    }
    score = 0;
    newTiles();
  }

  public Game(Game game) {
    this(Game.dupBoard(game.getBoard()), game.nextThree, game.hasNextThree);
  }

  public Game(int[][] board, TileID[] nextThree, boolean[] hasNextThree) {
    this.board = board;
    this.nextThree = new TileID[]{nextThree[0], nextThree[1], nextThree[2]};
    this.hasNextThree = new boolean[]{hasNextThree[0], hasNextThree[1], hasNextThree[2]};
  }

  public boolean[] getHasNextThree() {
    return hasNextThree;
  }

  public TileID[] getNextThree() {
    return nextThree;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public void newTiles() {
    nextThree = new TileID[3];
    hasNextThree = new boolean[3];
    for (int i = 0; i < 3; i++) {
      nextThree[i] = TileID.getRandomID();
      hasNextThree[i] = true;
    }
  }

  public static synchronized int[][] dupBoard(int[][] board) {
    int[][] newBoard = new int[10][10];
    for (int i = 0; i < 10; i++) {
      System.arraycopy(board[i], 0, newBoard[i], 0, 10);
    }
    return newBoard;
  }

   public static synchronized boolean checkPlacePiece(int[][] board, int row, int col, TileID tile) {
    List<Coordinate> offsets = tile.getOffsets();
    for (Coordinate c : offsets) {
      if (row + c.getRow() >= 10 || col + c.getCol() >= 10 ||
          board[row + c.getRow()][col + c.getCol()] == 1) {
        return false;
      }
    }
    return true;
  }

   public static synchronized void clearCol(int[][] board, int col) {
    for (int i = 0; i < 10; i++) {
      board[i][col] = 0;
    }
  }

   public static synchronized void clearRow(int[][] board, int row) {
    for (int i = 0; i < 10; i++) {
      board[row][i] = 0;
    }
  }

   public static synchronized int checkLineClear(int[][] board, int row, int col) {
    int result = 0;
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
      clearRow(board, row);
      result += 1;
    }
    if (clearCol) {
      clearCol(board, col);
      result += 1;
    }
    return result;
  }

   public static synchronized int[][] placePiece(int[][] board, int row, int col, TileID tile, int[] result) {
    List<Coordinate> offsets = tile.getOffsets();
    result[0] = 0;
    if (checkPlacePiece(board, row, col, tile)) {
      for (Coordinate c : offsets) {
        board[row + c.getRow()][col + c.getCol()] = 1;
        result[0] += checkLineClear(board, row + c.getRow(), col + c.getCol());
      }
      return board;
    }
    return null;
  }

  public void playPiece(int row, int col, TileID tile) {
    for (int i = 0; i < 3; i++) {
      if (hasNextThree[i] && nextThree[i].equals(tile)) {
        int [] scoreDiff = new int[1];
        int[][] board = placePiece(this.board, row, col, tile, scoreDiff);
        if (board != null) {
          score += tile.getSize() + scoreDiff[0] * 10;
          this.board = board;
          hasNextThree[i] = false;
          if (!hasUnplayedPiece()) {
            newTiles();
          }
          return;
        }
      }
    }
  }

  public void playPieceAI(int row, int col, TileID tile) {
    for (int i = 0; i < 3; i++) {
      int [] scoreDiff = new int[1];
      int[][] board = placePiece(this.board, row, col, tile, scoreDiff);
      if (board != null) {
        score += tile.getSize() + scoreDiff[0] * 10;
        this.board = board;
        hasNextThree[i] = false;
        if (!hasUnplayedPiece()) {
          newTiles();
        }
        return;
      }
    }
  }

  public int[][] getBoard() {
    return board;
  }

  public boolean hasUnplayedPiece() {
    for (int i = 0; i < 3; i++) {
      if (hasNextThree[i]) {
        return true;
      }
    }
    return false;
  }

  public int numUnplayedPiece() {
    int result = 0;
    for (int i = 0; i < 3; i++) {
      if (hasNextThree[i]) {
        result++;
      }
    }
    return result;
  }

  public static synchronized boolean canBePlaced(int[][] board, TileID tile) {
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (checkPlacePiece(board, i, j, tile)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isFinished() {
    for (int i = 0; i < 3; i++) {
      if (hasNextThree[i] && canBePlaced(board, nextThree[i])) {
        return false;
      }
    }
    return true;
  }

  public List<Move> getAllValidMoves() {
    List<Move> moves = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      if (hasNextThree[i]) {
        for (int j = 0; j < 10; j++) {
          for (int k = 0; k < 10; k++) {
            Move move = new Move(j, k, nextThree[i]);
            if (checkPlacePiece(board, j, k, nextThree[i])) {
              moves.add(move);
            }
          }
        }
      }
    }
    return moves;
  }

  public List<Move> getAllValidMovesForTile(TileID tile) {
    List<Move> moves = new ArrayList<>();
    for (int j = 0; j < 10; j++) {
      for (int k = 0; k < 10; k++) {
        Move move = new Move(j, k, tile);
        if (checkPlacePiece(this.getBoard(), j, k, tile)) {
          moves.add(move);
        }
      }
    }
//    System.out.println(moves.size());
    return moves;
  }

  public List<Move> getAllValidMovesForAllTiles() {
    List<Move> moves = null;
    for (TileID tile : TileID.values()) {
      if (moves == null) {
        moves = getAllValidMovesForTile(tile);
      } else {
        moves.addAll(getAllValidMovesForTile(tile));
      }
    }
    return moves;
  }

  public int getNumPiecesOnBoard() {
    int result = 0;
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] == 1) result++;
      }
    }
    return result;
  }

  public int getScore() {
    return score;
  }

  @Override
  public String toString() {
    String result = "\n  0 1 2 3 4 5 6 7 8 9 \n";
    for (int i = 0; i < 10; i++) {
      result += i + " ";
      for (int j = 0; j < 10; j++) {
        if (board[i][j] == 0) {
          result += ". ";
        } else {
          result += "x ";
        }
      }
      result += i + "\n";
    }
    result += "  0 1 2 3 4 5 6 7 8 9 \n";
    result += "Score: " + score + "\n";
    result += "\n";
    result += "0: " + nextThree[0].toString() + " " + hasNextThree[0] + "\n";
    result += "1: " + nextThree[1].toString() + " " + hasNextThree[1] + "\n";
    result += "2: " + nextThree[2].toString() + " " + hasNextThree[2] + "\n";
    return result;
  }
}
