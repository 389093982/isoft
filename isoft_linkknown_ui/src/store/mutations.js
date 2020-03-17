/*
直接更新state的多个方法的对象
 */
export default {
  CheckHasLoginConfirmDialog3: function (state, {callback}) {
    state.CheckHasLoginConfirmDialogCallBack = callback;
  }
}
