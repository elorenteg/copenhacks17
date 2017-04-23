from flask import Flask
from flask import request
from twilio.rest import Client
from bson import json_util
from pymongo import MongoClient
from bson.objectid import ObjectId
from datetime import datetime
from datetime import timedelta
from flask import jsonify
from flask_cors import CORS, cross_origin
import json
import threading

app = Flask(__name__)
CORS(app)

client = MongoClient()

# put your own credentials here
account_sid = "ACdb093f08856240f3434f71f49e746b22"
auth_token = "89a0c993ca3901c0ef352b613921a36b"
mongoclient = MongoClient('localhost', 27017)




db = client.copenhagen

@app.route('/user/<id>/loc/<latlong>', methods=['POST'])
def save_loc(id, latlong):
    users = db.users
    print id
    custos = users.find_one({"_id":  ObjectId(id)})
    custos['latlong']=latlong
    users.update({'_id': ObjectId(id)}, {"$set": custos}, upsert=False)
    newcusto =users.find_one({"_id":  ObjectId(id)})
    jsonCusto = newcusto
    jsonText = json.dumps(jsonCusto, default=json_util.default)
    return jsonify(json.loads(jsonText))


@app.route('/user', methods=['POST'])
def save_user():
    data = request.data
    jsondata = json.loads(data)
    users = db.users
    user_data = jsondata
    user_id = users.insert_one(user_data).inserted_id
    print user_id
    custos = users.find_one({"_id": user_id})
    jsonCusto = custos
    jsonText = json.dumps(jsonCusto, default=json_util.default)
    return jsonify(json.loads(jsonText))


@app.route('/user/<id>', methods=['GET'])
def get_user(id):
    users = db.users
    print id
    custos = users.find_one({"_id": ObjectId(id)})
    jsonCusto = custos
    jsonText = json.dumps(jsonCusto, default=json_util.default)
    return jsonify(json.loads(jsonText))


@app.route('/users', methods=['GET'])
def get_users():
    users = db.users
    print id
    custos = list(users.find())
    jsonCusto = custos
    jsonText = json.dumps(jsonCusto, default=json_util.default)
    return jsonify(json.loads(jsonText))


def send_sms(arg):
    print json.dumps(arg)
    jsondata = arg
    users = db.users
    for i in jsondata['atenders']:
        user = users.find_one({"_id": ObjectId(i['id'])})
        #print user['phonenumber']
        client = Client(account_sid, auth_token)
        body = "Welcome SyncMe"+i['id']+" your event starts in two hours"
        print body
        if 'phonenumber' in user:
            client.messages.create(
                to=user['phonenumber'],
                from_="+34960160242",
                body=body)


@app.route('/event/<userid>', methods=['POST'])
def save_event(userid):
    users = db.users
    data = request.data
    jsondata = json.loads(data)
    now = datetime.now()
    date_event = datetime.strptime(jsondata['date'], '%Y-%m-%dT%H:%M:%S.%fZ')
    run_at = date_event - timedelta(hours=2)
    print run_at
    delay = (run_at - now).total_seconds()
    atenderslist=[]
    for i in jsondata['atenders']:
        atenderslist.append(ObjectId(i['id']))
        user = users.find_one({"_id": ObjectId(i['id'])})
        #print user['phonenumber']
        client = Client(account_sid, auth_token)
        body = "Welcome SyncMe"+i['id']+" you are invited to the event the day "+str(date_event.date())+" at "+str(date_event.time())
        print body
        if 'phonenumber' in user:
            client.messages.create(
                to=user['phonenumber'],
                from_="+34960160242",
                body=body)


    if delay > 0:
        print "aramset"
        threading.Timer(delay, send_sms, [jsondata]).start()

    #custos = users.find_one({"_id":  ObjectId(userid)})
    users.find_and_modify(query={'_id': ObjectId(userid)},
                          update={"$set": {'atenders': atenderslist}})
    #users.update({'_id': ObjectId(userid)}, {"$set": custos}, upsert=False)
    newcusto =users.find_one({"_id":  ObjectId(userid)})
    print delay

    jsonCusto = newcusto
    jsonText = json.dumps(jsonCusto, default=json_util.default)
    return jsonify(json.loads(jsonText))


@app.route('/user/<id>/send', methods=['POST'])
def send_custom(id):
    users = db.users
    data = request.data
    jsondata = json.loads(data)
    print id
    custos = users.find_one({"_id": ObjectId(id)})
    print custos['atenders']
    atenders = list(users.find({'_id': {'$in':custos['atenders']}}))
    for user in atenders:
        client = Client(account_sid, auth_token)
        body = "Hi SyncMe" + str(user['_id']) + " you have received a message: "+jsondata['message']
        print body
        if 'phonenumber' in user:
            client.messages.create(
                to=user['phonenumber'],
                from_="+34960160242",
                body=body)
    jsonCusto = list(atenders)
    jsonText = json.dumps(jsonCusto, default=json_util.default)
    return jsonify(json.loads(jsonText))


@app.route('/user/<id>/atenders', methods=['GET'])
def get_atender_list(id):
    users = db.users
    print id
    custos = users.find_one({"_id": ObjectId(id)})
    print custos['atenders']
    atenders = list(users.find({'_id': {'$in':custos['atenders']}}))
    print atenders
    jsonCusto = atenders
    jsonText = json.dumps(jsonCusto, default=json_util.default)
    return jsonify(json.loads(jsonText))


if __name__ == '__main__':
    app.run()
