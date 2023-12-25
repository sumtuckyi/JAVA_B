import java.util.Scanner;
import java.lang.Math;

public class Solution {
    static int n, p;
    static int[] a; // 공부한 날짜를 담을 배열

    public static int solve() {
        int ans = 0; // 내가 찾은 최장 스트릭 길이
        for (int i = 0; i < n; i++) { // i := 공부를 시작한 날짜의 인덱스
            // a[i] 에 공부를 시작한 상태

            int L = i, R = n - 1, mid;
            while (L <= R) {
                mid = (L + R) / 2;
                int blank = (a[mid] - a[i] + 1) - (mid - i + 1); // 빈 날짜 개수 = 해킹이 필요한 일수
                if (blank > p) { // 불가능
                    R = mid - 1;
                } else { // 가능
                    L = mid + 1;
                    // (a[mid] - a[i] + 1) = 해당 기간의 일수, (p - blank) = 남은 해킹 가능한 일수
                    ans = Math.max(ans, (a[mid] - a[i] + 1) + (p - blank));
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int maximum_date = 1000001;
        int T = sc.nextInt(); // 테스트케이스
        for (int test_case = 1; test_case <= T; test_case++) {
            n = sc.nextInt(); // 공부를 한 날
            p = sc.nextInt(); // 해킹을 할 수 있는 기회
            a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = sc.nextInt();
            }
            int ans = solve();
            System.out.println("#" + test_case + " " + ans);
        }
    }
}