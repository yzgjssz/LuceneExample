package cn.yzg.lucene;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IndexSearchTest {
	@Test
	public void testIndexSearch() throws Exception{
		
		//创建分词器(创建索引和所有时所用的分词器必须一致)
		Analyzer analyzer = new IKAnalyzer();
		//创建查询对象,第一个参数:默认搜索域, 第二个参数:分词器
		//默认搜索域作用:如果搜索语法中指定域名从指定域中搜索,如果搜索时只写了查询关键字,则从默认搜索域中进行搜索
		QueryParser queryParser = new QueryParser("fileContext", analyzer);
		//查询语法=域名:搜索的关键字
		Query query = queryParser.parse("web");
		
		//指定索引和文档的目录
		Directory dir = FSDirectory.open(new File("E:\\lucenetest\\dic"));
		//索引和文档的读取对象
		IndexReader indexReader = IndexReader.open(dir);
		//创建索引的搜索对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//搜索:第一个参数为查询语句对象, 第二个参数:指定显示多少条
		TopDocs topdocs = indexSearcher.search(query, 5);
		//一共搜索到多少条记录
		System.out.println("=====count=====" + topdocs.totalHits);
		//从搜索结果对象中获取结果集
		ScoreDoc[] scoreDocs = topdocs.scoreDocs;
		
		for(ScoreDoc scoreDoc : scoreDocs){
			//获取docID
			int docID = scoreDoc.doc;
			//通过文档ID从硬盘中读取出对应的文档
			Document document = indexReader.document(docID);
			//get域名可以取出值 打印
			System.out.println("fileName:" + document.get("fileName"));
			System.out.println("fileSize:" + document.get("fileSize"));
			System.out.println("fileContext:" + document.get("fileContext"));
			System.out.println("============================================================");
		}
		
	}
	
	@Test
	public void testIndexTermQuery() throws Exception{
		//创建分词器(创建索引和所有时所用的分词器必须一致)
		Analyzer analyzer = new IKAnalyzer();
		
		//创建词元:就是词,   
		//没有默认搜索域
		Term term = new Term("fileName", "apache");
		//使用TermQuery查询,根据term对象进行查询
		TermQuery termQuery = new TermQuery(term);
		
		
		//指定索引和文档的目录
		Directory dir = FSDirectory.open(new File("E:\\lucenetest\\dic"));
		//索引和文档的读取对象
		IndexReader indexReader = IndexReader.open(dir);
		//创建索引的搜索对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//搜索:第一个参数为查询语句对象, 第二个参数:指定显示多少条
		TopDocs topdocs = indexSearcher.search(termQuery, 5);
		//一共搜索到多少条记录
		System.out.println("=====count=====" + topdocs.totalHits);
		//从搜索结果对象中获取结果集
		ScoreDoc[] scoreDocs = topdocs.scoreDocs;
		
		for(ScoreDoc scoreDoc : scoreDocs){
			//获取docID
			int docID = scoreDoc.doc;
			//通过文档ID从硬盘中读取出对应的文档
			Document document = indexReader.document(docID);
			//get域名可以取出值 打印
			System.out.println("fileName:" + document.get("fileName"));
			System.out.println("fileSize:" + document.get("fileSize"));
			System.out.println("fileContext:" + document.get("fileContext"));
			System.out.println("============================================================");
		}
	}
	
	@Test
	public void testNumericRangeQuery() throws Exception{
		//创建分词器(创建索引和所有时所用的分词器必须一致)
		Analyzer analyzer = new IKAnalyzer();
		
		//根据数字范围查询
		//查询文件大小,大于100 小于1000的文章
		//第一个参数:域名      第二个参数:最小值,  第三个参数:最大值, 第四个参数:是否包含最小值,   第五个参数:是否包含最大值
		Query query = NumericRangeQuery.newLongRange("fileSize", 100L, 1000L, true, true);		
		
		//指定索引和文档的目录
		Directory dir = FSDirectory.open(new File("E:\\lucenetest\\dic"));
		//索引和文档的读取对象
		IndexReader indexReader = IndexReader.open(dir);
		//创建索引的搜索对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//搜索:第一个参数为查询语句对象, 第二个参数:指定显示多少条
		TopDocs topdocs = indexSearcher.search(query, 5);
		//一共搜索到多少条记录
		System.out.println("=====count=====" + topdocs.totalHits);
		//从搜索结果对象中获取结果集
		ScoreDoc[] scoreDocs = topdocs.scoreDocs;
		
		for(ScoreDoc scoreDoc : scoreDocs){
			//获取docID
			int docID = scoreDoc.doc;
			//通过文档ID从硬盘中读取出对应的文档
			Document document = indexReader.document(docID);
			//get域名可以取出值 打印
			System.out.println("fileName:" + document.get("fileName"));
			System.out.println("fileSize:" + document.get("fileSize"));
			System.out.println("============================================================");
		}
	}
	
	@Test
	public void testBooleanQuery() throws Exception{
		//创建分词器(创建索引和所有时所用的分词器必须一致)
		Analyzer analyzer = new IKAnalyzer();
		
		//布尔查询,就是可以根据多个条件组合进行查询
		//文件名称包含apache的,并且文件大小大于等于100 小于等于1000字节的文章
		BooleanQuery query = new BooleanQuery();
		
		//根据数字范围查询
		//查询文件大小,大于100 小于1000的文章
		//第一个参数:域名      第二个参数:最小值,  第三个参数:最大值, 第四个参数:是否包含最小值,   第五个参数:是否包含最大值
		Query numericQuery = NumericRangeQuery.newLongRange("fileSize", 100L, 1000L, true, true);
		
		//创建词元:就是词,   
		Term term = new Term("fileName", "apache");
		//使用TermQuery查询,根据term对象进行查询
		TermQuery termQuery = new TermQuery(term);
		
		//Occur是逻辑条件
		//must相当于and关键字,是并且的意思
		//should,相当于or关键字或者的意思
		//must_not相当于not关键字, 非的意思
		//注意:单独使用must_not  或者 独自使用must_not没有任何意义
		query.add(termQuery, Occur.MUST);
		query.add(numericQuery, Occur.MUST);
		
		//指定索引和文档的目录
		Directory dir = FSDirectory.open(new File("E:\\dic"));
		//索引和文档的读取对象
		IndexReader indexReader = IndexReader.open(dir);
		//创建索引的搜索对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//搜索:第一个参数为查询语句对象, 第二个参数:指定显示多少条
		TopDocs topdocs = indexSearcher.search(query, 5);
		//一共搜索到多少条记录
		System.out.println("=====count=====" + topdocs.totalHits);
		//从搜索结果对象中获取结果集
		ScoreDoc[] scoreDocs = topdocs.scoreDocs;
		
		for(ScoreDoc scoreDoc : scoreDocs){
			//获取docID
			int docID = scoreDoc.doc;
			//通过文档ID从硬盘中读取出对应的文档
			Document document = indexReader.document(docID);
			//get域名可以取出值 打印
			System.out.println("fileName:" + document.get("fileName"));
			System.out.println("fileSize:" + document.get("fileSize"));
			System.out.println("============================================================");
		}
	}
	
	@Test
	public void testMathAllQuery() throws Exception{
		//创建分词器(创建索引和所有时所用的分词器必须一致)
		Analyzer analyzer = new IKAnalyzer();
		
		//查询所有文档
		MatchAllDocsQuery query = new MatchAllDocsQuery();
		
		//指定索引和文档的目录
		Directory dir = FSDirectory.open(new File("E:\\dic"));
		//索引和文档的读取对象
		IndexReader indexReader = IndexReader.open(dir);
		//创建索引的搜索对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//搜索:第一个参数为查询语句对象, 第二个参数:指定显示多少条
		TopDocs topdocs = indexSearcher.search(query, 5);
		//一共搜索到多少条记录
		System.out.println("=====count=====" + topdocs.totalHits);
		//从搜索结果对象中获取结果集
		ScoreDoc[] scoreDocs = topdocs.scoreDocs;
		
		for(ScoreDoc scoreDoc : scoreDocs){
			//获取docID
			int docID = scoreDoc.doc;
			//通过文档ID从硬盘中读取出对应的文档
			Document document = indexReader.document(docID);
			//get域名可以取出值 打印
			System.out.println("fileName:" + document.get("fileName"));
			System.out.println("fileSize:" + document.get("fileSize"));
			System.out.println("============================================================");
		}
	}
	
	@Test
	public void testMultiFieldQueryParser() throws Exception{
		//创建分词器(创建索引和所有时所用的分词器必须一致)
		Analyzer analyzer = new IKAnalyzer();
		
		String [] fields = {"fileName","fileContext"};
		//从文件名称和文件内容中查询,只有含有apache的就查出来
		MultiFieldQueryParser multiQuery = new MultiFieldQueryParser(fields, analyzer);
		//输入需要搜索的关键字
		Query query = multiQuery.parse("apache");
		
		//指定索引和文档的目录
		Directory dir = FSDirectory.open(new File("E:\\dic"));
		//索引和文档的读取对象
		IndexReader indexReader = IndexReader.open(dir);
		//创建索引的搜索对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//搜索:第一个参数为查询语句对象, 第二个参数:指定显示多少条
		TopDocs topdocs = indexSearcher.search(query, 5);
		//一共搜索到多少条记录
		System.out.println("=====count=====" + topdocs.totalHits);
		//从搜索结果对象中获取结果集
		ScoreDoc[] scoreDocs = topdocs.scoreDocs;
		
		for(ScoreDoc scoreDoc : scoreDocs){
			//获取docID
			int docID = scoreDoc.doc;
			//通过文档ID从硬盘中读取出对应的文档
			Document document = indexReader.document(docID);
			//get域名可以取出值 打印
			System.out.println("fileName:" + document.get("fileName"));
			System.out.println("fileSize:" + document.get("fileSize"));
			System.out.println("============================================================");
		}
	}
}
