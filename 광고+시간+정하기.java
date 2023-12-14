import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Solution {
    static class Time {
        int s;
        int e;

        public Time(int s, int e) {
            this.s = s;
            this.e = e;
        }
    }

    static Time[] arr;
    static int[] sum;
    static int N;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int test_case = 1; test_case <= T; test_case++) {
            StringTokenizer st;
            int L = Integer.parseInt(br.readLine());
            int result = -1;
            N = Integer.parseInt(br.readLine());
            arr = new Time[N]; // arr[i] := i 번 광고의 시작, 끝 정보
            sum = new int[N];  // sum[i] := 첫 i 개 광고들의 총 시간

            for (int i = 0; i < N; i++) {
                st = new StringTokenizer(br.readLine());
                int s = Integer.parseInt(st.nextToken());
                int e = Integer.parseInt(st.nextToken());
                arr[i] = new Time(s, e);

                if (i == 0) {
                    sum[i] = e - s;
                } else {
                    sum[i] = sum[i - 1] + (e - s);
                }
            }

            for (int i = 0; i < N; i++) {
                int target = arr[i].s + L;      // i 번 광고 시작부터 L 시간 동안 틀면 (arr[i].s + L) 까지 광고
                int ub = upperBound(target);    // target보다 늦게 끝나는 첫 번째 광고 찾기
                
                int temp = sum[ub - 1];         // 광고가 나가는 시간
                if (i != 0) temp -= sum[i - 1];

                if (ub != N && target > arr[ub].s) temp += (target - arr[ub].s);  // 마지막 광고가 나가는 시간 누적

                result = Math.max(result, temp);
            }

            System.out.println("#" + test_case + " " + result);
        }
    }

    static int upperBound(int target) {
        int start = 0;
        int end = N - 1;
        int answer = N;

        while (start <= end) {
            int mid = (start + end) / 2;

            if (arr[mid].e > target) {
                answer = mid;
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }

        return answer;
    }
}