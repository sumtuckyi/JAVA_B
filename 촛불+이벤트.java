import java.util.Scanner;
import java.io.FileInputStream;

class Solution
{
    public static void main(String args[]) throws Exception
    {
        Scanner sc = new Scanner(System.in);
        int T;
        T=sc.nextInt();

        for(int test_case = 1; test_case <= T; test_case++)
        {

            long N = sc.nextLong();

            long L = 1;             // 정답이 존재하는 가능성이 있는 왼쪽 끝
            long R = 10000000000L;  // 정답이 존재하는 가능성이 있는 오른쪽 끝
            long res = 0;           // 내가 찾은 최고의 정답
            while(L <= R) {
                long mid = (L + R) / 2;
                long value = mid * (mid + 1) / 2;

                if(N >= value) {    // * 가능한 케이스
                    res = mid;      // 정답을 갱신
                    L = mid + 1;    // 오른쪽 절반에 대해 추가 탐색
                } else {            // * 불가능한 케이스
                    R = mid - 1;    // 왼쪽 절반에 대해 추가 탐색
                }
            }

            long value = res * (res + 1) / 2;
            if(N != value) {
                res = -1;
            }
            System.out.printf("#%d %d\n", test_case, res);

        }
    }
}