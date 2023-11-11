import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class 프로세서연결하기 {
    static int[][] map;
    static List<int[]> coreList;
    static int ans, maxCnt, N;
    // maxCnt : 탐색한 방법 중, 연결한 최대 코어 개수
    // ans    : 최대 코어 연결 방법 중 최소 전선 길이
    static final int[] dr = {-1, 0, 1, 0};
    static final int[] dc = {0, -1, 0, 1};
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static void input() throws Exception {
        N = Integer.parseInt(br.readLine());
        map = new int[N][N];
        coreList = new ArrayList<>();
        StringTokenizer st;
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                if (i == 0 || j == 0 || i == N - 1 || j == N - 1) continue;
                if (map[i][j] == 1) coreList.add(new int[]{i, j});
            }
        }
        ans = 0;
        maxCnt = 0;
    }

    private static boolean inRange(int r, int c) {
        return 0 <= r && 0 <= c && r < N && c < N;
    }

    private static int extend(int[] point, int d) {
        int r = point[0] + dr[d], c = point[1] + dc[d];
        int res = 0;
        while (inRange(r, c)) {
            if (map[r][c] != 0){
                return -1;
            }
            r += dr[d];
            c += dc[d];
        }
        r = point[0] + dr[d];
        c = point[1] + dc[d];
        while (inRange(r, c)) {
            map[r][c] = 2;
            r += dr[d];
            c += dc[d];
            res++;
        }
        return res;
    }

    private static void rollback(int[] point, int d) {
        int r = point[0] + dr[d], c = point[1] + dc[d];
        while (inRange(r, c)) {
            map[r][c] = 0;
            r += dr[d];
            c += dc[d];
        }
    }

    private static void backtracking(int idx, int length, int cnt) {
        // idx: 이번에 선택한 코어의 번호
        // length: 지금까지 사용한 전선의 총 길
        // cnt: 지금까지 연결한 코어의 총 개수
        if (cnt > maxCnt) {
            maxCnt = cnt;
            ans = length;
        } else if (cnt == maxCnt) {
            ans = Math.min(ans, length);
        }
        if (idx == coreList.size()) {
            return;
        }

        for (int d = 0; d < 4; d++) {
            int nr = coreList.get(idx)[0] + dr[d];
            int nc = coreList.get(idx)[1] + dc[d];

            int wireLen = extend(coreList.get(idx), d);
            if (wireLen == -1) {
                continue;
            }
            backtracking(idx + 1, length + wireLen, cnt + 1);
            rollback(coreList.get(idx), d);
        }

        backtracking(idx + 1, length, cnt);
    }

    private static void output(int t) {
        System.out.println("#" + t + " " + ans);
    }


    public static void main(String[] args) throws Exception {
        int T = Integer.parseInt(br.readLine());
        for (int t = 1; t <= T; t++) {
            input();
            backtracking(0, 0, 0);
            output(t);
        }
    }
}
