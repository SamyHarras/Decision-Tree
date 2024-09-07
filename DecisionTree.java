import java.io.Serializable;
import java.util.ArrayList;
import java.text.*;
import java.lang.Math;

public class DecisionTree implements Serializable {

	DTNode rootDTNode;
	int minSizeDatalist;

	public static final long serialVersionUID = 343L;

	public DecisionTree(ArrayList<Datum> datalist , int min) {
		minSizeDatalist = min;
		rootDTNode = (new DTNode()).fillDTNode(datalist);
	}

	class DTNode implements Serializable{
		public static final long serialVersionUID = 438L;
		boolean leaf;
		int label = -1;
		int attribute;
		double threshold;

		DTNode left, right;

		DTNode() {
			leaf = true;
			threshold = Double.MAX_VALUE;
		}

		DTNode fillDTNode(ArrayList<Datum> datalist) {
			int myDatalistSize = datalist.size();
			if (myDatalistSize < minSizeDatalist) {
				return createLeafWithMajority(datalist);
			}

			boolean equal = true;
			int firstLabel = datalist.get(0).y;
			for (int i = 1; i < myDatalistSize; i++) {
				if (datalist.get(i).y != firstLabel) {
					equal = false;
					break;
				}
			}

			if (equal) {
				DTNode myLatestNode = new DTNode();
				myLatestNode.label = firstLabel;
				myLatestNode.leaf = true;
				return myLatestNode;
			}

			if (isPure(datalist)) {
				DTNode myLatestNode = new DTNode();
				myLatestNode.leaf = true;
				myLatestNode.threshold = datalist.get(0).y;
				return myLatestNode;
			}

			double bestAverageEntropy = Double.POSITIVE_INFINITY;
			int bestAttribute = -1;
			double bestThreshold = -1;

			for (int i = 0; i < datalist.get(0).x.length; i++) {
				for (Datum u : datalist) {
					double current = u.x[i];
					ArrayList<ArrayList<Datum>> splitData = split(datalist, i, current);
					ArrayList<Datum> leftList = splitData.get(1);
					ArrayList<Datum> rightList = splitData.get(0);

					if (leftList.isEmpty() || rightList.isEmpty()) {
						continue;
					}

					double averageEntropy = calculateAverageEntropy(leftList, rightList, myDatalistSize);

					if (averageEntropy < bestAverageEntropy) {
						bestAverageEntropy = averageEntropy;
						bestAttribute = i;
						bestThreshold = current;
					}
				}
			}

			if (bestAttribute == -1) {
				return createLeafWithMajority(datalist);
			}

			ArrayList<ArrayList<Datum>> finalSplit = split(datalist, bestAttribute, bestThreshold);
			DTNode newNode = new DTNode();
			newNode.attribute = bestAttribute;
			newNode.threshold = bestThreshold;
			newNode.leaf = false;

			newNode.left = fillDTNode(finalSplit.get(1));
			newNode.right = fillDTNode(finalSplit.get(0));

			return newNode;
		}

		private double calculateAverageEntropy(ArrayList<Datum> leftList, ArrayList<Datum> rightList, int totalSize) {
			return ((double) leftList.size() / totalSize) * calcEntropy(leftList) +
					((double) rightList.size() / totalSize) * calcEntropy(rightList);
		}

		private DTNode createLeafWithMajority(ArrayList<Datum> datalist) {
			DTNode leaf = new DTNode();
			leaf.label = findMajority(datalist);
			leaf.leaf = true;
			return leaf;
		}

		private ArrayList<ArrayList<Datum>> split(ArrayList<Datum> datalist, int index, double threshold ){
			ArrayList<Datum> leftList = new ArrayList<>();
			ArrayList<Datum> rightList = new ArrayList<>();

			for (Datum d : datalist){
				if (d.x[index] < threshold){
					leftList.add(d);
				}
				else {
					rightList.add(d);
				}
			}

			ArrayList<ArrayList<Datum>> temporaryVariable = new ArrayList<>();
			temporaryVariable.add(rightList);
			temporaryVariable.add(leftList);
			return temporaryVariable;
		}

		private boolean isPure(ArrayList<Datum> datalist) {
			int firstLabel = datalist.get(0).y;
			for (Datum datum : datalist) {
				if (datum.y != firstLabel) {
					return false;
				}
			}
			return true;
		}

		int findMajority(ArrayList<Datum> datalist) {

			int [] votes = new int[2];
			for (Datum data : datalist)
			{
				votes[data.y]+=1;
			}

			if (votes[0] >= votes[1])
				return 0;
			else
				return 1;
		}

		int classifyAtNode(double[] numbers) {
			if (this.leaf) {
				return this.label;
			} else {
				if (numbers[this.attribute] < this.threshold) {
					return this.left.classifyAtNode(numbers);
				} else {
					return this.right.classifyAtNode(numbers);
				}
			}
		}

		public boolean equals(Object dt2) {
			if (this == dt2) {
				return true;
			}
			if (dt2 == null || getClass() != dt2.getClass()) {
				return false;
			}
			DTNode other = (DTNode) dt2;

			if (this.leaf && other.leaf) {
				return this.label == other.label;
			} else if (!this.leaf && !other.leaf) {
				return this.attribute == other.attribute &&
						this.threshold == other.threshold &&
						(this.left != null ? this.left.equals(other.left) : other.left == null) &&
						(this.right != null ? this.right.equals(other.right) : other.right == null);
			}
			return false;
		}
	}

	double calcEntropy(ArrayList<Datum> datalist) {
		double entropy = 0;
		double px = 0;
		float [] counter= new float[2];
		if (datalist.size()==0)
			return 0;
		double num0 = 0.00000001,num1 = 0.000000001;

		//calculates the number of points belonging to each of the labels
		for (Datum d : datalist)
		{
			counter[d.y]+=1;
		}
		//calculates the entropy using the formula specified in the document
		for (int i = 0 ; i< counter.length ; i++)
		{
			if (counter[i]>0)
			{
				px = counter[i]/datalist.size();
				entropy -= (px*Math.log(px)/Math.log(2));
			}
		}

		return entropy;
	}

	int classify(double[] xQuery ) {
		return this.rootDTNode.classifyAtNode( xQuery );
	}

	String checkPerformance( ArrayList<Datum> datalist) {
		DecimalFormat df = new DecimalFormat("0.000");
		float total = datalist.size();
		float count = 0;

		for (int s = 0 ; s < datalist.size() ; s++) {
			double[] x = datalist.get(s).x;
			int result = datalist.get(s).y;
			if (classify(x) != result) {
				count = count + 1;
			}
		}

		return df.format((count/total));
	}

	public static boolean equals(DecisionTree dt1,  DecisionTree dt2)
	{
		boolean flag = true;
		flag = dt1.rootDTNode.equals(dt2.rootDTNode);
		return flag;
	}

}
