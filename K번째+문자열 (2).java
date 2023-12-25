import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Trie {
    char alphabet;      // 이 정점으로 이동하는 알파벳
    boolean isWordEnd;  // 이 정점에서 끝나는 문자열이 존재하는 지 표현
    int cnt = 0;        // 이 정점을 root로 하는 subtree에 포함된 문자열 개수

    Map<Character, Trie> children = new HashMap<Character, Trie>(); // 각 문자에 대해 이동하는 다른 정점을 기억하는 HashMap

    Trie(char alphabet) {
        this.alphabet = alphabet;
    }

    Trie() {
    }

}

public class Solution {
    public static int insert(String words, int idx, Trie trie) {
        // words[idx] 번 문자를 trie에 삽입
        if (idx == words.length()) {
            return 0;
        }

        char alphabet = words.charAt(idx);

        int subCnt = 0;  // 이번에 새로 추가된 T 의 개수
        if (!trie.children.containsKey(alphabet)) {
            Trie newTrie = new Trie(alphabet);
            newTrie.cnt = 1;
            subCnt = 1;
            trie.children.put(alphabet, newTrie);
        }

        subCnt += insert(words, idx + 1, trie.children.get(alphabet));
        trie.cnt += subCnt;

        return subCnt;
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int test_case = 1; test_case <= T; test_case++) {
            Trie head = new Trie();
            K = sc.nextInt() + 1;
            String words = sc.next();
            int len = words.length();
            for (int i = 0; i < len; i++) {  // i : 부분문자열의 시작 위치
                Trie indexTrie = head;
                insert(words, i, head);  // i에서 시작한 부분 문자열들을 삽입해줘!
            }
            results = new char[len];
            index = 0;
            dfs(head, 0, test_case);
            if (K>0) {
                result("none", test_case);
            }
        }
    }

    static char[] results;
    static int index;
    static int K;

    // trie := 현재 방문 중인 정점
    // depth := 현재 깊이
    // test_case := 테스트 케이스 번호
    public static void dfs(Trie trie, int depth, int test_case) {
        if (K == 0) return;
        
        K--;
        if (K == 0) {   // 원하는 문자열에 도달했다면,
            String result = "";
            for (int i = 0; i < depth; i++) {
                result += results[i];
            }
            result(result, test_case);
            return;
        }
        
        for (char i = 'a'; i <= 'z'; i++) { // 낮은 알파벳부터 하나씩 이동한다.
            if (trie.children.containsKey(i)) { // 해당 알파벳으로 이동할 수 있다면,
                Trie child = trie.children.get(i);
                if (child.cnt < K){ // 해당 정점으로 이동하더라도 K 개의 문자열보다 적은 개수의 문자열이 있다면,
                    K -= child.cnt; // 빠르게 해당 개수만큼 skip 한다.
                    continue;
                }

                results[depth] = i;
                dfs(trie.children.get(i), depth + 1, test_case);
                results[depth] = '_';
            }
        }
    }

    //결과 출력
    public static void result(String str, int test_case) {
        System.out.println("#" + test_case + " " + str);
    }
}