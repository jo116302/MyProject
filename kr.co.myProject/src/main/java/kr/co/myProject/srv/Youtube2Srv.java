package kr.co.myProject.srv;

import java.util.ArrayList;
import java.util.HashMap;

public interface Youtube2Srv {
	public String youtubeIdEnroll(String organization, String method, String id);
	public ArrayList<HashMap<String, Object>> playlist(String id);
	public ArrayList<HashMap<String, Object>> videoInfo(String id);
}
