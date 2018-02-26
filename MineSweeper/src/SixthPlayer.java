import jp.ne.kuramae.torix.lecture.ms.core.MineSweeper;
import jp.ne.kuramae.torix.lecture.ms.core.Player;
import java.util.*;
import java.lang.*;

public class SixthPlayer extends Player {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Player player = new SixthPlayer();
		MineSweeper mineSweeper = new MineSweeper();
		mineSweeper.start(player);

	}

	/**
	 * 爆弾がある場所にフラグを立てる
	 */
	boolean[][] flag;
	double[][] prob;
	int[][] virtual;
	int[][] count1;
	int[][] count2;
	int[][] countdamy;

	@Override
	protected void start() {
		flag = new boolean[getWidth()][getHeight()];
		prob = new double[getWidth()][getHeight()];
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				flag[x][y] = false;
			}
		}
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				prob[x][y] = restBombProb();
			}
		}
		LOOP: while (!isClear() && !isGameOver()) {
			int sfnum1;
			int sfnum2;

			do {
				do {
					// まず，フラグを立てる
					buildFlag();
					sfnum1 = safeCellnum();
					// 確実にセーフのマスを開ける
					openSafeCell();

				} while (sfnum1 != 0);

				virtualBoard();
				sfnum2 = safeCellnum();
				openSafeCell();
			} while (sfnum2 != 0);

			openUnSafeCell();

			showFlag();
		}
	}

	/**
	 * フラグがどこに立っているかをコンソールに表示する
	 */
	void showFlag() {
		System.out.println("======================");
		FLAG: for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (flag[x][y] == true) {
					System.out.print("x");
				} else if (getCell(x, y) >= 0) {
					System.out.print(getCell(x, y));
				} else {
					System.out.print(".");
				}
			}
			System.out.println();
		}

	}

	/**
	 * 指定位置にフラグを立てるべきかどうかを返す
	 * 
	 * 1110 1.10 1110 0000 で，x=1,y=1のとき， getCell(x+1,
	 * y+1)=1で，その周りに開いていないセルは一つだけなので 開いていないCell(x,y)は必ず爆弾となる．
	 * したがって，そこにはフラグを立てることになる．
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	void buildFlag() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				boolean f = isFlag(x, y);
				flag[x][y] = f;
			}
		}
	}

	boolean isFlag(int x, int y) {
		int bombNum = getCell(x, y);
		int num = 0;
		if (bombNum < 0) {
			for (int dy = -1; dy <= 1; dy++) {
				for (int dx = -1; dx <= 1; dx++) {
					if ((dx == 0 && dy == 0) == true) {
						continue;// この後の作業は行わず上に戻る
					}
					if (isOut(x + dx, y + dy) == false) {
						continue;
					}
					int n = getUnOpenCellNum(x + dx, y + dy);
					// まだ開いていないセルの数が，そのセルの周りにある爆弾の数と同じならばそこに爆弾がある．
					if (n == getCell(x + dx, y + dy)) {
						num = 1;
					}
				}
			}
		}
		if (num == 1) {
			return true;
		} else {
			return false;
		}

	}

	void openSafeCell() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				int fn = flagNum(x, y);
				int bombnum = getCell(x, y);
				if (bombnum <= 0) {
					continue;
				}
				if (fn == bombnum) {
					for (int dy = -1; dy <= 1; dy++) {
						for (int dx = -1; dx <= 1; dx++) {
							if (isOut(x + dx, y + dy) == false) {
								continue;
							}
							if ((dx == 0 && dy == 0) == true) {
								continue;
							}
							if (getCell(x + dx, y + dy) < 0) {
								if (flag[x + dx][y + dy] == false) {
									open(x + dx, y + dy);
								}

							}
						}
					}
				}
			}
		}
	}

	int safeCellnum() {
		int safenum = 0;
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				int fn = flagNum(x, y);
				int bombnum = getCell(x, y);
				if (bombnum <= 0) {
					continue;
				}
				if (fn == bombnum) {
					for (int dy = -1; dy <= 1; dy++) {
						for (int dx = -1; dx <= 1; dx++) {
							if (isOut(x + dx, y + dy) == false) {
								continue;
							}
							if ((dx == 0 && dy == 0) == true) {
								continue;
							}
							if (getCell(x + dx, y + dy) < 0) {
								if (flag[x + dx][y + dy] == false) {
									safenum++;
								}

							}
						}
					}
				}
			}
		}
		return safenum;
	}

	/**
	 * 指定したセルの周りで開いていないセルの数を返す
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	void openUnSafeCell() {
		OPEN: for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (flag[x][y] == true) {
					continue;// continueの意味はこの後の作業を行わず上のforに戻ってxを１増やして続ける
				}

				int bombNum = getCell(x, y);
				if (bombNum >= 0) {
					continue;
				}
				if (bombNum < 0) {
					prob[x][y] = getProb(x, y);
					double min = minProb();
					if (prob[x][y] == min) {
						open(x, y);
						break OPEN;// 1マスあけたらまたフラグをたてるところからするためにOPENの外に出る
					}
				}
			}
		}
	}

	int getUnOpenCellNum(int x, int y) {
		int unOpen = 0;
		for (int dy = -1; dy <= 1; dy++) {
			for (int dx = -1; dx <= 1; dx++) {
				if (isOut(x + dx, y + dy) == false) {
					continue;
				}
				if (dx == 0 && dy == 0) {
					continue;
				}
				if (getCell(x + dx, y + dy) < 0) {
					unOpen++;// x,yの周りに開いていないセルがあればunOpenを1増やす
				}
			}
		}
		return unOpen;
	}

	/**
	 * 外に飛び出していないか
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	boolean isOut(int x, int y) {
		if (0 <= x && x < getWidth() && 0 <= y && y < getHeight() == true) {
			return true;// x,yが範囲外ならfalseを返す
		} else {
			return false;
		}
	}

	int flagNum(int x, int y) {
		int num2 = 0;
		for (int dy = -1; dy <= 1; dy++) {
			for (int dx = -1; dx <= 1; dx++) {
				if (isOut(x + dx, y + dy) == false) {
					continue;
				}
				if ((dx == 0 && dy == 0) == true) {
					continue;
				}
				if (flag[x + dx][y + dy] == true) {
					num2 = num2 + 1;
				}
			}

		}
		return num2;
	}

	int totalFlagNum() {
		int tfnum = 0;
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (flag[x][y] == true) {
					tfnum++;
				}
			}
		}
		return tfnum;
	}

	int openNum() {
		int ofnum = 0;
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (getCell(x, y) > 0) {
					ofnum++;
				}
			}
		}
		return ofnum;
	}

	double restBombProb() {
		double rbprob;
		int restbomb;
		int mathNum = getWidth() * getHeight();
		if (mathNum == 81) {
			restbomb = 10 - totalFlagNum();
		} else {
			if (mathNum == 256) {
				restbomb = 40 - totalFlagNum();
			} else {
				if (mathNum == 480) {
					restbomb = 99 - totalFlagNum();
				} else {
					restbomb = 0;
				}
			}
		}
		rbprob = restbomb / mathNum - openNum()-totalFlagNum();
		return rbprob;
	}

	double getProb(int x, int y) {
		double prob1, cell, unopen;
		double prob2 = restBombProb();
		for (int dy = -1; dy <= 1; dy++) {
			for (int dx = -1; dx <= 1; dx++) {
				if (isOut(x + dx, y + dy) == false) {
					continue;
				}
				if ((dx == 0 && dy == 0) == true) {
					continue;
				}
				cell = getCell(x + dx, y + dy) - flagNum(x + dx, y + dy);
				unopen = getUnOpenCellNum(x + dx, y + dy);
				prob1 = cell / unopen;
				if (prob1 > prob2) {
					prob2 = prob1;
				}
			}
		}
		return prob2;
	}

	double minProb() {
		double min1;
		double min2 = 1;
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				int cell2 = getCell(x, y);
				if (cell2 >= 0) {
					continue;
				}
				if (flag[x][y] == true) {
					continue;
				}
				if (cell2 < 0) {
					min1 = getProb(x, y);
					if (min2 > min1) {
						min2 = min1;
					}
				}
			}
		}
		return min2;
	}

	int virflagNum(int x, int y) {
		int virnum = 0;
		for (int dy = -1; dy <= 1; dy++) {
			for (int dx = -1; dx <= 1; dx++) {
				if (isOut(x + dx, y + dy) == false) {
					continue;
				}
				if (dx == 0 && dy == 0) {
					continue;
				}
				if (virtual[x + dx][y + dy] == 9
						|| virtual[x + dx][y + dy] == 19) {
					virnum++;
				}
			}
		}
		return virnum;
	}

	// 仮想のボードを考えて、試しにフラグを置いてみたときどうなるかをみる。
	void virtualBoard() {
		virtual = new int[getWidth()][getHeight()];
		count1 = new int[getWidth()][getHeight()];
		count2 = new int[getWidth()][getHeight()];
		countdamy = new int[getWidth()][getHeight()];
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				virtual[x][y] = getCell(x, y);
				if (flag[x][y] == true) {
					virtual[x][y] = 9;// フラグが立っていうマスの数字は９
				}
			}
		}// ボード完成

		// シミュレーション開始
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (getCell(x, y) - flagNum(x, y) < 1) { // フラグの数が数字と同じ、または開いていない場合を除く。
					continue;
				}
				if (virtual[x][y] == 9) {
					continue;
				}
				int num = 1;
				int n = getUnOpenCellNum(x, y) - flagNum(x, y);
				for (int y1 = 0; y1 < getHeight(); y1++) {
					for (int x1 = 0; x1 < getWidth(); x1++) {
						count1[x1][y1] = 0;
						count2[x1][y1] = 0;
						countdamy[x1][y1] = 0;
					}
				}
				int sucnum = 0;

				for (int dy = -1; dy <= 1; dy++) {
					for (int dx = -1; dx <= 1; dx++) {
						if (isOut(x + dx, y + dy) == false) {
							continue;
						}
						if (dx == 0 && dy == 0) {
							continue;
						}
						if (virtual[x + dx][y + dy] == -1) {
							virtual[x + dx][y + dy] = 10 + num;// まわりの何もないマスを１１，１２・・・とする。
							num++;
						}
					}
				}

				// ・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・
				num = num - 1;
				int r = getCell(x, y) - flagNum(x, y);
				int[] d = new int[num + 1];
				int[] e = new int[num + 1];
				int[][] bag = new int[getWidth()][getHeight()];
				for (int y1 = 0; y1 < getHeight(); y1++) {
					for (int x1 = 0; x1 < getWidth(); x1++) {
						bag[x1][y1] = 0;
					}
				}
				for (int i = 1; i <= num; ++i) {
					d[i] = 1;
				}

				while (d[0] <= 0) {
					for (int i = 1; i <= num; i++) {
						e[i] = 10;
					}
					for (int i = 1; i <= r; i++) {
						e[i] = e[i] + d[i]; // e[i]に組み合わせを入れる。
					}
					int same = 0;
					if (r > 1) {
						for (int i = 1; i < r; i++) { // 重複しているものを除く
							for (int j = i + 1; j <= r; j++) {
								if (e[i] == e[j]) {
									same = 1;
								}
							}
						}
					}

					if (same == 0) {
						for (int y1 = 0; y1 < getHeight(); y1++) { // 仮にフラグをたてるところを１９にする。
							for (int x1 = 0; x1 < getWidth(); x1++) {
								for (int i = 1; i <= r; i++) {
									if (e[i] == virtual[x1][y1]) {
										bag[x1][y1] = virtual[x1][y1];
										virtual[x1][y1] = 19;
									}
								}
							}
						}
						int fault = 0;
						for (int y1 = 0; y1 < getHeight(); y1++) { // 仮想のフラグをたてた結果矛盾したものを除外する。
							for (int x1 = 0; x1 < getWidth(); x1++) {
								if (getCell(x1, y1) < 0) {
									continue;
								}
								if (getCell(x1, y1) < virflagNum(x1, y1)) {
									fault = 1;
								}
							}
						}

						if (fault == 0) {
							for (int y1 = 0; y1 < getHeight(); y1++) {
								for (int x1 = 0; x1 < getWidth(); x1++) {
									if (virtual[x1][y1] == 19) {
										count1[x1][y1] = count1[x1][y1] + 1;
									}
									if (0 < virtual[x1][y1]
											&& virtual[x1][y1] < 9) {
										if (virtual[x1][y1] == virflagNum(x1,
												y1)) {
											for (int dy = -1; dy <= 1; dy++) {
												for (int dx = 0; dx <= 1; dx++) {
													if (isOut(x1 + dx, y1 + dy) == false) {
														continue;
													}
													if (dx == 0 && dy == 0) {
														continue;
													}
													if (virtual[x1 + dx][y1
															+ dy] == -1) {
														countdamy[x1 + dx][y1
																+ dy] = countdamy[x1
																+ dx][y1 + dy] + 1;
													}
												}
											}
										}
									}
								}
							}
							for (int y1 = 0; y1 < getHeight(); y1++) {
								for (int x1 = 0; x1 < getWidth(); x1++) {
									if (countdamy[x1][y1] > 0) {
										count2[x1][y1]++;
										countdamy[x1][y1] = 0;
									}
								}
							}
							sucnum++;
						}
					}
					for (int y1 = 0; y1 < getHeight(); y1++) {
						for (int x1 = 0; x1 < getWidth(); x1++) {
							if (virtual[x1][y1] == 19) {
								virtual[x1][y1] = bag[x1][y1];
								bag[x1][y1] = 0;
							}
						}
					}

					for (int j = r; 0 <= j; --j) {
						++d[j];
						for (int k = j + 1; k <= r; k++) {
							d[k] = d[k - 1];
						}
						if (d[j] <= num) {
							break;
						}
					}

				}
				// ・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・・
				for (int y1 = 0; y1 < getHeight(); y1++) {
					for (int x1 = 0; x1 < getWidth(); x1++) {
						if (count1[x1][y1] == sucnum) {
							flag[x1][y1] = true;
							virtual[x1][y1] = 9;
						}
						if (count2[x1][y1] == sucnum) {
							open(x1, y1);
							virtual[x1][y1] = getCell(x1, y1);
						}
					}
				}
				for (int y1 = 0; y1 < getHeight(); y1++) {
					for (int x1 = 0; x1 < getWidth(); x1++) {
						if (virtual[x1][y1] > 10) {
							virtual[x1][y1] = -1;
						}
					}
				}
			}
		}
	}
}
