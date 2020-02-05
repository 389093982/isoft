//==============================================开始引入组件============================================================
import Vue from 'vue'
import Router from 'vue-router'
const indexLayout = () => import('../components/base/indexLayout');
const baseLayout = () => import('../components/base/baseLayout');
const index = () => import('../components/showPage/index');
const myselfIndex = () => import('../components/myself/myself/myselfIndex');
const loverIndex = () => import('../components/lover/loverIndex');
const usercentralIndex = () => import('../components/showPage/usercentralIndex');

Vue.use(Router);

//==============================================引入二级模块============================================================
const indexRouters = [
  {path:'index',component:index},
];
const myselfRouters = [
  {path:'index',component:myselfIndex},
];
const loverRouters = [
  {path:'index',component:loverIndex},
];
const usercentralRouters = [
  {path:'index',component:usercentralIndex},
];

//==============================================引入一级模块============================================================
export default new Router({
  //去掉#号
  mode:'history',
  routes: [
    {path:'/linklove',component:indexLayout,children:indexRouters},
    {path:'/myself',component:baseLayout,children:myselfRouters},
    {path:'/lover',component:baseLayout,children:loverRouters},
    {path:'/usercentral',component:baseLayout,children:usercentralRouters},
    { path: '*', redirect: '/linklove/index' },
  ]
})
