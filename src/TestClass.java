import Game.GameOptimized;

public class TestClass {

  public static void main(String[] args) {

    GameOptimized game = new GameOptimized();
    short[] board = new short[]{
        0b0000100000000000,
        0b0001100000000000,
        0b0001100000000000,
        0b0,
        (short) (0xFFFF & 0b1111111110000000),
        (short) (0xFFFF & 0b1111111110000000),
        0b0000001110000000,
        0b0011100011000000,
        0b0011100011000000,
        0b0010100010000000
    };
    game.setBoard(board);

    game.setMoveSet(new byte[]{-1, -1, 16});

    System.out.println(game);

    System.out.println(((((0xFFFF & GameOptimized.TILES[16][0]) >>> (6 + 9)) & 1)));

    System.out.println(game.isFinished());

  }

}
