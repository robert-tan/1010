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
      tempNode.getState().updateScore(score);
      tempNode = tempNode.getParent();
    }
  }

  private int simulateRandomPlayout(MCTSNode node) {
    MCTSNode tempNode = new MCTSNode(node);
    MCTSState tempState = tempNode.getState();
    int i = 0;
    while (i < 10 && tempState.getGame().getAllValidMoves().size() != 0) {
      tempState.randomPlay();
      i++;
    }
    return MCTSHeuristics.evaluate(tempState.getGame().getBoard());
  }

  @Override
  public Move getNextMove() {
    long start = System.currentTimeMillis();
    long end = start + 5000;

    MCTSNode root = new MCTSNode(new MCTSState(game));

    while (System.currentTimeMillis() < end) {
      MCTSNode promisingNode = selectPromisingNode(root);

      if (!promisingNode.getState().getGame().isFinished()) expandNode(promisingNode);

      MCTSNode nodeToExplore = promisingNode;
      if (promisingNode.getChildArray().size() > 0) {
        nodeToExplore = promisingNode.getRandomChildNode();
      }
      int playoutResult = simulateRandomPlayout(nodeToExplore);
      backPropagation(nodeToExplore, playoutResult);
    }

    MCTSNode winnerNode = root.getChildWithMaxScore();
    return winnerNode.getMovePlayed();
  }

  @Override
  public void setGame(Game game) {
    this.game = game;
  }

  public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
    if (nodeVisit == 0) return Integer.MAX_VALUE;
    return (nodeWinScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit)
        / (double) nodeVisit);
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
