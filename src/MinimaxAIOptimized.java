import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MinimaxAIOptimized implements GameAI {

  private static final int DEPTH = 3;

  private GameOptimized game;

  @Override
  public void setGame(Game game) {

  }

  public void setGame(GameOptimized game) {
    this.game = game;
  }

  @Override
  public Move getNextMove() {
//    long start = System.currentTimeMillis();
//    Move move = minimaxBestMove(getDepth(), game);
//    long end = System.currentTimeMillis();
//    System.out.println("Time take to calculate move: " + (end - start));
//    return move;
    return null;
  }

  public Byte[] getNextMoveOptimized() {
    long start = System.currentTimeMillis();
    Byte[] move = minimaxBestMove(getDepth(), game);
    long end = System.currentTimeMillis();
    System.out.println("Time take to calculate move: " + (end - start));
    return move;
  }

  private int getDepth() {
    int num = 0;
    for (int i = 0; i < 3; i++) {
      if (game.getMoveSet()[i] != -1) num++;
    }
    return num;
  }

  private int minimaxBestMoveAI(int depth, GameOptimized game) {
    int bestScore;
    if (depth == 0) {
      bestScore = evaluate(game.getBoardRows());
    } else {
      List<Integer> scores = new ArrayList<>();
      if (game.getMoveSet()[0] != -1 && game.getMoveSet()[1] != -1 && game.getMoveSet()[2] != -1) {
        for (byte i = 0; i < 19; i++) {
          Set<Byte[]> moves = game.getAllValidMovesForTile(i);
          int bestScoreTile = Integer.MIN_VALUE;
          int currentScoreTile;
          for (Byte[] move : moves) {
            GameOptimized temp = new GameOptimized(game);
            temp.aiPlayPiece(move[0], move[1], move[2]);
            currentScoreTile = minimaxBestMoveAI(depth - 1, temp);
            if (currentScoreTile > bestScoreTile) {
              bestScoreTile = currentScoreTile;
            }
          }
          scores.add(bestScoreTile);
        }
        Collections.sort(scores);
        bestScore = scores.get(2);
      } else {
        Set<Byte[]> moves = game.getAllValidMoves();
        bestScore = Integer.MIN_VALUE;
        int currentScore;
        for (Byte[] move : moves) {
          GameOptimized temp = new GameOptimized(game);
          temp.aiPlayPiece(move[0], move[1], move[2]);
          currentScore = minimaxBestMoveAI(depth - 1, temp);
          if (currentScore >= bestScore) {
            bestScore = currentScore;
          }
        }
      }
    }
    return bestScore;
  }

  // Depth > 0
  private Byte[] minimaxBestMove(int depth, GameOptimized game) {
    Set<Byte[]> moves = game.getAllValidMoves();
    Byte[] bestMove = null;
    int bestScore = Integer.MIN_VALUE;
    int currentScore;
    for (Byte[] move : moves) {
      GameOptimized temp = new GameOptimized(game);
      temp.playPiece(move[0], move[1], move[2]);
      currentScore = minimaxBestMoveAI(depth - 1, temp);
      if (currentScore >= bestScore) {
        bestScore = currentScore;
        bestMove = move;
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

  private int evaluate2(int[][] board) {
    int numEdges = 0;
    int numSquareGaps = 0;
    int numCorners = 0;
    int openSpaceScore = 0;
    int positionScore = 0;
    int numEnclosedSingles = 0;
    int numCantBePlaced = 0;
    int numLonePiece = 0;

    Set<TileID> canBePlaced = new HashSet<>();

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {

        if (i < 6 && j < 6) {
          boolean gap = true;
          for (int m = 0; m < 5; m++) {
            for (int n = 0; n < 5; n++) {
              if (board[i + m][j + n] == 1) {
                gap = false;
                break;
              }
            }
            if (!gap) {
              break;
            }
          }
          if (gap) numSquareGaps++;
        }

        if (board[i][j] == 1 && getAdjacents(board, new Coordinate(i, j), 0, true).size() > 0) {
          numEdges++;
        }

        if (i > 0 && i < 9 && j > 0 && j < 9 && board[i][j] == 1) {
          if (!((board[i - 1][j] == 1) || (board[i + 1][j] == 1) ||
              (board[i][j - 1] == 1) || (board[i][j + 1] == 1))) {
            numLonePiece++;
          }
        }

        for (TileID tile : TileID.values()) {
          if (!canBePlaced.contains(tile) && Game.checkPlacePiece(board, i, j, tile)) {
            canBePlaced.add(tile);
          }
        }

        if (board[i][j] == 0) {
          List<Coordinate> adjacents = getAdjacents(board, new Coordinate(i, j), 0, true);
          if (adjacents.size() == 0) {
            numEnclosedSingles++;
          }
        }

        if (board[i][j] == 1 && isCorner(board, i, j)) numCorners++;

        if (board[i][j] == 1) {
          if ((i == 0 || i == 9) && (j == 0 || j == 9)) {

          } else if (i == 0 || i == 9 || j == 0 || j == 9) {
            positionScore++;
          } else if (i == 1 || i == 8 || j == 1 || j == 8) {
            positionScore += 2;
          } else if (i == 2 || i == 7 || j == 2 || j == 7) {
            positionScore += 3;
          } else if (i == 3 || i == 6 || j == 3 || j == 6) {
            positionScore += 4;
          } else {
            positionScore += 6;
          }
        }

      }
    }

    openSpaceScore = largestOpenSpace(board);

    numCantBePlaced = 19 - canBePlaced.size();

    return 3 * openSpaceScore - 3 * numEdges - 8 * numCorners - positionScore - 10 * numEnclosedSingles
        - 10 * numCantBePlaced - 10 * numLonePiece + 10 * numSquareGaps;
  }

  public int evaluate(short[] board) {
    return 0 - positionScore(board);
  }

  private int positionScore(short[] board) {
    int result = 0;
    short[] vals0 = {
        (short) 0b1111111111000000,
        (short) 0b1000000001000000,
        (short) 0b1000000001000000,
        (short) 0b1000000001000000,
        (short) 0b1000000001000000,
        (short) 0b1000000001000000,
        (short) 0b1000000001000000,
        (short) 0b1000000001000000,
        (short) 0b1000000001000000,
        (short) 0b1111111111000000,
    };
    short[] vals1 = {
        (short) 0b0,
        (short) 0b0111111110000000,
        (short) 0b0100000010000000,
        (short) 0b0100000010000000,
        (short) 0b0100000010000000,
        (short) 0b0100000010000000,
        (short) 0b0100000010000000,
        (short) 0b0100000010000000,
        (short) 0b0111111110000000,
        (short) 0b0,
    };

    short[] vals2 = {
        (short) 0b0,
        (short) 0b0,
        (short) 0b0011111100000000,
        (short) 0b0010000100000000,
        (short) 0b0010000100000000,
        (short) 0b0010000100000000,
        (short) 0b0010000100000000,
        (short) 0b0011111100000000,
        (short) 0b0,
        (short) 0b0,
    };

    short[] vals3 = {
        (short) 0b0,
        (short) 0b0,
        (short) 0b0,
        (short) 0b0001111000000000,
        (short) 0b0001001000000000,
        (short) 0b0001001000000000,
        (short) 0b0001111000000000,
        (short) 0b0,
        (short) 0b0,
        (short) 0b0,
    };

    short[] vals4 = {
        (short) 0b0,
        (short) 0b0,
        (short) 0b0,
        (short) 0b0,
        (short) 0b0000110000000000,
        (short) 0b0000110000000000,
        (short) 0b0,
        (short) 0b0,
        (short) 0b0,
        (short) 0b0,
    };

    for (int i = 0; i < 10; i++) {
      result += Integer.bitCount(0xFFFF & (vals0[i] & board[i]));
      result += 2 * Integer.bitCount(0xFFFF & (vals1[i] & board[i]));
      result += 3 * Integer.bitCount(0xFFFF & (vals2[i] & board[i]));
      result += 4 * Integer.bitCount(0xFFFF & (vals3[i] & board[i]));
      result += 6 * Integer.bitCount(0xFFFF & (vals4[i] & board[i]));
    }
    return result;
  }

}
