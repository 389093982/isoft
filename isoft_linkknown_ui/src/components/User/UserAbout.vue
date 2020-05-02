<template>
  <div v-if="getUserName()" style="margin:2px 0 5px 5px;padding: 15px;min-height: 350px;">
    <Row v-if="!isLoginUserName(getUserName())">
        <span @click="$router.push({path:'/user/userDetail',query:{username:getUserName()}})">
          <HatAndFacePicture :src="user_small_icon" :vip_level="vip_level" :hat_in_use="hat_in_use" :src_size="30" :hat_width="30" :hat_height="10" :hat_relative_left="0" :hat_relative_top="-46" ></HatAndFacePicture>
        </span>
        <div style="position: absolute;top: 5px;left: 40px">
          <span>
            <span v-if="nick_name">{{nick_name}}</span>
            <span v-else>{{getUserName()}}</span>
          </span>
          <span style="margin-left: 100px">
            <span v-if="userName===loginUserName()"></span>
            <span v-else class="ToMyCenter" @click="$router.push({path:'/user/userDetail'})"><Icon type="ios-arrow-forward" /><i>我的个人中心</i></span>
          </span>
        </div>
    </Row>

    <div style="margin-top: 5px;">
      <Tabs :animated="false">
        <TabPane icon="ios-card-outline" :label="userName===loginUserName()?'我的博文':'作者博文'">
          <Row>
            <Col span="12">标题</Col>
            <Col span="6">分类</Col>
            <Col span="6">时间</Col>
          </Row>
          <Row v-for="(blog,index) in blogs" style="line-height: 28px;height: 28px;">
            <Col span="12" class="isoft_inline_ellipsis">
              <IBeautifulLink @onclick="$router.push({path:'/iblog/blogArticleDetail',query:{blog_id:blog.id}})">
                {{blog.blog_title | titleLimitFunc(titleLimitLenth)}}
              </IBeautifulLink>
            </Col>
            <Col span="6" class="isoft_inline_ellipsis">
              <span class="isoft_tag1 isoft_point_cursor" @click="$router.push({path:'/iblog/blogArticleDetail',
                query:{blog_id:blog.id}})">{{blog.catalog_name}}</span>
            </Col>
            <Col span="6" class="isoft_inline_ellipsis isoft_color_grey isoft_font12">
              <Time :time="blog.last_updated_time" type="date"/>
            </Col>
          </Row>
        </TabPane>
        <TabPane icon="ios-videocam-outline" :label="userName===loginUserName()?'我的课程':'作者课程'">
          <Row>
            <Col span="8">课程名称</Col>
            <Col span="16">课程类型</Col>
          </Row>
          <Row v-for="course in courses" :gutter="10" style="line-height: 28px;height: 28px;">
            <Col span="8" class="isoft_inline_ellipsis">
              <IBeautifulLink @onclick="$router.push({path:'/ilearning/courseDetail',query:{course_id:course.id}})">
                {{course.course_name}}
              </IBeautifulLink>
            </Col>
            <Col span="16" class="isoft_inline_ellipsis">
              <span class="isoft_tag1 isoft_point_cursor" @click="$router.push({ path:'/ilearning/courseSearch',
              query: { search: course.course_type }})">{{course.course_type}}</span>
              /
              <span class="isoft_tag1 isoft_point_cursor" @click="$router.push({ path: '/ilearning/courseSearch',
              query: { search: course.course_sub_type }})">{{course.course_sub_type}}</span>
            </Col>
          </Row>
        </TabPane>
        <TabPane icon="ios-book-outline" :label="userName===loginUserName()?'我的书单':'作者书单'">
          <Row>
            <Col span="16">图书名称</Col>
            <Col span="8">时间</Col>
          </Row>
          <Row v-for="(book,index) in books">
            <Row style="line-height: 28px;height: 28px;">
              <Col span="16" class="isoft_inline_ellipsis">
                <IBeautifulLink @onclick="$router.push({path:'/ibook/bookArticleDetail',query:{book_id:book.id}})">
                  {{book.book_name}}
                </IBeautifulLink>
              </Col>
              <Col span="8" class="isoft_color_grey isoft_font12">
                <Time :time="book.last_updated_time" type="date"/>
              </Col>
            </Row>
          </Row>
        </TabPane>
      </Tabs>
    </div>
  </div>
</template>

<script>
  import {GetCourseListByUserName, GetUserDetail, queryPageBlog, QueryPageBookList} from "../../api"
  import IBeautifulLink from "../Common/link/IBeautifulLink"
  import {checkEmpty, GetLoginUserName} from "../../tools"
  import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";

  export default {
    name: "UserAbout",
    components: {HatAndFacePicture, IBeautifulLink},
    props: {
      userName: {
        type: String,
        default: ''
      },
      titleLimitLenth:{
        type:Number,
        default:25,
      },
    },
    data() {
      return {
        // 当前 userName 的书单列表
        books: [],
        // 当前 userName 的博客列表
        blogs: [],
        // 当前 userName 的课程列表
        courses: [],
        // 当前 user 对应头像信息
        user_small_icon: '',
        nick_name: '',
        vip_level:'',
        hat:'',
        hat_in_use:'',
        defaultImg: require('../../../static/images/common_img/default.png'),
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      isLoginUserName: function (user_name) {
        return user_name === GetLoginUserName();
      },
      refreshUserInfo: function () {
        if (checkEmpty(this.userName) && checkEmpty(GetLoginUserName())) {
          return;
        }
        this.refreshQueryPageBookList();
        this.refreshBlogList();
        this.refreshCourseList();
        this.refreshUserDetail();
      },
      getUserName: function () {
        return !checkEmpty(this.userName) ? this.userName : GetLoginUserName();
      },
      loginUserName:function(){
        return GetLoginUserName();
      },
      refreshUserDetail: async function () {
        // TODO 改成从全局的 store 中读取,一次只查一下
        let params = {
          'userName':this.getUserName()
        };
        const result = await GetUserDetail(params);
        if (result.status === "SUCCESS") {
          this.user_small_icon = result.user.small_icon;
          this.nick_name = result.user.nick_name;
          this.vip_level = result.user.vip_level;
          this.hat = result.user.hat;
          this.hat_in_use = result.user.hat_in_use;
        }
      },
      refreshQueryPageBookList: async function () {
        const result = await QueryPageBookList({
          search_type: '',
          search_user_name: this.getUserName(),
          current_page: 1,
          offset: 10,
        });
        if (result.status === "SUCCESS") {
          this.books = result.books;
        }
      },
      refreshBlogList: async function () {
        const result = await queryPageBlog({
          offset: 20,
          current_page: 1,
          search_user_name: this.getUserName(),
        });
        if (result.status === "SUCCESS") {
          this.blogs = result.blogs;
        }
      },
      refreshCourseList: async function () {
        const result = await GetCourseListByUserName({userName: this.getUserName(), current_page: 1, offset: 10});
        if (result.status === "SUCCESS") {
          this.courses = result.courses;
        }
      },
    },
    watch: {
      "userName": "refreshUserInfo"      // 如果 userName 有变化,会再次执行该方法
    },
    mounted() {
      this.refreshUserInfo();
    },
    filters: {
      // 内容超长则显示部分
      titleLimitFunc:function (value,limitLenth) {
        if (value.length > limitLenth) {
          return value.slice(0,limitLenth) + ' · · ·'
        }else {
          return value
        }
      }
    },
  }
</script>

<style scoped>
  .ToMyCenter{
    color: #ff6900;
    font-size: 12px;
    cursor: pointer;

    position:relative;
    animation-name:myfirst;
    animation-duration:6s;
    animation-iteration-count:infinite;
    animation-play-state:running;
    /* Safari and Chrome: */
    -webkit-animation-name:myfirst;
    -webkit-animation-duration:6s;
    -webkit-animation-iteration-count:infinite;
    -webkit-animation-play-state:running;
  }

  @keyframes myfirst
  {
    0%   {color:red; left:0px; top:0px;}
    25%  {color:yellow; left:0px; top:0px;}
    50%  {color:blue; left:0px; top:0px;}
    75%  {color:green; left:0px; top:0px;}
    100% {color:red; left:0px; top:0px;}
  }

  @-webkit-keyframes myfirst /* Safari and Chrome */
  {
    0%   {color:red; left:0px; top:0px;}
    25%  {color:yellow; left:0px; top:0px;}
    50%  {color:blue; left:0px; top:0px;}
    75%  {color:green; left:0px; top:0px;}
    100% {color:red; left:0px; top:0px;}
  }
</style>
