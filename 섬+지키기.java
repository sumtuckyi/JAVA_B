import java.util.*;

class UserSolution {
    // 주어진 섬의 최대 너비
    public final int MAX_N = 20;
    // 각 구조물을 해시로 맵핑할 때 나올 수 있는 최댓값
    public final int MAX_HASH = 9999;

    public int n;
    // 2차원 배열 초기화 - 테두리에 패딩 만들기(해수면이 오르는 상황 시뮬레이션 시에 이용)
    public int[][] initMap = new int[MAX_N + 2][MAX_N + 2];
    // 구조물이 1개 설치된 경우를 상정하는 가상의 맵
    public int[][] modifiedMap = new int[MAX_N + 2][MAX_N + 2];

    // 구조물 설치 기준 좌표와 설치시 가로/세로 모양인지, reversed인지 여부를 모두 저장할 것
    public class Candidate {
        // 속성 정의
        int r; // x좌표
        int c; // y좌표
        boolean isHorizontal; // 가로/세로
        boolean isReverse; // 정방향인지 여부

        // 해당 클래스의 생성자
        public Candidate(int r, int c, boolean isHorizontal, boolean isReverse) {
            this.r = r;
            this.c = c;
            this.isHorizontal = isHorizontal;
            this.isReverse = isReverse;
        }
    }
    // ?리스트 선언
    public List<Candidate>[] candidate = new List[MAX_HASH + 1];

    // 한 번만 호출되지만 최대한 많은 전처리 작업을 수행하도록 함 
    public void init(int N, int[][] mMap) {
        n = N;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                // 테두리는 비움, 초기상태 입력받기
                modifiedMap[i + 1][j + 1] = initMap[i + 1][j + 1] = mMap[i][j];
        // 패턴을 저장할 배열 생성 
        for (int i = 0; i <= MAX_HASH; i++)
            candidate[i] = new ArrayList<>();

        for (int length = 2; length <= 5; length++) { // M의 길이별로 모두 읽기
            // 가로 방향으로 구하기 
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j + length - 1 <= n; j++) {
                    int hash = 0;
                    for (int k = 0; k + 1 < length; k++) // 구조물의 길이가 2면 뺄셈 1번, 길이가 5면 4번
                        // 오른쪽 수에서 왼쪽 수를 빼고 5를 더해 해시값(구조물 패턴을 식별) 만들기
                        hash = hash * 10 + (initMap[i][j + k + 1] - initMap[i][j + k] + 5);
                    // 해당 구조물을 세울 수 있는 좌표를 모두 저장 
                    candidate[hash].add(new Candidate(i, j, true, false));

                    // 구조물을 180도 돌려서 세우는 경우를 고려
                    int reverseHash = 0;
                    for (int k = length - 1; k - 1 >= 0; k--)
                        // 왼쪽 수에서 오른쪽 수를 빼고 5를 더해 해시값 만들기
                        reverseHash = reverseHash * 10 + (initMap[i][j + k - 1] - initMap[i][j + k] + 5);
                    if (reverseHash != hash) // 뒤집어도 같은 경우는 카운트에서 제외
                        candidate[reverseHash].add(new Candidate(i, j, true, true));
                }
            }
            // 세로 방향으로 구하기
            for (int i = 1; i + length - 1 <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    int hash = 0;
                    for (int k = 0; k + 1 < length; k++)
                        hash = hash * 10 + (initMap[i + k + 1][j] - initMap[i + k][j] + 5);
                    candidate[hash].add(new Candidate(i, j, false, false));

                    // 역방향(세로로 된 구조물을 180도 돌리는 경우)
                    int reverseHash = 0;
                    for (int k = length - 1; k - 1 >= 0; k--)
                        reverseHash = reverseHash * 10 + (initMap[i + k - 1][j] - initMap[i + k][j] + 5);
                    if (reverseHash != hash)
                        candidate[reverseHash].add(new Candidate(i, j, false, true));
                }
            }
        }
    }
    // 가볍고 빠른 작업을 처리하게 함
    public int numberOfCandidate(int M, int[] mStructure) {
        if (M == 1) // 구조물의 길이가 1이면 어떤 자리에도 넣을 수 있음
            return n * n;

        int hash = 0;
        // 인자로 받은 구조물의 해시값 구하기
        for (int i = 0; i + 1 < M; i++)
            hash = hash * 10 + (mStructure[i] - mStructure[i + 1] + 5);
        // 해당 구조물이 설치될 수 있는 경우의 수 리턴
        return candidate[hash].size();
    }

    // 물에 잠겼는지 여부를 나타내는 배열
    public boolean[][] check = new boolean[MAX_N + 2][MAX_N + 2];
    public int[] dx = {1, 0, -1, 0};
    public int[] dy = {0, 1, 0, -1};
    
    // 잠기지 않는 구역의 개수를 세기(bfs, 플러드필) - 인자로 구조물을 설치했을 때의 가상의 맵과 해수면 높이를 받음
    public int unsubmergedArea(int[][] mMap, int mSeaLevel) {
        Queue<int[]> q = new LinkedList<>();
        for (int i = 0; i <= n + 1; i++) {
            for (int j = 0; j <= n + 1; j++) {
                // 테두리의 노드를 전부 큐에 추가
                if (i == 0 || i == n + 1 || j == 0 || j == n + 1) {
                    q.add(new int[]{i, j});
                    check[i][j] = true;
                } else
                    check[i][j] = false;
            }
        }
        while (!q.isEmpty()) {
            // 큐에서 노드를 하나 꺼내서
            int[] front = q.poll();
            // 4방향 bfs탐색
            for (int i = 0; i < 4; i++) {
                // 해당 노드를 중심으로 상하좌우 노드의 값을 탐색
                int[] rear = {front[0] + dx[i], front[1] + dy[i]}; // 정수배열을 만들고 초기화
                // 탐색할 노드가 범위 내에 있으면서
                if (rear[0] >= 1 && rear[0] <= n && rear[1] >= 1 && rear[1] <= n) {
                    // 아직 방문되지 않았고, 가상의 맵에서 해수면보다 고도가 낮은 경우
                    if (!check[rear[0]][rear[1]] && mMap[rear[0]][rear[1]] < mSeaLevel) {
                        q.add(rear); // 큐에 추가
                        check[rear[0]][rear[1]] = true; // 방문처리
                    }
                }
            }
        }
        int ret = 0;
        // 잠기지 않은 지역의 수를 카운트하고 리턴
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                if (!check[i][j])
                    ret++;
        return ret;
    }

    // 해수면이 mSeaLevel만큼 상승하였고 mStructure 한 개를 설치하였을 때, 잠기지 않은 지역의 수가 최대인 경우 그 값
    public int maxArea(int M, int[] mStructure, int mSeaLevel) {
        int ret = -1;
        // 구조물의 크기가 1인 경우 
        if (M == 1) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    // 각 지역의 고도를 구조물 설치 이후의 고도로 변경
                    modifiedMap[i][j] = initMap[i][j] + mStructure[0];
                    // 그 경우에 잠기지 않는 지역의 수 중 최댓값을 갱신 
                    ret = Math.max(ret, unsubmergedArea(modifiedMap, mSeaLevel));
                    // 원상복구
                    modifiedMap[i][j] = initMap[i][j];
                }
            }
            return ret; // 최댓값 리턴
        }

        int hash = 0;
        // 해당 구조물의 해시값 구하기 
        for (int i = 0; i + 1 < M; i++)
            hash = hash * 10 + (mStructure[i] - mStructure[i + 1] + 5);
        // 해당 구조물을 세울 수 있는 모든 경우의 수에 대하여 각 경우마다 잠기지 않는 지역의 수가 얼마인지 탐색
        for (Candidate wall : candidate[hash]) { // ex) wall = {x, y, t, f}
            if (wall.isHorizontal) { // 가로방향으로 세울 수 있는 경우
                // 구조물을 세울 경우 새롭게 바뀌는 고도 - 한 칸만 계산하면 됨 : 역방향으로 세울 수 있는 거면 기준 좌표에서 (M-1)만큼 오른쪽으로 이동해서 그 값에 더해주고 아니면 기준 좌표 값에 더해줌
                int height = mStructure[0] + (wall.isReverse ? initMap[wall.r][wall.c + M - 1] : initMap[wall.r][wall.c]);
                for (int i = 0; i < M; i++) // 가상의 맵에서 구조물을 세울 지점들의 고도만 바꿔줌
                    modifiedMap[wall.r][wall.c + i] = height; 
                // 가상의 맵으로 시뮬레이션 돌려서 잠기지 않는 지역의 최댓값 갱신
                ret = Math.max(ret, unsubmergedArea(modifiedMap, mSeaLevel));
                // 원상복구
                for (int i = 0; i < M; i++)
                    modifiedMap[wall.r][wall.c + i] = initMap[wall.r][wall.c + i];
            } else { // 세로방향으로 세울 수 있는 경우도 마찬가지
                int height = mStructure[0] + (wall.isReverse ? initMap[wall.r + M - 1][wall.c] : initMap[wall.r][wall.c]);
                for (int i = 0; i < M; i++)
                    modifiedMap[wall.r + i][wall.c] = height;
                ret = Math.max(ret, unsubmergedArea(modifiedMap, mSeaLevel));
                for (int i = 0; i < M; i++)
                    modifiedMap[wall.r + i][wall.c] = initMap[wall.r + i][wall.c];
            }
        }
        return ret; // 최댓값 리턴
    }
}
