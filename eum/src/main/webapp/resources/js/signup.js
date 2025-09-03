(() => {
  'use strict';

  const ready = (fn) => {
    if (document.readyState !== 'loading') fn();
    else document.addEventListener('DOMContentLoaded', fn);
  };

  ready(() => {
    const view = document.getElementById('signupView') || document.querySelector('.login-view');
    if (!view) return;

    // 버튼/폼 핸들링
    const form = view.querySelector('form.login-box');
    const btnSubmit = view.querySelector('#doSignup');
    const btnSendCode = view.querySelector('#btnSendCode');
    const cbNoNotes = view.querySelector('#noNotesCheck');
    const inputNotes = view.querySelector('#sgNotes');

    // 인증버튼 눌렀을 때
    if (btnSendCode) {
      btnSendCode.addEventListener('click', () => {
        try { alert('인증번호가 전송되었습니다.'); } catch (e) {}
        const code = view.querySelector('#sgCode');
        if (code) code.focus();
      });
    }

    // 특이사항 없어요 체크 시 토글
    if (cbNoNotes && inputNotes) {
      const applyNotes = () => {
        if (cbNoNotes.checked) {
          inputNotes.value = '없음';
          inputNotes.disabled = true;
        } else {
          if (inputNotes.value === '없음') inputNotes.value = '';
          inputNotes.disabled = false;
        }
      };
      cbNoNotes.addEventListener('change', applyNotes);
      applyNotes();
    }

    // 더블 서브밋 방지 + 간단 검증
    if (form && btnSubmit) {
      let locked = false;
      form.addEventListener('submit', (e) => {
        // 간단 유효성 (프론트): 인증번호 자리수 체크
        const code = view.querySelector('#sgCode');
        if (code && code.value.trim().length < 4) {
          e.preventDefault();
          alert('인증번호를 확인해주세요.');
          code.focus();
          return;
        }

        if (locked) {
          e.preventDefault();
          return false;
        }
        locked = true;
        btnSubmit.disabled = true;
        btnSubmit.setAttribute('aria-busy', 'true');
      });
    }
  });
})();