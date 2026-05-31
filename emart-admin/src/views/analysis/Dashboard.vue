<template>
  <div class="analysis-dashboard">
    <el-row :gutter="16">
      <el-col :xs="24" :sm="12" :lg="6" v-for="item in overviewCards" :key="item.label">
        <el-card class="metric-card">
          <div class="metric-label">{{ item.label }}</div>
          <div class="metric-value">{{ item.value }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="section-row">
      <el-col :xs="24" :lg="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>销售趋势</span>
              <el-segmented v-model="trendRange" :options="rangeOptions" @change="fetchTrend" />
            </div>
          </template>
          <v-chart class="chart" :option="trendOption" autoresize />
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>商品销售排行榜</span>
              <el-segmented v-model="rankingRange" :options="rangeOptions" @change="fetchRanking" />
            </div>
          </template>
          <v-chart class="chart" :option="rankingOption" autoresize />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="section-row">
      <el-col :xs="24" :lg="12">
        <el-card>
          <template #header>
            <span>分类销售占比</span>
          </template>
          <v-chart class="chart" :option="categoryOption" autoresize />
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card>
          <template #header>
            <span>库存与订单状态</span>
          </template>
          <el-row :gutter="12">
            <el-col :span="12">
              <v-chart class="mini-chart" :option="stockOption" autoresize />
            </el-col>
            <el-col :span="12">
              <v-chart class="mini-chart" :option="statusOption" autoresize />
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="section-row">
      <template #header>
        <span>商品销售趋势预测与评估</span>
      </template>
      <el-descriptions :column="4" border>
        <el-descriptions-item label="近7日均销售额">¥{{ forecast.recentAverage || 0 }}</el-descriptions-item>
        <el-descriptions-item label="近3日均销售额">¥{{ forecast.shortTermAverage || 0 }}</el-descriptions-item>
        <el-descriptions-item label="未来7日预测销售额">¥{{ forecast.forecastAmount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="趋势判断">
          <el-tag :type="forecast.trend === '上升' ? 'success' : forecast.trend === '下降' ? 'danger' : 'info'">
            {{ forecast.trend || '稳定' }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <div class="forecast-text">{{ forecast.evaluation }}</div>
    </el-card>

    <el-row :gutter="16" class="section-row">
      <el-col :xs="24" :lg="12">
        <el-card>
          <template #header>
            <span>销售异常监控</span>
          </template>
          <el-table :data="anomalies" border stripe max-height="360">
            <el-table-column prop="type" label="类型" width="170" />
            <el-table-column prop="targetName" label="对象" min-width="180" />
            <el-table-column prop="currentValue" label="当前值" width="140" />
            <el-table-column prop="suggestion" label="处理建议" min-width="220" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card>
          <template #header>
            <span>用户画像</span>
          </template>
          <el-table :data="userProfiles" border stripe max-height="360">
            <el-table-column prop="username" label="用户" width="120" />
            <el-table-column prop="purchasePower" label="购买力" width="100" />
            <el-table-column prop="favoriteCategory" label="偏好分类" width="120" />
            <el-table-column prop="browseCount" label="浏览" width="80" />
            <el-table-column prop="orderCount" label="订单" width="80" />
            <el-table-column prop="totalAmount" label="消费额" width="110" />
            <el-table-column prop="region" label="地域/IP" min-width="140" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, LineChart, PieChart } from 'echarts/charts'
import { GridComponent, LegendComponent, TooltipComponent } from 'echarts/components'
import {
  getAnalysisOverview,
  getAnalysisSalesTrend,
  getAnomalies,
  getCategorySales,
  getOrderStatusReport,
  getProductRanking,
  getSalesForecast,
  getStockReport,
  getUserProfiles
} from '@/api/analysis'

use([
  CanvasRenderer,
  BarChart,
  LineChart,
  PieChart,
  GridComponent,
  LegendComponent,
  TooltipComponent
])

const trendRange = ref('day')
const rankingRange = ref('week')
const rangeOptions = [
  { label: '日', value: 'day' },
  { label: '周', value: 'week' },
  { label: '月', value: 'month' }
]

const overview = ref({
  totalSales: 0,
  totalOrders: 0,
  totalProducts: 0,
  totalUsers: 0,
  lowStockCount: 0,
  anomalyCount: 0,
  browseCount: 0
})
const anomalies = ref([])
const userProfiles = ref([])
const forecast = ref({})

const overviewCards = computed(() => [
  { label: '总销售额', value: `¥${overview.value.totalSales || 0}` },
  { label: '有效订单', value: overview.value.totalOrders || 0 },
  { label: '浏览行为', value: overview.value.browseCount || 0 },
  { label: '异常数量', value: overview.value.anomalyCount || 0 }
])

const trendOption = ref({
  tooltip: { trigger: 'axis' },
  legend: { data: ['销售额', '订单数'] },
  grid: { left: 45, right: 45, top: 36, bottom: 28 },
  xAxis: { type: 'category', data: [] },
  yAxis: [
    { type: 'value', name: '销售额' },
    { type: 'value', name: '订单数' }
  ],
  series: [
    { name: '销售额', type: 'line', smooth: true, data: [] },
    { name: '订单数', type: 'bar', yAxisIndex: 1, data: [] }
  ]
})

const rankingOption = ref({
  tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
  grid: { left: 90, right: 24, top: 24, bottom: 28 },
  xAxis: { type: 'value' },
  yAxis: { type: 'category', data: [] },
  series: [{ name: '销量', type: 'bar', data: [] }]
})

const categoryOption = ref({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0 },
  series: [
    {
      name: '分类销售额',
      type: 'pie',
      radius: ['42%', '68%'],
      data: []
    }
  ]
})

const stockOption = ref({
  tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
  grid: { left: 45, right: 24, top: 24, bottom: 50 },
  xAxis: { type: 'category', data: [] },
  yAxis: { type: 'value' },
  series: [{ name: '商品数', type: 'bar', data: [] }]
})

const statusOption = ref({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0 },
  series: [
    {
      name: '订单状态',
      type: 'pie',
      radius: '62%',
      data: []
    }
  ]
})

const fetchOverview = async () => {
  const res = await getAnalysisOverview()
  overview.value = res.data || overview.value
}

const fetchTrend = async () => {
  const res = await getAnalysisSalesTrend({ range: trendRange.value })
  const data = res.data || []
  trendOption.value.xAxis.data = data.map(item => item.label)
  trendOption.value.series[0].data = data.map(item => item.amount)
  trendOption.value.series[1].data = data.map(item => item.count)
}

const fetchRanking = async () => {
  const res = await getProductRanking({ range: rankingRange.value })
  const data = res.data || []
  rankingOption.value.yAxis.data = data.map(item => item.productName)
  rankingOption.value.series[0].data = data.map(item => item.salesCount)
}

const fetchCategorySales = async () => {
  const res = await getCategorySales()
  categoryOption.value.series[0].data = (res.data || []).map(item => ({
    name: item.categoryName,
    value: item.salesAmount
  }))
}

const fetchStockReport = async () => {
  const res = await getStockReport()
  const data = res.data || []
  stockOption.value.xAxis.data = data.map(item => item.label)
  stockOption.value.series[0].data = data.map(item => item.count)
}

const fetchOrderStatusReport = async () => {
  const res = await getOrderStatusReport()
  statusOption.value.series[0].data = (res.data || []).map(item => ({
    name: item.statusName,
    value: item.count
  }))
}

const fetchForecast = async () => {
  const res = await getSalesForecast()
  forecast.value = res.data || {}
}

const fetchAnomalies = async () => {
  const res = await getAnomalies()
  anomalies.value = res.data || []
}

const fetchUserProfiles = async () => {
  const res = await getUserProfiles()
  userProfiles.value = res.data || []
}

const fetchAll = async () => {
  try {
    await Promise.all([
      fetchOverview(),
      fetchTrend(),
      fetchRanking(),
      fetchCategorySales(),
      fetchStockReport(),
      fetchOrderStatusReport(),
      fetchForecast(),
      fetchAnomalies(),
      fetchUserProfiles()
    ])
  } catch (error) {
    ElMessage.error('获取数据分析信息失败')
  }
}

onMounted(fetchAll)
</script>

<style scoped>
.analysis-dashboard {
  padding: 20px;
}

.section-row {
  margin-top: 16px;
}

.metric-card {
  min-height: 94px;
}

.metric-label {
  color: #667085;
  font-size: 14px;
  margin-bottom: 10px;
}

.metric-value {
  color: #101828;
  font-size: 26px;
  font-weight: 700;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.chart {
  width: 100%;
  height: 320px;
}

.mini-chart {
  width: 100%;
  height: 320px;
}

.forecast-text {
  margin-top: 12px;
  color: #475467;
  line-height: 1.6;
}
</style>
