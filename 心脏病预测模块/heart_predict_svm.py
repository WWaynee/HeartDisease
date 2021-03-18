import joblib,pandas
from sklearn.preprocessing import StandardScaler

# heart SVM 模型
heart_model_path = './heart.dat'
model = joblib.load(heart_model_path)

# 归一化处理
standardScaler = StandardScaler()


# 预测单条数据
def single_predict(data):
    data = pandas.DataFrame(data, index=[0])
    # 添加one hot编码数据
    data = data.append([
        {'age':63,'sex':1,'cp':4,'trestbps':145,'chol':233,'fbs':1,'restecg':0,'thalach':150,'exang':0,'oldpeak':2.3,'slope':1,'ca':0,'thal':0,'target':1},
        {'age':63,'sex':1,'cp':3,'trestbps':145,'chol':233,'fbs':1,'restecg':0,'thalach':150,'exang':0,'oldpeak':2.3,'slope':2,'ca':0,'thal':1,'target':1},
        {'age':63,'sex':1,'cp':2,'trestbps':145,'chol':233,'fbs':1,'restecg':0,'thalach':150,'exang':0,'oldpeak':2.3,'slope':3,'ca':0,'thal':2,'target':1},
        {'age':63,'sex':1,'cp':1,'trestbps':145,'chol':233,'fbs':1,'restecg':0,'thalach':150,'exang':0,'oldpeak':2.3,'slope':1,'ca':0,'thal':3,'target':1},
    ], ignore_index=True)
    # 对非连续值：cp, slope, thal的处理
    first = pandas.get_dummies(data=data['cp'], prefix='cp')
    second = pandas.get_dummies(data=data['slope'], prefix='slope')
    third = pandas.get_dummies(data=data['thal'], prefix='thal')
    # 将新的列添加到数据表，旧的列去掉
    data = pandas.concat([data, first, second, third], axis=1)
    data = data.drop(columns=['cp', 'slope', 'thal', 'target'])
    # 归一化
    standardScaler.fit(data)
    data = standardScaler.transform(data)
    # 预测
    res = model.predict(data)
    return res[0]


# 预测一个CSV文件的数据
def multi_predict(path):
    # 读取数据
    data = pandas.read_csv(path)
    # 对非连续值：cp, slope, thal的处理
    first = pandas.get_dummies(data=data['cp'], prefix='cp')
    second = pandas.get_dummies(data=data['slope'], prefix='slope')
    third = pandas.get_dummies(data=data['thal'], prefix='thal')
    # 将新的列添加到数据表，旧的列去掉
    data = pandas.concat([data, first, second, third], axis=1)
    data = data.drop(columns=['cp', 'slope', 'thal', 'target'])
    # 归一化处理
    standardScaler.fit(data)
    data = standardScaler.transform(data)
    # 预测
    res = model.predict(data)
    return res


if __name__ == '__main__':
    # res = multi_predict('./heart.csv')
    obj = """[{
     "age": "70",
     "sex": "1",
     "cp": "4",
     "trestbps": "130",
     "chol": "322",
     "fbs": "0",
     "restecg": "2",
     "thalach": "109",
     "exang": "0",
     "oldpeak": "2.4",
     "slope": "2",
     "ca": "3",
     "thal": "3",
     "target": "2"
    }]"""
    res = single_predict(obj)
    print(res)