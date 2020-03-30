// 通过 mutation 间接更新 state 的多个方法的对象
export default {
  ShowCheckHasLoginConfirmDialog: function ({commit}, {callback}) {
    commit('ShowCheckHasLoginConfirmDialog', {callback});
  },
  autoLogin: function ({commit}, {autoLogin}) {
    commit('autoLogin', {autoLogin});
  }
}
