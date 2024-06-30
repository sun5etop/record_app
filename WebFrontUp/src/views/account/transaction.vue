<template>
  <div class="index">
    <el-container>
      <el-header><header-v></header-v></el-header>
      <!--查找框-->
      <span class="mt-4">

  </span>
    </el-container>

  </div>

  <el-input
      v-model="input"
      style="max-width: 400px"
      placeholder="请输入交易Id"
      class="input"
  >
    <template #append>
      <el-button @click="buy(input)">购买</el-button>
    </template>
  </el-input>

<!--标签-->
  <div class="flex gap-2">
    <el-tag
        v-for="item in labelItems"
        :key="item.label"
        :type="item.type"
        effect="dark"
    >
      {{ item.label }}
    </el-tag>
  </div>

<!--藏品矩阵-->
  <div class="matrix-container">
    <span class="matrix">
      <el-card
          v-for="(item, index) in items"
          :key="index"
          style="max-width: 280px"
          shadow="hover"
      >
        <template #header>
          <div class="header-container">
            <h1>唱片发行编号： {{item.tranRecordId}}<br>
              <el-tag type="success">AccountID: {{item.fromAccountId}}</el-tag>&nbsp;
              <el-tag type="warning">TIME: 2024-06-28</el-tag>
            </h1>
          </div>
        </template>‘

        <img
            style="width: 100%"
            :src=imag
        />
        <template #footer>
            <div class="footer-container" style="display: flex; justify-content: flex-end; align-items: center;">
              <el-tooltip :content=item.info effect="dark">
                <el-button style="color: white;" color="#529b2e">详情信息</el-button>
              </el-tooltip>
              <el-button type="primary" @click="publish(item.tranRecordId,item.tranId)">上架</el-button>

            </div>
        </template>
      </el-card>
    </span>
  </div>
</template>

<script>
import { ref } from 'vue';
import HeaderV from "@/components/HeaderV.vue";
import request from "@/utils/request.js";

export default {
  data() {
    const items = ref([]);
    const TransForm =ref({
      fromAccountId: '',
      tranRecordId:'',
    });
    const publishTransForm=ref({
      fromAccountId: '',
      toAccountId:'',
      tranRecordId:'',
      tranId:'',
    });
    const buyForm =ref({
      toAccountId: '',
      tranId:'',
    });
    //const imag ='https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png';
    return {
      items,
      records:[],
      TransForm,
      publishTransForm,
      input:'',
      buyForm
    };
  },
  methods:{
    buy(input,account_id){
      this.buyForm.tranId=input;
      this.buyForm.accountId=localStorage.getItem('accountId');
      request.post('/transaction/acceptTransaction',this.buyForm).then((res)=>{
        if(res.code==200){
          this.$message({
            type:"success",
            message:"保存交易成功！"
          })
          console.log("保存交易成功！");
        }
      });
    },

    addItem(newItem) {
      // this.items.value = [...this.items.value, newItem];
      this.items.push(newItem);
    },
    getMyTransaction(){
      request.get('/transaction/getMyTransaction').then((res)=>{
        console.log(res);
        this.records =res.data;
        for(let i=0;i<res.data.length;i++){
          const newItem = { tranId: res.data[i].tranId, fromAccountId:res.data[i].fromAccountId, toAccountId:res.data[i].toAccountId, tranRecordId:res.data[i].tranRecordId, image: 'https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png' };
          this.addItem(newItem);
        }

      });
    },
    publish(recordId,tranId){
      // request.post('/transaction/getTransactionByRecordId',recordId). then((res)=>{
      //   console.log(res);
      //   this.publishTransForm.tranId=res.data.tranId;
      //   this.publishTransForm.tranRecordId=recordId;
      //   this.publishTransForm.fromAccountId=res.data.fromAccountId;
      //   this.publishTransForm.toAccountId=res.data.toAccountId;
      // });

      this.publishTransForm.fromAccountId=localStorage.getItem("accountId");
      this.publishTransForm.tranRecordId=recordId;
      this.publishTransForm.tranId=tranId;
      request.post('/transaction/publishTransaction',this.publishTransForm).then((res) =>{
        if(res.code==200){
          this.$message({
            type:"success",
            message:"保存交易成功！"
          })
          console.log("保存交易成功！");
        }
      });
    }


  },
  components: {
    HeaderV
  },
  mounted() {
    this.getMyTransaction();
  },



};
</script>

<style scoped>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.matrix-container {
  //display: flex;
  margin-left: 20px;
  margin-top: 20px;
  flex-wrap: wrap;
  justify-content: center;
}


.matrix {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}
.input{
  margin-left: 20px;
  margin-top: 20px;
}


</style>