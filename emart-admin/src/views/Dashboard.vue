<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409EFF">
              <el-icon :size="30"><ShoppingCart /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">今日订单</div>
              <div class="stat-value">{{ stats.todayOrders }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67C23A">
              <el-icon :size="30"><Money /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">今日销售额</div>
              <div class="stat-value">¥{{ stats.todaySales }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #E6A23C">
              <el-icon :size="30"><Goods /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">总商品数</div>
              <div class="stat-value">{{ stats.totalProducts }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #F56C6C">
              <el-icon :size="30"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">总用户数</div>
              <div class="stat-value">{{ stats.totalUsers }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>销售趋势（最近7天）</span>
          </template>
          <v-chart class="chart" :option="salesTrendOption" autoresize />
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <template #header>
            <span>热门商品 Top10</span>
          </template>
          <v-chart class="chart" :option="hotProductOption" autoresize />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ShoppingCart, Money, Goods, User } from '@element-plus/icons-vue'
import { getSalesStats, getSalesTrend, getHotProducts } from '@/api/order'
import { ElMessage } from 'element-plus'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'

use([
  CanvasRenderer,
  LineChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

const stats = ref({
  todayOrders: 0,
  todaySales: 0,
  totalProducts: 0,
  totalUsers: 0
})

const salesTrendOption = ref({
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['销售额', '订单数']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: [
    {
      type: 'value',
      name: '销售额（元）',
      position: 'left'
    },
    {
      type: 'value',
      name: '订单数',
      position: 'right'
    }
  ],
  series: [
    {
      name: '销售额',
      type: 'line',
      data: [],
      smooth: true,
      itemStyle: {
        color: '#409EFF'
      }
    },
    {
      name: '订单数',
      type: 'line',
      yAxisIndex: 1,
      data: [],
      smooth: true,
      itemStyle: {
        color: '#67C23A'
      }
    }
  ]
})

const hotProductOption = ref({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'value',
    boundaryGap: [0, 0.01]
  },
  yAxis: {
    type: 'category',
    data: []
  },
  series: [
    {
      name: '销售数量',
      type: 'bar',
      data: [],
      itemStyle: {
        color: '#E6A23C'
      }
    }
  ]
})

onMounted(() => {
  fetchStats()
  fetchSalesTrend()
  fetchHotProducts()
})

const fetchStats = async () => {
  try {
    const res = await getSalesStats()
    stats.value = res.data
  } catch (error) {
    ElMessage.error('获取统计数据失败')
  }
}

const fetchSalesTrend = async () => {
  try {
    const res = await getSalesTrend()
    const trendData = res.data || []

    const dates = trendData.map(item => item.date.substring(5)) // 只显示 MM-DD
    const amounts = trendData.map(item => item.amount)
    const counts = trendData.map(item => item.count)

    salesTrendOption.value.xAxis.data = dates
    salesTrendOption.value.series[0].data = amounts
    salesTrendOption.value.series[1].data = counts
  } catch (error) {
    ElMessage.error('获取销售趋势失败')
  }
}

const fetchHotProducts = async () => {
  try {
    const res = await getHotProducts()
    const hotProducts = res.data || []

    const productNames = hotProducts.map(item => item.productName)
    const salesCounts = hotProducts.map(item => item.salesCount)

    hotProductOption.value.yAxis.data = productNames
    hotProductOption.value.series[0].data = salesCounts
  } catch (error) {
    ElMessage.error('获取热门商品失败')
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.chart {
  height: 300px;
  width: 100%;
}
</style>
