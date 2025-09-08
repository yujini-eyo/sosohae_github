<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>관리자 로그인</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <style>
    :root{
      --bg:#FFF8F2; --card:#ffffff; --line:#f0e6dc;
      --text:#4b3b33; --muted:#7a6b64;
      --primary:#f6a96d; --primary-600:#e98c45;
      --danger:#e25b5b;
      --radius:14px; --shadow:0 10px 30px rgba(0,0,0,.08);
    }

    /* ===== 상단 가림막 (기본) ===== */
    .page-top-occluder{
      position: fixed; left:0; top:0; width:100%; height:140px;
      background:
        radial-gradient(1200px 240px at 50% -60px, #FFE8C2 0%, rgba(255,232,194,.55) 35%, rgba(255,232,194,0) 60%),
        linear-gradient(180deg, var(--bg) 0%, rgba(255,248,242,0) 100%);
      z-index: 999; pointer-events: none;
    }
    /* 전역 헤더를 아예 숨기고 싶다면 아래 주석 해제
    .site-header, .header, #globalHeader { display:none !important; }
    */

    /* ===== 페이지 베이스 ===== */
    html,body{height:100%}
    body{
      font-family: system-ui, -apple-system, "Segoe UI", Roboto, sans-serif;
      background: var(--bg);
      color: var(--text);
      margin:0;
      display:grid; place-items:center;
      padding: 24px 16px;
    }

    /* 배경 장식 (라디얼) */
    body::before{
      content:""; position:fixed; inset:0;
      background:
        radial-gradient(600px 360px at 85% 10%, #FFDAB8 0%, rgba(255,218,184,0) 60%),
        radial-gradient(400px 280px at 10% 0%, #FADFCB 0%, rgba(250,223,203,0) 60%);
      opacity:.6; z-index:0; pointer-events:none;
    }

    .wrap{
      position: relative; z-index:1;
      width: min(420px, 94vw);
      margin: 40px auto 24px;
      padding: 28px 24px 22px;
      background: var(--card);
      border:1px solid var(--line);
      border-radius: var(--radius);
      box-shadow: var(--shadow);
      backdrop-filter: saturate(1.1);
    }
    .wrap header{
      display:flex; align-items:center; gap:10px; margin-bottom:14px;
    }
    .lock{
      width:36px; height:36px; border-radius:10px;
      display:grid; place-items:center; flex:0 0 36px;
      background:#FFE8C2; color:#744b1f; font-weight:900;
      box-shadow: inset 0 -1px 0 rgba(0,0,0,.05);
    }
    h2{margin:0; font-size:1.35rem; letter-spacing:.2px}

    label{display:block; margin:12px 0 6px; color: var(--muted); font-size:.95rem}
    .field{
      position:relative;
    }
    input[type=text], input[type=password]{
      width:100%; padding:12px 44px 12px 12px;
      border:1px solid #e9dfd7; border-radius:10px;
      background:#fff; color:var(--text);
      outline: none; transition: box-shadow .15s, border-color .15s;
      box-sizing:border-box;
    }
    input:focus{
      border-color: #ffd2ac;
      box-shadow: 0 0 0 4px rgba(246,169,109,.18);
    }

    .toggle-pass{
      position:absolute; right:8px; top:50%; transform: translateY(-50%);
      border:0; background:#fff; padding:6px 10px; border-radius:8px;
      font-size:.85rem; color:#795d4f; cursor:pointer;
      border:1px solid #eadfd7;
    }
    .toggle-pass:hover{background:#fff6ef}

    .btn{
      width:100%; padding:12px; border:0; border-radius:10px;
      background: var(--primary); color:#fff; font-weight:700; cursor:pointer;
      margin-top:14px; transition: transform .02s ease, background .15s;
    }
    .btn:hover{ background: var(--primary-600); }
    .btn:active{ transform: translateY(1px); }

    .muted{color:#8a7a73; font-size:12px; margin-top:10px; text-align:center}

    /* 에러 메시지 슬롯 */
    .alert{
      margin: 8px 0 4px; padding:10px 12px; border-radius:10px;
      background:#fff4f4; color:#8f2d2d; border:1px solid #ffd7d7;
      font-size:.92rem;
    }

    /* CSRF/폼 도움 텍스트 숨김용 유틸 */
    .sr-only{
      position:absolute !important; width:1px; height:1px; padding:0; margin:-1px; overflow:hidden; clip:rect(0,0,0,0); white-space:nowrap; border:0;
    }
  </style>
</head>
<body>
  <!-- 상단 글씨 가림막 -->
  <div class="page-top-occluder" aria-hidden="true"></div>

  <div class="wrap" role="region" aria-labelledby="loginTitle">
    <header>
      <div class="lock" aria-hidden="true">🔒</div>
      <h2 id="loginTitle">관리자 로그인</h2>
    </header>

    <!-- 에러 메시지 표시 (있을 경우) -->
    <c:if test="${not empty loginError}">
      <div class="alert" role="alert">
        <c:out value="${loginError}"/>
      </div>
    </c:if>

<form method="post" action="<c:url value='/admin/auth/login.do'/>" accept-charset="UTF-8" novalidate>
  <c:if test="${not empty _csrf}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  </c:if>

  <label for="id">아이디</label>
  <div class="field">
    <input type="text" id="id" name="id" required autocomplete="username" autofocus/>
  </div>

  <label for="password">비밀번호</label>
  <div class="field">
    <input type="password" id="password" name="password" required autocomplete="current-password"/>
    <button type="button" class="toggle-pass" aria-controls="password" aria-pressed="false" title="비밀번호 보기/숨기기">보기</button>
  </div>

  <button type="submit" class="btn">로그인</button>
  <p class="muted">관리자 전용 페이지입니다.</p>
</form>


  <script>
    // 비밀번호 보기/숨기기
    (function(){
      var btn = document.querySelector('.toggle-pass');
      var pw  = document.getElementById('password');
      if(!btn || !pw) return;
      btn.addEventListener('click', function(){
        var show = pw.type === 'password';
        pw.type = show ? 'text' : 'password';
        btn.setAttribute('aria-pressed', String(show));
        btn.textContent = show ? '숨기기' : '보기';
      });
    })();
  </script>
</body>
</html>
