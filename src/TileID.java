import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum TileID {

  ONE(1), TWO_VER(2), TWO_HOR(2), THREE_VER(3), THREE_HOR(3), THREE_BOTTOM_LEFT(3),
  THREE_BOTTOM_RIGHT(3), THREE_TOP_LEFT(3), THREE_TOP_RIGHT(3), FOUR_VERT(4), FOUR_HOR(4),
  FOUR_SQUARE(4), FIVE_TOP_RIGHT(5), FIVE_TOP_LEFT(5), FIVE_BOTTOM_RIGHT(5), FIVE_BOTTOM_LEFT(5),
  FIVE_VERT(5), FIVE_HOR(5), NINE_SQUARE(9);

  private List<Coordinate> offsets;
  private int size;

  TileID(int size) {
    this.size = size;
  }

  public static TileID getRandomID() {
    TileID[] ids = TileID.values();
    Random random = new Random();
    return ids[random.nextInt(ids.length)];
  }

  public int getSize() {
    return size;
  }

  public List<Coordinate> getOffsets() {
    if (offsets == null) {
      offsets = new ArrayList<>();
      switch (this) {
        case ONE:
          offsets.add(new Coordinate(0, 0));
          break;
        case TWO_VER:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(1, 0));
          break;
        case TWO_HOR:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(0, 1));
          break;
        case THREE_VER:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(1, 0));
          offsets.add(new Coordinate(2, 0));
          break;
        case THREE_HOR:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(0, 1));
          offsets.add(new Coordinate(0, 2));
          break;
        case THREE_BOTTOM_LEFT:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(1, 0));
          offsets.add(new Coordinate(1, 1));
          break;
        case THREE_BOTTOM_RIGHT:
          offsets.add(new Coordinate(0, 1));
          offsets.add(new Coordinate(1, 0));
          offsets.add(new Coordinate(1, 1));
          break;
        case THREE_TOP_LEFT:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(1, 0));
          offsets.add(new Coordinate(0, 1));
          break;
        case THREE_TOP_RIGHT:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(0, 1));
          offsets.add(new Coordinate(1, 1));
          break;
        case FOUR_VERT:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(1, 0));
          offsets.add(new Coordinate(2, 0));
          offsets.add(new Coordinate(3, 0));
          break;
        case FOUR_HOR:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(0, 1));
          offsets.add(new Coordinate(0, 2));
          offsets.add(new Coordinate(0, 3));
          break;
        case FOUR_SQUARE:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(1, 0));
          offsets.add(new Coordinate(0, 1));
          offsets.add(new Coordinate(1, 1));
          break;
        case FIVE_HOR:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(0, 1));
          offsets.add(new Coordinate(0, 2));
          offsets.add(new Coordinate(0, 3));
          offsets.add(new Coordinate(0, 4));
          break;
        case FIVE_VERT:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(1, 0));
          offsets.add(new Coordinate(2, 0));
          offsets.add(new Coordinate(3, 0));
          offsets.add(new Coordinate(4, 0));
          break;
        case FIVE_TOP_LEFT:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(1, 0));
          offsets.add(new Coordinate(0, 1));
          offsets.add(new Coordinate(2, 0));
          offsets.add(new Coordinate(0, 2));
          break;
        case FIVE_TOP_RIGHT:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(0, 1));
          offsets.add(new Coordinate(1, 2));
          offsets.add(new Coordinate(0, 2));
          offsets.add(new Coordinate(2, 2));
          break;
        case FIVE_BOTTOM_LEFT:
          offsets.add(new Coordinate(0, 0));
          offsets.add(new Coordinate(1, 0));
          offsets.add(new Coordinate(2, 1));
          offsets.add(new Coordinate(2, 0));
          offsets.add(new Coordinate(2, 2));
          break;
        case FIVE_BOTTOM_RIGHT:
          offsets.add(new Coordinate(0, 2));
          offsets.add(new Coordinate(2, 0));
          offsets.add(new Coordinate(2, 1));
          offsets.add(new Coordinate(1, 2));
          offsets.add(new Coordinate(2, 2));
          break;
        case NINE_SQUARE:
          for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
              offsets.add(new Coordinate(i, j));
            }
          }
          break;
        default: break;
      }
    }
    return offsets;
  }

  public short[] getPieceRows() {
    short[] pieceRows = new short[10];
    switch (this) {
      case ONE:
        pieceRows[0] = Short.MIN_VALUE;
        break;
      case TWO_VER:
        break;
      case TWO_HOR:
        break;
      case THREE_VER:
        break;
      case THREE_HOR:
        break;
      case THREE_BOTTOM_LEFT:
        break;
      case THREE_BOTTOM_RIGHT:
        break;
      case THREE_TOP_LEFT:
        break;
      case THREE_TOP_RIGHT:
        break;
      case FOUR_VERT:
        break;
      case FOUR_HOR:
        break;
      case FOUR_SQUARE:
        break;
      case FIVE_TOP_RIGHT:
        break;
      case FIVE_TOP_LEFT:
        break;
      case FIVE_BOTTOM_RIGHT:
        break;
      case FIVE_BOTTOM_LEFT:
        break;
      case FIVE_VERT:
        break;
      case FIVE_HOR:
        break;
      case NINE_SQUARE:
        break;
    }

    return pieceRows;
  }

}