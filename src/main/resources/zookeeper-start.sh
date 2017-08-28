#!/bin/sh
cd  server0/zookeeper-3.4.10
bin/zkServer.sh start conf/zoo.cfg
cd ../../server1/zookeeper-3.4.10
bin/zkServer.sh start conf/zoo.cfg
cd ../../server2/zookeeper-3.4.10
bin/zkServer.sh start conf/zoo.cfg
