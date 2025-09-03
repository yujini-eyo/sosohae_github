(() => {
  'use strict';

  const ready = (fn) => {
    if (document.readyState !== 'loading') fn();
    else document.addEventListener('DOMContentLoaded', fn);
  };

  ready(() => {
    const view = document.getElementById('loginView') || document.querySelector('.login-view');
    if (!view) return;

    // 로그인 실패 처리 (조각의 data-login-failed가 "true"/"false"로 들어옴)
    const failed = String(view.dataset.loginFailed || 'false') === 'true';
    if (failed) {
      try { alert('아이디나 비밀번호가 틀립니다. 다시 로그인 하세요!'); } catch (e) {}
      const id = document.getElementById('loginId');
      if (id) id.focus();
    }

    // 더블 클릭/엔터로 인한 중복 제출 방지
    const form = view.querySelector('form.login-box');
    const btn  = view.querySelector('#doLogin');
    if (form && btn) {
      let locked = false;
      form.addEventListener('submit', () => {
        if (locked) return false;
        locked = true;
        btn.disabled = true;
        btn.setAttribute('aria-busy', 'true');
      });
    }
  });
})();
