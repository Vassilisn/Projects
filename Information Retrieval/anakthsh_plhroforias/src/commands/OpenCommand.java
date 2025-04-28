package commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import org.apache.commons.io.FilenameUtils;

import com.opencsv.exceptions.CsvException;

import index.Indexer;
import search.QParser;
import view.MyFrame;

public class OpenCommand implements ActionListener{
	
	//Indexer- Query handler
	private Indexer myInd; 
	private QParser myQParser;
	private JFileChooser fileChooser; //Basically the pop-up window that enables the input file selection.
	private String path;
	private MyFrame myFrame;
	
	//constructor
	public OpenCommand(MyFrame frame)
	{
		path = new String("");
		myFrame = frame;
		fileChooser = new JFileChooser();

	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.openFile();
		} catch (IOException | CsvException e1) {
			e1.printStackTrace();
		}
	}
	
	public void openFile() throws IOException, CsvException
	{
		fileChooser.setCurrentDirectory(new File(".")); //Sets default folder for the file chooser to the one of the current program
		int response = fileChooser.showOpenDialog(null);
		
		if(response == JFileChooser.APPROVE_OPTION) //if a file was, indeed, selected..
		{
			path = fileChooser.getSelectedFile().getAbsolutePath(); //Store the path of the selected file
			if(FilenameUtils.getExtension(path).equals("csv")) //Check the extension of the file being opened.
			{
				myInd = new Indexer(path,myFrame);
				myQParser = new QParser(myInd,myFrame);
			}
			
		}else {
			System.out.println("You didn't choose a csv file"); //Either 'X' or "cancel" got pressed at the file choosing window.
		}
		
	}
	
	/*
	 * Getters - Setters. Methods responsible for returning/ editing the class fields. They provide access to this class's fields from other classes.
	 */
	
	public QParser getQParser()
	{
		return myQParser;
	}
}
