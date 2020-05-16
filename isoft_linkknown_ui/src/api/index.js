/*
包含n个接口请求函数的模块
函数的返回值: promise对象
 */
import {ajax, download} from './ajax'
const BASE_URL = '/api';
const WECHAT_PAY = '/wechatPayApi';

//【User模块】***************************************************************************************************
export const GetHotUsers = () => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/GetHotUsers", {}, 'POST');
export const GetUserDetail = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GetUserDetail', params, 'POST');
export const UpdateUserIcon = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/UpdateUserIcon', params, 'POST');
export const Login = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/PostLogin", params, 'POST');// 登录接口
export const CreateVerifyCode = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/createVerifyCode", params, 'POST');
export const ModifyPwd = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/modifyPwd", params, 'POST');
export const GetUserInfoByNames = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/GetUserInfoByNames", params, 'POST');
export const EditUserSignature = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/EditUserSignature', params, 'POST');
export const UpdateUserDetail = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/UpdateUserDetail', params, 'POST');
export const RefreshToken = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/RefreshToken", params, 'POST');
export const GitHubLogin = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/GitHubLogin", params, 'POST');
export const QueryAllUsers = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/QueryAllUsers", params, 'POST');// 查询所有用户
export const LoginRecordList = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/LoginRecordList", params, 'POST');// 登录记录分页查询
export const AppRegisterList = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/AppRegisterList", params, 'POST');// 系统注册分页查询
export const AddAppRegister = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/AddAppRegister", params, 'POST');// 添加系统注册
export const Regist = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/Regist", params, 'POST');// 注册接口
// UpdateUserPoints 未直接使用，被其他流程调用
// insert_loginRecord 未直接使用，被其他流程调用
// SsoFilter 未直接使用，被其他流程调用
// getUserInfoFromRequest 未直接使用，被其他流程调用


//【Blog模块】***************************************************************************************************
export const BlogArticleEdit = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/BlogArticleEdit', params, 'POST');// 编辑或者新增博客文章
export const BlogCatalogEdit = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/BlogCatalogEdit', params, 'POST');// 编辑或者新增博客分类
export const ArticleDelete = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/ArticleDelete', params, 'POST');
export const QueryCustomTagBlog = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryCustomTagBlog', params, 'GET');
export const queryPageBlog = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/queryPageBlog', params, 'GET');// 热门博客分页列表
export const GetMyBlogs = () => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GetMyBlogs', {}, 'GET');// 获取我的所有博客文章
export const ShowBlogArticleDetail = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/ShowBlogArticleDetail', params, 'GET');// 根据 blog_id 查询 blog 详细信息
export const GetMyCatalogs = () => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GetMyCatalogs', {}, 'GET');// 获取我的所有博客分类


//【Course模块】***************************************************************************************************
export const SearchCourseList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/SearchCourseList', params, 'GET');// 课程搜索
export const ShowCourseHistory = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/ShowCourseHistory", params, 'GET');
export const UpdateCourseIcon = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/UpdateCourseIcon", params, 'GET');// 完结视频更新
export const DeleteVideo = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/DeleteVideo", params, 'GET');
export const ChangeVideoOrder = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/ChangeVideoOrder", params, 'GET');
export const QueryCustomTagCourse = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/QueryCustomTagCourse", params, 'GET');
export const videoPlayUrl = BASE_URL + "/iwork/httpservice/isoft_linkknown_api/VideoPlay";
export const GetAllCourseType = () => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/GetAllCourseType", {}, 'GET');// 获取所有课程类型
export const GetHotCourseRecommend = () => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/GetHotCourseRecommend", {}, 'GET');// 获取热门推荐的课程
export const GetAllCourseSubType = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GetAllCourseSubType', params, 'GET');// 根据课程名称获取所有子类型名称
export const ShowCourseDetail = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/ShowCourseDetail", params, 'GET');// 显示课程详细信息
export const EditCourse = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/EditCourse", params, 'GET');// 新建课程
export const GetCourseListByUserName = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/GetCourseListByUserName", params, 'GET');// 根据用户名查询用户的课程信息
export const UploadVideo = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/UploadVideo", params, 'GET');
// updateCourseNum 未直接使用，被其他流程调用
// ShowCourseDetailForApp 未直接使用，被其他流程调用
// InsertOrUpdateVedioRecord 未直接使用，被其他流程调用


//【Comment模块】***************************************************************************************************
export const FilterCommentFirstLevel = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/FilterCommentFirstLevel', params, 'GET');// 获取一级评论列表
export const FilterCommentSecondLevel = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/FilterCommentSecondLevel', params, 'GET');// 获取二级子评论列表
export const AddComment = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/AddComment', params, 'GET');// 添加评论
export const deleteComment = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/deleteComment", params, 'POST');//删除评论


//【CMS模块】***************************************************************************************************
// FilterConfigurations2 未发现被使用，后面确认是否可以删除


//【Book模块】***************************************************************************************************
export const BookEdit = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/BookEdit', params, 'POST');
export const QueryPageBookList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryPageBookList', params, 'POST');
export const BookArticleList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/BookArticleList', params, 'POST');
export const UpdateBookIcon = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/UpdateBookIcon', params, 'POST');
export const DeleteBookById = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/DeleteBookById', params, 'POST');
export const BookCatalogEdit = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/BookCatalogEdit', params, 'POST');
export const BookCatalogList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/BookCatalogList', params, 'POST');
export const ShowBookArticleDetail = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/ShowBookArticleDetail', params, 'POST');
export const BookArticleEdit = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/BookArticleEdit', params, 'POST');
export const ShowBookCatalogDetail = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/ShowBookCatalogDetail', params, 'POST');
export const DeleteBookCatalog = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/DeleteBookCatalog', params, 'POST');
export const QueryCustomTagBook = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryCustomTagBook', params, 'POST');
export const QueryBookListByIds = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryBookListByIds', params, 'POST');
export const ChangeCatalogOrder = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/ChangeCatalogOrder', params, 'POST');


//【Goods模块】***************************************************************************************************
export const GoodsEdit = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GoodsEdit', params, 'POST');
export const GoodsList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GoodsList', params, 'POST');
export const GetGoodsDetail = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GetGoodsDetail', params, 'POST');


//【Common模块】***************************************************************************************************
export const fileUploadUrl = BASE_URL + "/iwork/httpservice/isoft_linkknown_api/fileUpload";
export const GetUserFavoriteList = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/GetUserFavoriteList", params, 'GET');
export const UpdateVoteTag = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/UpdateVoteTag', params, 'POST');
export const FilterVoteTags = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/FilterVoteTags', params, 'POST');
export const queryFavoriteCount = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/queryFavoriteCount", params, 'GET');
export const IsFavorite = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/IsFavorite", params, 'GET');
export const ToggleFavorite = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/ToggleFavorite", params, 'GET');// 切换收藏点赞
export const QueryPageFileUploadLog = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/QueryPageFileUploadLog", params, 'GET');// 查看文件上传日志


//【Advise模块】***************************************************************************************************
export const InsertAdvise = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/InsertAdvise", params, 'POST');// 插入意见或吐槽
export const queryPageAdvise = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/queryPageAdvise', params, 'GET');//查询意见或吐槽


//【Advertisement模块】***************************************************************************************************
export const EditAdvertisement = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/EditAdvertisement', params, 'GET');
export const GetPersonalAdvertisement = () => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GetPersonalAdvertisement', {}, 'GET');
export const QueryAdvertisementById = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryAdvertisementById', params, 'GET');
export const GetRandomAdvertisement = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GetRandomAdvertisement', params, 'GET');
export const RecordAdvstAccessLog = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/RecordAdvstAccessLog', params, 'GET');
export const GetAdvstAccessLog = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GetAdvstAccessLog', params, 'GET');
// timer_clean_advertisement_log 未直接使用，被其他流程调用


//【Job模块】***************************************************************************************************
export const EditCorporateDetail = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/EditCorporateDetail', params, 'POST');
export const QueryCorporateDetail = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryCorporateDetail', params, 'POST');
export const EditJobDetail = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/EditJobDetail', params, 'POST');
export const QueryJobById = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryJobById', params, 'POST');
export const EditResume = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/EditResume', params, "POST");
export const QueryResume = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryResume', params, "POST");
export const FilterPageJobList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/FilterPageJobList', params, "POST");
export const ApplyJob = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/ApplyJob', params, 'POST');
export const GetJobApplyList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GetJobApplyList', params, "POST");


//【Resource模块】***************************************************************************************************
export const EditResource = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/EditResource', params, "POST");
export const FilterPageResourceList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/FilterPageResourceList', params, "POST");
export const GetResourceInfo = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GetResourceInfo', params, "POST");
export const IsEnoughPoints = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/IsEnoughPoints', params, "POST"); //下载之前先查看积分
export const DownloadResourceFile = (params) => download(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/DownloadResourceFile', params, "GET"); // 下载
export const RecommendResource = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/RecommendResource', params, "POST");


//【Message模块】***************************************************************************************************
export const QueryPageMessageList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryPageMessageList', params, "POST");
// InsertMessage 未直接使用，被其他流程调用


//【Expert模块】***************************************************************************************************
export const QueryPageExpertAsk = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryPageExpertAsk', params, "POST");
export const EditQuestion = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/EditQuestion', params, "POST");
export const ShowExpertAskDetail = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/ShowExpertAskDetail', params, "POST");
export const EditExpertAnswer = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/EditExpertAnswer', params, "POST");
export const QueryPageExpertAnswerList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryPageExpertAnswerList', params, "POST");
export const ModifyGoodNumber = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/ModifyGoodNumber', params, "POST");
export const QueryExpertWallList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryExpertWallList', params, "POST");
export const QueryWaitYourAnswerList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryWaitYourAnswerList', params, "POST");
export const IWantAskAlso = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/IWantAskAlso', params, "POST");


//【Vip模块】***************************************************************************************************
export const QueryRechargeRight = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryRechargeRight', params, "POST");


//【Decorate模块】***************************************************************************************************
export const EditDecorateItem = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/EditDecorateItem', params, 'POST');
export const LoadAllDecorates = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/LoadAllDecorates', params, 'POST');
export const LoadDecorateItems = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/LoadDecorateItems', params, 'POST');
// EditDecorate 未使用，后面确认是否可以删除


//【Placement模块】***************************************************************************************************
export const FilterPlacement = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/FilterPagePlacement', params, 'POST');
export const EditPlacement = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/EditPlacement', params, 'POST');
export const QueryPlacementById = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryPlacementByIdOrName', params, 'POST');
export const QueryPlacementByName = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryPlacementByIdOrName', params, 'POST');
export const CopyPlacement = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/CopyPlacement', params, 'POST');
export const DeletePlacementById = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/DeletePlacementById', params, 'POST');
export const FilterElements = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/FilterPageElement', params, 'POST');// 占位符、页面元素模块
export const EditElement = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/EditElement', params, 'POST');
export const QueryElementById = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/QueryElementById', params, 'POST');
export const CopyElement = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/CopyElement', params, 'POST');
export const UpdateElementStatus = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/UpdateElementStatus', params, 'POST');
export const FilterElementByPlacement = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/FilterElementByPlacement', params, 'POST');


//【Pay模块】***************************************************************************************************
export const isoft_unifiedpay_order_api = "ws://localhost:6002/"+WECHAT_PAY+"/Order"; //支付系统的接口
export const addPayOrder = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/addPayOrder", params, 'POST');
export const queryPayOrderList = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/queryPayOrderList", params, 'POST');
export const AddPayActivity = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/AddPayActivity", params, 'POST'); //举办活动
export const QueryPagePayActivity = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/QueryPagePayActivity", params, 'POST'); //查询活动
export const UpdateActivity = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/UpdateActivity", params, 'POST'); //更新活动
export const QueryPagePayCoupon = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/QueryPagePayCoupon", params, 'POST'); //查询券
export const QueryGeneralCoupon = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/QueryGeneralCoupon", params, 'POST'); //查询通用券
export const QueryDesignatedCoupon = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/QueryDesignatedCoupon", params, 'POST'); //查询指定券
export const ReceiveCoupon = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/ReceiveCoupon", params, 'POST'); //用户领券
export const SearchCouponForPay = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/SearchCouponForPay", params, 'POST'); //查询券(支付中心调用)
export const UpdateCouponIsUsed = (params) => ajax(BASE_URL + "/iwork/httpservice/isoft_linkknown_api/UpdateCouponIsUsed", params, 'POST'); //券已使用，更新状态


//【Contact模块】***************************************************************************************************
export const AddContactMessage = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/AddContactMessage', params, "POST");
export const GetContactMessage = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GetContactMessage', params, "POST");
export const GetContactUserList = (params) => ajax(BASE_URL + '/iwork/httpservice/isoft_linkknown_api/GetContactUserList', params, "POST");



//跨模块使用,模块化部署时需要使用 nginx 代理**************************************************************************
export const LoginAddr = "/sso/login/";
export const expires = /*60 * 60 * */1000;
