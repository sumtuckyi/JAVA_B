import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.TreeMap;
import java.util.StringTokenizer;

class Node {
    int idx;
    Node prev;
    Node next;
    Node(int idx) {
        this.idx = idx;
    } // Node 클래스 생성자
} // LinkedList를 구성할 노드 클래스 정의

class UserSolution
{

    static final int N = 100; // 참가자의 수 
    static final int MAX_NUM_NODE = 400210;
    static final int LAST_START_NODE = N;
    static final int FIRST_END_NODE = MAX_NUM_NODE - N - 5;

    // Node클래스의 인스턴스로 이루어진 배열 선언 및 초기화 - MAX_NUM_NODE개의 Node 클래스 인스턴스를 위한 메모리 할당
    static Node[] node = new Node[MAX_NUM_NODE];    // 교점들의 이중 연결 리스트
    static int nodeCnt = 0; // ???
    // N+1개의 TreeMap 인스턴스를 위한 메모리 할당
    static TreeMap<Integer, Node> nodeMap[] = new TreeMap[N + 1];   // nodeMap[x] := x번 세로줄의 정점들의 y 좌표를 들고 있는 TreeMap

    public static void link(Node front, Node back) {    // 두 정점을 서로 연결해주기
        front.next = back;
        back.prev = front;
    }

    public static void init() {
        for (int i = 0; i < MAX_NUM_NODE; i++) {
            node[i] = new Node(i);
        } // node배열을 Node클래스 인스턴스로 채우기
        for (int i = 1; i <= N; i++) { // 참가자 별로 세로줄 생성하기 
            // initializing each element in the nodeMap array with a new instance of TreeMap<Integer, Node>
            nodeMap[i] = new TreeMap<>();
            
            nodeMap[i].put(0, node[i]);     // i 번 세로줄에 i 번 참가자를 놓는다. key값이 y좌표, i번 세로줄의 시작점을 추가한다.

            nodeMap[i].put(1000000000, node[FIRST_END_NODE + i - 1]);   // i 번 세로줄의 도착점을 추가한다.
            link(node[i], node[FIRST_END_NODE + i - 1]); // 아직 가로줄이 생성되지 않았으므로 시작점과 도착점을 연결한다. 
        }
        nodeCnt = N + 1; // Node의 개수가 101개..? 
    }

    public static void add(int mX, int mY) {  // (mX, mY)와 (mX + 1, mY)을 잇는 가로줄 추가하기, O(Log N)-이분탐색
        // 각각 새로 생성된 가로줄의 왼쪽점과 오른쪽점
        Node nowLeft = node[nodeCnt++]; // nodeCnt번 인덱스의 Node 인스턴스를 nowLeft에 할당하고 nodeCnt 변수의 값을 1증가시킴
        Node nowRight = node[nodeCnt++];

        Node prevLeft = nodeMap[mX].floorEntry(mY).getValue();          // mX 번 세로 줄에서 mY 직전 정점 찾기 - getValue()는 Node 인스턴스를 반환
        Node prevRight = nodeMap[mX + 1].floorEntry(mY).getValue();     // (mX + 1) 번 세로 줄에서 mY 직전 정점 찾기

        Node nextLeft = prevLeft.next;      // mX 번 세로 줄에서 mY 직후 정점 찾기
        Node nextRight = prevRight.next;    // (mX + 1) 번 세로 줄에서 mY 직후 정점 찾기

        // 순서 재조정 하기
        link(prevLeft, nowRight);
        link(nowRight, nextRight);

        link(prevRight, nowLeft);
        link(nowLeft, nextLeft);

        nodeMap[mX].put(mY, nowLeft); // mX번 참가자의 트리맵에 새로 추가된 점을 추가
        nodeMap[mX + 1].put(mY, nowRight); // mX+1번 참가자의 트리맵에 새로 추가된 점을 추가
    }

    public static void remove(int mX, int mY) {  // (mX, mY)와 (mX + 1, mY)을 잇는 가로줄 제거하기, O(Log N)
        Node nowLeft = nodeMap[mX].get(mY);         // mX번 세로 줄에서 mY 위치 정점 찾기
        Node nowRight = nodeMap[mX + 1].get(mY);    // (mX + 1)번 세로 줄에서 mY 위치 정점 찾기

        Node prevLeft = nowRight.prev;
        Node prevRight = nowLeft.prev;

        Node nextLeft = nowLeft.next;
        Node nextRight = nowRight.next;

        // 순서 재조정 하기 - 삭제할 가로줄 기준으로 직전 정점과 직후 정점을 연결해주기
        link(prevLeft, nextLeft);
        link(prevRight, nextRight);

        nodeMap[mX].remove(mY); // mX번 참가자의 트리맵에서 삭제할 가로줄의 왼쪽점 제거
        nodeMap[mX + 1].remove(mY); // mX+1번 참가자의 트리맵에서 삭제할 가로줄의 오른쪽점 제거
    }

    public static int numberOfCross(int mID) {  // mID번 참가자가 지나게 되는 가로줄의 개수 구하기, O(5000)
        int ret = -1; // y좌표가 0인 시작점은 카운트 안 함
        Node now = node[mID];   // mID 번 세로줄에서 출발하기 - now의 초기값은 (mID, 0)위치의 정점
        while (now.idx < node[FIRST_END_NODE].idx) {    // 마지막 줄에 도착할 때까지 다음으로 이동하기 (now.next != null)
            ret++;
            now = now.next; // mID참가자의 LinkedList에서 다음 노드로 계속해서 이동
        }
        return ret;
    }

    public static int participant(int mX, int mY) {  // (mX, mY)에 도착하는 참가자 찾기, O(5000)
        Node now = nodeMap[mX].floorEntry(mY).getValue();  // mX 번 세로 줄에서 mY 직전 정점 찾기
 
        while (now.idx > node[LAST_START_NODE].idx) // 최상단에 도착할 때까지 이전으로 돌아가기
            now = now.prev;
        return now.idx;
    }
}