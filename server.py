import json
import csv
from flask import Flask, jsonify, Response, request

from functools import wraps

def check_auth(username, password):
    return username == "username" and password == "password"


def authenticate():
    """Sends a 401 response that enables basic auth"""
    return Response(
        "Could not verify your access level for that URL.\n"
        "You have to login with proper credentials",
        401,
        {"WWW-Authenticate": 'Basic realm="Login Required"'},
    )


def requires_auth(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        auth = request.authorization
        if not auth or not check_auth(auth.username, auth.password):
            return authenticate()
        return f(*args, **kwargs)

    return decorated

app = Flask(__name__)


@app.route("/storeTruth")
@requires_auth
def set_state():
    truePoints = request.args.get('truePoints')
    fileToSave = "truePointsUser1.csv";
    
    with open(fileToSave, 'a', newline='') as csvFile:
        writer = csv.writer(csvFile)
        writer.writerow([truePoints])
        csvFile.close()
    return jsonify({"truePoints": truePoints})

@app.route("/storeLie")
@requires_auth
def set_temp():
    liePoints = request.args.get('liePoints')
    pressure = request.args.get('pressure')
    accelerationX = request.args.get('accelerationX')
    inputTimeDiff = request.args.get('inputTimeDiff')
    
    with open("lieUser1.csv", 'a', newline='') as csvFile:
        writer = csv.writer(csvFile)
        writer.writerow([liePoints, pressure, accelerationX, inputTimeDiff])
        csvFile.close()
    return jsonify({"liePoints": liePoints, "pressure": pressure, "accelerationX": accelerationX, "inputTimeDiff": inputTimeDiff})


if __name__ == "__main__":
    app.run(host="0.0.0.0")
