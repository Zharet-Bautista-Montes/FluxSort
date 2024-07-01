import java.util.Random;
import java.util.stream.IntStream;

public class FluxSort 
{
	//{'E', 'A', 'O', 'S', 'R', 'N', 'I', 'D', 'L', 'C', 'T', 'U', 'M', 'P', 'B', 'G', 'V', 'Y', 'Q', 'H', 'F', 'Z', 'J', 'X', 'W', 'K', '0', '5', '4', '2', '9', '8', '6', '7', '3', '1' }
	//{'C', 'A', 'P', 'M', 'E', 'T', 'B', 'S', 'R', 'D', 'L', 'V', 'G', 'F', 'I', 'H', 'O', 'N', 'J', 'Z', 'Q', 'U', 'Y', 'K', 'W', 'X', '9', '4', '1', '3', '0', '5', '7', '2', '6', '8' }
	//{'Z', 'Y', 'X', 'W', 'V', 'U', 'T', 'S', 'R', 'Q', 'P', 'O', 'N', 'M', 'L', 'K', 'J', 'I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A', '9', '8', '7', '6', '5', '4', '3', '2', '1', '0' }
	public char vector[] = { 'E', 'A', 'O', 'S', 'R', 'N', 'C', 'A', 'P', 'M', 'E', 'T', 'Z', 'Y', 'X', 'W', 'V', 'U', 'T', 'I', 'D', 'L', 'C', 'T', 'U', 'B', 'S', 'R', 'D', 'L', 'V', 'T', 'S', 'R', 'Q', 'P', 'O', 'M', 'P', 'B', 'G', 'V', 'Y', 'G', 'F', 'I', 'H', 'O', 'N' }; 
	public static int[] testarray;
	public static int streams;
	public static int comparisons;
	public static int swaps;

	public static void main(String[] args) 
	{
		FluxSort fs = new FluxSort();
		System.out.println("Length \t Swaps");
		for(int x = 1; x <= 10000; x++)
		{
			testarray = IntStream.generate(() -> new Random().nextInt(1000)).limit(x).toArray();
			streams = 0; comparisons = 0; swaps = 0;
			//printArray(testarray, "(Unsorted input)");
			long start = System.nanoTime();
			fs.fluxSort(testarray);
			long end = System.nanoTime();
			//printArray(testarray, "(Sorted output)");
			//System.out.println("Comparisons performed: " + comparisons);
			//System.out.println("Swaps performed: " + swaps);
			//System.out.println("Needed streams: " + streams);
			//System.out.println("Total execution time: " + (end - start) + " ns");
			//System.out.println("----------------------------------------------");
			System.out.println(x + " \t " + swaps);
		}
	}

	public void fluxSort(int[] arrayed) 
	{
		boolean locked = true, thorough = false; 
		int tht = -1, aux = 0, top = 0, btm = arrayed.length-1;
		while(true)
		{
			//Reverse every backflux found upstream
			locked = false; thorough = false; int i = top; 
			while(i <= btm)
			{
				if(tht != -1)
				{          
					if(i == btm || less(i, i+1))
					{
						if(i-tht == 1) exch(i, tht); 
						else 
						{
							reverse(tht, i); 
							thorough = true; 
						}
						aux = i; tht = -1; 
						if(i == btm) i++;
					}
					else i++;
				}
				else
				{ if(i != btm && less(i+1, i)) { locked = true; tht = i; } i++; }
			}
			btm = aux-1; //printArray(arrayed, "(Reverse upstream)");
			streams++; if (!locked || btm-top <= 1) return;

			//Splash every pair of waves found upstream
			if(!thorough)
			{
				int j = top, pre = top; boolean upeak = false;
				while(j <= btm)
				{
					if(less(j+1, j) || j == btm)
					{
						if(upeak)
						{
							reverse(pre, j);
							pre = j+1;
							upeak = false;
						}
						else upeak = true;
					}
					j++;
				}	
				//printArray(arrayed, "(Splash upstream)"); 
				streams++;
			}

			//Just splash the whole stream
			else
			{
				reverse(top, btm);
				//printArray(arrayed, "(Splash everywhere)"); 
				streams++;
			}

			//Reverse every backflux found downstream
			locked = false; thorough = false; int u = btm; 
			while(u >= top)
			{
				if(tht != -1)
				{
					if(u == top || less(u-1, u))
					{
						if(tht-u == 1) exch(tht, u); 
						else 
						{
							reverse(u, tht); 
							thorough = true; 
						}
						aux = u; tht = -1; 
						if(u == top) u--;
					}
					else u--;
				}
				else
				{ if(u != top && less(u, u-1)) { locked = true; tht = u ; } u--; }
			}
			top = aux+1; //printArray(arrayed, "(Reverse downstream)");
			streams++; if (!locked || btm-top <= 1) return;

			//Splash every pair of waves found downstream
			if(!thorough)
			{
				int v = btm, post = btm; boolean dpeak = false;
				while(v >= top)
				{
					if(less(v, v-1) || v == top)
					{
						if(dpeak)
						{
							reverse(v, post);
							post = v-1;
							dpeak = false;
						}
						else dpeak = true;
					}
					v--;
				}
				//printArray(arrayed, "(Splash downstream)"); 
				streams++;
			}

			//Just splash the whole stream
			else
			{
				reverse(top, btm);
				//printArray(arrayed, "(Splash everywhere)"); 
				streams++;
			}
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
		int halb = (int) ((o-e)/2);
		for (int m = 0; m <= halb; m++)
			if(less(o-m, e+m))
				exch(e+m, o-m); 
	}

	private static void printArray(char[] arrayed, String msg)
	{
		for(int k=0; k<arrayed.length; k++)
			System.out.print(arrayed[k] + " ");
		System.out.println(" " + msg);
	}
	
	private static void printArray(int[] arrayed, String msg)
	{
		for(int k=0; k<arrayed.length; k++)
			System.out.print(arrayed[k] + " ");
		System.out.println(" " + msg);
	}
}