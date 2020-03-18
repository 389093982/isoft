<template>
  <div class="box_bg">
    <div class="box_area">
      <slot name="header">
        <div class="title" :class="titleColorClass">
          <span class="leftHeaderLeft" style="float: left;">
            <slot name="header_left">{{title}}</slot>
          </span>
          <span style="float: right;">
            <slot name="header_right"></slot>
          </span>

        </div>
      </slot>
      <slot name="content"></slot>
    </div>
  </div>
</template>

<script>
  import {oneOf} from "../../../tools";

  export default {
    name: "IBeautifulCard",
    props: {
      title: {
        type: String,
        default: '默认标题',
      },
      theme: {
        type: String,
        default: 'default',
        validator(value) {
          return oneOf(value, ['default', 'blue', 'green', 'orange', 'purple']);
        },
      },
    },
    computed: {
      titleColorClass() {
        return [this.theme + 'Theme'];
      }
    }
  }
</script>

<style scoped>
  .box_bg {
    padding: 2px;
    margin: 2px;
  }

  .box_area {
    background-color: #ffffff;
  }

  .title {
    height: 40px;
    line-height: 40px;
    padding: 0 15px;
    font-size: 16px;
    text-align: left;
  }

  .defaultTheme {
    background: linear-gradient(to right, rgba(255, 0, 0, 0.17), rgba(255, 0, 0, 0.03));
  }

  .blueTheme {
    background: linear-gradient(to right, rgba(17, 27, 255, 0.44), rgba(132, 146, 255, 0.1));
  }

  .greenTheme {
    background: linear-gradient(to right, rgba(17, 255, 10, 0.43), rgba(177, 255, 109, 0.16));
  }

  .orangeTheme {
    background: linear-gradient(to right, rgba(255, 115, 14, 0.43), rgba(255, 199, 143, 0.11));
  }

  .purpleTheme {
    background: linear-gradient(to right, rgba(144, 26, 255, 0.39), rgba(200, 165, 255, 0.11));
  }

  .leftHeaderLeft {
    cursor: pointer;
    transition: all 2s ease-in;
  }

  .leftHeaderLeft:hover {
    color: #00b500;
  }
</style>
