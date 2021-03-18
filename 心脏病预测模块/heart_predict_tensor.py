from hdfs import Client
from sklearn.model_selection import train_test_split
import tensorflow as tf
import matplotlib.pyplot as plt
import pandas,os,numpy


# 下载数据集
def download(hdfs_path, local_path):
    # 初始化HDFS连接
    client = Client('http://fisher.lazybone.xyz:9001', root='/')
    if os.path.exists(local_path):
        print('文件已存在')
        return
    client.download(hdfs_path=hdfs_path, local_path=local_path)


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


# 创建Tensorflow输入管道
def get_dataset(data, shuffle=True, batch_size=32):
    data = data.copy()
    label = data.pop('target')
    # 创建输入管道
    dataset = tf.data.Dataset.from_tensor_slices((dict(data), label))
    # 打乱数据
    if shuffle:
        dataset = dataset.shuffle(buffer_size=len(data))
    dataset = dataset.batch(batch_size=batch_size)
    return dataset


# TF模型训练
def tf_train(train_data, val_data, test_data):
    feature_colunms = []
    for header in ['age','cp','trestbps','chol','fbs','restecg','thalach','exang','oldpeak','slope','ca','thal']:
        feature_colunms.append(tf.feature_column.numeric_column(header))
    # 数字列
    age_column = tf.feature_column.numeric_column('age')
    # 分桶列
    age_buckets = tf.feature_column.bucketized_column(age_column, boundaries=[18, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70])
    feature_colunms.append(age_buckets)
    # 指示符列
    thal = tf.feature_column.categorical_column_with_vocabulary_list('thal', [3, 6, 7])
    # thal = tf.feature_column.categorical_column_with_vocabulary_list('thal', ['fixed', 'normal', 'reversible'])
    thal_one_hot = tf.feature_column.indicator_column(thal)
    feature_colunms.append(thal_one_hot)
    # 嵌入列
    thal_embedding = tf.feature_column.embedding_column(thal, dimension=8)
    feature_colunms.append(thal_embedding)
    # crossed交叉列
    crossed_feature = tf.feature_column.crossed_column([age_buckets, thal], hash_bucket_size=1000)
    crossed_feature = tf.feature_column.indicator_column(crossed_feature)
    feature_colunms.append(crossed_feature)

    # 创建特征层
    feature_layer = tf.keras.layers.DenseFeatures(feature_colunms)
    # 创建训练模型
    model = tf.keras.Sequential([
        feature_layer,
        tf.keras.layers.Dense(128, activation='relu'),
        tf.keras.layers.Dense(128, activation='relu'),
        tf.keras.layers.Dense(1, activation='sigmoid')
    ])
    model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
    model.fit(train_data, validation_data=val_data, epochs=5)
    # 评估
    loss, acc = model.evaluate(test_data)
    print('\nLoss: {}, Acc: {}'.format(loss, acc))


if __name__ == '__main__':
    # 读取数据
    origin_data, train, val, test = read('./心脏病诊断数据集.csv', 'gbk', ',')
    # 训练，验证，测试集输入管道
    # train_dataset = get_dataset(train)
    # val_dataset = get_dataset(val, shuffle=False)
    # test_dataset = get_dataset(test, shuffle=False)
    # tf_train(train_dataset, val_dataset, test_dataset)
    print()
