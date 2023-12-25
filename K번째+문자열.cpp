#include <iostream>
#include <map>
#include <string>

using namespace std;

class Trie {
public:
    char alphabet;                  // 이 정점으로 이동하는 알파벳
    bool isWordEnd;                 // 이 정점에서 끝나는 문자열이 존재하는 지 표현
    int cnt = 0;                    // 이 정점을 root로 하는 subtree에 포함된 문자열 개수
    map<char, Trie*> children;      // 각 문자에 대해 이동하는 다른 정점을 기억하는 Map

    Trie(char alphabet) : alphabet(alphabet), isWordEnd(false) {}

    Trie() : alphabet('\0'), isWordEnd(false) {}
};

int K;
char results[405];

int insert(const string& words, int idx, Trie* trie) {
    // words[idx] 번 문자를 trie에 삽입
    if (idx == words.length()) {
        return 0;
    }

    char alphabet = words[idx];

    int subCnt = 0;  // 이번에 새로 추가된 T의 개수
    if (trie->children.find(alphabet) == trie->children.end()) {
        Trie* newTrie = new Trie(alphabet);
        newTrie->cnt = 1;
        subCnt = 1;
        trie->children[alphabet] = newTrie;
    }

    subCnt += insert(words, idx + 1, trie->children[alphabet]);
    trie->cnt += subCnt;

    return subCnt;
}

void dfs(Trie* trie, int depth, int test_case) {
    if (K == 0)
        return;

    K--;
    if (K == 0) {  // 원하는 문자열에 도달했다면,
        string result = "";
        for (int i = 0; i < depth; i++) {
            result += results[i];
        }
        cout << "#" << test_case << " " << result << endl;
        return;
    }

    for (char i = 'a'; i <= 'z'; i++) {  // 낮은 알파벳부터 하나씩 이동한다.
        if (trie->children.find(i) != trie->children.end()) {  // 해당 알파벳으로 이동할 수 있다면,
            Trie* child = trie->children[i];
            if (child->cnt < K) {  // 해당 정점으로 이동하더라도 K 개의 문자열보다 적은 개수의 문자열이 있다면,
                K -= child->cnt;  // 빠르게 해당 개수만큼 skip 한다.
                continue;
            }

            results[depth] = i;
            dfs(trie->children[i], depth + 1, test_case);
            results[depth] = '\0';
        }
    }
}

void result(const string& str, int test_case) {
    cout << "#" << test_case << " " << str << endl;
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int T;
    cin >> T;

    for (int test_case = 1; test_case <= T; test_case++) {
        Trie* head = new Trie();
        cin >> K;
        K++;
        string words;
        cin >> words;
        int len = words.length();
        for (int i = 0; i < len; i++) {  // i : 부분문자열의 시작 위치
            insert(words, i, head);  // i에서 시작한 부분 문자열들을 삽입
        }
        results[len] = '\0';
        dfs(head, 0, test_case);
        if (K > 0) {
            result("none", test_case);
        }
    }

    return 0;
}
