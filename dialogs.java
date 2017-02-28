package taf;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;



// handle dialog presentation and input
public class dialogs {

	public static JProgressBar scenarioProgressBar;
	public static JFrame frame;
	public static JPanel panel;
	public static int percentage;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static int debugPauseDialog (){
		
		int option = 0;
		
		Object[] options = {"pause", 
							"continue", 
							"terminate"};
		
		option = JOptionPane.showOptionDialog(null, "Debug pause called for step: "+ ActorLibrary.operationalDataset + "... do you wish to terminate?",
				"debug pause",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,null,null,null);
				
		return option;
		
	}
	
	public static void scenarioProgressMeter(String mode, int percentage){
		
		//new BorderLayout();
		//JProgressBar scenarioProgressBar;
		//scenarioProgressBar = new JProgressBar(0, 100);
		
		if (ActorLibrary.silent != "ON")
		{
			return; // silent mode on by default. Don't generate dialog.
		}
		
		switch (mode){
		case "new":
			Progressbar.createAndShowGUI(false);
		
			break;
		case "increment":
			//scenarioProgressBar.setValue(percentage);

			Progressbar.updateTask(ActorLibrary.operationalDataset,percentage);
			//Progressbar.actionPerformed(ActorLibrary.operationalDataset);
			
			break;
		case "finished":
			Progressbar.updateTask("Scenario " + ActorLibrary.scenarioIndex + " complete", 100);
			//Progressbar.closepProgressFrame();
			break;
			
		}
		
		
	}
	
	
}
