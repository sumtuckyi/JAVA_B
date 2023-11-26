import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.LinkedList;


class UserSolution {
    private static final int MAX_CARD = 50000;

    private static class Table {
        public static int joker;
        public static int begin, end;
        public static int[] cards = new int[MAX_CARD * 2 + 5];
        public static LinkedList<Integer>[][] idxList = new LinkedList[20][20];
        // idxList[joker][score] := 조커의 점수가 joker일 때, 점수가 score인 인덱스 리스트
        // 예를 들어, 현재 joker = 9점이고, findNumber의 타겟 점수가 19 점이라면,
        // 우리는 idxList[9][19] => 원하는 인덱스 리스트를 바로 알 수 있다.

        // 이차원 배열 채우기(idx는 4장 카드 중 맨 앞에 있는 카드의 인덱스, mdir는 idx를 deque의 앞/뒤 중 어디에 추가할 것인지를 나타냄) 
        public static void updateIdx(int idx, int mdir) {   // 카드의 인덱스가 [idx ~ (idx + 3)]인 네 장의 카드의 점수를 mdir 방향에 추가한다.
            int sum = 0; // 4장의 카드 중 조커가 아닌 카드들의 합
            int joker_cnt = 0; // 조커 카드의 개수
            for (int i = 0; i < 4; i++) { // 카드 4장의 점수 계산
                if (cards[idx + i] == -1) // idx가 4장의 카드 중 가장 앞쪽 카드의 번호 
                    joker_cnt++; // 조커 카드면 개수만 세기 
                else
                    sum += cards[idx + i]; // 조커 카드가 아니면 카드에 적힌 값을 더해줌
            }
            for (int i = 0; i < 20; i++) {  // 조커 점수가 i 라고 가정하자. 
                int num = (sum + (joker_cnt * i)) % 20; // score계산
                if (mdir == 0)  // 왼쪽이면
                    idxList[i][num].add(0, idx); // deque의 맨 앞에 인덱스를 추가
                else  // 오른쪽이면
                    idxList[i][num].add(idx); // deque의 맨 뒤에 인덱스를 추가 
            }
        }

        // 각 테스트 케이스의 처음에 한 번 호출 
        public static void init(int mJoker, int[] mNumbers) {   // 초기 5장과 조커로 초기화하기
            joker = mJoker % 20;    // 조커 점수를 20으로 나눈 나머지로 초기화하기
            begin = end = MAX_CARD;	// 카드들의 처음, 끝 위치 초기화하기(인덱스 : 50000)
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    idxList[i][j] = new LinkedList<>(); // 2차원 배열 초기화
                }
            }
            for (int i = 0; i < 5; i++) { // 처음 5장의 카드를 cards 배열의 50000부터 50004번 인덱스에 채우기 
                cards[end + i] = mNumbers[i];
            }
            end += 5; // end 인덱스 갱신(다음번에 오른쪽에 카드를 추가해야 한다면 갱신된 end인덱스부터 채우게 됨)
            for (int i = 0; i < 2; i++) { // 카드가 5장인 경우 4장의 카드의 합을 구한다면, 2가지 경우의 수가 존재
                updateIdx(MAX_CARD + i, 1); // 50000번부터 4장, 50001번부터 4장
            }
        }

        public static void pushFront(int[] mNumbers) {  // 새로운 5 장을 왼쪽에 놓기
            begin -= 5; // 기존 카드 배열의 앞에 5장을 추가하므로 begin인덱스를 갱신
            for (int i = 0; i < 5; i++) { // cards 배열에 5장의 카드 추가 
                cards[begin + i] = mNumbers[i];
            }
            int target = begin; 
            for (int i = 4; i >= 0; i--) { // 추가된 카드 중 맨 뒤에서부터(즉, 인덱스가 큰 경우부터) 4장의 카드 합을 계산하여 
                updateIdx(target + i, 0); // 이차원배열 갱신 (총 5가지 경우의 수*20가지(조커의 점수로 가능한 경우의 수))
            }
        }

        public static void pushBack(int[] mNumbers) {   // 새로운 5 장을 오른쪽에 놓기
            for (int i = 0; i < 5; i++) {
                cards[end + i] = mNumbers[i];
            }
            int target = end - 3; // 새로운 4장의 카드 조합의 시작점 지정
            end += 5; // 다음에 오른쪽에 카드가 추가될 인덱스를 갱신
            for (int i = 0; i < 5; i++) { // 추가된 카드 중 맨 앞에서부터(즉, 인덱스가 작은 경우부터) 4장의 카드 합을 계산하여
                updateIdx(target + i, 1); // 이차원배열 갱신
            }
        }

        public static int find(int mNum, int mNth, int[] ret) { // mNum 점수 중에 mNth 번째 위치 찾기
            LinkedList<Integer> list = idxList[joker][mNum]; // 현재 조커카드의 값이 joker일 때, 4장의 카드의 점수가 mNum을 만족하는 경우(카드 조합의 시작 인덱스)
            if (mNth > list.size()) // 조건을 만족하는 경우의 수가 mNth보다 작은 경우 
                return 0;
            // 그렇지 않은 경우라면, 해당 배열에서 mNth번째 인덱스를 찾기
            int idx = list.get(mNth - 1);
            for (int i = 0; i < 4; i++) { // 찾았으니 해당 인덱스를 포함 연속하는 4장의 카드에 적힌 값을 ret배열에 저장
                ret[i] = cards[idx + i];
            }
            return 1;
        }

        void changeJoker(int mJoker) {  // 조커 점수 바꾸기
            joker = mJoker % 20;
        }
    }


    private static Table t = new Table();
    
    // 초기 조커값, 5장의 카드 세팅
    void init(int mJoker, int mNumbers[]) {
        t.init(mJoker, mNumbers);
    }
    // 카드 추가하기
    void putCards(int mDir, int mNumbers[]) {
        if (mDir == 0) {
            t.pushFront(mNumbers);
        } else {
            t.pushBack(mNumbers);
        }
    }
    // 카드 4장의 합이 mNum이 되는 mNth번째 조합을 반환
    int findNumber(int mNum, int mNth, int ret[]) {
        return t.find(mNum, mNth, ret);
    }
    // 조커카드의 값을 변경 
    void changeJoker(int mValue) {
        t.changeJoker(mValue);
    }
}
