<template>
  <div>
    <Table highlight-row :columns="activityColumns" :data="activityDatas"></Table>
  </div>
</template>
<script>
  export default {
    name: "SearchActivity",
    data () {
      return {
        activityColumns: [
          {
            title: '活动ID',
            key: 'activity_id'
          },
          {
            title: '活动类型',
            key: 'activity_type'
          },
          {
            title: '数量',
            key: 'type_entity_account'
          },
          {
            title: '举办方',
            key: 'organizer'
          },
          {
            title: '活动开始日期',
            key: 'start_date'
          },
          {
            title: '活动结束日期',
            key: 'end_date'
          },
          {
            title: '创建人',
            key: 'created_by'
          },
          {
            title: '创建时间',
            key: 'created_time'
          },
        ],
        activityDatas: [
          {
            activity_id: 'John Brown',
            activity_type: 18,
          },
        ]
      }
    },
    methods: {

    }
  }
</script>

<style scoped>

</style>
