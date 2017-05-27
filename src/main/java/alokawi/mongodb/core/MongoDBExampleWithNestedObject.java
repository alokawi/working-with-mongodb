/**
 * 
 */
package alokawi.mongodb.core;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * @author alokkumar
 *
 */
public class MongoDBExampleWithNestedObject {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MongoDBExampleWithNestedObject dbExample = new MongoDBExampleWithNestedObject();

		dbExample.run();
	}

	private void run() {
		try (MongoClient mongoClient = new MongoClient()) {
			MongoDatabase database = mongoClient.getDatabase("local");
			MongoCollection<Document> collection = database.getCollection("startup_log");

			Bson filter = Filters.or(new Document("buildinfo.version", "3.2.1"), new Document("buildinfo.version", 32));
			
			System.out.println(filter);
			
			FindIterable<Document> documents = collection.find(filter);

			MongoCursor<Document> iterator = documents.iterator();
			int count = 0;
			while (iterator.hasNext()) {
				Document document = (Document) iterator.next();
				System.out.println("###" + count++ + " : " + document);

				Document buildInfoDocument = (Document) document.get("buildinfo");

				/*-
				 * "version" : "3.2.1",
					"gitVersion" : "a14d55980c2cdc565d4704a7e3ad37e4e535c1b2",
					"modules" : [ ],
					"allocator" : "system",
					"javascriptEngine" : "mozjs",
				 */

				// prints 3.2.1
				String version = buildInfoDocument.getString("version");
				System.out.println(version);

				// prints null
				String string = document.getString("buildinfo.version");
				System.out.println(string);

				break;
			}

		}
	}

}
