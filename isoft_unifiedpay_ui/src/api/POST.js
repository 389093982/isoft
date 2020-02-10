// 所有POST请求在此发送
import axios from 'axios'
import Qs from 'qs'

export function POST(url, params) {
  //返回异步请求结果
  return new Promise(function(resolve,reject){
    //发送请求并返回数据
    axios.post(url, Qs.stringify(params))
      .then(res => {
        // console.log(res.data);
        resolve(res.data);
      })
      .catch(err => {
        console.log(err);
        reject(err);
      });
  });


}


