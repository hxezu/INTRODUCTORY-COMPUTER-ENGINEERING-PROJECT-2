package mid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminFunc {

	private ArrayList<User> userList; // 전체 user list
	private ArrayList<Doctor> docList; // 전체 doctor list
	private Scanner scanner;
	ArrayList depList = new ArrayList();
	ArrayList docArray = new ArrayList();
	private ArrayList<String[]> resList;
	boolean Flag;

	public AdminFunc() {

	}

	public void startAdmin(ArrayList<User> uList, ArrayList<Doctor> dList) {

		userList = uList;
		docList = dList;

		scanner = new Scanner(System.in);

		System.out.println();
		System.out.println("====== 관리자 메뉴 ======");
		System.out.println("1)진료 관리 2)진료 관련 정보 조회 3)로그아웃 ");
		A: while (true) {

			// 관리자 메뉴 실행, 메인메뉴로 가기 전까지 무한 반복

			/*
			 * 1번 메뉴 선택 -> manageResMenu() 실행 2번 메뉴 선택 -> checkingMenu() 실행 3번 메뉴 선택 ->
			 * 메인메뉴로 가기 (while문 break), 로그아웃 시 이 클래스에서 수정된 doctor들의 정보 (Console 클래스에서도)
			 * 적용되어야 함
			 */

			try {
				System.out.print("메뉴를 입력하세요 : ");
				String input = scanner.nextLine();
				switch (input) {
				case "1":
					manageResMenu();
					break;
				case "2":
					checkingMenu();
					break;
				case "3":
					System.out.println("메인메뉴로 돌아갑니다.");
					break A;
				default:
					throw new InputMismatchException("1~3 사이의 숫자를 입력해주세요.");
				}
				
				System.out.println();
				System.out.println("====== 관리자 메뉴 ======");
				System.out.println("1)진료 관리 2)진료 관련 정보 조회 3)로그아웃 ");

			} catch (InputMismatchException e) {
				System.err.println("1~3 사이의 숫자를 입력해주세요.");
				continue A;
			}
		}
	}

	public void manageResMenu() { // 관리자 메뉴 -> 1. 진료 관리

		System.out.println();
		System.out.println("====== 진료 관리 메뉴 ======");
		System.out.println("1)의사 명단 관리 2)의사별 시간표 관리 3)뒤로가기");

		B: while (true) {

			// 진료 관리 메뉴 실행, 특정 상황 전까지 무한 반복 (이전 관리자 메뉴로 돌아가거나, 또는 2번 메뉴 의사별 시간표 관리 종료 시 이전
			// 관리자 메뉴로)

			// 1번 메뉴 선택 -> manageDocList() 실행 2번 메뉴 선택 -> manageTimetable() 실행

			try {
				System.out.print("메뉴를 입력하세요 : ");
				String input = scanner.nextLine();
				switch (input) {
				case "1":
					manageDocList();
					break;
				case "2":
					manageTimetable();
					break;
				case "3":
					System.out.println("관리자 메뉴로 돌아갑니다.");
					break B;
				default:
					throw new InputMismatchException("1~3 사이의 숫자를 입력해주세요.");
				}
				System.out.println();
				System.out.println("====== 진료 관리 메뉴 ======");
				System.out.println("1)의사 명단 관리 2)의사별 시간표 관리 3)뒤로가기 ");
			} catch (InputMismatchException e) {
				System.err.println("1~3 사이의 숫자를 입력해주세요.");
				continue B;
			}
		}
	}

	public void manageDocList() { // 의사 명단 관리 메뉴

		// 1번 메뉴 선택 -> addDoctor() 실행 2번 메뉴 선택 -> removeDoctor() 실행

		A: while (true) {
			System.out.println();
			System.out.println("====== 의사 명단 관리 메뉴 ======");
			System.out.println("1)추가 2)삭제 3)뒤로가기 ");
			System.out.print("메뉴를 입력하세요 : ");
			String input = scanner.nextLine();
			while (true) {
				if (input.equals("1") || input.equals("2") || input.equals("3"))
					break;
				System.out.println("1~3 사이의 숫자를 입력해주세요.");
				System.out.print("메뉴를 입력하세요 : ");
				input = scanner.nextLine();
			}

			switch (input) {
			case "1":
				boolean ok1 = addDoctor();
				if (ok1)
					break A;
				else
					continue A;
			case "2":
				boolean ok2 = removeDoctor();
				if (ok2)
					break A;
				else
					continue A;
			case "3":
				System.out.println("진료 관리 메뉴로 돌아갑니다.");
				break A;
			}
		}
	}

	public boolean addDoctor() { // 의사 추가
		A: while (true) {
			System.out.println();
			System.out.println("------새로운 의사 등록(뒤로가기:q)------");
			printDepList();

			// 진료과 선택 -> 의사 이름 입력
			String input = "";

			B: while (true) {
				input = scanner.nextLine();
				if (input.equals("q")) {
					System.out.println("의사 명단 관리 메뉴로 돌아갑니다.");
				} else if (input.length() != 0) {
					try {
						if (Integer.parseInt(input) > 0 && Integer.parseInt(input) <= depList.size()) {
							Object depName = depList.get(Integer.parseInt(input) - 1);
							String depNameC = depName.toString();
							System.out.print("새롭게 등록할 의사의 이름을 입력해주세요 : ");
							String newDocName = scanner.nextLine();

							String match = "[^\uAC00-\uD7A3]";
							while (newDocName.length() == 0 || !(newDocName.equals(newDocName.replaceAll(match, "")))) {
								if (newDocName.equals("q")) {
									System.out.println("진료과 선택 화면으로 돌아갑니다.");
									continue A;
								}
								System.out.print("올바른 의사의 이름을 입력해주세요: ");
								newDocName = scanner.nextLine();
							}

							docList.add(new Doctor(depNameC, newDocName));
							System.out.println("성공적으로 추가되었습니다.");
							System.out.println(depNameC + " 의사 명단");
							for (Doctor d : docList) {
								if (d.getDepName().equals(depNameC)) {
									System.out.println("- " + d.getName());
								}
							}
							return true;

						} else {
							throw new NumberFormatException();
						}
					} catch (NumberFormatException e) {
						System.out.print("해당 과가 존재하지 않습니다. 다시 입력하세요 :  ");
						continue B;
					}

				} else {
					System.out.print("해당 과가 존재하지 않습니다. 다시 입력하세요 :  ");
					continue B;
				}
				break B;
			}
			return false;
		}
	}

	public boolean removeDoctor() { // 의사 삭제

		String[] list = new String[1000];

		int i = 0;
		for (Doctor d : docList) {
			list[i] = d.getDepName();
			i++;
		}
		for (int k = 0; k < list.length; k++) { // 중복제거
			if (!depList.contains(list[k]) && list[k] != null)
				depList.add(list[k]);
		}

		A: while (true) {
			System.out.println();
			System.out.println("------기존 의사 삭제(뒤로가기:q)------");
			for (int l = 0; l < depList.size(); l++) {
				System.out.println((l + 1) + ")" + depList.get(l));
			}

			// 진료과 선택
			System.out.print("진료과목을 선택해주세요 : ");

			// 진료과 선택 -> 의사 이름 입력
			String input = "";
			B: while (true) {
				input = scanner.nextLine();
				if (input.equals("q")) {
					System.out.println("의사 명단 관리 메뉴로 돌아갑니다.");
				} else if (input.length() != 0) {
					try {
						if (Integer.parseInt(input) > 0 && Integer.parseInt(input) <= depList.size()) {
							Object depName = depList.get(Integer.parseInt(input) - 1);
							String depNameC = depName.toString();

							ArrayList<String> list1 = new ArrayList<>();
							for (Doctor d : docList) {
								if (d.getDepName().equals(depNameC)) {
									list1.add(d.getName());
								}
							}

							for (int k = 0; k < list1.size(); k++) { // 중복제거
								if (!docArray.contains(list1.get(k)) && list1.get(k) != null)
									docArray.add(list1.get(k));
							}
							for (int l = 0; l < docArray.size(); l++) {
								System.out.println((l + 1) + ")" + docArray.get(l));
							}
							System.out.print("기능을 수행할 의사를 선택해주세요 : ");
							String delDocName = "";

							C: while (true) {
								delDocName = scanner.nextLine();
								if (delDocName.equals("q")) {
									System.out.println("진료과 선택 화면으로 돌아갑니다.");
									docArray.clear();
									continue A;
								} else if (delDocName.length() != 0) {
									try {
										if (Integer.parseInt(delDocName) >= 1
												&& Integer.parseInt(delDocName) <= docArray.size()) {
											for (Doctor d : docList) {
												if (docArray.get(Integer.parseInt(delDocName) - 1)
														.equals(d.getName())) {
													docList.remove(docList.indexOf(d));

													// 해당 의사 앞으로 배정된 환자 예약 모두 삭제
													for (User u : userList) {
														ArrayList<Integer> tempAL = new ArrayList<>();
														for (String[] temp : u.getResList()) {
															if (temp[1].equals(d.getName())) {
																tempAL.add(u.getResList().indexOf(temp));
															}
														}
														Collections.reverse(tempAL);
														for (Integer inn : tempAL) {
															u.getResList().remove((int) inn);
														}
													}
													break;
												}
											}
											System.out.println("성공적으로 삭제되었습니다.");
											System.out.println(depNameC + " 의사 명단");
											for (Doctor d : docList) {
												if (d.getDepName().equals(depNameC)) {
													System.out.println("- " + d.getName());
												}
											}
											docArray.clear();
											return true;
										} else {
											System.out.print("해당하는 의사가 없습니다. 다시 입력해주세요  : ");
											continue C;
										}
									} catch (NumberFormatException e) {
										System.out.print("해당하는 의사가 없습니다. 다시 입력해주세요  : ");
										continue C;
									}

								} else {
									System.out.print("해당하는 의사가 없습니다. 다시 입력해주세요  : ");
									continue C;
								}
							}

						} else {
							throw new NumberFormatException();
						}
					} catch (NumberFormatException e) {
						System.out.print("해당 과가 존재하지 않습니다. 다시 입력해주세요  : ");
						continue B;
					}

				} else {
					System.out.print("해당 과가 존재하지 않습니다. 다시 입력해주세요  : ");
					continue A;
				}
				break;
			}
			docArray.clear();
			return false;
		}

	}

	public void manageTimetable() { // 의사별 시간표 관리 메뉴

		A: while (true) {
			// 의사 이름 입력 -> 날짜 선택 -> on/off할 시간 선택
			System.out.println("------의사별 시간표 관리(뒤로가기:q)------");

			// 의사 출력
			System.out.print("기능을 수행할 의사의 이름을 입력해주세요 : ");
			String input = scanner.nextLine();
			boolean inputOK = false;
			if (input.equals("q")) {
				System.out.println("진료 관리 메뉴로 돌아갑니다. ");
				break A;
			} else {
				for (Doctor d : docList) {
					if (input.equals(d.getName())) {
						Flag = true;
						inputOK = true;
						int tempDay = detailTimetable(printDaytable(d));
						if (tempDay == 0) {
							break;
						}
						onOffFunc(d, tempDay);
						break;
					}
				}
				if (!inputOK) {
					System.out.println("해당 이름의 의사가 존재하지 않습니다.");
				}
				continue A;
			}
		}
		docArray.clear();

	}

	public void onOffFunc(Doctor d, int day) { // onoff 기능 함수
		int[][] schArray = new int[7][8];
		schArray = d.getDocSch();
		/* 날짜 출력 */

		int time = 0;

		D: while (true) {
			try {
				System.out.print("스케줄을 변경할 타임을 입력하세요 (뒤로가기-q) : ");
				String timeTemp = scanner.nextLine();
				if (timeTemp.equals("q")) {
					break;
				} else {
					time = Integer.parseInt(timeTemp);
					if (!(time >= 1 && time <= 7))
						throw new InputMismatchException();
					else {
						if (d.getDocSch()[day - 1][time] == 0 || schArray[day - 1][time] == 1) {
							boolean isItOn = d.changeSch(day, time);
							if (isItOn) {
								System.out.println("성공적으로 ON 되었습니다. ");
							}
							else {
								System.out.println("성공적으로 OFF 되었습니다. ");
							}
						} else
							System.out.println("해당 시간대는 OFF가 불가능합니다. ");
						break D;
					}
				}

			} catch (InputMismatchException e) {
				System.err.println("1~7 사이의 숫자를 입력해주세요.");
			} catch (NumberFormatException e) {
				System.err.println("1~7 사이의 숫자를 입력해주세요.");
			}
		}
	}

	public void checkingMenu() { // 관리자 메뉴 -> 2. 조회

		// 1번 메뉴 선택 -> checkTimetable() 실행 2번 메뉴 선택 -> checkUser() 실행

		System.out.println();
		System.out.println("====== 진료 관련 정보 조회 메뉴 ======");
		System.out.println("1)의사 시간표 조회 2)사용자 정보 조회 3)뒤로가기 ");
		System.out.print("메뉴를 입력하세요 : ");
		String input = scanner.nextLine();
		while (true) {
			if (input.equals("1") || input.equals("2") || input.equals("3"))
				break;
			System.out.println("1~3 사이의 숫자를 입력해주세요.");
			System.out.print("메뉴를 입력하세요 : ");
			input = scanner.nextLine();
		}
		switch (input) {
		case "1":
			checkTimetable();
			break;
		case "2":
			checkUser();
			break;
		case "3":
			System.out.println("관리자 메뉴로 돌아갑니다.");
			break;
		}
	}

	public void checkTimetable() {
		// 진료과 선택 -> 의사 선택 -> printTimetable(doc) 실행
		A: while (true) {
			System.out.println();
			System.out.println("------의사 시간표 조회(뒤로가기:q)------");
			Flag = false;
			// 의사 출력
			System.out.print("조회할 의사의 이름을 입력해주세요 : ");
			String input = scanner.nextLine();
			boolean inputOK = false;
			if (input.equals("q")) {
				System.out.println("진료 관련 정보 조회 메뉴로 돌아갑니다.");
				break A;
				// checkingMenu();
			} else {
				for (Doctor d : docList) {
					if (input.equals(d.getName())) {
						detailTimetable(printDaytable(d));
						inputOK = true;
						break;
					}
				}
				if (!inputOK) {
					System.out.println("해당 이름의 의사가 존재하지 않습니다.");
				}
				docArray.clear();
				continue A;
			}
		}

	}

	public void printDepList() {
		String[] list = new String[1000];

		int i = 0;
		for (Doctor d : docList) {
			list[i] = d.getDepName();
			i++;
		}
		for (int k = 0; k < list.length; k++) { // 중복제거
			if (!depList.contains(list[k]) && list[k] != null)
				depList.add(list[k]);
		}
		for (int l = 0; l < depList.size(); l++) {
			System.out.println((l + 1) + ")" + depList.get(l));
		}
		// 진료과 선택
		System.out.print("진료과목을 선택해주세요 : ");
	}

	public int[][] printDaytable(Doctor d) {
		System.out.println();
		// 해당 의사의 진료 시간표 출력
		int[][] schArray = new int[7][8];

		LocalDate nowDate = LocalDate.now();
		// 해당 의사의 진료 시간표 출력
		schArray = d.getDocSch();

		for (int i = 0; i < 7; i++) {
			System.out.println((i + 1) + "." + nowDate.plusDays(schArray[i][0] - 1));

		}
		return schArray;
	}

	public int detailTimetable(int[][] schArray) {
		int day = 0, time = 0;
		int[] timeSet = { 9, 10, 11, 14, 15, 16, 17 };
		System.out.print("날짜를 선택하세요 (뒤로가기-q): ");
		C: while (true) {
			try {
				String dayTemp = scanner.nextLine();

				if (dayTemp.equals("q")) {
					return 0;
				} else {
					day = Integer.parseInt(dayTemp);
					if (!(day >= 1 && day <= 7))
						throw new InputMismatchException();
					else {
						for (int j = 1; j < 8; j++) {
							if (schArray[day - 1][j] == 0)
								System.out.println(j + "부 (" + timeSet[j - 1] + ":00 ~ " + (timeSet[j - 1] + 1) + ":00)"
										+ "-OFF ");
							else if (schArray[day - 1][j] == 4)
								System.out.println(j + "부 (" + timeSet[j - 1] + ":00 ~ " + (timeSet[j - 1] + 1) + ":00)"
										+ "-(마감)");
							else
								System.out.println(j + "부 (" + timeSet[j - 1] + ":00 ~ " + (timeSet[j - 1] + 1) + ":00)"
										+ "-(잔여 : " + (4 - schArray[day - 1][j]) + "명)");
						}
					}
				}

			} catch (InputMismatchException | NumberFormatException e) {
				System.err.print("1~7 사이의 숫자를 입력해주세요.");
				continue C;
			}

			System.out.print(" ");
			System.out.println();
			return day;
		}
	}

	public void checkUser() {

		A: while (true) {
			System.out.println();
			System.out.println("------사용자 조회(뒤로가기:q)------");
			System.out.print("조회할 사용자의 이름을 입력해주세요 : ");
			String inputUser = scanner.nextLine();
			boolean inputOK = false;

			if (inputUser == "q") {
				break A;
			}
			// 사용자 이름 입력 -> 해당 사용자 명단 출력 -> printUser(us) 실행
			else {
				for (User u : userList) {
					if (inputUser.equals(u.getName())) {
						printUser(inputUser);
						inputOK = true;
						break;
					}
				}
				if (!inputOK) {
					System.out.println("해당 이름의 사용자가 존재하지 않습니다.");
				}
				continue A;
			}
		}

	}

	public void printUser(String inputUser) {
		// 해당 사용자의 정보 출력
		int i = 0;
		int[] timeSet = { 9, 10, 11, 14, 15, 16, 17 };
		LocalDate nowDate = LocalDate.now(); // 오늘 날짜
		System.out.println();

		for (User u : userList) {
			if (u.getName().equals(inputUser)) {
				System.out.println("* 회원 정보 *");
				System.out.println("이름 : " + u.getName());
				System.out.println("주민번호 : " + u.getResinum());
				System.out.println("* 진료 예약 내역 *");
				resList = u.getResList();
				for (String[] temp : resList) {
					int date = Integer.parseInt(temp[2]);
					int time = Integer.parseInt(temp[3]);
					System.out.println((i + 1) + "." + temp[0] + " - " + temp[1] + " - " + nowDate.plusDays(date + 1)
							+ " - " + (timeSet[time - 1]) + ":00 ~ " + (timeSet[time - 1] + 1) + ":00");
					i++;
				}
			}
		}
	}
}
