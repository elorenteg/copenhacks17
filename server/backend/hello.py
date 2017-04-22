from flask import Flask
from flask import request
from twilio.rest import Client
from pymongo import MongoClient
import json

app = Flask(__name__)
client = MongoClient()

# put your own credentials here
account_sid = "ACdb093f08856240f3434f71f49e746b22"
auth_token = "89a0c993ca3901c0ef352b613921a36b"
mongoclient = MongoClient('localhost', 27017)

db = client.copenhagen

@app.route('/event', methods=['POST'])
def save_event():
    data = request.json
    print("datais")
    print(json.dumps(data))
    '''
    client = Client(account_sid, auth_token)

    client.messages.create(
        to="+34668825668",
        from_="+34960160242",
        body="Hi Ester this is francesc and this is my second SMS to you!!")
    '''
    return 'Hello World!'

if __name__ == '__main__':
    app.run()
