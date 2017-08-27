/*
 * Kegan Schaub
 * Partner: Zach Van Uum
 * 
 * CsC 335 Bowling Score Iteration I
 * Section Leader: Gabe
 * 
 * Comments: aaaah yaa sorry. I know this code is pretty disgusting
 * but I really didn't want to rewrite it. 
 */
package model;

import java.util.ArrayList;

public class BowlingLine implements IBowlingLine {
	
	// An ArrayList that will contain the users input
	ArrayList<Integer> scoreBoard = new ArrayList<Integer>();
	/*
	 * This helper method returns an array of pins knocked down from the frame
	 * 
	 * Precondition: If the frame is missing a turn from the player a -1 is put in place
	 */
	private int[] getFrame(int frame){
		int[] frameRolls = new int [2];
		
			if ((frame*2 -2) > scoreBoard.size()-1){
				frameRolls[0] = -1;
			}
			else {
				frameRolls[0] = scoreBoard.get(frame * 2 -2);
			}
			if ((frame*2 -1) > scoreBoard.size()-1){
				frameRolls[1] = -1;
			}
			else {
				frameRolls[1] = scoreBoard.get(frame * 2 -1);
			}
		
		return frameRolls;
	}
	private int currFrame(){
		int i = ((scoreBoard.size()-1) / 2) + 1;
		return i;
	}
	
	@Override
	public int totalScore() {
		int totalScore = 0;
		
		for (int i = 1; i <= currFrame(); i++){
			int[] frameRolls = getFrame(i);
			int[] nextFrameRolls = getFrame(i+1);
			int[] nextNextFrameRolls = getFrame(i+2);
			
			//Special case if all strikes
			if (i == 11){
				if (totalScore == 290 && frameRolls[0] == 10){
					totalScore += 10;
				}
			}
			else if (i <= 10){
				//Is it a strike and a non strike?
				if ((frameRolls[0] == 10) && (nextFrameRolls[0] == 10) && nextNextFrameRolls[0] != 10 && nextNextFrameRolls[0] != -1){
					totalScore += 10 + nextFrameRolls[0] + nextNextFrameRolls[0]; 
				}
				//Are the next two strikes?
				else if ((frameRolls[0] == 10) && (nextFrameRolls[0] == 10) && nextNextFrameRolls[0] == 10 && (nextNextFrameRolls[0] != -1) && (nextNextFrameRolls[1] != -1)){
					totalScore += 30; 
				}
				//Is it a strike?
				else if (frameRolls[0] == 10 && (nextFrameRolls[0] != -1 && nextFrameRolls[1] != -1)){
					totalScore += 10 + nextFrameRolls[0] + nextFrameRolls[1];
				}
				//Is it a Spare?
				else if (frameRolls[0] + frameRolls[1] == 10 && (nextFrameRolls[0] != -1)){
					totalScore += 10 + nextFrameRolls[0];
				}
				//Add up anything else
				else if (frameRolls[1] != -1){
					totalScore += frameRolls[0] + frameRolls[1];
				}
				else{
					totalScore += frameRolls[0];
				}
			}
		}
		return totalScore;
	}
/*
 * Prints the score at a specific frame
 * 
 */
	@Override
	public int scoreAtFrame(int frame) {
		int totalScore = 0;
		
		for (int i = 1; i <= frame; i++){
			if (canShowScoreFrame(frame)){
				int[] frameRolls = getFrame(i);
				int[] nextFrameRolls = getFrame(i+1);
				int[] nextNextFrameRolls = getFrame(i+2);
				
				if (i <= 10){
					//Is it a strike and a non strike?
					if ((frameRolls[0] == 10) && (nextFrameRolls[0] == 10) && nextNextFrameRolls[0] != 10){
						totalScore += 10 + nextFrameRolls[0] + nextNextFrameRolls[0]; 
					}
					//Are the next two strikes?
					else if ((frameRolls[0] == 10 || frameRolls[1] == 10) && (nextFrameRolls[0] + nextFrameRolls[1] == 10)){
						totalScore += 10 + nextFrameRolls[0] + nextFrameRolls[1] + nextNextFrameRolls[0] + nextNextFrameRolls[1]; 
					}
					//Is it a strike?
					else if (frameRolls[0] == 10 || frameRolls[1] == 10){
						totalScore += 10 + nextFrameRolls[0] + nextFrameRolls[1];
					}
					//Is it a Spare?
					else if (frameRolls[0] + frameRolls[1] == 10){
						totalScore += 10 + nextFrameRolls[0];
					}
					//Add up anything else
					else{
						totalScore += frameRolls[0] + frameRolls[1];
					}
				}
			}
		}
		return totalScore;
	}
/*
 * First if statement checks if a strike is being added and if so, if it is the beginning of the frame.
 * It will also put a zero in place of the second number in the frame
 * 
 * Second if statement checks if the pins can be added at all
 *
 */
	@Override
	public void pinsDowned(int pins) {
		if (pins == 10){
			scoreBoard.add(pins);
			scoreBoard.add(0);
		}
		else if (0 <= pins && pins <= pinsLeftToDown()){
			scoreBoard.add(pins);
		}
	}
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
	@Override
	public boolean canShowScoreFrame(int frame) {
		int[] frameRolls = getFrame(frame);
		int [] nextFrameRolls = getFrame(frame+1);
		int [] nextNextFrameRolls = getFrame(frame+2);
		
		if (currFrame() > 10 && totalScore() != 300){
			return true;
		}
		if (frameRolls[1] == -1){
			return false;
		}
		if ((frameRolls[0] == 10 || frameRolls[1] == 10) && (nextFrameRolls[0] + nextFrameRolls[1] == 10) && nextNextFrameRolls[0] == -1){
			return false;
		}
		if ((frameRolls[0] == 10 || frameRolls[1] == 10) && (nextFrameRolls[0] == -1) && (nextFrameRolls[1] == -1)){
			return false;
		}
		if ((frameRolls[0] + frameRolls[1] == 10) && (nextFrameRolls[0] != -1)){
			return true;
		}
		if ((frameRolls[0] + frameRolls[1] == 10) && (nextFrameRolls[1] == -1)){
			return false;
		}
		else{
			return true;
		}
	}
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
	@Override
	public String getRollsForFrame(int frame) {
		int[] frameRolls = getFrame(frame);
		int[] lastRolls = getFrame(frame + 1);
		int[] lastRolls2 = getFrame(frame + 2);
		
		// Is it 10th frame
		// 
		if (frame >= 10){
			int[] backRolls = getFrame(frame -1);//helps all strikes

			// XXX
			if (frameRolls[0] == 10 && lastRolls[0] == 10 && backRolls[0] == 10) return "XXX";
			// X2
			if (frameRolls[0] == 10 && lastRolls[0] < 10 && lastRolls[0] > 0 && lastRolls[1] == -1) return "X" + lastRolls[0];
			// X27
			if (frameRolls[0] == 10 && (lastRolls[0] < 10 && lastRolls[0] > 0) && lastRolls[1] < 10 && lastRolls[1] > 0) return "X" + lastRolls[0] + "" + lastRolls[1];
			// XX
			if (frameRolls[0] == 10 && lastRolls[0] == 10 && lastRolls2[0] == -1) return "XX ";
			// -/X
			if (frameRolls[0] == 0 && frameRolls[1] == 10 && lastRolls[0] == 10) return "-/X";
			// XX-
			if (frameRolls[0] == 10 && lastRolls[0] == 10 && (lastRolls2[0]) == 0) return "XX-";
			// X2/
			if (frameRolls[0] == 10 && (lastRolls[0] > 0 && lastRolls[0] < 10) && lastRolls[0] + lastRolls[1] == 10) return "X" + frameRolls[1] + "/";
			// 2/X
			if (frameRolls[0] < 10 && frameRolls[0] > 0 && (frameRolls[0] + frameRolls[1] == 10) && lastRolls[0] == 10) return frameRolls[0] + "/X";
			// 2/-
			if (frameRolls[0] + frameRolls[1] == 10 && lastRolls[0] ==0) return frameRolls[0] + "/-";				
			// X2-
			if ((frameRolls[0] == 10) && (lastRolls[0] > 0 && lastRolls[0] < 10) && lastRolls[1] == 0) return "X" + lastRolls[0] + "-";
			// 24-
			if(frameRolls[0] > 0 && frameRolls[0] < 10 && (frameRolls[1] > 0 && frameRolls[1] < 10) && lastRolls[0] == 0) return frameRolls[0] + "" + frameRolls[1] + "-";
			// 4/7
			if (frameRolls[0] + frameRolls[1] == 10 && (lastRolls[0] > 0 && lastRolls[0] < 10)) return frameRolls[0] + "/" + lastRolls[0];
			// --
			if (frameRolls[0] == 0 && frameRolls[1] == 0) return "--";
			// -/-
			if (frameRolls[0] == 0 && frameRolls[1] == 10 && lastRolls[0] == 0) return "-/-";
			// -/4
			if (frameRolls[0] == 0 && frameRolls[1] == 10 && (lastRolls[0] < 10 && lastRolls[0] > 0)) return "-/" + lastRolls[0];
		
		}
		// If any other frame
		if (frameRolls[0] == 10 && frameRolls[1] == 0) return "X  ";
		if (frameRolls[0] == 0 && frameRolls[1] == 10) return "- /";
		if (frameRolls[0] == 0 && frameRolls[1] == 0) return "- -";
		if ((frameRolls[0] > 0 && frameRolls[0] < 10 ) && frameRolls[1] == 0) return frameRolls[0] + " -";
		if (frameRolls[0] == 0 && frameRolls[1] == -1) return "-  ";
		if (frameRolls[0] == 0 && (frameRolls[1] > 0 && frameRolls[1] < 10)) return "- " + frameRolls[1];
		if ((frameRolls[0] > 0 && frameRolls[0] < 10) && frameRolls[1] == -1) return frameRolls[0] + "  ";
		if ((frameRolls[1] > 0 && frameRolls[1] < 10) && frameRolls[0] == -1) return "  " + frameRolls[1];
		if (frameRolls[0] != -1 && frameRolls[1] == -1) return frameRolls[1] + " -";
		if (frameRolls[0] != -1 && frameRolls[1] == -1) return frameRolls[0] + " " + frameRolls[1];
		if (frameRolls[1] == -1) return "- ";
		if (frameRolls[0] + frameRolls[1] == 10) return frameRolls[0] + " /";
		else {
			return frameRolls[0] + " " + frameRolls[1];
		}
	}
	@Override
	public int pinsLeftToDown() {
		int[] frameRolls = getFrame(currFrame());
		int[] nextFrameRolls = getFrame(currFrame() +1);
		
		if (scoreBoard.isEmpty()){
			return 10;
		}
		else if (nextFrameRolls[0] == -1 && frameRolls[1] != -1){
			return 10;
		}
		else{
			return (10 - frameRolls[0]);
		}
	}
/*
 * 
 *
 */
	@Override
	public boolean gameOver() {
		int[] frameRolls = getFrame(currFrame());
		
		if ((currFrame() == 10 && frameRolls[0] == 0) || (currFrame() == 11 && frameRolls[0] == 0)){
			return true;
		}
		else if (currFrame() == 12){
			return true;
		}
		else if (currFrame() == 10 && frameRolls[1] != -1 && frameRolls[0] + frameRolls[1] != 10){
			return true;
		}
		else if (currFrame() == 11 && frameRolls[0] + frameRolls[1] == 10){
			return true;
		}
		else{
			return false;
		}
	}
	
	private int frameNum = 1;
	private int everyTwo = 0;
	private int k = 3;
	
	public String printBoard(){
		String result = "";
		
		String midLine = "";
		String topVertLine = "";
		String midVertLine = "";
		String vertLineInput = "";
		
		for (int i = 0; i < 91; i++){
			if (i == 0 || i == 88){
				topVertLine += "";
				midVertLine += "";
				vertLineInput += "";
			}
			else if (i % 8 ==0){
				midLine += "+";
				topVertLine += "|";
				midVertLine += "|";
				vertLineInput += "|";
			}
			else{
				midLine += "-";
				topVertLine += " ";
				midVertLine += " ";
				vertLineInput += " ";
			}
			
		}
		int tempK = 3;
		int tempFrame = 1;
		int erry2 = 0;
		
		midVertLine += "         ";
		
		while (tempK <= k){
			if (erry2 <= 2){
				midVertLine = midVertLine.substring(0, tempK) + 
						getRollsForFrame(tempFrame) + midVertLine.substring(tempK+3);
				erry2++;	
				
			}
			else{
				if (scoreAtFrame(tempFrame) == 0){
					//do nothing
				}
				else{
					String temp = "" + scoreAtFrame(tempFrame); 
					vertLineInput = vertLineInput.substring(0, tempK+1) + scoreAtFrame(tempFrame) +
							vertLineInput.substring(tempK+ temp.length()+1);
				}
				erry2 = 0;
				tempFrame++;
				tempK += 8;
			}
		}
		
		System.out.println(topVertLine.substring(0, 5) + "1" + 
		topVertLine.substring(6, 13) + "2" + topVertLine.substring(14, 21) + 
		"3" + topVertLine.substring(22, 29) + "4" + topVertLine.substring(30, 37) + "5" + 
		topVertLine.substring(38, 45) + "6" + topVertLine.substring(46, 53) + "7" +
		topVertLine.substring(54, 61) + "8" + topVertLine.substring(62, 69) + "9" + 
		topVertLine.substring(70, 76) + "10" + topVertLine.substring(78, 85));
		
		result = midLine + "\n" + midVertLine.substring(0, 84) + "TOTAL" + "\n" + vertLineInput.substring(0, 85) + totalScore();
		
		
		//Because each frame has two input we have to increment frameNum every two
		if (getRollsForFrame(frameNum) == "X  " && frameNum != 10 && k != 75){
			k += 8;
		}
		else if (everyTwo % 2 != 0 && frameNum != 10 && k != 75){
			frameNum++;
			k += 8;
		}
		everyTwo++;
		
	
		return result;
	}

}
