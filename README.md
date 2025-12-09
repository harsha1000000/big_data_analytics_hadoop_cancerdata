# big_data_analytics_hadoop_cancerdata
 Big Data Analytics on Cancer Dataset Using Hadoop

This project focuses on applying Big Data Analytics techniques using the Hadoop ecosystem to perform large-scale processing and analysis of a cancer dataset. The objective is to use distributed computing to efficiently handle, preprocess, and analyze cancer patient data for malignancy prediction and pattern discovery.

Project Objective

The main goal of this project is to:

Store and process large cancer datasets using HDFS

Implement MapReduce jobs to perform data cleaning, filtering, and feature extraction

Analyze cancer characteristics (e.g., tumor size, shape, texture)

Identify malignant vs benign samples through parallel computation

Demonstrate how Big Data tools improve performance and scalability in healthcare analytics

 Architecture & Workflow
Cancer Dataset → HDFS Upload → MapReduce Jobs → Output Files → Analysis → Results/Visualization

Tools Used:

Hadoop HDFS

Hadoop MapReduce

Hive / Pig (optional)

Java / Python for MapReduce

Local Hadoop cluster / Docker-based Hadoop

 Dataset Used

The project uses the Breast Cancer Wisconsin Dataset, which contains numerical diagnostic features of breast cell nuclei extracted from medical images.

Key attributes:

Radius

Texture

Smoothness

Compactness

Symmetry

Fractal Dimension

Class: Benign / Malignant

Dataset size can be scaled for Big Data simulation.

 Hadoop Components Used
1. HDFS

Stores the cancer dataset in distributed chunks

Supports parallel data access

Ensures fault tolerance

2. MapReduce

Used for:

Data cleaning

Missing value handling

Feature computation

Counting malignant vs benign cases

Summary statistics (mean, min, max per feature)

3. YARN

Resource manager

Allocates cluster nodes for MapReduce tasks


 How It Works
 Step 1 — Upload Dataset to HDFS
hdfs dfs -put cancerdata.csv /user/hadoop/input/

 Step 2 — Run MapReduce Job
hadoop jar canceranalysis.jar CancerDriver /user/hadoop/input /user/hadoop/output

 Step 3 — View Output
hdfs dfs -cat /user/hadoop/output/part-r-00000

 Results

Successfully processed dataset using Hadoop cluster

Reduced execution time for large dataset simulations

Identified malignant vs benign case distribution

Extracted meaningful statistics using distributed processing

Demonstrated how Big Data techniques help in healthcare analytics

 Technologies Used

Hadoop (HDFS + MapReduce + YARN)

Java / Python

Linux / Ubuntu / Hadoop on Docker

Big Data Concepts

Healthcare Analytics

 Conclusion

This project demonstrates how Hadoop can efficiently process large healthcare datasets and enable faster computation for predictive analytics. Big Data techniques like distributed storage, parallel processing, and fault tolerance make Hadoop suitable for healthcare data processing, real-time monitoring, and disease prediction.
