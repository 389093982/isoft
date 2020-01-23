//==============================================开始引入组件============================================================
import Vue from 'vue'
import Router from 'vue-router'
const baseLayout = () => import('../components/base/baseLayout');
const index = () => import('../components/showPage/index');

Vue.use(Router);

//==============================================引入二级模块============================================================
//首页
const printloveRouters = [
  {path:'index',component:index},
];


//==============================================引入一级模块============================================================
export default new Router({
  //去掉#号
  mode:'history',
  routes: [
    {path:'/printlove',component:baseLayout,children:printloveRouters},//首页
    { path: '*', redirect: '/printlove/index' },//首页
  ]
})
