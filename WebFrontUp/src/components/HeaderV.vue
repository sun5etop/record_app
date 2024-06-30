<!--<template>-->
<!--    <el-header class="top-box">-->
<!--      <span v-show="this.userType=='1'|| this.userType=='5'" style="position: absolute;left: 0;margin-left: 12px;">账户总额:{{ balance }}</span>-->
<!--      <div style="margin-right: 20px;">-->
<!--        <img class="logo" src="../assets/logo.jfif">-->
<!--      </div>-->
<!--      <span><h2 style="color:black;">区块链养老保险系统平台</h2></span>-->
<!--      <span style="position: absolute;right: 0;margin-right: 10px;">当前身份：{{this.username}} <el-button @click="toBack">注销</el-button></span>-->
<!--    </el-header>-->
<!--</template>-->
<!--<script>-->
<!--import request from '@/utils/request';-->

<!--export default {-->
<!--    data(){-->
<!--      return{-->
<!--        username: "公安总局",-->
<!--        userType:null,-->
<!--      }-->
<!--    },-->
<!--    props:['balance'],-->
<!--    mounted(){-->
<!--      this.load();-->
<!--    },-->
<!--    methods:{-->
<!--      toBack(){-->
<!--        localStorage.removeItem('username');-->
<!--        localStorage.removeItem('userAddress');-->
<!--        localStorage.removeItem('userType');-->
<!--        this.$router.push('/');-->
<!--      },-->
<!--      load(){-->
<!--        if(localStorage.getItem('username')!=null){-->
<!--          this.username=localStorage.getItem('username');-->
<!--        }-->
<!--        this.userType=localStorage.getItem('userType');-->
<!--        -->
<!--      },-->
<!--      -->
<!--      -->
<!--    }-->
<!--}-->
<!--</script>-->
<!--<style  scoped>-->
<!--header{-->
<!--  padding: 0;-->
<!--}-->
<!--.top-box{-->
<!--  display: flex;-->
<!--  justify-content: center;-->
<!--  align-items: center;-->
<!--  background-color: rgb(178,220,232);-->
<!--  padding: 5px 10px 0 10px;-->
<!--}-->
<!--.logo{-->
<!--  width: 50px;-->
<!--  display: block;-->
<!--  margin-left: 20px;-->
<!--  }-->
<!--</style>-->


<template>
  <el-header class="top-box">
<!--    logo-->
    <div style="display: flex; align-items: center;">
<!--      <div style="margin-right: 20px;">-->
<!--        <img class="logo" src="../assets/logo.jfif">-->
<!--      </div>-->
    </div>
<!--    button set-->
    <div style="display: flex; justify-content: center; align-items: center;">
      <el-button class="threeButton" @click="goTo('首页')">首页</el-button>
      <el-button class="threeButton" @click="goTo('仓库')">仓库</el-button>
      <el-button class="logOutButton" @click="goTo('交易')">交易</el-button>
    </div>
    <div>
      <el-text class="userName" style="color: white">{{userName}}</el-text>
      <el-button class="logOutButton" @click="logout">注销</el-button>
    </div>
  </el-header>
</template>

<script>
import request from '@/utils/request';
export default {
  data(){
    return{
      userName: "",
      // userType:null,
    }
  },
  mounted(){
    const storedData = localStorage.getItem('ownerName');
    if(storedData) {
      this.userName = storedData;
    }
  },
  methods: {
    goTo(page) {
      switch (page) {
        case '首页':
          this.$router.push('/account/accountIndex');
          break;
        case '交易':
          this.$router.push('/account/transaction');
          break;
        case '仓库':
          this.$router.push('/account/repository');
          break;
        default:
          break;
      }
    },
    logout() {
      // 实现注销逻辑，例如清除登录状态等
      // 这里可以根据具体情况进行实现
      this.$router.push('/'); //返回登陆界面
      console.log('注销');
    }
  }
};
</script>

<!--import request from '@/utils/request';-->

<!--export default {-->
<!--    data(){-->
<!--      return{-->
<!--        username: "公安总局",-->
<!--        userType:null,-->
<!--      }-->
<!--    },-->
<!--    props:['balance'],-->
<!--    mounted(){-->
<!--      this.load();-->
<!--    },-->
<!--    methods:{-->
<!--      toBack(){-->
<!--        localStorage.removeItem('username');-->
<!--        localStorage.removeItem('userAddress');-->
<!--        localStorage.removeItem('userType');-->
<!--        this.$router.push('/');-->
<!--      },-->
<!--      load(){-->
<!--        if(localStorage.getItem('username')!=null){-->
<!--          this.username=localStorage.getItem('username');-->
<!--        }-->
<!--        this.userType=localStorage.getItem('userType');-->
<!--        -->
<!--      },-->
<!--      -->
<!--      -->
<!--    }-->
<!--}-->
<!--</script>-->

<style scoped>
.userName {
  margin-right: 10px;
}
.top-box {
  background-color: #302d2d;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}
.threeButton {
  margin-right: 60px;
  margin-left: 100px;
  background-color: #d60017;
  color: white;
  border-color: #d60017;
  border-radius: 8px;
  width: 80px;
}
.logOutButton {
  background-color: #d60017;
  color: white;
  border-color: #d60017;
  border-radius: 8px;
  width: 80px;
}
.logo {
  width: 40px;
  height: 40px;
}
</style>

