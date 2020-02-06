<template>
  <div>
    <a @click="showInfo = true" style="color: #ff537c">女友消息</a>
    <Drawer :closable="true" width="43%" v-model="showInfo">
      <Tabs value="name1">
        <TabPane label="女友小倩" name="name1">
          <!--外边框-->
          <div style="background-color: rgba(255,83,124,0.67);float:left;width: 50%;height: 500px;border: 1px #b06cef solid;border-radius: 10px;padding: 3px 0 0 4px">
            <!--内边框-->
            <div style="background-color: white;float:left;width: 98%;height: 490px;border: 1px #b06cef solid;border-radius: 10px;">
              <div style="background-color: rgba(255,189,111,0.16);float:left;width: 100%;height: 320px;border-top-left-radius: 10px;border-top-right-radius: 10px;">
                聊天记录
              </div>
              <hr>
              <div style="background-color: rgba(176,108,239,0.1);float:left;width: 100%;height: 170px;border-bottom-left-radius: 10px;border-bottom-right-radius: 10px;">
                <div style="width: 100%;height: 135px;padding: 10px 10px 0 10px ">
                  发送信息 发送信息
                  发送信息 发送信息
                  发送信息 发送信息
                  发送信息 发送信息
                </div>
                <div style="width: auto;height: auto;margin: 0 0 0 160px ">
                  <Button type="warning">Enter发送</Button>
                </div>
              </div>rt
            </div>
          </div>
          <div style="clear: both"></div>
        </TabPane>
      </Tabs>
    </Drawer>
  </div>
</template>

<script>
	export default {
		name: "loverMessage",
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

</style>
