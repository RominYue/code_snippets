#coding = utf8
#pymongo api: http://api.mongodb.org/python/current/tutorial.html

from pymongo import MongoClient

#connection
client = MongoClient(host='183.174.228.25',port = 27017)
#use db
db = client.foo_db
#select collection, all oprations are in module collections
collection = db.restaurants

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
cursor = db.restaurants.find(filter=None, projection=None, skip=0, limit=0)
ond_document = db.restaurants.find_one(filter_or_id=None)

#count,dinstinct, sort

#aggregate

#index

print collection.find_one()
