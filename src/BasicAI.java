import java.util.Collections;
import java.util.List;

public class BasicAI implements  GameAI {

  private Game game;

  public void setGame(Game game) {
    this.game = game;
  }

  /*
   * Greedy algorithm
   */
  @Override
  public Move getNextMove() {
    List<Move> moves = game.getAllValidMoves();
    Collections.shuffle(moves);
    Move bestMove = null;
    int bestScore = 0;
    for (Move move : moves) {
      int[][] board = Game.dupBoard(game.getBoard());
      int[] result = new int[1];
      Game.placePiece(board, move.getRow(), move.getCol(), move.getTile(), result);
      if (result[0] * 10 + move.getTile().getSize() > bestScore) {
        bestMove = move;
        bestScore = result[0] * 10 + move.getTile().getSize();
      }
    }
    return bestMove;
  }

}
