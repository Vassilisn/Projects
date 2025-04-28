package view;

import commands.AutoComplete;
import commands.OpenCommand;
import commands.SearchCommand;
import index.Indexer;
import model.Collection;
import search.QParser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

public class MyFrame extends JFrame implements ActionListener{
	
	//Indexer,QParser
	/* 
	 * Objects associated with the indexing and query-result process.
	 */
	private Indexer myInd;
	private QParser qParser;
	
	//Menu bar
	private JMenuBar menuBar;
	
	//Menu tabs. Buttons on the menu bar
	private JMenu fileMenu;
	private JMenu historyMenu;
	private JMenu recommendedMenu;
	private JMenu sortMenu;
	
	//Menu items. The drop down buttons appearing under each menu tab.
	private JMenuItem openItem;
	private JMenuItem exitItem;
	private JMenuItem showHistory;
	private JMenuItem showRecommended;
	private JMenuItem sortByRating;
	private JMenuItem sortByYear;
	
	//Text field
	/* Objects associated with the search text field. The auto complete object is responsible for adding the auto complete properties to the text field.
	 * Its suggestions are restricted to the search history. So you'll see suggestions only for words already search for.
	 */
	private JTextField textField;
	private AutoComplete auto;
	
	//Buttons
	private JButton searchButton;
	
	/*
	 * Buttons to restrict the search to a specific field. If none is selected search will occur for every field.
	 */
	
	private JRadioButton titleRadioButton;
	private JRadioButton yearRadioButton;
	private JRadioButton genreRadioButton;
	private JRadioButton overviewRadioButton;
	private JRadioButton cncRadioButton;
	private JRadioButton allRadioButton;
	
	//Group. All the JRadioButtons belong to this group. This way only one can be selected at a time.
	private ButtonGroup radioButtonGroup;
	
	//commands
	/* OpenCommand object is responsible for file input.
	 * SearchCommand is responsible for extracting the query from the text field and searching for it in the index.
	 */
	private OpenCommand myOpen;
	private SearchCommand mySearch;
	
	//History
	//Search history
	private ArrayList<String> history;
	
	//Collection
	private Collection myCollection = new Collection();
	
	//Images
	//Program logo
	private JLabel logoLabel;
	private ImageIcon logo;

	//Separator. Just a line basically. It's purpose is to separate the search text field from the result text pane.
	private JSeparator sep;
	
	//HistoryPage
	private HistoryWindow newWin;
	
	//Recommendations Window
	private RecommendationsWindow recWindow;
	private ArrayList<Document> recommendations;
	
	//Results. Matchings.
	private String results;
	private int counter;
	
	private JTextPane txt; //Result area
	private JScrollPane jsp; //Enables scrolling in the result area(only when needed).
	
	//Objects necessary for giving different colors to words.
	private StyleContext cont; 
    private DefaultStyledDocument styledDoc;
	private ArrayList<String> dcList;
	private ArrayList<Document> documentList;
	
	private DefaultHighlighter.DefaultHighlightPainter myHighlighter;
	
    //constructor
	public MyFrame()
	{
		
		
		//Text field
		/*
		 * KeyListener detects when a key is pressed & released. This is necessary for the word suggestion/ auto completion.
		 */
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent e)
			{
				auto.checkKeyPress(e);
			}
		});
		auto = new AutoComplete(this);
		
		//Buttons
		searchButton = new JButton("Search");
		titleRadioButton = new JRadioButton("title");
		yearRadioButton = new JRadioButton("year");
		genreRadioButton = new JRadioButton("genre");
		overviewRadioButton = new JRadioButton("overview");
		cncRadioButton = new JRadioButton("Cast & Crew");
		allRadioButton = new JRadioButton("All");
		
		//Button group
		radioButtonGroup = new ButtonGroup();
		
		//Menu bar
		menuBar = new JMenuBar();
		
		//Menu tabs
		fileMenu = new JMenu("File");
		historyMenu = new JMenu("History");
		recommendedMenu = new JMenu("Recommended");
		sortMenu = new JMenu("Sort By");
		
		//Menu items
		openItem = new JMenuItem("Open");
		exitItem = new JMenuItem("Exit");
		showHistory = new JMenuItem("Show History");
		showRecommended = new JMenuItem("Show Recommendations");
		sortByYear = new JMenuItem("Year");
		sortByRating = new JMenuItem("Rating");

		
		//commands
		myOpen = new OpenCommand(this);
		mySearch = new SearchCommand(this);
		
		//History
		history = new ArrayList<String>();
		
		//Recommendations
		recommendations = new ArrayList<Document>();
		
		//Label
		logoLabel = new JLabel();
		
		//Separator
		sep = new JSeparator();

		//Results
		cont = new StyleContext();
		styledDoc = new DefaultStyledDocument();
		dcList = new ArrayList<String>();
		documentList = new ArrayList<Document>();
		
		txt = new JTextPane(styledDoc);
		jsp = new JScrollPane(txt, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setBounds(new Rectangle(0,55,1000,700));
		txt.setEditable(false);
		this.add(jsp);
		
		//Highlighter
		myHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
		
		this.setGUI();		
		
	}
	
	public void setGUI()
	{
		
		/*
		 * JFrame basic parameters. Basically setting up the GUI.	
		 */
		this.setSize(1000,800);	//Windows size
		this.getContentPane().setBackground(new Color(220,208,255)); //Windows color.			
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Enables windows closing when the 'X' is pressed(in the upper right corner).
		this.setLayout(null); //Setting this to null disables any layout "templates". We will have to give each frame component its coordinates manually.
		this.setLocationRelativeTo(null); //Windows spawn location.
		this.setResizable(false); //Setting this to false doesn't allow the resizing of the GUI.
		this.setTitle("Information Retrieval"); //Program title.
		this.setVisible(true); //Without this there will be nothing visible to the user.
		
		/*
		 * widgets
		 */

		//Menu bar
		this.setJMenuBar(menuBar);
		menuBar.setVisible(true);
		
		//Menu tabs
		menuBar.add(fileMenu);
		menuBar.add(historyMenu);
		menuBar.add(recommendedMenu);
		menuBar.add(sortMenu);
		
		//Menu items
		fileMenu.add(openItem);
		fileMenu.add(exitItem);
		historyMenu.add(showHistory);
		recommendedMenu.add(showRecommended);
		sortMenu.add(sortByYear);
		sortMenu.add(sortByRating);
		
		//Images
		logo = new ImageIcon("logo.png");
		logoLabel.setIcon(logo);
		logoLabel.setBounds(10,10,80,27);
		this.add(logoLabel);
		
		//JTextField
		textField.setPreferredSize(new Dimension(250,40));
		textField.setBounds(100,10,500,20);
		this.add(textField);

		//JButtons
		this.add(searchButton);
		searchButton.setBounds(605,10,80,19);
		
		//JRadioButtons
		this.add(titleRadioButton);
		titleRadioButton.setFocusable(false); //This is only for aesthetic purposes. Removes the effect on top of the name of a radiobutton, when selected.
		titleRadioButton.setBounds(140,32,50,15);
		titleRadioButton.setBackground(new Color(220,208,255));

		this.add(yearRadioButton);
		yearRadioButton.setFocusable(false); //This is only for aesthetic purposes. Removes the effect on top of the name of a radiobutton, when selected.
		yearRadioButton.setBounds(260,32,55,15);
		yearRadioButton.setBackground(new Color(220,208,255));

		this.add(genreRadioButton);
		genreRadioButton.setFocusable(false); //This is only for aesthetic purposes. Removes the effect on top of the name of a radiobutton, when selected.
		genreRadioButton.setBounds(195,32,60,15);
		genreRadioButton.setBackground(new Color(220,208,255));

		this.add(cncRadioButton);
		cncRadioButton.setFocusable(false); //This is only for aesthetic purposes. Removes the effect on top of the name of a radiobutton, when selected.
		cncRadioButton.setBounds(335,32,100,15);
		cncRadioButton.setBackground(new Color(220,208,255));

		this.add(allRadioButton);
		allRadioButton.setFocusable(false); //This is only for aesthetic purposes. Removes the effect on top of the name of a radiobutton, when selected.
		allRadioButton.setBounds(95,32,40,15);
		allRadioButton.setBackground(new Color(220,208,255));
		
		this.add(overviewRadioButton);
		overviewRadioButton.setFocusable(false); //This is only for aesthetic purposes. Removes the effect on top of the name of a radiobutton, when selected.
		overviewRadioButton.setBounds(450,32,100,15);
		overviewRadioButton.setBackground(new Color(220,208,255));
		
		//ButtonGroup.Add radiobuttons to the group. This way only one can be selected at a time.
		radioButtonGroup.add(titleRadioButton);
		radioButtonGroup.add(genreRadioButton);
		radioButtonGroup.add(yearRadioButton);
		radioButtonGroup.add(cncRadioButton);
		radioButtonGroup.add(allRadioButton);
		radioButtonGroup.add(overviewRadioButton);

		
		//Separator
		this.add(sep);
		sep.setBounds(0,50,2000,2);
		
		//Action Listeners
		/*
		 * Responsible for the "event driven" actions. Something has to happen when a button is pressed.
		 */
		openItem.addActionListener(this);
		exitItem.addActionListener(this);
		showHistory.addActionListener(this);
		showRecommended.addActionListener(this);
		searchButton.addActionListener(this);
		sortByYear.addActionListener(this);
		sortByRating.addActionListener(this);
		searchButton.setFocusable(false);
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == openItem)
		{
			myOpen.actionPerformed(e); //User has opted for the file opening option
		}else if(e.getSource() == showHistory) {
			newWin = new HistoryWindow(this); //User wishes to see the search history
		}else if(e.getSource() == showRecommended) {
				recWindow = new RecommendationsWindow(this);
		}else if(e.getSource() == searchButton){ //User pressed search.
			txt.setText(""); //After each search the result area has to be cleared, for the new results to take place
			counter = 0; //Enumerate each result.
			dcList.clear();
			documentList.clear();
			if(newWin == null) //Create history window after search button is pressed(only if "Show History" button hasn't been pressed prior to that)
			{
				newWin = new HistoryWindow(this);
				newWin.dispatchEvent(new WindowEvent(newWin, WindowEvent.WINDOW_CLOSING));
			}
			if(recWindow == null) //Create rec window after search button is pressed(only if "Show Recommendations" button hasn't been pressed prior to that)
			{
				recWindow = new RecommendationsWindow(this);
				recWindow.dispatchEvent(new WindowEvent(recWindow, WindowEvent.WINDOW_CLOSING));
			}
			mySearch.actionPerformed(e);
			try {
				this.highlightTxt();
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(e.getSource() == sortByYear || e.getSource() == sortByRating){	
			counter = 0; //Enumerate each result.
			try {
				this.sortRes(e);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				this.highlightTxt();
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(e.getSource() == exitItem) {
			this.dispose();
		}
	}
	
	public void refRes(Document doc) throws BadLocationException
	{
		/* Some queries match multiple fields of a certain doc but should only show up once.
		 * Add doc to the results already shown(dcList). If the doc is already contained in the list it should not be shown an extra time. 
		 * Doc added -> Increment enumerator
		 */
		
		
		if(!dcList.contains(doc.get("title"))) 
		{
			documentList.add(doc);
			dcList.add(doc.get("title"));  
			DefaultStyledDocument dcm = new DefaultStyledDocument();
			counter += 1;
			Style style = cont.addStyle("myStyle", null);

			for(int i=0; i<doc.getFields().size();i++)
			{
				if(i==0)
				{
					StyleConstants.setForeground(style, Color.BLACK); //Enumerator + '.' color is black
					results = counter + ". ";
					styledDoc.insertString(styledDoc.getLength(), results, style); //Insert current no. + '.' in black colored font
				}
				StyleConstants.setForeground(style, Color.RED); //Field name color is red.
				results = doc.getFields().get(i).name();
				styledDoc.insertString(styledDoc.getLength(), results, style); //Insert field name in red.

				//Field contents are standard black.
				StyleConstants.setForeground(style, Color.BLACK);
				results = ": " + doc.getFields().get(i).stringValue();
				styledDoc.insertString(styledDoc.getLength(), results, style);
				styledDoc.insertString(styledDoc.getLength(), "\t", style); //Fields in the same line are separated by tab
				if(i==3 || i==8)
				{
					styledDoc.insertString(styledDoc.getLength(), "\n", style); // "Split" doc fields in different lines
				}

			}
			styledDoc.insertString(styledDoc.getLength(), "\n", style);
			
			/*
			 * Insert a (blue colored) separating line between each result doc.
			 */
			StyleConstants.setForeground(style, Color.BLUE);
			results = "-";
			for(int i=0;i<8;i++)
			{
				results += results;
			}
			styledDoc.insertString(styledDoc.getLength(), results, style);
			styledDoc.insertString(styledDoc.getLength(), "\n", style);

		}

	}
	
	
	//Method responsible for highlighting the query term, among the results list.
	public void highlightTxt() throws BadLocationException 
	{
		
		int pos = 0;
		boolean flag = false;
		int length = this.getQ().length();
		
		//Iterate through the text in the result display area.
        while ((pos + length) <= txt.getDocument().getLength()) 
        {

        	String match = txt.getDocument().getText(pos, length).toLowerCase(); // Lowercase is used to ensure that the term will be highlighted even if typed in upper/lower case.
        	if(match.equals(this.getQ().toLowerCase()))
        	{
        		txt.getHighlighter().addHighlight(pos, (pos + length), myHighlighter);
        	}
        	pos ++; //increase caret position by 1.
        }
		
	}
	
	//Sort results by either rating or year. This method is almost identical to "refRes" in terms of handling the result insertion to the display area.
	public void sortRes(ActionEvent ev) throws BadLocationException
	{
		ArrayList<String> dcList2 = new ArrayList<String>();
		DefaultStyledDocument dcm = new DefaultStyledDocument();
		counter += 1;
		Style style = cont.addStyle("myStyle", null);
		

		
		if(ev.getSource() == sortByYear)
		{
			
			txt.setText("");
			for(int i=0; i<documentList.size();i++)
			{
				for(int j=0; j<(documentList.size() -1); j++ )
				{
					
					int temp1 = Integer.parseInt(documentList.get(j+1).getField("year").stringValue());
					int temp2 = Integer.parseInt(documentList.get(j).getField("year").stringValue());
					
					Document tempDoc;

					if(temp1 < temp2)
					{
						Collections.swap(documentList, j, j+1);
					}
				}
			}
			
			
			
			for(int i=0; i<documentList.size();i++)
			{
				for(int j=0; j<documentList.get(i).getFields().size();j++)
				{
					
					
					if(j==0)
					{
						StyleConstants.setForeground(style, Color.BLACK); //Enumerator + '.' color is black
						results = counter + ". ";
						styledDoc.insertString(styledDoc.getLength(), results, style); //Insert current no. + '.' in black colored font
					}
					StyleConstants.setForeground(style, Color.RED); //Field name color is red.
					results = documentList.get(i).getFields().get(j).name();
					styledDoc.insertString(styledDoc.getLength(), results, style); //Insert field name in red.

					//Field contents are standard black.
					StyleConstants.setForeground(style, Color.BLACK);
					results = ": " + documentList.get(i).getFields().get(j).stringValue();
					styledDoc.insertString(styledDoc.getLength(), results, style);
					styledDoc.insertString(styledDoc.getLength(), "\t", style); //Fields in the same line are separated by tab
					if(j==3 || j==8)
					{
						styledDoc.insertString(styledDoc.getLength(), "\n", style); // "Split" doc fields in different lines
					}

				}
				styledDoc.insertString(styledDoc.getLength(), "\n", style);
				
				/*
				 * Insert a (blue colored) separating line between each result doc.
				 */
				StyleConstants.setForeground(style, Color.BLUE);
				results = "-";
				for(int k=0;k<8;k++)
				{
					results += results;
				}
				styledDoc.insertString(styledDoc.getLength(), results, style);
				styledDoc.insertString(styledDoc.getLength(), "\n", style);
				counter += 1;


		}	
		}else if(ev.getSource() == sortByRating)
		{
			txt.setText("");
			for(int i=0; i<documentList.size();i++)
			{
				for(int j=0; j<(documentList.size() -1); j++ )
				{
					
					double temp1 = Double.parseDouble(documentList.get(j+1).getField("rating").stringValue());
					double temp2 = Double.parseDouble(documentList.get(j).getField("rating").stringValue());
					
					Document tempDoc;

					if(temp1 < temp2)
					{
						Collections.swap(documentList, j, j+1);
					}
				}
			}
			
			for(int i=0; i<documentList.size();i++)
			{
				for(int j=0; j<documentList.get(i).getFields().size();j++)
				{
					if(j==0)
					{
						StyleConstants.setForeground(style, Color.BLACK); //Enumerator + '.' color is black
						results = counter + ". ";
						styledDoc.insertString(styledDoc.getLength(), results, style); //Insert current no. + '.' in black colored font
					}
					StyleConstants.setForeground(style, Color.RED); //Field name color is red.
					results = documentList.get(i).getFields().get(j).name();
					styledDoc.insertString(styledDoc.getLength(), results, style); //Insert field name in red.

					//Field contents are standard black.
					StyleConstants.setForeground(style, Color.BLACK);
					results = ": " + documentList.get(i).getFields().get(j).stringValue();
					styledDoc.insertString(styledDoc.getLength(), results, style);
					styledDoc.insertString(styledDoc.getLength(), "\t", style); //Fields in the same line are separated by tab
					if(j==3 || j==8)
					{
						styledDoc.insertString(styledDoc.getLength(), "\n", style); // "Split" doc fields in different lines
					}

				}
				styledDoc.insertString(styledDoc.getLength(), "\n", style);
				
				/*
				 * Insert a (blue colored) separating line between each result doc.
				 */
				StyleConstants.setForeground(style, Color.BLUE);
				results = "-";
				for(int k=0;k<8;k++)
				{
					results += results;
				}
				styledDoc.insertString(styledDoc.getLength(), results, style);
				styledDoc.insertString(styledDoc.getLength(), "\n", style);
				counter += 1;

			}
		}
	}	
		
	
	//Return the name of the radio button currently selected. If none is selected return value is an empty string ("").
	public String getSelectedRadioButton()
	{
		for(Enumeration<AbstractButton> buttons = radioButtonGroup.getElements(); buttons.hasMoreElements();)
		{
			AbstractButton button = buttons.nextElement();
			
			if(button.isSelected())
			{
				return button.getText();
			}
		}
		return "";
	}
	
	/*
	 * Getters - Setters. Methods responsible for returning/ editing the class fields. They provide access to this class's fields from other classes.
	 */
	public JTextField getTextField()
	{
		return textField;
	}
	
	public String getQ() {
		return textField.getText();
	}
	
	public ArrayList<String> getHistory()
	{
		return history;
	}
	
	public void setHistory(String q)
	{
		history.add(q);
	}
	
	public Collection getCollection()
	{
		return myCollection;
	}

	public Indexer getMyInd() {
		return myInd;
	}

	public void setMyInd(Indexer myInd) {
		this.myInd = myInd;
	}

	public QParser getqParser() {
		return qParser;
	}

	public void setqParser(QParser qParser) {
		this.qParser = qParser;
	}
	
	public OpenCommand getOpen()
	{
		return myOpen;
	}
	
	public SearchCommand getSearch()
	{
		return mySearch;
	}
	
	public HistoryWindow getWindow()
	{
		return newWin;
	}
	
	public RecommendationsWindow getRecWin()
	{
		return recWindow;
	}
	
	public ArrayList<Document> getRecommendationsList()
	{
		return recommendations;
	}
}
