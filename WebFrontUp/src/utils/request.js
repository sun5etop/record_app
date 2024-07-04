import axios from 'axios'
import router from "@/router/index.js";
const request = axios.create({
	// baseURL: 'http://111.67.201.180',

	baseURL: 'http://172.19.132.130:8080',
	timeout: 50000
})

//request 拦截器
//可以自请求发送前对请求做一些处理
//比如统一加入token，对请求进行加密
request.interceptors.request.use(config => {
	config.headers['Content-Type'] = 'application/json;charset=utf-8';
	config.headers['token']=encodeURIComponent(localStorage.getItem('accountId'));
	config.headers['userAddress']=encodeURIComponent(localStorage.getItem('userAddress'));

	//请求头添加token
	let uToken=localStorage.getItem("uToken");
	if(uToken){
		config.headers['u-token']=uToken;
	}
	return config
},	error =>{
	console.log('err' + error) //debug
	return Promise.reject(error)
});

//response拦截器
//2 使用axios设置后置拦截器，处理后台被拦截，没有登录的请求
// axios.interceptors.response.use(result=>{
// 	let data = result.data;
//
// 	//只要前台被拦截的请求里面含这两个参数，那么就跳转到登录界面
// 	// if(data.msg==="NoUser")
// 	// 	this.$router.push('/');
//
// 	return result;
// },error => {
// 	let data = error.data;
// 	if(data.msg==="NoUser")
// 		this.$router.push('/');
// 	return Promise.reject(error);
// });


request.interceptors.response.use(

	response => {
		let res = response.data;
//debug
		//兼容服务端返回的字符串数据
		// if(!res.success&&res.msg==='NoUser'){
		// 	this.$router.push('/');
		// }

		if (typeof res == 'string') {
			res = res ? JSON.parse(res) : res
		}
		console.log(res);

		return res;
	},
		error => {
			console.log('err' + error) //debug
			return Promise.reject(error)
		}

)

export default request
