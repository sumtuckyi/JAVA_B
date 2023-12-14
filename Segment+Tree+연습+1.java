import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Solution {

    static int n;
    static int[] a, maxTree, minTree;

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
            maxTree = new int[4 * n];
            minTree = new int[4 * n];

            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(st.nextToken());
            }

            init(1, 0, n - 1);

            for (int i = 0; i < m; i++) {
                st = new StringTokenizer(br.readLine());
                int query = Integer.parseInt(st.nextToken());
                int left = Integer.parseInt(st.nextToken());
                int right = Integer.parseInt(st.nextToken());

                if (query == 0) {
                    update(1, 0, n - 1, left, right);
                } else if (query == 1) {
                    int max = queryMax(1, 0, n - 1, left, right - 1);
                    int min = queryMin(1, 0, n - 1, left, right - 1);
                    sb.append(max - min).append(" ");
                }
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    // 재귀방식 이용 - bottom up방식 -> 구간의 길이가 1인 자식 노드의 값부터 구한 다음에 위로 올라가면서 부모노드를 채워줌
    // 세그먼트 트리 초기화 - 인자로 노드, 해당 노드가 대표하는 구간의 왼쪽값, 오른쪽값을 받음
    public static void init(int node, int nodeLeft, int nodeRight) {
        if (nodeLeft == nodeRight) { // 재귀함수 종료조건 - 길이 1인 구간을 나타내는 노드인 경우
            maxTree[node] = a[nodeLeft]; // 최댓값을 담고 있는 트리의 노드값을 갱신
            minTree[node] = a[nodeLeft];
            return;
        }

        // 자식노드로 넘어가기 전 구간 분할
        int mid = (nodeLeft + nodeRight) / 2;

        init(
                node * 2,
                nodeLeft,
                mid
        ); // 왼쪽 자식 노드의 값을 초기화

        init(
                node * 2 + 1,
                mid + 1,
                nodeRight
        ); // 오른쪽 자식 노드의 값을 초기화

        maxTree[node] = Math.max(maxTree[node * 2], maxTree[node * 2 + 1]); // 자식 노드의 값 중에서 더 큰 값을 저장
        minTree[node] = Math.min(minTree[node * 2], minTree[node * 2 + 1]);
    }

    // 세그먼트 트리 갱신 - 인자 : 쿼리인덱스에 있는 값을 value로 바꿔줘, node는 현재 위치의 노드
    public static void update(int node, int nodeLeft, int nodeRight, int queryIndex, int value) {
        if (queryIndex < nodeLeft || nodeRight < queryIndex) {
            return;
        } // 쿼리인덱스가 해당 노드가 담당하는 구간에 속하지 않는 경우(l보다 작고 r보다 큰)라면 리턴

        if (nodeLeft == nodeRight) {
            maxTree[node] = value;
            minTree[node] = value;
            return;
        } // 리프 노드인 경우, 해당 노드의 값을 갱신

        // 쿼리인덱스가 해당 노드가 대표하는 구간에 속하는 경우
        int mid = (nodeLeft + nodeRight) / 2;

        update(
                node * 2,
                nodeLeft,
                mid,
                queryIndex,
                value
        ); // 왼쪽 자식노드 재귀적 업데이트

        update(
                node * 2 + 1,
                mid + 1,
                nodeRight,
                queryIndex,
                value
        ); // 오른쪽 자식 노드 재귀적 업데이트

        maxTree[node] = Math.max(maxTree[node * 2], maxTree[node * 2 + 1]);
        minTree[node] = Math.min(minTree[node * 2], minTree[node * 2 + 1]);
    }

    // 최대값 쿼리
    public static int queryMax(int node, int nodeLeft, int nodeRight, int queryLeft, int queryRight) {
        if (queryRight < nodeLeft || nodeRight < queryLeft) {
            return 0;
        } // 구간이 겹치지 않는 경우, 현 조건에서의 max연산 항등원인 0을 반환

        if (queryLeft <= nodeLeft && nodeRight <= queryRight) {
            return maxTree[node];
        } // 노드가 대표하는 구간이 쿼리 구간에 포함되는 경우

        // 노드가 대표하는 구간과 쿼리 구간이 일부 중첩되는 경우 - 자식 노드에 대해 재귀호출 
        int mid = (nodeLeft + nodeRight) / 2;
        int leftMax = queryMax(
                node * 2,
                nodeLeft,
                mid,
                queryLeft,
                queryRight
        );
        int rightMax = queryMax(
                node * 2 + 1,
                mid + 1,
                nodeRight,
                queryLeft,
                queryRight
        );

        return Math.max(leftMax, rightMax);
    }

    // 최소값 쿼리
    static int queryMin(int node, int nodeLeft, int nodeRight, int queryLeft, int queryRight) {
        if (queryLeft > nodeRight || queryRight < nodeLeft) {
            return Integer.MAX_VALUE;
        } // 구간이 겹치지 않는 경우, 현 조건에서의 min연산 항등원인 Integer.MAX_VALUE을 반환

        if (queryLeft <= nodeLeft && nodeRight <= queryRight) {
            return minTree[node];
        }

        int mid = (nodeLeft + nodeRight) / 2;
        int leftMin = queryMin(
                node * 2,
                nodeLeft,
                mid,
                queryLeft,
                queryRight
        );
        int rightMin = queryMin(
                node * 2 + 1,
                mid + 1,
                nodeRight,
                queryLeft,
                queryRight
        );

        return Math.min(leftMin, rightMin);
    }


}