package index;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import view.MyFrame;

public class Indexer {
	
	private MyFrame myFrame;
	private Document doc;
	private String sourcePath;
	private File indexPath;
	private Directory dir;
	private IndexWriter indexWriter;
	private File myFile;
	private StandardAnalyzer analyzer;
	private IndexWriterConfig indexWriterConfig;
	private List<IndexableField> docFields = new ArrayList<IndexableField>();
	
	public Indexer(String path, MyFrame myFrame) throws IOException
	{
		
		this.myFrame = myFrame;
		sourcePath = path; //Path of the file to be opened.
		myFile = new File(sourcePath);
		/* The following few lines create a separate directory called "Indexes" inside the parent directory of the opened file.
		 * It will be dedicated to storing the created indexes.
		 * Paths.get(path).getParent().toString(), specifically is used to automatically create the new directory inside the parent directory of the opened fiel.
		 * Presuming that the user will want the indexes to be created in the same directory as the source file.
		 * If that's not the case for you, please change the path accordingly.
		 */
		File newDir = new File(Paths.get(path).getParent().toString() + "\\Indexes");
		newDir.mkdir();
		indexPath = new File(Paths.get(path).getParent().toString() + "\\Indexes"); 
		analyzer = new StandardAnalyzer(); //Standard analyzer for the tokenization of the text.
		dir = FSDirectory.open(indexPath.toPath());
		indexWriterConfig = new IndexWriterConfig(analyzer); //Configurations for the index writer
		indexWriterConfig.setOpenMode(OpenMode.CREATE);	 //Creates a new index or overwrites an existing one.
		indexWriter = new IndexWriter(dir,indexWriterConfig); //Configures index writer using the analyzer.

		
		this.indexFile();
		
	}
	
	public void indexFile() throws IOException
	{
		//Iterate through the opened file
		Reader in = new FileReader(myFile);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
		/*Isolate the important csv columns and store them in TextFields.
		 *Then add said TextFields to the same Document obj.
		 */
		for(CSVRecord rec : records)
		{
			doc = new Document();
			doc.add(new TextField("title",rec.get("Series_Title"),Field.Store.YES));
			doc.add(new TextField("rating",rec.get("IMDB_Rating"),Field.Store.YES));
			doc.add(new TextField("year",rec.get("Released_Year"),Field.Store.YES));
			doc.add(new TextField("genre",rec.get("Genre"),Field.Store.YES));
			doc.add(new TextField("director",rec.get("Director"),Field.Store.YES));
			doc.add(new TextField("star1",rec.get("Star1"),Field.Store.YES));
			doc.add(new TextField("star2",rec.get("Star2"),Field.Store.YES));
			doc.add(new TextField("star3",rec.get("Star3"),Field.Store.YES));
			doc.add(new TextField("star4",rec.get("Star4"),Field.Store.YES));
			doc.add(new TextField("overview",rec.get("Overview"),Field.Store.YES));

			indexWriter.addDocument(doc); //Adds the created doc obj to the index.
			
		}	
		docFields = doc.getFields(); //List of the document fields. Will be used later for remote access to the document's field names.
		indexWriter.commit(); //Commits all pending changes to the index.
		indexWriter.close(); //Index changes have finished. Now index should be closed.
	}
	
	/*
	 * Getters - Setters. Methods responsible for returning/ editing the class fields. They provide access to this class's fields from other classes.
	 */
	public StandardAnalyzer getAnalyzer()
	{
		return analyzer;
	}

	public File getIndexPath() {
		return indexPath;
	}

	public Directory getDir() {
		return dir;
	}
	
	public List<IndexableField> getDocFields()
	{
		return docFields;
	}
}
