#coding = utf8
#pymongo api: http://api.mongodb.org/python/current/tutorial.html

from pymongo import MongoClient

#connection
client = MongoClient(host='183.174.228.2',port = 38018)
#use db
db = client.cnnews
#select collection, all oprations are in module collections
collection = db.cnnewsdata

#insert
#db.restaurants.insert_one(document)
#db.restaurants.insert_many(document_list)

#update
#db.restaurants.replace_one(filter, replacement, upsert = False)
#db.restaurants.update_one(filter, update, upsert=False)
#db.restaurants.update_many(filter, update, upsert=False)

#delete
#db.restaurants.delete_one(filter)
#db.restaurants.delete_many(filter)

#find
#cursor = db.restaurants.find(filter=None, projection={'content':1}, skip=0, limit=2)
#ond_document = db.restaurants.find_one(filter_or_id=None)

#count,dinstinct, sort

#aggregate

#index

if __name__ == '__main__':
    ofs = open('./cnnews.tsv','w')
    cursor = db.restaurants.find(filter=None, projection={'content':1}, skip=0, limit=2)
    for doc in cursor:
        print doc['_id'],doc['content']
        print type(doc['_id']), type(doc['content'])
        tmp = doc['_id'] + '\t' + doc['content'] + '\n'
        ofs.write(tmp)
    ofs.close()
