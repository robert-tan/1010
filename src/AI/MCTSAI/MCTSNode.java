package AI.MCTSAI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import Game.*;

public class MCTSNode {

  private MCTSState state;
  private MCTSNode parent;
  Move movePlayed;
  private List<MCTSNode> childArray;

  public MCTSNode() {
    this.state = new MCTSState();
    childArray = new ArrayList<>();
  }

  MCTSNode(MCTSState state) {
    this.state = state;
    childArray = new ArrayList<>();
  }

  public MCTSNode(MCTSState state, MCTSNode parent, List<MCTSNode> childArray) {
    this.state = state;
    this.parent = parent;
    this.childArray = childArray;
  }

  MCTSNode(MCTSNode node) {
    this.childArray = new ArrayList<>();
    this.state = new MCTSState(node.getState());
    if (node.getParent() != null) {
      this.parent = node.getParent();
    }
    List<MCTSNode> oldChildArray = node.getChildArray();
    for (MCTSNode child : oldChildArray) {
      this.childArray.add(new MCTSNode(child));
    }
  }

  public Move getMovePlayed() {
    return movePlayed;
  }

  public void setMovePlayed(Move movePlayed) {
    this.movePlayed = movePlayed;
  }

  public MCTSState getState() {
    return state;
  }

  public void setState(MCTSState state) {
    this.state = state;
  }

  public MCTSNode getParent() {
    return parent;
  }

  public void setParent(MCTSNode parent) {
    this.parent = parent;
  }

  public List<MCTSNode> getChildArray() {
    return childArray;
  }

  public void setChildArray(List<MCTSNode> childArray) {
    this.childArray = childArray;
  }

  public MCTSNode getRandomChildNode() {
    int random = (int) (Math.random() * childArray.size());
    return childArray.get(random);
  }

  public MCTSNode getChildWithMaxScore() {
    return Collections
        .max(this.childArray, Comparator.comparing(c -> c.getState().getBoardScore()));
  }
}
