import java.util.*;

class Trie { // 트리 구조의 개별 노드
    char alphabet; // 이 정점으로 이동하는 알파벳 = 간선에 대응되는 알파벳
    boolean isWordEnd; // 이 정점에서 끝나는 문자열이 존재하는 지 표현
    int cnt; // 이 정점을 root로 하는 subtree에 포함된 문자열 개수

    Map<Character, Trie> children = new HashMap<Character, Trie>(); // 각 문자에 대해 이동하는 다른 정점을 기억하는 HashMap

    Trie(char alphabet) {
        this.alphabet = alphabet;
        this.cnt = 0;
    }

    Trie() {
    }
}

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int test_case = 1; test_case <= T; test_case++) {
            Trie head = new Trie();
            K = sc.nextInt(); // 사전 순서로 K번째 문자열 찾기
            String words = sc.next(); // 새로운 단어
            int len = words.length();

            if (K > len) {
                print("none", test_case);
                continue;
            } // 예외 처리

            // 단어 집합으로 트리 채우기
            for (int i = 0; i < len; i++) { // i 번째 문자에서 시작하는 접미열을 Trie에 반영 => len개의 단어만큼 반복
                Trie indexTrie = head; // 루트 노드를 참조하는 새로운 변수 선언

                for (int j = i; j < len; j++) { // j 번째 문자로 이동하기
                    char alphabet = words.charAt(j);

                    if (!indexTrie.children.containsKey(alphabet)) { // 새로운 문자라면 정점 추가하기
                        Trie newTrie = new Trie(alphabet); // 새로운 노드 생성
                        indexTrie.children.put(alphabet, newTrie); // 생성한 노드를 루트 노드의 자식으로 추가
                    }
                    indexTrie = indexTrie.children.get(alphabet); // 루트에서 한 단계 하위로 이동 = 루트노드의 자식 노드 중 일치하는 노드를 가져와
                    indexTrie.cnt++; // 해당 노드를 루트로 하는 서브트리의 단어 개수 증가 = 하위 문자열 개수 증가
                }
                // 한 단어의 마지막 문자열을 나타내는 노드의 isWordEnd 값만 바꿔주기
                indexTrie.isWordEnd = true;
            }
            results = new char[len];
            dfs(head, 0, test_case); // 트리 구성을 마쳤으면 K번째 단어를 탐색
        }
    }

    static char[] results; // 이동하는 경로 위에 놓인 문자
    static int K;

    // trie := 현재 방문 중인 정점
    // depth := 현재 깊이
    // test_case := 테스트 케이스 번호
    public static void dfs(Trie trie, int depth, int test_case) {
        if (K == 0)
            return;

        if (trie.isWordEnd) { // 해당 정점에서 끝나는 단어가 있다면
            K--; // 단어의 개수 카운트
            if (K == 0) { // 원하는 문자열에 도달했다면,
                String result = "";
                for (int i = 0; i < depth; i++) {
                    result += results[i];
                }
                print(result, test_case); // 정답 출력
                return;
            }
        }
        // 해당 정점이 단어의 끝이 아니라면
        for (char i = 'a'; i <= 'z'; i++) { // 낮은 알파벳부터 하나씩 이동한다.
            if (trie.children.containsKey(i)) { // 해당 알파벳으로 이동할 수 있다면,
                Trie child = trie.children.get(i); // 하위 노드로 이동
                if (child.cnt < K) { // 해당 정점으로 이동하더라도 K 개의 문자열보다 적은 개수의 문자열이 있다면,
                    K -= child.cnt; // 빠르게 해당 개수만큼 skip 한다.(cnt변수를 사용하는 이유)
                    continue;
                }

                results[depth] = i;
                dfs(child, depth + 1, test_case);
                results[depth] = '_'; // ?
            }
        }
    }

    // 결과 출력
    public static void print(String str, int test_case) {
        System.out.println("#" + test_case + " " + str);
    }
}