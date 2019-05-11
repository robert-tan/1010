import java.util.ArrayList;
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

  void updateScore(double score) {
    if (visitCount == 1) {
      boardScore = score;
    } else {
      boardScore = ((boardScore * visitCount) + score) / visitCount - 1;
    }
  }

  void randomPlay() {
    List<Move> moves = game.getAllValidMoves();
    int random = (int) (Math.random() * moves.size());
    Move move = moves.get(random);
    game.playPiece(move.getRow(), move.getCol(), move.getTile());
  }
}
