from sklearn.model_selection import train_test_split
import tensorflow as tf
import matplotlib.pyplot as plt
import pandas,os,numpy


# 读取文件
def read(path, encoding, sep):
    # 读取文件
    reader = pandas.read_csv(path, encoding=encoding, sep=sep)
    # 训练集，验证集，测试集分割
    train, test = train_test_split(reader, test_size=0.1, random_state=1)
    train, val = train_test_split(train, test_size=0.1, random_state=1)
    print('Length of train: {}'.format(len(train)))
    print('Length of val: {}'.format(len(val)))
    print('Length of test: {}'.format(len(test)))
    # 返回数据
    return reader, train, val, test


# THAL列转化
def convert_thal(data):
    data.loc[data['thal'] == 'fixed'] = 3
    data.loc[data['thal'] == 'normal'] = 7
    data.loc[data['thal'] == 'reversible'] = 5
    print(data.head())
    return data


# 数据归一化
def normalize(data):
    mean = numpy.mean(data, axis=0)
    std = numpy.std(data, axis=0)
    data = (data - mean) / std
    return data


# 训练模型
def train_model(data):
    data = convert_thal(data)
    # 提取Y轴
    train_label = data.pop('target')
    # 将DataFrame转为Array
    train_data = data.values
    # 归一化
    train_data = normalize(train_data)
    # DataFrame转Array
    train_label = train_label.values
    m = len(train_data)
    n = 13
    # 建立线性模型
    model = tf.keras.Sequential()
    model.add(tf.keras.layers.Dense(1, input_shape=(n,)))
    model.compile(optimizer='adam', loss='mse')
    log = model.fit(train_data, train_label, epochs=200)
    print(log)
    return model


# 测试集测试
def test_data(model, test):
    test_label = test.pop('target')
    test_label = test_label.values
    test = test.values
    test = normalize(test)
    result = model.predict(test)
    # print(result)


if __name__ == '__main__':
    # 读取数据
    origin_data, train, val, test = read('./心脏病诊断数据集.csv', 'gbk', ',')
    # 线性回归预测
    model = train_model(train)
    # 测试集测试
    test_data(model, test)