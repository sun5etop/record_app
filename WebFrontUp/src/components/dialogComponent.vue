<template>
  <transition name="dialog-fade">
    <el-dialog
        v-if="showDialog"
        :title="dialogTitle"
        class="dialog-component"
        :visible.sync="showDialog"
        width="500px"
        @close="closeDialog(0)"
    >
      <el-form
          ref="formInfo"
          :model="formInfo"
          class="demo-form-inline"
          label-width="80px"
      >
        <el-form-item label="日期：" prop="date" required>
          <el-input v-model="formInfo.date"></el-input>
        </el-form-item>
        <el-form-item label="姓名：" prop="name" required>
          <el-input v-model="formInfo.name"></el-input>
        </el-form-item>
        <el-form-item label="省份：" prop="province" required>
          <el-input v-model="formInfo.province"></el-input>
        </el-form-item>
        <el-form-item label="市区：" prop="city" required>
          <el-input v-model="formInfo.city"></el-input>
        </el-form-item>
        <el-form-item label="地址：" prop="address" required>
          <el-input v-model="formInfo.address"></el-input>
        </el-form-item>
        <el-form-item label="邮编：" prop="zip" required>
          <el-input v-model="formInfo.zip"></el-input>
        </el-form-item>
        <el-form-item style="text-align: right;">
          <el-button type="primary" @click="submitForm('formInfo')"
          >确定</el-button
          >
          <el-button @click="closeDialog(0)">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </transition>
</template>

<script>
export default {
  name: "DialogComponent",
  props: {
    dialogTitle: {
      type: String,
      default: "添加人员",
    },
    itemInfo: {
      type: Object,
      default: function () {
        return {};
      },
    },
  },
  data() {
    return {
      showDialog: false,
      formInfo: JSON.parse(JSON.stringify(this.itemInfo)),
    };
  },
  methods: {
    // 保存操作
    submitForm(formName) {
      const that = this;
      const params = Object.assign(that.formInfo, {});
      that.$refs[formName].validate((valid) => {
        if (valid) {
          // 走保存请求
          that.$message({
            message: "操作成功！",
            type: "success",
          });
          that.closeDialog(1);
        } else {
          return false;
        }
      });
    },
    // 关闭弹框
    closeDialog(flag) {
      this.$refs["formInfo"].resetFields();
      this.showDialog = false;
      this.$emit("closeDialog", flag);
    },
  },
};
</script>

<style scoped lang="scss">
.dialog-fade-enter-active {
  -webkit-animation: dialog-fade-in 0.3s;
  animation: dialog-fade-in 0.3s;
}
.dialog-fade-leave-active {
  -webkit-animation: dialog-fade-out 0.3s;
  animation: dialog-fade-out 0.3s;
}
@-webkit-keyframes dialog-fade-in {
  0% {
    -webkit-transform: translate3d(0, -20px, 0);
    transform: translate3d(0, -20px, 0);
    opacity: 0;
  }
  100% {
    -webkit-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);
    opacity: 1;
  }
}
@keyframes dialog-fade-in {
  0% {
    -webkit-transform: translate3d(0, -20px, 0);
    transform: translate3d(0, -20px, 0);
    opacity: 0;
  }
  100% {
    -webkit-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);
    opacity: 1;
  }
}
@-webkit-keyframes dialog-fade-out {
  0% {
    -webkit-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);
    opacity: 1;
  }
  100% {
    -webkit-transform: translate3d(0, -20px, 0);
    transform: translate3d(0, -20px, 0);
    opacity: 0;
  }
}
@keyframes dialog-fade-out {
  0% {
    -webkit-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);
    opacity: 1;
  }
  100% {
    -webkit-transform: translate3d(0, -20px, 0);
    transform: translate3d(0, -20px, 0);
    opacity: 0;
  }
}
</style>