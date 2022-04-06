package kr.co.myProject.srv;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class BibleSrvImpl implements BibleSrv {

	// 성경의 기본적인 구성 정보 - 크롤링도 가능하지만 크롤링 복잡성과 요청 횟수를 줄이기 위해 별도로 정의
	final static String[] classification = {
		"창세기", "출애굽기", "레위기", "민수기", "신명기", "여호수아", "사사기", "룻기", "사무엘상", "사무엘하", "열왕기상", "열왕기하", 
		"역대상", "역대하", "에스라", "느헤미야", "에스더", "욥기", "시편", "잠언", "전도서", "아가", "이사야", "예레미야", "예레미야애가", "에스겔", 
		"다니엘", "호세아", "요엘", "아모스", "오바댜", "요나", "미가", "나훔", "하박국", "스바냐", "학개", "스가랴", "말라기", 
		"마태복음", "마가복음", "누가복음", "요한복음", "사도행전", "로마서", "고린도전서", "고린도후서", "갈라디아서", "에베소서", "빌립보서", "골로새서", 
		"데살로니가전서", "데살로니가후서", "디모데전서", "디모데후서", "디도서", "빌레몬서", "히브리서", "야고보서", "베드로전서", "베드로후서", 
		"요한1서", "요한2서", "요한3서", "유다서", "요한계시록"
	};
	final static String[] classificationsAcronyms = {
		"창", "출", "레", "민", "신", "수", "삿", "롯", "삼상", "삼하", "왕상", "왕하", "대상", "대하", "스", "느", "에", "욥", "시", "잠", "전",
		"아", "사", "렘", "애", "겔", "단", "호", "욜", "암", "옵", "욘", "미", "나", "합", "습", "학", "슥", "말", "마", "막", "눅", "요", 
		"행", "롬", "고전", "고후", "갈", "엡", "빌", "골", "살전", "살후", "딤전", "딤후", "딛", "몬", "히", "약", "벧전", "벧후", 
		"요1", "요2", "요3", "유", "계"
	};
	final static int[] classificationNum = {
		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
		2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2
	};
	final static String[] classificationInfo = {
		"gen 50", "exo 40", "lev 27", "num 36", "deu 34", "jos 24", "jdg 21", "rut 4", "1sa 31", "2sa 24", "1ki 22", "2ki 25", 
		"1ch 29", "2ch 36", "ezr 10", "neh 13", "est 10", "job 42", "psa 150", "pro 31", "ecc 12", "sng 8", "isa 66", "jer 52",
		"lam 5", "ezk 48", "dan 12", "hos 14", "jol 3", "amo 9", "oba 1", "jnh 4", "mic 7", "nam 3", "hab 3", "zep 3", "hag 2", 
		"zec 14", "mal 4", "mat 28", "mrk 16", "luk 24", "jhn 21", "act 28", "rom 16", "1co 16", "2co 13", "gal 6", "eph 6", 
		"php 4", "col 4", "1th 5", "2th 3", "1ti 6", "2ti 4", "tit 3", "phm 1", "heb 13", "jas 5", "1pe 5", "2pe 3", "1jn 5", 
		"2jn 1", "3jn 1", "jud 1", "rev 22"
	};
	
	@Override
	public LinkedHashMap<String, Object> bibleSearch(String vol, int chapter) {
		// TODO Auto-generated method stub
		String idx = null;
		System.out.println("VOL : "+vol);
		for (int i=0;i<classification.length;i++) {
			if (classification[i].equals(vol)) {
				idx = classificationInfo[i].split(" ")[0];
				break;
			}
		}
		
		return connectionWeb(idx, chapter);
	}

	/**
	 * GODpia에서 말씀 크롤링
	 * @param vol (String) : 성경 목차
	 * @param chapter (int) : 성경 장수
	 */
	// 신명기 6장 18절~19절이 연결되어있기 때문에 수정
	// private static HashMap<Integer, String> connectionWeb(String vol, int chapter) {
	private static LinkedHashMap<String, Object> connectionWeb(String vol, int chapter) {
		String url = "http://bible.godpia.com/read/reading.asp?ver=gae&ver2=&vol="+vol+"&chap="+chapter+"&sec=";
		System.out.println("URL : "+url);
		org.jsoup.Connection webConnection = Jsoup.connect(url);
		
		LinkedHashMap<String, Object> hm = new LinkedHashMap<String, Object>();
		
		try {
			Document document = webConnection.get();
			
			Iterator<Element> als = document.select(".wide p").iterator();
			
			while (als.hasNext()) {
				Element al = als.next();
				
				LinkedHashMap<String, Object> con = new LinkedHashMap<String, Object>();
				con.put("verseInt", Integer.parseInt(al.id().split("[_-]")[3]));
				con.put("verseStr", al.id().split("[_]")[3]);
				con.put("verse", al.text().replaceFirst(al.id().split("[_]")[3], ""));
				
				hm.put(al.id().split("[_-]")[3], con);
			}
								
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hm;
	}
}
