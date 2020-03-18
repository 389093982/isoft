/*
直接更新state的多个方法的对象
 */
export default {
  ShowCheckHasLoginConfirmDialog: function (state, {callback}) {
    state.CheckHasLoginConfirmDialogCallBack = callback;
  }
}
