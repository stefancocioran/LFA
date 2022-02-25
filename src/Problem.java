import java.util.*;
import java.util.stream.Collectors;

public class Problem {
	private static final Random random = new Random();
	private final String problemType;
	private final HashSet<Pair> visited = new HashSet<>();
	private final int n;
	private final int m;
	private final int s;
	private final int f;
	private final int[] startStates;
	private final int[] finalStates;
	private final Integer[][] stateTransitions;
	private final ArrayList<HashSet<Integer>> reversedTransitions;
	private ArrayList<Integer> mergeSequence = new ArrayList<>();
	private ArrayList<Integer> states;

	public Problem(String problemType, Integer[][] stateTransitions, ArrayList<HashSet<Integer>> reversedTransitions,
				   int[] startStates, int[] finalStates, int n, int m, int s, int f) {
		this.problemType = problemType;
		this.n = n;
		this.m = m;
		this.s = s;
		this.f = f;
		this.startStates = startStates;
		this.finalStates = finalStates;
		this.stateTransitions = stateTransitions;
		this.reversedTransitions = reversedTransitions;
	}

	public static int getRandom(ArrayList<Integer> array) {
		int rnd = random.nextInt(array.size());
		return array.get(rnd);
	}

	public void solve() {
		ArrayList<Integer> sol;
		switch (problemType) {
			case "accessible":
				sol = runAccessible();
				break;
			case "productive":
				sol = runProductive();
				break;
			case "useful":
				sol = runUseful();
				break;
			case "synchronize":
				sol = runSynchronize();
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + problemType);
		}

		for (int i : sol) {
			System.out.println(i);
		}
	}

	private ArrayList<Integer> runSynchronize() {
		ArrayList<Integer> sol = new ArrayList<>();
		states = new ArrayList<>();
		if (s == 0) {
			for (int i = 0; i < n; i++) {
				states.add(i);
			}
		} else {
			states = (ArrayList<Integer>) Arrays.stream(startStates).boxed().collect(Collectors.toList());
		}

		while (states.size() > 1) {
			// Se aleg primele 2 stari disponibile
			int stateS = getRandom(states);
			int stateT = getRandom(states);
			while (stateS == stateT) {
				stateT = getRandom(states);
			}
			// Parcurg cu DFS pana se ajunge intr-o stare de forma (s_i, s_i)
			getMergeSequence(stateS, stateT);
			// Rezultatul partial il adaug la solutie
			sol.addAll(mergeSequence);
			// Pentru fiecare stare efectuez tranzitiile pe portiunea de cuvant gasita
			// Starile disponibile vor fi cele rezultate in urma efectuarii tranzitiilor
			updateStates(mergeSequence);
		}
		return sol;
	}

	public void getMergeSequence(Integer stateS, Integer stateT) {
		if (stateS > stateT) {
			int aux = stateS;
			stateS = stateT;
			stateT = aux;
		}
		DFS(new Pair(stateS, stateT));
		visited.clear();
	}

	public boolean DFS(Pair state) {
		visited.add(state);
		// Daca ajunge intr-o stare de forma (s_i, s_i), se termina
		if (state.first == state.second) {
			if (f == 0) {
				return true;
			} else if (containsValue(finalStates, state.first)) {
				return true;
			}
		}
		// Se alege un simbol pentru tranzitie (de la 0 la m-1)
		for (int i = 0; i < m; i++) {
			int newStateS = stateTransitions[state.first][i];
			int newStateT = stateTransitions[state.second][i];
			// Starile din pereche vor fi ordonate crescator dupa valoare
			// astfel incat sa nu apara stari duplicate
			// ex: (S1, S2) va coincide cu (S2, S1)
			if (newStateS > newStateT) {
				int aux = newStateS;
				newStateS = newStateT;
				newStateT = aux;
			}
			Pair newNode = new Pair(newStateS, newStateT);
			// Verific daca pe aceasta tranzitie ajung intr-o stare nevizitata
			if (!visited.contains(newNode)) {
				mergeSequence.add(i);
				if (DFS(newNode)) {
					return true;
				}
				mergeSequence.remove(mergeSequence.size() - 1);
			}
		}
		return false;
	}

	private void updateStates(ArrayList<Integer> steps) {
		HashSet<Integer> newStates = new HashSet<>();
		for (int currentState : states) {
			for (Integer step : steps) {
				currentState = stateTransitions[currentState][step];
			}
			newStates.add(currentState);
		}
		states = new ArrayList<>(newStates);
		mergeSequence = new ArrayList<>();
	}

	private ArrayList<Integer> runAccessible() {
		return BFS(startStates, s, "accessible");
	}

	private ArrayList<Integer> runProductive() {
		return BFS(finalStates, f, "productive");
	}

	private ArrayList<Integer> runUseful() {
		ArrayList<Integer> solAccessible = runAccessible();
		ArrayList<Integer> solProductive = runProductive();
		// Returneaza intersectia multimilor pentru starile accesibile si productive
		return solAccessible.stream()
				.distinct()
				.filter(solProductive::contains).collect(Collectors.toCollection(ArrayList::new));
	}

	private ArrayList<Integer> BFS(int[] beginSearchStates, int size, String type) {
		ArrayList<Integer> sol = new ArrayList<>();
		int[] visited = new int[n];
		LinkedList<Integer> queue = new LinkedList<>();

		// Marchez ca fiind vizitate starile din care se pleaca cautarea
		// si le adaug in coada
		for (int i = 0; i < size; i++) {
			visited[beginSearchStates[i]] = 1;
			queue.add(beginSearchStates[i]);
		}

		while (queue.size() != 0) {
			int state = queue.poll();
			sol.add(state);
			Integer[] neighbors;
			// Daca se cauta starile accesibile, vecinii nodului curent vor
			// fi luati din matricea de tranzitii de stari data ca input
			if (type.equals("accessible")) {
				neighbors = stateTransitions[state];
			} else {
				// Altfel daca se cauta starile productive, vecinii nodului
				// curent vor fi luati din lista de adiacenta pentru parcurgerea
				// in sens invers
				neighbors = reversedTransitions.get(state).toArray(new Integer[0]);
			}
			for (int neigh : neighbors) {
				if (visited[neigh] == 0) {
					visited[neigh] = 1;
					queue.add(neigh);
				}
			}
		}
		return sol;
	}

	public boolean containsValue(int[] array, int value) {
		for (int j : array) {
			if (j == value) {
				return true;
			}
		}
		return false;
	}

	public class Pair {
		private final Integer first;
		private final Integer second;

		public Pair(Integer first, Integer second) {
			this.first = first;
			this.second = second;
		}

		@Override
		public int hashCode() {
			return Objects.hash(first, second);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Pair pair = (Pair) o;
			return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
		}
	}
}
