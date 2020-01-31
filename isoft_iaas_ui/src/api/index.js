/*
包含n个接口请求函数的模块
函数的返回值: promise对象
 */
import {ajax, download} from './ajax'

const BASE_URL = '/api';
const WECHAT_PAY = '/wechatPayApi';

// 编辑或者新增博客分类
export const BlogCatalogEdit = (catalog_name, catalog_desc) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/BlogCatalogEdit', {
  catalog_name,
  catalog_desc
}, 'POST');

// 获取我的所有博客分类
export const GetMyCatalogs = () => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/GetMyCatalogs2', {}, 'GET');

// 获取我的所有博客文章
export const GetMyBlogs = () => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/GetMyBlogs2', {}, 'GET');

// 编辑或者新增博客文章
export const BlogArticleEdit = (article_id, bookId, blog_title, short_desc, key_words, catalog_name, content, link_href) =>
  ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/BlogArticleEdit', {
    article_id,
    bookId,
    blog_title,
    short_desc,
    key_words,
    catalog_name,
    content,
    link_href
  }, 'POST');
export const ArticleDelete = (article_id) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/ArticleDelete', {article_id}, 'POST');

// 热门博客分页列表
export const queryPageBlog = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/queryPageBlog', params, 'GET');

export const BookEdit = (book_id, book_name, book_desc) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/BookEdit', {
  book_id,
  book_name,
  book_desc
}, 'POST');
export const UpdateBookIcon = (book_id, book_img) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/UpdateBookIcon', {
  book_id,
  book_img
}, 'POST');
export const DeleteBookById = (id) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/DeleteBookById', {id}, 'POST');
export const BookList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/BookList', params, 'POST');
export const BookArticleList = (book_id) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/BookArticleList', {book_id}, 'POST');
export const BookCatalogEdit = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/BookCatalogEdit', params, 'POST');
export const BookCatalogList = (book_id) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/BookCatalogList', {book_id}, 'POST');
export const ShowBookArticleDetail = (book_catalog_id) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/ShowBookArticleDetail', {book_catalog_id}, 'POST');
export const BookArticleEdit = (id, book_catalog_id, content) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/BookArticleEdit', {
  id,
  book_catalog_id,
  content
}, 'POST');
export const ShowBookCatalogDetail = (id) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/ShowBookCatalogDetail', {id}, 'POST');

export const GoodEdit = (good_id, good_name, good_desc, good_price, good_seller, seller_contact, good_images) =>
  ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/GoodEdit', {
    good_id,
    good_name,
    good_desc,
    good_price,
    good_seller,
    seller_contact,
    good_images
  }, 'POST');
export const GoodList = () => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/GoodList', {}, 'POST');
export const NewOrder = (good_id) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/NewOrder', {good_id}, 'POST');
export const GetGoodDetail = (id) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/GetGoodDetail', {id}, 'POST');
export const GetOrderDetail = (orderCode) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/GetOrderDetail', {orderCode}, 'POST');

export const GetUserDetail = (userName) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/GetUserDetail', {userName}, 'POST');
export const UpdateUserIcon = (userName, small_icon) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/UpdateUserIcon', {
  userName,
  small_icon
}, 'POST');

// 根据 blog_id 查询 blog 详细信息
export const ShowBlogArticleDetail = (id) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/ShowBlogArticleDetail', {id}, 'GET');

// 新建课程
export const NewCourse = (course_name,course_type,course_sub_type,course_short_desc) =>
  ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/NewCourse2", {
    course_name,
    course_type,
    course_sub_type,
    course_short_desc
  }, 'GET');

// 根据用户名查询用户的课程信息
export const GetCourseListByUserName = (userName) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/GetCourseListByUserName", {userName}, 'GET');

// 完结视频更新
export const UpdateCourseIcon = (course_id, small_image) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/UpdateCourseIcon", {
  course_id,
  small_image
}, 'GET');
export const UploadVideo = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/UploadVideo", params, 'GET');
export const DeleteVideo = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/DeleteVideo", params, 'GET');

// 显示课程详细信息
export const ShowCourseDetail = (course_id) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/ShowCourseDetail2", {course_id}, 'GET');

// 切换收藏点赞
export const ToggleFavorite = (favorite_id, favorite_type) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/ToggleFavorite2", {
  favorite_id,
  favorite_type
}, 'GET');

// 添加评论
export const AddComment = (parent_id, content, theme_pk, theme_type, comment_type, refer_user_name) =>
  ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/AddComment2', {
    parent_id,
    content,
    theme_pk,
    theme_type,
    comment_type,
    refer_user_name
  }, 'GET');

// 获取评论列表
export const FilterComment = (theme_pk, theme_type, parent_id, comment_type, offset,current_page) =>
  ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/FilterComment2', {
    theme_pk,
    theme_type,
    parent_id,
    comment_type,
    offset,
    current_page
  }, 'GET');

// 获取所有课程类型
export const GetAllCourseType = () => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/GetAllCourseType2", {}, 'GET');

export const ShowCourseHistory = (offset, current_page) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/ShowCourseHistory2", {
  offset,
  current_page
}, 'GET');

// 获取热门推荐的课程
export const GetHotCourseRecommend = () => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/GetHotCourseRecommend2", {}, 'GET');

// 根据课程名称获取所有子类型名称
export const GetAllCourseSubType = (course_type) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/GetAllCourseSubType2', {course_type}, 'GET');

// 课程搜索
export const SearchCourseList = (search) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/SearchCourseList2', {search}, 'GET');

// 添加配置项
export const AddConfiguration = (parent_id, configuration_name, configuration_value) =>
  ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/AddConfiguration2', {
    parent_id,
    configuration_name,
    configuration_value
  }, 'GET');

export const FilterElementByPlacement = (placement) => ajax(BASE_URL+'/iwork/filterElementByPlacement',{placement},'GET');

// 登录接口
export const Login = (username, passwd, redirectUrl) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/PostLogin2", {
  username,
  passwd,
  redirectUrl
}, 'POST');

// 注册接口
export const Regist = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/Regist2", params, 'POST');

export const CreateVerifyCode = (username) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/createVerifyCode", {username}, 'POST');

export const ModifyPwd = (username, passwd, verifyCode) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/modifyPwd", {
  username,
  passwd,
  verifyCode
}, 'POST');

export const GetHotUsers = () => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/GetHotUsers", {}, 'POST');
export const GetUserInfoByNames = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/GetUserInfoByNames", params, 'POST');

// 系统注册分页查询
export const AppRegisterList = (offset, current_page, search) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/AppRegisterList2", {
  offset,
  current_page,
  search
}, 'POST');

// 添加系统注册
export const AddAppRegister = (app_address) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/AddAppRegister2", {app_address}, 'POST');

// 登录记录分页查询
export const LoginRecordList = (offset, current_page, search) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/LoginRecordList2", {
  offset,
  current_page,
  search
}, 'POST');

// 意见或建议
export const InsertAdvise = (advise) => ajax(BASE_URL + "/iwork/httpservice/isoft_iaas_api/InsertAdvise", {advise}, 'POST');

export const queryPageAdvise = (offset, current_page) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/queryPageAdvise', {
  offset,
  current_page
}, 'GET');

// 广告模块
export const GetPersonalAdvertisement = () => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/GetPersonalAdvertisement', {}, 'GET');
export const QueryAdvertisementById = (id) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/QueryAdvertisementById', {id}, 'GET');
export const GetRandomAdvertisement = (limit) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/GetRandomAdvertisement', {limit}, 'GET');
export const RecordAdvstAccessLog = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/RecordAdvstAccessLog', params, 'GET');
export const GetAdvstAccessLog = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/GetAdvstAccessLog', params, 'GET');

export const EditAdvertisement = (id, advertisement_label, linked_type, linked_refer, linked_img) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/EditAdvertisement', {
  id,
  advertisement_label,
  linked_type,
  linked_refer,
  linked_img
}, 'GET');

export const EditCorporateDetail = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/EditCorporateDetail', params, 'GET');

export const EditJobDetail = (id, corporate_id, job_name, job_age, job_address, salary_range) =>
  ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/EditJobDetail', {
    id,
    corporate_id,
    job_name,
    job_age,
    job_address,
    salary_range
  }, 'GET');

export const QueryCorporateDetail = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/QueryCorporateDetail', params, 'GET');
export const ApplyJob = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/ApplyJob', params, 'GET');

export const QueryJobById = (id) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/QueryJobById', {id}, 'GET');

export const EditResume = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/EditResume', params, "POST");

export const QueryResume = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/QueryResume', params, "POST");

export const FilterPageJobList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/FilterPageJobList', params, "POST");

//会员中心-初始下单
export const pay = (ProductId,ProductDesc,TransAmount,TransCurrCode) => ajax(WECHAT_PAY+'/Pay',{ProductId,ProductDesc,TransAmount,TransCurrCode},'POST');

// 资源列表模块
export const EditResource = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/EditResource', params, "POST");
export const FilterPageResourceList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/FilterPageResourceList', params, "POST");
export const GetResourceInfo = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_iaas_api/GetResourceInfo', params, "POST");
export const DownloadResourceFile = (params) => download(BASE_URL + '/iwork/httpservice/isoft_iaas_api/DownloadResourceFile', params, "GET");

export const fileUploadUrl = BASE_URL + "/iwork/httpservice/isoft_iaas_api/fileUpload"


// 跨模块使用,模块化部署时需要使用 nginx 代理
export const LoginAddr = "/#/sso/login/";

export const expires = /*60 * 60 * */1000;
