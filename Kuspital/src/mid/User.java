package mid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class User {
	private String id;
	private String pw;
	private String name;
	private String resinum;
	private ArrayList<String[]> resList;

	public User(String id, String pw, String name, String resinum) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.resinum = resinum;
		resList = new ArrayList<String[]>();
	}

	// getter, setter func

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResinum() {
		return resinum;
	}

	public void setResinum(String resinum) {
		this.resinum = resinum;
	}

	public ArrayList<String[]> getResList() {
		return resList;
	}

	public void setResList(ArrayList<String[]> resList) {
		this.resList = resList;
	}

	// add reservation
	public void addRes(String depName, String docName, String resDay, String resTime) {
		String[] temp = new String[] { depName, docName, resDay, resTime };
		resList.add(temp);
	}

	// delete reservation info
	public void removeRes(int i) {
		resList.remove(i);

	}

	public void sortRes() {
		Collections.sort(resList, new SortResComparator());
	}

	class SortResComparator implements Comparator<String[]> {

		@Override
		public int compare(String[] o1, String[] o2) {
			if (Integer.parseInt(o1[2]) > Integer.parseInt(o2[2])) {
				return 1;
			} else if (Integer.parseInt(o1[2]) < Integer.parseInt(o2[2])) {
				return -1;
			} else {
				if (Integer.parseInt(o1[3]) > Integer.parseInt(o2[3])) {
					return 1;
				} else if (Integer.parseInt(o1[3]) < Integer.parseInt(o2[3])) {
					return -1;
				} else
					return 0;
			}
		}
	}
}
