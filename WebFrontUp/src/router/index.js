import { createRouter, createWebHistory } from 'vue-router'
import login from '../views/login/login.vue'
import adminIndex from "@/views/admin/adminIndex.vue";
import recordAdmin from "@/views/admin/recordAdmin.vue";
import transactionAdmin from "@/views/admin/transactionAdmin.vue";
import repository from "@/views/account/repository.vue";
import transaction from "@/views/account/transaction.vue";
import accountIndex from "@/views/account/accountIndex.vue";
import NotFound from "@/components/NotFound.vue";
// import reister from '../views/register.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: login
    },
    {
      path: '/admin/adminIndex',
      name: 'adminIndex',
      component: adminIndex
    },
    {
      path: '/admin/recordAdmin',
      name: 'recordAdmin',
      component: recordAdmin
    },
    {
      path: '/admin/transactionAdmin',
      name: 'transactionAdmin',
      component: transactionAdmin
    },
    {
      path:'/register',
      name:'register',
      component: () => import('../views/login/register.vue')
    },
    {
      path:'/account/repository',
      name:'repository',
      component: repository
    },
    {
      path:'/account/accountIndex',
      name:'accountIndex',
      component:  accountIndex,
      meta:{
        title: '唱片主页'
      }
    },
    {
      path:'/account/transaction',
      name:'transaction',
      component: transaction
    },
    {
      path: '/:catchAll(.*)',
      name: 'NotFound',
      component: NotFound
    }
  ]
})
router.beforeEach((to, from, next) => {
  // 获取token
  const token = localStorage.getItem('uToken')
  // console.log("token");
  // console.log(token);
  // if (to.path === '/') {
  //   token && next(from.fullPath || '/account/accountIndex')
  //   next()
  // } else {
  //   // 判断有木有token
  //   token && next()
  //   next('/')
  // }
  if (to.path === '/') {
    if (token) {
      // 避免无限重定向
      if (from.path === '/account/accountIndex') {
        next(false);
      } else {
        next('/account/accountIndex');
      }
    } else {
      next();
    }
  } else {
    if (token) {
      next();
    } else {
      next('/');
    }
  }
})


export default router