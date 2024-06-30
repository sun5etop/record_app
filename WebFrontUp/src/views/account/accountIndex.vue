<template>
  <div class="index">
    <el-container>
      <el-header><HeaderV></HeaderV></el-header>
    </el-container>
  </div>
  <div>
<!--    <el-button @click="showRecord">点击掉落</el-button>-->
    <Modal
        :show-modal="isModalVisible"
        :modal-title="modalTitle"
        :image-url="imageUrl"
        @close="closeModal"
    />
<!--    <Modal :imageUrl="imageSrc" v-if="isModalVisible" @close="closeModal">-->
<!--      &lt;!&ndash; 可选的其他插槽内容 &ndash;&gt;-->
<!--    </Modal>-->
  </div>
  <div class="recordPicture">
    <img  class="Img" src="../../assets/record.jpg" @click="dropRecords" alt="点击掉落"/>
  </div>
</template>

<script>
import Modal from '../../components/Modal.vue'
import { ref } from 'vue';
import request from '../../utils/request'
import HeaderV from '../../components/HeaderV.vue';

export default {
  data(){
    // var imgUrl;
    return{
      isModalVisible: false,
      modalTitle: '掉落唱片！',
      // modalText: '金色传说！',
      // imageUrl: new URL('../assets/backup.jpg',import.meta.url).href,
      imageUrl: '',
    }
  },
  components: {
    HeaderV,
    Modal,
  },
  methods:{
    dropRecords() {
      console.log('点击了图片，调用概率函数，是否能够实现掉落呢！');
      request.get('/account/halfRandomDrop').then((res)=>{
        // 201未成功
        if(res.code!=201){
          this.$message({
            type:"success",
            message:res.msg,
          })
          localStorage.setItem('recordType', res.data);
          const recordType = localStorage.getItem('recordType');
          if(recordType==1)
          {
            this.imageUrl = "../../public/1.png";
          }
          else if(recordType==2)
          {
            this.imageUrl = "../../public/2.png";
          }
          else {
            this.imageUrl = "../../public/3.png";
          }
          this.showRecord();

        }
        else {
          this.$message({
            type:"warning",
            message:"what a pity！未掉落！",
          })
        }
      })
      // 在这里调用你的函数或者执行你的逻辑
      // this.myFunction();
    },

    showRecord() {
      // console.log('hh')
      this.isModalVisible = true;
    },

    closeModal() {
      this.isModalVisible = false;
    }



  },

  setup() {

    const loginForm = ref({
      accountId: '',
    });
    return {
      loginForm,
    };
  }
}


</script>

<style scoped>
  .index {
    background-color: #302d2d;
  }

  .Img{
    max-width: 100%; /* 确保容器宽度铺满父元素 */
    max-height: 60vh; /* 确保容器高度铺满视口 */
  }


  .recordPicture {
    background-color: #1e1c1c;
    display: flex;
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */
    width: 100%; /* 确保容器宽度铺满父元素 */
    height: 100vh; /* 确保容器高度铺满视口 */
  }


</style>