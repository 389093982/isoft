<template>
  <div style="background-color: white;min-height: 600px;">

    <Row>
      <Col span="7">&nbsp;</Col>
      <Col span="10">
        <div style="min-height: 420px">
          <!--用于数据展示-->
          <div v-if="attentionDatas">
            <div style="text-align: center;font-size: 15px;position: relative;top: 10px;">我的关注</div>
            <div v-for="(user,index) in attentionDatas" style="position: relative;top: 20px;">
              <Row>
                <Col span="2">
                  <span @click="$router.push({path:'/user/userDetail',query:{username:user.user_name}})">
                    <HatAndFacePicture :src="user.small_icon" :vip_level="user.vip_level" :hat_in_use="user.hat_in_use" :src_size="30"
                                       :hat_width="30" :hat_height="10" :hat_relative_left="0" :hat_relative_top="-46" ></HatAndFacePicture>
                  </span>
                </Col>
                <Col span="10">
                  <div style="position: relative;top:-4px">
                    <div style="font-size: 10px">{{user.nick_name}}</div>
                    <span v-if="user.gender==='male'" style="position: relative;top: -8px;"><Icon type="md-male" style="color: #0099ff"/></span>
                    <span v-else-if="user.gender==='female'" style="position: relative;top: -8px;"><Icon type="md-female" style="color: #ff0000"/></span>
                  </div>
                </Col>
                <Col span="2"><span style="font-size: 10px">关注</span>:{{user.attention_counts}}</Col>
                <Col span="2"><span style="font-size: 10px">粉丝</span>:{{user.fensi_counts}}</Col>
                <Col span="8"><span style="font-size: 10px">关注日期:&nbsp;</span><span style="font-size: 12px">{{formatAttentionTime(user.attention_time)}}</span></Col>
              </Row>
            </div>
          </div>

          <div v-if="fensiDatas">
            <div style="text-align: center;font-size: 15px;position: relative;top: 10px;">我的粉丝</div>
            <div v-for="(user,index) in fensiDatas" style="position: relative;top: 20px;">
              <Row>
                <Col span="2">
                  <span @click="$router.push({path:'/user/userDetail',query:{username:user.user_name}})">
                    <HatAndFacePicture :src="user.small_icon" :vip_level="user.vip_level" :hat_in_use="user.hat_in_use" :src_size="30"
                                       :hat_width="30" :hat_height="10" :hat_relative_left="0" :hat_relative_top="-46" ></HatAndFacePicture>
                  </span>
                </Col>
                <Col span="10">
                  <div style="position: relative;top:-4px">
                    <div style="font-size: 10px">{{user.nick_name}}</div>
                    <span v-if="user.gender==='male'" style="position: relative;top: -8px;"><Icon type="md-male" style="color: #0099ff"/></span>
                    <span v-else-if="user.gender==='female'" style="position: relative;top: -8px;"><Icon type="md-female" style="color: #ff0000"/></span>
                  </div>
                </Col>
                <Col span="2"><span style="font-size: 10px">关注</span>:{{user.attention_counts}}</Col>
                <Col span="2"><span style="font-size: 10px">粉丝</span>:{{user.fensi_counts}}</Col>
                <Col span="8"><span style="font-size: 10px">关注日期:&nbsp;</span><span style="font-size: 12px">{{formatAttentionTime(user.attention_time)}}</span></Col>
              </Row>
            </div>
          </div>
        </div>

        <!--分页-->
        <div style="text-align: center;position: relative;top: 30px;">
          <Page :total="page.totalCount" :page-size="page.offset" :current="page.currentPage" show-total show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"/>
        </div>


      </Col>
      <Col span="7">&nbsp;</Col>
    </Row>

  </div>
</template>

<script>
	import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";
	import {QueryAttentionOrFensi} from "../../api/index"
  import {formatUTCtime} from "../../tools/index"
  export default {
		name: "UserAttentionOrFensi",
    components: {HatAndFacePicture},
    data() {
      return {
        AttentionOrFensi:'',
        page:{totalCount:0,currentPage:1,offset:10},
        attentionDatas: [],
        fensiDatas: [],
      }
    },
    methods:{
      refreshAttentionOrFensi: async function () {
        let params = {
          'attention_object_type':'user',
          'AttentionOrFensi':this.AttentionOrFensi,
          'current_page':this.page.currentPage,
          'offset':this.page.offset
        };
        const result = await QueryAttentionOrFensi(params);
        if (result.status === 'SUCCESS') {
          if (this.AttentionOrFensi === 'Attention') {
            this.attentionDatas = result.queryDatas;
            this.fensiDatas = "";
          }else if (this.AttentionOrFensi === 'Fensi') {
            this.fensiDatas = result.queryDatas;
            this.attentionDatas = "";
          }

        }
      },
      formatAttentionTime:function(time){
        return formatUTCtime(time,'yyyy-MM-dd');
      },
      pageChange:function (page) {
        this.page.currentPage = page;
        this.refreshAttentionOrFensi()
      },
      pageSizeChange:function (pageSize) {
        this.page.offset = pageSize;
        this.refreshAttentionOrFensi()
      },
    },
    mounted(){
      this.AttentionOrFensi = this.$route.query.AttentionOrFensi;
      this.refreshAttentionOrFensi();
    }
	}
</script>

<style scoped>

</style>
