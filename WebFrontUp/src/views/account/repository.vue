<template>
  <div class="index">
    <el-container>
      <el-header><header-v></header-v></el-header>
    </el-container>
  </div>

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
<!--            <h1>{{item.name}}<br>-->
              <el-tag type="success">ID: {{item.id}}</el-tag>&nbsp;
              <el-tag type="warning">TIME: {{item.time}}</el-tag>
<!--            </h1>-->
          </div>
        </template>
        <img
            style="width: 100%"
            :src="item.image"
        />
        <template #footer>
            <div class="footer-container" style="display: flex; justify-content: flex-end; align-items: center;">
              <el-tooltip :content=item.info effect="dark">
                <el-button style="color: white;" color="#529b2e">详情信息</el-button>
              </el-tooltip>
              <el-button type="primary" @click="sell(item.id)">出售</el-button>

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
    const imag ='https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png';
    return {
      items,
      records:[],
      TransForm
    };
  },
  methods:{
    addItem(newItem) {
      // this.items.value = [...this.items.value, newItem];
      this.items.push(newItem);
    },
    getAccountAllRecords(){
      request.get('/account/getAccountAllRecords').then((res)=>{
        console.log(res);
        this.records =res.data;
        for(let i=0;i<res.data.length;i++){
          localStorage.setItem('recordType', res.data[i].recordType);
          const recordType = localStorage.getItem('recordType');
          console.log(recordType);
          if(recordType==1){
            console.log("halo");
            const newItem = {
              name: res.data[i].recordType,
              id: res.data[i].recordId,
              time: res.data[i].publishTime,
              info: '新添加的',
              image: '../../../public/1.png'
            };
            this.addItem(newItem);
          }
          if(recordType==2){
            console.log("halo");
            const newItem = {
              name: res.data[i].recordType,
              id: res.data[i].recordId,
              time: res.data[i].publishTime,
              info: '新添加的',
              image: '../../../public/2.png'
            };
            this.addItem(newItem);
          }
          if(recordType==3){
            console.log("halo");
            const newItem = {
              name: res.data[i].recordType,
              id: res.data[i].recordId,
              time: res.data[i].publishTime,
              info: '新添加的',
              image: '../../../public/3.png'
            };
            this.addItem(newItem);
          }

        }

      });
    },
    sell(recordId){
      this.TransForm.fromAccountId=localStorage.getItem("accountId");
      this.TransForm.tranRecordId=recordId;
      request.post('/transaction/saveTransaction',this.TransForm).then((res) =>{
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
    this.getAccountAllRecords();
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

</style>


