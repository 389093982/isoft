import Vue from 'vue'
import Router from 'vue-router'
import {joinArray} from "../tools"

// es6 import 异步语法,使用异步组件加载机制减少耗时操作
const Login = () => import("@/components/SSO/Login/Login");
const RegistOrForget = () => import("@/components/SSO/Login/RegistOrForget");
const AppRegist = () => import("@/components/SSO/AppRegist");
const LoginRecord = () => import("@/components/SSO/LoginRecord");
const IEmptyLayout = () => import("@/components/ILayout/IEmptyLayout");
const ICMSLayout = () => import("@/components/ILayout/ICMSLayout");
const IBlog = () => import("@/components/IBlog/IBlog");
const BlogList = () => import("@/components/IBlog/BlogList");
const BlogArticleDetail = () => import("@/components/IBlog/BlogArticleDetail");
const BlogArticleEdit = () => import("@/components/IBlog/BlogArticleEdit");
const BookCatalogEdit = () => import("@/components/IBook/BookCatalogEdit");
const BookList = () => import("@/components/IBook/BookList");
const BookCatalogs = () => import("@/components/IBook/BookCatalogs");
const BookArticleDetail = () => import("@/components/IBook/BookArticleDetail");
const UserDetail = () => import("@/components/User/UserDetail");
const UserGuide = () => import("@/components/User/UserGuide");
const ILearningIndex = () => import("@/components/ILearning/Index");
const BoutiqueCourse = () => import("@/components/ILearning/boutiqueCourse");
const CourseSpace = () => import("@/components/ILearning/CourseSpace/CourseSpace");
const EditCourse = () => import("@/components/ILearning/CourseSpace/EditCourse");
const RecentlyViewed = () => import("@/components/ILearning/CourseSpace/RecentlyViewed");
const MyCourseList = () => import("@/components/ILearning/CourseSpace/MyCourseList");
const CourseDetail = () => import("@/components/ILearning/Course/CourseDetail");
const Advise = () => import("@/components/ILearning/Advise");
const About = () => import("@/components/ILearning/About");
const VideoPay = () => import("@/components/ILearning/Course/VideoPay");
const CourseSearch = () => import("@/components/ILearning/Course/CourseSearch");
const AdviseList = () => import("@/components/Background/AdviseList");
const FoundList = () => import("@/components/IFound/FoundList");
const DiscountList = () => import("@/components/IFound/DiscountList");
const ActivityList = () => import("@/components/IFound/ActivityList");
const GoodList = () => import("@/components/IGood/GoodList");
const GoodEdit = () => import("@/components/IGood/GoodEdit");
const GoodDetail = () => import("@/components/IGood/GoodDetail");
const PayConfirm = () => import("@/components/IGood/PayConfirm");
const ILayout = () => import("@/components/ILayout/ILayout");
const VipIntroduction = () => import("@/components/VipCenter/VipIntroduction");
const Recharge = () => import("@/components/VipCenter/Recharge");
const AdvApply = () => import("@/components/Advertisement/Apply");
const AdvManage = () => import("@/components/Advertisement/Manage");
const JobList = () => import("@/components/IJob/JobList");
const ResumeManage = () => import("@/components/IJob/ResumeManage");
const CorporateDetail = () => import("@/components/IJob/CorporateDetail");
const EditCorporate = () => import("@/components/IJob/EditCorporate");
const EditJob = () => import("@/components/IJob/EditJob");
const EditResume = () => import("@/components/IJob/EditResume");
const JobApplyList = () => import("@/components/IJob/JobApplyList");
const ResourceList = () => import("@/components/Resource/ResourceList");
const UploadResource = () => import("@/components/Resource/UploadResource");
const DownloadResource = () => import("@/components/Resource/DownloadResource");
const AskExpert = () => import("@/components/Expert/AskExpert");
const AnswerExpert = () => import("@/components/Expert/AnswerExpert");
const EditQuestion = () => import("@/components/Expert/EditQuestion");
const MessageList = () => import("@/components/Message/MessageList");
const BusinessIntroduce = () => import("@/components/Bussiness/Introduce");
const Site = () => import("@/components/Site");
const CssDemo = () => import("@/components/CssDemo");

Vue.use(Router);

function getRootRouters() {
  return [
    {path: '/', redirect: '/ilearning/index'},
    {path: '/css/demo', component: CssDemo,},
    {path: '/site', component: ILayout, children: [{path: 'index', component: Site},]}
  ]
}

const ILearningRouter = [{
  path: '/ilearning', component: ILayout,
    children: [
      {path: 'index', component: ILearningIndex,},
      {path: 'boutiqueCourse', component: BoutiqueCourse,},
      {path: 'course_space', component: CourseSpace,
        redirect: '/ilearning/course_space/myCourseList',
        children: [
          {path: 'editCourse', component: EditCourse,},
          {path: 'myCourseList', component: MyCourseList,},
          {path: 'RecentlyViewed', component: RecentlyViewed,},
        ]
      },
      {path: 'course_detail', component: CourseDetail,},
      {path: 'video_play', component: VideoPay,},
      {path: 'advise', component: Advise,},
      {path: 'about', component: About,},
      {path: 'course_search', component: CourseSearch,},
    ]
}];

const IBlogRouter = [{
  path: '/iblog', component: ILayout,
    children: [
      {path: 'blog_index', component: IBlog},
      {path: 'blog_list', component: BlogList},
      {path: 'blog_detail', component: BlogArticleDetail},
      {path: 'blog_edit', component: BlogArticleEdit},
    ]
}];

const IBookRouter = [{
  path: '/ibook', component: ILayout,
    children: [
      {path: 'book_list', component: BookList},
      {path: 'book_detail', component: BookArticleDetail},
      {path: 'book_edit', component: BookCatalogEdit},
      {path: 'book_list', component: BookList},
      {path: 'book_catalogs', component: BookCatalogs},
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
      {path: 'advise_list', component: AdviseList},
      {path: 'sso/appRegist', component: AppRegist},
      {path: 'sso/loginRecord', component: LoginRecord},
    ]
}];

const IUserReouter = [{
  path: '/user', component: ILayout,
    children: [
      {path: 'detail', component: UserDetail},
      {path: 'guide', component: UserGuide},
      {path: 'mine/detail', component: UserDetail},
    ]
}];

const IExpert = [{
  path: '/expert', component: ILayout,
    children: [
      {path: 'ask_expert', component: AskExpert},
      {path: 'answer_expert', component: AnswerExpert},
      {path: 'edit_question', component: EditQuestion},
    ]
}];

const IJob = [{
  path: '/job', component: ILayout,
    children: [
      {path: 'jobList', component: JobList},
      {path: 'resume_manage', component: ResumeManage},
      {path: 'corporate_detail', component: CorporateDetail},
      {path: 'corporate_edit', component: EditCorporate},
      {path: 'job_edit', component: EditJob},
      {path: 'resume_edit', component: EditResume},
      {path: 'apply_list', component: JobApplyList},
    ]
}];

const IAdvertisement = [{
  path: '/advertisement', component: ILayout,
    children: [
      {path: 'apply', component: AdvApply},
      {path: 'manage', component: AdvManage},
    ]
}];

const IFoundReouter = [{
  path: '/ifound', component: ILayout,
    children: [
      {path: 'found_list', component: FoundList},
      {path: 'discount_list', component: DiscountList},
      {path: 'activity_list', component: ActivityList},
    ]
}];

const IGoodReouter = [{
  path: '/igood', component: ILayout,
    children: [
      {path: 'good_list', component: GoodList},
      {path: 'mine/good_list', component: GoodList},
      {path: 'mine/good_edit', component: GoodEdit},
      {path: 'good_detail', component: GoodDetail},
      {path: 'pay_confirm', component: PayConfirm},
    ]
}];

const MessageRouter = [{
  path: '/message', component: ILayout,
    children: [
      {path: 'message_list', component: MessageList},
    ]
}];

const BusinessRouter = [{
  path: '/business', component: ILayout,
  children: [
    {path: 'introduce', component: BusinessIntroduce},
  ]
}];

const ResourceRouter = [{
  path: '/resource', component: ILayout,
    children: [
      {path: 'list', component: ResourceList},
      {path: 'upload', component: UploadResource},
      {path: 'download', component: DownloadResource},
    ]
}];

const VipCenterReouter = [{
  path: '/vipcenter', component: ILayout,
    children: [
      {path: 'vipIntroduction', component: VipIntroduction},
      {path: 'recharge', component: Recharge},
    ]
}];

function getAllRouters() {
  let allRouters = [];
  allRouters = joinArray(allRouters, IExpert);
  allRouters = joinArray(allRouters, IJob);
  allRouters = joinArray(allRouters, IAdvertisement);
  allRouters = joinArray(allRouters, IFoundReouter);
  allRouters = joinArray(allRouters, IGoodReouter);
  allRouters = joinArray(allRouters, IUserReouter);
  allRouters = joinArray(allRouters, ILearningRouter);
  allRouters = joinArray(allRouters, IBlogRouter);
  allRouters = joinArray(allRouters, IBookRouter);
  allRouters = joinArray(allRouters, ISSOReouter);
  allRouters = joinArray(allRouters, ICMSReouter);
  allRouters = joinArray(allRouters, VipCenterReouter);
  allRouters = joinArray(allRouters, ResourceRouter);
  allRouters = joinArray(allRouters, MessageRouter);
  allRouters = joinArray(allRouters, BusinessRouter);
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
