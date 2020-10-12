<template>
  <div class="xuanchuan_box">
    <div>
      <img src="../../../static/images/index/text_box.png"/>
      <img src="../../../static/images/index/text_box.png"/>
      <img src="../../../static/images/index/text_box.png"/>
      <img src="../../../static/images/index/text_box.png"/>
    </div>
    <div>
      <div>
        <div class="circle"></div>
      </div>
    </div>
    <div>
      <img src="../../../static/images/index/text_box.png"/>
      <img src="../../../static/images/index/text_box.png"/>
      <img src="../../../static/images/index/text_box.png"/>
      <img src="../../../static/images/index/text_box.png"/>
    </div>
  </div>
</template>
AAAA
<script>
  export default {
    name: "XuanChuan"
  }
</script>

<style scoped>
  .xuanchuan_box {
    width: 90%;
    margin: 50px auto;
  }
  .xuanchuan_box > div {
    width: 30%;
    height: 380px;
    display: inline-block;
    vertical-align: top;
  }
  .xuanchuan_box > div:nth-child(1) {
    width: 38%;
  }
  .xuanchuan_box > div:first-child {
    padding-left: 10%;
  }
  .xuanchuan_box > div:last-child {
    padding-right: 10%;
  }
  .xuanchuan_box > div:first-child img,.xuanchuan_box > div:last-child img {
    display: block;
  }
  .xuanchuan_box > div:first-child img:nth-child(1), .xuanchuan_box > div:first-child img:nth-child(4) {
    transform: translateX(100px);
  }

  .xuanchuan_box > div:last-child img:nth-child(1), .xuanchuan_box > div:last-child img:nth-child(4) {
    transform: translateX(-100px);
  }
  .xuanchuan_box > div:nth-child(2) .circle {
    width: 200px;
    height: 200px;
    border-radius: 50%;
    background-color: #b882ff;
    margin-top: 100px;
  }
</style>
