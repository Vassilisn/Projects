package commands;

import java.awt.event.KeyEvent;

import view.MyFrame;

/* This class is responsible for the utility of "auto-completion". Its suggesting properties are limited to the search history only.
 * So after the history list has at least one query, this class will begin to auto-fill the query being typed at the moment.
 * Only if the keys pressed thus far, are considered substring for a word in the search history.
 * In the case that multiple words contain the current substring, the most recent one will be suggested/ autofilled.
 * Until the key press that distinguishes those words apart.
 */
public class AutoComplete {

	private MyFrame myFrame;
	
	//constructor
	public AutoComplete(MyFrame myFrame)
	{
		this.myFrame = myFrame;
	}
	
	
	public void checkKeyPress(java.awt.event.KeyEvent e)
	{
		//If either backspace or delete are pressed the "autofilled" part of the word should be deleted and suggestion process should be halted
		//Until another key press takes place.
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) 
		{
			//Do nothing. Interrupt the suggestion.
		}
		else
		{
			String checkMe = myFrame.getQ(); //Current state of the query. Changes after each key press
			//Iterate through the search history to see if a word contains the current state of our query.
			for(String data:myFrame.getHistory())
			{
				
				String checkFrom = ""; //Empty string. Will be filled with the potential suggestion.
				for(int i=0; i<checkMe.length(); i++)
				{
					if(checkMe.length()<data.length())
					{
						checkFrom = checkFrom + data.charAt(i); //Change string to the substring potentially missing from the query.
					}
				}
				if(checkFrom.equals(checkMe))
				{
					myFrame.getTextField().setText(data); //Auto-fill the rest of the word
					//"setSelectionStart" and "setSelectionEnd" help in filling only the rest of the word missing.
					myFrame.getTextField().setSelectionStart(checkMe.length()); 
					myFrame.getTextField().setSelectionEnd(data.length());
				}
			}
		}
		

		
		
		
	}
	
}
