#!/usr/bin/env python
# -*- coding:utf-8 -*-
from flask import Flask, request, jsonify
from flask_cors import CORS
import heart_predict_svm

app = Flask(__name__)
CORS(app, resources=r'/*')


@app.route('/hello')
def hello():
    return 'Hello World!'


@app.route('/single', methods=['POST'])
def single_detection():
    data = request.get_json()
    res = heart_predict_svm.single_predict(data)
    return jsonify({'msg': 'ok', 'code': '200', 'res': int(res)})


@app.route('/multi', methods=['POST'])
def multi_detection():
    path = request.data
    res = heart_predict_svm.multi_predict(path)
    return res


if __name__ == '__main__':
    app.debug = True
    app.run('127.0.0.1', port=8848)