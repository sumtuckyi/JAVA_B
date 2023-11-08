import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
 
class Solution
{
    static char[] arr;
    static int n;
    static StringBuilder sb = new StringBuilder();
    public static void main(String args[]) throws Exception
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         
        for(int tc = 1 ; tc <= 10 ; tc++) {
            sb.append("#" + tc + " ");
            // 한 줄의 데이터를 읽은 다음 이를 정수로 변환
            n = Integer.parseInt(br.readLine());
            // (n+1) 길이로 배열 초기화
            arr = new char[n + 1];
            // n줄 입력받기 
            for(int i = 1 ; i <= n ; i++) {
                // 입력한 줄에서 처음으로 나오는 문자만 저장
                arr[i] = br.readLine().split(" ")[1].charAt(0);
            }
            // 매개변수를 루트노드로 삼아 중위순회 진행
            dfs(1);
            sb.append("\n"); // 다음 테케 출력 전에 줄바꿈
        }
        System.out.println(sb);
    }
     
    public static void dfs(int cur) {  // cur := 현재 탐색 중인 정점의 번호
 
        if(cur > n) return; // 존재하지 않는 정점이라면 리턴
         
        dfs(cur*2); // 왼쪽 자식을 루트노드로 하여 중위순회
        sb.append(arr[cur]); // 부모 노드를 정답 배열에 추가
        dfs(cur*2 + 1); // 오른쪽 자식을 루트노드로 하여 중위순회
    }
}