import java.io.BufferedReader;
import java.io.InputStreamReader;


public class 촛불이벤트 {

    static long ans; // 각 테스트케이스의 정답
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


    private static void output(int t) {
        System.out.println("#" + t + " " + ans);
    } // 테스트케이스 번호를 인자로 받아 답을 출력
    
    
    public static void main(String[] args) throws Exception {
        int T = Integer.parseInt(br.readLine()); // br.readLine()으로 읽어온 스트링을 정수로 변환
        for(int t = 1; t <= T; t++) {
            // 이분탐색 로직
            boolean tof = false;
            long N = Long.parseLong(br.readLine()); // 양초의 개수
            long start = 1; // 이분탐색 시작점
            long end = (long) Math.sqrt(2*N); // 이분탐색 끝점
            System.out.println("n의 값" + N + " " + start + " " + end);
            while (start <= end) {
                long mid = (start + end) / 2;
                System.out.println("mid" + " " + mid);
                System.out.println(mid*(mid+1));
                if (mid*(mid + 1) == 2 * N) {
                    ans = mid;
                    tof = true;
                    output(t);
                    break;
                } else if (mid*(mid + 1) < 2 * N) {
                    System.out.println("목표값보다 작음 " + mid*(mid + 1));
                    start = mid + 1;
                    System.out.println("start"+ " " + start);

                } else {
                    System.out.println("목표값보다 큼");
                    end = mid - 1;
                }
            }
            // 반복문을 다 돌았는데도 조건을 만족하는 자연수가 존재하지 않는 경우
            if (!tof) {
                ans = -1;
                output(t);
            }
        }
    }
}