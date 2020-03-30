/*
ajax请求函数模块
返回值: promise对象(异步返回的数据是: response.data)
 */
import axios from 'axios'
import store from '@/store'
import Qs from 'qs'
import {CheckNotLogin} from "../tools"
import {checkCanRefresh, refreshToken} from "../tools/sso";

// 允许带认证信息的配置
axios.defaults.withCredentials = true;

// axios 拦截器拦截 401 异常跳往登录页面
axios.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // 此处注意 tokenString 过期 3 小时内可以 RefreshToken,超过 3 小时必须自动登录
          if (checkCanRefresh()) {
            refreshToken(store, function () {
              if (!CheckNotLogin()) {
                window.location.href = "/sso/login/?redirectUrl=" + window.location.href;
              }
            });
          } else {
            if (!CheckNotLogin()) {
              window.location.href = "/sso/login/?redirectUrl=" + window.location.href;
            }
          }
      }
    }
    return Promise.reject(error.response.data)   // 返回接口返回的错误信息
  }
);

export const ajax = function (url, data = {}, type = 'GET') {
  return new Promise(function (resolve, reject) {
    // 执行异步ajax请求
    let promise
    if (type === 'GET') {
      // 准备url query参数数据
      let dataStr = '' //数据拼接字符串
      Object.keys(data).forEach(key => {
        dataStr += key + '=' + data[key] + '&'
      })
      if (dataStr !== '') {
        dataStr = dataStr.substring(0, dataStr.lastIndexOf('&'))
        url = url + '?' + dataStr
      }
      // 发送get请求,得到 promise 对象
      promise = axios.get(url)
    } else {
      // 发送post请求,得到 promise 对象
      // 使用 Qs.stringify 来解决 axios 发 post 请求,后端接收不到参数的解决方案
      promise = axios.post(url, Qs.stringify(data))
    }
    promise.then(function (response) {
      // 成功了调用resolve()
      resolve(response.data)
    }).catch(function (error) {
      //失败了调用reject()
      reject(error)
    })
  })
}

export const download = function (url, data = {}, type = 'GET') {
  let dataStr = '' //数据拼接字符串
  Object.keys(data).forEach(key => {
    dataStr += key + '=' + data[key] + '&'
  })
  if (dataStr !== '') {
    dataStr = dataStr.substring(0, dataStr.lastIndexOf('&'))
    url = url + '?' + dataStr
  }
  // 发送get请求
  window.location.href = url;
}

