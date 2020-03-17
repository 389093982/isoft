// 通过 mutation 间接更新 state 的多个方法的对象
export default {
  CheckHasLoginConfirmDialog3: function ({commit}, {callback}) {
    commit('CheckHasLoginConfirmDialog3', {callback});
  }
}
