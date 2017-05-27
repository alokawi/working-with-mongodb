/**
 * 
 */
package alokawi.mongodb.core;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * @author alokkumar
 *
 */
public class MongoDBGroupByCountExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MongoDBGroupByCountExample dbExample = new MongoDBGroupByCountExample();

		dbExample.run();
	}

	private void run() {
		try (MongoClient mongoClient = new MongoClient()) {
			MongoDatabase database = mongoClient.getDatabase("local");
			MongoCollection<Document> collection = database.getCollection("startup_log");

			AggregateIterable<Document> aggregateIterable = collection.aggregate(Arrays.asList(new Document("$group",
					new Document("_id", "$buildinfo.version").append("versionCount", new BasicDBObject("$sum", 1)))));
			MongoCursor<Document> iterator = aggregateIterable.iterator();
			while (iterator.hasNext()) {
				Document document = (Document) iterator.next();
				System.out.println(document);
				Integer count = document.getInteger("versionCount");
				System.out.println(count);

			}

		}
	}

}
