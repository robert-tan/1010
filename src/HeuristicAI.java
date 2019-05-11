import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HeuristicAI implements GameAI {

  private Game game;

  @Override
  public void setGame(Game game) {
    this.game = game;
  }

  @Override
  public Move getNextMove() {
    List<Move> moves = game.getAllValidMoves();
    Collections.shuffle(moves);
    Move bestMove = null;
    int bestScore = Integer.MIN_VALUE;
    for (Move move : moves) {
      int[][] board = Game.dupBoard(game.getBoard());
      int[] result = new int[1];
      Game.placePiece(board, move.getRow(), move.getCol(), move.getTile(), result);
      int score = evaluate(board);
      if (score > bestScore) {
        bestMove = move;
        bestScore = score;
      }
    }
    return bestMove;
  }

  private int positionScore(int[][] board) {
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
            count += 6;
          }
        }
      }
    }
    return count;
  }

  private int evaluateRowsAndCols(int[][] board) {
    int score = 0;
    for (int i = 0; i < 9; i++) {
      score += evaluateRow(board, i);
      score += evaluateCol(board, i);
    }
    return score;
  }

  private int evaluateRow(int[][] board, int row) {
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

  private int evaluateCol(int[][] board, int col) {
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

  private boolean isCorner(int[][] board, int row, int col) {
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

  private int getNumCorners(int[][] board) {
    int numCorners = 0;
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] == 1 && isCorner(board, i, j)) numCorners++;
      }
    }
    return numCorners;
  }

  private List<Coordinate> getAdjacents(int[][] board, Coordinate c, int fill, boolean diag) {
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
    if (diag) {
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
    }
    return adjacents;
  }

  private Coordinate getNext(int[][] board, int fill) {
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] == fill) return new Coordinate(i, j);
      }
    }
    return null;
  }

//  private int getNumClusters(int[][] board) {
//    int[][] tempBoard = Game.dupBoard(board);
//    int numClusters = 0;
//    Coordinate c;
//    while((c = getNext(tempBoard, 1)) != null) {
//      numClusters++;
//      tempBoard[c.getRow()][c.getCol()] = 0;
//      while ((c = getNextAdjacent(tempBoard, c, 1)) != null) {
//        tempBoard[c.getRow()][c.getCol()] = 0;
//      }
//    }
//    return numClusters;
//  }

  private int largestOpenSpace(int[][] board) {
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

  private int openSpaceHelper(int[][] board, Coordinate c) {
    List<Coordinate> adjacents = getAdjacents(board, c, 0, false);
    int result = adjacents.size();
    for (Coordinate d : adjacents) {
      board[d.getRow()][d.getCol()] = 1;
    }
    for (Coordinate d : adjacents) {
      result += openSpaceHelper(board, d);
    }
    return result;
  }

  private int enclosedSingles(int[][] board) {
    int result = 0;
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] == 0) {
          List<Coordinate> adjacents = getAdjacents(board, new Coordinate(i, j), 0, true);
          if (adjacents.size() == 0) {
            result++;
          }
        }
      }
    }
    return result;
  }

  private int numCantBePlaced(int[][] board) {
    int result = 0;
    for (TileID tile : TileID.values()) {
      if (!Game.canBePlaced(board, tile)) {
        result += tile.getSize();
      }
    }
    return result;
  }

//  private int numLonePiece(int[][] board) {
//    int result = 0;
//    for (int i = 0; i < 10; i++) {
//      for (int j = 0; j < 10; j++) {
//        if (board[i][j] == 1) {
//          if (!((i > 0 && board[i - 1][j] == 1) || (i < 9 && board[i + 1][j] == 1) ||
//              (j > 0 && board[i][j - 1] == 1) || (j < 9 && board[i][j + 1] == 1))) {
//            result++;
//          }
//        }
//      }
//    }
//    return result;
//  }

  private int numLonePiece(int[][] board) {
    int result = 0;
    for (int i = 1; i < 9; i++) {
      for (int j = 1; j < 9; j++) {
        if (board[i][j] == 1) {
          if (!((board[i - 1][j] == 1) || (board[i + 1][j] == 1) ||
              (board[i][j - 1] == 1) || (board[i][j + 1] == 1))) {
            result++;
          }
        }
      }
    }
    return result;
  }

  private int getNumEdges(int[][] board) {
    int numEdges = 0;
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] == 1 && getAdjacents(board, new Coordinate(i, j), 0, true).size() > 0) {
          numEdges++;
        }
      }
    }
    return numEdges;
  }

  private int numSquareGaps(int[][] board, int size) {
    int result = 0;
    for (int i = 0; i < 11 - size; i++) {
      for (int j = 0; j < 11 - size; j++) {
        boolean gap = true;
        for (int m = 0; m < size; m++) {
          for (int n = 0; n < size; n++) {
            if (board[i + m][j + n] == 1) {
              gap = false;
              break;
            }
          }
          if (!gap) {
            break;
          }
        }
        if (gap) result++;
      }
    }
    return result;
  }

  private int evaluate(int[][] board) {
    return 3 * largestOpenSpace(board) - 3 * getNumEdges(board) - 8 * getNumCorners(board) - positionScore(board)
        - 10 * enclosedSingles(board) - 10 * numCantBePlaced(board) - 10 * numLonePiece(board) + 10 * numSquareGaps(board, 5);
  }

}
