package commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import search.QParser;
import view.MyFrame;
import view.RecommendationsWindow;

public class SearchCommand implements ActionListener{

	private String query;
	private MyFrame myFrame;
	private QParser qParser;
	private List<Document> ret;
	private boolean flag;
	
	//constructor
	public SearchCommand(MyFrame myFrame) {
		query = "";
		ret = new ArrayList<Document>();
		this.myFrame = myFrame;
		flag = false;
		qParser = myFrame.getqParser();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
				
		flag = false; //Boolean helper variable. Set to false by default. If there are EXACT matches for the query in the index, it is set to true.
		
		/* The same query shouldn't be recorded twice. Only new ones should be added to the history list.
		 * If a query already exists in the history list, but not on the top(last search keyword), then it will be replaced on the top, if searched again.
		 */
		if(!myFrame.getHistory().contains(myFrame.getQ()))
		{
			myFrame.setHistory(myFrame.getQ());
			myFrame.getWindow().refresh();

		}else if(myFrame.getHistory().contains(myFrame.getQ()))
		{
			myFrame.getHistory().remove(myFrame.getQ());
			myFrame.setHistory(myFrame.getQ());
			myFrame.getWindow().refresh();
		}
		
		/* Get query from GUI's text field. Look for any selected radiobuttons that will restrict the search to a specific field only.
		 * If none is selected the the search will take place for every single of the fields.
		 */
		try {
			if(!myFrame.getQ().equals(""))
			{
				if(myFrame.getSelectedRadioButton().equals("title") || myFrame.getSelectedRadioButton().equals("genre") || myFrame.getSelectedRadioButton().equals("year") || myFrame.getSelectedRadioButton().equals("overview"))
				{
					ret = myFrame.getOpen().getQParser().searchIndex(myFrame.getSelectedRadioButton(), myFrame.getQ());
				}
				else if(myFrame.getSelectedRadioButton().equals("") || myFrame.getSelectedRadioButton().equals("All"))
				{
					ret = myFrame.getOpen().getQParser().searchIndex("title", myFrame.getQ());
					ret = myFrame.getOpen().getQParser().searchIndex("year", myFrame.getQ());
					ret = myFrame.getOpen().getQParser().searchIndex("genre", myFrame.getQ());
					ret = myFrame.getOpen().getQParser().searchIndex("rating", myFrame.getQ());
					ret = myFrame.getOpen().getQParser().searchIndex("director", myFrame.getQ());
					ret = myFrame.getOpen().getQParser().searchIndex("star1", myFrame.getQ());
					ret = myFrame.getOpen().getQParser().searchIndex("star2", myFrame.getQ());
					ret = myFrame.getOpen().getQParser().searchIndex("star3", myFrame.getQ());
					ret = myFrame.getOpen().getQParser().searchIndex("star4", myFrame.getQ());
					ret = myFrame.getOpen().getQParser().searchIndex("overview", myFrame.getQ());

				}
				else if(myFrame.getSelectedRadioButton().equals("Cast & Crew"))
				{
					ret = myFrame.getOpen().getQParser().searchIndex("director", myFrame.getQ());
					ret = myFrame.getOpen().getQParser().searchIndex("star1", myFrame.getQ());					
					ret = myFrame.getOpen().getQParser().searchIndex("star2", myFrame.getQ());
					ret = myFrame.getOpen().getQParser().searchIndex("star3", myFrame.getQ());
					ret = myFrame.getOpen().getQParser().searchIndex("star4", myFrame.getQ());
					
				}
				
				
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		
		/* Check if the initial search had exact matches.
		 * If not (flag is still set to false) attempt a fuzzy search based on Levenshtein's distance algorithm.
		 * Distance between the two words ( 1 -> query, 2 -> potential match) is set to 1 character.
		 */
		if(flag == false)
		{
			String errorMsg = "No results found for \"" + myFrame.getQ() + "\".";
			String fuzzyMsg = "\n Attempting fuzzy search based on Levenshtein's algorithm with distance equal to 1.";
			JOptionPane.showMessageDialog(null, errorMsg + fuzzyMsg, "Message Window: ", JOptionPane.INFORMATION_MESSAGE);
			
			try {
				if(!myFrame.getQ().equals(""))
				{
					if(myFrame.getSelectedRadioButton().equals("title") || myFrame.getSelectedRadioButton().equals("genre") || myFrame.getSelectedRadioButton().equals("year"))
					{
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex(myFrame.getSelectedRadioButton(), myFrame.getQ());
					}
					else if(myFrame.getSelectedRadioButton().equals("") || myFrame.getSelectedRadioButton().equals("All"))
					{
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("title", myFrame.getQ());
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("year", myFrame.getQ());
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("genre", myFrame.getQ());
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("rating", myFrame.getQ());
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("director", myFrame.getQ());
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("star1", myFrame.getQ());
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("star2", myFrame.getQ());
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("star3", myFrame.getQ());
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("star4", myFrame.getQ());
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("overview", myFrame.getQ());

					}
					else if(myFrame.getSelectedRadioButton().equals("Cast & Crew"))
					{
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("director", myFrame.getQ());
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("star1", myFrame.getQ());
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("star2", myFrame.getQ());
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("star3", myFrame.getQ());
						ret = myFrame.getOpen().getQParser().fuzzySearchIndex("star4", myFrame.getQ());
					}
				}
			} catch (IOException | BadLocationException e1) {
				e1.printStackTrace();
			}

		}
	}
	
	/*
	 * Getters - Setters. Methods responsible for returning/ editing the class fields. They provide access to this class's fields from other classes.
	 */
	
	public void setFlag(boolean f)
	{
		this.flag = f;
	}
	
	public boolean getFlag()
	{
		return flag;
	}
	
}
