/*
包含n个接口请求函数的模块
函数的返回值: promise对象
 */
import {ajax, download} from './ajax'

const BASE_URL = '/api';

export const FilterPageRequireList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_oa_api/FilterPageRequireList', params, "POST");
export const EditRequire = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_oa_api/EditRequire', params, "POST");
