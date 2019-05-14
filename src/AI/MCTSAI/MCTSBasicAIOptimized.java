package AI.MCTSAI;

import AI.*;
import Game.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MCTSBasicAIOptimized {

  private Byte[][] moves = new Byte[3][];

  private GameOptimized game;

  private MCTSNodeOptimized selectPromisingNode(MCTSNodeOptimized root) {
    MCTSNodeOptimized node = root;
    while (node.getChildArray().size() != 0) {
      node = findBestNodeWithUCT(node);
    }
    return node;
  }

  private void expandNodeOrig(MCTSNodeOptimized node, byte[] perm, int which) {
    for (Byte[] move : node.getState().getGame().getAllValidMovesForTile(perm[which])) {
      GameOptimized temp = new GameOptimized(node.getState().getGame());
      temp.playPiece(move[0], move[1], move[2]);
      MCTSNodeOptimized newNode = new MCTSNodeOptimized(new MCTSStateOptimized(temp));
      newNode.setParent(node);
      node.getChildArray().add(newNode);
      newNode.setMovePlayed(move);
      if (which == 0 || which == 1) {
        expandNodeOrig(newNode, perm, which + 1);
      }
    }
  }

  private void backPropagation(MCTSNodeOptimized nodeToExplore, double score) {
    MCTSNodeOptimized tempNode = nodeToExplore;
    while (tempNode != null) {
      tempNode.getState().incrementVisit();
      tempNode.getState().addScore(score);
      tempNode = tempNode.getParent();
    }
  }

  private int simulateRandomPlayout(MCTSNodeOptimized node) {
    MCTSNodeOptimized tempNode = new MCTSNodeOptimized(node);
    MCTSStateOptimized tempState = tempNode.getState();
    if (tempState.getGame().getNumUnplayedPiece() != 0) {
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

  private void getNextMoves() {
    MCTSNodeOptimized[] roots = new MCTSNodeOptimized[6];
    byte[][] perms = game.getMoveSetPermutations();
    for (int i = 0; i < 6; i++) {
//      long expandStart = System.currentTimeMillis();
      roots[i] = new MCTSNodeOptimized(new MCTSStateOptimized(game));
      expandNodeOrig(roots[i], perms[i], 0);
//      System.out.println("Expansion took: " + (System.currentTimeMillis() - expandStart) + " ms");
    }

    long start = System.currentTimeMillis();
    long end = start + 10000;
    int j = 0;
    while (System.currentTimeMillis() < end) {
//      System.out.println("Iteration number: " + j);
      j++;
      for (int i = 0; i < 6; i++) {
        MCTSNodeOptimized promisingNode = selectPromisingNode(roots[i]);
        if ((promisingNode == roots[i] || promisingNode.getParent() == roots[i]) &&
            roots[i].getState().getBoardScore() != Integer.MIN_VALUE) {
          roots[i].getState().setBoardScore(Integer.MIN_VALUE);
          continue;
        }
        MCTSNodeOptimized nodeToExplore = promisingNode;
        if (promisingNode.getChildArray().size() > 0) {
          nodeToExplore = promisingNode.getRandomChildNode();
        }
        int playoutResult = simulateRandomPlayout(nodeToExplore);
        backPropagation(nodeToExplore, playoutResult);
      }
    }
    int movesEvaluated = 0;
    MCTSNodeOptimized winnerTree = roots[0];
    for (MCTSNodeOptimized root : roots) {
      if (root.getChildArray().size() > 0 &&
          root.getState().getBoardScore() > winnerTree.getState().getBoardScore()) {
        winnerTree = root;
      }
      movesEvaluated += root.getState().getVisitCount();
    }
//    System.out.println("Winner tree: " + i);
//    for (MCTSNodeOptimized node : winnerTree.getChildArray()) {
//      System.out.println("Visit Count: " + node.getState().getVisitCount() + " " + node.movePlayed + " has score: " + node.getState().getBoardScore());
//    }
    MCTSNodeOptimized t0 = winnerTree.getChildWithMaxScore();
    moves[0] = t0.movePlayed;
    if (t0.getChildArray().size() != 0) {
      MCTSNodeOptimized t1 = t0.getChildWithMaxScore();
      moves[1] = t1.movePlayed;
      if (t1.getChildArray().size() != 0) {
        MCTSNodeOptimized t2 = t1.getChildWithMaxScore();
        moves[2] = t2.movePlayed;
      }
    }
//    System.out.println("Moves Evaluated: " + movesEvaluated);
//    System.out.println(moves[0]);
//    System.out.println(moves[1]);
//    System.out.println(moves[2]);
  }

  public Byte[] getNextMove() {
    if (moves[0] != null) {
      Byte[] move = moves[0];
      moves[0] = null;
      return move;
    }
    if (moves[1] != null) {
      Byte[] move = moves[1];
      moves[1] = null;
//      try {
//        Thread.sleep(200);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
      return move;
    }
    if (moves[2] != null) {
      Byte[] move = moves[2];
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

  public void setGame(GameOptimized game) {
    this.game = game;
  }

  private static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
    if (nodeVisit == 0) return Integer.MAX_VALUE;
    return ((nodeWinScore) / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit)
        / (double) nodeVisit);
  }

  private static MCTSNodeOptimized findBestNodeWithUCT(MCTSNodeOptimized node) {
    int parentVisit = node.getState().getVisitCount();
    return Collections.max(
        node.getChildArray(),
        Comparator.comparing(c -> uctValue(parentVisit, c.getState().getBoardScore(),
            c.getState().getVisitCount()))
    );
  }

}
