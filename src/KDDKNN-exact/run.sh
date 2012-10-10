#!/bin/bash



dataDir=../data/test
HADOOP=/Users/Hadoop/hadoop-0.20.2/bin/hadoop
xmlconf=src/main/resources/kddknn/conf.xml
knnjar=target/kddknn.jar


$HADOOP dfs -rmr knn-input
$HADOOP dfs -put $dataDir knn-input
#$HADOOP jar $knnjar indexing -conf $xmlconf 
#$HADOOP jar $knnjar hybridknn -conf $xmlconf 

$HADOOP dfs -rmr query
$HADOOP dfs -put ../data/query/u1 query
$HADOOP jar $knnjar queryknn -conf $xmlconf 
