import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

class Robot {
	int rID; 			// 로봇의 아이디
	int IQ;  			// 지능(앞으로 감소할 것임)
	int wID;  			// 일하고 있는 작업 번호
	int begin;  		// 일을 시작한 시각(지능 삭감을 위해 필요한 정보)
	boolean broken;  	// 고장 여부
	int maxidx, minidx; // maxHeap과 minHeap 에서의 위치 기억

	Robot() {
		rID = 0;
		IQ = 0;
		wID = 0;
		begin = 0;
		broken = false;
	}
}

class Job {	// 작업 객체 클래스
	Robot[] arr;  // 투입된 로봇 정보 - returnJob에서 작업 아이디만 주어지기 때문
	int n;		  // 투입된 로봇 개수

	Job() {
		n = 0;
		arr = new Robot[0];
	}
}

class MaxComp implements RobotComparator {
	public void set(Robot a, int idx) {
		a.maxidx = idx;  // max heap 에서의 위치 업데이트
	}

	public int compare(Robot a, Robot b) {  // 가장 똑똑한 친구를 먼저 선택
		return a.IQ != b.IQ ? a.IQ - b.IQ : b.rID - a.rID;
	}
}

class MinComp implements RobotComparator {
	public void set(Robot a, int idx) {
		a.minidx = idx;
	}

	public int compare(Robot a, Robot b) {
		return a.IQ != b.IQ ? b.IQ - a.IQ : b.rID - a.rID;
	}
}

class Que<C extends RobotComparator> {  // 쉬는 로봇들을 담는 자료구조
	private static final int MAXN = 100005;

	Robot[] arr = new Robot[MAXN];
	int size;
	C comp;

	public void init(C comp) {
		size = 0;
		this.comp = comp;
	}

	public void init(int n, C comp, Robot[] heapified) {  // 초기화
		this.comp = comp;
		for (int i = 0; i < n; ++i) {
			arr[i] = heapified[i + 1];
			this.comp.set(arr[i], i);
		}

		size = n;
	}

	public void push(Robot v) {  // 삽입
		arr[size] = v;
		comp.set(arr[size], size);

		int cur = size++;

		while (cur > 0) {
			int par = (cur - 1) / 2;
			if (comp.compare(arr[cur], arr[par]) <= 0)
				break;
			swap(arr, cur, par);
			comp.set(arr[cur], cur);
			comp.set(arr[par], par);
			cur = par;
		}
	}

	public Robot pop() {  // 제일 높은 우선 순위 제거
		if (size == 0)
			return null;

		Robot ret = arr[0];
		arr[0] = arr[--size];
		comp.set(arr[0], 0);

		int cur = 0;
		while (cur * 2 + 1 < size) {
			int child = cur * 2 + 2 < size && comp.compare(arr[cur * 2 + 1], arr[cur * 2 + 2]) < 0 ? cur * 2 + 2 : cur * 2 + 1;
			if (comp.compare(arr[child], arr[cur]) <= 0)
				break;
			swap(arr, cur, child);
			comp.set(arr[cur], cur);
			comp.set(arr[child], child);
			cur = child;
		}

		return ret;
	}

	public void remove(int idx) {  // 임의의 위치 제거 O(log N)
		if (idx >= size)
			return;

		arr[idx] = arr[--size];
		comp.set(arr[size], idx);

		int cur = idx;
		while (cur * 2 + 1 < size) {
			int child = cur * 2 + 2 < size && comp.compare(arr[cur * 2 + 1], arr[cur * 2 + 2]) < 0 ? cur * 2 + 2 : cur * 2 + 1;
			if (comp.compare(arr[child], arr[cur]) <= 0)
				break;
			swap(arr, cur, child);
			comp.set(arr[cur], cur);
			comp.set(arr[child], child);
			cur = child;
		}

		while (cur > 0) {
			int par = (cur - 1) / 2;
			if (comp.compare(arr[cur], arr[par]) <= 0)
				break;
			swap(arr, cur, par);
			comp.set(arr[cur], cur);
			comp.set(arr[par], par);
			cur = par;
		}
	}

	private void swap(Robot[] arr, int i, int j) {
		Robot temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}

interface RobotComparator {
	int compare(Robot a, Robot b);

	void set(Robot r, int i);  // 특정 로봇을 Heap 에서의 위치를 업데이트
}

class UserSolution
{
	final int MAXN = 50001;
	final int MAXM = 50001;
	final int MAXK = 200001;

	Que<MaxComp> maxQueue;
	Que<MinComp> minQueue;
	Robot[] robot;
	Job[] jobs;

	void init(int N) {  // 초기화
		robot = new Robot[MAXN];
		jobs = new Job[MAXM];
		maxQueue = new Que<>();
		minQueue = new Que<>();
		for (int i = 1; i <= N; ++i) {
			robot[i] = new Robot();
			robot[i].rID = i;
			robot[i].IQ = 0;
			robot[i].wID = 0;
			robot[i].begin = 0;
			robot[i].broken = false;
		}
		for (int i = 0; i < MAXM; ++i) {
			jobs[i] = new Job();
		}

		maxQueue.init(N, new MaxComp(), robot);
		minQueue.init(N, new MinComp(), robot);
	}

	int callJob(int cTime, int wID, int mNum, int mOpt) {  // cTime 시간에 wID 작업으로 mNum대의 로봇을 투입한다. mOpt 기준에 맞게 한다.
		jobs[wID].arr = new Robot[mNum];
		jobs[wID].n = mNum;

		int ret = 0;
		if (mOpt == 0) {	// 높은 지능을 우선으로
			int cnt = 0;
			while (cnt < mNum) {	
				Robot p = maxQueue.pop();	// 제일 높은 지능의 로봇을 선택

				// 작업에 로봇을 배정한다.
				p.wID = wID;
				p.begin = cTime;
				jobs[wID].arr[cnt++] = p;

				// 해당 로봇을 minque 에서도 제외한다.
				minQueue.remove(p.minidx);
				ret += p.rID;
			}
		} else {	// 낮은 지능을 우선으로
			int cnt = 0;
			while (cnt < mNum) {
				Robot p = minQueue.pop();	// 제일 낮은 지능의 로봇을 선택

				// 작업에 로봇을 배정한다.
				p.wID = wID;
				p.begin = cTime;
				jobs[wID].arr[cnt++] = p;

				// 해당 로봇을 maxque 에서도 제외한다.
				maxQueue.remove(p.maxidx);
				ret += p.rID;
			}
		}

		return ret;
	}

	void returnJob(int cTime, int wID) {	// cTime 시간에 wID 작업에 있는 로봇들을 반환한다.
		int n = jobs[wID].n;
		for (int i = 0; i < n; ++i) {
			Robot p = jobs[wID].arr[i];
			if (p.broken || p.wID != wID)	// 로봇이 고장났거나, 다른 작업 중이면 무시한다. (Lazy return) -> 고장난 로봇을 배열에 그대로 둬도 되는 이유
				continue;

			// 작업 시간만큼 지능을 낮춘다.
			p.IQ -= cTime - p.begin; // 센터에 다시 들어온 시간 - 센터에서 나간 시간
			p.wID = 0;

			// 가용 로봇 집합에 반환시킨다. - 센터로 복귀
			maxQueue.push(p);
			minQueue.push(p);
		}
	}

	void broken(int cTime, int rID) {	// cTime 시간에 rID 로봇이 고장난다.
		if (robot[rID].wID != 0) {	// 작업 중이라면, 작업을 취소하고 고장 처리한다.
			robot[rID].wID = 0; // 작업을 중지
			robot[rID].broken = true; // 고장처리
		} 
	} // 로봇 클래스만 수정하면 되나? 작업 클래스는? 배열에서 요소를 삭제하지 않음

	void repair(int cTime, int rID) {	// cTime 시간에 rID 로봇이 고쳐졌다.
		if (robot[rID].broken) {
			robot[rID].IQ = -cTime;	// 로봇의 지능 지수를 -cTime 으로 처리한다. = 지능을 0으로 초기화
			robot[rID].broken = false;

			// 가용 로봇 집합에 반환시킨다.
			maxQueue.push(robot[rID]);
			minQueue.push(robot[rID]);
		}
	}

	int check(int cTime, int rID) {	// cTime 시간에 rID 로봇의 상황을 확인한다.
		int ret;
		if (robot[rID].broken)			// 고장난 상태
			ret = 0;
		else if (robot[rID].wID != 0)	// 작업 중인 상태 
			ret = -robot[rID].wID;
		else							// 그 외의 상태
			ret = cTime + robot[rID].IQ;	// 깎인 지능(센터를 나가있던 시간)에 흐른 시간을 더하면 원래 지능이 계산된다.

		return ret;
	}
}