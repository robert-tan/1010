package AI.MCTSAI;

import java.util.List;
import Game.*;

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
//    List<Game.Move> moves = game.getAllValidMoves();
//    for (Game.Move move : moves) {
//      Game.Game temp = new Game.Game(game);
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
    TileID randomTile = GameOptimized.TILE_IDS[((int) (Math.random() * 10)) + 9]; //
    moves = game.getAllValidMovesForTile(randomTile);
    if (moves.size() == 0) return false;
    int random;
    random = (int) (Math.random() * moves.size());
    Move move = moves.get(random);
    game.playPieceAI(move.getRow(), move.getCol(), move.getTile());
//    Game.Move bestMove = moves.get(0);
//    int bestScore = Integer.MIN_VALUE;
//    for (Game.Move move : moves) {
//      int[][] board = Game.Game.dupBoard(game.getBoard());
//      int[] result = new int[1];
//      Game.Game.placePiece(board, move.getRow(), move.getCol(), move.getTile(), result);
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
