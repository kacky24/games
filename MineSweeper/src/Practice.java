import java.util.*;
import java.lang.*;
import java.io.*;

class Practice {
	public static void main(String args[]) throws java.lang.Exception {
		Scanner s = new Scanner(System.in);
		int n = s.nextInt();
		int r = s.nextInt();
		int[] d = new int[n + 1];
		for (int i = 1; i <= n; i++){
			d[i] = 1;
		}
		while (d[0] <= 0) {
			for (int i = 1; i <= r; i++){
				System.out.print(" " + d[i]);
			}
			System.out.println("");
			for (int j = r; 0 <= j; --j) {
				++d[j];
				for (int k = j + 1; k <= r; k++){
					d[k] = d[k - 1];
				}
				if (d[j] <= n){
					break;
				}
			}

		}
	}
}