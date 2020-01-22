import Vue from 'vue'
import Router from 'vue-router'

const RequireList = () => import("@/components/Require/RequireList");
const RequireEdit = () => import("@/components/Require/RequireEdit");
const OAItem = () => import("@/components/OAItem");
const ILayout = () => import("@/components/ILayout/ILayout");

Vue.use(Router)

function joinArray(arr1, arr2) {
  [].push.apply(arr1, arr2);
  return arr1;
}

function getRootRouters() {
  return [{
    path: '/',
    redirect: '/oa/index'
  }]
};

const OARouter = [{
  path: '/oa', component: ILayout,
  children: [
    {path: 'index', component: OAItem},
    {path: 'requireList', component: RequireList},
    {path: 'requireEdit', component: RequireEdit},
  ]
}];

function getAllRouters() {
  let allRouters = [];
  allRouters = joinArray(allRouters, OARouter);
  allRouters = joinArray(allRouters, getRootRouters());
  return allRouters;
}

export default new Router({
  // # 主要用来区分前后台应用, history 模式需要使用 nginx 代理
  // History 模式,去除vue项目中的 #
  // mode: 'history',
  routes: getAllRouters(),
  // 页面跳转时,让页面滚动在顶部
  scrollBehavior(to, from, savedPosition) {
    // from 和 to 相同路由页面不滚动到顶部
    if (from.path === to.path) {
      return;
    }
    if (savedPosition) {
      return savedPosition;
    } else {
      return {x: 0, y: 0}
    }
  },
})
