//==============================================开始引入组件============================================================
import Vue from 'vue'
import Router from 'vue-router'
const baseLayout = () => import('../components/base/baseLayout');
const index = () => import('../components/showPage/index');
const myself = () => import('../components/myself/myself/myself');
const lover = () => import('../components/lover/lover');
const usercentral = () => import('../components/showPage/usercentral');

Vue.use(Router);

//==============================================引入二级模块============================================================
const linkloveRouters = [
  {path:'index',component:index},
  {path:'myself',component:myself},
  {path:'lover',component:lover},
  {path:'usercentral',component:usercentral},
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
