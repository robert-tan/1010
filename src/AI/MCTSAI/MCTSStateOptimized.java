package AI.MCTSAI;

import java.util.List;
import Game.*;

public class MCTSStateOptimized {

  private GameOptimized game;
  private int visitCount;
  private double boardScore;

  public MCTSStateOptimized() {
    game = new GameOptimized();
  }

  public MCTSStateOptimized(MCTSStateOptimized state) {
    this.game = new GameOptimized(state.getGame());
    this.visitCount = state.getVisitCount();
    this.boardScore = state.getBoardScore();
  }

  public MCTSStateOptimized(GameOptimized game) {
    this.game = new GameOptimized(game);
  }

  public GameOptimized getGame() {
    return game;
  }

  public void setGame(GameOptimized game) {
    this.game = game;
  }

  public int getVisitCount() {
    return visitCount;
  }

  public void setVisitCount(int visitCount) {
    this.visitCount = visitCount;
  }

  public double getBoardScore() {
    return boardScore;
  }

  public void setBoardScore(int boardScore) {
    this.boardScore = boardScore;
  }

  void incrementVisit() {
    visitCount++;
  }

  void addScore(double score) {
    if (boardScore != Integer.MIN_VALUE) boardScore += score;
  }

  boolean randomPlay() {
    List<Byte[]> moves;
    byte which =  (byte) (((byte) (Math.random() * 10)) + 9); //
    moves = game.getAllValidMovesForTile(which);
    if (moves.size() == 0) return false;
    int random;
    random = (int) (Math.random() * moves.size());
    Byte[] move = moves.get(random);
    game.aiPlayPiece(move[0], move[1], move[2]);
    return true;
  }

}
