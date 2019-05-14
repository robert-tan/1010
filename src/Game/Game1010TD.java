package Game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

@Deprecated
public class Game1010TD {

  private TileID[] nextThree;
  private boolean[] hasNextThree;

  private String filePath = "";

  private List<INDArray> boardList = new ArrayList<>();
  private List<Double> boardProbabilityList = new ArrayList<>();

  private boolean updateAIAutomatic = false;
  private int gameDecision = 0;
  private boolean aiLoad = false;
  private int updateLimit = 0;
  private int gameCounter = 0;
  private boolean updateAIFile = false;

  public boolean isAiLoad() {
    return aiLoad;
  }

  public INDArray getNextBestMove(INDArray board) {
    double maxNuber = 0d;
    int indexInArray = 0;
    INDArray nextMove = null;


    return null;
  }

  public void getNextThree() {
    nextThree = new TileID[3];
    hasNextThree = new boolean[3];
    for (int i = 0; i < 3; i++) {
      nextThree[i] = TileID.getRandomID();
      hasNextThree[i] = true;
    }
  }

  private boolean checkPlacePiece(INDArray board, Move move) {
    List<Coordinate> offsets = move.getTile().getOffsets();
    for (Coordinate c : offsets) {
      if (move.getRow() + c.getRow() >= 10 || move.getCol() + c.getCol() >= 10 ||
          board.getInt(move.getRow() + c.getRow(), move.getCol() + c.getCol()) == 1) {
        return false;
      }
    }
    return true;
  }

  private List<Move> getPossibleMoves(INDArray board) {
    List<Move> moves = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      if (hasNextThree[i]) {
        for (int j = 0; j < 10; j++) {
          for (int k = 0; k < 10; k++) {
            Move move = new Move(j, k, nextThree[i]);
            if (checkPlacePiece(board, move)) {
              moves.add(move);
            }
          }
        }
      }
    }
    return moves;
  }

  private void updateReward() {
    if (!updateAIAutomatic) {
      return;
    }
    if ((gameCounter >= updateLimit) && updateAIAutomatic) {
      gameCounter = 0;
      updateAIFile = true;
    }

    double probVal = 0d;
    for (int i = boardList.size() - 1; i >= 0; i--) {

    }
  }

  private List<INDArray> getPossibleBoards(INDArray board, List<Move> moves) {
    List<INDArray> result = new ArrayList<>();
    for (int i = 0; i < moves.size(); i++) {
      INDArray inputArray = Nd4j.zeros(10, 10);
      Nd4j.copy(board, inputArray);
      placePiece(inputArray, moves.get(i));
      result.add(inputArray);
    }
    return result;
  }

  private void readProbTable() {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line = "";
      while ((line = br.readLine()) != null) {
        INDArray input = Nd4j.zeros(10, 10);
        String[] nextLine = line.split(" ");
        String tempLine1 = nextLine[0];
        String tempLine2 = nextLine[1];
        String testLine[] = tempLine1.split(":");
        for (int i = 0; i < 10; i++) {
          for (int j = 0; j < 10; j++) {
            double number = Double.parseDouble(testLine[i]);
            input.putScalar(new int[]{i, j}, number);
          }
        }
        double doubleNumber = Double.parseDouble(tempLine2);
        boardList.add(input);
        boardProbabilityList.add(doubleNumber);
        aiLoad = true;
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  private void saveToFile() {
    try (FileWriter writer = new FileWriter(filePath);) {
      for (int i = 0; i < boardList.size(); i++) {
        INDArray array = boardList.get(i);
        double rewardVal = boardProbabilityList.get(i);
        String tempString = array.toString()
            .replace('[', ' ')
            .replace(']', ' ')
            .replace(',', ':')
            .replaceAll("\\s", "");
        String output = tempString + " " + String.valueOf(rewardVal);
        writer.append(output);
        writer.append('\r');
        writer.append('\n');
        writer.flush();
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  private boolean isValidMove(INDArray board, Move move) {
    for (int i = 0; i < 3; i++) {
      if (hasNextThree[i] && nextThree[i].equals(move.getTile()) && checkPlacePiece(board, move)) {
        return true;
      }
    }
    return false;
  }

  private boolean removeHasNext(TileID tile) {
    for (int i = 0; i < 3; i++) {
      if (hasNextThree[i] && nextThree[i].equals(tile)) {
        hasNextThree[i] = false;
        //Check get next;
        return true;
      }
    }
    return false;
  }

  private boolean placePiece(INDArray board, Move move) {
    if (isValidMove(board, move)) {
      List<Coordinate> offsets = move.getTile().getOffsets();
      if (checkPlacePiece(board, move)) {
        for (Coordinate c : offsets) {
          board.put(move.getRow() + c.getRow(), move.getCol() + c.getCol(), 1);
          checkLineClear(board, move.getRow() + c.getRow(), move.getCol() + c.getCol());
        }
        removeHasNext(move.getTile());
        return true;
      }
    }
    return false;
  }

  private void checkLineClear(INDArray board, int row, int col) {
    boolean clearRow = true;
    boolean clearCol = true;
    for (int i = 0; i < 10 ; i++) {
      if (board.getInt(i, col) == 0) {
        clearCol = false;
      }
      if (board.getInt(row, i) == 0) {
        clearRow = false;
      }
    }
    if (clearRow) {
      clearRow(board, row);
    }
    if (clearCol) {
      clearCol(board, col);
    }
  }

  private void clearCol(INDArray board, int col) {
    for (int i = 0; i < 10; i++) {
      board.put(i, col, 0);
    }
  }

  private void clearRow(INDArray board, int row) {
    for (int i = 0; i < 10; i++) {
      board.put(row, i, 0);
    }
  }

}
