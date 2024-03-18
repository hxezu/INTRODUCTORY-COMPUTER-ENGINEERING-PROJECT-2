package mid;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

public class UserFunc {

	// success user login
	private ArrayList<User> userList; // 전체 user list
	private ArrayList<Doctor> docList; // 전체 doctor list

	private User user; // 현재 로그인한 유저
	private String id;
	private String name;
	private String resinum;
	private ArrayList<String[]> resList;
	private Scanner scanner = new Scanner(System.in);

	public UserFunc() {

	}

	public void startUser(User u, ArrayList<User> uList, ArrayList<Doctor> dList) {
		id = u.getId();
		name = u.getName();
		resinum = u.getResinum();
		userList = uList;
		docList = dList;
		resList = u.getResList();

		user = new User(id, u.getPw(), name, resinum); // 로그인한 user의 정보 불러오기
		user.setResList(resList);

		A: while (true) {
			// 사용자 메뉴 실행, 메인메뉴로 가기 전까지 무한 반복

			/*
			 * 1번 메뉴 선택 -> resMenu() 실행 2번 메뉴 선택 -> myPageMenu() 실행 3번 메뉴 선택 -> 메인메뉴로 가기
			 * (while문 break), 로그아웃 시 이 클래스에서 수정된 현재 유저의 정보 (Console 클래스에서도) 적용되어야 함
			 */

			System.out.println("====== 사용자 메뉴 ======");
			System.out.println("1) 진료 예약 2) 마이 페이지 3) 로그아웃");
			System.out.print("메뉴를 입력하세요 : ");

			String input = scanner.nextLine();
			switch (input) {
			case "1":
				resMenu();
				break;
			case "2":
				showMyPage();
				break;
			case "3":
				System.out.println("메인메뉴로 돌아갑니다.");
				break A;
			default:
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
				break;
			}
		}
	}

	public void resMenu() { // 사용자메뉴 -> 1. 진료예약

		// 진료과 선택 -> 의사 선택 -> 날짜 선택 -> 진료 가능 시간 선택 -> 예약 완료
		// User class의 addRes() 활용
		// -> (편의 상 추가한 doctor.settingResP 사용 시)
		// addRes() 활용 시 해당 Doctor의 해당 날짜의 해당 시간의 진료 예약 인원 +1 (해당의사객체.settingResP(day,
		// time, 1))
		// 해당 Doctor의 스케줄 정보 수정
		// depDocList [0]에 진료과 이름 저장, [1]부터 의사 이름 나열

		// 진료과 의사 리스트 정렬, 초기화
		ArrayList<String[]> depDocList = new ArrayList<String[]>();
		Collections.sort(docList);
		ArrayList<String> tempList = new ArrayList<String>();

		int count = 0;
		Iterator<Doctor> iterator = docList.iterator();
		while (iterator.hasNext()) {
			Doctor doc = iterator.next();
			if (count == 0) {
				tempList.add(doc.getDepName());
				count++;
			}

			if (tempList.get(0).equals(doc.getDepName())) {
				tempList.add(doc.getName());
			} else {
				String[] tempArr = new String[tempList.size()];
				for (int i = 0; i < tempList.size(); i++)
					tempArr[i] = tempList.get(i);
				depDocList.add(tempArr);
				tempList = new ArrayList<String>();
				tempList.add(doc.getDepName());
				tempList.add(doc.getName());
			}
		}

		// 마지막 인덱스 추가 작업
		String[] tempArr = new String[tempList.size()];
		for (int i = 0; i < tempList.size(); i++)
			tempArr[i] = tempList.get(i);
		depDocList.add(tempArr);

		// 진료과, 의사, 예약 일자, 예약 시간대
		int depNum = 0, docNum = 0, dayNum = 0, timeNum = 0;
		String input;

		A: while (true) {
			try {
				System.out.println("====== 진료 예약 ======");
				System.out.println("진료 예약 - 진료 과(뒤로가기'q')");
				for (int i = 0; i < depDocList.size(); i++) {
					System.out.println((i + 1) + ") " + depDocList.get(i)[0]);
				}
				System.out.print("진료 예약을 할 진료과를 골라주세요: ");
				input = scanner.nextLine();
				if (input.length() == 1) {
					if (input.equals("q") || input.equals("Q"))
						break;
					depNum = Integer.parseInt(input) - 1;
					if (depNum < 0 || depNum > depDocList.size() - 1)
						throw new NumberFormatException();
				} else
					throw new NumberFormatException();
			} catch (NumberFormatException e) {
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
				scanner.nextLine();
				continue;
			}

			while (true) {
				try {
					System.out.println("====== 진료 예약 ======");
					System.out.println(depDocList.get(depNum)[0] + " 의사 (뒤로가기'q')");
					for (int i = 1; i < depDocList.get(depNum).length; i++) {
						System.out.println(i + ") " + depDocList.get(depNum)[i]);
					}
					System.out.print("진료 예약을 할 의사를 골라주세요: ");
					input = scanner.nextLine();
					if (input.length() == 1) {
						if (input.equals("q") || input.equals("Q"))
							break;
						docNum = Integer.parseInt(input);
						if (docNum < 1 || docNum > depDocList.get(depNum).length - 1)
							throw new NumberFormatException();
					} else
						throw new NumberFormatException();
				} catch (NumberFormatException e) {
					System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
					scanner.nextLine();
					continue;
				}

				while (true) {
					try {
						LocalDate nowDate = LocalDate.now();
						DayOfWeek dayOfWeek = nowDate.getDayOfWeek();
						System.out.println("====== 진료 예약 ======");
						System.out.println(depDocList.get(depNum)[docNum] + " 진료 가능 날짜 (뒤로가기'q')");
						for (int i = 1; i <= 7; i++) {
							System.out.print(i + ") " + nowDate.plusDays(i));
							dayOfWeek = nowDate.plusDays(i).getDayOfWeek();
							System.out.println(" (" + dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN) + ")");
						}
						System.out.print("진료 예약을 할 날짜를 골라주세요: ");
						input = scanner.nextLine();
						if (input.length() == 1) {
							if (input.equals("q") || input.equals("Q"))
								break;
							dayNum = Integer.parseInt(input) - 1;
							if (dayNum < 0 || dayNum > 7 - 1)
								throw new NumberFormatException();
						} else
							throw new NumberFormatException();
					} catch (NumberFormatException e) {
						System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
						scanner.nextLine();
						continue;
					}

					B: while (true) {
						try {
							int[] timeSet = { 9, 10, 11, 14, 15, 16, 17 };
							System.out.println("====== 진료 예약 ======");
							System.out.println(depDocList.get(depNum)[docNum] + " 진료 가능 시간 (뒤로가기'q')");

							Doctor setDoc = null;
							for (Doctor doc : docList) {
								if (doc.getName().equals(depDocList.get(depNum)[docNum])) {
									setDoc = doc;
									break;
								}
							}
							int[][] docSch = setDoc.getDocSch();

							for (int i = 0; i < 7; i++) {
								System.out.print((i + 1) + ") " + (i + 1) + "부(" + timeSet[i] + ":00 ~ "
										+ (timeSet[i] + 1) + ":00) ");
								if (docSch[dayNum][i + 1] == 0 || docSch[dayNum][i + 1] > 3)
									System.out.println("(마감)");
								else
									System.out.println("(잔여: " + (4 - docSch[dayNum][i + 1]) + ")");
							}
							System.out.print("진료 예약을 할 시간를 골라주세요: ");
							input = scanner.nextLine();
							if (input.length() == 1) {
								if (input.equals("q") || input.equals("Q"))
									break;
								timeNum = Integer.parseInt(input);
								if (timeNum < 1 || timeNum > 7)
									throw new NumberFormatException();
							} else
								throw new NumberFormatException();

							if (setDoc.getDocSch()[dayNum][timeNum] == 0 || setDoc.getDocSch()[dayNum][timeNum] > 3) {
								System.out.println("마감된 예약 시간대 입니다. 다시 입력해주세요.");
								scanner.nextLine();
								continue;
							} else {
								for (User user : userList) {
									if (user.getId().equals(id)) {
										for (String[] resArr : user.getResList()) {
											if (resArr[0].equals(setDoc.getDepName())) {
												System.out.println("이미 예약 완료하신 진료입니다.");
												scanner.nextLine();
												continue B;
											}
										}

										if (user.getResList().size() == 3) {
											System.out.println("진료예약 가능횟수를 초과하였습니다.");
											scanner.nextLine();
											continue B;
										}
										user.addRes(depDocList.get(depNum)[0], depDocList.get(depNum)[docNum],
												Integer.toString(dayNum), Integer.toString(timeNum));
									}
								}

								setDoc.getDocSch()[dayNum][timeNum]++;
								setDoc.setDocSch(docSch);

								System.out.println("예약이 완료되었습니다. 사용자 메뉴로 돌아갑니다.");
								scanner.nextLine();
								break A;
							}

						} catch (NumberFormatException e) {
							System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
							scanner.nextLine();
							continue;
						}
					}
				}
			}
		}
	}

	public int showMyPage() {

		// 마이페이지 내용 출력
		// 예약 취소 가능 User class의 removeRes 활용
		// -> (편의 상 추가한 doctor.settingResP 사용 시)
		// removeRes() 활용 시 해당 Doctor의 해당 날짜의 해당 시간의 진료 예약 인원 -1
		// (해당의사객체.settingResP(day, time, -1))
		// 해당 Doctor의 스케줄 정보 수정

		try {
			user.sortRes();
			resList = user.getResList();

			System.out.println("*마이페이지(뒤로가기:q)*");
			System.out.print("비밀번호를 입력해주세요: ");
			String PW = scanner.nextLine();

			if (PW.equals("q")) {
				return 0;
			} else if (PW.equals(user.getPw())) {

				while (true) {
					System.out.println("*회원정보*");
					System.out.println("이름: " + this.user.getName());
					System.out.println("주민번호: " + this.user.getResinum());
					System.out.println("아이디: " + this.user.getId());

					System.out.println("*진료 예약 내역*");

					int[] timeSet = { 9, 10, 11, 14, 15, 16, 17 }; // 시간 정보
					LocalDate nowDate = LocalDate.now(); // 오늘 날짜

					int i = 0;

					for (String[] temp : resList) { // temp[0] = 진료 과 , temp[1] = 의사, temp[2] = 진료 날짜, temp[3]=진료 시간
						int date = Integer.parseInt(temp[2]);
						int time = Integer.parseInt(temp[3]);
						System.out
								.println((i + 1) + "." + temp[0] + " - " + temp[1] + " - " + nowDate.plusDays(date + 1)
										+ " - " + (timeSet[time - 1]) + ":00 ~ " + (timeSet[time - 1] + 1) + ":00");

						i++;
					}

					System.out.print("*진료 취소를 원할 시, 'C'를 입력해주세요.*");
					String cancel = scanner.nextLine();

					if (cancel.equals("C") || cancel.equals("c")) {
						System.out.print("몇 번째 진료를 취소하시겠습니까?");
						int cancelNum = Integer.parseInt(scanner.nextLine());

						if (1 <= cancelNum && cancelNum <= i) {

							for (Doctor dt : docList) {
								String[] temp = resList.get(cancelNum - 1);
								if (temp[1].equals(dt.getName())) {
									dt.getDocSch()[Integer.parseInt(temp[2])][Integer.parseInt(temp[3])]--;
									break;
								}
							}
							user.removeRes(cancelNum - 1); // 예약 정보 제거
							System.out.println("예약이 성공적으로 취소되었습니다.");

						} else {
							throw new NumberFormatException(); // cancelNum 잘못 입력했을 시
						}

					} else if (cancel.equals("q") || cancel.equals("Q"))
						break;

					else {
						throw new NumberFormatException(); // cancel 잘못 입력시
					}
				}

			} else {
				System.out.println("비밀번호가 틀렸습니다. 다시 입력해주세요");
			}

		} catch (NumberFormatException e) {
			System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
		}
		return 0;
	}
}