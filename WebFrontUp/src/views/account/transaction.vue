<template>
  <div class="back">
    <div class="index">
      <el-container>
        <el-header>
          <header-v></header-v>
        </el-header>
      </el-container>
    </div>

    <div class="content">
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
              <h1>唱片id： {{ item.tranRecordId }}<br>
                <el-tag type="success" >卖家AccountID: {{ item.fromAccountId }}</el-tag>&nbsp;
                <el-tag type="warning">TIME: 2024-06-28</el-tag>
                <br>
                <el-tag type="primary" v-if="item.tranStatus == 0">已上链</el-tag>
                <el-tag type="success" v-else-if="item.tranStatus == 1">已售出</el-tag>
                <el-tag type="danger" v-else-if="item.tranStatus == 2">未上链</el-tag>
              </h1>
            </div>
            <div class="footer-container" style="display: flex; justify-content: flex-end; align-items: center;">
                <el-button type="success" @click="showTranId(index)">获取交易id</el-button>
                <el-button type="primary" @click="publish(item.tranRecordId,item.tranId)">上架</el-button>

              </div>
            <!--          获取交易id对话框-->
            <el-dialog
                v-model="dialogVisible"
                title="交易Id："
                width="500"
            >
    <span>{{ items[cardIndex].tranId }}</span>
    <template #footer>
      <div class="dialog-footer">

        <el-button type="primary" @click="dialogVisible = false">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
          </template>
        </el-card>
      </span>
      </div>
    </div>
  </div>

</template>

<script>
import {ref} from 'vue';
import HeaderV from "@/components/HeaderV.vue";
import request from "@/utils/request.js";

export default {
  data() {
    const items = ref([]);
    const TransForm = ref({
      fromAccountId: '',
      tranRecordId: '',
    });
    const publishTransForm = ref({
      fromAccountId: '',
      toAccountId: '',
      tranRecordId: '',
      tranId: '',
    });
    const buyForm = ref({
      toAccountId: '',
      tranId: '',
    });
    //const imag ='https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png';
    return {
      items,
      records: [],
      TransForm,
      publishTransForm,
      input: '',
      buyForm,
      dialogVisible: false,
      cardIndex:0,
      curId: '',
    };
  },
  methods: {
    showTranId(i) {
      this.dialogVisible = true;
      this.cardIndex=i;
    },
    buy(input, account_id) {
      this.buyForm.tranId = input;
      this.buyForm.toAccountId = localStorage.getItem('accountId');
      console.log(this.buyForm.toAccountId);
      request.post('/transaction/acceptTransaction', this.buyForm).then((res) => {
        if (res.code == 200) {
          this.$message({
            type: "success",
            message: "购买成功！"
          })
          console.log("购买成功！");
        }
      });
    },

    addItem(newItem) {
      this.items.push(newItem);
    },
    getMyTransaction() {
      request.get('/transaction/getMyTransaction').then((res) => {
        console.log(res);
        this.records = res.data;
        for (let i = 0; i < res.data.length; i++) {
          const newItem = {
            tranId: res.data[i].tranId,
            fromAccountId: res.data[i].fromAccountId,
            toAccountId: res.data[i].toAccountId,
            tranRecordId: res.data[i].tranRecordId,
            tranStatus: res.data[i].tranStatus,
          };
          this.addItem(newItem);
        }

      });
    },
    publish(recordId, tranId) {
      // request.post('/transaction/getTransactionByRecordId',recordId). then((res)=>{
      //   console.log(res);
      //   this.publishTransForm.tranId=res.data.tranId;
      //   this.publishTransForm.tranRecordId=recordId;
      //   this.publishTransForm.fromAccountId=res.data.fromAccountId;
      //   this.publishTransForm.toAccountId=res.data.toAccountId;
      // });

      this.publishTransForm.fromAccountId = localStorage.getItem("accountId");
      this.publishTransForm.tranRecordId = recordId;
      this.publishTransForm.tranId = tranId;
      request.post('/transaction/publishTransaction', this.publishTransForm).then((res) => {
        if (res.code == 200) {
          this.$message({
            type: "success",
            message: "交易上链成功！"
          })
          console.log("交易上链成功！");
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

.index {
  background-color: #302d2d;
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

.input {
  margin-left: 20px;
  margin-top: 20px;
}

.back {
  background-color: #1e1c1c;
  position: absolute;
  width: 100%;
  height: 100%;
}

</style>