<template>
  <div class="modal" v-if="showModal">
    <div class="modal-content">
      <span class="close" @click="closeModal">&times;</span>
      <h2 class="windowTitle">{{ modalTitle }}</h2>
      <div class="img_container">
        <img class="img" :src="imageUrl" alt="显示的图片">
      </div>
<!--      <p class="goldMyth">{{ modalText }}</p>-->
    </div>
  </div>
<!--  <div class="modal" v-if="showModal">-->
<!--    <el-container class="modal-content">-->
<!--      <span class="close" @click="closeModal">&times;</span>-->
<!--      <el-header class="windowTitle"><h2>{{ modalTitle }}</h2></el-header>-->
<!--      <el-main>-->
<!--        <img class="img" :src="imageUrl" alt="显示图片">-->
<!--      </el-main>-->
<!--    </el-container>-->
<!--  </div>-->
</template>

<script>
export default {
  props: {
    showModal: {
      type: Boolean,
      default: false
    },
    modalTitle: String,
    modalText: String,
    imageUrl: String
  },
  methods: {
    closeModal() {
      this.$emit('close');
      console.log(this.imageUrl)
    }
  }
};
</script>

<style scoped>
.modal {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  z-index: 999; /* 确保弹窗在最上层 */
}

.img_container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.img {
  max-width: 50%;
  max-height: 50%;
  object-fit: contain; /* 保持图片的宽高比，完整显示在容器内 */
}

.windowTitle {
  text-align: center;
}

.modal-content {
  background-color: #fefefe;
  padding: 20px;
  border: 1px solid #888;

  max-width: 40%; /* 弹窗最大宽度 */
  max-height: 80%; /* 弹窗最大高度 */
  overflow: auto; /* 如果内容超过弹窗高度，添加滚动条 */
  position: relative; /* 确保弹窗内容不会溢出 */
  //justify-content: center;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}
</style>
