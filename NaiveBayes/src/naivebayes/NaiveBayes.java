/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naivebayes;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
public class NaiveBayes {
static List<String[]> training = new ArrayList<>();
static List<String[]> testData1 = new ArrayList<>();
static List<String[]> testData2 = new ArrayList<>();
static List<String[]> testData3 = new ArrayList<>();
static List<String[]> testData4 = new ArrayList<>();
static List<Integer[]> probabilities = new ArrayList<>();



    public static void readIn() throws IOException{
        // Reads all Data into StringList
        List<String> lines = Files.readAllLines(Paths.get("C:/Users/Rubin1/Downloads/353data.tsv"), StandardCharsets.UTF_8);   
        //Algorithim to Randomly assign data into 5 seperate parts
        Random rand = new Random();
        int dataLeft = 46510;
        for(int i = 0; i < 9302; i++){
            int randPick = rand.nextInt(dataLeft); //Picks a value between 0 (inclusive) and dataLeft(exclusive)
            String[] toFill = lines.get(randPick).split("\\s+");
            training.add(toFill);
            lines.remove(randPick);
            dataLeft = dataLeft - 1;
        }
        for(int i = 0; i < 9302; i++){
            int randPick = rand.nextInt(dataLeft); //Picks a value between 0 (inclusive) and dataLeft(exclusive)
            String[] toFill = lines.get(randPick).split("\\s+");
            testData1.add(toFill);
            lines.remove(randPick);
            dataLeft = dataLeft - 1;
        }
        for(int i = 0; i < 9302; i++){
            int randPick = rand.nextInt(dataLeft); //Picks a value between 0 (inclusive) and dataLeft(exclusive)
            String[] toFill = lines.get(randPick).split("\\s+");
            testData2.add(toFill);
            lines.remove(randPick);
            dataLeft = dataLeft - 1;
        }
        for(int i = 0; i < 9302; i++){
            int randPick = rand.nextInt(dataLeft); //Picks a value between 0 (inclusive) and dataLeft(exclusive)
            String[] toFill = lines.get(randPick).split("\\s+");
            testData3.add(toFill);
            lines.remove(randPick);
            dataLeft = dataLeft - 1;
        }
        for(int i = 0; i < 9302; i++){
            int randPick = rand.nextInt(dataLeft); //Picks a value between 0 (inclusive) and dataLeft(exclusive)
            String[] toFill = lines.get(randPick).split("\\s+");
            testData4.add(toFill);
            lines.remove(randPick);
            dataLeft = dataLeft - 1;
        }
        
    }
    
    static List<Integer> yesAge = new ArrayList<>();
    static List<Integer> noAge = new ArrayList<>();
    static List<Integer> yesfnl = new ArrayList<>();
    static List<Integer> nofnl = new ArrayList<>();
    static List<Integer> yesEdNum = new ArrayList<>();
    static List<Integer> noEdNum = new ArrayList<>();
    static List<Integer> yesCapG = new ArrayList<>();
    static List<Integer> noCapG = new ArrayList<>();
    static List<Integer> yesCapL = new ArrayList<>();
    static List<Integer> noCapL = new ArrayList<>();
    static List<Integer> yesHPW = new ArrayList<>();
    static List<Integer> noHPW = new ArrayList<>();
    
    static List<double[]> yesList = new ArrayList<>();
    static List<double[]> noList = new ArrayList<>();
    
    public static void numVarHandler(){
        for(String[] check: training){
            if(check[14].equals(">50K")){
                yesAge.add(Integer.parseInt(check[0]));
                yesfnl.add(Integer.parseInt(check[2]));
                yesEdNum.add(Integer.parseInt(check[4]));
                yesCapG.add(Integer.parseInt(check[10]));
                yesCapL.add(Integer.parseInt(check[11]));
                yesHPW.add(Integer.parseInt(check[12]));
            }
            else{
                noAge.add(Integer.parseInt(check[0]));
                nofnl.add(Integer.parseInt(check[2]));
                noEdNum.add(Integer.parseInt(check[4]));
                noCapG.add(Integer.parseInt(check[10]));
                noCapL.add(Integer.parseInt(check[11]));
                noHPW.add(Integer.parseInt(check[12]));
            }
        }
        
        double[] a = new double[2];
        a = meanAndSd(yesAge);
        yesList.add(a);
        a = meanAndSd(yesfnl);
        yesList.add(a);
        a = meanAndSd(yesEdNum);
        yesList.add(a);
        a = meanAndSd(yesCapG);
        yesList.add(a);
        a = meanAndSd(yesCapL);
        yesList.add(a);
        a = meanAndSd(yesHPW);
        yesList.add(a);
        a = meanAndSd(noAge);
        noList.add(a);
        a = meanAndSd(nofnl);
        noList.add(a);
        a = meanAndSd(noEdNum);
        noList.add(a);
        a = meanAndSd(noCapG);
        noList.add(a);
        a = meanAndSd(noCapL);
        noList.add(a);
        a = meanAndSd(noHPW);
        noList.add(a);
                
    }
    
    public static double[] meanAndSd(List<Integer> con){
        double[] meanSD = new double[2];
        double x = con.size();
        double mean = 0;
        for(int i: con){
            mean+=i;
        }
        mean = mean/x;
        meanSD[0] = mean;   
        double SD = 0;
        double y = 0;
        for(int i: con){
            y = i - mean;
            y = y*y;
            SD += y;
        }
        SD = SD/(x-1);
        SD = Math.sqrt(SD);
        meanSD[1] = SD;
        return meanSD;
        
    }
    
    static int totalYes = 0;
    static int totalNo = 0;
    static double probYes = 0;
    static double probNo = 0;
   
    public static void probTotal(){
        for(String[] check: training){
            if(check[14].equals(">50K"))
                totalYes++;
            else
                totalNo++;
        }
        
        probYes = (double)totalYes/(totalYes + totalNo);
        probNo = (double)totalNo/(totalYes + totalNo);
       
    }
    public static double[] findProb(String str, int index){
        double yCounter = 0;
        double nCounter = 0;
        boolean found = false;
        for(String[] find: training){
            if(find[index].equals(str)){
                found = true;
                if(find[14].equals(">50K"))
                    yCounter++;
                else
                    nCounter++;
            }
        }
        double[] toReturn = new double[2];
        if(found == false)
            return toReturn;
        double a = yCounter/totalYes;
        toReturn[0] = a;
        a = nCounter/totalNo;
        toReturn[1] = a;
        a = toReturn[0];
        double b = toReturn[1];
        //Normalize
        toReturn[0] = a/(a+b);
        toReturn[1] = b/(a+b);
        return toReturn;
    }
    
    public static double normalDistProb(int a, double[] b){
        //Where a is the value, b[0] is the mean, b[1] is the SD
        double x = 2*Math.PI;
        x = Math.sqrt(x);
        x = x*b[1];
        x = 1/x;
        double y = (double)a - b[0];
        y = y*y;
        y*= -1;
        double z = 2*(b[1]*b[1]);
        y = y/z;
        y = Math.exp(y);
        return x*y;
    }
    static int counterCorrect = 0;
    static int counterIncorrect = 0;
    static int counterthree = 0;
    
    public static double test(List<String[]> toTestList){
        int[] contData = {0,2,4,10,11,12};        //Continuous Atrri in this DataSet: 0, 2, 4, 10, 11, 12
        int[] discreteData = {1,3,5,6,7,8,9,13}; //Non Continuous Attri : 1, 3, 5, 6 , 7 , 8 , 9 , 13
        
        for(String[] toTest: toTestList){
                double likelihoodYes = probYes;
                double likelihoodNo = probNo;
                
                for(int i : discreteData){
                    double a[] = findProb(toTest[i], i);
                    if(a[0] == 0.0)
                        continue;                        //FOR MISSING ATTRIBUTES! 
                    likelihoodYes*=a[0];
                    likelihoodNo*=a[1];
                }
                int j = 0;
                for(int i: contData){
                   double[] a = yesList.get(j);
                   double x = normalDistProb(Integer.parseInt(toTest[i]), a);
                    a = noList.get(j);
                   double y = normalDistProb(Integer.parseInt(toTest[i]), a);
                   double z = x/(x+y);
                          y = y/(x+y);
                          
                   likelihoodYes *= z;
                   likelihoodNo *= y;
                   j++;
                }
                
                String text;
                if(likelihoodYes>likelihoodNo){
                    text = ">50K";
                    counterthree++;
                }
                else
                    text = "<=50K";
                
                double temp = likelihoodYes/(likelihoodYes + likelihoodNo);
                double temp2 = likelihoodNo/(likelihoodYes+likelihoodNo);
                //for(int i = 0; i < 15; i++)
                //System.out.print(toTest[i] + " ");
                //System.out.println("Prediction -> " + text);
                if(text.equals(toTest[14]))
                    counterCorrect++;
                else
                    counterIncorrect++;
                
                for(int z = 0; z < 15; z++)
                    System.out.print(toTest[z] + " ");
                System.out.println("PREDIC: -- > " + text);
        }
        System.out.println();
        double acc = (double)counterCorrect/(counterCorrect+counterIncorrect);
        return acc;
    }
    
    public static void main(String[] args) throws IOException {
        readIn();
        numVarHandler();
        probTotal();
        System.out.println("Accuracy for fold 1 = " + test(testData1));
        System.out.println("Accuracy for fold 2 = " +test(testData2));
        System.out.println("Accuracy for fold 3 = " +test(testData3));
        System.out.println("Accuracy for fold 4 = " +test(testData4));
    }
    
}