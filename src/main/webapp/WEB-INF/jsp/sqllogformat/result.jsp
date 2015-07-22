<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<script type="text/javascript" src="/js/jquery.zclip.min.js"></script>
<script>
	$(document).ready(function() {
		// 특수 공백문자 일반 공백문자로 치환 
		$('a#copy-description').zclip({ path : '/js/ZeroClipboard.swf',
		copy : ($('p#description').text()).replace(/ /gi, " "),
		afterCopy : function() {
			alert("복사 완료");
		}
		});
		// The link with ID "copy-description" will copy
		// the text of the paragraph with ID "description"
	});
</script>
</head>
<body>
	<table border="1">
		<tr>
			<td>parameter</td>
			<td>${formattedSql.parameters}</td>
		</tr>
		<tr>
			<td>sql</td>
			<td><p id="description">${formattedSql.sql}</p></td>
		</tr>
	</table>

	<a href="#" id="copy-description" class="">복사하기</a>

</body>
</html>