import Vue from 'vue'
import Router from 'vue-router'
import {joinArray} from "../tools"

// es6 import 异步语法,使用异步组件加载机制减少耗时操作
const Login = () => import("@/components/SSO/Login/Login");
const RegistOrForget = () => import("@/components/SSO/Login/RegistOrForget");
const LoginRecord = () => import("@/components/SSO/LoginRecord");
const IEmptyLayout = () => import("@/components/ILayout/IEmptyLayout");
const ICMSLayout = () => import("@/components/ILayout/ICMSLayout");
const BlogIndex = () => import("@/components/IBlog/BlogIndex");
const BlogList = () => import("@/components/IBlog/BlogList");
const BlogArticleDetail = () => import("@/components/IBlog/BlogArticleDetail");
const BlogArticleEdit = () => import("@/components/IBlog/BlogArticleEdit");
const BookCatalogEdit = () => import("@/components/IBook/BookCatalogEdit");
const BookList = () => import("@/components/IBook/BookList");
const BookCatalogs = () => import("@/components/IBook/BookCatalogs");
const BookArticleDetail = () => import("@/components/IBook/BookArticleDetail");
const UserDetail = () => import("@/components/User/UserDetail");
const UserInfoSet = () => import("@/components/User/Set/UserInfoSet");
const UserInfo = () => import("@/components/User/Set/UserInfo");
const UserGuide = () => import("@/components/User/UserGuide");
const UserAttentionOrFensi = () => import("@/components/User/UserAttentionOrFensi");
const ILearningIndex = () => import("@/components/ILearning/Index");
const DomainName = () => import("@/components/ILearning/DomainName");
const JingpinCourse = () => import("@/components/ILearning/JingpinCourse");
const CourseSpace = () => import("@/components/ILearning/CourseSpace/CourseSpace");
const EditCourse = () => import("@/components/ILearning/CourseSpace/EditCourse");
const RecentlyViewed = () => import("@/components/ILearning/CourseSpace/RecentlyViewed");
const MyCourseList = () => import("@/components/ILearning/CourseSpace/MyCourseList");
const CourseDetail = () => import("@/components/ILearning/Course/CourseDetail");
const Advise = () => import("@/components/ILearning/Advise");
const About = () => import("@/components/ILearning/About");
const VideoPlay = () => import("@/components/ILearning/Course/VideoPlay");
const CourseSearch = () => import("@/components/ILearning/Course/CourseSearch");
const AdviseList = () => import("@/components/Background/AdviseList");
const FoundList = () => import("@/components/IFound/FoundList");
const NewsList = () => import("@/components/IFound/NewsList");
const ActivityList = () => import("@/components/IFound/ActivityList");
const ILayout = () => import("@/components/ILayout/ILayout");
const VipIntroduction = () => import("@/components/VipCenter/VipIntroduction");
const Recharge = () => import("@/components/VipCenter/Recharge");
const Pay = () => import("@/components/Payment/Pay");
const OrderList = () => import("@/components/Payment/OrderList");
const OrderDetail = () => import("@/components/Payment/OrderDetail");
const MyCouponList = () => import("@/components/Payment/MyCouponList");
const CouponCenter = () => import("@/components/Payment/CouponCenter");
const ShoppingCart = () => import("@/components/Payment/ShoppingCart");
const JobList = () => import("@/components/IJob/JobList");
const ResumeManage = () => import("@/components/IJob/ResumeManage");
const CorporateDetail = () => import("@/components/IJob/CorporateDetail");
const CorporateEdit = () => import("@/components/IJob/CorporateEdit");
const JobEdit = () => import("@/components/IJob/JobEdit");
const ResumeEdit = () => import("@/components/IJob/ResumeEdit");
const JobApplyList = () => import("@/components/IJob/JobApplyList");
const ResourceList = () => import("@/components/Resource/ResourceList");
const ResourceUpload = () => import("@/components/Resource/ResourceUpload");
const ResourceDownload = () => import("@/components/Resource/ResourceDownload");
const ExpertAsk = () => import("@/components/Expert/ExpertAsk");
const ExpertAnswer = () => import("@/components/Expert/ExpertAnswer");
const EditQuestion = () => import("@/components/Expert/EditQuestion");
const MessageList = () => import("@/components/Message/MessageList");
const BusinessIntroduce = () => import("@/components/Business/Introduce");
const BusinessList = () => import("@/components/Business/BusinessList");
const BusinessEdit = () => import("@/components/Business/BusinessEdit");
const BusinessDetail = () => import("@/components/Business/BusinessDetail");
const PayConfirm = () => import("@/components/Business/PayConfirm");
const ContactList = () => import("@/components/Contact/ContactList");
const SiteIndex = () => import("@/components/SiteIndex");
const NotFound = () => import("@/components/NotFound");
const CssDemo = () => import("@/components/CssDemo");
const ElementList = () => import("@/components/Background/IPlacement/ElementList");
const ElementEdit = () => import("@/components/Background/IPlacement/ElementEdit");
const PlacementList = () => import("@/components/Background/IPlacement/PlacementList");
const PlacementEdit = () => import("@/components/Background/IPlacement/PlacementEdit");
const AppVersionList = () => import("@/components/Background/AppVersionList");
const UserSearch = () => import("@/components/Background/UserSearch");
const CreateActivity = () => import("@/components/Background/Pay/CreateActivity");
const SearchActivity = () => import("@/components/Background/Pay/SearchActivity");
const SearchCoupon = () => import("@/components/Background/Pay/SearchCoupon");
const FileUploadMonitor = () => import("@/components/Background/FileUploadMonitor");
const KaoshiClassify = () => import("@/components/Background/KaoshiClassify");
const KaoshiTimu = () => import("@/components/Background/KaoshiTimu");
const KaoshiTimuEdit = () => import("@/components/Background/KaoshiTimuEdit");

Vue.use(Router);

function getRootRouters() {
  return [
    {path: '/', redirect: '/ilearning/index'},
    {path: '/css/cssDemo', component: CssDemo,},
    {path: '/site', component: ILayout,
      children: [
        {path: 'siteIndex', component: SiteIndex},
      ]
    },
    {path: '/404',name: '404',component: NotFound},
    {path: '*',redirect: '/404'}
  ]
}

const ILearningRouter = [{
  path: '/ilearning', component: ILayout,
    children: [
      {path: 'index', component: ILearningIndex,},
      {path: 'domainName', component: DomainName,},
      {path: 'jingpinCourse', component: JingpinCourse,},
      {path: 'courseSpace', component: CourseSpace,
        redirect: '/ilearning/courseSpace/myCourseList',
        children: [
          {path: 'editCourse', component: EditCourse,},
          {path: 'myCourseList', component: MyCourseList,},
          {path: 'recentlyViewed', component: RecentlyViewed,},
        ]
      },
      {path: 'courseDetail', component: CourseDetail,},
      {path: 'videoPlay', component: VideoPlay,},
      {path: 'advise', component: Advise,},
      {path: 'about', component: About,},
      {path: 'courseSearch', component: CourseSearch,},
    ]
}];

const IBlogRouter = [{
  path: '/iblog', component: ILayout,
    children: [
      {path: 'blogIndex', component: BlogIndex},
      {path: 'blogList', component: BlogList},
      {path: 'blogArticleDetail', component: BlogArticleDetail},
      {path: 'blogArticleEdit', component: BlogArticleEdit},
    ]
}];

const IBookRouter = [{
  path: '/ibook', component: ILayout,
    children: [
      {path: 'bookList', component: BookList},
      {path: 'bookArticleDetail', component: BookArticleDetail},
      {path: 'bookCatalogEdit', component: BookCatalogEdit},
      {path: 'bookCatalogs', component: BookCatalogs},
    ]
}];

const ISSOReouter = [{
  path: '/sso', component: IEmptyLayout,
    children: [
      {path: 'login', component: Login},
      {path: 'regist', component: RegistOrForget},
      {path: 'forget', component: RegistOrForget},
    ]
}];

const ICMSReouter = [{
  path: '/background', component: ICMSLayout,
    children: [
      {path: 'adviseList', component: AdviseList},
      {path: 'sso/loginRecord', component: LoginRecord},
      {path: 'elementEdit', component: ElementEdit},
      {path: 'elementList', component: ElementList},
      {path: 'placementList', component: PlacementList},
      {path: 'placementEdit', component: PlacementEdit},
      {path: 'appVersionList', component: AppVersionList},
      {path: 'userSearch', component: UserSearch},
      {path: 'createActivity', component: CreateActivity},
      {path: 'searchActivity', component: SearchActivity},
      {path: 'searchCoupon', component: SearchCoupon},
      {path: 'fileUploadMonitor', component: FileUploadMonitor},
      {path: 'kaoshiClassify', component: KaoshiClassify},
      {path: 'kaoshiTimu', component: KaoshiTimu},
      {path: 'kaoshiTimuEdit', component: KaoshiTimuEdit},
    ]
}];

const IUserReouter = [{
  path: '/user', component: ILayout,
    children: [
      {path: 'userDetail', component: UserDetail},
      {path: 'userGuide', component: UserGuide},
      {path: 'mine/userDetail', component: UserDetail},
      {path: 'set', component: UserInfoSet,
        redirect: '/user/set/userInfo',
        children: [
          {path: 'userInfo', component: UserInfo,},
        ]},
      {path: 'userAttentionOrFensi', component: UserAttentionOrFensi},
    ]
}];

const IExpert = [{
  path: '/expert', component: ILayout,
    children: [
      {path: 'expertAsk', component: ExpertAsk},
      {path: 'expertAnswer', component: ExpertAnswer},
      {path: 'editQuestion', component: EditQuestion},
    ]
}];

const IJob = [{
  path: '/job', component: ILayout,
    children: [
      {path: 'jobList', component: JobList},
      {path: 'resumeManage', component: ResumeManage},
      {path: 'corporateDetail', component: CorporateDetail},
      {path: 'corporateEdit', component: CorporateEdit},
      {path: 'jobEdit', component: JobEdit},
      {path: 'resumeEdit', component: ResumeEdit},
      {path: 'jobApplyList', component: JobApplyList},
    ]
}];

const IFoundReouter = [{
  path: '/found', component: ILayout,
    children: [{
        path: 'list', component: FoundList,
          children: [
            {path: 'activityList', component: ActivityList},
            {path: 'newsList', component: NewsList},
          ]
     },]
}];

const MessageRouter = [{
  path: '/message', component: ILayout,
    children: [
      {path: 'messageList', component: MessageList},
    ]
}];

const BusinessRouter = [{
  path: '/business', component: ILayout,
    children: [
      {path: 'businessIntroduce', component: BusinessIntroduce},
      {path: 'businessList', component: BusinessList},
      {path: 'businessEdit', component: BusinessEdit},
      {path: 'businessDetail', component: BusinessDetail},
      {path: 'payConfirm', component: PayConfirm},
    ]
}];

const ContactRouter = [{
  path: '/contact', component: ILayout,
  children: [
    {path: 'contactList', component: ContactList},
  ]
}];

const ResourceRouter = [{
  path: '/resource', component: ILayout,
    children: [
      {path: 'resourceList', component: ResourceList},
      {path: 'resourceUpload', component: ResourceUpload},
      {path: 'resourceDownload', component: ResourceDownload},
    ]
}];

const VipCenterReouter = [{
  path: '/vipcenter', component: ILayout,
    children: [
      {path: 'vipIntroduction', component: VipIntroduction},
      {path: 'recharge', component: Recharge},
    ]
}];

const Payment = [{
  path: '/payment', component: ILayout,
  children: [
    {path: 'pay',name:'pay', component: Pay},
    {path: 'orderList', component: OrderList},
    {path: 'orderDetail', component: OrderDetail},
    {path: 'myCouponList', component: MyCouponList},
    {path: 'couponCenter', component: CouponCenter},
    {path: 'shoppingCart', component: ShoppingCart},
  ]
}];

function getAllRouters() {
  let allRouters = [];
  allRouters = joinArray(allRouters, IExpert);
  allRouters = joinArray(allRouters, IJob);
  allRouters = joinArray(allRouters, IFoundReouter);
  allRouters = joinArray(allRouters, IUserReouter);
  allRouters = joinArray(allRouters, ILearningRouter);
  allRouters = joinArray(allRouters, IBlogRouter);
  allRouters = joinArray(allRouters, IBookRouter);
  allRouters = joinArray(allRouters, ISSOReouter);
  allRouters = joinArray(allRouters, ICMSReouter);
  allRouters = joinArray(allRouters, VipCenterReouter);
  allRouters = joinArray(allRouters, Payment);
  allRouters = joinArray(allRouters, ResourceRouter);
  allRouters = joinArray(allRouters, MessageRouter);
  allRouters = joinArray(allRouters, BusinessRouter);
  allRouters = joinArray(allRouters, ContactRouter);
  allRouters = joinArray(allRouters, getRootRouters());
  return allRouters;
}

export default new Router({
  // # 主要用来区分前后台应用, history 模式需要使用 nginx 代理
  // History 模式,去除vue项目中的 #
  mode: 'history',
  routes: getAllRouters(),
  // 页面跳转时,让页面滚动在顶部
  scrollBehavior(to, from, savedPosition) {
    // from 和 to 相同路由页面不滚动到顶部
    if (from.path === to.path) {
      return;
    }
    if (savedPosition) {
      return savedPosition;
    } else {
      return {x: 0, y: 0}
    }
  },
})
