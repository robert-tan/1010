import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    Random rand = new Random();

    GameOptimized game = new GameOptimized();
//    Game game1 = game.toGame();
//    HeuristicAI ai1 = new HeuristicAI();
//    ai1.setGame(game1);
//    HeuristicAIOptimized ai = new HeuristicAIOptimized();
//    ai.setGame(game);

    MinimaxAIOptimized ai = new MinimaxAIOptimized();
    ai.setGame(game);

    while(!game.isFinished()) {
      System.out.println("Optimized AI:");
      System.out.println(game);
      System.out.println("Optimized board eval: " + ai.evaluate(game.getBoardRows()));
//      System.out.println("Normal board eval: " + ai1.evaluate(ai.getGame().toBoard()));
//      System.out.println();
//      System.out.println("Normal AI:");
//      System.out.println(game1);
//      System.out.println("Normal board eval: " + ai1.evaluate(game1.getBoard()));

//      List<Move> moves = game1.getAllValidMoves();
//      if (moves.size() < 10) {
//        for (Move move : moves) {
//          System.out.println(move);
//        }
//      }
//      Move move = ai1.getNextMove();
//      System.out.println("Normal AI Determined Best Move: " + move);
////      sc.nextLine();
//      game1.playPiece(move.getRow(), move.getCol(), move.getTile());

      Byte[] move1 = ai.getNextMoveOptimized();
      System.out.println("Optimized AI Determined Best Move: Row = " + move1[0] + " Col = " + move1[1] + " Tile = " + GameOptimized.TILE_IDS[move1[2]]);
      game.playPiece(move1[0], move1[1], move1[2]);

    }

    System.out.println("Game over ;(");
    System.out.println(game);

  }

}
