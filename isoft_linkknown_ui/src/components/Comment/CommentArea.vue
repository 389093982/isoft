<template>
  <div>
    <div v-for="(comment,index) in comments" style="margin-bottom:5px;padding: 10px;border: 1px solid #e9e9e9;">
      <div v-if="parent_id === 0" class="Floor"># {{total - (current_page - 1) * offset - index}}楼</div>
      <!--评论分三列-->
      <Row>
        <!--第一列：头像-->
        <Col span="2">
          <div v-if="parent_id===0">
            <router-link to="">
              <img class="isoft_hover_red" style="cursor: pointer;border: 1px solid grey;border-radius:50%; position: relative; top:5px" width=40px height=40px :src=comment.small_icon @error="defImg()">
            </router-link>
          </div>
          <div v-else style="margin: 12px 0 0 15px ">
            <router-link to="">
              <img class="isoft_hover_red" style="cursor: pointer;border: 1px solid grey;border-radius:50%;" width=25px height=25px :src=comment.small_icon @error="defImg()">
            </router-link>
          </div>
        </Col>
        <!--第二列： 第一行用户名，第二行评论内容-->
        <Col span="15" style="position: relative;left: -15px">
          <Row>
            <span style="color: rgba(119, 119, 119, 0.62);font-size: 13px">{{comment.nick_name}}</span>
            <router-link to="">{{comment.refer_user_name}}</router-link>
          </Row>
          <Row>
            <span v-if="parent_id === 0" style="color: rgb(121, 119, 118)"><code>评论</code>:</span>
            <span v-else style="color: rgb(121, 119, 118)"><code>回复</code>:</span>
            <span style="color: #555;font-size: 14px">{{comment.content}}</span>
          </Row>
        </Col>
        <!--第三列：第一行:时间, 第二行：子评论数 回复他/她  点赞-->
        <Col span="7">
          <i style="font-size: 12px">
            <Row>
              <span style="float: right;color: rgb(173, 170, 168);margin-right: 10px"><Time :time="comment.created_time" :interval="1"/></span>
            </Row>
            <Row>
            <span style="float: right;">
              <a @click="toggleShow(index,comment)" v-if="comment.sub_amount > 0" style="color: rgb(173, 170, 168)">子评论数({{comment.sub_amount}})</a>
              <a v-if="comment.depth < 2" href="javascript:;" style="color: rgb(173, 170, 168)" @click="replyComment(comment.id,comment.created_by)">回复他/她</a>&nbsp;
              <a href="javascript:;" style="color: rgb(173, 170, 168)">点赞</a>
          </span>
            </Row>
          </i>
        </Col>
      </Row>

      <!-- 递归,子评论区域 -->
      <CommentArea v-if="comment.sub_amount > 0" :parent_id="comment.id" :parent_comment="comment" :theme_pk="theme_pk" :theme_type="theme_type" :key="comment.id"/>
    </div>

    <!-- 顶级评论支持分页 -->
    <Page v-if="parent_id == 0 && total > 0" :total="total" :page-size="offset" show-total show-sizer
          :styles="{'text-align': 'center','margin-top': '10px'}"
          @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>

    <!-- 评论表单 -->
    <Modal
      v-model="showCommentForm"
      width="800"
      title="回复"
      :mask-closable="false">
      <CommentForm v-if="showCommentForm" :parent_id="_parent_id" :theme_pk="theme_pk" :theme_type="theme_type"
                   :refer_user_name="_refer_user_name" @refreshComment="refreshComment"/>
    </Modal>
  </div>
</template>

<script>
  import {FilterComment} from "../../api/index"
  import CommentForm from "./CommentForm"
  import {GetUserDetail} from "../../api"

  export default {
    name: "CommentArea",
    // 评论清单
    props: {
      parent_comment: {
        type: Object,
        default: null,
      },
      parent_id: {
        type: Number,
        default: -1,
      },
      theme_pk: {
        type: Number,
        default: -1,
      },
      theme_type: {
        type: String,
        default: "",
      }
    },
    components: {CommentForm},
    data() {
      return {
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 10,
        comments: [],
        showCommentForm: false,
        // 回复评论,两个参数分别是被评论id,被评论人
        _parent_id: 0,
        _refer_user_name: "",
      }
    },
    methods: {
      handleChange(page) {
        this.current_page = page;
        this.refreshComment();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshComment();
      },
      // 刷新当前父级评论对应的评论列表
      refreshComment: async function (comment_type) {
        if (comment_type == undefined) {
          comment_type = "all";
        }
        const result = await FilterComment(this.theme_pk, this.theme_type, this.parent_id, comment_type, this.offset, this.current_page);
        if (result.status == "SUCCESS") {
          this.showCommentForm = false;
          this.comments = result.comments;
          if (this.parent_id == 0 && result.paginator != null) {
            this.total = result.paginator.totalcount;     // 顶级评论支持分页
          }
        }
      },
      // 回复评论,两个参数分别是被评论id,被评论人
      replyComment: function (id, refer_user_name) {
        this._parent_id = id;
        this._refer_user_name = refer_user_name;
        this.showCommentForm = true;
      },
    },
    mounted: function () {
      this.refreshComment();
    },
    watch: {
      "theme_pk": "refreshComment",      // 如果 theme_pk 有变化,会再次执行该方法
      "parent_comment": "refreshComment",       // 如果 comment 有变化,会再次执行该方法
    }
  }
</script>

<style scoped>
  .Floor{
    color: #ff6900;
    font-size: 15px;

    position:relative;
    animation-name:myfirst;
    animation-duration:4s;
    animation-iteration-count:infinite;
    animation-direction:alternate;
    animation-play-state:running;
    /* Safari and Chrome: */
    -webkit-animation-name:myfirst;
    -webkit-animation-duration:4s;
    -webkit-animation-iteration-count:infinite;
    -webkit-animation-direction:alternate;
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
  a {
    color: red;
    margin-right: 10px;
  }
</style>
