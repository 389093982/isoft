<template>
  <div style="background: #FFFFFF;padding: 10px;">
    <Row style="background: #FFFFFF;">
      <Col span="6" style="padding: 5px 0 0 10px ;background: #f8f8f9;">
        <IBeautifulCard title="用户信息设置">
          <div slot="content">
            <p style="padding: 5px 5px 5px 20px;">
              <IBeautifulLink @onclick="$router.push({path:'/user/set/userInfo'})">基础信息修改</IBeautifulLink>
            </p>
            <p style="padding: 5px 5px 5px 20px;">
              <IBeautifulLink @onclick="$router.push({ path: '/sso/forget',query:{pattern:2}})">密码修改</IBeautifulLink>
            </p>
          </div>
        </IBeautifulCard>
      </Col>
      <Col span="18">
        <div style="padding: 2px;background: #f8f8f9;margin-left: 10px;">
          <div style="padding: 10px;background: #FFFFFF;min-height: 500px;">
            <router-view/>
          </div>
        </div>
      </Col>
    </Row>

  </div>
</template>

<script>
  import IBeautifulLink from "../../Common/link/IBeautifulLink";
  import IBeautifulCard from "../../Common/card/IBeautifulCard";
  import ISimpleConfirmModal from "../../Common/modal/ISimpleConfirmModal";
  import {CheckAdminLogin} from "../../../tools/index"

  export default {
    name: "UserInfoSet",
    components: {ISimpleConfirmModal, IBeautifulCard, IBeautifulLink},
    data (){
      return {

      }
    },
    methods: {
      openCourse:function () {
        if (this.isAdmin()) {
          this.$router.push({path:'/ilearning/courseSpace/editCourse'})
        }else {
          this.$refs.isSimpleConfirmModal.showModal();
        }
      },
      isAdmin: function () {
        return CheckAdminLogin();
      },
    }
  }
</script>

<style scoped>

</style>
