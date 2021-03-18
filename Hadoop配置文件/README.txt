Hadoop版本：3.2.1
JDK版本：1.8
使用方法：
	1. 将Hadoop文件夹中"HADOOP_HOME/etc/hadoop"中的配置文件替换为"Hadoop配置文件"中的文件
	2. 修改"HADOOP_HOME/etc/hadoop/hadoop-env.sh"，将其中"export JAVA_HOME"变量修改为本机JDK位置
注意：如果需要部署到云服务器上，需要进行以下额外操作
	1. 修改本机hostname，将其修改为本机域名（最快捷的方法，不需要客户机修改host文件）
	2. 修改云服务器host文件，添加一条规则: HOSTNAME PUBLIC_IP。HOSTNAME为本机域名，PUBLIC_IP为本机公网IP地址
	3. 开放云服务器9000-9010端口