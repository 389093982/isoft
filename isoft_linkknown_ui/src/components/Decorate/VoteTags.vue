<template>
  <span>
    <span class="isoft_tag4" @click="updateVoteTag(tag)"
          v-for="(tag, index) in voteTags">{{tag}}（{{parseTagNum(tag)}}）</span>
  </span>
</template>

<script>
  import {FilterVoteTags, UpdateVoteTag} from "../../api"

  export default {
    name: "VoteTags",
    props: {
      referer_type: {
        type: String,
        default: '',
      },
      referer_id: {
        type: String,
        default: '',
      },
      tag_name: {
        type: String,
        default: '',
      }
    },
    data() {
      return {
        voteTags: this.GLOBAL.voteTags,
        vTags: [],
      }
    },
    methods: {
      parseTagNum: function (tag_name) {
        let fTags = this.vTags.filter(tag => tag.tag_name === tag_name);
        return fTags != null && fTags.length > 0 ? fTags[0].vote_number : 0;
      },
      refreshVoteTags: async function () {
        const result = await FilterVoteTags({referer_type: this.referer_type, referer_id: this.referer_id});
        if (result.status === "SUCCESS") {
          this.vTags = result.voteTags;
        }
      },
      updateVoteTag: async function (tag_name) {
        const result = await UpdateVoteTag({
          referer_type: this.referer_type,
          referer_id: this.referer_id,
          tag_name: tag_name
        });
        if (result.status === "SUCCESS") {
          this.$Message.success("投票成功!");
          this.refreshVoteTags();
        } else {
          this.$Message.error(result.errorMsg);
        }
      },
    },
    mounted() {
      this.refreshVoteTags();
    }
  }
</script>

<style scoped>

</style>
