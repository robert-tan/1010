# 1010! with Self-Playing AI
The popular mobile game 1010! complete with several variants of AI implementations that will play the game by itself. Currently includes Minimax and Monte Carlo Tree Search. Future variations may include implementations utilizing concurrency, temporal difference via Q-learning, neural networks (dl4j)...etc. Below, I have detailed the included AIs and game implementations:

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
* Maximizes the board score among valid moves
* Minimizes based on the worst cases for the next 3 tiles
    - Since search is implemented with each move, minimization discards the scores for the worst 2 tiles
    - However, in doing so the AI discards the possibility of 2 or more same (worst case) tiles appearing in the next move set
* Exponentially slower than the Heuristic AI
* Scores reach 35000+ at depth 2-3 (around 0-20 seconds per set of moves depending on dynamic depth, can not calculate median due to time impracticality)
* With one more layer of depth, has the potential to reach infinite play time (however, it is very impractical to calculate the next set of 3 as the time complexity rises incredibly fast)

### Monte Carlo Tree Search (MCTS) AI
* Simulates a large number of outcomes and back-propagates results through the result tree
* Balances exploration and exploitation with UCT
* Implemented without knowledge of the game itself other than basic rules
* Faster and arguably better than Minimax for this game
* Scores depend on how much search time is allowed (given 2 seconds of search time for each set of 3 moves, able to reach 100000+)
* Limitations likely due to:
  - Ambiguity of 'goodness' for each outcome (several implementations for this exist)
  - Existence of shallow traps
  - Improper balance of exploration and exploitation (has to do with the board score)
* Most of these limitations are likely to be overcome with a Temporal Difference (TD) implementation
* Can reach even higher scores when combined with Heuristic board evaluation algorithm, but will become much slower to adequately search possibilities

### Monte Carlo Tree Search (MCTS) AI with Optimized Game
* Same as MCTS except implemented with optimizations to the game as detailed below
* Uses a much smaller bitboard array to store the board
* Usually between 50000 and 150000 moves evaluated given two seconds of search time

## Optimizations of the Game Class
* Current game used contains many class, function, and memory overheads
  - Board implemented via a 10x10 array of integers
  - Most operations cycle through each entry of the board
* Created optimized version using a bitboard implementation via an array of 10 shorts
  - Each row represented with one short
  - Now instead of iterating over 100 integers, board iterates over 10 shorts
* Removed the Move class
* Changed Tile identification (TileID) to bytes with a symbol table
* Added width and height properties of tiles to reduce unnecessary iterations on loops
* See a roughly 3x increase in performance on game operations (tested using the Basic AI and Random AI)
* Limitations: 
  - Heuristic function too complex to convert using bitboards (therefore Minimax and Heuristic AIs would be impractical to implement this way)
  - Bitwise operations on Java not as efficient/robust as in other (lower level) languages
