<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<style>
.cls1 {
	text-decoration: none;
}

.cls2 {
	text-align: center;
	font-size: 30px;
}
</style>
<meta charset="UTF-8">
<title>글목록창</title>
<!-- jQuery CDN -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

</head>
<body>
	<div id="status" style="text-align: center; margin: 8px 0;">목록을
		불러오는 중…</div>
	<table align="center" border="1" width="80%" id="board-table">
		<thead>
			<tr align="center" bgcolor="lightgreen">
				<td>글번호</td>
				<td>작성자</td>
				<td>제목</td>
				<td>작성일</td>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	<script>
$(document).ready(function () {
  $.ajax({
    url: '<c:url value="/board/listArticlesJson.do"/>',
    method: 'GET',
    dataType: 'json',
    success: function (data) {
      $('#status').text('');
      var $tbody = $('#board-table tbody');

      if ($.isArray(data) && data.length > 0) {
        data.forEach(function (row) {
          var viewUrl = '<c:url value="/board/viewArticle.do"/>' + '?articleNO=' + encodeURIComponent(row.articleNO || '');
          var $tr = $('<tr/>');
          $tr.append('<td>' + (row.articleNO ?? '') + '</td>');
          $tr.append('<td>' + (row.id ?? '') + '</td>');
          $tr.append('<td><a href="' + viewUrl + '">' + (row.title ?? '') + '</a></td>');
          $tr.append('<td>' + (row.writeDate ?? '') + '</td>');
          $tbody.append($tr);
        });
      } else {
        $('#status').text('등록된 글이 없습니다.');
      }
    },
    error: function (xhr, status, err) {
      $('#status').text('에러 발생: ' + (err || status));
    }
  });
});
</script>
</body>
</html>