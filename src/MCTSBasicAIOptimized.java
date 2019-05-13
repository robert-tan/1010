//import java.util.ArrayList;
//import java.util.List;
//
//public class MCTSBasicAIOptimized {
//
//  private static final double MONTE_C = 1.414;
//
//  private GameOptimized game;
//
//  private class Tree {
//    GameOptimized origGame = new short[10];
//    GameOptimized expandBoard = new short[10];
//    TreeNode root;
//    List<TreeNode> leaves = new ArrayList<>();
//  }
//
//  private class TreeNode {
//    Byte[] move = new Byte[3];
//    int N;
//    int W;
//    TreeNode parent;
//    List<TreeNode> leaves = new ArrayList<>();
//  }
//
//  public Byte[][] bestThreeMoves(byte[] which, long time) {
//    byte[][] perms = {
//        which,
//        {which[0], which[2], which[1]},
//        {which[1], which[0], which[2]},
//        {which[1], which[2], which[0]},
//        {which[2], which[0], which[1]},
//        {which[2], which[1], which[0]}
//    };
//    Tree[] trees = new Tree[perms.length];
//    for (int i = 0; i < trees.length; i++) {
//      trees[i] = null; //New Tree
//    }
//
//    return null;
//  }
//
//  public Tree newTree(GameOptimized game, byte[] pieces) {
//    Tree t = new Tree();
//    System.arraycopy(board, 0, t.origBoard, 0, 10);
//    System.arraycopy(board, 0, t.expandBoard, 0, 10);
//    t.root = new TreeNode();
//
//    for (int i = 0; i <= 10 - GameOptimized.TILE_WIDTHS[pieces[0]]; i++) {
//      for (int j = 0; j <= 10 - GameOptimized.TILE_HEIGHTS[pieces[0]]; i++) {
//        if (game.pla)
//      }
//    }
//  }
//
//}


// Rollout function: randomly place pieces until game ends - score is then the number of randomly placed pieces