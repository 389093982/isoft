<template>
  <div>
    <a @click="showInfo = true">我的信息</a>
    <Drawer :closable="true" width="50%" v-model="showInfo">
      <Tabs value="name1">
        <TabPane label="我的信息" name="name1">
          <!--内容-->
          <div style="background-color: rgba(176,108,239,0.04);float:left;height: auto;border: 1px #b06cef solid;border-radius: 10px">
            <div class="basicInformation">
              <div>
                <h3><Icon type="ios-paper-outline" />基础信息</h3>
              </div>
              <div style="float: left;width:30%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">安徽·六安</em>
              </div>
              <div style="float: left;width:15%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">170cm</em>
              </div>
              <div style="float: left;width:15%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">单身</em>
              </div>
              <div style="float: left;width:15%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">未婚</em>
              </div>
              <div style="float: left;width:15%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">狮子座</em>
              </div>
              <div style="float: left;width:60%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">蚌埠学院-机械制造与自动化</em>
              </div>
              <div style="float: left;width:60%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">工作地:上海德慧软件有限公司</em>
              </div>
              <div style="float: left;width:30%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">月收入:1-2万</em>
              </div>
              <div style="clear: both"></div>
            </div>

            <div class="personalizedSignature">
              <h3><Icon type="ios-paper-outline" />个性签名</h3>
              <div style="width:80%; height: 25px;margin: 10px 0 0 60px;text-align: left;border-radius: 10px">
                <code style="font-size:15px;">我命由我不由天！但是还是要给老板打工，唉...</code>
              </div>
            </div>

            <div class="professionalSkills">
              <h3><Icon type="ios-paper-outline" />专业技能</h3>
              <div style="width:80%; height: 25px;margin: 10px 0 0 60px;text-align: left;border-radius: 10px">
                <code style="font-size:15px;">spring/jsp/js/html/mysql/oracle等计算机相关的技术</code>
              </div>
            </div>

            <div class="hobby">
              <h3><Icon type="ios-paper-outline" />兴趣爱好</h3>
              <div style="margin: 10px 0 0 60px">
                <div>
                  <code style="font-size:15px;">喜欢的一道菜:未填写</code>
                  <code style="font-size:15px; margin-left: 120px">欣赏的一个名人:未填写</code>
                </div>
                <div style="margin-top: 20px">
                  <code style="font-size:15px;">喜欢的一首歌:未填写</code>
                  <code style="font-size:15px; margin-left: 120px">喜欢的一本书:未填写</code>
                </div>
                <div style="margin-top: 20px">
                  <code style="font-size:15px;">喜欢做的事:未填写</code>
                </div>
              </div>
            </div>

            <div class="habitsAndCustoms">
              <h3><Icon type="ios-paper-outline" />生活习惯</h3>
              <div style="width:80%; height: 25px;margin: 10px 0 0 60px;text-align: left;border-radius: 10px">
                <code style="font-size:15px;">我命由我不由天！！！</code>
              </div>
            </div>

            <div class="dynamics">
              <h3><Icon type="ios-paper-outline" />Ta的动态</h3>
              <div style="width:80%; height: 25px;margin: 10px 0 0 60px;text-align: left;border-radius: 10px">
                <code style="font-size:15px;">最新动态:xxx</code>
              </div>
            </div>

            <div class="attention">
              <h3><Icon type="ios-paper-outline" />Ta的关注</h3>
              <div style="width:80%; height: 25px;margin: 10px 0 0 60px;text-align: left;border-radius: 10px">
                <code style="font-size:15px;">关注对象:xxx</code>
              </div>
            </div>

            <div class="fans">
              <h3><Icon type="ios-paper-outline" />Ta的粉丝</h3>
              <div style="width:80%; height: 25px;margin: 10px 0 0 60px;text-align: left;border-radius: 10px">
                <code style="font-size:15px;">我的粉丝:xxx</code>
              </div>
            </div>

            <div class="loverCondition">
              <h3><Icon type="ios-paper-outline" />择偶条件</h3>
              <div style="float: left;width:20%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">23-37岁</em>
              </div>
              <div style="float: left;width:10%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">未婚</em>
              </div>
              <div style="float: left;width:10%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">安徽</em>
              </div>
              <div style="float: left;width:20%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">170-175cm</em>
              </div>
              <div style="float: left;width:20%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">月薪:5千以上</em>
              </div>
            </div>

            <div style="margin:100px 0 100px 20px ;">
              扫码下载APP随时发布更多动态
              <div style="width: 150px;height: 150px;margin: 20px 0 0 20px; border:2px #ff6900 solid">

              </div>
            </div>

          </div>
        </TabPane>

        <TabPane label="修改信息" name="name2">
          <!--内容-->
          <div style="background-color: rgba(176,108,239,0.04);float:left;height: auto;border: 1px #b06cef solid;border-radius: 10px">
            <div class="basicInformation">
              <div>
                <h3><Icon type="ios-paper-outline" />基础信息</h3>
              </div>
              <div style="float: left;width:30%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">安徽·六安</em>
              </div>
              <div style="float: left;width:15%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">170cm</em>
              </div>
              <div style="float: left;width:15%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">单身</em>
              </div>
              <div style="float: left;width:15%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">未婚</em>
              </div>
              <div style="float: left;width:15%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">狮子座</em>
              </div>
              <div style="float: left;width:60%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">蚌埠学院-机械制造与自动化</em>
              </div>
              <div style="float: left;width:60%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">工作地:上海德慧软件有限公司</em>
              </div>
              <div style="float: left;width:30%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">月收入:1-2万</em>
              </div>
              <div style="clear: both"></div>
            </div>

            <div class="personalizedSignature">
              <h3><Icon type="ios-paper-outline" />个性签名</h3>
              <div style="width:80%; height: 25px;margin: 10px 0 0 60px;text-align: left;border-radius: 10px">
                <code style="font-size:15px;">我命由我不由天！但是还是要给老板打工，唉...</code>
              </div>
            </div>

            <div class="professionalSkills">
              <h3><Icon type="ios-paper-outline" />专业技能</h3>
              <div style="width:80%; height: 25px;margin: 10px 0 0 60px;text-align: left;border-radius: 10px">
                <code style="font-size:15px;">spring/jsp/js/html/mysql/oracle等计算机相关的技术</code>
              </div>
            </div>

            <div class="hobby">
              <h3><Icon type="ios-paper-outline" />兴趣爱好</h3>
              <div style="margin: 10px 0 0 60px">
                <div>
                  <code style="font-size:15px;">喜欢的一道菜:未填写</code>
                  <code style="font-size:15px; margin-left: 120px">欣赏的一个名人:未填写</code>
                </div>
                <div style="margin-top: 20px">
                  <code style="font-size:15px;">喜欢的一首歌:未填写</code>
                  <code style="font-size:15px; margin-left: 120px">喜欢的一本书:未填写</code>
                </div>
                <div style="margin-top: 20px">
                  <code style="font-size:15px;">喜欢做的事:未填写</code>
                </div>
              </div>
            </div>

            <div class="habitsAndCustoms">
              <h3><Icon type="ios-paper-outline" />生活习惯</h3>
              <div style="width:80%; height: 25px;margin: 10px 0 0 60px;text-align: left;border-radius: 10px">
                <code style="font-size:15px;">我命由我不由天！！！</code>
              </div>
            </div>

            <div class="dynamics">
              <h3><Icon type="ios-paper-outline" />Ta的动态</h3>
              <div style="width:80%; height: 25px;margin: 10px 0 0 60px;text-align: left;border-radius: 10px">
                <code style="font-size:15px;">最新动态:xxx</code>
              </div>
            </div>

            <div class="attention">
              <h3><Icon type="ios-paper-outline" />Ta的关注</h3>
              <div style="width:80%; height: 25px;margin: 10px 0 0 60px;text-align: left;border-radius: 10px">
                <code style="font-size:15px;">关注对象:xxx</code>
              </div>
            </div>

            <div class="fans">
              <h3><Icon type="ios-paper-outline" />Ta的粉丝</h3>
              <div style="width:80%; height: 25px;margin: 10px 0 0 60px;text-align: left;border-radius: 10px">
                <code style="font-size:15px;">我的粉丝:xxx</code>
              </div>
            </div>

            <div class="loverCondition">
              <h3><Icon type="ios-paper-outline" />择偶条件</h3>
              <div style="float: left;width:20%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">23-37岁</em>
              </div>
              <div style="float: left;width:10%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">未婚</em>
              </div>
              <div style="float: left;width:10%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">安徽</em>
              </div>
              <div style="float: left;width:20%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">170-175cm</em>
              </div>
              <div style="float: left;width:20%; height: 25px;margin: 10px 0 0 10px ;background-color: #eaebff;text-align: center;border-radius: 10px;border: 1px #b06cef solid">
                <em style="font-size:15px;">月薪:5千以上</em>
              </div>
            </div>

            <div style="margin:100px 0 100px 20px ;">
              扫码下载APP随时发布更多动态
              <div style="width: 150px;height: 150px;margin: 20px 0 0 20px; border:2px #ff6900 solid">

              </div>
            </div>

          </div>
        </TabPane>
      </Tabs>
    </Drawer>
  </div>
</template>

<script>
	export default {
		name: "myinfo",
    props:{
		  showInfo:false,
    },
    data () {
      return {
        pStyle: {
          showInfo:false,
          fontSize: '16px',
          color: 'rgba(0,0,0,0.85)',
          lineHeight: '24px',
          display: 'block',
          marginBottom: '16px'
        }
      }
    }
	}
</script>

<style scoped>
  .demo-drawer-profile .ivu-col{
    margin-bottom: 12px;
  }
  .basicInformation{
    margin: 20px 1% 0 5% ;
  }
  .personalizedSignature{
    margin: 50px 1% 0 5% ;
  }
  .publicAlbum{
    margin: 50px 1% 0 5% ;
  }
  .professionalSkills{
    margin: 50px 1% 0 5% ;
  }
  .hobby{
    margin: 50px 1% 0 5% ;
  }
  .habitsAndCustoms{
    margin: 50px 1% 0 5% ;
  }
  .dynamics{
    margin: 50px 1% 0 5% ;
  }
  .attention{
    margin: 50px 1% 0 5% ;
  }
  .fans{
    margin: 50px 1% 0 5% ;
  }
  .loverCondition{
    margin: 50px 1% 0 5% ;
  }
</style>
