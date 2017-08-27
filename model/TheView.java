package model;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class TheView extends JFrame{

	public static void main(String[] args) {
		TheView bowlingView = new TheView();
	    bowlingView.setVisible(true);
	}

	private JPanel panel;
	private JPanel panel2;
	private JLabel instructions;
	private JButton roll;
	private JButton newGame;
	private JTextArea frame;
	private JList rollList;
	private Integer currentRoll;
	private BowlingLine game;
	private int counting;

	public TheView() {
		JOptionPane.showMessageDialog(null, "Select Pins First (0..Max Possible)");
		
		instructions = new JLabel("Select a number before pressing Roll");
		newGame = new JButton("New Game");
		roll = new JButton("Roll");
		frame = new JTextArea();
		panel = new JPanel();
		panel2 = new JPanel();
		counting = 0;
		
		game = new BowlingLine();
		setProperties();
		setUpModel();
		addComponents();
		registerListeners();
	}
	

	private void setProperties() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(650,280);
		setLocation(75,50);
		this.setLayout(new BorderLayout());
		setTitle("Bowling");
		
		panel.setSize(650,200);
		panel2.setSize(650,300);
		panel2.setLayout(new FlowLayout());
		frame.setPreferredSize(new Dimension(600,200));
	}
	
	private void addComponents(){
		panel.setBackground(Color.ORANGE);
	    panel.add(roll);
	    panel.add(instructions);
	    panel.add(newGame);

		panel2.setBackground(Color.ORANGE);
	    panel2.add(rollList);
	    panel2.add(frame);
	    
		this.add(panel, BorderLayout.NORTH);
		this.add(panel2, BorderLayout.SOUTH);
	}
	
	private void setUpModel(){
		//Create new JList object for rolls
		Integer[] rolls = {0,1,2,3,4,5,6,7,8,9,10};
		rollList = new JList(rolls);
		rollList.setBackground(Color.ORANGE);
	}
	
	private void updateModel(){
		rollList.removeAll();
		DefaultListModel model = new DefaultListModel();
	    for(int i=0; i<=10-currentRoll; i++){
	        model.addElement(i);
	    }
	    rollList.setModel(model);
	    
	    if(isSecondRoll()){
	    	rollList.removeAll();
			DefaultListModel model2 = new DefaultListModel();
		    for(int i=0; i<11; i++){
		        model2.addElement(i);
		    }
		    rollList.setModel
		    (model2);
	    }
	}
	
	private void updateText(){
		//frame.replaceSelection(game.printBoard());
		System.out.println(game.printBoard());
	}
	
	private void registerListeners() {
		//Create listener for roll selection list
	    rollList.addListSelectionListener(new ListListener());
	    
	    //Create listener for roll button
	    ActionListener al = new RollOrNewListener();
	    roll.addActionListener(al);
	    newGame.addActionListener(al);
	}
	
    private class ListListener implements ListSelectionListener {
    	public void valueChanged(ListSelectionEvent event) {
    		if (event.getSource() == rollList) {
    			//Take selected roll and let RollListener use it
    			//currentRoll = Integer.parseInt(rollList.getSelectedValue().toString());
			}
		}
    }
    
    private class RollOrNewListener implements ActionListener{
    	
		public void actionPerformed(ActionEvent arg0) {
			
			if(arg0.getSource() == roll){
				currentRoll = Integer.parseInt(rollList.getSelectedValue().toString());
				if(currentRoll == null){ JOptionPane.showMessageDialog(null, "Select roll first."); }
				
				updateText();
				updateModel();
				game.pinsDowned(currentRoll);
				
				if(game.gameOver() == true){
					System.exit(0);
				}
			}
			else if(arg0.getSource() == newGame){
				NewGame();
			}
		}
		
    }
	
    private void NewGame(){
    	System.out.println("hmmmm");
    }
    
    private Boolean isSecondRoll(){
    	counting++;
    	
    	if(counting % 2 == 0){ return true; }
    	
    	return false; 
    }
}
