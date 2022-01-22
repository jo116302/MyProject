package kr.co.myProject.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
@RequestMapping(value = "/tc")
public class TestController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@ResponseBody
	@RequestMapping(value = "/json", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	public String customJson(
			@RequestParam(defaultValue = "") String query
			, @RequestParam(defaultValue = "") String startCount
			, @RequestParam(defaultValue = "") String listCount
			, @RequestParam(defaultValue = "") String collection
			) {
		// TODO Auto-generated method stub
		StringBuffer res = new StringBuffer();
		
		try {
			URL url = new URL("http://sf-1.duckdns.org:7900/json/jsp/search_json.jsp");
			
			// *.openConnection()은 해당 url로 연결이 아닌 연결 설정을 위한 인스턴스 생성과정의 메소드이다.
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			
			// 요청 파라미터 보내기
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			
			StringBuffer parameter = new StringBuffer();
			parameter.append("query=").append(query).append('&');
			parameter.append("startCount=").append(startCount).append('&');
			parameter.append("listCount=").append(listCount).append('&');
			parameter.append("collection=").append(collection);
			
			logger.info("Parameter : " + parameter.toString());
			
			bw.write(parameter.toString());
			bw.flush();
			bw.close();
			
			// 응답 읽어오기
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			String read = null;
			StringBuffer sb = new StringBuffer();
			while ((read = br.readLine()) != null) {
				sb.append(read).append(System.getProperty("line.separator"));
			}
			
			br.close();
			
			// JSON 수정하기
			JsonParser jp = new JsonParser();
			JsonObject searchQueryResult = (JsonObject) jp.parse(sb.toString()); 
			
			System.out.println("SearchQueryResult ===================");
			/*
			 * Class : class com.google.gson.JsonObject
			 * 
			 * JSON의 특정 Key를 한번에 반복적으로 수행하고 싶은 경우에는 *.get().getAsJsonObject() 와 같이 getAsJsonObject를 붙여준다. 
			 * 한줄이 아닌 경우 아래와 같이 반복적으로 JsonObject 변수에 저장해주면서 수행해야된다.
			 *  - searchQueryResult = (JsonObject) searchQueryResult.get("SearchQueryResult");
			 *  - searchQueryResult = (JsonObject) searchQueryResult.get("Collection");
			 */
			
			JsonArray ja = (JsonArray) searchQueryResult.get("SearchQueryResult").getAsJsonObject().get("Collection");
			JsonObject returnObject = searchQueryResult.deepCopy();;
			
			for (int depth=ja.size()-1;depth>-1;depth--) {
				System.out.println("--------- 제 "+depth+" depth 입니다. ---------");
				System.out.println("----- Collection Name은 "+ja.get(depth).getAsJsonObject().get("Id")+" 입니다. -----");
				
				int count = ja.get(depth).getAsJsonObject().get("DocumentSet").getAsJsonObject().get("Count").getAsInt();
				if (count > 0) {
					System.out.println("----- 검색 결과 "+count+"건이므로 삭제했습니다. -----");
					System.out.println(ja.get(depth));
				} else {
					System.out.println("----- 검색 결과 0건이므로 삭제했습니다. -----");
					
					returnObject.get("SearchQueryResult").getAsJsonObject().get("Collection").getAsJsonArray().remove(depth);
					System.out.println(searchQueryResult);
				}
			}
			
			System.out.println("===================");
			
			res.append(returnObject.toString()).append(System.getProperty("line.separator"));
			res.append(System.getProperty("line.separator")).append("Request Parameter : ").append(parameter);
			res.append(System.getProperty("line.separator")).append("Response Status Code : ").append(conn.getResponseCode());
			res.append(System.getProperty("line.separator")).append("Response getHeaderFields : ").append(conn.getHeaderFields());
			conn.disconnect();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info(res.toString());
		
		return res.toString(); 
	}

}
