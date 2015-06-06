import org.apache.commons.collections.ArrayStack;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;

/**
 * @author
 *
 */
public class FuzzyCMeansClustering {

    private static final String FILE_DATA_IN = "src/main/resources/training_v3.json";//输入的样本数据
    private static final String FILE_PAR = "src/main/resources/training_v3.json";//参数配置
    private static final String FILE_CENTER = "src/main/resources/fuzzyC_centroids.json";//聚类中心
    private static final String FILE_MATRIX = "src/main/resources/fuzzyC_uMatrix.json";//隶属度矩阵

    public int numpattern;//样本数
    public int dimension;//每个样本点的维数
    public int cata;//要聚类的类别数
    public int maxcycle;//最大的迭代次数
    public double m;//参数m
    public double limit;//误差限

    public ArrayList<Vector> cityVecs;
    ArrayList<ArrayList<Double>> centroids =new ArrayList<>();
    private ArrayList<Cluster> clusters;




    public FuzzyCMeansClustering() {
        super();
    }

    public FuzzyCMeansClustering(int numpattern, int dimension, int cata, int maxcycle, double m, double limit) {
        this.numpattern = numpattern;
        this.dimension = dimension;
        this.cata = cata;
        this.maxcycle = maxcycle;
        this.m = m;
        this.limit = limit;
    }

    public FuzzyCMeansClustering(int k, double fuzzy, double limit, ArrayList<Vector> s){
        this.numpattern = s.size();
        this.dimension = s.get(0).data.size();
        this.cata =k;
        this.maxcycle =25;
        this.m = fuzzy;
        this.limit =limit;

        this.cityVecs =s;
        this.clusters = new ArrayList<>();
        this.clusters = new ArrayList<Cluster>();

        for (int i=0; i<k; i++){
            Cluster c = new Cluster();
            c.centroid = new ArrayList<>();
            c.vectors = new ArrayList<>();
            clusters.add(c);
        }
    }

    /**
     * 读取配置文件
     * @return
     */
    public boolean getPar() {

        //打开配置文件

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(FILE_PAR));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //读取配置文件
        String line = null;
        for (int i = 0; i < 6; i++) {
            try {
                line = br.readLine();
            } catch (IOException e) {
                // TODO 自动生成 catch 块
                e.printStackTrace();
            }
            switch(i) {
                case 0: numpattern = Integer.valueOf(line); break;
                case 1: dimension = Integer.valueOf(line); break;
                case 2: cata = Integer.valueOf(line); break;
                case 3: m = Double.valueOf(line); break;
                case 4: maxcycle = Integer.valueOf(line); break;
                case 5: limit = Double.valueOf(line); break;
            }
        }
        //读取配置文件
		
	/*	try{
			InputStream in = getClass().getResourceAsStream("E:\\weka_learing\\DataMing\\src\\initParam.properties");//将配置文件读取到InputStream对象中
			Properties prop = new Properties();
			prop.load(in);
			numpattern = Integer.valueOf(prop.getProperty("numpattern"));
			dimension = Integer.valueOf(prop.getProperty("dimension"));
			cata = Integer.valueOf(prop.getProperty("cata"));
			m = Integer.valueOf(prop.getProperty("m"));
			maxcycle = Integer.valueOf(prop.getProperty("maxcycle"));
			limit = Integer.valueOf(prop.getProperty("limit"));
			
		}catch(IOException e){
			e.printStackTrace();
		}
	*/

        return true;
    }


    /**
     * 读取样本
     * @param pattern
     * @return
     */
    //样本保存在二维数组中
//    public boolean getPattern(double[][] pattern) {
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new FileReader(FILE_DATA_IN));
//        } catch (FileNotFoundException e) {
//            // TODO 自动生成 catch 块
//            e.printStackTrace();
//        }
//
//        //读取样本文件
//        String line = null;
//        String regex = "\\s+";//String regex = ",";
//        int row =0;
//        while (true) {
//            try {
//                line = br.readLine();
//            } catch (IOException e) {
//                // TODO 自动生成 catch 块
//                e.printStackTrace();
//            }
//
//            if (line == null)
//                break;
//
//            String[] split = line.split(regex);//字符串以','拆分
//
//            for (int i = 0; i < split.length; i++){
//                pattern[row][i] = Double.valueOf(split[i]);
//            }
//            row++;
//        }
//
//        return false;
//    }



    /**
     * 求数组最大值最小值
     * @param a 输入
     * @return 返回一个包含2个元素的一维数组
     */
    public static double[] min_max_fun(double []a){

        double minmax[] = new double[2];
        double minValue=a[0];
        double maxValue=a[1];
        for(int i=1;i<a.length;i++){
            if(a[i]<minValue)
                minValue=a[i];
            if(a[i]>maxValue)
                maxValue=a[i];
        }
        minmax[0]=minValue;
        minmax[1]=maxValue;
        return minmax;
    }
    /**
     * 规范化样本
     * @param pattern 样本
     * @param numpattern 样本数量
     * @param dimension  样本属性个数
     * @return
     */
    public static boolean Normalized(ArrayList<Vector> pattern,int numpattern,int dimension){
        double min_max[] = new double[2];
        double a[] = new double[pattern.size()];//提取列
        //先复制pattern到copypattern
        double copypattern[][] = new double[numpattern][dimension];
        for(int i=0;i<pattern.size();i++){
            for(int j=0;j<pattern.get(i).data.size();j++){
                copypattern[i][j]=pattern.get(i).data.get(j);
            }
        }

        for(int j=0;j<pattern.get(0).data.size();j++){
            for(int k=0;k<pattern.size();k++)
                a[k]=pattern.get(k).data.get(j);
            for(int i=0;i<pattern.size();i++){
                min_max=min_max_fun(a);
                double minValue = min_max[0];
                double maxValue = min_max[1];
                //pattern[i][j]=(pattern[i][j]-minValue)/(maxValue-minValue)是错误写法
                pattern.get(i).data.set(j,(copypattern[i][j]-minValue)/(maxValue-minValue));
            }
        }

        return true;
    }

    /**
     * 求矩阵第j列之和操作--用于规范化样本
     * @param array
     * @param j
     * @return
     */
    public static double sumArray(double array[][],int j){
        double sum=0;
        for(int i=0;i<array.length;i++){
            sum+=array[i][j];
        }
        return sum;
    }

    /**
     * 本函数完成Fcm_myself聚类算法
     * @param pattern 为样本点向量  指标为：样本指标*维数=维数指标
     * @param dimension 每个样本的维数
     * @param numpattern 样本的个数
     * @param cata 分类的数目
     * @param m Fcm_myself的重要模糊控制参数m
     * @param maxcycle 最大循环次数
     * @param limit 算法结束迭代的阈值1e-6
     * @param umatrix 输出的划分矩阵   c*numpattern
     * @param rescenter 输出的样本的聚类中心矩阵    cata*dimension
     * @param result 目标函数的值
     */
    public boolean Fcm_myself_fun(ArrayList<Vector> pattern, int dimension, int numpattern, int cata, double m,
                                  int maxcycle, double limit, double[][] umatrix, double[][] rescenter, double result) {
        //验证输入参数的有效性
        if (cata >= numpattern || m <= 1)
            return false;
        //规格化样本--蠢哭了
        //Normalized(pattern,numpattern,dimension);

        int dai =0,testflag=0;//迭代次数，迭代标志

        //初始化隶属度
        double temp[][] = new double[cata][numpattern];
        for(int i=0;i<umatrix.length;i++){
            for(int j=0;j<umatrix[i].length;j++){
                umatrix[i][j] = Math.random();
                temp[i][j] = umatrix[i][j];
            }
        }

        for(int i=0;i<umatrix.length;i++)
            for(int j=0;j<umatrix[i].length;j++){
                umatrix[i][j] = temp[i][j]/sumArray(temp,j);
            }

        double [][] umatrix_temp = new double[cata][numpattern];
        while(testflag==0){

            //每次保存更新前的隶属度

            for(int i=0;i<umatrix_temp.length;i++){
                for(int j=0;j<umatrix_temp[i].length;j++){
                    umatrix_temp[i][j]=umatrix[i][j];
                }
            }

            //更新聚类中心
            for(int t=0;t<cata;t++){
                for(int i=0;i<dimension;i++){
                    double a=0,b=0;
                    for(int k=0;k<numpattern;k++){
                        a += Math.pow(umatrix[t][k],m)*pattern.get(k).data.get(i);
                        //	System.out.println(a);
                        b += Math.pow(umatrix[t][k],m);
                    }
                    rescenter[t][i] = a/b;
			/*		if(t==0&&i==2){
						System.out.println(a);
						System.out.println(b);
						System.out.println(rescenter[t][i]);

					}*/
                }
            }

            //更新隶属度
            double c=0,d=0;
            for(int t=0;t<cata;t++){
                for(int k=0;k<numpattern;k++){
                    double e=0;
                    for(int j=0;j<cata;j++){
                        for(int i=0;i<dimension;i++){
                            c+=Math.pow(pattern.get(k).data.get(i)-rescenter[t][i],2);
                            d+=Math.pow(pattern.get(k).data.get(i)-rescenter[j][i],2);
                        }
                        e+=c/d;
                        c=0;d=0;
                    }
                    umatrix[t][k]=1/e;
                }

            }

            //判断是否收敛或达到最大迭代次数
            double cha[][] = new double[cata][numpattern];
            for(int i=0;i<umatrix_temp.length;i++)
                for(int j=0;j<umatrix_temp[i].length;j++){
                    cha[i][j]=Math.abs(umatrix_temp[i][j]-umatrix[i][j]);
                }

            double f=0;
            for(int i=0;i<cata;i++){
                for(int j=0;j<numpattern;j++){
                    if(cha[i][j]>f)
                        f=cha[i][j];//f矩阵中最大值
                }
            }

            if(f<=1e-6||dai>maxcycle)
                testflag=1;

            dai=dai+1;

            result = objectfun(umatrix, rescenter, pattern, cata, numpattern, dimension,m);
//            System.out.println(result+" ");//控制台输出目标函数值
        }
        return true;
    }


    /**
     * 计算目标函数值
     * @param u
     * @param v
     * @param x
     * @param c
     * @param dimension
     * @param m
     * @return
     */
    public double objectfun(double u[][],double v[][], ArrayList<Vector> x,int c,int numpattern,int dimension,double m) {
        //此函数计算优化的目标函数
        int i,j,k;
        double t=0,objectValue;
        double J_temp[][] = new double[numpattern][c];
        for(i=0;i<cata;i++)
            for(j=0;j<numpattern;j++){
                J_temp[j][i]=0;
            }

        objectValue = 0;
        for(i = 0; i < c; i++) {
            for(k = 0; k < numpattern; k++) {

                for(j = 0; j < dimension; j++) {
                    t+=Math.pow(x.get(k).data.get(i)-v[i][j],2);
                }
                J_temp[k][i]+=Math.pow(u[i][k],m)*t;
                t=0;
            }
        }

        for(i=0;i<cata;i++){
            for(j=0;j<numpattern;j++){
                objectValue +=J_temp[j][i];
            }
        }

        return objectValue;
    }

    /**
     * 运行Fcm_myself算法
     *
     */
    public void runFcm_myself() {

        double[][] pattern = new double[numpattern][dimension];
        double[][] umatrix = new double[cata][numpattern];
        double[][] rescenter = new double[cata][dimension];
        double result=0;

        //获取样本
//        getPattern(pattern);
        //执行Fcm_myself_fun
        Fcm_myself_fun(cityVecs, dimension, numpattern, cata, m, maxcycle, limit, umatrix, rescenter, result);

        //输出结果
        Export(umatrix, rescenter);
    }

    /**
     * 输出隶属度矩阵和聚类的中心
     * @param umatrix
     * @param rescenter
     */
    public void Export(double[][] umatrix, double[][] rescenter) {
        String str = null;
        String tab = "	";
        //矩阵转置，便于在txt中显示
        double[][] new_umatrix = new double[numpattern][cata];
        for(int i=0;i<new_umatrix.length;i++){
            for(int j=0;j<new_umatrix[i].length;j++){
                new_umatrix[i][j] = umatrix[j][i];
            }
        }
        //输出隶属度矩阵
        try {
            FileWriter matrixFileWriter = new FileWriter(FILE_MATRIX);

            for (int i = 0; i < numpattern; i++) {
                str = "";
//                System.out.println(cityVecs.get(i).city);

                double[] u= new double[cata];
                for (int j = 0; j < cata; j++) {
                    str += new_umatrix[i][j] + tab;
                    u[j]=new_umatrix[i][j];
                }
                str += "\n";
                matrixFileWriter.write(str);
                clusters.get(maxIndex(u)).vectors.add(cityVecs.get(i));
            }

            matrixFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //输出聚类的中心
        try {
            FileWriter centerFileWriter = new FileWriter(FILE_CENTER);

            for (int i = 0; i < cata; i++) {
                ArrayList<Double> centroid = new ArrayList<>();
                str = "";
                for (int j = 0; j < dimension; j++) {
                    str += rescenter[i][j] + tab;
                    centroid.add(rescenter[i][j]);
                    clusters.get(i).centroid.add( rescenter[i][j]);
                }
                str += "\n";
                centerFileWriter.write(str);
                centroids.add(centroid);
            }

            centerFileWriter.close();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }

    }

    public ArrayList<ArrayList<Double>> getCentroids(){
        return this.centroids;
    }

    public ArrayList<Cluster> getClusters() {
        return this.clusters;
    }
    private int maxIndex(double[] a){

        int index=0;
        double max =Double.MIN_VALUE;

        for(int i=0; i<a.length;i++){
            if(max<a[i]){
                index =i;
                max =a[i];
            }
        }
        return index;
    }

    public ArrayList<Double> getSSE(){

        ArrayList<Double> SSEs= new ArrayList<>();

        for(Cluster c: clusters){

            SSEs.add(calculateSSE(c));

        }
        return  SSEs;

    }

    private double calculateSSE(Cluster c) {
        double sse;
        double sum=0.0;

        for(Vector cityVector: c.vectors){
            sum+=squareSum(cityVector.data, c.centroid);
        }

        sse= sum/(double)c.vectors.size();
        return sse;
    }

    private  double squareSum(ArrayList<Double> a, ArrayList<Double> b) {
        double sum = 0;
        for (int i = 0; i < a.size(); i++) {
            sum += (a.get(i) - b.get(i)) * (a.get(i) - b.get(i));
        }
        return sum;
    }

    /**
     * 主函数
     * @param args
     */
//    public static void main(String[] args) {
//        FCM Fcm_myself = new FCM();
//        Fcm_myself.runFcm_myself();
//    }


}