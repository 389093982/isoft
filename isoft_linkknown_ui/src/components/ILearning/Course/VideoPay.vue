<template>
  <div>
    <Row :gutter="10">
      <Col span="16">
        <div style="margin: 0 auto;">
          <video ref="video" width="100%" height="100%" controls preload="auto" id="videoPath" autoplay="autoplay"
                 controlslist="nodownload">
            <source type="video/mp4">
            <source type="video/ogg">
            您的浏览器不支持Video标签。
          </video>
        </div>
      </Col>
      <Col span="8">
        AAAAAAAAAAAAAAA
      </Col>
    </Row>
  </div>
</template>

<script>
  import {videoPlayUrl} from "../../../api"

  export default {
    name: "VideoPay",
    data() {
      return {}
    },
    methods: {
      playVideo: function () {
        var xhr = new XMLHttpRequest();                                                     //创建XMLHttpRequest对象
        xhr.open('GET', videoPlayUrl + "?video_id=" + this.$route.query.video_id, true);    //配置请求方式、请求地址以及是否同步
        xhr.responseType = 'blob';                                                          //设置结果类型为blob;
        xhr.onload = function (e) {
          if (this.status === 200) {
            // 获取blob对象
            var blob = this.response;
            // 获取blob对象地址，并把值赋给容器
            $("#videoPath").attr("src", URL.createObjectURL(blob));
          }
        };
        xhr.send();
      },
    },
    mounted: function () {
      this.playVideo();
    }
  }
</script>

<style scoped>

</style>
