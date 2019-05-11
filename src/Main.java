import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    Random rand = new Random();
    Game game = new Game();
    GameAI ai = new MinimaxAI();
    ai.setGame(game);

    while(!game.isFinished()) {
      System.out.println(game.toString());
      List<Move> moves = game.getAllValidMoves();
      if (moves.size() < 10) {
        for (Move move : moves) {
          System.out.println(move);
        }
      }
      Move move = ai.getNextMove();
      System.out.println("AI Determined Best Move: " + move);
//      sc.nextLine();
      game.playPiece(move.getRow(), move.getCol(), move.getTile());
    }

    System.out.println("Game over ;(");
    System.out.println(game);

  }

}
