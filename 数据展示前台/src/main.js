// * Vue Material Dashboard - v1.3.2
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import VueRouter from "vue-router";
import App from "./App";

// Element
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
Vue.use(ElementUI);

// Vuex
import store from "./store/store.js";

// Vue-Router
import routes from "./routes/routes";
Vue.use(VueRouter);
// configure router
const router = new VueRouter({
  routes, // short for routes: routes,
  mode: "history",
  linkExactActiveClass: "nav-item active"
});

// 绑定自己api
import api from "./api/index.js";
Vue.use(function(Vue) {
  Vue.prototype.$api = api;
});

// Echarts
import ECharts from "vue-echarts"; // 在 webpack 环境下指向 components/ECharts.vue
// 注册组件后即可使用
Vue.component("v-chart", ECharts);

// Plugins
// MaterialDashboard plugin
import MaterialDashboard from "./material-dashboard";
import Chartist from "chartist";
Vue.prototype.$Chartist = Chartist;
Vue.use(MaterialDashboard);

/* eslint-disable no-new */
new Vue({
  el: "#app",
  render: h => h(App),
  router,
  store,
  data: {
    Chartist: Chartist
  }
});
