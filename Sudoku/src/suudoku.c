/*
 * suudoku.c
 *
 *  Created on: 2015/04/17
 *      Author: kakky
 */
#include<stdio.h>
#include<math.h>

#define square 9
#define true 1
#define false 0

int main(void) {
	int problem[square * square];
	int i, j, k, n, p, q, g, r;
	int back = 1, judge = 0;
	FILE*input;
	input = fopen("problem22.txt", "r");

	for (r = 0; r < square * square; r++) {
		problem[r] = 0;
	}

	for (p = 0; p < square; p++) {
		for (q = 0; q < square - 1; q++) {
			k = square * p + q;
			fscanf(input, "%d ", &problem[k]);
		}
		fscanf(input, "%d\n", &problem[k + 1]);
	}

	for (n = 0; n < square * square; n++) {
		if(problem[n]>0 && problem[n]<10){
			continue;
		}

		if (problem[n] == 0) {
			problem[n] = 11;
		}
		while (check(problem, n) == 0) {
			problem[n]++;
		}

		if (problem[n] > 19) {
			problem[n] = 0;
			while (judge == 0) {
				if (problem[n - back] > 10) {
					problem[n - back]++;
					judge++;
				} else {
					back++;
				}
			}
			judge = 0;
			n -= back + 1;
			back = 1;
		}

	}

	for (g = 0; g < square * square; g++) {
		if (problem[g] > 10) {
			problem[g] = problem[g] - 10;
		}
	}

	for (i = 0; i < square; i++) {
		for (j = 0; j < square - 1; j++) {
			k = square * i + j;
			printf("%d", problem[k]);
		}
		printf("%d\n", problem[k + 1]);
	}

	fclose(input);
	return 0;

}

int check(int array[], int k) {
	int a, b, c, d, row, file, rest1, rest2, corner1, corner2;
	int count1 = 0, count2 = 0, count3 = 0;
	file = k % square;
	row = (k - file) / square;
	for (a = 0; a < square; a++) {
		if ((array[square * a + file] + 10 == array[k])
				|| (array[square * a + file] == array[k])) {
			count1++;
		}
	}
	for (b = 0; b < square; b++) {
		if ((array[square * row + b] + 10 == array[k])
				|| (array[square * row + b] == array[k])) {
			count2++;
		}
	}
	rest1 = file % 3;
	rest2 = row % 3;
	corner1 = file - rest1;
	corner2 = row - rest2;
	for (c = corner2; c < corner2 + 3; c++) {
		for (d = corner1; d < corner1 + 3; d++) {
			if ((array[square * c + d] + 10 == array[k])
					|| (array[square * c + d] == array[k])) {
				count3++;
			}
		}
	}

	if (count1 == 1 && count2 == 1 && count3 == 1) {
		return 1;
	} else {
		return 0;
	}
}
