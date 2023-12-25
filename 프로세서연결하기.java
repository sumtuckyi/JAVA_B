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
    // ans : 최대 코어 연결 방법 중 최소 전선 길이
    static final int[] dr = { -1, 0, 1, 0 }; // 좌상우하 순서로 4방향 탐색
    static final int[] dc = { 0, -1, 0, 1 };
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static void input() throws Exception {
        N = Integer.parseInt(br.readLine()); // 맵의 크기
        map = new int[N][N];
        coreList = new ArrayList<>(); // 아직 전원이 연결되지 않은 코어의 좌표
        StringTokenizer st;
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine(), " "); // 맵의 한 행
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                if (i == 0 || j == 0 || i == N - 1 || j == N - 1)
                    continue; // 가장자리에 있는 코어는 패스
                if (map[i][j] == 1)
                    coreList.add(new int[] { i, j }); // 그렇지 않다면 좌표를 저장
            }
        }
        ans = 0;
        maxCnt = 0;
    }

    // (r, c)가 맵의 범위 내에 있는지 판별
    private static boolean inRange(int r, int c) {
        return 0 <= r && 0 <= c && r < N && c < N;
    }

    //
    private static int extend(int[] point, int d) { // 코어의 좌표, 방향을 인자로 받음 => d방향으로 연결 시도
        int r = point[0] + dr[d], c = point[1] + dc[d];
        int res = 0;
        while (inRange(r, c)) { // (r, c)가 맵의 범위 내에 있는 한, 반복
            if (map[r][c] != 0) { // (r, c)위치에 다른 코어가 존재하는 경우
                return -1;
            }
            // 그렇지 않다면 전진
            r += dr[d];
            c += dc[d];
        }
        // 반복문 종료 => 가장자리에 도착할 때까지 다른 코어를 만나지 않은 경우 = 해당 코어와 가장자리가 전선으로 연결가능함
        // 현재 좌표값을 초기화
        r = point[0] + dr[d];
        c = point[1] + dc[d];
        // 범위 내에 있는 한, 전선으로 연결된 상태를 맵에서 2로 표현
        while (inRange(r, c)) {
            map[r][c] = 2;
            r += dr[d];
            c += dc[d];
            res++; // 전선의 길이에 더해주기
        }
        return res; // 연결에 성공한 경우에는 사용한 전선의 길이를 리턴
    }

    private static void rollback(int[] point, int d) { // point좌표로부터 d방향으로의 연결을 되돌리기
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
        if (cnt > maxCnt) { // 현재까지 연결한 코어의 총 개수가 기존 최댓값보다 크다면 갱신
            maxCnt = cnt;
            ans = length;
        } else if (cnt == maxCnt) { // 같다면
            ans = Math.min(ans, length); // 전선의 총 길이 중 최솟값으로 갱신
        }
        if (idx == coreList.size()) { // 재귀 중단조건 - 모든 코어를 선택한 경우
            return;
        }
        // idx번 코어 기준, 4방향 탐색
        for (int d = 0; d < 4; d++) {
            int nr = coreList.get(idx)[0] + dr[d];
            int nc = coreList.get(idx)[1] + dc[d];

            int wireLen = extend(coreList.get(idx), d); // d방향으로 연결한 경우, 사용한 전선의 길이를 리턴
            if (wireLen == -1) { // 해당 방향으로 연결이 불가능한 경우
                continue;
            }
            // 연결이 가능하다면, 연결하고 다음 코어를 선택
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
            input(); // 맵 정보 입력받기
            backtracking(0, 0, 0); // 정답 도출하기
            output(t); // 정답 출력
        }
    }
}
