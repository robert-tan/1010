import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MCTSState {

  private Game game;
  private int visitCount;
  private double boardScore;

  public MCTSState() {
    game = new Game();
  }

  public MCTSState(MCTSState state) {
    this.game = new Game(state.getGame());
    this.visitCount = state.getVisitCount();
    this.boardScore = state.getBoardScore();
  }

  public MCTSState(Game game) {
    this.game = new Game(game);
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
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

//  public List<MCTSState> getAllPossibleStates() {
//    List<MCTSState> result = new ArrayList<>();
//    List<Move> moves = game.getAllValidMoves();
//    for (Move move : moves) {
//      Game temp = new Game(game);
//      temp.playPiece(move.getRow(), move.getCol(), move.getTile());
//      result.add(new MCTSState(temp));
//    }
//    return result;
//  }

  void incrementVisit() {
    visitCount++;
  }

//  void updateScore(double score) {
//    boardScore = ((boardScore * (visitCount - 1)) + score) / visitCount;
//  }

  void addScore(double score) {
    if (boardScore != Integer.MIN_VALUE) boardScore += score;
  }

  void randomPlay2() {
    List<Move> moves = game.getAllValidMoves();
    int random = (int) (Math.random() * moves.size());
    Move move = moves.get(random);
    game.playPiece(move.getRow(), move.getCol(), move.getTile());
  }

  void randomPlay() {
    List<Move> moves = game.getAllValidMoves();
    int random;
//    if (moves.size() > 10) {
//      moves.forEach(m -> {
//        Game temp = new Game(game);
//        temp.playPiece(m.getRow(), m.getCol(), m.getTile());
//        m.setMoveScore(MCTSHeuristics.evaluate(temp.getBoard()));
//      });
//      Collections.sort(moves);
//      random = (int) (Math.random() * 10);
//      random = moves.size() - random - 1;
//    } else {
    random = (int) (Math.random() * moves.size());
//    }
    Move move = moves.get(random);
    game.playPiece(move.getRow(), move.getCol(), move.getTile());
  }

}
