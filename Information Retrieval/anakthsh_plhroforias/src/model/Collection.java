package model;

import java.util.ArrayList;

import org.apache.lucene.document.Document;

public class Collection {
	
	ArrayList<Document> docList;
	
	public Collection() {
		docList = new ArrayList<Document>();
	}
	
	public void addDoc(Document doc) {
		docList.add(doc);
	}
	
	public void printCollection()
	{
		for(int i=0;i<docList.size();i++)
		{
		}
	}
	
}
