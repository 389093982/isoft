<template>
  <div style="background: #FFFFFF;padding: 10px;">
    <Row style="background: #FFFFFF;">
      <Col span="6" style="padding: 5px 0 0 10px ;background: #f8f8f9;">
        <IBeautifulCard title="我的课程空间">
          <div slot="content">
            <p style="padding: 5px 5px 5px 20px;">
              <IBeautifulLink @onclick="openCourse()">我要开课
              </IBeautifulLink>
            </p>
            <p style="padding: 5px 5px 5px 20px;">
              <IBeautifulLink @onclick="$router.push({path:'/ilearning/courseSpace/myCourseList'})">我的课程
              </IBeautifulLink>
            </p>
            <p style="padding: 5px 5px 5px 20px;">
              <IBeautifulLink @onclick="$router.push({path:'/ilearning/courseSpace/recentlyViewed'})">最近浏览
              </IBeautifulLink>
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

    <!--暂时只有admin用户才可以开课-->
    <i-simple-confirm-modal ref="isSimpleConfirmModal" modalWidth="300" modalTitle="温馨提示:">
      <slot>
        开课需联系管理员
      </slot>
    </i-simple-confirm-modal>

  </div>
</template>

<script>
  import IBeautifulLink from "../../Common/link/IBeautifulLink";
  import IBeautifulCard from "../../Common/card/IBeautifulCard";
  import RecentlyViewed from "./RecentlyViewed";
  import ISimpleConfirmModal from "../../Common/modal/ISimpleConfirmModal";
  import {CheckAdminLogin} from "../../../tools/index"

  export default {
    name: "CourseSpace",
    components: {ISimpleConfirmModal, RecentlyViewed, IBeautifulCard, IBeautifulLink},
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
