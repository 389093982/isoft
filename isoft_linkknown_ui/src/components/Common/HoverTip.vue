<template>
  <span>
    <div class="box" style="display: inline-block;" @click="showBusiness = true">
      <span style="position: relative;">
        <img style="width: 120px;height: 150px;position: absolute;top: -90px;left: -20px;" src="../../../static/images/common_img/logo01.gif"/>
        <!--<img style="width: 120px;height: 150px;position: absolute;top: -90px;left: -20px;" src="../../../static/images/logo02.gif"/>-->
      </span>
      <div style="margin: 15px 5px 5px 50px;color: white;text-align: center;" class="isoft_font16">
        <p>创业必备</p>
        <p>点击我了解</p>
        <div style="position: relative;">
          <img style="position: absolute;right: 10px;top: -33px;width: 10px;height: 17px;" src="../../../static/images/common_img/right.png"/>
        </div>
      </div>
    </div>

    <div v-if="showBusiness" class="isoft_shade"></div>

    <div class="center_box" v-if="showBusiness" style="display: flex;">
      <span class="isoft_point_cursor isoft_close" @click="showBusiness = false"><Icon type="md-close" size="20"/></span>
      <div class="to_zixun_box" style="position: relative;">
        <img src="../../../static/images/common_img/jianzhi.png" style="width: 280px;height: 450px;"/>
        <div class="to_zixun" @click="contactAdmin">马上咨询管理员</div>
      </div>
      <div style="width: 100%;padding: 10px 0 10px 20px;display: flex;">
        <div style="width: 60%;padding: 15px 10px;">
          <p class="question_tip">创业太难怎么办？</p>
          <p class="question_tip">创业成本太高怎么办？</p>
          <p class="question_tip">创业没有途径怎么办？</p>
          <p class="question_tip">离职没工资怎么办？</p>
          <p class="answer_tip">在链知网总有&nbsp;“薪”&nbsp;发现</p>

          <Form ref="formValidate" :model="formValidate" :rules="ruleValidate">
            <FormItem label="1.您的技术整体水平:" prop="userLevel">
                <RadioGroup v-model="formValidate.userLevel">
                    <Radio label="primary">初级</Radio>
                    <Radio label="intermediate">中级</Radio>
                    <Radio label="senior">高级</Radio>
                </RadioGroup>
            </FormItem>
            <FormItem label="2.您所擅长的方向:" prop="userGoodAt">
                <RadioGroup v-model="formValidate.userGoodAt">
                    <Radio label="frontUI">前端UI</Radio>
                    <Radio label="backDesigner">后端设计</Radio>
                    <Radio label="requirementAnalysis">需求分析</Radio>
                </RadioGroup>
            </FormItem>
            <FormItem label="3.您期望的资源:" prop="userExpect">
                <RadioGroup v-model="formValidate.userExpect">
                    <Radio label="videoTutorial">视频教程</Radio>
                    <Radio label="cooperationWithLinkknown">与链知网合作</Radio>
                    <Radio label="otherResource">其他资源</Radio>
                </RadioGroup>
            </FormItem>
          </Form>

          <div class="isoft_button_blue" style="width: 60%;margin-left: 10%;margin-top: 20px;" @click="handleSubmit('formValidate')">了解一下</div>
        </div>
        <div style="width: 40%;border-left: 1px dashed #c5c5c5;padding: 15px 10px;">
          <p style="color: #f60;font-size: 18px;">90%的合作用户</p>
          <p style="font-size: 18px;">通过链知网获得了兼职收入</p>

          <div v-for="(icon, index) in icons" style="margin: 15px 0;display: flex;">
            <div>
              <img :src="'../../../static/images/common_img/' + icon.icon_name"/>
            </div>
            <div style="margin-left: 25px;">
              <p style="font-size: 14px;font-weight: 500;color: #555;line-height: 22px;margin-bottom: 10px;">{{icon.icon_label}}</p>
              <p style="font-size: 12px;font-weight: 400;color: #888;line-height: 14px;">{{icon.icon_label2}}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </span>
</template>

<script>
  export default {
    name: "HoverTip",
    data (){
      return {
        showBusiness: false,
        icons: [
          {icon_name: 'icon_pin.jpg', icon_label: '品质', icon_label2: '原生态视频教程'},
          {icon_name: 'icon_bao.jpg', icon_label: '保证', icon_label2: '保证资源的质量'},
          {icon_name: 'icon_you.jpg', icon_label: '优秀', icon_label2: '优秀的学习资源'},
          {icon_name: 'icon_quan.jpg', icon_label: '全新', icon_label2: '全新的技术方案'},
        ],
        formValidate: {
          userLevel: '',
          userGoodAt: '',
          userExpect: '',
        },
        ruleValidate: {
          userLevel: [
            { required: true, message: '必选项!', trigger: 'change' }
          ],
          userGoodAt: [
            { required: true, message: '必选项!', trigger: 'change' }
          ],
          userExpect: [
            { required: true, message: '必选项!', trigger: 'change' }
          ]
        }
      }
    },
    methods:{
      contactAdmin:function () {
        this.showBusiness = false;
        this.$router.push({path:'/contact/contactList'})
      },
      handleSubmit (name) {
        this.$refs[name].validate((valid) => {
          if (valid) {
            this.toSeeIt();
          } else {
            this.$Message.error('调研问题必填哦');
          }
        })
      },
      toSeeIt:function () {
        this.showBusiness = false;
        this.$router.push({path:'/ilearning/about'})
      },
    }
  }
</script>

<style scoped>
  .box {
    width: 190px;
    height: 74px;
    position: fixed;
    bottom: 80px;
    background: rgba(0,0,0,.75);
    border-top-right-radius: 37px;
    border-bottom-right-radius: 37px;
    cursor: pointer;
    z-index: 100;
    zoom: .8;
    left: -158px;
    transition: left 1s ease;
  }
  .box:hover {
    left: 0;
  }
  .center_box {
    z-index: 1025;
    position: fixed;
    top: 60px;
    left: 10%;
    right: 10%;
    padding: 18px 20px;
    background: rgba(248, 248, 248, 1);
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.16);
    border-radius: 6px;
  }

  .isoft_close {
    position: absolute;
    right: 15px;
    top: 15px;
    padding: 2px;
    color: #959595;
    background: rgba(248, 248, 248, 1);
    border: 1px solid #959595;
    border-radius: 50%;
    transition: transform 0.5s ease-in;
  }
  .isoft_close:hover {
    color: #000000;
    border: 1px solid #000000;
    transform: rotateZ(360deg);
  }

  .to_zixun{
    position: absolute;
    bottom: 100px;
    background-color: #dfd684;
    padding: 10px 20px;
    text-align: center;
    border-radius: 5px;
    font-size: 16px;
    color: white;
    left: 10%;
    right: 10%;
    cursor: pointer;
    display: none;
  }
  .to_zixun_box:hover .to_zixun{
    display: block;
  }

  .question_tip {
    font-size: 16px;
    color: green;
    height: 30px;
    line-height: 30px;
  }
  .answer_tip {
    font-size: 16px;
    color: #e80000;
    height: 30px;
    line-height: 30px;
  }
</style>
