import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)


const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/home',
    children: [
      { path: '403', name: 'NoAuth', meta: { name: 'noAuth' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: 'Homepage' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: 'AdminInfo' }, component: () => import('../views/manager/Admin') },
      { path: 'adminPage', name: 'AdminPage', meta: { name: 'Info' }, component: () => import('../views/manager/AdminPage') },
      { path: 'studentPage', name: 'StudentPage', meta: { name: 'Info' }, component: () => import('../views/manager/StudentPage') },
      { path: 'password', name: 'Password', meta: { name: 'Change your password' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: 'Announcement' }, component: () => import('../views/manager/Notice') },
      { path: 'student', name: 'student', meta: { name: 'Student Info' }, component: () => import('../views/manager/Student') },
      { path: 'department', name: 'department', meta: { name: 'Club Info' }, component: () => import('../views/manager/Department') },
      { path: 'application', name: 'Application', meta: { name: 'Application Management' }, component: () => import('../views/manager/Application') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', name: 'Home', meta: { name: 'Homepage' }, component: () => import('../views/front/Home') },
      { path: 'person', name: 'Person', meta: { name: 'Personal info' }, component: () => import('../views/front/Person') },
      { path: 'departmentDetail', name: 'DepartmentDetail', meta: { name: 'Club info' }, component: () => import('../views/front/DepartmentDetail') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: 'Login' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: 'Register' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: 'Unable to access' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// router.beforeEach((to ,from, next) => {
//   let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
//   if (to.path === '/') {
//     if (user.role) {
//       if (user.role === 'USER') {
//         next('/front/home')
//       } else {
//         next('/home')
//       }
//     } else {
//       next('/login')
//     }
//   } else {
//     next()
//   }
// })

export default router
