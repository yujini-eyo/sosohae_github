    // 비밀번호 변경시 로그인 비밀번호에 반영
    function getUsers(){ try { return JSON.parse(localStorage.getItem('users') || '{}'); } catch { return {}; } }
    function setUsers(u){ localStorage.setItem('users', JSON.stringify(u)); }

    // 현재 로그인한 사용자 (없으면 로그인 화면으로)
    const uid = localStorage.getItem('loggedInUser');
    if (!uid) {
    const back = encodeURIComponent('mypage.html');
    location.replace(`login.html?next=${back}`);
    }
    
    // ========= 개인정보 편집 토글 =========
    const editBtn = document.getElementById('editBtn');
    const cancelBtn = document.getElementById('cancelBtn');
    const saveBtn = document.getElementById('saveBtn');
    const readBlock = document.getElementById('readBlock');
    const editBlock = document.getElementById('editBlock');

    const v = id => document.getElementById('v_' + id);
    const i = id => document.getElementById(id);

    function toEdit() {
      readBlock.style.display = 'none';
      editBlock.style.display = 'grid';
      saveBtn.style.display = cancelBtn.style.display = 'inline-flex';
      editBtn.style.display = 'none';
    }
    function toRead() {
      readBlock.style.display = 'grid';
      editBlock.style.display = 'none';
      saveBtn.style.display = cancelBtn.style.display = 'none';
      editBtn.style.display = 'inline-flex';
    }
    editBtn.addEventListener('click', toEdit);
    cancelBtn.addEventListener('click', toRead);

    saveBtn.addEventListener('click', () => {
      if (!i('name').value.trim() || !i('email').value.trim()) {
        alert('이름/이메일을 확인해주세요.');
        return;
      }
      v('name').textContent = i('name').value;
      v('email').textContent = i('email').value;
      v('phone').textContent = i('phone').value;
      v('birth').textContent = i('birth').value;
      v('addr').textContent = i('addr').value;
      v('note').textContent = i('note').value;
      toRead();
      alert('개인정보를 저장했습니다.');
    });

    // 프로필 이미지 버튼
    const avatarBtn = document.getElementById('avatarBtn');
    const avatarInput = document.getElementById('avatarInput');
    avatarBtn.addEventListener('click', () => avatarInput.click());

    // ========= 보안 설정: 비밀번호 강도/일치/보기 토글 =========
    const newPw = document.getElementById('newPw');
    const newPw2 = document.getElementById('newPw2');
    const pwErr = document.getElementById('pwErr');
    const pwBar = document.getElementById('pwBar');
    const pwLevel = document.getElementById('pwLevel');
    const reqs = {
      r1: document.getElementById('r1'),
      r2: document.getElementById('r2'),
      r3: document.getElementById('r3'),
      r4: document.getElementById('r4'),
    };

    function setReq(li, ok) {
      li.classList.toggle('ok', ok);
      li.innerHTML = (ok ? '✔ ' : '• ') + li.textContent.replace(/^✔\s|•\s/, '');
    }

    function strength(pw) {
      const hasLen = pw.length >= 8;
      const hasUpper = /[A-Z]/.test(pw);
      const hasLower = /[a-z]/.test(pw);
      const hasNum = /\d/.test(pw);
      const hasSpec = /[^A-Za-z0-9]/.test(pw);
      setReq(reqs.r1, hasLen);
      setReq(reqs.r2, hasUpper && hasLower);
      setReq(reqs.r3, hasNum);
      setReq(reqs.r4, hasSpec);

      let score = 0;
      if (hasLen) score++;
      if (hasUpper && hasLower) score++;
      if (hasNum) score++;
      if (hasSpec) score++;

      const pct = [0, 25, 60, 80, 100][score];
      pwBar.style.width = pct + '%';
      pwBar.style.background = score <= 1 ? '#FCA5A5' : (score === 2 ? '#FDE68A' : '#86EFAC');
      pwLevel.textContent = '강도: ' + (score <= 1 ? '약함' : (score === 2 ? '보통' : '강함'));
    }

    newPw.addEventListener('input', () => strength(newPw.value));
    newPw2.addEventListener('input', () => {
      pwErr.style.display = (newPw.value && newPw2.value && newPw.value !== newPw2.value) ? 'block' : 'none';
    });

    // 보기/숨김 토글
    document.querySelectorAll('.toggle-eye').forEach(btn => {
      btn.addEventListener('click', () => {
        const target = document.getElementById(btn.dataset.target);
        target.type = target.type === 'password' ? 'text' : 'password';
        btn.querySelector('i').classList.toggle('fa-eye');
        btn.querySelector('i').classList.toggle('fa-eye-slash');
      });
    });

  // 비밀번호 저장 핸들러 (교체)
document.getElementById('pwSave').addEventListener('click', () => {
  const curEl = document.getElementById('curPw');
  const a = newPw.value || '';
  const b = newPw2.value || '';

  // 1) 일치 검사
  if (a !== b) { pwErr.style.display = 'block'; return; }
  // 2) 규칙: 8자 이상만 필수
  if (a.length < 8) { alert('비밀번호는 8자 이상이어야 합니다.'); return; }

  // 3) 현재 비번 확인(있을 때만)
  const users = getUsers();
  users[uid] = users[uid] || {};
  if (curEl && users[uid].password && users[uid].password !== curEl.value) {
    alert('현재 비밀번호가 일치하지 않습니다.');
    return;
  }

  // 4) 새 비밀번호 저장
  users[uid].password = a;
  setUsers(users);

  // 5) 세션 무효화 + 로그인 페이지로 이동 (다음 로그인부터 새 비번 사용)
  alert('비밀번호가 변경되었습니다. 다시 로그인해주세요.');
  localStorage.removeItem('loggedInUser'); // 로컬 로그인 세션 종료
  localStorage.removeItem('ss_auth');      // 토큰을 썼다면(없어도 안전)
  location.replace(`login.html?next=${encodeURIComponent('mypage.html')}`);
});

    // MFA
    const mfaToggle = document.getElementById('mfaToggle');
    const mfaHint = document.getElementById('mfaHint');
    const qrBox = document.getElementById('qrBox');
    mfaToggle.addEventListener('change', () => {
      const on = mfaToggle.checked;
      mfaHint.style.display = on ? 'block' : 'none';
      qrBox.style.display = on ? 'grid' : 'none';
    });

    // ========= 봉사포인트 상태값(예시) =========
    const PT = {
      current: 12500,
      month_gain: 3200,
      month_spend: 1000,
      total_gain: 24700,
      total_spend: 12200,
      total_hours: 18.5,
      goal_done: 2, goal_target: 5,
      tiers: [
        { name: '씨앗', min: 0 },
        { name: '새싹', min: 1000 },
        { name: '나무', min: 5000 },
        { name: '숲', min: 15000 }
      ]
    };

    function computeTier(cur) {
      const ts = PT.tiers;
      let idx = 0;
      for (let i = 0; i < ts.length; i++) {
        if (cur >= ts[i].min) idx = i;
      }
      const tier = ts[idx];
      const next = ts[idx + 1] || null;
      let pct = 100;
      let nextText = '최고 등급 달성!';
      if (next) {
        const span = next.min - tier.min;
        pct = Math.max(0, Math.min(100, ((cur - tier.min) / span) * 100));
        nextText = `다음 등급(${next.name}) 까지 ${Math.max(0, next.min - cur).toLocaleString()} P`;
      }
      return { tier: tier.name, pct, nextText };
    }

    (function initPointsUI() {
      const $ = (id) => document.getElementById(id);
      $('pt_current').textContent = `${PT.current.toLocaleString()} P`;
      $('pt_month_gain').textContent = `+${PT.month_gain.toLocaleString()}`;
      $('pt_month_spend').textContent = `-${PT.month_spend.toLocaleString()}`;
      $('pt_total_gain').textContent = `${PT.total_gain.toLocaleString()} P`;
      $('pt_total_spend').textContent = `${PT.total_spend.toLocaleString()} P`;
      $('pt_total_hours').textContent = `${PT.total_hours} h`;

      const { tier, pct, nextText } = computeTier(PT.current);
      $('pt_tier').textContent = tier;
      $('pt_next_text').textContent = nextText;
      $('pt_progress').style.width = `${pct}%`;

      const goalPct = Math.min(100, (PT.goal_done / PT.goal_target) * 100);
      $('pt_goal_bar').style.width = `${goalPct}%`;
    })();

    // 탭 전환
    document.querySelectorAll('.tab-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        document.querySelectorAll('.tab-btn').forEach(b => b.setAttribute('aria-selected', 'false'));
        btn.setAttribute('aria-selected', 'true');
        const tab = btn.dataset.tab;
        document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
        document.getElementById('panel-' + tab).classList.add('active');
      });
    });

    // 유형 필터
    const typeSelect = document.getElementById('typeSelect');
    if (typeSelect) {
      typeSelect.addEventListener('change', () => {
        const v = typeSelect.value;
        document.querySelectorAll('#pt_rows tr').forEach(tr => {
          const t = tr.getAttribute('data-type');
          tr.style.display = (v === 'all' || v === t) ? '' : 'none';
        });
      });
    }

    // 액션 버튼 (데모)
    const clickAlert = (msg) => () => alert(msg);
    document.getElementById('sendPoint').addEventListener('click', clickAlert('포인트 보내기: 수신자와 전송 포인트를 입력하는 모달을 띄우세요.'));
    document.getElementById('redeemPoint').addEventListener('click', clickAlert('보상 교환: 제휴 보상(이동지원, 식사쿠폰 등) 카탈로그 모달을 띄우세요.'));
    document.getElementById('logHelp').addEventListener('click', clickAlert('봉사 등록: 봉사 유형/시간/메모를 기록하는 폼을 띄우세요.'));
    document.getElementById('donatePool').addEventListener('click', clickAlert('나눔 기부: 공용 나눔풀로 기부 포인트를 입력하는 모달을 띄우세요.'));
    document.getElementById('viewBadges').addEventListener('click', clickAlert('뱃지: 획득/예정 뱃지 리스트를 보여주세요.'));
    document.getElementById('policyBtn').addEventListener('click', () => {
      document.querySelector('.tab-btn[data-tab="rules"]').click();
    });

/* ========== mypage.html 전용 로그인 연동( login.html 그대로, mypage만 수정 ) ========== */
(function () {
  // --- util ---
  const $ = (id) => document.getElementById(id);
  function getUsers(){ try { return JSON.parse(localStorage.getItem('users') || '{}'); } catch { return {}; } }
  function setUsers(u){ localStorage.setItem('users', JSON.stringify(u)); }

  // 로그인 가드: 없으면 로그인 페이지로
  const uid = localStorage.getItem('loggedInUser');
  if (!uid) {
    const back = encodeURIComponent('mypage.html');
    location.replace(`login.html?next=${back}`);
    return;
  }

  // 헤더 표시명 교체(있으면)
  const nameSpan = document.querySelector('.user-name');
  if (nameSpan) {
    const u = getUsers()[uid];
    nameSpan.textContent = ((u && (u.name || u.nickname)) || uid) + ' 님';
  }

  // 폼/읽기 데이터 채우기
  const users = getUsers();
  const me = users[uid] || {};
  const map = {
    name:  me.name  ?? '',
    email: me.email ?? '',
    phone: me.phone ?? '',
    birth: me.birthday ?? me.birth ?? '',
    addr:  me.address ?? me.addr ?? '',
    note:  me.note ?? ''
  };
  Object.entries(map).forEach(([k,val])=>{
    const readEl = $('v_'+k), editEl = $(k);
    if (readEl) readEl.textContent = val;
    if (editEl) editEl.value = val;
  });

  // 저장 버튼: 기존 UI 업데이트 후, 로컬 스토리지에도 영속
  $('saveBtn')?.addEventListener('click', function () {
    const u = getUsers();
    u[uid] = u[uid] || {};
    u[uid].name    = $('name')?.value?.trim()  || '';
    u[uid].email   = $('email')?.value?.trim() || '';
    u[uid].phone   = $('phone')?.value?.trim() || '';
    u[uid].birth   = $('birth')?.value || '';
    u[uid].address = $('addr')?.value?.trim()  || '';
    u[uid].note    = $('note')?.value?.trim()  || '';
    setUsers(u);
  });

  // 비밀번호 변경: 현재 비번 일치 시 교체(로컬 기준)
 $('pwSave')?.addEventListener('click', function () {
  const cur = $('curPw')?.value || '';
  const a = $('newPw')?.value || '', b = $('newPw2')?.value || '';
  const u = getUsers(); u[uid] = u[uid] || {};
  if (u[uid].password && u[uid].password !== cur) return;
  // 규칙 완화: 8자 이상만 필수 (UI 핸들러에서 이미 경고했지만 가드 한 번 더)
  if (a.length < 8) return;
  if (a && a === b) { u[uid].password = a; setUsers(u); }
});

  // (선택) 로그아웃 버튼이 있다면 실제 로그아웃 동작 연결
  document.querySelectorAll('[onclick*="로그아웃"]').forEach(btn=>{
    btn.addEventListener('click', (e)=>{
      e.preventDefault?.();
      localStorage.removeItem('loggedInUser');
      location.replace('login.html');
    }, { once:true });
  });
})();
