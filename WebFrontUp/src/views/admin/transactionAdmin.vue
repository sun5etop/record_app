<template>
  <h2 style="text-align: center;">管理员——交易管理</h2>
  <!--    <h2 style="text-align: center;">{{data}}</h2>-->

  <el-container class="layout-container-demo" style="height: 500px">
    <el-aside width="400px">
      <el-scrollbar>
        <el-menu :default-openeds="['1', '3']">
          <el-menu-item index="1" @click="jumpAccount()">
            <template #title>
              <el-icon><message /></el-icon><p :style="{ fontSize: '30px' }">账户管理</p>
            </template>
          </el-menu-item>
          <el-menu-item index="2" @click="jumpRecord()">
            <template #title>
              <el-icon><icon-menu /></el-icon><p :style="{ fontSize: '30px' }">唱片管理</p>
            </template>
          </el-menu-item>
          <el-menu-item index="3" @click="jumpTransaction">
            <template #title>
              <el-icon><setting /></el-icon><p :style="{ fontSize: '30px' }">交易管理</p>
            </template>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <el-header style="text-align: right; font-size: 12px">
        <div class="toolbar">
          <el-dropdown>
            <el-icon style="margin-right: 8px; margin-top: 1px">
              <setting />
            </el-icon>
          </el-dropdown>
          <span :style="{ fontSize: '30px' }">{{data}}</span>
          <el-button type="primary" plain @click="logout()">注销</el-button>
        </div>
      </el-header>

      <el-main>
        <el-scrollbar>
          <el-table :data=transactions>
            <el-table-column prop="tranId" label="tranId" width="140" />
            <el-table-column prop="fromAccountId" label="fromAccountId" width="120" />
            <el-table-column prop="toAccountId" label="toAccountId" />
            <el-table-column prop="tranRecordId" label="tranRecordId" />
            <el-table-column prop="tranStatus" label="tranStatus" />

          </el-table>
        </el-scrollbar>
      </el-main>
    </el-container>
  </el-container>

</template>

<script>
import request from "@/utils/request.js";

export default {

  data() {
    return {
      data: '',
      transactions:[]
    };
  },
  mounted() {
    const storedData = localStorage.getItem('ownerName');
    if (storedData) {
      this.data = storedData;
    }
    this.getAllTransactions();
  },
  methods: {
    logout(){
      this.$router.push('/');
    },
    jumpAccount() {
      this.$router.push('/admin/adminIndex');

    },
    jumpRecord() {
      this.$router.push('/admin/recordAdmin');

    },
    jumpTransaction() {
      this.$router.push('/admin/transactionAdmin');
    },
    getAllTransactions(){
      request.get('/transaction/getAllTransactions').then((res)=>{
        console.log(res);
        this.transactions =res.data;
      })
    }
  }
};
// import {
//   Document,
//   Menu as IconMenu,
//   Location,
//   Setting,
// } from '@element-plus/icons-vue'
// const handleOpen = (key: string, keyPath: string[]) => {
//   console.log(key, keyPath)
// }
// const handleClose = (key: string, keyPath: string[]) => {
//   console.log(key, keyPath)
// }
import {ref} from 'vue'
import {Menu as IconMenu, Message, Setting} from '@element-plus/icons-vue'

const item = {
  date: '2016-05-02',
  name: 'Tom',
  address: 'No. 189, Grove St, Los Angeles',
}
// const tableData = ref(Array.from({ length: 20 }).fill(item))


</script>

<style scoped>
.layout-container-demo .el-header {
  position: relative;
  background-color: var(--el-color-white);
  color: var(--el-text-color-primary);
}

.layout-container-demo .el-aside {
  color: var(--el-text-color-primary);
  background: var(--el-color-white);
}

.layout-container-demo .el-menu {
  border-right: none;
}

.layout-container-demo .el-main {
  padding: 0;
}

.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
</style>