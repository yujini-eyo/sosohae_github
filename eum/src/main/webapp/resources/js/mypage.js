(function () {
  'use strict';

  function ready(fn) {
    if (document.readyState !== 'loading') fn();
    else document.addEventListener('DOMContentLoaded', fn);
  }

  function strength(pw) {
    var score = 0;
    if (!pw) return 0;
    if (pw.length >= 8) score++;
    if (/[a-z]/.test(pw) && /[A-Z]/.test(pw)) score++;
    if (/\d/.test(pw)) score++;
    if (/[^A-Za-z0-9]/.test(pw)) score++;
    if (pw.length >= 12) score++;
    return Math.min(score, 5);
  }

  ready(function () {
    var view = document.getElementById('mypageView');
    if (!view) return;

    // ===== 플래시/알림 =====
    var ds = view.dataset || {};
    var updateOK = String(ds.updateOk || 'false') === 'true';
    var pwOK     = String(ds.pwOk || 'false') === 'true';
    var pwFail   = String(ds.pwFail || 'false') === 'true';
    try {
      if (updateOK) alert('내 정보가 저장되었습니다.');
      if (pwOK)     alert('비밀번호가 변경되었습니다.');
      if (pwFail)   alert('비밀번호가 일치하지 않습니다.');
    } catch (e) {}

    // ===== 개인정보 편집 토글 =====
    var editBtn   = document.getElementById('editBtn');
    var cancelBtn = document.getElementById('cancelBtn');
    var saveBtn   = document.getElementById('saveBtn');
    var readBlock = document.getElementById('readBlock');
    // ✅ HTML에 맞춰서: form id 는 editBlock
    var form      = document.getElementById('profileForm');

    var initial = {};
    if (form) {
      var ids = ['name','email','phone','birth','address','notes'];
      for (var i = 0; i < ids.length; i++) {
        var el = form.querySelector('#' + ids[i]);
        if (el) initial[ids[i]] = el.value;
      }
    }

    function enterEdit() {
      if (!form) return;
      if (readBlock) readBlock.style.display = 'none';
      form.style.display = '';
      if (editBtn)   editBtn.style.display = 'none';
      if (cancelBtn) cancelBtn.style.display = '';
      if (saveBtn)   saveBtn.style.display = '';
      var nameEl = form.querySelector('#name');
      if (nameEl) nameEl.focus();
    }

    function exitEdit() {
      if (!form) return;
      if (readBlock) readBlock.style.display = '';
      form.style.display = 'none';
      if (editBtn)   editBtn.style.display = '';
      if (cancelBtn) cancelBtn.style.display = 'none';
      if (saveBtn)   saveBtn.style.display = 'none';
    }

    if (editBtn) editBtn.addEventListener('click', enterEdit);
    if (cancelBtn) cancelBtn.addEventListener('click', function () {
      for (var k in initial) {
        if (Object.prototype.hasOwnProperty.call(initial, k)) {
          var el = form.querySelector('#' + k);
          if (el) el.value = initial[k];
        }
      }
      exitEdit();
    });
    if (saveBtn) saveBtn.addEventListener('click', function () {
      var email = form ? form.querySelector('#email') : null;
      if (email && !email.checkValidity()) {
        alert('이메일 형식을 확인해 주세요.');
        email.focus();
        return;
      }
      if (form) form.submit();
    });

    // ===== 아바타 업로드 =====
    var avatarBtn   = document.getElementById('avatarBtn');
    var avatarInput = document.getElementById('avatarInput');
    // ✅ HTML엔 id가 없어서 .avatar를 찾도록 변경
    var avatarPrev  = document.querySelector('avatarPreview');

    if (avatarBtn) avatarBtn.addEventListener('click', function () {
      if (readBlock && readBlock.style.display !== 'none') enterEdit();
      if (avatarInput) avatarInput.click();
    });

    if (avatarInput) avatarInput.addEventListener('change', function () {
      var file = avatarInput.files && avatarInput.files[0];
      if (!file) return;
      if (!/image\/(png|jpeg)/.test(file.type)) {
        alert('JPG 또는 PNG 이미지를 선택해 주세요.');
        avatarInput.value = '';
        return;
      }
      if (file.size > 2 * 1024 * 1024) {
        alert('이미지 크기는 최대 2MB까지 가능합니다.');
        avatarInput.value = '';
        return;
      }
      var reader = new FileReader();
      reader.onload = function (e) {
        if (avatarPrev) {
          avatarPrev.style.backgroundImage = "url('" + e.target.result + "')";
          avatarPrev.textContent = '';
        }
      };
      reader.readAsDataURL(file);
    });

    // ===== 비밀번호 변경 =====
    var pwForm  = document.getElementById('pwForm');
    var curPw   = document.getElementById('curPw');
    var newPw   = document.getElementById('newPw');
    var newPw2  = document.getElementById('newPw2');
    var pwErr   = document.getElementById('pwErr');
    var pwBar   = document.getElementById('pwBar');
    var pwLevel = document.getElementById('pwLevel');
    var reqs    = {
      r1: document.getElementById('r1'),
      r2: document.getElementById('r2'),
      r3: document.getElementById('r3'),
      r4: document.getElementById('r4')
    };

    // show/hide password
    var toggles = document.querySelectorAll('.toggle-eye');
    Array.prototype.forEach.call(toggles, function (btn) {
      btn.addEventListener('click', function () {
        var targetId = btn.getAttribute('data-target');
        var input = document.getElementById(targetId);
        if (!input) return;
        input.type = (input.type === 'password') ? 'text' : 'password';
      });
    });

    function updateStrength() {
      var val = (newPw && newPw.value) ? newPw.value : '';
      var sc = strength(val);
      var pctArr = [0, 25, 50, 75, 100];
      var pct = pctArr[sc] || 0;
      if (pwBar) {
        pwBar.style.width = pct + '%';
        pwBar.style.background = sc >= 4 ? '#16a34a' : (sc >= 2 ? '#f59e0b' : '#ef4444');
      }
      if (pwLevel) pwLevel.textContent = '강도: ' + (sc >= 4 ? '강함' : (sc >= 2 ? '보통' : '약함'));

      if (reqs.r1) (val.length >= 8 ? reqs.r1.classList.add('ok') : reqs.r1.classList.remove('ok'));
      if (reqs.r2) ((/[a-z]/.test(val) && /[A-Z]/.test(val)) ? reqs.r2.classList.add('ok') : reqs.r2.classList.remove('ok'));
      if (reqs.r3) (/\d/.test(val) ? reqs.r3.classList.add('ok') : reqs.r3.classList.remove('ok'));
      if (reqs.r4) (/[^A-Za-z0-9]/.test(val) ? reqs.r4.classList.add('ok') : reqs.r4.classList.remove('ok'));
    }

    if (newPw) newPw.addEventListener('input', function () {
      updateStrength();
      if (pwErr && newPw2) pwErr.style.display = (newPw.value === newPw2.value) ? 'none' : '';
    });
    if (newPw2) newPw2.addEventListener('input', function () {
      updateStrength();
      if (pwErr && newPw) pwErr.style.display = (newPw.value === newPw2.value) ? 'none' : '';
    });

    if (pwForm) pwForm.addEventListener('submit', function (e) {
      if (!newPw || !newPw2 || newPw.value !== newPw2.value) {
        if (e && e.preventDefault) e.preventDefault();
        if (pwErr) pwErr.style.display = '';
        if (newPw2) newPw2.focus();
        return false;
      }
    });

    // ===== 2단계 인증 UI =====
    var mfaToggle = document.getElementById('mfaToggle');
    var mfaHint   = document.getElementById('mfaHint');
    var qrBox     = document.getElementById('qrBox');

    if (mfaToggle) mfaToggle.addEventListener('change', function () {
      if (mfaToggle.checked) {
        if (mfaHint) mfaHint.style.display = '';
        if (qrBox) { qrBox.style.display = 'grid'; qrBox.textContent = 'QR 코드를 불러오는 중...'; }
      } else {
        if (mfaHint) mfaHint.style.display = 'none';
        if (qrBox)  qrBox.style.display = 'none';
      }
    });

    // ===== 포인트 탭 =====
    var tabs = document.querySelectorAll('.tab-btn');
    var panels = {
      overview: document.getElementById('panel-overview'),
      history:  document.getElementById('panel-history'),
      rules:    document.getElementById('panel-rules')
    };

    Array.prototype.forEach.call(tabs, function (btn) {
      btn.addEventListener('click', function () {
        Array.prototype.forEach.call(tabs, function (b) { b.setAttribute('aria-selected', 'false'); });
        btn.setAttribute('aria-selected', 'true');
        for (var key in panels) {
          if (panels[key]) panels[key].classList.remove('active');
        }
        var key2 = btn.getAttribute('data-tab');
        if (panels[key2]) panels[key2].classList.add('active');
      });
    });

    // 내역 필터
    var typeSelect = document.getElementById('typeSelect');
    var rows = document.querySelectorAll('#pt_rows tr');
    if (typeSelect) typeSelect.addEventListener('change', function () {
      var val = typeSelect.value;
      Array.prototype.forEach.call(rows, function (tr) {
        tr.style.display = (val === 'all' || tr.getAttribute('data-type') === val) ? '' : 'none';
      });
    });

    // 등급/목표 진행도 (간단 계산)
    var goalBar = document.getElementById('pt_goal_bar');
    try {
      var goalNowEl   = document.getElementById('goalNow');
      var goalTotalEl = document.getElementById('goalTarget');
      var goalNow   = parseFloat(goalNowEl ? goalNowEl.textContent : '0');
      var goalTotal = parseFloat(goalTotalEl ? goalTotalEl.textContent : '1');
      var goalPct   = Math.max(0, Math.min(100, Math.round((goalNow / Math.max(goalTotal,1)) * 100)));
      if (goalBar) goalBar.style.width = goalPct + '%';
    } catch (e) {}

    var progressEl = document.getElementById('pt_progress');
    var nextTextEl = document.getElementById('pt_next_text');
    var nextText   = nextTextEl ? nextTextEl.textContent : '';
    // ✅ replaceAll 대신 정규식
    var m = nextText.replace(/,/g, '').match(/(\d+)/);
    var approxPct = m ? Math.max(0, Math.min(100, 100 - Math.min(100, Math.round((parseInt(m[1],10) / 10000) * 100)))) : 60;
    setTimeout(function () { if (progressEl) progressEl.style.width = approxPct + '%'; }, 100);
  });
})();