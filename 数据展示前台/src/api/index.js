import axios from "axios";
import URLs from "./URLs.js";
// console.log(URLs);
// 自定义一个axios实例默认值
const Request = axios.create({
  baseURL: URLs.baseUrl,
  method: "GET",
  params: {
    // appkey: "Dirty_1579775100154"
    appkey: "Lazy_Bone_1569767870124"
  }
});

// 预测心脏病
function predictHeartDisease(data) {
  // devServer
  // return axios.post("/api/heart/single", data);
  // production 上线
  return axios.post(URLs.predictHeartDisease, data);
}

// getThal
function getThal() {
  return axios.get(URLs.getThal);
}

// getMainBloodVessels
function getMainBloodVessels() {
  return axios.get(URLs.getMainBloodVessels);
}

// 糖尿病
function getHyperglycemia() {
  return axios.get(URLs.getHyperglycemia);
}
// 高血压
function getHypertension() {
  return axios.get(URLs.getHypertension);
}
// 高血脂
function getHyperlipemia() {
  return axios.get(URLs.getHyperlipemia);
}

export default {
  predictHeartDisease,
  getThal,
  getMainBloodVessels,
  getHyperglycemia,
  getHypertension,
  getHyperlipemia
};
