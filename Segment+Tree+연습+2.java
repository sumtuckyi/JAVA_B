import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Solution {

    static int n;
    static int[] a;
    static long[] sumTree;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int t = Integer.parseInt(br.readLine());

        for (int test = 1; test <= t; test++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            n = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
            sb.append("#").append(test).append(" ");
            a = new int[n];
            sumTree = new long[4 * n];

            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(st.nextToken());
                if (i % 2 == 1) {
                    a[i] = -a[i];
                }
            }

            init(1, 0, n - 1);

            for (int i = 0; i < m; i++) {
                st = new StringTokenizer(br.readLine());
                int query = Integer.parseInt(st.nextToken());
                int left = Integer.parseInt(st.nextToken());
                int right = Integer.parseInt(st.nextToken());

                if (query == 0) {
                    if (left % 2 == 1) {
                        right = -right;
                    }
                    update(1, 0, n - 1, left, right);  // O(Log N)
                } else if (query == 1) {
                    long sum = querySum(1, 0, n - 1, left, right - 1);  // O(Log N)
                    if (left % 2 == 1){
                        sum = -sum;
                    }
                    sb.append(sum).append(" ");
                }
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    // 세그먼트 트리 초기화
    public static void init(int node, int nodeLeft, int nodeRight) {
        // 현재 노드의 번호가 node 이고, 이 노드가 관리하는 구간은 [nodeLeft ~ nodeRight] 이다.
        if (nodeLeft == nodeRight) {
            sumTree[node] = a[nodeLeft];
            return;
        } // 현재 노드가 리프 노드인 경우, 바로 초기화

        int mid = (nodeLeft + nodeRight) / 2;

        init(
                node * 2,
                nodeLeft,
                mid
        ); // 왼쪽 자식 노드 재귀호출

        init(
                node * 2 + 1,
                mid + 1,
                nodeRight
        );
        // 왼쪽 자식 노드가 대표하는 값 + 오른쪽 자식 노드가 대표하는 값
        sumTree[node] = sumTree[node * 2] + sumTree[node * 2 + 1];
    }

    // 세그먼트 트리 갱신 - point update
    public static void update(int node, int nodeLeft, int nodeRight, int queryIndex, int value) {
        // node : 현재 노드 번호
        // nodeLeft, Right : 현재 노드가 관리하는 영역
        // queryIndex : update 하고 싶은 배열의 index
        // value : update 하고 싶은 값

        // 현재 노드가 관리하는 영역에 queryIndex 가 없는 경우
        if (queryIndex < nodeLeft || nodeRight < queryIndex) {
            return;
        }

        if (nodeLeft == nodeRight) {
            sumTree[node] = value;
            return;
        }

        int mid = (nodeLeft + nodeRight) / 2;

        update(
                node * 2,
                nodeLeft,
                mid,
                queryIndex,
                value
        );

        update(
                node * 2 + 1,
                mid + 1,
                nodeRight,
                queryIndex,
                value
        );

        sumTree[node] = sumTree[node * 2] + sumTree[node * 2 + 1];
    }

    public static long querySum(int node, int nodeLeft, int nodeRight, int queryLeft, int queryRight) {
        // node : 현재 노드 번호
        // nodeLeft, Right : 현재 노드가 관리하는 영역
        // queryLeft, Right : Sum을 구하고 싶은 영역

        // 관심 없는 영역을 관리하는 노드라면, 무시하자.
        if (queryRight < nodeLeft || nodeRight < queryLeft) {
            return 0;  // 덧셈에 대한 항등원
        }

        // 뭉텅이로 처리할 수 있는 상태라면, 바로 처리하자 
        if (queryLeft <= nodeLeft && nodeRight <= queryRight) {
            return sumTree[node];
        }

        int mid = (nodeLeft + nodeRight) / 2;
        long leftSum = querySum(
                node * 2,
                nodeLeft,
                mid,
                queryLeft,
                queryRight
        );
        long rightSum = querySum(
                node * 2 + 1,
                mid + 1,
                nodeRight,
                queryLeft,
                queryRight
        );

        return leftSum + rightSum;
    }
}