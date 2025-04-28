package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import index.Indexer;
import view.MyFrame;


public class QParser {
		
	private Indexer indexer;
	private MyFrame myFrame;
	
	//constructor
	public QParser(Indexer indexer, MyFrame myFrame)
	{
		this.myFrame = myFrame;
		this.indexer = indexer;
	}
	
	/* inField -> The name of the field in which we should search for the query
	 * query -> Keyword(s) typed in the search text field
	 * Searches through the indexes to match the query keyword to the field value of a stored doc. 
	 */
	public List<Document> searchIndex(String inField,String query) throws ParseException, IOException, BadLocationException
	{
		
		Query q = new QueryParser(inField,indexer.getAnalyzer()).parse(query);
		IndexReader indexReader = DirectoryReader.open(indexer.getDir());
		IndexSearcher searcher = new IndexSearcher(indexReader); 
		TopDocs topDocs = searcher.search(q, 1000); //Search this amount of matches			
		List<Document> documents = new ArrayList<>();
		for(ScoreDoc scoreDoc : topDocs.scoreDocs)
		{
			documents.add(searcher.doc(scoreDoc.doc));
			myFrame.refRes(searcher.doc(scoreDoc.doc)); //Call the method responsible for editing the result area.
			myFrame.getSearch().setFlag(true);
		}
	
		for(int i=0;i<documents.size();i++)
		{
			myFrame.getRecommendationsList().add(documents.get(i));
		}
		
		return documents;
	}
	
	public List<Document> fuzzySearchIndex(String inField,String query) throws IOException, BadLocationException
	{
				
		Query fq = new FuzzyQuery(new Term(inField,query),1);
		IndexReader indexReader = DirectoryReader.open(indexer.getDir());
		IndexSearcher searcher = new IndexSearcher(indexReader); 
		TopDocs topDocs = searcher.search(fq, 1000); //Search this amount of matches			
		List<Document> documents = new ArrayList<>();
		for(ScoreDoc scoreDoc : topDocs.scoreDocs)
		{
			documents.add(searcher.doc(scoreDoc.doc));
			myFrame.refRes(searcher.doc(scoreDoc.doc)); //Call the method responsible for editing the result area.
		}
		return documents;
	}
	
	/*
	 * Getters - Setters. Methods responsible for returning/ editing the class fields. They provide access to this class's fields from other classes.
	 */
	public Indexer getIndexer()
	{
		return indexer;
	}
	
}
