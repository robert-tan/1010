import AI.*;
import AI.MCTSAI.*;
import Game.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    Random rand = new Random();
//    Game game = new Game();

    GameOptimized game = new GameOptimized();
//    Game.Game game1 = game.toGame();
//    HeuristicAI ai1 = new HeuristicAI();
//    ai1.setGame(game1);
    MCTSBasicAIOptimized ai = new MCTSBasicAIOptimized();
    ai.setGame(game);
//    ai.setGame(game);

//    GameAI ai = new MCTSBasicAI();
//    ai.setGame(game);

    System.out.println("Running Simulation...");

    while(!game.isFinished()) {
//      System.out.println("Optimized AI:");
      System.out.println(game);
//      System.out.println("Optimized board eval: " + ai.evaluate(game.getBoardRows()));
//      System.out.println("Normal board eval: " + ai1.evaluate(ai.getGame().toBoard()));
//      System.out.println();
//      System.out.println("Normal AI:");
//      System.out.println(game1);
//      System.out.println("Normal board eval: " + ai1.evaluate(game1.getBoard()));

//      List<Game.Move> moves = game1.getAllValidMoves();
//      if (moves.size() < 10) {
//        for (Game.Move move : moves) {
//          System.out.println(move);
//        }
//      }
      Byte[] move = ai.getNextMove();
//      System.out.println("AI Determined Best Game.Move: " + Arrays.toString(move));
//      Byte[] move = new Byte[]{sc.nextByte(), sc.nextByte(), sc.nextByte()};
      game.playPiece(move[0], move[1], move[2]);

//      Byte[] move1 = ai.getNextMoveOptimized();
//      System.out.println("Optimized AI Determined Best Game.Move: Row = " + move1[0] + " Col = " + move1[1] + " Tile = " + Game.GameOptimized.TILE_IDS[move1[2]]);
//      game.playPiece(move1[0], move1[1], move1[2]);

    }

    System.out.println("Game.Game over ;(");
    System.out.println(game);

  }

}
