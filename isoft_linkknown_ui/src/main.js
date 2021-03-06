// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'

import {CheckSSOLogin} from "./tools"
// 引用全局静态数据
import global_ from './components/GlobalData' //引用文件
// 使用 iview
import iView from 'iview'
import 'iview/dist/styles/iview.css'
// 使用 vue-markdown
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
// 使用全局的 animate.css
import animated from 'animate.css'
// 使用全局的 hover.css
import hovered from 'hover.css'
//使用vuescroll滚动条
import vuescroll from "vuescroll";
//引入vuescroll样式
import "vuescroll/dist/vuescroll.css";
// 注册自定义公共组件
import IBeautifulLink from "./components/Common/link/IBeautifulLink"
import "./assets/css/isoft_common.css"

import * as vClickOutside from 'v-click-outside-x';
Vue.use(vClickOutside);
//------------------------------------------------------------------

Vue.prototype.GLOBAL = global_;                    //挂载到Vue实例上面,通过 this.GLOBAL.xxx 访问全局变量
Vue.use(iView);

Vue.use(mavonEditor);

Vue.use(animated);

Vue.use(hovered);

Vue.component('IBeautifulLink', IBeautifulLink);

Vue.use(vuescroll);
//------------------------------------------------------------------

Vue.config.productionTip = false;


router.beforeEach((to, from, next) => {
  // LoadingBar 加载进度条
  iView.LoadingBar.start();

  CheckSSOLogin(to, from, next);
});

router.afterEach(route => {
  // LoadingBar 加载进度条
  iView.LoadingBar.finish();
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: {App},
  template: '<App/>',
  store, // 使用上vuex
});
