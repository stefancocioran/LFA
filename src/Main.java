import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
	private static int n, m, s, f;
	private static int[] startStates, finalStates;
	private static Integer[][] stateTransitions;
	private static ArrayList<HashSet<Integer>> reversedTransitions;

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: Main <problem>");
			System.exit(1);
		}

		String type = args[0];
		readInput();
		Problem problem = new Problem(type, stateTransitions, reversedTransitions, startStates, finalStates, n, m, s, f);
		problem.solve();
	}

	public static void readInput() {
		Scanner scanner = new Scanner(System.in);
		n = Integer.parseInt(scanner.next());
		m = Integer.parseInt(scanner.next());
		s = Integer.parseInt(scanner.next());
		f = Integer.parseInt(scanner.next());

		stateTransitions = new Integer[n][m];
		reversedTransitions = new ArrayList<>(n);
		for (int i = 0; i < n; i++)
			reversedTransitions.add(new HashSet<>());

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				int num = Integer.parseInt(scanner.next());
				reversedTransitions.get(num).add(i);
				stateTransitions[i][j] = num;
			}
		}

		startStates = new int[s];
		finalStates = new int[f];

		for (int i = 0; i < s; i++) {
			startStates[i] = Integer.parseInt(scanner.next());
		}

		for (int i = 0; i < f; i++) {
			finalStates[i] = Integer.parseInt(scanner.next());
		}
	}
}
