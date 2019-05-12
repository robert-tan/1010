import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class AITests {

  private static final int SIM_NUM = 5000;
  private static final int THREAD_COUNT = 4;
  private static final Random rand = new Random();

  public static void main(String[] args) {
    System.out.println("Welcome to 1010! AI Simulations. Running all sims:");
//    runRandomSims();
//
//    GameAI basicAI = new BasicAI();
//    runAISims(basicAI, "Basic AI");

//    Lock lock = new ReentrantLock();
//
//    Runnable r = new Runnable() {
//      @Override
//      public void run() {
//        System.out.println("Running simulations...");
//        double[] scores = new double[SIM_NUM];
//        for (int i = 0; i < SIM_NUM / 4; i++) {
//          Game game = new Game();
//          while(!game.isFinished()) {
//            List<Move> moves = game.getAllValidMoves();
//            Move move = moves.get(rand.nextInt(moves.size()));
//            game.playPiece(move.getRow(), move.getCol(), move.getTile());
//          }
//          scores[i] = (double) game.getScore();
//        }
//        printSummaryStatistics(scores);
//      }
//    };
//
//    List<Thread> threads = new ArrayList<>();
//    for (int i = 0; i < THREAD_COUNT; i++) {
//      Thread t = new Thread(r);
//      threads.add(t);
//      t.start();
//    }
//
//    for (Thread t : threads) {
//      try {
//        t.join();
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//    }
    GameAI heuristicAI = new HeuristicAI();
    runAISims(heuristicAI, "Heuristic AI");

    HeuristicAIOptimized heuristicAIOptimized = new HeuristicAIOptimized();
    runAISimsOptimized(heuristicAIOptimized, "Optimized Heuristic AI");
  }

  private static void printSummaryStatistics(double[] scores, long start) {
    long end = System.currentTimeMillis();
    System.out.println("Time taken: " + (end - start) + " ms");
    DescriptiveStatistics stats = new DescriptiveStatistics(scores);
    System.out.println("- Mean Score: " + stats.getMean());
    System.out.println("- Min Score: " + stats.getMin());
    System.out.println("- 1st Quartile: " + stats.getPercentile(25));
    System.out.println("- Median Score: " + stats.getPercentile(50));
    System.out.println("- 3rd Quartile: " + stats.getPercentile(75));
    System.out.println("- Max Score: " + stats.getMax());
    System.out.println("- Standard Deviation: " + stats.getStandardDeviation());
    System.out.println();
  }

  private static void runRandomSims() {
    System.out.println("Running Random Move simulations...");
    double[] scores = new double[SIM_NUM];
    long start = System.currentTimeMillis();
    for (int i = 0; i < SIM_NUM; i++) {
      Game game = new Game();
      while(!game.isFinished()) {
        List<Move> moves = game.getAllValidMoves();
        Move move = moves.get(rand.nextInt(moves.size()));
        game.playPiece(move.getRow(), move.getCol(), move.getTile());
      }
      scores[i] = (double) game.getScore();
    }
    System.out.println("Completed Random Simulations");
    printSummaryStatistics(scores, start);
  }

  private static void runAISims(GameAI ai, String aiName) {
    System.out.println("Running " + aiName + " simulations...");
    double[] scores = new double[SIM_NUM];
    long start = System.currentTimeMillis();
    for (int i = 0; i < SIM_NUM; i++) {
      Game game = new Game();
      ai.setGame(game);
      while(!game.isFinished()) {
        Move move = ai.getNextMove();
        game.playPiece(move.getRow(), move.getCol(), move.getTile());
      }
      scores[i] = (double) game.getScore();
    }
    System.out.println("Completed " + aiName + " Simulations:");
    printSummaryStatistics(scores, start);
  }

  private static void runAISimsOptimized(HeuristicAIOptimized ai, String aiName) {
    System.out.println("Running " + aiName + " simulations...");
    double[] scores = new double[SIM_NUM];
    long start = System.currentTimeMillis();
    for (int i = 0; i < SIM_NUM; i++) {
      GameOptimized game = new GameOptimized();
      ai.setGame(game);
      while(!game.isFinished()) {
        Byte[] move = ai.getNextMoveOptimized();
        game.playPiece(move[0], move[1], move[2]);
      }
      scores[i] = (double) game.getScore();
    }
    System.out.println("Completed " + aiName + " Simulations:");
    printSummaryStatistics(scores, start);
  }

}
