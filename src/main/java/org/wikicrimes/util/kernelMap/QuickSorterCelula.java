package org.wikicrimes.util.kernelMap;

/**
 * 
 * @author mairon
 *
 */
public class QuickSorterCelula {
	private Object[] a;

	private int n;

	public void sort(Object[] a) {
		this.a = a;
		n = a.length;
		quicksort(0, n - 1);
	}

	private void quicksort(int lo, int hi) {
		int i = lo, j = hi;
		Celula casaTemp = (Celula)a[(lo + hi) / 2];

		//  Aufteilung
		while (i <= j) {
			while (((Celula)a[i]).getEstimativa() < casaTemp.getEstimativa())
				i++;
			while (((Celula)a[j]).getEstimativa() > casaTemp.getEstimativa())
				j--;
			if (i <= j) {
				exchange(i, j);
				i++;
				j--;
			}
		}

		// Rekursion
		if (lo < j)
			quicksort(lo, j);
		if (i < hi)
			quicksort(i, hi);
	}

	private void exchange(int i, int j) {
		Celula casaTemp = (Celula)a[i];
		a[i] = a[j];
		a[j] = casaTemp;
	}

} // end class QuickSorter
