package kr.co.myProject.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import kr.co.myProject.srv.Youtube2Srv;
import kr.co.myProject.srv.Youtube2SrvImpl;

@Controller
@RequestMapping("/youtube2")
public class youtube2Controller {
	
	@Resource(name = "youtubeSrv")
	public Youtube2Srv youtubeSrv;
	
	private static final Logger logger = LoggerFactory.getLogger(youtube2Controller.class);

	/*
	 * Crawling으로 youtube 동영상 콘텐츠 정보 획득 후 json으로 반환
	 * 
	 * @id : youtube video id
	 */
	@RequestMapping(value = "/crawling/{id}", method = RequestMethod.GET)
	@ResponseBody
	// public HashMap<String, Object> getYoutubeCrawling(@RequestParam String id) {
	public HashMap<String, Object> getYoutubeCrawling(@PathVariable String id) {
		HashMap<String, Object> hm = new HashMap<String, Object>();

		String youtubeId = id;
		try {
			Document document = Jsoup.connect("https://www.youtube.com/watch?v=" + youtubeId).get();

			hm.put("kakao_title", document.select("meta[property=og:title]").first().attr("content"));
			hm.put("kakao_img", document.select("meta[property=og:image]").first().attr("content"));
			hm.put("kakao_description", document.select("meta[property=og:description]").first().attr("content"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hm;
	}

	/*
	 * youtube api description
	 * 
	 * Search:list api - channelid를 입력받아 업로드 되어있는 동영상 정보들을 json으로 획득 - cost : 100
	 * PlaylistItems: list api - playid를 입력받아 구성되어있는 재생목록에 있는 동영상 정보들을 json으로 획득 -
	 * cost : 1
	 */

	@RequestMapping(value = "/api/getvideoinfo/{id}")
	@ResponseBody
	public HashMap<String, Object> getYoutubeApiVideoInfo(@PathVariable String id) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		
		hm.put("videoInfo", youtubeSrv.videoInfo(id));
		if (Youtube2SrvImpl.PROPERTIES_FILENAME.equals("youtube.key1") && (youtubeSrv.videoInfo(id).get(0).containsKey("ErrorReason") ? youtubeSrv.videoInfo(id).get(0).get("ErrorReason").equals("quotaExceeded") : false)) {
			Youtube2SrvImpl.PROPERTIES_FILENAME = "youtube.key2";
			
			hm = new HashMap<String, Object>();
			hm.put("videoinfo", youtubeSrv.videoInfo(id));
		} else if (youtubeSrv.videoInfo(id).get(0).containsKey("ErrorReason")) {
			logger.error("■■■■■ YoutubevideoInfo key : "+Youtube2SrvImpl.PROPERTIES_FILENAME+", ErrorReason : "+hm.containsKey("ErrorReason"));
			hm.put("Error", youtubeSrv.videoInfo(id).get(0).get("error"));
		}
		
		logger.info("■■■■■ YoutubevideoInfo key : "+Youtube2SrvImpl.PROPERTIES_FILENAME);

		return hm;
	}

	@RequestMapping(value = "/api/playlist/{id}")
	@ResponseBody
	public HashMap<String, Object> getYoutubePlaylist(@PathVariable String id) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		
		int idx = 1;
		String[] keys = {"youtube.key1", "youtube.key2", "youtube.key3"};
		
		hm.put("videoinfo", youtubeSrv.playlist(id));
		if ((youtubeSrv.playlist(id).get(0).containsKey("ErrorReason") ? youtubeSrv.playlist(id).get(0).get("ErrorReason").equals("quotaExceeded") : false) && idx < 3) {
			idx++;
			Youtube2SrvImpl.PROPERTIES_FILENAME = keys[idx];
			
			hm = new HashMap<String, Object>();
			hm.put("videoinfo", youtubeSrv.playlist(id));
		} else if (youtubeSrv.playlist(id).get(0).containsKey("ErrorReason")) {
			logger.error("■■■■■ YoutubePlayList key : "+Youtube2SrvImpl.PROPERTIES_FILENAME+", ErrorReason : "+hm.containsKey("ErrorReason"));
			hm.put("Error", youtubeSrv.playlist(id).get(0).get("error"));
		}
		
		logger.info("■■■■■ YoutubePlayList key : "+Youtube2SrvImpl.PROPERTIES_FILENAME);

		return hm;
	}
}
