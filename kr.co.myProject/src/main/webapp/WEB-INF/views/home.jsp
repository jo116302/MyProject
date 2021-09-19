<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <!-- 추가할부분 --> <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ page session="false" %>
<!doctype html>
<html lang="ko_KR">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author"	content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
<meta name="generator" content="Hugo 0.84.0">

<meta property="og:type" content="website">
<meta property="og:title" content="유튜브 원클릭 시스템">
<meta property="og:description" content="YouTube Forwarder System">
<meta property="og:image" content="http://youtube2.kr/logo2.jpg">
<meta property="og:url" content="http://youtube2.kr/y/">
<meta property="og:site_name" content="홈즈와이">
<meta property="og:locale" content="ko_KR">


<title>유튜브 주소생성기</title>
<!--
<link rel="canonical" href="https://getbootstrap.com/docs/5.0/examples/sign-in/">
<link rel="apple-touch-icon" sizes="180x180" href="http://youtube2.kr/ss/apple-touch-icon.png">
<link rel="icon" type="image/png" sizes="32x32"	href="http://youtube2.kr/ss/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="16x16"	href="http://youtube2.kr/ss/favicon-16x16.png">
<link rel="manifest" href="http://youtube2.kr/ss/site.webmanifest"> 
<link rel="mask-icon" href="http://youtube2.kr/ss/safari-pinned-tab.svg" color="#5bbad5">
<meta name="msapplication-TileColor" content="#da532c">
<meta name="theme-color" content="#ffffff">


<link rel="manifest" href="manifest.json">
-->

<!-- kako sdk -->
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<!-- ajax api -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

<!-- Bootstrap core CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
	crossorigin="anonymous"></script>


<style>
.bd-placeholder-img {
	font-size: 1.125rem;
	text-anchor: middle;
	-webkit-user-select: none;
	-moz-user-select: none;
	user-select: none;
}

@media ( min-width : 768px) {
	.bd-placeholder-img-lg {
		font-size: 3.5rem;
	}
}
</style>


<!-- Custom styles for this template -->
<link href="http://youtube2.kr/signin.css" rel="stylesheet">
</head>

<script type="text/javascript">
createUrl = () => {
   let httpYoutubeLinkCreate = getParameter('v');
   crawling(httpYoutubeLinkCreate);
   let printUrl = '<br />유튜브 원클릭 주소가 복사 되었습니다.<br />' + '<div id="link">http://youtube2.kr/homezy/?y=' + httpYoutubeLinkCreate + '</div>' + '<span class="sociallink ml-1"><a href="javascript:KakaoToalk_Share()" id="KakaoToalk_Share" title="카카오톡으로 공유"><img src="http://youtube2.kr/y/kakao_li_2.png" alt="" width="300" height="45" /></a></span>';

   if (httpYoutubeLinkCreate == '') {
      document.getElementById('youtubeUrl').focus();
      alert('Youtube URL를 넣어주세요.');
   } else {
      document.getElementById("createUrlPrint").innerHTML=printUrl;
      // url 복사 기능까지 사용하시려면 copyUrl()을 그냥 두시고, 그렇지 않으면 주석이나 지워도 될 것같아요.
      copyUrl();
   }
}

// 유튜브 크롤링
crawling = (httpYoutubeLinkCreate) => {
        $.ajax({
                // url은 임시로 사용
                url : 'http://34.135.199.229:8080/youtubeCrawling.jsp',
                type : 'POST',
                data : {id : httpYoutubeLinkCreate},
                dataType : 'json',
                timeout : 1000,  // 단위 ms
                async : false,
                success : (data) => {
                	console.log(data);
                    kakao_title = data.kakao_title;
                    kakao_img = data.kakao_img;
                    kakao_description = data.kakao_description;
                }
        })
}


Kakao.init('d02fd05af201f3eee3c8edac91f3cc16');
let kakao_img = 'http://youtube2.kr/y/logo2.jpg';
let kakao_title = '라이브 예배실황';
let kakao_description = '온라인 예배';

KakaoToalk_Share = () => {
    let youtubeLink = document.getElementById('link').textContent;
    Kakao.Link.sendDefault({
        objectType : 'feed',
        content : {
                // 제목
                title : kakao_title,
                // 설명
                description : kakao_description,
                // 이미지 URL
                imageUrl : kakao_img,
                // 이미지 size
                imageWidth : 300,
                imageHeight : 169,
                // Youtube Link
                link: {
                    mobileWebUrl: youtubeLink,
                    webUrl: youtubeLink
                }
        },
        // 버튼 주석처리 가능 (사용하지 않을 경우)
        buttons : [
            {
                    title : '유튜브 앱으로 시청',
                    link: {
                            mobileWebUrl: youtubeLink,
                    webUrl: youtubeLink
                }
            }
        ]
	});
}


copyUrl = () => {
	let link = document.createRange();
	link.selectNode(document.getElementById('link'));
	window.getSelection().removeAllRanges();
	window.getSelection().addRange(link);
	document.execCommand('copy');
	window.getSelection().removeAllRanges();
}

//파라미터 추출
function getParameter(param) {
	let requestParam = "";
	
	//현재 주소를 decoding
	const url = document.getElementById('youtubeUrl').value.replaceAll(' ','');
	
	if (url.split('/')[2] == 'youtu.be') {
		// 공유 링크로 들어오는 경우
		requestParam = url.split('/')[3];
	} else {
		// 웹 URL로 들어오는 경우
		const paramArr = (url.substring(url.indexOf("?") + 1, url.length)).split("&");
		for (let i = 0; i < paramArr.length; i++) {
			let temp = paramArr[i].split("="); //파라미터 변수명을 담음
			
			if (temp[0].toUpperCase() == param.toUpperCase()) {
				// 변수명과 일치할 경우 데이터 삽입
				requestParam = paramArr[i].split("=")[1];
				break;
			}
		}
	}

    return requestParam;
}

playListAjax = async (channelId) => {
    const result = await $.ajax({
        // url은 임시로 사용
        url : 'http://localhost:8080/youtube2/api/playlist/'+channelId,
        type : 'GET',
        dataType : 'json',
        timeout : 2000,  // 단위 ms
	})
	return result.videoinfo;
}

selOptionButton = () => {
	$('#youtubelist').empty();
	const channelId = $('select[name=youtubeChannel]').val();
	playListAjax(channelId).then((result) => {
		let youtubelistHtml = '';
		if (result.length > 0) {
			youtubelistHtml += '<ul id="youtubeListUl">';
			
			for (let idx=0; idx < result.length; idx++) {
				youtubelistHtml += '<li>';
				youtubelistHtml += '<div id="thumbnail"><img src="'+result[idx].Thumbnail+'" height="100px"></div>';
				youtubelistHtml += '<div id="description"><strong>'+result[idx].Title+'</strong>'+(result[idx].Description !=null ? '<br />'+result[idx].Description : '')+'</div>';
				youtubelistHtml += '</li>';
			}
			youtubelistHtml += '</ul>';
		}
		
		$('#youtubelist').append(youtubelistHtml);
	});
}
</script>
<body class="text-center">

	<main class="form-signin">
		<form>
			<img class="mb-4" src="http://youtube2.kr/hz.svg" alt="" width="128" height="65">
			<h1 class="h3 mb-3 fw-normal">The culture of Christ.</h1>

			<div class="form-floating">
				<input class="form-control" id="youtubeUrl" autocomplete="off" type="text" placeholder="YouTube URL 입력하세요"> 
				<label for="floatingInput">YouTube URL 입력하세요</label>
			</div>
				<div class="checkbox mb-3"></div>
				<button type="button" class="w-100 btn btn-primary" data-bs-toggle="dropdown" aria-expanded="false" onclick="createUrl()">원클릭 유튜브 주소 만들기</button>
			</div>
			<div class="mt-5 mb-3 text-muted" id="createUrlPrint"></div>
		</form>
	</main>
	
	<select id="youtubeChannel" name="youtubeChannel">
		<option value="UC9Rzd-bAdxTcDNOmgeOBCTg">수원삼일교회</option>
		<option value="UC1v6BgyI1_n8_oNVR5dKriw">수원삼일교회TV</option>
	</select>
	<div id="selOptionButton" onclick="selOptionButton()">조회</div>
	<div id="youtubelist"></div>
</body>
</html>
