import java.util.Scanner;
import java.io.File;
import java.util.Comparator;
import java.util.Arrays;
public class Greedy1{

	static class WmLComparator implements Comparator<Record>{

		public int compare(Record one, Record two){
			if (one.WmL == two.WmL){
				if(one.weight < two.weight)
					return 1;
				else
					return -1;
			}else if(one.WmL < two.WmL){
				return 1;
			}else {
				return -1;
			}
		}
	}

	static class Record{
		int weight;
		int length;
		int WmL;

		public Record(int weight, int length){
			this.weight = weight;
			this.length = length;
			this.WmL = weight - length;
		}
	}

	public static long weightedSum(Record[] records){
		int len = 0;
		long total = 0;
		for(Record r : records){
			len += r.length;
			total += len*r.weight;
		}
		return total;
	}

	public static void main(String[] args)throws Exception {
		Scanner in = new Scanner (new File("/home/mayur/input.txt"));

		int numberOfJobs = in.nextInt();
		Record[] records = new Record[numberOfJobs];

		for(int i=0; in.hasNext(); i++){
			records[i] = new Record(in.nextInt(), in.nextInt());
		}

		Arrays.sort(records, new WmLComparator());
		System.out.println("Weighted sum is :  " + weightedSum(records));
	}
}