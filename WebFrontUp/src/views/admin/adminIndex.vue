<template>
    <h2 style="text-align: center;">管理员——账户管理</h2>
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
          <el-table :data="accounts">
            <el-table-column prop="ownerName" label="ownerName"  />
            <el-table-column prop="accountAddress" label="accountAddress" />
            <el-table-column prop="accountId" label="accountId"/>
            <el-table-column prop="password" label="password" />
            <el-table-column label="status" >
              <p>未冻结</p>
            </el-table-column>
            <el-table-column align="right">
              <template #header>
                <p>option</p>
              </template >
              <el-button type="primary" plain @click="editAccount()">
                编辑用户
              </el-button>
            </el-table-column>
          </el-table>

        </el-scrollbar>

        <!--弹出的弹窗内容-->
        <el-dialog title="编辑账号信息" v-model="dialogVisible"  center width="20%" >
          <div style="display: flex;flex-direction: column;align-items: center;justify-content: center;">
            <el-form :inline="true">
              <el-form-item>
                <p>当前用户名：ykm</p>
                <el-input v-model="accountName" placeholder="请输入用户名" clearable></el-input>
              </el-form-item>
              <el-form-item>
                <p>当前用户密码：123</p>
                <el-input v-model="accountPW" placeholder="请输入账户密码" clearable></el-input>
              </el-form-item>
              <el-switch v-model="frozen" active-text="正常" inactive-text="冻结" />
            </el-form>
          </div>
          <span slot="footer" class="dialog-footer">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button @click="dialogVisible = false" type="primary" >确 定</el-button>
            </span>
        </el-dialog>
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
      accounts:[],
      dialogVisible: false,
      accountName: "",
      accountPW:"",
      frozen:"",
    };
  },
  mounted() {
    const storedData = localStorage.getItem('ownerName');
    if (storedData) {
      this.data = storedData;
    }
      this.getAllAccounts();
  },
  methods:{
    logout(){
      this.$router.push('/');
    },
    jumpAccount()
    {
      this.$router.push('/admin/adminIndex');

    },
    jumpRecord()
    {
      this.$router.push('/admin/recordAdmin');

    },
    jumpTransaction()
    {
      this.$router.push('/admin/transactionAdmin');

    },

    getAllAccounts(){
      request.get('/account/getAllAccounts').then((res)=>{
        console.log(res.data.msg);
        if(res.data.success)
          this.accounts =res.data;
      })
    },
    editAccount(){
      console.log(this.dialogVisible);
      this.dialogVisible = true;//显示弹窗
      console.log(this.dialogVisible);

    },
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
import { ref } from 'vue'
import { Menu as IconMenu, Message, Setting } from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from "element-plus";

const item = {
  date: '2016-05-02',
  name: 'Tom',
  address: 'No. 189, Grove St, Los Angeles',
}
 //const tableData = ref(Array.from({ length: 20 }).fill(item))



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