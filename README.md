# 1010
The popular mobile game 1010! complete with several variants of AI implementations that will play the game by itself. Currently includes Minimax and Monte Carlo Tree Search. Future variations may include implementations utilizing concurrency, temporal difference, neural networks (dl4j)...etc.

## Included AI Implementations
### Random AI
* Plays completely random moves
* Fastest "algorithm"
* Median score around 65

### Basic AI
* Plays the move (out of all valid moves) which gives the most immediately maximum point increase (in other words, it is a "greedy" algorithm
* Still a very fast algorithm
* Median score around 120

### Heuristic AI
* Plays the move maximizing the score assigned to the board by an evaluate function. The function is constantly refined and takes into consideration
* MUCH slower than the previous two as the evaluate function takes significant time complexity (in the current iteration, optimizations should be possible)
* Median score around 1150

### Minimax AI
* Searches a dynamic depth ahead (typically 2-3 moves depending on available moves and evaluate score)
* Game.Board evaluated with the same algorithm as the Heuristic AI at the end of search
* Able to evaluate the best SET of moves given any 3 piece set
* Exponentially slower than the Heuristic AI
* Scores reach 35000+ at depth 2-3 (can not calculate median due to time impracticality)

### Monte Carlo Tree Search (MCTS) AI
* Simulates a large number of outcomes and back-propagates results through the result tree
* Balances exploration and exploitation with UCT
* Scores depend on how much search time is allowed but general MUCH MUCH lower than Minimax AI
* Conclude MCTS is not a very useful algorithm for 1010! as perfect play will never reach termination and many shallow traps exist
