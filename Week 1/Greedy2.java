import java.util.Scanner;
import java.io.File;
import java.util.Comparator;
import java.util.Arrays;
public class Greedy2{

	static class WbLComparator implements Comparator<Record>{

		public int compare(Record one, Record two){
			if (one.WbL == two.WbL){
				if(one.weight < two.weight)
					return 1;
				else
					return -1;
			}else if(one.WbL < two.WbL){
				return 1;
			}else {
				return -1;
			}
		}
	}

	static class Record{
		int weight;
		int length;
		double WbL;

		public Record(int weight, int length){
			this.weight = weight;
			this.length = length;
			this.WbL = (double)weight/ (double)length;
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

		Arrays.sort(records, new WbLComparator());
		System.out.println("Weighted sum is :  " + weightedSum(records));
	}
}