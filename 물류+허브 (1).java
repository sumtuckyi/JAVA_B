import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

class E implements Comparable<E> {
    int end, weight;

    public E(int end, int weight) {
        this.end = end;
        this.weight = weight;
    }

    @Override
    public int compareTo(E o) {
        return this.weight - o.weight;
    }
}

class UserSolution {
    HashMap<Integer, Integer> id2idx;
    List<E>[] list, revList;
    int Num;

    public int init(int N, int sCity[], int eCity[], int mCost[]) {

        id2idx = new HashMap<>();
        int idx = 0;

        // index 압축 과정
        for (int i = 0; i < N; i++) {
            if (!id2idx.containsKey(sCity[i]))
                id2idx.put(sCity[i], idx++);
            if (!id2idx.containsKey(eCity[i]))
                id2idx.put(eCity[i], idx++);
        }
        Num = id2idx.size();

        list = new List[Num];
        revList = new List[Num];

        for (int i = 0; i < Num; i++) {
            list[i] = new ArrayList<>();
            revList[i] = new ArrayList<>();
        }

        for (int i = 0; i < N; i++) {
            // 정방향 그래프 구성
            list[id2idx.get(sCity[i])].add(new E(id2idx.get(eCity[i]), mCost[i]));
            // 역방향 그래프 구성
            revList[id2idx.get(eCity[i])].add(new E(id2idx.get(sCity[i]), mCost[i]));
        }
        return Num;
    }
    
    public void add(int sCity, int eCity, int mCost) {
        // 정방향 그래프에 간선 추가하기
        list[id2idx.get(sCity)].add(new E(id2idx.get(eCity), mCost));

        // 역방향 그래프에 간선 추가하기
        revList[id2idx.get(eCity)].add(new E(id2idx.get(sCity), mCost));
	}


    public int cost(int mHub) {
        // mHub 에서 모든 정점까지의 최단 거리
        int[] distance = dijkstra(list, mHub);
        // 모든 정점에서 mHub 까지의 최단 거리
        int[] revdistance = dijkstra(revList, mHub);

        int sum = 0;
        for (int i = 0; i < Num; i++) {
            sum += distance[i];
            sum += revdistance[i];
        }

        return sum;
    }

    private int[] dijkstra(List<E>[] list, int mHub) {
        int X = id2idx.get(mHub);
        boolean visit[] = new boolean[Num];

        int[] distance = new int[Num];

        Arrays.fill(distance, Integer.MAX_VALUE);
        Queue<E> queue = new PriorityQueue<E>();

        queue.add(new E(X, 0));
        distance[X] = 0;

        while (!queue.isEmpty()) {
            E curNode = queue.poll();
            int cur = curNode.end;

            if (visit[cur]) continue;
            visit[cur] = true;

            for (E n : list[cur]) {
                if (distance[n.end] > distance[cur] + n.weight) {
                    distance[n.end] = distance[cur] + n.weight;
                    queue.add(new E(n.end, distance[n.end]));
                }
            }
        }
        return distance;
    }

}
