package kr.co.myProject.srv;

import java.util.LinkedHashMap;

public interface BibleSrv {

	public LinkedHashMap<String, Object> bibleSearch(String vol, int chapter);
}
