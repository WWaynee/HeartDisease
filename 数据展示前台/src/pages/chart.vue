<template>
  <!-- 图表分析 -->
  <el-container>
    <el-header>
      <h3>图表分析</h3>
    </el-header>
    <el-divider></el-divider>
    <el-main>
      <!-- 选择器 -->
      <el-row style="margin-bottom: 50px;">
        <el-col>
          <label style="margin-left: 30px;">相关因素：</label>
          <el-select
            v-model="selectValue"
            placeholder="请选择"
            @change="selectChange"
          >
            <el-option
              v-for="item in select"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <!-- 柱状图 -->
      <v-chart
        :options="barOptions"
        :auto-resize="true"
        theme="light"
        class="barChart"
      />
      <!-- 饼图 -->
      <v-chart
        :options="pieOptions"
        :auto-resize="true"
        theme="light"
        class="pirChart"
        style=""
      />
    </el-main>
  </el-container>
</template>

<script>
import ECharts from "vue-echarts";
// 柱状图
import "echarts/lib/chart/bar";
// 饼图
require("echarts/lib/chart/pie");
// 提示
import "echarts/lib/component/tooltip";
// 图例// import "echarts/lib/component/legend";
// 标题
import "echarts/lib/component/title";

export default {
  components: {
    "v-chart": ECharts
  },
  data() {
    return {
      // 选择
      select: [
        {
          value: "Thal",
          label: "Thal"
        },
        {
          value: "MainBloodVessels",
          label: "主血管数"
        },
        {
          value: "Hyperglycemia",
          label: "糖尿病"
        },
        {
          value: "Hypertension",
          label: "高血压"
        },
        {
          value: "Hyperlipemia",
          label: "高血脂"
        }
      ],
      selectValue: "Thal",
      // 柱状图图表数据 默认配置
      barOptions: {
        title: {
          text: "",
          left: "center"
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow" // 默认为直线，可选为：'line' | 'shadow'‘
          },
          formatter: function(params) {
            let value = (params[0].value * 100).toFixed(2) + "%";
            let seriesName = params[0].seriesName;
            let name = params[0].name;
            return `${name}<br/>${seriesName}: ${value}`;
          }
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true
        },
        xAxis: {
          type: "category",
          data: [],
          axisTick: {
            alignWithLabel: true
          }
        },
        yAxis: {
          type: "value",
          axisLabel: {
            show: true,
            interval: "auto",
            formatter(value, index) {
              return (value * 100).toFixed(2) + "%";
            }
          },
          show: true
        },
        series: {
          name: "发病率",
          type: "bar",
          barWidth: "60%",
          data: []
        }
      },
      // 饼图图表数据 默认配置
      pieOptions: {
        title: {
          text: "",
          left: "center"
        },
        tooltip: {
          trigger: "item",
          // formatter: "{a} <br/>{b} : {c} ({d}%)"
          formatter: "{a} <br/>{b} : {d}%"
        },
        series: {
          name: "",
          type: "pie",
          // radius: "55%",
          center: ["50%", "60%"],
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)"
            }
          }
        }
      }
    };
  },
  methods: {
    selectChange(value) {
      console.log(value);
      if (value == "Thal") this.getThal();
      else if (value == "MainBloodVessels") this.getMainBloodVessels();
      else if (value == "Hyperglycemia") this.getHyperglycemia();
      else if (value == "Hypertension") this.getHypertension();
      else if (value == "Hyperlipemia") this.getHyperlipemia();
      else console.log("选中的因素类型没有对应的请求方法");
    },
    // getThal
    getThal() {
      this.barOptions.title.text = "不同Thal值的人的心脏病发病率";
      this.barOptions.xAxis.data = ["THAL值为3的人", "THAL值为6的人", "THAL值为7的人"];
      this.pieOptions.title.text = "心脏病病人中，不同THAL值患者的比例";
      this.pieOptions.series.name = "Thal"
      this.$api.getThal().then(resp => {
        // console.log(resp.data);
        const proportion = []; // 不同thal值在患者中的比例数据
        const incidence = []; // 不同thal值人的发病率数据
        for (const prop in resp.data) {
          proportion.push({
            value: parseFloat(resp.data[prop].proportion.data),
            name: resp.data[prop].proportion.description.slice(7)
          });
          incidence.push(parseFloat(resp.data[prop].incidence.data));
        }
        // console.log(proportion, incidence);
        this.barOptions.series.data = incidence; //柱状图发病率
        this.pieOptions.series.data = proportion; //饼图比例
      });
    },
    // getMainBloodVessels 主血管数
    getMainBloodVessels() {
      this.barOptions.title.text = "不同主血管数的人的心脏病发病率";
      this.barOptions.xAxis.data = ["主血管数为0", "主血管数为1", "主血管数为2", "主血管数为3"];
      this.pieOptions.title.text = "心脏病病人中，不同主血管数患者的比例";
      this.pieOptions.series.name = "心脏病的病人中";
      this.$api.getMainBloodVessels().then(resp => {
        // console.log(resp.data);
        const proportion = []; // 不同thal值在患者中的比例数据
        const incidence = []; // 不同thal值人的发病率数据
        for (const prop in resp.data) {
          proportion.push({
            value: parseFloat(resp.data[prop].proportion.data),
            name: resp.data[prop].proportion.description.slice(7)
          });
          incidence.push(parseFloat(resp.data[prop].incidence.data));
        }
        // console.log(proportion, incidence);
        this.barOptions.series.data = incidence; //柱状图发病率
        this.pieOptions.series.data = proportion; //饼图比例
      });
    },
    // getHyperglycemia 糖尿病
    getHyperglycemia() {
      this.barOptions.title.text = "糖尿病患者的心脏病发病率";
      this.barOptions.xAxis.data = ["患有糖尿病的人"];
      this.pieOptions.title.text = "心脏病病人中，糖尿病患者的比例";
      this.pieOptions.series.name = "心脏病的病人中";
      this.$api.getHyperglycemia().then(resp => {
        console.log(resp.data)
        const incidence = [resp.data.incidence.data];
        const {data: proportionData, description: proportionDesc} = resp.data.proportion;
        const proportion = [
          {value: proportionData, name: proportionDesc.slice(7)},
          {value: 1 - parseFloat(proportionData), name: "其他未患糖尿病的患者比例"},
        ]
        this.barOptions.series.data = incidence; //柱状图发病率
        this.pieOptions.series.data = proportion; //饼图比例
      })
    },
    // getHypertension 高血压
    getHypertension() {
      this.barOptions.title.text = "高血压的心脏病发病率";
      this.barOptions.xAxis.data = ["有高血压的人"];
      this.pieOptions.title.text = "心脏病病人中，高血压患者的比例";
      this.pieOptions.series.name = "心脏病的病人中";
      this.$api.getHypertension().then(resp => {
        console.log(resp.data)
        const incidence = [resp.data.incidence.data];
        const {data: proportionData, description: proportionDesc} = resp.data.proportion;
        const proportion = [
          {value: proportionData, name: proportionDesc.slice(7)},
          {value: 1 - parseFloat(proportionData), name: "其他未患高血压的患者比例"},
        ]
        this.barOptions.series.data = incidence; //柱状图发病率
        this.pieOptions.series.data = proportion; //饼图比例
      })
    },
    // getHyperlipemia 高血脂
    getHyperlipemia() {
      this.barOptions.title.text = "高血脂的心脏病发病率";
      this.barOptions.xAxis.data = ["有高血脂的人"];
      this.pieOptions.title.text = "心脏病病人中，高血脂患者的比例";
      this.pieOptions.series.name = "心脏病的病人中";
      this.$api.getHyperlipemia().then(resp => {
        console.log(resp.data)
        const incidence = [resp.data.incidence.data];
        const {data: proportionData, description: proportionDesc} = resp.data.proportion;
        const proportion = [
          {value: proportionData, name: proportionDesc.slice(7)},
          {value: 1 - parseFloat(proportionData), name: "其他未患高血脂的患者比例"},
        ]
        this.barOptions.series.data = incidence; //柱状图发病率
        this.pieOptions.series.data = proportion; //饼图比例
      })
    }
  },
  mounted() {
    // getThal
    this.getThal();
  }
};
</script>

<style>
.el-main {
  width: 100%;
}
.el-select {
  margin-top: -20px;
  margin-left: 10px;
  /* margin-bottom: 30px; */
}
.barChart {
  float: left;
  margin-bottom: 50px;
  margin-right: 100px;
  margin-left: 30px;
}
.pirChart {
  float: left;
  margin-bottom: 100px;
}
</style>
