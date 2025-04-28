package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;


/* Separate window that displays the current state of the search history. Refreshes automatically after each insert.
 * There's no need to re-open the window.
 */
public class HistoryWindow extends JFrame{

	private MyFrame myFrame;	
	
	//History
	private JList myList;
	private DefaultListModel<String> myModel;
	private JScrollPane historyScrollPane; //Scrolling properties (only if necessary).
	private JSeparator jSep;
	
	//Images
	private JLabel logoLabel;
	private ImageIcon logo;
	
	
	public HistoryWindow(MyFrame myFrame)
	{
		this.myFrame = myFrame;	
		
		//History
		myModel = new DefaultListModel<>();
		myList = new JList(myModel);
		historyScrollPane = new JScrollPane(myList);
		jSep = new JSeparator();

		this.setWindow();

		//Logo
		logo = new ImageIcon("logo.png");
		logoLabel = new JLabel();
		logoLabel.setIcon(logo);
		logoLabel.setText("History");
		logoLabel.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		logoLabel.setBounds(200,-20,200,100);
		this.add(logoLabel);
		
		this.refresh();

	}
	
	//Window set up.
	public void setWindow()
	{

		//New Window
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(600,600);
		this.setTitle("Recent History");
		this.setLayout(null);
		this.setResizable(false);
		this.setVisible(true);
		this.getContentPane().setBackground(new Color(220,208,255));
		
		//History
		this.add(historyScrollPane);
		historyScrollPane.setBounds(0,60,585,501);
		this.add(jSep);
		jSep.setBounds(0,50,2000,10);
	}
	
	/*
	 * Getters - Setters. Methods responsible for returning/ editing the class fields. They provide access to this class's fields from other classes.
	 */
	public DefaultListModel<String> getModel()
	{
		return myModel;
	}
	
	//Refreshes the history window with the new state of the search history, in reverse order. Most recent search should appear on top of the list.
	public void refresh()
	{
		this.getModel().clear();
		for(int i=0;i<myFrame.getHistory().size();i++)
		{
			this.getModel().addElement(myFrame.getHistory().get(myFrame.getHistory().size() - i - 1));
		}
	}
	
}
