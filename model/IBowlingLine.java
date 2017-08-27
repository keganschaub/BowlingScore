package model;

/**
  C Sc 335 
  Interface for the Bowling line project

  Iteration 1: The model

This interface abstractly describes one design for maintaining
bowling line scores.  Since we will run tests on your code,
use this design. We need method signatures to match.  This
will be the last time we run tests on your code. It will be
up to you to write your own tests in the future.

@author Rick Mercer
*/

public interface IBowlingLine {

  static final int LAST_FRAME = 10;

  /**
  * Returns the TOTAL score of the game. This score should be
  * determined according to standard bowling rules and should 
  * include all pins knocked down in the current frame, even 
  * if the current frame is not yet completed.
  * 
  * Example in the middle of 4th frame, 
  *
  *   1 |    2 |    3 |    4 |    5 |    6 |    7 |    8 |    9 |   10 |
  * 5 3 |    X |    X |  8   |      |      |      |      |      |      |   TOTAL
  *   8 |   36 |      |      |      |      |      |      |      |      |    62 
  *
  * @return the current total score of the game
  */
  int totalScore(); // public by default, abstract assumed with ;

  /**
   * Returns the total score of the game so far at the specified frame. This
   * includes bonus points in frames after the specified frame if applicable.
   * The standard bowling score rules apply. 
   * 
   * @param frame
   *          the number of the frame to return the score of
   *          
   * Precondition: frame is in the range 1..10
   */
  int scoreAtFrame(int frame);

  /**
   * Records that the specific number of pins has been knocked down.
   * 
   * Precondition: it is expected that only valid rolls in the current context
   * of the game will be used when this method is called. pins is in the range
   * of 0..pinsRemaining. No assertions can be made such as pinsDowned(-1) or
   * pinsDowned(5) when there are only 4 pins possible to knock down.
   * 
   * @param pins
   *          the pins knocked down by the bowler
   *          
   * Precondition: 0 <= pins <= the only pins standing for the next roll
   */
  void pinsDowned(int pins);

  /**
   * Determines whether the score in a specified frame from
   * scoreAtFrame(int) can be shown yet. This is determined by
   * whether or not the specified frame is complete, including 
   * all future "bonus" rolls that contribute to the specified frame.
   * 
   * Examples: 
   * 
   * 1) If only one of the two rolls have happened in the specified
   *    frame, this method will return false. 
   * 
   * 2) If a strike has happened in the specified frame but the next 
   *    two rolls have not yet occurred, this method will return false. 
   * 
   * 3) If a spare has happened in the specified frame and the next 
   *    roll has also occurred, this method will return true.
   * 
   * @param frame
   *         The number of the frame to return whether or not it 
   *         can be scored in the range 1..10
   * @return true
   *         if the total score so far at this frame can be
   *         completely determined or false otherwise.
   *         
   * Precondition: frame is in the range of 1..10        
   */
  boolean canShowScoreFrame(int frame);

  /**
   * Returns the results of the given frame as a string showing 
   * both rolls (if applicable). Usually this is three characters 
   * long and replaces the first 10 with "X" for a strike and any 
   * combination of two rolls adding to ten with "/" in the second 
   * place to represent a spare.
   * 
   * Examples.  Note: All strings must have length of 3.  The spaces MUST match
   *  
   *  "4 3" - a frame where a 4 was rolled first, then a 3.
   *  "X  " - a frame where a strike was rolled. 
   *  "6 /" - a frame where a 6 was rolled first, then a 4, making a spare.
   *  "6 -" - a frame where a 6 was rolled first, then a gutter ball.
   *  "- 6" - a frame where a gutter ball is rolled first, then a 6
   *  "- -" - a frame with two gutter balls
   *  "2  " - a frame where a 2 was rolled first,
   *          but the second roll has not yet occurred.
   *  "8/X" - 10th frame spare followed by a strike
   *  "XXX" - 3 strikes in the tenth frame  
   *  "54 " - Only get two roles in the 10th if no strike or spare      
   *  "X9/" - Roll a 10, a 9, then a 1 in the 10th      
   *  "4/7" - Roll a 4, a 6, then a 7 in the 10th      
   * 
   * @param frame
   *          the number of the frame to return the displayed results from, in
   *          the range 1..10
   * @return the rolls that occurred in the specified frame as a String
   */
  public abstract String getRollsForFrame(int frame);

  /**
   * Returns the maximum number of pins allowed on the next roll.
   * 
   * Examples: At start of a new game, no pinsDowned, return 10 * 
   *   First roll is 10, return 10 
   *   First roll is 7, return 3 
   *   First roll is 0 (gutter ball), return 10 
   *   First roll is 9, return 1 
   *   First three rolls are 3, 7, and 6: return 4 
   *   The 10th frame is over, return 0
   * 
   * @return The maximum number of pins the bowler should be able to knock down
   *         on the next roll
   */
  public abstract int pinsLeftToDown();
  
  /**
   * Returns true if the game is over and no more balls can be rolled
   */
  public abstract boolean gameOver();
  
  /*
   * Returns a string of the game
   */
  public abstract String printBoard();

}