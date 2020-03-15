<template>
  <div>

    <div v-for="(comment,index) in comments" style="margin-bottom:5px;padding: 10px;border: 1px solid #e9e9e9;">
      <div v-if="comment.parent_id === 0" class="Floor"># {{total - (current_page - 1) * offset - index}}楼</div>
      <!--评论分两行-->
      <Row>
        <!--第一行 第一列：头像-->
        <Col span="2">
          <div>
            <router-link :to="{path:'/user/detail',query:{username:comment.user_name}}">
              <HatAndFacePicture :src="comment.small_icon" :vip_level="comment.vip_level" :hat_in_use="comment.hat_in_use" :src_size="40" :hat_width="36" :hat_height="10" :hat_relative_left="2" :hat_relative_top="-56" ></HatAndFacePicture>
            </router-link>
          </div>
        </Col>
        <!--第一行 第二列： 分两行 -->
        <Col span="22" style="position: relative;left: -15px">
          <!--第一行：用户名-->
          <Row>
            <Col span="20">
              <router-link :to="{path:'/user/detail',query:{username:comment.user_name}}">
                <span style="color: rgba(119, 119, 119, 0.62);font-size: 13px">{{comment.nick_name}}</span>
              </router-link>
            </Col>
          </Row>
          <!--第二行：内容-->
          <Row>
            <Col span="19">
              <span style="color: #555;font-size: 14px;" v-html="comment.content"></span>
            </Col>
          </Row>
        </Col>
      </Row>

      <!--第二行：时间、回复、点赞、删除-->
      <Row>
        <i style="font-size: 12px">
          <!--第一列: 时间、回复-->
          <Col span="18">
            <span style="color: rgb(173, 170, 168);margin: 0 0 0 45px ">
              <Time :time="comment.created_time" :interval="1"/>
            </span>
            <span>
              <a @click="replyComment(comment.id,comment.created_by)" style="color: rgb(173, 170, 168);">
                <span>
                  <span style="font-size: 15px">·</span>
                  <span v-if="comment.sub_amount>0">({{comment.sub_amount}})</span>
                  回复
                </span>
              </a>
            </span>
          </Col>
          <!--第二列: 点赞、删除-->
          <Col span="6">
            <Icon type="md-heart-outline"  style="font-size: 20px;cursor: pointer;color: rgb(173, 170, 168)"/>&nbsp;&nbsp;&nbsp;
            <span v-if="isLoginUserName(comment.user_name)" @click="deleteComment(1,comment.id,comment.theme_pk,comment.theme_type)">
              <Icon type="ios-trash-outline" style="font-size: 18px;cursor: pointer;color: rgb(173, 170, 168)"/>
            </span>
          </Col>
        </i>
      </Row>

      <!-- 二级评论区域 -->
      <SonCommentArea v-if="comment.parent_id===0" :parent_user_name="comment.nick_name" :org_parent_id="comment.id" :parent_id="comment.id" :parent_comment="comment" :theme_pk="theme_pk" :theme_type="theme_type" :key="comment.id" @refreshComment="refreshComment" />
    </div>

    <!-- 一级评论支持分页 -->
    <Page v-if="parent_id === 0 && total > 0" :total="total" :page-size="offset" show-total show-sizer :styles="{'text-align': 'center','margin-top': '10px'}" @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>

    <!-- 评论表单 -->
    <Modal v-model="showCommentForm" width="800" title="回复" :mask-closable="false">
      <CommentForm v-if="showCommentForm" :org_parent_id="org_parent_id" :parent_id="parent_id" :theme_pk="theme_pk" :theme_type="theme_type" :refer_user_name="refer_user_name" @refreshComment="refreshComment"/>
    </Modal>

  </div>
</template>

<script>
  import {FilterCommentFirstLevel,deleteComment} from "../../api/index"
  import CommentForm from "./CommentForm"
  import {GetLoginUserName} from "../../tools"
  import SonCommentArea from "./SonCommentArea";
  import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";

  export default {
    name: "CommentArea",
    // 评论清单
    props: {
      theme_pk: {
        type: Number,
        default: -1,
      },
      theme_type: {
        type: String,
        default: "",
      },
    },
    components: {HatAndFacePicture, SonCommentArea, CommentForm},
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
        org_parent_id: 0,
        parent_id: 0,
        refer_user_name: "",
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
        const result = await FilterCommentFirstLevel(this.theme_pk, this.theme_type, comment_type, this.offset, this.current_page);
        if (result.status === "SUCCESS") {
          this.showCommentForm = false;
          this.comments = result.comments;
          this.total = result.paginator.totalcount;
        }
      },
      // 回复评论,两个参数分别是被评论id,被评论人
      replyComment: function (id, refer_user_name) {
        this.org_parent_id = id;
        this.parent_id = id;
        this.refer_user_name = refer_user_name;
        this.showCommentForm = true;
      },
      isLoginUserName: function (user_name) {
        return user_name === GetLoginUserName();
      },
      // 删除评论
      deleteComment:async function (level,id,theme_pk,theme_type) {
        let params = {
          "level":level,
          "id":id,
          "theme_pk":theme_pk,
          "theme_type":theme_type
        };
        const result = await deleteComment(params);
        if (result.status === "SUCCESS") {
          this.refreshComment();
          this.$Message.success("删除成功！")
        }
      }
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
  .Floor {
    color: #109e16;
    font-size: 15px;
  }
  a {
    color: red;
    margin-right: 10px;
  }
</style>
