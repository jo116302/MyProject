package kr.co.myProject.controller;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.myProject.srv.BibleSrvImpl;

@Controller
@RequestMapping("/bible")
public class BibleController {

	@Autowired
	public BibleSrvImpl bibleSrvImpl;
	
	@ResponseBody
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public LinkedHashMap<String, Object> getBible(
			@RequestParam String vol, 
			@RequestParam int chapter
	) {
		return bibleSrvImpl.bibleSearch(vol, chapter);
	}
}
