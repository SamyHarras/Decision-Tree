import java.util.ArrayList;

public class DTVisualizer {
    private DecisionTree.DTNode root;

    public DTVisualizer(DecisionTree.DTNode root) {
        this.root = root;
    }

    public String toString() {
        final int childrenSpaceRoot = 31; //Make this higher if nodes don't have enough space
        final int childrenSpace = 3;
        final int maxNodeLength = 13; //Assuming 6 characters max for attribute and threshold
        // final int textWidth = maxNodeLength * (int) Math.pow(2, (nbNodes - 1)/2);
        final int textWidth = 200; //Make this smaller if display is too small


        String text = "";
        int nbNodes = 0;
        ArrayList<DecisionTree.DTNode> nodesToVisit = new ArrayList<DecisionTree.DTNode>();
        ArrayList<DecisionTree.DTNode> temp = new ArrayList<DecisionTree.DTNode>();

        nodesToVisit.add(root);

        //Counts the nodes
        while (!nodesToVisit.isEmpty()) {
            temp = new ArrayList<DecisionTree.DTNode>();
            //Adds nodes in order of height starting from the root
            for (DecisionTree.DTNode node:nodesToVisit) {
                nbNodes++;

                //Decision tree nodes must either have 0 or 2 children never 1
                if (node.left != null) {
                    temp.add(node.left );
                    temp.add(node.right);
                }
            }
            nodesToVisit = temp;
        }

        //Maximum tree height = (total nodes - 1) / 2, Maximum tree width = 2^height
        //Text format: hat symbol below the comma, with 3 spaces between children data
        /*              1.0,2.0
         *                 ^
         *         3.0,4.0   5.0,6.0
         *            ^         ^
         *          0   1     1   1
         */


        ArrayList<Integer> hatPos = new ArrayList<Integer>();
        ArrayList<Integer> temp1 = new ArrayList<Integer>();
        String t1, t2;
        int i = 0;
        int j = 0;
        String row;
        int center = 0; //Comma position
        boolean rootChildren = true;

        t1 = t2 = "";

        nodesToVisit.clear();
        nodesToVisit.add(root);

        while (!nodesToVisit.isEmpty()) {
            temp = new ArrayList<DecisionTree.DTNode>();
            temp1 = new ArrayList<Integer>();
            i = 0;
            row = "";
            //Adds nodes in order of height starting from the root
            for (DecisionTree.DTNode node:nodesToVisit) {
                j = i / 2;
                if (!node.leaf) {
                    t1 = String.valueOf(node.attribute);
                    t2 = String.valueOf(node.threshold);
                } else {
                    t1 = String.valueOf(node.label);
                }

                if (node == root) {
                    center = textWidth / 2;
                    //Centers the commas to the width
                    if (!node.leaf) {
                        row += spaces(center - t1.length()) + t1 + "," + t2;
                    } else {
                        row += spaces(center - t1.length() / 2) + t1;
                    }

                } else {
                    if (rootChildren) {

                        if (!node.leaf) {
                            //If we're positioning the left child
                            if (i % 2 == 0) {
                                center = hatPos.get(j) - t2.length() - 1 - childrenSpaceRoot/2;

                                row += spaces(center - row.length() - t1.length()) +
                                        t1 + "," + t2;
                            } else {
                                center = hatPos.get(j) + t1.length() + 1 + childrenSpaceRoot/2;

                                row += spaces(childrenSpaceRoot) + t1 + "," + t2;
                            }

                        } else {
                            if (i % 2 == 0) {
                                center = hatPos.get(j) - t1.length()/2 + 1 - childrenSpaceRoot/2;

                                row += spaces(hatPos.get(j) - row.length() - childrenSpaceRoot/2 - t1.length())
                                        + t1;
                            } else {
                                center = hatPos.get(j) + t1.length()/2 + 1 + childrenSpaceRoot/2;

                                row += spaces(childrenSpaceRoot) + t1;
                            }

                        }

                    } else {
                        if (!node.leaf) {
                            //If we're positioning the left child
                            if (i % 2 == 0) {
                                center = hatPos.get(j) - t2.length() - 1 - childrenSpace/2;

                                row += spaces(center - row.length() - t1.length()) +
                                        t1 + "," + t2;
                            } else {
                                center = hatPos.get(j) + t1.length() + 1 + childrenSpace/2;

                                row += spaces(childrenSpace) + t1 + "," + t2;
                            }

                        } else {
                            if (i % 2 == 0) {
                                center = hatPos.get(j) - t1.length()/2 + 1 - childrenSpace/2;

                                row += spaces(hatPos.get(j) - row.length() - childrenSpace/2 - t1.length())
                                        + t1;
                            } else {
                                center = hatPos.get(j) + t1.length()/2 + 1 + childrenSpace/2;

                                row += spaces(childrenSpace) + t1;
                            }

                        }
                    }
                }

                //Decision tree nodes must either have 0 or 2 children never 1
                if (node.left != null) {
                    temp.add(node.left );
                    temp.add(node.right);
                    temp1.add(center);

                }
                i++;
            }
            text += row + "\n";

            nodesToVisit = temp;
            hatPos = temp1;
            if (!nodesToVisit.isEmpty() && nodesToVisit.get(0) != root.left) rootChildren = false;
            row = "";
            //Places the hat symbol
            for (int pos:hatPos) {
                row += spaces(pos - row.length()) + "^";
            }
            text += row + "\n";

        }

        return text;

    }

    private String spaces(int n) {
        final String space = " ";
        String text = "";
        for (int i = 0; i < n; i++) {
            text += space;
        }
        return text;
    }


}
