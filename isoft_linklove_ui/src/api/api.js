// 所有界面的跨站请求在此统一管理，统一为POST异步请求
import {POST} from './POST'
const baseUrl = '/api/iwork/httpservice/';
const appName = 'linklove';

export const Login = (params) => POST(baseUrl+appName+'/Login',params);
export const Regist = (params) => POST(baseUrl+appName+'/Regist',params);
