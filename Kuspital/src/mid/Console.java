package mid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Console {

	/*
	 * 필요한 함수 및 변수 자유롭게 추가 가능 -> 의미가 모호한 경우 해당 함수 및 변수의 의미 또는 역할 주석으로 작성
	 * 
	 * 해당 프로그램에서 사용하는 이 클래스를 포함한 모든 클래스에서 'q' 키 입력 시 이전 메뉴 (상황 or 선택지)로 돌아감을 항상
	 * 고려해야함
	 * 
	 * 다른 클래스에 영향이 가는 부분 수정 및 추가 시 알리거나 논의하기
	 */

	private ArrayList<User> userList; // 전체 user list
	private ArrayList<Doctor> docList; // 전체 doctor list
	private UserFunc uf;
	private AdminFunc af;
	private Scanner scanner;
	private String UserFileString = "data/UserInfo.txt";
	private String DoctorFileString = "data/DoctorInfo.txt";
	private String ReserveFileString = "data/ReservationInfo.txt";

	public Console() {
		scanner = new Scanner(System.in);

		// user들의 정보 userList에 저장
		userList = new ArrayList<User>();

		// doctor들의 정보 docList에 저장
		docList = new ArrayList<Doctor>();

	}

	public void showMain() { // 메인메뉴 show

		int count = 0;

		A: while (true) {

			if (count == 0) {
				if (!loadingData())
					break;
				else
					count++;
			}

			// 회원가입한 유저 userList에 저장되는지 확인
			/*
			 * for(User user : userList) { System.out.println(user.getId()+user.getPw()); }
			 */

			/*
			 * 1번 메뉴 선택 -> signUp() 실행 2번 메뉴 선택 -> login() 실행 3번 메뉴 선택 -> saveFile() 실행 후
			 * while문 break해 프로그램 종료
			 */

			System.out.println("<KU 병원>");
			System.out.println("1) 회원가입 2) 로그인 3) 종료");
			System.out.print("메뉴를 입력하세요 : ");
			String input = scanner.nextLine();
			switch (input) {
			case "1":
				signUp();
				break;
			case "2":
				login();
				break;
			case "3":
				saveFile();
				System.out.println("프로그램을 종료합니다.");
				break A;
			default:
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
				break;
			}

		}
	}

	public Boolean loadingData() {

		// 데이터 파일에서 읽어올 때 오류 발생 시 return false;

		if (!readUserFile())
			return false;
		if (!readDocFile())
			return false;
		if (!readResFile())
			return false;

		return true;
	}

	public Boolean readDocFile() { // DoctorInfo.txt 불러오기

		/*
		 * 데이터 파일에서 읽어올 때 오류 발생 시 return false; 정보 읽어오고 new Doctor(진료과 이름, 의사 이름, 의사 개인
		 * 스케줄); 사용 진료과 이름, 의사 이름은 String 의사 개인 스케줄은 2차원 배열로 저장 -> int[7][8] -> 한 row마다
		 * [진료일자8자리, 1부 예약 여부, ... , 7부 예약 여부]로 저장 -> 일주일씩 열리므로 row들이 7개
		 */

		try {
			File doctorFile = new File(DoctorFileString);
			Scanner fileScan = new Scanner(doctorFile);
			while (fileScan.hasNextLine()) {
				String tempDepname = fileScan.next();
				String tempName = fileScan.next();
				int[][] tempDocsch = new int[7][8];
				for (int i = 0; i < 7; i++) {
					String tempDaynum = fileScan.next();
					int tempnum = Integer.parseInt(tempDaynum);
					tempDocsch[i][0] = tempnum;
					String tempTimenum = fileScan.next();
					String[] tempArr = tempTimenum.split("");
					for (int j = 1; j < 8; j++) {
						tempDocsch[i][j] = Integer.parseInt(tempArr[j - 1]);
					}
				}

				Doctor doc = new Doctor(tempDepname, tempName);
				doc.setDocSch(tempDocsch);
				docList.add(doc);

			}

		} catch (FileNotFoundException e) {
			System.out.println("파일을 찾을 수 없습니다. ");
			return false;
			// e.printStackTrace();
		}

		return true;
	}

	@SuppressWarnings("resource")
	public Boolean readUserFile() { // UserInfo.txt 불러오기

		/*
		 * 데이터 파일에서 읽어올 때 오류 발생 시 return false; 정보 읽어오고 new User(아이디, 패스워드, 이름, 주민번호);
		 * 사용 아이디, 패스워드, 이름, 주민번호는 String
		 */

		try {
			File userFile = new File(UserFileString);
			Scanner fileScan = new Scanner(userFile);
			while (fileScan.hasNextLine()) {
				String temp = fileScan.nextLine();
				String[] tempArr = temp.split(" ");
				if (tempArr.length == 4) {
					String tempID = tempArr[0];
					String tempPW = tempArr[1];
					String tempName = tempArr[2];
					String tempResnum = tempArr[3];

					userList.add(new User(tempID, tempPW, tempName, tempResnum));
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("파일을 찾을 수 없습니다. ");
			return false;
			// e.printStackTrace();
		}

		return true;
	}

	public Boolean readResFile() { // ReservationInfo.txt 불러오기

		/*
		 * 데이터 파일에서 읽어올 때 오류 발생 시 return false; 사용자 아이디 탐색하고 일치하면 ([진료과 이름, 의사 이름, 예약
		 * 시간]을 String 배열로 (size 3) 저장) <- 이것은 addRes(strArr); -> (편의 상 추가한
		 * doctor.settingResP 사용 시) addRes() 활용 시 해당 Doctor의 해당 날짜의 해당 시간의 진료 예약 인원 +1
		 * (해당의사객체.settingResP(day, time, 1))
		 */
		try {
			File reserFile = new File(ReserveFileString);
			Scanner fileScan = new Scanner(reserFile);
			while (fileScan.hasNextLine()) {
				String tempID = fileScan.nextLine();
				String temp = fileScan.nextLine();
				String[] tempArr = temp.split(" ");
				if (tempArr.length == 4) {
					String tempDep = tempArr[0];
					String tempDoc = tempArr[1];
					String tempDay = tempArr[2];
					String tempTime = tempArr[3];

					Iterator<User> iterator1 = userList.iterator();
					while (iterator1.hasNext()) {
						User user = iterator1.next();
						if (user.getId().equals(tempID)) {
							user.addRes(tempDep, tempDoc, tempDay, tempTime);
						}
					}
				}

			}

		} catch (FileNotFoundException e) {
			System.out.println("파일을 찾을 수 없습니다. ");
			return false;
			// e.printStackTrace();
		}

		return true;
	}

	public void signUp() {

		// 메인메뉴의 1번 회원가입
		// 회원가입 성공 시 addUser(아이디, 패스워드, 이름, 주민번호) 실행

		String name, resinum, id, pw; // 이름, 주민번호, 아이디, 비밀번호
		System.out.println("\n====== 회원 가입 ======");

		// 이름
		while (true) {
			System.out.print("이름 : ");
			name = scanner.nextLine();

			String name_ws = name.replaceAll(" ", "");
			if (checkForm(name, true) == false)
				System.out.println("한글만 입력하세요");
			else if (name.length() != name_ws.length())
				System.out.println("공백없이 입력하세요. ");
			else if (name.length() < 2 || name.length() > 7)
				System.out.println("2~7자 사이로 입력하세요. ");
			else
				break;
		}

		// 주민번호
		String resinum_ws, resinum_tr, resinum_nn;
		A: while (true) {
			System.out.print("주민번호 : ");
			resinum = scanner.nextLine();

			resinum_ws = resinum.replaceAll("-", "");
			resinum_tr = resinum_ws.replaceAll(" ", "");
			resinum_nn = resinum.replaceAll(" ", "");

			if (!resinum_nn.equals(resinum)) { // 공백 있는 경우
				System.out.println("공백없이 입력하세요. ");
			} else {

				// 등록된 주민번호 중 동일한 번호 존재
				for (User user : userList) {
					if ((resinum.trim()).equals(user.getResinum()) || (resinum_ws).equals(user.getResinum())
							|| (resinum_tr).equals(user.getResinum())) {
						System.out.println("이미 등록된 정보입니다. ");
						continue A;
					}
				}

				// '-'가 들어갔을 때 올바르지 않은 형식 ex. (숫자5자리)-(숫자7자리),
				for (int i = 0; i < resinum.length(); i++) {
					char st = resinum.charAt(i);
					if (st == '-' && i != 6) {
						System.out.println("주민번호 입력 형태는 공백없이 (숫자6자리)-(숫자7자리) 또는 (숫자13자리) 입니다.");
						continue A;
					}
				}

				// '-' 포함되지 않을 때 숫자가 13자리가 아닐 경우
				if (resinum_ws.length() != 13)
					System.out.println("제대로 입력했는지 확인해주세요.");
				else
					break; // 위의 경우가 모두 아닐 때 다음으로 넘어감
			}

		}

		// 아이디
		B: while (true) {
			System.out.print("아이디(5~12자리 영어, 숫자만) : ");
			id = scanner.nextLine();
			for (User user : userList) {
				if ((id.trim()).equals(user.getId())) {
					System.out.println("이미 같은 아이디가 존재합니다.");
					continue B;
				}
			}
			String id_ws = id.replaceAll(" ", "");
			if (checkForm(id, false) == false)
				System.out.println("숫자와 영어만 입력하세요");
			else if (id.length() != id_ws.length())
				System.out.println("공백없이 입력하세요. ");
			else if (id.length() < 5 || id.length() > 12)
				System.out.println("5~12자 사이로 입력하세요. ");
			else
				break;
		}

		// 비밀번호
		while (true) {
			System.out.print("비밀번호(8~20자리 영어, 숫자만) : ");
			pw = scanner.nextLine();

			String pw_ws = pw.replaceAll(" ", "");
			if (checkForm(pw, false) == false)
				System.out.println("숫자와 영어만 입력하세요");
			else if (pw.length() != pw_ws.length())
				System.out.println("공백없이 입력하세요. ");
			else if (pw.length() < 8 || pw.length() > 20)
				System.out.println("8~20자 사이로 입력하세요. ");
			else
				break;
		}

		while (true) {
			System.out.print("이 정보로 가입하시겠습니까?(y/n) : ");
			String ans = scanner.nextLine().trim();
			if (ans.equals("y") || ans.equals("Y")) {
				addUser(id, pw, name, resinum_ws);
				System.out.println("회원가입이 완료되었습니다.\n");
				break;
			} else if (ans.equals("n") || ans.equals("N")) {
				System.out.println("정보 입력을 취소합니다.");
				break;
			} else {
				System.out.println("올바른 입력이 아닙니다. 다시 입력해주세요.");
			}
		}
	}

	public boolean checkForm(String str, boolean bool) { // bool=true이면 name 체크, bool==false이면 id, pw 체크
		// str에 숫자, 영어, 한글 포함되는지 각각 체크
		boolean numCheck = false;
		boolean engCheck = false;
		boolean korCheck = false;
		boolean strangeCheck = false;

		for (int i = 0; i < str.length(); i++) {
			int index = str.charAt(i);

			if (index >= 48 && index <= 57) // 숫자 포함
				numCheck = true;
			else if (index >= 65 && index <= 122) // 영어 포함
				engCheck = true;
			else if (str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) // 한글 포함
				korCheck = true;
			else // 그 외
				strangeCheck = true;
		}

		if (bool == true) {
			if (numCheck == true || engCheck == true || strangeCheck == true)
				return false;
			else
				return true;
		} else {
			if (korCheck == true || strangeCheck == true)
				return false;
			else
				return true;
		}
	}

	public void addUser(String id, String pw, String name, String resinum) {

		// userList에 신규 회원 정보 추가 (ArrayList에 add)
		userList.add(new User(id, pw, name, resinum));

	}

	public void login() {

		// 메인메뉴의 2번 로그인

		/*
		 * 사용자 로그인 성공 시 uf = new UserFunc(); uf.startUser(로그인 성공한 유저 객체, userList,
		 * docList);
		 */

		/*
		 * 관리자 로그인 성공 시 af = new AdminFunc(); af.startAdimin(userList, docList);
		 */

		/*
		 * for(User user : userList) { System.out.println(user.getId()+user.getPw()); }
		 */

		while (true) {
			System.out.println("\n====== 로그인 ======");
			System.out.print("아이디 : ");
			String inputID = scanner.nextLine();
			System.out.print("비밀번호를 입력하세요 : ");
			String inputPW = scanner.nextLine();

			String loginID = inputID.trim();
			String loginPW = inputPW.trim();

			// 관리자 로그인
			if (loginID.equals("admi") && loginPW.equals("admin")) {
				System.out.println("관리자 로그인 성공");
				af = new AdminFunc();
				af.startAdmin(userList, docList);
				break;
			}

			// 사용자 로그인
			User helloUser = loginUser(loginID, loginPW);
			if (helloUser == null) {
				System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
			} else {
				uf = new UserFunc();
				uf.startUser(helloUser, userList, docList);
				break;
			}

		}

	}

	public User loginUser(String id, String pw) {

		for (User user : userList) {
			if (user.getId().equals(id)) {
				if (user.getPw().equals(pw)) {
					System.out.println(user.getName() + "님 로그인 성공");
					return user;
				}
			}
		}
		return null;
	}

	public void saveFile() { // 메인메뉴에서 프로그램 종료하기 전에 저장된 정보들 파일로 저장
		saveDocFile(); // DoctorInfo.txt 파일 저장
		saveUserFile(); // UserInfo.txt 파일 저장
		saveResFile(); // ReservationInfo.txt 파일 저장
	}

	public void saveDocFile() { // DoctorInfo.txt 파일 저장

		File oFile = new File(DoctorFileString);
		try {
			int c = 0;
			FileOutputStream fos = new FileOutputStream(oFile);
			for (Doctor doc : docList) {
				if (c != 0) {
					String text = "\n";
					byte[] buf = text.getBytes();
					fos.write(buf);
					fos.flush();
				} else
					c++;
				String text = doc.getDepName() + "\n";
				byte[] buf1 = text.getBytes();
				fos.write(buf1);
				fos.flush();
				text = doc.getName() + "\n";
				byte[] buf2 = text.getBytes();
				fos.write(buf2);
				fos.flush();

				int c2 = 0;
				for (int i = 0; i < 7; i++) {
					if (c2 != 0) {
						text = "\n";
						byte[] buf = text.getBytes();
						fos.write(buf);
						fos.flush();
					} else
						c2++;
					text = doc.getDocSch()[i][0] + " ";
					byte[] buf3 = text.getBytes();
					fos.write(buf3);
					fos.flush();
					for (int j = 1; j < 8; j++) {
						text = Integer.toString(doc.getDocSch()[i][j]);
						byte[] buf4 = text.getBytes();
						fos.write(buf4);
						fos.flush();
					}
				}
			}
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// docList에 있는 정보 저장
	}

	// 아래와 같이 작성해두긴 했으나, saveUserFile()과 saveResFile()은 편의에 따라 ArrayList 한 번 탐색할 때 한
	// 번에 저장하는 것도 좋음

	public void saveUserFile() { // UserInfo.txt 파일 저장

		// userList에 있는 개인정보 (아이디, 패스워드, 이름, 주민번호) 저장

		File oFile = new File(UserFileString);
		try {
			int c = 0;
			FileOutputStream fos = new FileOutputStream(oFile);
			for (User user : userList) {
				if (c != 0) {
					String text = "\n";
					byte[] buf = text.getBytes();
					fos.write(buf);
					fos.flush();
				} else
					c++;
				String text = user.getId() + " ";
				byte[] buf1 = text.getBytes();
				fos.write(buf1);
				fos.flush();
				text = user.getPw() + " ";
				byte[] buf2 = text.getBytes();
				fos.write(buf2);
				fos.flush();
				text = user.getName() + " ";
				byte[] buf3 = text.getBytes();
				fos.write(buf3);
				fos.flush();
				text = user.getResinum();
				byte[] buf4 = text.getBytes();
				fos.write(buf4);
				fos.flush();
			}
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveResFile() { // ReservationInfo.txt 파일 저장

		// userList에 있는 예약 정보 저장
		File oFile = new File(ReserveFileString);
		try {
			int c = 0;
			FileOutputStream fos = new FileOutputStream(oFile);

			Iterator<User> iterator = userList.iterator();
			while (iterator.hasNext()) {
				User user = iterator.next();

				Iterator<String[]> itString = user.getResList().iterator();
				while (itString.hasNext()) {
					if (c != 0) {
						String text = "\n";
						byte[] buf = text.getBytes();
						fos.write(buf);
						fos.flush();
					} else
						c++;
					String[] tempArr = itString.next();
					String text = user.getId() + "\n";
					byte[] buf1 = text.getBytes();
					fos.write(buf1);
					fos.flush();
					text = tempArr[0] + " ";
					byte[] buf2 = text.getBytes();
					fos.write(buf2);
					fos.flush();
					text = tempArr[1] + " ";
					byte[] buf3 = text.getBytes();
					fos.write(buf3);
					fos.flush();
					text = tempArr[2] + " ";
					byte[] buf4 = text.getBytes();
					fos.write(buf4);
					fos.flush();
					text = tempArr[3];
					byte[] buf5 = text.getBytes();
					fos.write(buf5);
					fos.flush();
				}
			}
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
