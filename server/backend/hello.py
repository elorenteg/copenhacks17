from flask import Flask
from twilio.rest import Client
app = Flask(__name__)


# put your own credentials here
account_sid = "ACdb093f08856240f3434f71f49e746b22"
auth_token = "89a0c993ca3901c0ef352b613921a36b"


@app.route('/')
def hello_world():
    client = Client(account_sid, auth_token)

    client.messages.create(
        to="+34668825668",
        from_="+34960160242",
        body="Hi Ester this is francesc and this is my second SMS to you!!")
    return 'Hello World!'

if __name__ == '__main__':
    app.run()
