
import java.util.ArrayList;
import java.util.Arrays;

public class TestDecisionTree {

    public static void main(String[] args) {

        // local directory
        String localDir = System.getProperty("user.dir");
        //You might need to change these based on your operating system.
        String base = localDir + "/data/";

        //You might need to change these based on your operating system.
        String basedb = localDir + "/data/db/";

        boolean verbose = true ;

        try {

            DataReader dr = new DataReader();
            try {
                dr.read_data(basedb + "data_minimal_overlap.csv");
            } catch (Exception e) {
                e.printStackTrace();
                throw new AssertionError("[ERROR] Could not read csv file.");
            }
            // split the data into training and testing
            dr.splitTrainTestData(.5);

            // build a decision tree based on the data
            DecisionTree dt = DataReader.readSerializedTree(base + "data_minimal_overlap/thresh1.ser");
            if (dt == null) {
                throw new AssertionError("[ERROR] Could not read DT from file.");
            }


            DecisionTree.DTNode curNode = dt.rootDTNode;
            //For partial
            System.out.println("Root");
            System.out.println("Leaf: " + curNode.leaf + " Attribute: " + curNode.attribute + " Threshold: " + curNode.threshold);

             curNode = curNode.left;

            System.out.println("Root.left");
            System.out.println("Leaf: " + curNode.leaf + " Label: " + curNode.label);
            System.out.println("Leaf: " + curNode.leaf + " Attribute: " + curNode.attribute + " Threshold: " + curNode.threshold);

            curNode = curNode.left;

            System.out.println("Root.left.left");
            System.out.println("Leaf: " + curNode.leaf + " Label: " + curNode.label);
            System.out.println("Leaf: " + curNode.leaf + " Attribute: " + curNode.attribute + " Threshold: " + curNode.threshold);

            // curNode = dt.rootDTNode.left.right;
            // System.out.println("Root.left.right");
            // // System.out.println("Leaf: " + curNode.leaf + " Label: " + curNode.label);
            // System.out.println("Leaf: " + curNode.leaf + " Attribute: " + curNode.attribute + " Threshold: " + curNode.threshold);



            //CHECKS FIRST THRESHOLD For MINIMAL
            // System.out.println("Root");
            // System.out.println("Leaf: " + curNode.leaf + " Attribute: " + curNode.attribute + " Threshold: " + curNode.threshold);

            // curNode = curNode.left;

            // System.out.println("Root.left");
            // System.out.println("Leaf: " + curNode.leaf + " Label: " + curNode.label);
            // //System.out.println("Leaf: " + curNode.leaf + " Attribute: " + curNode.attribute + " Threshold: " + curNode.threshold);

            // // curNode = curNode.left;

            // // System.out.println("Root.left.left");
            // // System.out.println("Leaf: " + curNode.leaf + " Attribute: " + curNode.attribute + " Threshold: " + curNode.threshold);

            // curNode = dt.rootDTNode.right;

            // System.out.println("Root.right");
            // System.out.println("Leaf: " + curNode.leaf + " Attribute: " + curNode.attribute + " Threshold: " + curNode.threshold);

            // curNode = curNode.right;

            // System.out.println("Root.right.right");
            // System.out.println("Leaf: " + curNode.leaf + " Label: " + curNode.label);

            // curNode = dt.rootDTNode.right.left;

            // System.out.println("Root.right.left");
            // System.out.println("Leaf: " + curNode.leaf + " Attribute: " + curNode.attribute + " Threshold: " + curNode.threshold);

            // curNode = curNode.left;
            // System.out.println("Root.right.left.left");
            // System.out.println("Leaf: " + curNode.leaf + " Label: " + curNode.label);

            //Using check-performance
            int limit = 0;
            String trainText, testText;
            System.out.println("\n\nCheck performance high overlap");
            for (int i = 0; limit < 128; i++) {
                limit = (int) Math.pow(2, i);

                dr = new DataReader();
                try {
                    dr.read_data(basedb + "data_high_overlap.csv");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AssertionError("[ERROR] Could not read csv file.");
                }
                // split the data into training and testing
                dr.splitTrainTestData(.5);

                // build a decision tree based on the data
                dt = DataReader.readSerializedTree(base + "data_high_overlap/thresh" + limit + ".ser");
                if (dt == null) {
                    throw new AssertionError("[ERROR] Could not read DT from file.");
                }

                trainText = dt.checkPerformance(dr.trainData);
                testText = dt.checkPerformance(dr.testData);
                System.out.println("minSizeDatalist: " + limit +
                        "\tTraining error: " + trainText +
                        "\t Test error: " + testText);
            }

            limit = 0;
            System.out.println("\n\nCheck performance partial overlap");
            for (int i = 0; limit < 128; i++) {
                limit = (int) Math.pow(2, i);

                dr = new DataReader();
                try {
                    dr.read_data(basedb + "data_partial_overlap.csv");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AssertionError("[ERROR] Could not read csv file.");
                }
                // split the data into training and testing
                dr.splitTrainTestData(.5);

                // build a decision tree based on the data
                dt = DataReader.readSerializedTree(base + "data_partial_overlap/thresh" + limit + ".ser");
                if (dt == null) {
                    throw new AssertionError("[ERROR] Could not read DT from file.");
                }

                trainText = dt.checkPerformance(dr.trainData);
                testText = dt.checkPerformance(dr.testData);
                System.out.println("minSizeDatalist: " + limit +
                        "\tTraining error: " + trainText +
                        "\t Test error: " + testText);
            }

            limit = 0;
            System.out.println("\n\nCheck performance Minimal overlap");
            for (int i = 0; limit < 128; i++) {
                limit = (int) Math.pow(2, i);

                dr = new DataReader();
                try {
                    dr.read_data(basedb + "data_minimal_overlap.csv");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AssertionError("[ERROR] Could not read csv file.");
                }
                // split the data into training and testing
                dr.splitTrainTestData(.5);

                // build a decision tree based on the data
                dt = DataReader.readSerializedTree(base + "data_minimal_overlap/thresh" + limit + ".ser");
                if (dt == null) {
                    throw new AssertionError("[ERROR] Could not read DT from file.");
                }

                trainText = dt.checkPerformance(dr.trainData);
                testText = dt.checkPerformance(dr.testData);
                System.out.println("minSizeDatalist: " + limit +
                        "\tTraining error: " + trainText +
                        "\t Test error: " + testText);

            }



            // Datum newDatum;
            // ArrayList<Datum> data = new ArrayList<Datum>();
            // double[] x = new double[2];
            // x[0] = x[1] = 0;

            // newDatum = new Datum(x, 0);
            // data.add(newDatum);

            // x = new double[2];
            // x[0] = 3; x[1] = 1;
            // newDatum = new Datum(x, 0);
            // data.add(newDatum);

            // x = new double[2];
            // x[0] = 1; x[1] = 0;
            // newDatum = new Datum(x, 1);
            // data.add(newDatum);

            // x = new double[2];
            // x[0] = 2; x[1] = 1;
            // newDatum = new Datum(x, 1);
            // data.add(newDatum);

            // // for (Datum d1:data) {
            // //     System.out.println(d1);
            // // }

            // DecisionTree d = new DecisionTree(data, 1);
            // curNode = d.rootDTNode;

            // System.out.println("Root");
            // System.out.println(curNode);

            // curNode = curNode.left;
            // System.out.println("Root.left");
            // System.out.println(curNode);

            // curNode = d.rootDTNode.left.left;
            // System.out.println("Root.left.left");
            // System.out.println(curNode);

            // curNode = d.rootDTNode.left.right;
            // System.out.println("Root.left.right");
            // System.out.println(curNode);

            // curNode = d.rootDTNode.right;
            // System.out.println("Root.right");
            // System.out.println(curNode);


        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}