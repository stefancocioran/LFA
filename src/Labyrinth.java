import java.util.Scanner;

public class Labyrinth {
	private static int l, c, r, o;
	private static int[] startStates, finalStates;
	private static Integer[][] stateTransitions;

	public static void main(String[] args) {
		readInput();
		printInput();
	}

	public static void readInput() {
		Scanner scanner = new Scanner(System.in);
		l = Integer.parseInt(scanner.next());
		c = Integer.parseInt(scanner.next());
		r = Integer.parseInt(scanner.next());
		o = Integer.parseInt(scanner.next());

		stateTransitions = new Integer[l * c][4];

		for (int i = 0; i < l; i++) {
			for (int j = 0; j < c; j++) {
				// est  nord  vest  sud
				// 0      1     2    3
				int read = Integer.parseInt(scanner.next());
				int[] num = decimalToBinary(read);

				if (num[0] == 0 && j != c - 1) {
					stateTransitions[i * c + j][0] = i * c + j + 1;
				} else {
					stateTransitions[i * c + j][0] = i * c + j;
				}

				if (num[1] == 0 && i != 0) {
					stateTransitions[i * c + j][1] = (i - 1) * c + j;
				} else {
					stateTransitions[i * c + j][1] = i * c + j;
				}
				if (num[2] == 0 && j != 0) {
					stateTransitions[i * c + j][2] = i * c + j - 1;
				} else {
					stateTransitions[i * c + j][2] = i * c + j;
				}

				if (num[3] == 0 && i != l - 1) {
					stateTransitions[i * c + j][3] = (i + 1) * c + j;
				} else {
					stateTransitions[i * c + j][3] = i * c + j;
				}
			}
		}
		startStates = new int[r];
		finalStates = new int[o];

		for (int i = 0; i < r; i++) {
			int line = Integer.parseInt(scanner.next());
			int col = Integer.parseInt(scanner.next());
			startStates[i] = line * c + col;
		}

		for (int i = 0; i < o; i++) {
			int line = Integer.parseInt(scanner.next());
			int col = Integer.parseInt(scanner.next());
			finalStates[i] = line * c + col;
		}
	}

	public static void printInput() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(l * c).append(" ").append(4).append(" ").append(r).append(" ").append(o).append("\n");
		for (int i = 0; i < l * c; i++) {
			for (int j = 0; j < 4; j++) {
				if (j == 3) {
					stringBuilder.append(stateTransitions[i][j]).append(" \n");
				} else {
					stringBuilder.append(stateTransitions[i][j]).append(" ");
				}
			}
		}
		for (int i = 0; i < r; i++) {
			if (i == r - 1) {
				stringBuilder.append(startStates[i]).append(" \n");
			} else {
				stringBuilder.append(startStates[i]).append(" ");
			}
		}
		for (int i = 0; i < o; i++) {
			if (i == o - 1) {
				stringBuilder.append(finalStates[i]).append("\n");
			} else {
				stringBuilder.append(finalStates[i]).append(" ");
			}
		}
		System.out.println(stringBuilder);
	}

	public static int[] decimalToBinary(int num) {
		int[] numBinary = new int[4];
		int i = 0;

		while (num > 0) {
			numBinary[i++] = num % 2;
			num = num / 2;
		}

		return numBinary;
	}
}
