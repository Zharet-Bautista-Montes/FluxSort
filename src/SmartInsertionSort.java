public class SmartInsertionSort 
{
	//{'E', 'A', 'O', 'S', 'R', 'N', 'I', 'D', 'L', 'C', 'T', 'U', 'M', 'P', 'B', 'G', 'V', 'Y', 'Q', 'H', 'F', 'Z', 'J', 'X', 'W', 'K', '0', '5', '4', '2', '9', '8', '6', '7', '3', '1' }
	//{'C', 'A', 'P', 'M', 'E', 'T', 'B', 'S', 'R', 'D', 'L', 'V', 'G', 'F', 'I', 'H', 'O', 'N', 'J', 'Z', 'Q', 'U', 'Y', 'K', 'W', 'X', '9', '4', '1', '3', '0', '5', '7', '2', '6', '8' }
	//{'Z', 'Y', 'X', 'W', 'V', 'U', 'T', 'S', 'R', 'Q', 'P', 'O', 'N', 'M', 'L', 'K', 'J', 'I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A', '9', '8', '7', '6', '5', '4', '3', '2', '1', '0' }
	public char vectorNaive[] = { 'E', 'A', 'O', 'S', 'R', 'N', 'I', 'D', 'L', 'C', 'T', 'U', 'M', 'P', 'B', 'G', 'V', 'Y', 'Q', 'H', 'F', 'Z', 'J', 'X', 'W', 'K', '0', '5', '4', '2', '9', '8', '6', '7', '3', '1' }; 
	public char vectorSmart[] = vectorNaive.clone();
	public static int streams;
	public static int comparisons;
	public static int swaps;

	public static void main(String[] args) 
	{	
		//Test the naive insertion sorting algorithm
		SmartInsertionSort sis = new SmartInsertionSort();
		streams = 0; comparisons = 0; swaps = 0;
		printArray(sis.vectorNaive, "(Unsorted input for naive insertion)");
		long start = System.nanoTime();
		sis.naiveSort(sis.vectorNaive);
		long end = System.nanoTime();
		printArray(sis.vectorNaive, "(Sorted output for naive insertion)");
		System.out.println("Comparisons performed: " + comparisons);
		System.out.println("Swaps performed: " + swaps);
		System.out.println("Needed streams: " + streams);
		System.out.println("Total execution time: " + (end - start) + " ns");

		//Test the naive insertion sorting algorithm
		System.out.println("----------------------------------------------");
		streams = 0; comparisons = 0; swaps = 0;
		printArray(sis.vectorSmart, "(Unsorted input for smart insertion)");
		start = System.nanoTime();
		sis.smartSort(sis.vectorSmart);
		end = System.nanoTime();
		printArray(sis.vectorSmart, "(Sorted output for smart insertion)");
		System.out.println("Comparisons performed: " + comparisons);
		System.out.println("Swaps performed: " + swaps);
		System.out.println("Needed streams: " + streams);
		System.out.println("Total execution time: " + (end - start) + " ns");
	}

	public void naiveSort(char[] arrayed)
	{
		for(int i = 1; i < arrayed.length; i++)
		{
			for(int j = i; j > 0; j--)
				if(less(j, j-1, false))
				{
					exch(j, j-1, false);
					//printArray(arrayed, "");
				}
			streams++;
		}
	}

	public void smartSort(char[] arrayed) 
	{
		int window = 1, top = 0, btm = arrayed.length-1;
		boolean flag; int aux = 0;
		//For each window size (until sqrt(2N))
		while(true)
		{
			aux = top; flag = true;
			if(aux + window > btm) return;
			//Move one position to the right until bottom is reached
			while(aux + window <= btm)
			{
				for(int i = aux; i < aux + window; i++)
				{
					if(flag) //The first time, perform naive insertion
					{
						for(int j = i+1; j <= aux + window; j++)
							if(less(j, j-1, true))
							{
								exch(j, j-1, true);
								//printArray(arrayed, "");
							}
						flag = false;
					}
					else //From now on, just move the lowest element to the left
					{
						for(int j = aux + window; j > i; j--)
							if(less(j, j-1, true))
							{
								exch(j, j-1, true);
								//printArray(arrayed, "");
							}
					}
				}
				aux++;
			}
			btm = aux; flag = true; window++; streams++;
			if(aux - window < top) return;
			//Move one position to the left until top is reached
			while(aux - window >= top)
			{
				for(int u = aux; u > aux - window; u--)
				{
					if(flag) //The first time, perform naive insertion
					{
						for(int v = u-1; v >= aux - window; v--)
							if(less(v+1, v, true))
							{
								exch(v+1, v, true);
								//printArray(arrayed, "");
							}
						flag = false;
					}
					else //From now on, just move the greatest element to the right
					{
						for(int v = aux - window; v < u; v++)
							if(less(v+1, v, true))
							{
								exch(v+1, v, true);
								//printArray(arrayed, "");
							}
					}
				}
				aux--;
			}
			top = aux; flag = true; window++; streams++;
		}
	}

	private boolean less(int w, int y, boolean smart)
	{
		comparisons++;
		if (!smart) return vectorNaive[w] < vectorNaive[y]; 
		else return vectorSmart[w] < vectorSmart[y]; 
	}

	private void exch(int a, int z, boolean smart)
	{
		if(!smart)
		{
			char temp = vectorNaive[a]; 
			vectorNaive[a] = vectorNaive[z]; 
			vectorNaive[z] = temp; 
		}
		else
		{
			char temp = vectorSmart[a]; 
			vectorSmart[a] = vectorSmart[z]; 
			vectorSmart[z] = temp; 
		}
		swaps++;
	}

	private static void printArray(char[] arrayed, String msg)
	{
		for(int k=0; k<arrayed.length; k++)
			System.out.print(arrayed[k] + " ");
		System.out.println(" " + msg);
	}
}