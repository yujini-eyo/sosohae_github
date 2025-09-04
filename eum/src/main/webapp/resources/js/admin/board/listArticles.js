// 관리자 게시글 목록 JS
(function () {
  function bindDeleteConfirm() {
    var forms = document.querySelectorAll('.js-delete-form');
    for (var i = 0; i < forms.length; i++) {
      forms[i].addEventListener('submit', function (e) {
        var ok = window.confirm('정말 삭제할까요?');
        if (!ok) e.preventDefault();
      });
    }
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', bindDeleteConfirm);
  } else {
    bindDeleteConfirm();
  }
})();
