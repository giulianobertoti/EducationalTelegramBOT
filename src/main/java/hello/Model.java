package hello;

import org.bson.Document;

import com.github.fakemongo.Fongo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Model {

	//you can use the real MongoDB with the line above
	//MongoClient client = new MongoClient();
	
	Fongo fongo = new Fongo("mongo");
	
	public void addResponse(Document response){
		MongoDatabase db = fongo.getDatabase("telegram");
    	MongoCollection<Document> responses = db.getCollection("responses");
    	responses.insertOne(response);
	}
	
	public String searchResponse(Document spec){
		MongoDatabase db = fongo.getDatabase("telegram");
    	MongoCollection<Document> responses = db.getCollection("responses");
		FindIterable<Document> found = responses.find(spec);
        Document response = found.first();
        return response.getString("response");
	}
}
