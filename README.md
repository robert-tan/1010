# 1010
The popular mobile game 1010! complete with several variants of AI implementations that will play the game by itself. Currently includes Minimax and Monte Carlo Tree Search. Future variations may include implementations utilizing concurrency, temporal difference, neural networks (dl4j)...etc.

## Included AI Implementations
### Random AI
* Plays completely random moves, used as a control to benchmark other implementations of AIs
* Fastest "algorithm"
* Median score around 65

### Basic AI
* Plays the move (out of all valid moves) which gives the most immediately maximum point increase (in other words, it is a "greedy" algorithm
* Still a very fast algorithm
* Median score around 120

### Heuristic AI
* Plays the move maximizing the score assigned to the board by an evaluate function. The function is constantly refined and takes into consideration
* Heuristic function involves considerations of: openness, enclosed singles, isolated singles, whether tiles can be placed, the number of edges, the number of corners, and the number of pieces filled on the board
* MUCH slower than the previous two as the evaluate function takes significant time complexity (in the current iteration, optimizations should be possible)
* Median score around 1150

### Minimax AI
* Searches a dynamic depth ahead (typically 2-3 moves depending on available moves and evaluate score)
* Board evaluated with the same algorithm as the Heuristic AI at the end of search
* Able to evaluate the best SET of moves given any 3 piece set
* Exponentially slower than the Heuristic AI
* Scores reach 35000+ at depth 2-3 (can not calculate median due to time impracticality)
* With one more layer of depth, has the potential to reach infinite play time (however, it is very impractical to calculate the next set of 3 as the time complexity rises incredibly fast)

### Monte Carlo Tree Search (MCTS) AI
* Simulates a large number of outcomes and back-propagates results through the result tree
* Balances exploration and exploitation with UCT
* Implemented without knowledge of the game itself other than basic rules
* Scores depend on how much search time is allowed (given 5 seconds of search time for each set of 3 moves, able to reach 10000+)
* Limitations likely due to:
  - Ambiguity of 'goodness' for each outcome (several implementations for this exist)
  - Existence of shallow traps
  - Improper balance of exploration and exploitation (has to do with the board score)
* Most of these limitations are likely to be overcome with a Temporal Difference (TD) implementation
* Can reach even higher scores when combined with Heuristic board evaluation algorithm, but will become much slower to adequately search possibilities
