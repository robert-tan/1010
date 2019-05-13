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

  boolean randomPlay2() {
    List<Move> moves = game.getAllValidMoves();
    if (moves.size() == 0) return false;
    int random = (int) (Math.random() * moves.size());
    Move move = moves.get(random);
    game.playPiece(move.getRow(), move.getCol(), move.getTile());
    return true;
  }

  boolean randomPlay() {
    List<Move> moves;
    TileID randomTile = GameOptimized.TILE_IDS[(int) (Math.random() * 19)];
    moves = game.getAllValidMovesForTile(randomTile);
    if (moves.size() == 0) return false;
    int random;
    random = (int) (Math.random() * moves.size());
    Move move = moves.get(random);
    game.playPiece(move.getRow(), move.getCol(), move.getTile());
//    Move bestMove = moves.get(0);
//    int bestScore = Integer.MIN_VALUE;
//    for (Move move : moves) {
//      int[][] board = Game.dupBoard(game.getBoard());
//      int[] result = new int[1];
//      Game.placePiece(board, move.getRow(), move.getCol(), move.getTile(), result);
//      int score = MCTSHeuristics.evaluate(board);
//      if (score > bestScore) {
//        bestMove = move;
//        bestScore = score;
//      }
//    }
//    game.playPiece(bestMove.getRow(), bestMove.getCol(), bestMove.getTile());
    return true;
  }

}
