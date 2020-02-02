//==============================================开始引入组件============================================================
import Vue from 'vue'
import Router from 'vue-router'
const baseLayout = () => import('../components/base/baseLayout');
const index = () => import('../components/showPage/index');

Vue.use(Router);

//==============================================引入二级模块============================================================
//首页
const linkloveRouters = [
  {path:'index',component:index},
];


//==============================================引入一级模块============================================================
export default new Router({
  //去掉#号
  mode:'history',
  routes: [
    {path:'/linklove',component:baseLayout,children:linkloveRouters},//首页
    { path: '*', redirect: '/linklove/index' },//首页
  ]
})
