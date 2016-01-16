package mongo_api;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoTest {

    public static void main(String[] args) {
        //connect
        MongoClient client = new MongoClient("xxx.xxx.xxx.xx", 27017);

        MongoDatabase db = client.getDatabase("students");

        MongoCollection<Document> collection = db.getCollection("grades");

        Insert(collection);
        Query(collection);
        Update(collection);
        Delete(collection);

        System.out.println("DataBase OK!");
    }

    public static Document CreateDoc() {
        Document doc = new Document().append("student_id", 1200)
                                     .append("type", "hw")
                                     .append("score", 50);
        return doc;
    }

    public static void Insert(MongoCollection<Document> collection) {
        Document doc = CreateDoc();
        collection.insertOne(doc);
        //collection.insertMany(arg0);
    }

    public static void Query(MongoCollection<Document> collection){

        System.out.print("Find One");
        Document doc = collection.find().first();

        System.out.print("Find All into");
        List<Document> doclist = collection.find().into(new ArrayList<Document>());
        for (Document cur: doclist){
            //do some thing
        }

        System.out.print("Find All Iterable");
        MongoCursor<Document> cur = collection.find().iterator();
        try {
            while (cur.hasNext()) {
                Document document = cur.next();
                //do some thing
            }
        } finally {
            cur.close();
        }

        System.out.print("Count");
        long cnt = collection.count();

        //Query with a filter
        Bson filter = Filters.and(Filters.eq("student_id", 5), Filters.gt("score", 45));
        doclist =  collection.find(filter).into(new ArrayList<Document>());
        //Query with projections
        Bson projection = Projections.fields(Projections.include("student_id","score"),
                                                                     Projections.exclude("_id"));
        doclist =  collection.find(filter).projection(projection).into(new ArrayList<Document>());
        //Query with sort,skip,limit
        Bson sort = Sorts.orderBy(Sorts.ascending("student_id","score"), Sorts.descending("type"));
        doclist =  collection.find(filter)
                             .projection(projection)
                             .sort(sort)
                             .limit(10)
                             .skip(5)
                             .into(new ArrayList<Document>());
    }

    public static void Update(MongoCollection<Document> collection){
        Bson filter = Filters.and(Filters.gt("score", 45));
        //replace
        collection.replaceOne(filter, new Document("score",40)
                                                  .append("type", "hw")
                                                  .append("update", true));
        //update
        collection.updateOne(filter, new Document("$set", new Document("score",20)));
        collection.updateMany(filter, new Document("$inc", new Document("score",20)));
    }

    public static void Delete(MongoCollection<Document> collection){
        Bson filter = Filters.and(Filters.gt("score", 45));
        collection.deleteOne(filter);
        collection.deleteMany(filter);
    }
}
