package AI.MCTSAI;

import AI.*;
import Game.*;
import java.util.Collections;
import java.util.Comparator;

public class MCTSBasicAI implements GameAI {

  private Move[] moves = new Move[3];

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

  private void expandNodeOrig(MCTSNode node, TileID[] perm, int which) {
    for (Move move : node.getState().getGame().getAllValidMovesForTile(perm[which])) {
      Game temp = new Game(node.getState().getGame());
      temp.playPiece(move.getRow(), move.getCol(), move.getTile());
      MCTSNode newNode = new MCTSNode(new MCTSState(temp));
      newNode.setParent(node);
      node.getChildArray().add(newNode);
      newNode.setMovePlayed(move);
      if (which == 0 || which == 1) {
        expandNodeOrig(newNode, perm, which + 1);
      }
    }
  }

  private void expandNode(MCTSNode node) {
    for (Move move : node.getState().getGame().getAllValidMovesForAllTiles()) {
      Game temp = new Game(node.getState().getGame());
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
    if (tempState.getGame().numUnplayedPiece() != 3) {
      tempState.setBoardScore(Integer.MIN_VALUE);
      return 0;
    }
    int i = 0;
    while (tempState.randomPlay()) {
      i++;
    }
//    int numPieces = tempState.getGame().getNumPiecesOnBoard();
//    if (numPieces < 30) {
//      return (i > 20) ? 3 : 1;
//    } else if (numPieces < 50) {
//      return (i > 15) ? 2 : 1;
//    }
//    if (i <= 2) {
//      tempState.setBoardScore(Integer.MIN_VALUE);
//      return 0;
//    }
//    return MCTSHeuristics.evaluate(tempState.getGame().getBoard());
    return i;
  }

  private TileID[][] getTileSet(Game game) {
    TileID[] set = game.getNextThree();
    return new TileID[][]{
        {set[0], set[1], set[2]},
        {set[0], set[2], set[1]},
        {set[1], set[0], set[2]},
        {set[1], set[2], set[0]},
        {set[2], set[0], set[1]},
        {set[2], set[1], set[0]}
    };
  }

//  private List<Game.Move> getNextMoves() {
////    Game.TileID[][] perms = getTileSet();
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

  private void getNextMoves() {

    MCTSNode[] roots = new MCTSNode[6];
    TileID[][] perms = getTileSet(game);
    for (int i = 0; i < 6; i++) {
      long expandStart = System.currentTimeMillis();
      roots[i] = new MCTSNode(new MCTSState(game));
      expandNodeOrig(roots[i], perms[i], 0);
      System.out.println("Expansion took: " + (System.currentTimeMillis() - expandStart) + " ms");
    }

    long start = System.currentTimeMillis();
    long end = start + 2000;
    int j = 0;
    while (System.currentTimeMillis() < end) {
//      System.out.println("Iteration number: " + j);
      j++;
      for (int i = 0; i < 6; i++) {
        MCTSNode promisingNode = selectPromisingNode(roots[i]);

        if (promisingNode == roots[i] && j == 1) {

          continue;
        } else if (promisingNode == roots[i]){
          continue;
        } else {
//          if (!promisingNode.getState().getGame().isFinished()) {
//            expandNode(promisingNode);
//          }
        }

        MCTSNode nodeToExplore = promisingNode;
        if (promisingNode.getChildArray().size() > 0) {
          nodeToExplore = promisingNode.getRandomChildNode();
        }
        int playoutResult = simulateRandomPlayout(nodeToExplore);
        backPropagation(nodeToExplore, playoutResult);
      }
    }

    MCTSNode winnerTree = roots[0];
    int movesEvaluated = 0;
    int i = 0;
    for (MCTSNode root : roots) {
      if (root.getState().getBoardScore() > winnerTree.getState().getBoardScore()) {
        winnerTree = root;
        i++;
      }
      movesEvaluated += root.getState().getVisitCount();
    }
    System.out.println("Winner tree: " + i);
    for (MCTSNode node : winnerTree.getChildArray()) {
      System.out.println("Visit Count: " + node.getState().getVisitCount() + " " + node.movePlayed + " has score: " + node.getState().getBoardScore());
    }
    System.out.println("Moves Evaluated: " + movesEvaluated);
    MCTSNode t0 = winnerTree.getChildWithMaxScore();
    moves[0] = t0.movePlayed;
    MCTSNode t1 = null;
    MCTSNode t2 = null;
    if (t0.getChildArray().size() != 0) {
      t1 = t0.getChildWithMaxScore();
      moves[1] = t1.movePlayed;
      if (t1.getChildArray().size() != 0) {
        t2 = t1.getChildWithMaxScore();
        moves[2] = t2.movePlayed;
      }
    }
    System.out.println(moves[0]);
    System.out.println(moves[1]);
    System.out.println(moves[2]);
  }

  @Override
  public Move getNextMove() {
    if (moves[0] != null) {
      Move move = moves[0];
      moves[0] = null;
      return move;
    }
    if (moves[1] != null) {
      Move move = moves[1];
      moves[1] = null;
//      try {
//        Thread.sleep(200);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
      return move;
    }
    if (moves[2] != null) {
      Move move = moves[2];
      moves[2] = null;
//      try {
//        Thread.sleep(200);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
      return move;
    }
    getNextMoves();
    return getNextMove();
  }

  @Override
  public void setGame(Game game) {
    this.game = game;
  }

  private static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
    if (nodeVisit == 0) return Integer.MAX_VALUE;
    return ((nodeWinScore) / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit)
        / (double) nodeVisit);
  }

  private static MCTSNode findBestNodeWithUCT(MCTSNode node) {
    int parentVisit = node.getState().getVisitCount();
    return Collections.max(
        node.getChildArray(),
        Comparator.comparing(c -> uctValue(parentVisit, c.getState().getBoardScore(),
            c.getState().getVisitCount()))
    );
  }

}
