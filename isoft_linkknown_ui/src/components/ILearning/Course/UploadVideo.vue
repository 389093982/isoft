<template>
  <span>
    <Modal
      v-model="showDialog"
      width="700"
      title="上传/更新视频"
      :mask-closable="false"
      :styles="{top: '20px'}">
      <div>
        <p style="padding:10px;">课程名称：{{course.course_name}}</p>
        <p style="background-color: rgba(253,0,0,0.11);padding: 10px;">
          上传需知： ①视频格式为" 序号.名称.mp4 ",  例如： 1.数据库基础.mp4 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;②点击叉号可删除视频！
        </p>

        <Scroll height="280" style="margin: 5px 0;">
          <Tag v-for="(cVideo, index) in cVideos" style="width:98%;height: 40px;line-height: 40px;">
            <span>第{{index + 1}}集: {{cVideo.video_name | filterSuffix}}</span>
            <span style="float: right;">
              <Icon type="md-close" @click="handleDeleteVideo(cVideo.id)" :size="16"/>
              <Icon type="md-arrow-up" v-show="index + 1 > 1"
                    @click="handleChangeVideoOrder(cVideos[index-1].id, cVideos[index].id)" :size="16"/>
              <Icon type="md-arrow-down" v-show="index + 1 < cVideos.length"
                    @click="handleChangeVideoOrder(cVideos[index].id, cVideos[index+1].id)" :size="16"/>
            </span>
          </Tag>
          <Spin fix size="large" v-if="isLoading">
             <div class="isoft_loading"></div>
          </Spin>
        </Scroll>

         <IFileUpload ref="fileUpload" size="small" :auto-hide-modal="true" :multiple="false" :format="['mp4']"
                      @uploadComplete="uploadComplete" :action="fileUploadUrl" uploadLabel="上传视频" :prefix-validate-func="prefixValidateFunc" />
      </div>
    </Modal>

    <a href="javascript:;" style="color:#f55e13;font-family: Arial;font-weight: 700;"
       @click="uploadVideoFunc">上传/更新视频</a>

    <!--暂时只有admin用户才可以上传视频-->
    <i-simple-confirm-modal ref="isSimpleConfirmModal" modalWidth="300" modalTitle="温馨提示:">
      <slot>
        上传视频需联系管理员
      </slot>
    </i-simple-confirm-modal>

  </span>
</template>

<script>
  import IFileUpload from "../../Common/file/IFileUpload";
  import {ChangeVideoOrder, DeleteVideo, fileUploadUrl, ShowCourseDetail, UploadVideo} from "../../../api"
  import {CheckAdminLogin, handleSpecial} from "../../../tools";
  import ISimpleConfirmModal from "../../Common/modal/ISimpleConfirmModal";

  export default {
    name: "UploadVideo",
    components: {ISimpleConfirmModal, IFileUpload},
    // 当前需要上传视频的课程
    props: ["course"],
    data() {
      return {
        fileUploadUrl: fileUploadUrl + "?table_name=course_video&table_field=first_play",
        isLoading: true,
        showDialog: false,
        cVideos: [],
      }
    },
    methods: {
      handleChangeVideoOrder: async function (id, id2) {
        const result = await ChangeVideoOrder({id: id, id2: id2});
        if (result.status === "SUCCESS") {
          this.$Message.success("操作成功!");
          this.refreshCourseDetail(this.course.id);
        } else {
          this.$Message.error("操作失败!");
        }
      },
      handleDeleteVideo: async function (id) {
        const result = await DeleteVideo({course_id: this.course.id, id: id});
        if (result.status === "SUCCESS") {
          this.$Message.success("删除成功!");
          this.refreshCourseDetail(this.course.id);
        } else {
          this.$Message.error("删除失败!");
        }
      },
      uploadComplete: async function (data) {
        if (data.status === "SUCCESS") {
          let uploadFilePath = data.fileServerPath;     // uploadFilePath 使用 hash 值时含有特殊字符 + 等需要转义
          let courseId = this.course.id;
          let filename = data.fileName;      // 上传文件名称
          const result = await UploadVideo({
            id: courseId,
            video_name: filename,
            video_path: handleSpecial(uploadFilePath),
            duration: data.duration,
          });
          if (result.status === "SUCCESS") {
            this.refreshCourseDetail(courseId);
            this.$emit('uploadComplete');
          } else {
            this.$Message.error("视频更新失败!");
          }
        }
      },
      refreshCourseDetail: async function (course_id) {
        this.isLoading = true;
        try {
          const result = await ShowCourseDetail({course_id});
          if (result.status === "SUCCESS") {
            this.cVideos = result.cVideos;
          }
        } finally {
          this.isLoading = false;
        }
      },
      uploadVideoFunc: function () {
        if (this.isAdmin()) {
          this.showDialog = true;
          this.refreshCourseDetail(this.course.id);
        }else {
          this.$refs.isSimpleConfirmModal.showModal();
        }
      },
      isAdmin: function () {
        return CheckAdminLogin();
      },
      prefixValidateFunc:function (value) {
        let ele = value.split(".");
        if (ele.length === 3 && !isNaN(ele[0]) && ele[2]==='mp4') {
          return true;
        }else {
          return false;
        }
      }
    },
    filters: {
      filterSuffix: function (value) {
        // 去除视频文件后缀
        return value.slice(value.indexOf(".")+1,value.lastIndexOf("."));
      },
    },
  }
</script>

<style scoped>
  @import "../../../assets/css/isoft_common.css";

</style>
