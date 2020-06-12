import Vue from 'vue'
import Router from 'vue-router'
import LuckyWheel from '@/components/Huodong/LuckyWheel'
import {joinArray} from "../../src/tools";

Vue.use(Router)

function getRootRouters() {
  return [
    {path: '/', redirect: '/huodong/luckywheel'},
    {path: '*',redirect: '/404'}
  ]
}

const HuodongRouter = [{
  path: '/huodong/luckywheel', component: LuckyWheel,
}];

function getAllRouters() {
  let allRouters = [];
  allRouters = joinArray(allRouters, HuodongRouter);
  allRouters = joinArray(allRouters, getRootRouters());
  return allRouters;
}

export default new Router({
  mode: 'history',
  base:'/isoft_webapp_ui/',
  routes: getAllRouters(),
})
