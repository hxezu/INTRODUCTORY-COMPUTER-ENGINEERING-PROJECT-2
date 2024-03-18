package mid;

public class Doctor implements Comparable<Doctor>{
	
	private String depName;		// 진료 과 이름
	private String name;		// 의사 이름
	private int[][] docSch;		// 의사 스케줄
	private int[][] resPNum;	// 해당 날짜 해당 시간 현재 예약 인원 계산 (편의상 추가)
	
	public Doctor(String depName, String name) {
		super();
		this.depName = depName;
		this.name = name;
		docSch = new int[7][8];
		for(int i=0; i<7; i++) {
			docSch[i][0] = i+1;
			for(int j=1; j<8; j++) {
				docSch[i][j] = 1;
			}
		}
		resPNum = new int[7][7]; // 편의상 추가
	}
	
	
	// getter, setter func
	
	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[][] getDocSch() {
		return docSch;
	}

	public void setDocSch(int[][] docSch) {
		this.docSch = docSch;
	}
	
	// 스케줄 수정
	public boolean changeSch(int day, int time) {
		// 스케줄 off면 0, on이면 1
		// 스케줄 배열의 day(1~7)일째, 해당 time(1~7) 시간 change
		
		// 0으로 바뀌면 false 리턴, 1로 바뀌면 true 리턴
		if (docSch[day - 1][time] == 1) {
			docSch[day - 1][time] = 0; return false;
		}
		else {
			docSch[day - 1][time] = 1; return true;
		}
	}
	
	// 특정 날짜 특정 시간의 현재 예약 인원 계산 (편의상 추가, 사용하지 않아도 되는 함수)
	public void settingResP(int day, int time, int count) {
		// day는 오늘 날짜 기준으로 1 ~ 7, time도 1 ~ 7
		resPNum[day - 1][time - 1] = resPNum[day -1][time - 1] + count;
	}
	
	@Override
	public int compareTo(Doctor doc) {
		// TODO Auto-generated method stub
		return this.depName.compareTo(doc.getDepName());
	}
	
}
