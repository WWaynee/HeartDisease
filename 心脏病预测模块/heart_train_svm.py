import pandas,joblib
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import GridSearchCV
from sklearn.metrics import classification_report
from sklearn.metrics import confusion_matrix

# 导入数据
data = pandas.read_csv('./heart.csv')

# 对非连续值：cp, slope, thal的处理
first = pandas.get_dummies(data=data['cp'], prefix='cp')
second = pandas.get_dummies(data=data['slope'], prefix='slope')
third = pandas.get_dummies(data=data['thal'], prefix='thal')

# 将新的列添加到数据表，旧的列去掉
data = pandas.concat([data, first, second, third], axis=1)
data = data.drop(columns=['cp', 'slope', 'thal'])

# 生成数据集和标签集
label = data['target'].values
data = data.drop(labels='target', axis=1)

# 训练集，测试集分割
data_train, data_test, label_train, label_test = train_test_split(data, label, random_state=1)
# 归一化处理
standardScaler = StandardScaler()
standardScaler.fit(data_train)
data_train = standardScaler.transform(data_train)
data_test = standardScaler.transform(data_test)

# 模型建立，寻找更好的参数
param_grid = [
    {
        'C': [0.001, 0.01, 0.1, 1, 10, 100],
        'penalty': ['l2'],
        'class_weight': ['balanced', None]
    }
]

# 模型训练
model = GridSearchCV(LogisticRegression(), param_grid=param_grid)
model.fit(data_train, label_train)

# 预测
predict_res = model.predict(data_test)

# 预测结果
print(classification_report(label_test, predict_res))
print(confusion_matrix(label_test, predict_res))

# 保存模型
# path = './heart.dat'
# joblib.dump(model, path)
# print('Model Saved!')
