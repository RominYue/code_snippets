using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MongoDB.Driver;
using MongoDB.Bson;
using MongoDB.Driver.Core;

namespace mongoexp
{
    class Program
    {
        static void Main(string[] args)
        {
            var connectionString = "mongodb://xxx.xxx.xxx.xx:27017";

            var client = new MongoClient(connectionString);  //connect to mongo server

            var db = client.GetDatabase("students"); //choose database

            var col = db.GetCollection<BsonDocument>("grades"); //choose collection

            BsonDocument doc = testBsonDoc();  //document in C#

            Query(col);
            Console.WriteLine("OK!");
            Console.ReadLine();
        }

        static BsonDocument testBsonDoc()
        {
            var doc = new BsonDocument();
            doc["student_id"] = 10000;
            doc["type"] = "hw2";
            //doc["address"] = new BsonDocument { { "key", "value" } };
            doc.Add("score", 30);

            /*
            var nestedArray = new BsonArray();
            nestedArray.Add("red");
            nestedArray.Add("blue");
            doc["array"] = nestedArray;
            */
            Console.WriteLine(doc);
            return doc;
        }

        static void Insert(IMongoCollection<BsonDocument> col, BsonDocument doc)
        {
            //插入操作，同步异步插入
            col.InsertOne(doc);
            // await col.InsertOneAsync(doc);
            // col.InsertMany(docs);
            // await col.InsertManyAsync(docs);
        }

        static void Query(IMongoCollection<BsonDocument> col)
        {
            //查询，同步，异步
            //Find the First Document in a Collection
            var doc = col.Find(new BsonDocument()).FirstOrDefault();
            //var document = await collection.Find(new BsonDocument()).FirstOrDefaultAsync();
            Console.WriteLine(doc);

            //Find All Documents in a Collection
            var documents = col.Find(new BsonDocument()).ToList();
            foreach(var document in documents)
            {
                Console.WriteLine(document);
            }
            //var documents = await collection.Find(new BsonDocument()).ToListAsync();
            //await collection.Find(new BsonDocument()).ForEachAsync(d => Console.WriteLine(d));

            var cursor = col.Find(new BsonDocument()).ToCursor();
            foreach(var document in cursor.ToEnumerable())
            {
                Console.WriteLine(document);
            }

            //选择条件
            var builder = Builders<BsonDocument>.Filter;
            var filter = builder.Gt("score", 50) & builder.Lte("score", 100);
            cursor = col.Find(filter).ToCursor();

            //排序
            var sortFilter = Builders<BsonDocument>.Sort.Descending("score").Ascending("type");
            cursor = col.Find(filter).Sort(sortFilter).Skip(2).Limit(2).ToCursor();

            //投影
            var projectFilter = Builders<BsonDocument>.Projection.Exclude("_id").Include("type");
            cursor = col.Find(filter).Project(projectFilter).ToCursor();
        }

        static void Update(IMongoCollection<BsonDocument> col)
        {
            var filter = Builders<BsonDocument>.Filter.Lt("score", 100);
            var update = Builders<BsonDocument>.Update.Inc("score", 100);
            var result = col.UpdateOne(filter, update);
            //var result = col.UpdateMany(filter, update);
            if (result.IsModifiedCountAvailable)
            {
                Console.WriteLine(result.ModifiedCount);
            }

            //替换 replacing
            var result_replace = col.ReplaceOne(filter, new BsonDocument("name","yomin"));

        }

        static void Delete(IMongoCollection<BsonDocument> col)
        {
            var filter = Builders<BsonDocument>.Filter.Eq("score", 110));
            var result = col.DeleteOne(filter);
        }
    }
}
