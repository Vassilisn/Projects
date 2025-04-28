package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

public class RecommendationsWindow extends JFrame{
	
	private MyFrame myFrame;	
	private JList myList;
	private DefaultListModel<String> recModel;
	private JScrollPane recommendationsScrollPane; //Scrolling properties (only if necessary).
	private JSeparator jSep;
	
	private JLabel logoLabel;
	private ImageIcon logo;
		
	public RecommendationsWindow(MyFrame myFrame)
	{
		this.myFrame = myFrame;	
		
		//Recommendations
		recModel = new DefaultListModel<>();
		myList = new JList(recModel);
		recommendationsScrollPane = new JScrollPane(myList);
		jSep = new JSeparator();
		
		this.setWindow();

		//Logo
		logo = new ImageIcon("logo.png");
		logoLabel = new JLabel();
		logoLabel.setIcon(logo);
		logoLabel.setText("Recommended");
		logoLabel.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		logoLabel.setBounds(200,-20,600,100);
		this.add(logoLabel);
		
		this.refresh();

	}
	
	//Window set up.
	public void setWindow()
	{

		//New Window
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(600,600);
		this.setTitle("Recommendations");
		this.setLayout(null);
		this.setResizable(false);
		this.setVisible(true);
		this.getContentPane().setBackground(new Color(220,208,255));
		
		//Rec
		this.add(recommendationsScrollPane);
		recommendationsScrollPane.setBounds(0,60,585,501);
		this.add(jSep);
		jSep.setBounds(0,50,2000,10);
	}

	/*
	 * Getters - Setters. Methods responsible for returning/ editing the class fields. They provide access to this class's fields from other classes.
	 */
	public DefaultListModel<String> getModel()
	{
		return recModel;
	}
	
	//Refreshes the window with the new state of recommendations.
	public void refresh()
	{
		
		ArrayList<String> s = new ArrayList<String>();
		ArrayList<String> s2 = new ArrayList<String>();

		if(myFrame.getRecommendationsList().size() != 0)
		{
			this.getModel().clear();
			for(int i=0;i<myFrame.getRecommendationsList().size();i++)
			{
				String gLine[] = myFrame.getRecommendationsList().get(i).get("genre").split(",");
				for(int j=0; j<gLine.length;j++)
				{
					
					if(!s.contains(gLine[j].trim()))
					{
						s.add(gLine[j].trim());
					}
				}
				for(int j=1; j<5;j++)
				{
					String cLine = myFrame.getRecommendationsList().get(i).get("star"+ j);
					if(!s2.contains(cLine.trim()))
					{
						s2.add(cLine.trim());
					}
				}
				
			}
			
			this.getModel().addElement("Recommended Genres based on your activity:");
			this.getModel().addElement("");
			for(String x : s)
			{
				this.getModel().addElement(x);
			}
	
			this.getModel().addElement("Recommended Actors based on your activity:");
			this.getModel().addElement("");
			for(String x : s2)
			{
				this.getModel().addElement(x);
			}
			
		}else {
			this.getModel().addElement("No recommendations available based on your recent activity.");
		}
	}
	
}
