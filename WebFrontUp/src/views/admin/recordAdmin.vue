<template>
  <h2 style="text-align: center;">管理员——唱片管理</h2>
  <!--    <h2 style="text-align: center;">{{data}}</h2>-->

  <el-container class="layout-container-demo" style="height: 1020px">
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
      <el-button type="primary" plain style="margin-right: auto" @click="addRecords()">添加唱片</el-button>
      <!--弹出的弹窗内容-->
      <el-dialog title="编辑账号信息" v-model="dialogVisible"  center width="20%" >
        <div style="display: flex;flex-direction: column;align-items: center;justify-content: center;">
          <el-form :inline="true">
            <el-form-item>
              <p>发行唱片类型：</p>
              <el-select v-model="value" placeholder="选择唱片类型" style="width: 240px">
                <el-option
                    v-for="item in options()"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <p>发行唱片数量：</p>
              <el-input v-model="recordsNum" placeholder="请输入唱片发行数量，小于10" clearable></el-input>
            </el-form-item>
          </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button @click="dialogVisible = false" type="primary" >确 定</el-button>
            </span>
      </el-dialog>

      <el-main >
        <el-scrollbar>
          <el-table :data=records >
            <el-table-column prop="recordId" label="recordId"  />
            <el-table-column prop="recordType" label="recordType"  />
            <el-table-column prop="publishTime" label="publishTime" />
            <el-table-column prop="recordOwnerId" label="recordOwnerId" />
          </el-table>
        </el-scrollbar>
      </el-main>
    </el-container>
  </el-container>

</template>

<script>
import request from "@/utils/request.js";
const value = ref('')
const options = [
  {
    value: 'Option1',
    label: '类型1：JAY-Jay',
  },
  {
    value: 'Option2',
    label: '类型2：叶惠美-Jay',
  },
  {
    value: 'Option3',
    label: '类型3：启示录-G.E.M',
  },
]

export default {

  data() {
    return {
      data: '',
      records:[],
      recordsName:"",
      recordsNum:0,
      dialogVisible:false,

    };
  },
  mounted() {
    const storedData = localStorage.getItem('ownerName');
    if (storedData) {
      this.data = storedData;
    }
    this.getAllRecords();
  },
  methods:{
    options() {
      return options
    },
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
    getAllRecords(){
      request.get('/account/getAllRecords').then((res)=>{
        console.log(res);
        this.records =res.data;
      })
    },
    addRecords(){
      this.dialogVisible=true;
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
import { ref } from 'vue'
import { Menu as IconMenu, Message, Setting } from '@element-plus/icons-vue'

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