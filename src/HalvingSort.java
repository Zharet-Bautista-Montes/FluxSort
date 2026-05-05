import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

public class HalvingSort 
{
	//{'E', 'A', 'O', 'S', 'R', 'N', 'I', 'D', 'L', 'C', 'T', 'U', 'M', 'P', 'B', 'G', 'V', 'Y', 'Q', 'H', 'F', 'Z', 'J', 'X', 'W', 'K', '0', '5', '4', '2', '9', '8', '6', '7', '3', '1' }
	//{'C', 'A', 'P', 'M', 'E', 'T', 'B', 'S', 'R', 'D', 'L', 'V', 'G', 'F', 'I', 'H', 'O', 'N', 'J', 'Z', 'Q', 'U', 'Y', 'K', 'W', 'X', '9', '4', '1', '3', '0', '5', '7', '2', '6', '8' }
	//{'Z', 'Y', 'X', 'W', 'V', 'U', 'T', 'S', 'R', 'Q', 'P', 'O', 'N', 'M', 'L', 'K', 'J', 'I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A', '9', '8', '7', '6', '5', '4', '3', '2', '1', '0' }
	//{'Z', '0', 'Y', '1', 'X', '2', 'W', '3', 'V', '4', 'U', '5', 'T', '6', 'S', '7', 'R', '8', 'Q', '9', 'P', 'A', 'O', 'B', 'N', 'C', 'M', 'D', 'L', 'E', 'K', 'F', 'J', 'G', 'I', 'H' } Nightmare case
	public static char[] vector = { 'E', 'A', 'O', 'S', 'R', 'N', 'I', 'D', 'L', 'C', 'T', 'U', 'M', 'P', 'B', 'G', 'V', 'Y', 'Q', 'H', 'F', 'Z', 'J', 'X', 'W', 'K', '0', '5', '4', '2', '9', '8', '6', '7', '3', '1' }; 
	public static int[] testarray;
	public static int streams;
	public static int comparisons;
	public static int swaps;
	private static Scanner modesetter;

	public static void main(String[] args) 
	{
		HalvingSort hs = new HalvingSort();
		modesetter = new Scanner(System.in);
		boolean running = true;
		while(running)
		{
			System.out.println("Choose Mode:\n -C for detailed test with chars\n "
					+ "-I for massive tests with integers\n -E to exit the program ");
			String mode = modesetter.next();
			if(mode.equals("C"))
			{
				charMode(hs);
			}
			else if(mode.equals("I"))
			{
				integerMode(hs);
			}
			else if(mode.equals("E"))
			{
				running = false;
			}
			else System.out.println("Not valid option! \n");
		}
	}
	
	public static void charMode(HalvingSort fs)
	{
		streams = 0; comparisons = 0; swaps = 0;
		testarray = new String(vector).chars().toArray();
		printArray(testarray, "(Unsorted input)", true);
		long start = System.nanoTime();
		fs.halvingSort(testarray, true);
		long end = System.nanoTime();
		if(fs.isSorted(testarray.length)) printArray(testarray, "(Sorted output)", true);
		System.out.println("Comparisons performed: " + comparisons);
		System.out.println("Swaps performed: " + swaps);
		System.out.println("Needed streams: " + streams);
		System.out.println("Total execution time: " + (end - start) + " ns");
		System.out.println("----------------------------------------------");
	}
	
	public static void integerMode(HalvingSort fs)
	{
		ArrayList<Integer> comparlist = new ArrayList<Integer>();
		ArrayList<Integer> swaplist = new ArrayList<Integer>();
		ArrayList<Integer> timelist = new ArrayList<Integer>();
		System.out.println("Length\tCompars\tSwaps\tSorted\tTime");
		for(int x = 8; x <= 50000; x *= 2)
		{
			testarray = IntStream.generate(() -> new Random().nextInt(100)).limit(x).toArray();
			int[] refarray = testarray.clone();
			streams = 0; comparisons = 0; swaps = 0;
			long start = System.nanoTime();
			fs.halvingSort(testarray, false);
			long end = System.nanoTime();
			boolean validator = fs.isSorted(testarray.length-1);
			String sorted = validator == true ? "Yes" : "No";
			comparlist.add(comparisons);
			swaplist.add(swaps);
			timelist.add((int) (end - start));
			System.out.println(x + "\t" + comparisons + "\t " + swaps 
					+ "\t" + sorted + " \t"  + (end - start) + " ns");
			if (!validator) printArray(refarray, "WARNING!", false);
		}
		double comparlog = 0.0, swaplog = 0.0, timelog = 0.0;
		for(int r = 1; r < timelist.size(); r++)
		{
			comparlog += (Math.log10(comparlist.get(r)) - Math.log10(comparlist.get(r-1)))/Math.log10(2);
			swaplog += (Math.log10(swaplist.get(r)) - Math.log10(swaplist.get(r-1)))/Math.log10(2);
			timelog += (Math.log10(timelist.get(r)) - Math.log10(timelist.get(r-1)))/Math.log10(2);
		}
		System.out.println((comparlog/(timelist.size()-1)) + " Logarithmic comparisons");
		System.out.println((swaplog/(timelist.size()-1)) + " Logarithmic swaps");
		System.out.println((timelog/(timelist.size()-1)) + " Logarithmic time");
	}

	public void halvingSort(int[] arrayed, boolean verbose) 
	{
		halve(0, arrayed.length-1, verbose);
	}

	private boolean less(int w, int y)
	{
		comparisons++;
		return testarray[w] < testarray[y]; 
	}

	private void exch(int a, int z)
	{
		int temp = testarray[a]; 
		testarray[a] = testarray[z]; 
		testarray[z] = temp; 
		swaps++;
	}
	
	private void halve(int l, int r, boolean verbose)
	{
		int half = (int) (r-l)/2;
		if(half >= 1)
		{
			halve(l, l+half, verbose); streams++;
			if (verbose) printArray(testarray, "(Halved from " + l + " to " + (l+half) + ")", verbose);
			halve(r-half, r, verbose); streams++;
			if (verbose) printArray(testarray, "(Halved from " + (r-half) + " to " + r + ")", verbose);
			for (int n = half; n >= 0; n--)
				if(less(r-n, l+n))
					exch(l+n, r-n);
			streams++;
			if (verbose) printArray(testarray, "(Halved from " + l + " to " + r + ")", verbose);
			halve(l, l+half, verbose); streams++;
			if (verbose) printArray(testarray, "(Halved from " + l + " to " + (l+half) + ")", verbose);
			halve(r-half, r, verbose); streams++;
			if (verbose) printArray(testarray, "(Halved from " + (r-half) + " to " + r + ")", verbose);
		}
		else if(less(r, l)) exch(l, r);
	}
	
	private boolean isSorted(int arraylength)
	{
		for(int n=0; n<arraylength-1; n++)
			if(!less(n, n+1) && less(n+1, n))
				return false;
		return true;
	}

	private static void printArray(int[] arrayed, String msg, boolean cryptic)
	{
		for(int k=0; k<arrayed.length; k++)
		{
			String token = cryptic ? ((char) arrayed[k] + "") : (arrayed[k] + "");
			System.out.print(token + " ");
		}
		System.out.println(" " + msg);
	}
}