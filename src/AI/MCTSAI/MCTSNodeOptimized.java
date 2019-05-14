package AI.MCTSAI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MCTSNodeOptimized {

  private MCTSStateOptimized state;
  private MCTSNodeOptimized parent;
  Byte[] movePlayed;
  private List<MCTSNodeOptimized> childArray;

  public MCTSNodeOptimized() {
    this.state = new MCTSStateOptimized();
    childArray = new ArrayList<>();
  }

  MCTSNodeOptimized(MCTSStateOptimized state) {
    this.state = state;
    childArray = new ArrayList<>();
  }

  public MCTSNodeOptimized(MCTSStateOptimized state, MCTSNodeOptimized parent, List<MCTSNodeOptimized> childArray) {
    this.state = state;
    this.parent = parent;
    this.childArray = childArray;
  }

  MCTSNodeOptimized(MCTSNodeOptimized node) {
    this.childArray = new ArrayList<>();
    this.state = new MCTSStateOptimized(node.getState());
    if (node.getParent() != null) {
      this.parent = node.getParent();
    }
    List<MCTSNodeOptimized> oldChildArray = node.getChildArray();
    for (MCTSNodeOptimized child : oldChildArray) {
      this.childArray.add(new MCTSNodeOptimized(child));
    }
  }

  public Byte[] getMovePlayed() {
    return movePlayed;
  }

  public void setMovePlayed(Byte[] movePlayed) {
    this.movePlayed = movePlayed;
  }

  public MCTSStateOptimized getState() {
    return state;
  }

  public void setState(MCTSStateOptimized state) {
    this.state = state;
  }

  public MCTSNodeOptimized getParent() {
    return parent;
  }

  public void setParent(MCTSNodeOptimized parent) {
    this.parent = parent;
  }

  public List<MCTSNodeOptimized> getChildArray() {
    return childArray;
  }

  public void setChildArray(List<MCTSNodeOptimized> childArray) {
    this.childArray = childArray;
  }

  public MCTSNodeOptimized getRandomChildNode() {
    int random = (int) (Math.random() * childArray.size());
    return childArray.get(random);
  }

  public MCTSNodeOptimized getChildWithMaxScore() {
    return Collections
        .max(this.childArray, Comparator.comparing(c -> c.getState().getBoardScore()));
  }
}
