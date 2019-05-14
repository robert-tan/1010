package AI.MCTSAI;

import java.util.ArrayList;
import java.util.List;
import Game.*;

public class MCTSHeuristics {

  private static int positionScore(int[][] board) {
    int count = 0;
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] == 1) {
          if (i == 0 || i == 9 || j == 0 || j == 9) {
            count++;
          } else if (i == 1 || i == 8 || j == 1 || j == 8) {
            count += 2;
          } else if (i == 2 || i == 7 || j == 2 || j == 7) {
            count += 3;
          } else if (i == 3 || i == 6 || j == 3 || j == 6) {
            count += 4;
          } else {
            count += 5;
          }
        }
      }
    }
    return count;
  }

  private static int evaluateRowsAndCols(int[][] board) {
    int score = 0;
    for (int i = 0; i < 9; i++) {
      score += evaluateRow(board, i);
      score += evaluateCol(board, i);
    }
    return score;
  }

  private static int evaluateRow(int[][] board, int row) {
    int score = 0;
    for (int i = 1; i < 9; i += 3) {
      if (board[row][i] == 0 && board[row][i + 1] == 0 && board[row][i - 1] == 0) {
        score += 2;
      } else if (board[row][i] == 0 && (board[row][i - 1] == 0 || board[row][i + 1] == 0)){
        score += 1;
      } else if (board[row][i] == 0) {
        score -= 3;
      }
    }
    return score;
  }

  private static int evaluateCol(int[][] board, int col) {
    int score = 0;
    for (int i = 1; i < 9; i += 3) {
      if (board[i][col] == 0 && board[i - 1][col] == 0 && board[i + 1][col] == 0) {
        score += 2;
      } else if (board[i][col] == 0 && (board[i - 1][col] == 0 || board[i + 1][col] == 0)){
        score += 1;
      } else if (board[i][col] == 0) {
        score -= 3;
      }
    }
    return score;
  }

  //Get num edges
  //Get num corners

  private static boolean isCorner(int[][] board, int row, int col) {
    if (row > 0 && col > 0 //&& board[row - 1][col - 1] == 0
        && board[row - 1][col] == 0 && board[row][col - 1] == 0) {
      return true;
    }
    if (row > 0 && col < 9 //&& board[row - 1][col + 1] == 0
        && board[row - 1][col] == 0 && board[row][col + 1] == 0) {
      return true;
    }
    if (row < 9 && col > 0 //&& board[row + 1][col - 1] == 0
        && board[row + 1][col] == 0 && board[row][col - 1] == 0) {
      return true;
    }
    if (row < 9 && col < 9 //&& board[row + 1][col + 1] == 0
        && board[row + 1][col] == 0 && board[row][col + 1] == 0) {
      return true;
    }
    return false;
  }

  private static int getNumCorners(int[][] board) {
    int numCorners = 0;
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] == 1 && isCorner(board, i, j)) numCorners++;
      }
    }
    return numCorners;
  }

  private static List<Coordinate> getAdjacents(int[][] board, Coordinate c, int fill) {
    List<Coordinate> adjacents = new ArrayList<>();
    if (c.getCol() > 0 && board[c.getRow()][c.getCol() - 1] == fill) {
      adjacents.add(new Coordinate(c.getRow(), c.getCol() - 1));
    }
    if (c.getRow() > 0 && board[c.getRow() - 1][c.getCol()] == fill) {
      adjacents.add(new Coordinate(c.getRow() - 1, c.getCol()));
    }
    if (c.getCol() < 9 && board[c.getRow()][c.getCol() + 1] == fill) {
      adjacents.add(new Coordinate(c.getRow(), c.getCol() + 1));
    }
    if (c.getRow() < 9 && board[c.getRow() + 1][c.getCol()] == fill) {
      adjacents.add(new Coordinate(c.getRow() + 1, c.getCol()));
    }
    if (c.getRow() > 0 && c.getCol() > 0 && board[c.getRow() - 1][c.getCol() - 1] == fill) {
      adjacents.add(new Coordinate(c.getRow() - 1, c.getCol() - 1));
    }
    if (c.getRow() > 0 && c.getCol() < 9 && board[c.getRow() - 1][c.getCol() + 1] == fill) {
      adjacents.add(new Coordinate(c.getRow() - 1, c.getCol() + 1));
    }
    if (c.getRow() < 9 && c.getCol() > 0 && board[c.getRow() + 1][c.getCol() - 1] == fill) {
      adjacents.add(new Coordinate(c.getRow() + 1, c.getCol() - 1));
    }
    if (c.getRow() < 9 && c.getCol() < 9 && board[c.getRow() + 1][c.getCol() + 1] == fill) {
      adjacents.add(new Coordinate(c.getRow() + 1, c.getCol() + 1));
    }
    return adjacents;
  }

  private static Coordinate getNext(int[][] board, int fill) {
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] == fill) return new Coordinate(i, j);
      }
    }
    return null;
  }

//  private int getNumClusters(int[][] board) {
//    int[][] tempBoard = Game.Game.dupBoard(board);
//    int numClusters = 0;
//    Game.Coordinate c;
//    while((c = getNext(tempBoard, 1)) != null) {
//      numClusters++;
//      tempBoard[c.getRow()][c.getCol()] = 0;
//      while ((c = getNextAdjacent(tempBoard, c, 1)) != null) {
//        tempBoard[c.getRow()][c.getCol()] = 0;
//      }
//    }
//    return numClusters;
//  }

  private static int largestOpenSpace(int[][] board) {
    int[][] tempBoard = Game.dupBoard(board);
    int largestOpenSpace = 0;
    Coordinate c;
    while ((c = getNext(tempBoard, 0)) != null) {
      tempBoard[c.getRow()][c.getCol()] = 1;
      int curr = 1 + openSpaceHelper(tempBoard, c);
      if (curr > largestOpenSpace) {
        largestOpenSpace = curr;
      }
    }
    return largestOpenSpace;
  }

  private static int openSpaceHelper(int[][] board, Coordinate c) {
    List<Coordinate> adjacents = getAdjacents(board, c, 0);
    int result = adjacents.size();
    for (Coordinate d : adjacents) {
      board[d.getRow()][d.getCol()] = 1;
    }
    for (Coordinate d : adjacents) {
      result += openSpaceHelper(board, d);
    }
    return result;
  }

  private static int enclosedSingles(int[][] board) {
    int result = 0;
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] == 0) {
          List<Coordinate> adjacents = getAdjacents(board, new Coordinate(i, j), 0);
          if (adjacents.size() == 0) {
            result++;
          }
        }
      }
    }
    return result;
  }

  private static int numCantBePlaced(int[][] board) {
    int result = 0;
    for (TileID tile : TileID.values()) {
      if (!Game.canBePlaced(board, tile)) {
        result += tile.getSize();
      }
    }
    return result;
  }

  public static int numLonePiece(int[][] board) {
    int result = 0;
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] == 1) {
          if (!((i > 0 && board[i - 1][j] == 1) || (i < 9 && board[i + 1][j] == 1) ||
              (j > 0 && board[i][j - 1] == 1) || (j < 9 && board[i][j + 1] == 1))) {
            result++;
          }
        }
      }
    }
    return result;
  }

  private static int getNumEdges(int[][] board) {
    int numEdges = 0;
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] == 1 && getAdjacents(board, new Coordinate(i, j), 0).size() > 0) {
          numEdges++;
        }
      }
    }
    return numEdges;
  }

  private int largeSquareGaps(int[][] board) {
    return 0;
  }

  public static int evaluate(int[][] board) {
    return 1000 + 3 * largestOpenSpace(board) - 3 * getNumEdges(board) - 6 * getNumCorners(board) - positionScore(board)
        - 10 * enclosedSingles(board) - 10 * numCantBePlaced(board) - 5 * numLonePiece(board);
  }


}
