import java.text.DecimalFormat;
import java.util.ArrayList;

public class TestDecisionTree_2 {

    public static final boolean verbose = false,
            printFirstWrong = false;

    public static void main(String[] args) {
        //this may work but I'm not willing to try this on windows, change div manually to \\ in windows if needed
        final char div = System.getProperty("os.name").toLowerCase().startsWith("window")?'\\':'/';//windows/mac/linux compatibility
        // local directory
        String localDir = System.getProperty("user.dir") + div + "src" + div + "data" + div;

        try {
            DataReader dr;
            DecisionTree dt, DT;
            String[] vals = new String[4];
            String characteristics = "";
            boolean one, two;
            for (String filename : new String[]{"data_high_overlap", "data_minimal_overlap", "data_partial_overlap"}){
                System.out.println("Checking " + filename.split("_")[1] + " overlap");
                for (int i = 1; i <= 128; i *= 2) {//for each threshold inside
                    dr = new DataReader();
                    dr.read_data(localDir + "db" + div + filename + ".csv");
                    dr.splitTrainTestData(.5);// split the datalist into trainData and testData
                    // build a decision tree based on the data
                    dt = DataReader.readSerializedTree(localDir + filename + div + "thresh" + i + ".ser");
                    if (dt == null) throw new AssertionError("[ERROR] Could not read DT from file.");
                    vals[0] = dt.checkPerformance(dr.trainData);//training error
                    vals[1] = dt.checkPerformance(dr.testData);//test error
                    //now we test our own
                    DT = new DecisionTree(dr.trainData, i);//root node is filled in
                    vals[2] = DT.checkPerformance(dr.trainData);//training error
                    vals[3] = DT.checkPerformance(dr.testData);//test error

                    one = !DecisionTree.equals(DT, dt);//not equals
                    two = !vals[2].equals(vals[0]) || !vals[3].equals(vals[1]);//difference performances

                    if(one || two) characteristics = "\t\t\tkey of " + getCharacteristics(dt) + "\n\tbut fillDTNode " + getCharacteristics(DT);
                    if(verbose) {
                        System.out.print("minSizeDatalist: " + i +
                                "\tTraining error: " + vals[0] +
                                "\t Test error: " + vals[1]);
                        if(two) System.out.print(" NO MATCH");
                        if(one) System.out.print(" NOT EQUALS ");
                        System.out.println();
                        System.out.print(
                                "My DecisionTree: " +
                                        "\tTraining error: " + vals[2] +
                                        "\t Test error: " + vals[3]);
                        if(two) System.out.print(" NO MATCH");//no need for space bc if no match then not equals
                        if(one) {
                            System.out.println(" NOT EQUALS ");
                            System.out.println(characteristics);
                        }
                        System.out.println();
                    } else if (one || two){// two implies one so || is moot but still
                        System.out.println();
                        if(two) System.out.print("NO MATCH, ");
                        System.out.println("NOT EQUALS (" + filename + " thresh " + i + ")");//NOTE: it may somehow be possible for the tress to be equal but not produce matching values, in which case you should probably give up now
                        System.out.println("\tminSizeDatalist: " + i + "\tTraining error: " + vals[0] + "\t Test error: " + vals[1]);
                        System.out.println("\tMy DecisionTree: " + "\tTraining error: " + vals[2] + "\t Test error: " + vals[3]);
                        System.out.println(characteristics);
                    }
                    if(printFirstWrong && one) {
                        printCompare(dt, DT, true);
                        return;
                    }
                }
                System.out.println("-------------------------------------------------");
            }
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static String getCharacteristics(DecisionTree input){//returns #leafs, #internal, height
        int[] tbr = new int[3];
        tbr[2] = getHeight(input.rootDTNode, tbr);
        return tbr[0] + " leaf, " + tbr[1] + " internal, " + tbr[2] + " root height.";
    }

    private static int getHeight(DecisionTree.DTNode input, int[] counter){
        if(input.leaf) {
            counter[0]++;
            return 0;
        }//else
        counter[1]++;
        return 1 + Math.max(getHeight(input.left, counter), getHeight(input.right, counter));
    }

    private static ArrayList<String> treeStrings(DecisionTree input){
        DecimalFormat df = new DecimalFormat("0.000");
        ArrayList<String> holder = new ArrayList<>();
        addNodeToArr(input.rootDTNode, df, 0, holder);
        return holder;
    }

    private static void addNodeToArr(DecisionTree.DTNode input, DecimalFormat df, int depth, ArrayList<String> holder){
        String base = "";
        for(int i = 0; i < depth; i++) base += "\t";
        if(input.leaf) {
            holder.add(base + "Leaf : " + input.label);
            return;
        }
        holder.add(base + "Internal node: attribute " + input.attribute + " with threshold " + df.format(input.threshold));
        if(input.left != null) addNodeToArr(input.left, df, depth + 1, holder);
        if(input.right != null) addNodeToArr(input.right, df, depth + 1, holder);
    }

    public static void printCompare(DecisionTree dt, DecisionTree DT, boolean all){
        ArrayList<String> key = treeStrings(dt), you = treeStrings(DT);
        final int length = Math.min(key.size(), you.size());
        boolean match;
        for(int K = 0; K < length; K++){
            match = key.get(K).equals(you.get(K));
            if(!match) System.out.print("MISMATCH ");
            if(all || !match) System.out.println("key: \t" + key.get(K));
            if(!match) System.out.print("MISMATCH ");
            if(all || !match) System.out.println("you: \t" + you.get(K));
        }
        System.out.println((Math.max(key.size(), you.size()) - length) + " items cut off");
    }
}