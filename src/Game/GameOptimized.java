package Game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameOptimized {

  private static final short FULL_ROW = (short) 0b1111111111000000;

  private static final short[] ONE_BOARD = {Short.MIN_VALUE, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] TWO_V_BOARD = {Short.MIN_VALUE, Short.MIN_VALUE, 0, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] TWO_H_BOARD = {Short.MIN_VALUE >> 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] THREE_V_BOARD = {Short.MIN_VALUE, Short.MIN_VALUE, Short.MIN_VALUE, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] THREE_H_BOARD = {Short.MIN_VALUE >> 2, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] THREE_BL_BOARD = {Short.MIN_VALUE, Short.MIN_VALUE >> 1, 0, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] THREE_BR_BOARD = {(short) ((0xFFFF & Short.MIN_VALUE) >>> 1), Short.MIN_VALUE >> 1, 0, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] THREE_TL_BOARD = {Short.MIN_VALUE >> 1, Short.MIN_VALUE, 0, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] THREE_TR_BOARD = {Short.MIN_VALUE >> 1, (short) ((0xFFFF & Short.MIN_VALUE) >>> 1), 0, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] FOUR_V_BOARD = {Short.MIN_VALUE, Short.MIN_VALUE, Short.MIN_VALUE, Short.MIN_VALUE, 0, 0, 0, 0, 0, 0};
  private static final short[] FOUR_H_BOARD = {Short.MIN_VALUE >> 3, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] FOUR_SQ_BOARD = {Short.MIN_VALUE >> 1, Short.MIN_VALUE >> 1, 0, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] FIVE_TR_BOARD = {Short.MIN_VALUE >> 2, (short) ((0xFFFF & Short.MIN_VALUE) >>> 2), (short) ((0xFFFF & Short.MIN_VALUE) >>> 2), 0, 0, 0, 0, 0, 0, 0};
  private static final short[] FIVE_TL_BOARD = {Short.MIN_VALUE >> 2, Short.MIN_VALUE, Short.MIN_VALUE, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] FIVE_BR_BOARD = {(short) ((0xFFFF & Short.MIN_VALUE) >>> 2), (short) ((0xFFFF & Short.MIN_VALUE) >>> 2), Short.MIN_VALUE >> 2, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] FIVE_BL_BOARD = {Short.MIN_VALUE, Short.MIN_VALUE, Short.MIN_VALUE >> 2, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] FIVE_V_BOARD = {Short.MIN_VALUE, Short.MIN_VALUE, Short.MIN_VALUE, Short.MIN_VALUE, Short.MIN_VALUE, 0, 0, 0, 0, 0};
  private static final short[] FIVE_H_BOARD = {Short.MIN_VALUE >> 4, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  private static final short[] NINE_SQ_BOARD = {Short.MIN_VALUE >> 2, Short.MIN_VALUE >> 2, Short.MIN_VALUE >> 2, 0, 0, 0, 0, 0, 0, 0};

  public static final short[][] TILES = {ONE_BOARD, TWO_H_BOARD, TWO_V_BOARD, THREE_V_BOARD,
      THREE_H_BOARD, THREE_BL_BOARD, THREE_BR_BOARD, THREE_TL_BOARD, THREE_TR_BOARD, FOUR_V_BOARD,
      FOUR_H_BOARD, FOUR_SQ_BOARD, FIVE_TR_BOARD, FIVE_TL_BOARD, FIVE_BR_BOARD, FIVE_BL_BOARD,
      FIVE_V_BOARD, FIVE_H_BOARD, NINE_SQ_BOARD
  };

  public static final byte[] TILE_SIZES = {1, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 5, 5, 5, 5, 5, 5, 9};
  public static final byte[] TILE_WIDTHS = {1, 2, 1, 1, 3, 2, 2, 2, 2, 1, 4, 2, 3, 3, 3, 3, 1, 5, 3};
  public static final byte[] TILE_HEIGHTS = {1, 1, 2, 3, 1, 2, 2, 2, 2, 4, 1, 2, 3, 3, 3, 3, 5, 1, 3};
  public static final TileID[] TILE_IDS = {TileID.ONE, TileID.TWO_HOR, TileID.TWO_VER, TileID.THREE_VER,
      TileID.THREE_HOR, TileID.THREE_BOTTOM_LEFT, TileID.THREE_BOTTOM_RIGHT, TileID.THREE_TOP_LEFT,
      TileID.THREE_TOP_RIGHT, TileID.FOUR_VERT, TileID.FOUR_HOR, TileID.FOUR_SQUARE, TileID.FIVE_TOP_RIGHT,
      TileID.FIVE_TOP_LEFT, TileID.FIVE_BOTTOM_RIGHT, TileID.FIVE_BOTTOM_LEFT, TileID.FIVE_VERT,
      TileID.FIVE_HOR, TileID.NINE_SQUARE
  };

  private short [] boardRows;
  private int score;
  private byte[] moveSet;

  public GameOptimized() {
    boardRows = new short[10];
    moveSet = new byte[3];
    score = 0;
    getNextMoveSet();
  }

  public GameOptimized(GameOptimized game) {
    boardRows = new short[10];
    moveSet = new byte[3];
    copyBoard(game);
    System.arraycopy(game.moveSet, 0, moveSet, 0, 3);
    score = game.score;
  }

  public GameOptimized(Game game) {
    boardRows = new short[10];
    moveSet = new byte[3];
    fromBoard(game.getBoard());
    score = game.getScore();
    for (int i = 0; i < 3; i++) {
      if (game.getHasNextThree()[i]) {
        for (byte j = 0; i < 19; i++) {
          if (TILE_IDS[j].equals(game.getNextThree()[i])) {
            moveSet[i] = j;
          }
        }
      } else {
        moveSet[i] = -1;
      }
    }
    getNextMoveSet();
  }

  public void setMoveSet(byte[] moveSet) {
    this.moveSet = moveSet;
  }

  public void setBoard(short[] boardRows) {
    this.boardRows = boardRows;
  }

  public byte[] getMoveSet() {
    return moveSet;
  }

  public byte[][] getMoveSetPermutations() {
    return new byte[][] {
        {moveSet[0], moveSet[1], moveSet[2]},
        {moveSet[0], moveSet[2], moveSet[1]},
        {moveSet[1], moveSet[0], moveSet[2]},
        {moveSet[1], moveSet[2], moveSet[0]},
        {moveSet[2], moveSet[0], moveSet[1]},
        {moveSet[2], moveSet[1], moveSet[0]}
    };
  }

  private void getNextMoveSet() {
    moveSet[0] = (byte) (19 * Math.random());
    moveSet[1] = (byte) (19 * Math.random());
    moveSet[2] = (byte) (19 * Math.random());
  }

  public static short[] getRandomTile() {
    byte rand = (byte) (19 * Math.random());
    return TILES[rand];
  }

  private void copyBoard(GameOptimized game) {
    for (int i = 0; i < 10; i++) {
      boardRows[i] = game.boardRows[i];
    }
  }

  public boolean checkPlacePiece(short[] pieceRows) {
    for (int i = 0; i < 10; i++) {
      if (!((pieceRows[i] & boardRows[i]) == 0)) {
        return false;
      }
    }
    return true;
  }

  private void clearCol(byte col) {
    short val = Short.MIN_VALUE;
    val = (short) ((0xFFFF & val) >>> col);
    for (int i = 0; i < 10; i++) {
      boardRows[i] = (short) (~val & boardRows[i]);
    }
  }

  private void clearRow(byte col) {
    boardRows[col] = 0;
  }

  private boolean checkRowClear(byte row) {
    if (boardRows[row] == FULL_ROW) {
      clearRow(row);
      return true;
    }
    return false;
  }

  private boolean checkColClear(byte col) {
    boolean clearCol = true;
    short val = Short.MIN_VALUE;
    val = (short) ((0xFFFF & val) >>> col);
    for (int i = 0; i < 10; i++) {
      if ((val & boardRows[i]) == 0) {
        clearCol = false;
      }
    }
    if (clearCol) {
      clearCol(col);
      return true;
    }
    return false;
  }

  private byte checkLineClear() {
    byte result = 0;
    for (byte i = 0; i < 10; i++) {
      if (checkRowClear(i)) {
        result++;
      }
      if (checkColClear(i)) {
        result++;
      }
    }
    return result;
  }

  public byte placePiece(short[] pieceRows) {
    if (!checkPlacePiece(pieceRows)) {
      return -1;
    }
    for (int i = 0; i < 10; i++) {
      boardRows[i] = (short) (pieceRows[i] | boardRows[i]);
    }
    return checkLineClear();
  }

  public void playPiece(byte row, byte col, byte tileID) {
    if (tileID == moveSet[0]) {
      moveSet[0] = -1;
    } else if (tileID == moveSet[1]) {
      moveSet[1] = -1;
    } else if (tileID == moveSet[2]) {
      moveSet[2] = -1;
    } else {
      System.out.println("Row: " + row);
      System.out.println("Col: " + col);
      System.out.println("Game.TileID: " + TILE_IDS[tileID]);
      throw new RuntimeException();
    }
    short[] offsetTile = offsetTile(row, col, tileID);
    byte result = placePiece(offsetTile);
    score += result * 10 + TILE_SIZES[tileID];
    if (noUnplayedPiece()) {
      getNextMoveSet();
    }
  }

  public void aiPlayPiece(byte row, byte col, byte tileID) {
    short[] offsetTile = offsetTile(row, col, tileID);
    byte result = placePiece(offsetTile);
    score += result * 10 + TILE_SIZES[tileID];
  }

  private short[] offsetTile(byte row, byte col, byte tileID) {
    short[] result = new short[10];
    for (int i = 0; i < TILE_HEIGHTS[tileID]; i++) {
      result[i + row] = (short) ((0xFFFF & TILES[tileID][i]) >>> col);
    }
    return result;
  }

  public boolean canBePlaced(byte tileID) {
    if (tileID == -1) return false;
    for (byte i = 0; i <= 10 - TILE_HEIGHTS[tileID]; i++) {
      for (byte j = 0; j <= 10 - TILE_WIDTHS[tileID] ; j++) {
        if (//((((0xFFFF & TILES[tileID][i]) >>> (6 + j)) & 1) == 0) &&
            checkPlacePiece(offsetTile(i, j, tileID))) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isFinished() {
    for (byte i = 0; i < 3; i++) {
      if (canBePlaced(moveSet[i])) return false;
    }
    return true;
  }

  private boolean noUnplayedPiece() {
    return (moveSet[0] == -1 && moveSet[1] == -1 && moveSet[2] == -1);
  }

  public byte getNumUnplayedPiece() {
    byte result = 0;
    if (moveSet[0] == -1) result++;
    if (moveSet[1] == -1) result++;
    if (moveSet[2] == -1) result++;
    return result;
  }

  public Set<Byte[]> getAllValidMoves() {
    Set<Byte[]> moves = new HashSet<>();
    for (byte which : moveSet) {
      if (which == -1) continue;
      for (byte i = 0; i <= 10 - TILE_HEIGHTS[which]; i++) {
        for (byte j = 0; j <= 10 - TILE_WIDTHS[which]; j++) {
          if (checkPlacePiece(offsetTile(i, j, which))) {
            moves.add(new Byte[]{i, j, which});
          }
        }
      }
    }
    return moves;
  }

  public List<Byte[]> getAllValidMovesForTile(byte which) {
    List<Byte[]> moves = new ArrayList<>();
    for (byte i = 0; i <= 10 - TILE_HEIGHTS[which]; i++) {
      for (byte j = 0; j <= 10 - TILE_WIDTHS[which]; j++) {
        if (checkPlacePiece(offsetTile(i, j, which))) {
          moves.add(new Byte[]{i, j, which});
        }
      }
    }
    return moves;
  }

  public int[][] toBoard() {
    int[][] result = new int[10][10];
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        result[i][j] = (boardRows[i] & ((0xFFFF & Short.MIN_VALUE) >>> j)) != 0 ? 1 : 0;
      }
    }
    return result;
  }

  public Game toGame() {
    TileID[] ids = new TileID[3];
    boolean[] hasNextThree = new boolean[3];
    for (int i = 0; i < 3; i++) {
      if (moveSet[i] == -1) {
        ids[i] = TileID.ONE;
        hasNextThree[i] = false;
      } else {
        ids[i] = TILE_IDS[moveSet[i]];
        hasNextThree[i] = true;
      }
    }
    Game game = new Game(toBoard(), ids, hasNextThree);
    game.setScore(score);
    return game;
  }

  private void fromBoard(int[][] board) {
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        boardRows[i] = (short) (boardRows[i] & (board[i][j] << (15 - j)));
      }
    }
  }

  public byte findTileFromTileID(TileID tileID) {
    for (byte i = 0; i < 19; i++) {
      if (tileID.equals(TILE_IDS[i])) return i;
    }
    return -1;
  }

  @Override
  public String toString() {
    String result = "\n  0 1 2 3 4 5 6 7 8 9 \n";
    for (int i = 0; i < 10; i++) {
      result += i + " ";
      for (short j = 0; j < 10; j++) {
        if ((boardRows[i] & ((0xFFFF & Short.MIN_VALUE) >>> j)) == 0) {
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
    result += "0: " + ((moveSet[0] == -1) ? "None" : moveSet[0] + " " + TILE_IDS[moveSet[0]]);
    result += "\n1: " + ((moveSet[1] == -1) ? "None" : moveSet[1] + " " + TILE_IDS[moveSet[1]]);
    result += "\n2: " + ((moveSet[2] == -1) ? "None" : moveSet[2] + " " + TILE_IDS[moveSet[2]]);
    return result;
  }

  public short[] getBoardRows() {
    return boardRows;
  }

  public int getScore() {
    return score;
  }
}
