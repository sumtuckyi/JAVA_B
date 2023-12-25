import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

class Solution {

    public static void main(String args[]) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        int T = Integer.parseInt(st.nextToken());
        int N, M;
        for (int test_case = 1; test_case <= T; test_case++) {
            st = new StringTokenizer(br.readLine(), " "); // N, M 입력받기
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            st = new StringTokenizer(br.readLine(), " "); // c1, c2 입력받기
            int dx = Math.abs(Integer.parseInt(st.nextToken()) - Integer.parseInt(st.nextToken()));

            int[] cows = new int[N];
            st = new StringTokenizer(br.readLine(), " "); // 소들의 z좌표
            for (int i = 0; i < N; i++) {
                cows[i] = Integer.parseInt(st.nextToken());
            }
            Arrays.sort(cows);

            int min = Integer.MAX_VALUE;
            int count = 0;

            st = new StringTokenizer(br.readLine(), " "); // 말들의 z좌표
            for (int i = 0; i < M; i++) { // 각 말마다 이분탐색 실시
                int hPos = Integer.parseInt(st.nextToken()); // 말의 개별 z좌표
                int X = binSearch(cows, hPos); // X := hPos 이상인 소들 중에서 제일 왼쪽 소
                bw.write("index" + X + "\n");
                // 1. hPos 와 cows[X] 사이의 거리
                // X < cows.length인 경우 => value와 일치하거나 더 큰 값이 존재한다는 것(=cows[X])
                if (X < cows.length) { // hPos 오른쪽에 소가 "존재" 한다면, 그 소와의 거리 갱신
                    int dist = cows[X] - hPos;
                    if (min == dist) { // 알고 있던 최소 거리랑 같다면, 경우의 수 증가시키기
                        count++;
                    } else if (min > dist) { // 알고있던 최소 거리보다 더 좋다면,
                        min = dist; // 해당 값으로 갱신
                        count = 1; // 1번 있다.
                    }
                }

                // 2. hPos 와 cows[X - 1] 사이의 거리
                // 이분탐색으로 찾은 값의 바로 왼쪽값이랑도 차이를 구해서 갱신
                // X - 1 >= 0 => 왼쪽값의 인덱스가 유효한 범위인 경우에만
                if (X - 1 >= 0) { // hPos 왼쪽에 소가 "존재" 한다면, 그 소와의 거리 갱신
                    int dist = hPos - cows[X - 1];
                    if (min == dist) { // 알고 있던 최소 거리랑 같다면, 경우의 수 증가시키기
                        count++;
                    } else if (min > dist) { // 알고있던 최소 거리보다 더 좋다면,
                        min = dist; // 해당 값으로 갱신
                        count = 1; // 1번 있다.(카운트 초기화)
                    }
                }
            }

            bw.write("#" + test_case + " " + (dx + min) + " " + count + "\n");
        }

        bw.flush();
        bw.close();

    }

    private static int binSearch(int[] arr, int value) {

        int ans = arr.length;

        int L = 0, R = arr.length - 1, mid = 0;

        while (L <= R) {
            mid = (L + R) / 2;
            if (arr[mid] >= value) { // value 이상인 위치를 보고 있다면
                ans = mid;
                R = mid - 1;
            } else { // value 미만인 위치를 보고 있다면
                L = mid + 1;
            }
        }
        // value(찾고자하는 값)보다 크거나 같은 값을 찾지 못한 경우 N이 리턴됨
        return ans;
    }
}