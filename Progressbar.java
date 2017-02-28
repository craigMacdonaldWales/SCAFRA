/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 
package taf;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.beans.*;
import java.util.Random;

public class Progressbar extends JPanel
                             implements ActionListener, 
                                        PropertyChangeListener {

    public static JProgressBar progressBar;
    //private JButton startButton;
    public static JTextArea taskOutput;
    public static JTextArea upperPanel;
   // public static Jpanel panel;
    public Task task;
    //public JFrame frame;
    static  JFrame frame = new JFrame("Scenario Progress");
    public static JTextArea taskInfo;
    public static JTextArea dataInfo;
    public static boolean frameopen = false;

    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            //Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += dialogs.percentage;
                setProgress(Math.min(progress, 100));
            }
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
       
       
        
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            //startButton.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            taskOutput.append("Done!\n");
           // frame.setVisible(false);
        	//frame.dispose();
            
            //taskoutput.empty();
            //task.close;
            //frame.dispose();
        }
    }

    public static void updateTask(String TaskDesc, int percentage){
    	//setCursor(null); //turn off the wait cursor
        taskOutput.append(TaskDesc + "\n");
        taskOutput.setCaretPosition(taskOutput.getDocument().getLength()); // scroll to the last item?
        progressBar.setValue(percentage);
        
    	
    }
    
    
    public Progressbar() {
        super(new BorderLayout());
        
       // frame.setVisible(false);
     	//frame.dispose();
        
        //Create the demo's UI.
        //startButton = new JButton("Start");
        //startButton.setActionCommand("start");
        //startButton.addActionListener(this);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        taskInfo = new JTextArea(5,10);
        
        dataInfo = new JTextArea(10,30);
        
        taskInfo.append(String.format("Scenario being processed :" + ActorLibrary.scenarioIndex));
        dataInfo.append(String.format("input data"));
        dataInfo.append("\n");
        
        progressBar.setStringPainted(true);
        
        taskOutput = new JTextArea(10, 30);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
        //taskOutput.maintainPosition(true);
        //taskOutput.stickToBottom(true);
        
       // taskInfo.append(String.format("Scenario being processed :" + ActorLibrary.scenarioIndex));
       
        
        JPanel panel = new JPanel();
        //panel.add(startButton);
        panel.add(taskInfo);
        panel.add(progressBar);
        panel.add(dataInfo);
        
        add(panel, BorderLayout.PAGE_START);
        //add(upperPanel, BorderLayout.CENTER);
        JScrollPane scrollableTextArea = new JScrollPane(taskOutput);
        JScrollPane scrollableTextAreaData = new JScrollPane(dataInfo);
        //scrollableTextArea.
       // maintainPosition(true);
        
        //add (scrollableTextArea, BorderLayout.CENTER); // centres the component
       // maintainPosition(true);
        //scrollableTextArea.setValue(getMaximumSize());
        //scrollableTextArea.maintainPosition().setValue(true);
        
      
        panel.add(scrollableTextArea);
        panel.add(scrollableTextAreaData);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    }

    /**
     * Invoked when the user presses the start button.
     */
    public void actionPerformed(ActionEvent evt) {
       // startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
        
        //taskInfo.append(String.format(ActorLibrary.scenarioIndex));
    }

    /**
     * Invoked when task's progress property changes.
     */
    public static void propertyChangeData(String AKA, String data) {
    	
    	//taskInfo.append(String.format("Scenario being processed :" + ActorLibrary.scenarioIndex));
    	
       // if ("progress" == evt.getPropertyName()) {
           // int progress = (Integer) evt.getNewValue();
            //progressBar.setValue(progress);
            //dataInfo.append(String.format("\n", task.getProgress()));
    		dataInfo.append("Data input - item: " + AKA + " data " + data + "\n");
    		//dataInfo.append("\n");
            dataInfo.setCaretPosition(dataInfo.getDocument().getLength());
            //taskOutput.append(String.format("\r"));
            
        //} 
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
    	
    	//taskInfo.append(String.format("Scenario being processed :" + ActorLibrary.scenarioIndex));
    	
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            //taskOutputData.append(String.format("\n", task.getProgress()));
            
            //taskOutput.append(String.format("\r"));
            
        } 
    }

    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    static void createAndShowGUI(boolean closeIndicator) {
        //Create and set up the window.
    	
    	if (frameopen){
	    	try {
	    		frame.setVisible(false);
	    		frameopen = false;
	    		//frame.dispose();
	    	//
	           // Thread.sleep(1);
	        } catch (NullPointerException ignore) {}
    	}
         	//return;
         //}
    	//if (frame != null){
    		//frame.dispose();
    	//}
    	
    	
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        
        //scenarioOutput = new JTextArea(5, 20);
        //scenarioOutput.append(ActorLibrary.scenarioIndex);
        //scenarioOutput.setEditable(false);
        //frame.setContentPane(scenarioOutput);
        
        //Create and set up the content pane.
        JComponent newContentPane = new Progressbar();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        
        //JPanel upperPanel = new JPanel();
        //upperPanel.add(new JTextArea("Scenario in progress:" + ActorLibrary.scenarioIndex));
        
        //frame.getContentPane().add(upperPanel);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        frameopen = true; /// stop creation of further dialogs!
        
       
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(false);
            }
        });
    }
    
    public static void closepProgressFrame(){
    	frame.setVisible(false);
    	frame.dispose();
    }
    
}