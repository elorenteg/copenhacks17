from flask import Flask
from flask import request
from twilio.rest import Client
from pymongo import MongoClient
from datetime import datetime
from datetime import timedelta
import json
import threading

app = Flask(__name__)

client = MongoClient()

# put your own credentials here
account_sid = "ACdb093f08856240f3434f71f49e746b22"
auth_token = "89a0c993ca3901c0ef352b613921a36b"
mongoclient = MongoClient('localhost', 27017)


def send_sms(arg):
    print(arg)
    '''
    client = Client(account_sid, auth_token)
    client.messages.create(
        to="+34668825668",
        from_="+34960160242",
        body="Hi Ester this is francesc and this is my second SMS to you!!")
    '''


db = client.copenhagen


@app.route('/event', methods=['POST'])
def save_event():
    data = request.data
    jsondata = json.loads(data)
    now = datetime.now()

    run_at = datetime.strptime(jsondata['date'], '%Y-%m-%dT%H:%M:%S.%fZ') + timedelta(seconds=10)
    print run_at
    delay = (run_at - now).total_seconds()
    print delay
    threading.Timer(delay, send_sms, ["some message"]).start()
    return json.dumps(data)


if __name__ == '__main__':
    app.run()
