

Clustering cities by Craigslist Post

Author:
	Xun Xu, Fan Yang
	Calpoly Pomona

Software implementation, clustering differnt cities based on craiglist Post, Using K-Means and FuzzyC-Mensa Algorithms.

input data "src/main/resources/training_v3.json".
example data point: {"city":"chicago","category":"photography","section":"for-sale","heading":"DVD'S AND BLU REY'S "}

ectorize each individual data point from input file and create a total. 
“28-dimensional” feature vector for each city.The city vector will look like this: 

e.g: New York :
City(NY)={(‘For Sale’%, S_avePrice%) (‘Housing’%, H_avePrice%) ( ‘Community’%, C_avePrice%) and (‘Services’%, Ser_avePrice%, )}
total 8 feature. 
add 20 extended features obtained from LDA Topic Modeling. 
e.g: New York :
City(NY){(‘ForSale’,topic_1 distribution)(‘ForSale’,topic_2 distribution)(‘ForSale’,topic_3 distribution)(‘ForSale’,topic_4 distribution)(‘ForSale’,topic_5 distribution)......(‘Services’,topic_1 distribution}.

Clustering Algorithms:
K-Means, FuzzyCMeans.

Evaluation:
Internal: SSE. External: purity.
External test file: "src/main/resources/test_k3.json" (k=3,city=16)

APIs used : Standford nlp Core, Standford ner, hackcs.lda, GMapsFx, javafx.

-SoftWare instruction:
Main Class: MainApp.java

To run program simple double click compiled jar file: "com.cs599.xx.jar"
1). click "Load Data", select input craigilist data file from "src/main/resources/training_v3.json"
2). select clustering Method "KMeans/FuzzyCMenas"
3). select to add features for each city : "Percentage Feature" , "Ave.Price Feature", "LDA Feature", and "Normaliztaion" Feature when needed. Noric that if you add LDA Feature, it will take a while for program to veclize lda feature from 20,000 post contents.
4).choose how many clusters you want to form(1-5), for FuzzyCMeans, you will need to choose Fuzzier m(m>1).
5). click "RunClustering" wait for program to finish clustering, clustered city will be maked with same color shown on the google map.
6).Clustering results will be display on the btm left corner, or you can click "Evaluation" to open evauation result in a pop-up window.

(p.s, the program is still un-bolished, which means there are still alot of bugs, for example, the city some time will not be mark or display correctly on the map)


