<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />


<div class="admin-section">
<h1>관리자 메인</h1>
<div class="cards">
<a class="card" href="${ctx}/admin/member/list-lite.do">회원 관리(라이트)</a>
<a class="card" href="${ctx}/admin/inquiry/view-lite.do?id=1">1:1 문의(라이트) 샘플</a>
<a class="card" href="${ctx}/admin/board/adminList.do">게시판 관리(기존)</a>
<a class="card" href="${ctx}/admin/main/edit.do">메인 페이지 관리(에디터)</a>
</div>
</div>
<style>
.admin-section{background:#fff;padding:20px;border:1px solid #EADBC8;border-radius:16px}
.cards{display:grid;grid-template-columns:repeat(auto-fill,minmax(220px,1fr));gap:12px}
.card{display:block;padding:16px;border:1px solid #EADBC8;border-radius:14px;text-decoration:none;color:#1C1917;background:#FFF8F2}
.card:hover{background:#FFE8C2}
</style>