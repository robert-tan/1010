import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MCTSBasicAI implements GameAI {

  //???
  private int level = 3;

  private Game game;

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  private MCTSNode selectPromisingNode(MCTSNode root) {
    MCTSNode node = root;
    while (node.getChildArray().size() != 0) {
      node = findBestNodeWithUCT(node);
    }
    return node;
  }

  private void expandNode(MCTSNode node) {
    for (Move move : node.getState().getGame().getAllValidMoves()) {
      Game temp = new Game(game);
      temp.playPiece(move.getRow(), move.getCol(), move.getTile());
      MCTSNode newNode = new MCTSNode(new MCTSState(temp));
      newNode.setParent(node);
      node.getChildArray().add(newNode);
      newNode.setMovePlayed(move);
    }
  }

  private void backPropagation(MCTSNode nodeToExplore, double score) {
    MCTSNode tempNode = nodeToExplore;
    while (tempNode != null) {
      tempNode.getState().incrementVisit();
      tempNode.getState().addScore(score);
//      tempNode.getState().updateScore(score);
      tempNode = tempNode.getParent();
    }
  }

  private int simulateRandomPlayout(MCTSNode node) {
    MCTSNode tempNode = new MCTSNode(node);
    MCTSState tempState = tempNode.getState();
    int i = 0;
    int j = 0;
    while (tempState.getGame().getAllValidMoves().size() != 0) {
      tempState.randomPlay();
      for (TileID tile : TileID.values()) {
        if (Game.canBePlaced(tempState.getGame().getBoard(), tile)) i++;
      }
    }
    return i;
  }

//  private TileID[][] getTileSet() {
//    TileID[] set = game.getNextThree();
//    return new TileID[][]{
//        set,
//        {set[0], set[2], set[1]},
//        {set[1], set[0], set[2]},
//        {set[1], set[2], set[0]},
//        {set[2], set[0], set[1]},
//        {set[2], set[1], set[0]}
//    }
//  }

//  private List<Move> getNextMoves() {
////    TileID[][] perms = getTileSet();
//    long start = System.currentTimeMillis();
//    long end = start + 5000;
//
//    MCTSNode root = new MCTSNode(new MCTSState(game));
//
//    while (System.currentTimeMillis() < end) {
//      MCTSNode promisingNode = selectPromisingNode(root);
//
//      if (!promisingNode.getState().getGame().isFinished()) {
//        expandNode(promisingNode);
//      }
//
//      MCTSNode nodeToExplore = promisingNode;
//      if (promisingNode.getChildArray().size() > 0) {
//        nodeToExplore = promisingNode.getRandomChildNode();
//      }
//      int playoutResult = simulateRandomPlayout(nodeToExplore);
//      backPropagation(nodeToExplore, playoutResult);
//    }
//
//    MCTSNode winnerNode = root.getChildWithMaxScore();
//    for (MCTSNode node : root.getChildArray()) {
//      System.out.println("Visit Count: " + node.getState().getVisitCount() + " " + node.movePlayed + " has score: " + node.getState().getBoardScore());
//    }
//    return winnerNode.getMovePlayed();
//  }

  @Override
  public Move getNextMove() {
    long start = System.currentTimeMillis();
    long end = start + 5000;

    MCTSNode root = new MCTSNode(new MCTSState(game));

    while (System.currentTimeMillis() < end) {
      MCTSNode promisingNode = selectPromisingNode(root);

      if (!promisingNode.getState().getGame().isFinished()) {
        expandNode(promisingNode);
      }

      MCTSNode nodeToExplore = promisingNode;
      if (promisingNode.getChildArray().size() > 0) {
        nodeToExplore = promisingNode.getRandomChildNode();
      }
      int playoutResult = simulateRandomPlayout(nodeToExplore);
      backPropagation(nodeToExplore, playoutResult);
    }

    MCTSNode winnerNode = root.getChildWithMaxScore();
    for (MCTSNode node : root.getChildArray()) {
      System.out.println("Visit Count: " + node.getState().getVisitCount() + " " + node.movePlayed + " has score: " + node.getState().getBoardScore());
    }
    return winnerNode.getMovePlayed();
  }

  @Override
  public void setGame(Game game) {
    this.game = game;
  }

  public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
    if (nodeVisit == 0) return Integer.MAX_VALUE;
    return ((nodeWinScore / 1000) / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit)
        / (double) nodeVisit);
//    return 0.5 + 1.41 * Math.sqrt(Math.log(totalVisit)
//        / (double) nodeVisit);
  }

  static MCTSNode findBestNodeWithUCT(MCTSNode node) {
    int parentVisit = node.getState().getVisitCount();
    return Collections.max(
        node.getChildArray(),
        Comparator.comparing(c -> uctValue(parentVisit, (double) c.getState().getBoardScore(),
            c.getState().getVisitCount()))
    );
  }

}
