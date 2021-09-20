package kr.co.myProject.srv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

@Service("youtubeSrv")
@PropertySource("classpath:/profile/youtube.properties")
public class Youtube2SrvImpl implements Youtube2Srv {
	/** --------------------------------- s:Youtube API --------------------------------- **/
	/** Global instance properties filename. */
	/*
	 * youtube.key 당 할당량 10,000이며, search는 100이기 때문에 key를 2개로 분리
	 */
	/*
	@Value("${youtube.key}")
	private String PROPERTIES_FILENAME;
	*/
	public static String PROPERTIES_FILENAME="youtube.key1";

	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/**
	 * Global instance of the max number of videos we want returned (50 = upper
	 * limit per page).
	 */
	private static final long NUMBER_OF_VIDEOS_RETURNED = 50;

	/** Global instance of Youtube object to make all API requests. */
	private static YouTube youtube;
	/** --------------------------------- e:Youtube API --------------------------------- **/
	@Autowired
	private Environment environment;		// 빈 주입을 받습니다.

    private String getUtil(String key){
        return environment.getProperty(key);
    }

	@Override
	public ArrayList<HashMap<String, Object>> playlist(String id) {
		// TODO Auto-generated method stub
		String apiKey = getUtil(PROPERTIES_FILENAME);
		ArrayList<HashMap<String, Object>> videos = new ArrayList<HashMap<String,Object>>();
		
		try {
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
				public void initialize(HttpRequest request) throws IOException {
				}
			}).setApplicationName("youtube-cmdline-search-sample").build();

			YouTube.Search.List search = youtube.search().list("id,snippet");
			search.setKey(apiKey);
			search.setChannelId(id);
			search.setOrder("date");
			search.setType("video");

			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/description,snippet/thumbnails/high/url)");
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
			SearchListResponse searchResponse = search.execute();

			List<SearchResult> searchResultList = searchResponse.getItems();
			
			if (searchResponse != null) {
				Iterator<SearchResult> iteSearchResult = searchResultList.iterator();
				
				while (iteSearchResult.hasNext()) {
					HashMap<String, Object> videoinfo = new HashMap<String, Object>();
					SearchResult singleVideo = iteSearchResult.next();
					ResourceId rId = singleVideo.getId();

					// Double checks the kind is video.
					if (rId.getKind().equals("youtube#video")) {
						Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("high");
						videoinfo.put("Video_Id", rId.getVideoId());
						videoinfo.put("Title", singleVideo.getSnippet().getTitle());
						videoinfo.put("Thumbnail", thumbnail.getUrl());
						videoinfo.put("Description", singleVideo.getSnippet().getDescription());
					}
					
					videos.add(videoinfo);
				}
			}
		} catch (GoogleJsonResponseException e) {
			HashMap<String, Object> error = new HashMap<String, Object>();
			error.put("errorCode", e.getDetails().getCode());
			error.put("ErrorReason", e.getDetails().getErrors().get(0).getReason());
			error.put("error", "There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
			videos.add(error);
		} catch (IOException e) {
			HashMap<String, Object> error = new HashMap<String, Object>();
			error.put("error", "There was an IO error: " + e.getCause() + " : " + e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return videos;
	}

	@Override
	public ArrayList<HashMap<String, Object>> videoInfo(String id) {
		// TODO Auto-generated method stub
		String apiKey = getUtil(PROPERTIES_FILENAME);
		ArrayList<HashMap<String, Object>> videos = new ArrayList<HashMap<String,Object>>();
		
		try {
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
				public void initialize(HttpRequest request) throws IOException {
				}
			}).setApplicationName("youtube-cmdline-videoList-sample").build();
			
			YouTube.Videos.List video = youtube.videos().list("id,snippet");
			video.setKey(apiKey);
			video.setId(id);
			video.setFields("items(id,kind,snippet/title,snippet/description,snippet/thumbnails/high/url)");
			video.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
			VideoListResponse videoResponse = video.execute();
			
			List<Video> videoResultList = videoResponse.getItems();
			
			if (videoResponse != null) {
				Iterator<Video> iteVideoResult = videoResultList.iterator();
				
				while (iteVideoResult.hasNext()) {
					HashMap<String, Object> videoinfo = new HashMap<String, Object>();
					Video singleVideo = iteVideoResult.next();
					System.out.println(singleVideo.toString());

					// Double checks the kind is video.
					if (singleVideo.getKind().equals("youtube#video")) {
						Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("high");
						videoinfo.put("Video_Id", singleVideo.getId());
						videoinfo.put("Title", singleVideo.getSnippet().getTitle());
						videoinfo.put("Thumbnail", thumbnail.getUrl());
						videoinfo.put("Description", singleVideo.getSnippet().getDescription());
					}
					
					videos.add(videoinfo);
				}
			}
			
		} catch (GoogleJsonResponseException e) {
			HashMap<String, Object> error = new HashMap<String, Object>();
			error.put("errorCode", e.getDetails().getCode());
			error.put("ErrorReason", e.getDetails().getErrors().get(0).getReason());
			error.put("error", "There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
			videos.add(error);
		} catch (IOException e) {
			HashMap<String, Object> error = new HashMap<String, Object>();
			error.put("error", "There was an IO error: " + e.getCause() + " : " + e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return videos;
		
		/*
		String strUrl = "https://www.googleapis.com/youtube/v3/videos?key="+apiKey+"&id="+id+"&part=snippet";
		System.out.println("strUrl : "+strUrl);
		StringBuffer sbReceptionJson = new StringBuffer();
		
		HttpURLConnection youtubeConn = null;
		
		try {
			URL youtubeVideoInfoUrl = new URL(strUrl);
			youtubeConn = (HttpURLConnection) youtubeVideoInfoUrl.openConnection();
			youtubeConn.setRequestProperty("Content-Type", "application/json");
			youtubeConn.setRequestMethod("GET");
			
			youtubeConn.setDoOutput(true);
			youtubeConn.setDoInput(true);
			youtubeConn.setUseCaches(false);
			youtubeConn.setDefaultUseCaches(false);
			youtubeConn.setConnectTimeout(2000);
			youtubeConn.setReadTimeout(2000);
			
			if (youtubeConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String receptionJson = "";
				
				BufferedReader in = new BufferedReader(new InputStreamReader(youtubeConn.getInputStream(), "UTF-8"));
				while ((receptionJson = in.readLine()) != null) {
					sbReceptionJson.append(receptionJson);
				}
				in.close();
			}
		} catch (Exception e) {
			sbReceptionJson.append("{\"error:\"+e.getCause()+\" \"+e.getMessage()}");
		}
		
		
		return sbReceptionJson.toString();
		*/
	}

	@Override
	public String youtubeIdEnroll(String organization, String method, String id) {
		// TODO Auto-generated method stub
		return null;
	}
}