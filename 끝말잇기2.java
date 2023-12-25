import java.util.HashSet;
import java.util.TreeSet;

class UserSolution {
    public static class Cycle {
        public int[] prv, nxt;  // prv[i] := i 번 사람의 이전 순서, nxt[i] := i 번 사람의 다음 순서
        public int n, cur;      // n := 전체 사람의 수, cur := 현재 순서

        public Cycle(int n) {
            this.n = n;
            prv = new int[n + 1];
            nxt = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                prv[i] = i - 1;
                if (prv[i] == 0) prv[i] = n;
                nxt[i] = i + 1;
                if (nxt[i] == n + 1) nxt[i] = 1;
            }
        }

        public void setCur(int id) {
            cur = id;
        }

        public void move() {  // 다음 순서로 이동하기, O(1)
            cur = nxt[cur];
        }

        public int pop() {  // O(1) - 탈락자 결정 이후 처리
            nxt[prv[cur]] = nxt[cur];
            prv[nxt[cur]] = prv[cur];
            return cur;
        }
    }

    TreeSet<String>[] wordSet;      // wordSet[c]  := c 로 시작하는 사용 가능한 단어 집합
    HashSet<String> usedWordSet;    // usedWordSet := 사용한 적이 있는 단어 집합
    String[] newWords;              // 이번 라운드에 의해 추가될 단어들
    int totalPlayer;
    Cycle cycle;

    // 자료구조 초기화하기
    public void init(int N, int M, char[][] mWords) {
        totalPlayer = N;
        wordSet = new TreeSet[26]; // 알파벳 종류별로 26개 만들기
        usedWordSet = new HashSet<>();
        newWords = new String[M];
        for (int i = 0; i < 26; i++) {
            wordSet[i] = new TreeSet<>();
        }

        for (int i = 0; i < M; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < mWords[i].length; j++) {
                if (mWords[i][j] == '\0') break;
                sb.append(mWords[i][j]);
            }
            wordSet[sb.charAt(0) - 'a'].add(sb.toString());  // 시작하는 알파벳 집합에 추가하기
        }

        cycle = new Cycle(totalPlayer);
    }

    public int playRound(int mID, char mCh) {
        cycle.setCur(mID);  // 시작하는 사람 지정해주기
        int newCnt = 0;     // 이번 라운드에 추가될 단어 개수

        while (!wordSet[mCh - 'a'].isEmpty()) {
            // mCh로 시작하는 사전순으로 앞선 단어 찾기, O(log M)
            String word = wordSet[mCh - 'a'].pollFirst();

            // 선택한 적이 있다는 기록 남기기, O(1)
            usedWordSet.add(word);

            // 뒤집은 단어 추가하기
            StringBuilder sb = new StringBuilder(word);
            String reverseWord = sb.reverse().toString();
            if (!usedWordSet.contains(reverseWord) )  // 뒤집은 단어를 사용한 적이 없다면, 다음 라운드에 추가할 가능성이 있다!
                newWords[newCnt++] = reverseWord;

            // 다음 시작 알파벳 결정하기
            mCh = reverseWord.charAt(0);

            // 순서 넘기기, O(1)
            cycle.move();
        }

        // 뒤집은 단어들 추가하기
        for (int i = 0; i < newCnt; i++) {
            if (usedWordSet.contains(newWords[i])) continue;
            wordSet[newWords[i].charAt(0) - 'a'].add(newWords[i]);
        }
        return cycle.pop();
    }
}