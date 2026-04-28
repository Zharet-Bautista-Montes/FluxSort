import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

public class FluxSort 
{
	//{'E', 'A', 'O', 'S', 'R', 'N', 'I', 'D', 'L', 'C', 'T', 'U', 'M', 'P', 'B', 'G', 'V', 'Y', 'Q', 'H', 'F', 'Z', 'J', 'X', 'W', 'K', '0', '5', '4', '2', '9', '8', '6', '7', '3', '1' }
	//{'C', 'A', 'P', 'M', 'E', 'T', 'B', 'S', 'R', 'D', 'L', 'V', 'G', 'F', 'I', 'H', 'O', 'N', 'J', 'Z', 'Q', 'U', 'Y', 'K', 'W', 'X', '9', '4', '1', '3', '0', '5', '7', '2', '6', '8' }
	//{'Z', 'Y', 'X', 'W', 'V', 'U', 'T', 'S', 'R', 'Q', 'P', 'O', 'N', 'M', 'L', 'K', 'J', 'I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A', '9', '8', '7', '6', '5', '4', '3', '2', '1', '0' }
	//{'Z', '0', 'Y', '1', 'X', '2', 'W', '3', 'V', '4', 'U', '5', 'T', '6', 'S', '7', 'R', '8', 'Q', '9', 'P', 'A', 'O', 'B', 'N', 'C', 'M', 'D', 'L', 'E', 'K', 'F', 'J', 'G', 'I', 'H' } Nightmare case
	public static char[] vector = { 48, 86, 80, 30, 51, 42, 56, 93, 6, 23, 90, 79, 17, 67, 45, 96, 20, 56, 64, 61, 88, 0, 76, 60, 95, 93, 41, 18, 22, 69, 30, 60, 18, 64, 53, 67, 67, 97, 23, 41, 29, 80, 66, 39, 70, 48, 91, 24, 32, 12, 37, 47, 15, 13, 36, 95, 23, 41, 28, 4, 86, 95, 39, 63 }; 
	public static int[] testarray;
	public static int streams;
	public static int comparisons;
	public static int swaps;
	private static Scanner modesetter;

	public static void main(String[] args) 
	{
		FluxSort nfs = new FluxSort();
		modesetter = new Scanner(System.in);
		boolean running = true;
		while(running)
		{
			System.out.println("Choose Mode:\n -C for detailed test with chars\n "
					+ "-I for massive tests with integers\n -E to exit the program ");
			String mode = modesetter.next();
			if(mode.equals("C"))
			{
				charMode(nfs);
			}
			else if(mode.equals("I"))
			{
				integerMode(nfs);
			}
			else if(mode.equals("E"))
			{
				running = false;
			}
			else System.out.println("Not valid option! \n");
		}
	}
	
	public static void charMode(FluxSort fs)
	{
		streams = 0; comparisons = 0; swaps = 0;
		testarray = new String(vector).chars().toArray();
		printArray(testarray, "(Unsorted input)", true);
		long start = System.nanoTime();
		fs.fluxSort(testarray, true);
		long end = System.nanoTime();
		if(fs.isSorted(testarray.length)) printArray(testarray, "(Sorted output)", true);
		System.out.println("Comparisons performed: " + comparisons);
		System.out.println("Swaps performed: " + swaps);
		System.out.println("Needed streams: " + streams);
		System.out.println("Total execution time: " + (end - start) + " ns");
		System.out.println("----------------------------------------------");
	}
	
	public static void integerMode(FluxSort fs)
	{
		System.out.println("Length\tCompars\tSwaps\tSorted\tTime");
		for(int x = 1; x <= 1000; x++)
		{
			testarray = IntStream.generate(() -> new Random().nextInt(100)).limit(x).toArray();
			int[] refarray = testarray.clone();
			streams = 0; comparisons = 0; swaps = 0;
			long start = System.nanoTime();
			fs.fluxSort(testarray, false);
			long end = System.nanoTime();
			boolean validator = fs.isSorted(testarray.length-1);
			String sorted = validator == true ? "Yes" : "No";
			System.out.println(x + "\t" + comparisons + "\t " + swaps 
					+ "\t" + sorted + " \t"  + (end - start) + " ns");
			if (!validator) printArray(refarray, "WARNING!", false);
		}
	}

	public void fluxSort(int[] arrayed, boolean verbose) 
	{
		boolean locked; 
		int tht = -1, aux = 0, top = 0, btm = arrayed.length-1;
		while(true)
		{
			//Reverse every contrary flux found upstream
			locked = false; int i = top; tht = -1;
			while(i <= btm)
			{
				if(tht != -1) //Marked
				{          
					if(i == btm || less(i, i+1))
					{
						locked = true; 
						if(i-tht == 1) exch(i, tht); 
						else reverse(tht, i); 
						aux = i; tht = -1; 
						if(i == btm) i++;
					}
					else i++;
				}
				else
				{ if(i != btm && less(i+1, i)) { tht = i; } i++; }
			}
			btm = aux; streams++; 
			if (verbose) printArray(arrayed, "(Reverse upstream)", false);
			if (!locked || btm-top <= 1) return;
			
			//Overlap every parallel fluxes found upstream
			int j = top; locked = false;
			while(j < btm)
			{
				if(tht != -1) //Marked
				{
					if(tht == top || !less(j+1, tht-1))
					{
						locked = true; 
						if(j-tht == 1) exch(j, tht); 
						else reverse(tht, j); 
						tht = -1;
					}
					else tht--;
				}
				else if(less(j+1, j)) { tht = j; } 
				j++; 
			}
			if (verbose) printArray(arrayed, "(Overlap upstream)", false); 
			streams++; if (!locked || btm-top <= 1) return;
			
			//Reverse every contrary flux downstream
			locked = false; int u = btm; tht = -1;
			while(u >= top)
			{
				if(tht != -1) //Marked
				{
					if(u == top || less(u-1, u))
					{
						locked = true; 
						if(tht-u == 1) exch(tht, u); 
						else reverse(u, tht);  
						aux = u; tht = -1; 
						if(u == top) u--;
					}
					else u--;
				}
				else
				{ if(u != top && less(u, u-1)) { tht = u ; } u--; }
			}
			top = aux; streams++; 
			if (verbose) printArray(arrayed, "(Reverse downstream)", false);
			if (!locked || btm-top <= 1) return;

			//Overlap every parallel fluxes found downstream
			int v = btm; locked = false;
			while(v > top)
			{
				if(tht != -1) //Marked
				{
					if(tht == btm || !less(tht+1, v-1))
					{
						locked = true; 
						if(tht-v == 1) exch(tht, v); 
						else reverse(v, tht); 
						tht = -1;
					}
					else tht++;
				}
				else if(less(v, v-1)) { tht = v; } 
				v--; 
			}
			if (verbose) printArray(arrayed, "(Overlap downstream)", false); 
			streams++; if (!locked || btm-top <= 1) return;
		} 
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

	private void reverse(int e, int o) 
	{
		int half = (int) ((o-e)/2), m = 0;
		while(m <= half)
		{
			if(less(o-m, e+m))
				exch(e+m, o-m); 
			else break;
			m++;
		}
	}
	
//	private int reverseOut(int l, int r, int top, int btm) 
//	{
//		while(l > top && r < btm)
//		{
//			exch(l, r); l--; r++;
//			if(!less(r, l)) break;
//		}
//		return l + 1;
//	}
	
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