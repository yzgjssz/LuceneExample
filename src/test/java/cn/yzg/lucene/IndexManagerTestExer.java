package cn.yzg.lucene;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IndexManagerTestExer {
	@Test
	public void testIndexCreate()throws Exception{
		//创建文档列表，保存多个Document
		List<Document> docList = new ArrayList<Document>();
		//指定文件夹所在目录
		File dir = new File("E:\\lucenetest\\searchsource");
		for(File file:dir.listFiles()){
			//获取文件名称
			String fileName=file.getName();
			//文件内容
			String fileContext=FileUtils.readFileToString(file);
			//文件的大小
			Long fileSize=FileUtils.sizeOf(file);
			//新建document对象
			Document doc = new Document();
			/*
			 * 是否分词：要
			 * 是否索引：要
			 * 是否存储：要
			 */
			TextField nameField= new TextField("filename", fileName, Store.YES);
			/*
			 * 是否分词：要
			 * 是否索引：要
			 * 是否存储：可要可不要
			 */
			TextField contextField= new TextField("fileContext",fileContext,Store.NO);
			/*
			 * 是否分词：要
			 * 是否索引：要
			 * 是否存储：要
			 */
			LongField longField = new LongField("fileSize",fileSize,Store.YES);
			//将所有的域都存入到文档中
			doc.add(nameField);
			doc.add(contextField);
			doc.add(longField);
			//将文档存入文档集合中
		}
		//创建分词器
		Analyzer analyzer= new IKAnalyzer();
		//指定索引和文档存储的目录
		Directory directory = FSDirectory.open(new File("E:\\lucenetest\\dic"));
		//创建写对象的初始化对象
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
		//创建索引和文档写对象
		IndexWriter indexWriter = new IndexWriter(directory,config);
		//将文档加入到索引和文档的写对象中
		for(Document doc:docList){
			indexWriter.addDocument(doc);
		}
		//提交
		indexWriter.commit();
		//关闭流
		indexWriter.close();
	}
}
