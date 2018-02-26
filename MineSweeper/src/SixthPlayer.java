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
	 * ���e������ꏊ�Ƀt���O�𗧂Ă�
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
					// �܂��C�t���O�𗧂Ă�
					buildFlag();
					sfnum1 = safeCellnum();
					// �m���ɃZ�[�t�̃}�X���J����
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
	 * �t���O���ǂ��ɗ����Ă��邩���R���\�[���ɕ\������
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
	 * �w��ʒu�Ƀt���O�𗧂Ă�ׂ����ǂ�����Ԃ�
	 * 
	 * 1110 1.10 1110 0000 �ŁCx=1,y=1�̂Ƃ��C getCell(x+1,
	 * y+1)=1�ŁC���̎���ɊJ���Ă��Ȃ��Z���͈�����Ȃ̂� �J���Ă��Ȃ�Cell(x,y)�͕K�����e�ƂȂ�D
	 * ���������āC�����ɂ̓t���O�𗧂Ă邱�ƂɂȂ�D
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
						continue;// ���̌�̍�Ƃ͍s�킸��ɖ߂�
					}
					if (isOut(x + dx, y + dy) == false) {
						continue;
					}
					int n = getUnOpenCellNum(x + dx, y + dy);
					// �܂��J���Ă��Ȃ��Z���̐����C���̃Z���̎���ɂ��锚�e�̐��Ɠ����Ȃ�΂����ɔ��e������D
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
	 * �w�肵���Z���̎���ŊJ���Ă��Ȃ��Z���̐���Ԃ�
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	void openUnSafeCell() {
		OPEN: for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (flag[x][y] == true) {
					continue;// continue�̈Ӗ��͂��̌�̍�Ƃ��s�킸���for�ɖ߂���x���P���₵�đ�����
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
						break OPEN;// 1�}�X��������܂��t���O�����Ă�Ƃ��납�炷�邽�߂�OPEN�̊O�ɏo��
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
					unOpen++;// x,y�̎���ɊJ���Ă��Ȃ��Z���������unOpen��1���₷
				}
			}
		}
		return unOpen;
	}

	/**
	 * �O�ɔ�яo���Ă��Ȃ���
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	boolean isOut(int x, int y) {
		if (0 <= x && x < getWidth() && 0 <= y && y < getHeight() == true) {
			return true;// x,y���͈͊O�Ȃ�false��Ԃ�
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

	// ���z�̃{�[�h���l���āA�����Ƀt���O��u���Ă݂��Ƃ��ǂ��Ȃ邩���݂�B
	void virtualBoard() {
		virtual = new int[getWidth()][getHeight()];
		count1 = new int[getWidth()][getHeight()];
		count2 = new int[getWidth()][getHeight()];
		countdamy = new int[getWidth()][getHeight()];
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				virtual[x][y] = getCell(x, y);
				if (flag[x][y] == true) {
					virtual[x][y] = 9;// �t���O�������Ă����}�X�̐����͂X
				}
			}
		}// �{�[�h����

		// �V�~�����[�V�����J�n
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (getCell(x, y) - flagNum(x, y) < 1) { // �t���O�̐��������Ɠ����A�܂��͊J���Ă��Ȃ��ꍇ�������B
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
							virtual[x + dx][y + dy] = 10 + num;// �܂��̉����Ȃ��}�X���P�P�C�P�Q�E�E�E�Ƃ���B
							num++;
						}
					}
				}

				// �E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E
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
						e[i] = e[i] + d[i]; // e[i]�ɑg�ݍ��킹������B
					}
					int same = 0;
					if (r > 1) {
						for (int i = 1; i < r; i++) { // �d�����Ă�����̂�����
							for (int j = i + 1; j <= r; j++) {
								if (e[i] == e[j]) {
									same = 1;
								}
							}
						}
					}

					if (same == 0) {
						for (int y1 = 0; y1 < getHeight(); y1++) { // ���Ƀt���O�����Ă�Ƃ�����P�X�ɂ���B
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
						for (int y1 = 0; y1 < getHeight(); y1++) { // ���z�̃t���O�����Ă����ʖ����������̂����O����B
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
				// �E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E�E
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
