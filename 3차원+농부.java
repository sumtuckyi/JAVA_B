import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Scanner;

class Solution {

    public static void main(String args[]) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        int T = Integer.parseInt(st.nextToken());
        int N, M;
        for (int test_case = 1; test_case <= T; test_case++) {
            st = new StringTokenizer(br.readLine(), " ");
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            st = new StringTokenizer(br.readLine(), " ");
            int dx = Math.abs(Integer.parseInt(st.nextToken()) - Integer.parseInt(st.nextToken()));

            int[] cows = new int[N];
            st = new StringTokenizer(br.readLine(), " ");
            for (int i = 0; i < N; i++) {
                cows[i] = Integer.parseInt(st.nextToken());
            }
            Arrays.sort(cows);

            int min = Integer.MAX_VALUE;
            int count = 0;

            st = new StringTokenizer(br.readLine(), " ");
            for (int i = 0; i < M; i++) {
                int hPos = Integer.parseInt(st.nextToken());
                int X = binSearch(cows, hPos);  // X := hPos 이상인 소들 중에서 제일 왼쪽 소

                // 1. hPos 와 cows[X] 사이의 거리
                if (X < cows.length) {  // hPos 오른쪽에 소가 "존재" 한다면, 그 소와의 거리 갱신
                    int dist = cows[X] - hPos;
                    if (min == dist) {  // 알고 있던 최소 거리랑 같다면, 경우의 수 증가시키기
                        count ++;
                    } else if (min > dist) {  // 알고있던 최소 거리보다 더 좋다면, 
                        min = dist;  // 해당 값으로 갱신
                        count = 1;   // 1번 있다.
                    }
                }

                // 2. hPos 와 cows[X - 1] 사이의 거리
                if (X - 1 >= 0) {  // hPos 왼쪽에 소가 "존재" 한다면, 그 소와의 거리 갱신
                    int dist = hPos - cows[X - 1];
                    if (min == dist) {  // 알고 있던 최소 거리랑 같다면, 경우의 수 증가시키기
                        count ++;
                    } else if (min > dist) {  // 알고있던 최소 거리보다 더 좋다면, 
                        min = dist;  // 해당 값으로 갱신
                        count = 1;   // 1번 있다.
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

        while (L <= R){
            mid = (L + R) / 2;
            if (arr[mid] >= value) {  // value 이상인 위치를 보고 있다면
                ans = mid;
                R = mid - 1;
            } else {  // value 미만인 위치를 보고 있다면
                L = mid + 1;
            }
        }

        return ans;
    }
}