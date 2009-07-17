package org.wikicrimes.util.kernelMap;

/**
 * 
 * @author mairon
 *
 */
public class InsertionSorterCelula {
	private static Object[] a;

	private static int n;

	public static void sort(Object[] a0) {
		a = a0;
		n = a.length;
		insertionsort();
	}

	private static void insertionsort() {
		double t;
		int i, j;
		for (i = 1; i < n; i++) {
			j = i;
			t = ((Celula)a[j]).getEstimativa();
			
			Celula casaTemp = (Celula)a[j];
			
			while (j > 0 && ((Celula)a[j - 1]).getEstimativa() > t) {
				a[j] = a[j - 1];
				j--;
			}
			a[j] = casaTemp;
		}
	}

} // end class InsertionSorter
