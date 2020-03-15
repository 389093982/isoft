<template>
  <div>

    <div v-for="(comment,index) in comments" style="margin-bottom:5px;padding: 10px;border: 1px solid #e9e9e9;">
      <!--评论分两行-->
      <Row>
        <!--第一行 第一列：头像-->
        <Col span="2">
          <div style="margin: 12px 0 0 15px ">
            <router-link :to="{path:'/user/detail',query:{username:comment.created_user_account}}">
              <HatAndFacePicture :src="comment.created_user_small_icon" :vip_level="comment.created_user_vip_level" :hat_in_use="comment.created_user_hat_in_use" :src_size="25" :hat_width="21" :hat_height="7" :hat_relative_left="2" :hat_relative_top="-44" ></HatAndFacePicture>
            </router-link>
          </div>
        </Col>
        <!--第一行 第二列： 分两行 -->
        <Col span="22" style="position: relative;left: -15px">
          <!--第一行：用户名-->
          <Row>
            <Col span="20">
              <router-link :to="{path:'/user/detail',query:{username:comment.created_user_account}}">
                <span style="color: rgba(119, 119, 119, 0.62);font-size: 13px">{{comment.created_user_nick_name}}</span>
              </router-link>
            </Col>
          </Row>
          <!--第二行：内容-->
          <Row>
            <Col span="19">
              <span v-if="comment.depth === 1" style="color: rgb(121, 119, 118)"></span>
              <span v-else style="color: rgb(121, 119, 118)"><code>回复&nbsp;</code>
                <span style="color: rgba(119, 119, 119, 0.62);font-size: 13px">{{comment.refer_nick_name}}</span>&nbsp;:&nbsp;
              </span>
              <span style="color: #555;font-size: 14px" v-html="comment.content"></span>
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
                  <span style="font-size: 15px">·</span>回复
                </span>
              </a>
            </span>
          </Col>
          <!--第二列: 点赞、删除-->
          <Col span="6">
            <Icon type="md-heart-outline"  style="font-size: 20px;cursor: pointer;color: rgb(173, 170, 168)"/>&nbsp;&nbsp;&nbsp;
            <span v-if="isLoginUserName(comment.created_user_account)" @click="deleteComment(2,comment.id,comment.org_parent_id)">
              <Icon type="ios-trash-outline" style="font-size: 18px;cursor: pointer;color: rgb(173, 170, 168)"/>
            </span>
          </Col>
        </i>
      </Row>

    </div>

    <!-- 评论表单 -->
    <Modal v-model="showCommentForm" width="800" title="回复" :mask-closable="false">
      <CommentForm v-if="showCommentForm" :org_parent_id="org_parent_id" :parent_id="parent_id" :theme_pk="theme_pk" :theme_type="theme_type" :refer_user_name="refer_user_name" @refreshComment="refreshComment"/>
    </Modal>

  </div>
</template>

<script>
  import {FilterCommentSecondLevel,deleteComment} from "../../api/index"
  import {GetLoginUserName} from "../../tools"
  import CommentForm from "./CommentForm";
  import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";

  export default {
    name: "SonCommentArea",
    // 评论清单
    props: {
      parent_user_name: {
        type: String,
        default: "",
      },
      org_parent_id: {
        type: Number,
        default: -1,
      },
      parent_id: {
        type: Number,
        default: -1,
      },
      parent_comment: {
        type: Object,
        default: null,
      },
      theme_pk: {
        type: Number,
        default: -1,
      },
      theme_type: {
        type: String,
        default: "",
      },
    },
    components: {HatAndFacePicture, CommentForm},
    data() {
      return {
        comments: [],
        showCommentForm: false,
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
      refreshComment: async function (comment_type) {
        const result = await FilterCommentSecondLevel(this.theme_pk, this.theme_type,this.org_parent_id);
        if (result.status === "SUCCESS") {
          this.showCommentForm = false;
          this.comments = result.comments;
        }

        // CommentForm调SonCommentArea,  SonCommentArea再调CommentArea,  但是父组件刷新，这里做个传值判断是否继续调父组件，否则会成死循环
        if (comment_type === 'all') {
          this.$emit('refreshComment',comment_type);
        }
      },
      // 回复评论,两个参数分别一级评论的id,被评论人
      replyComment: function (id, refer_user_name) {
        this.parent_id = id;
        this.refer_user_name = refer_user_name;
        this.showCommentForm = true;
      },
      isLoginUserName: function (user_name) {
        return user_name === GetLoginUserName();
      },
      // 删除评论
      deleteComment:async function (level,id,org_parent_id) {
        let params = {
          "level":level,
          "id":id,
          "org_parent_id":org_parent_id
        };
        const result = await deleteComment(params);
        if (result.status === "SUCCESS") {
          this.refreshComment();
          this.$Message.success("删除成功！");
          this.$emit('refreshComment','all');
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

</style>
