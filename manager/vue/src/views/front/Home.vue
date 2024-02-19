<template>
  <div class="main-content">
    <div style="width: 70%; margin: 20px auto">
      <el-carousel height="600px" style="border-radius: 10px">
        <el-carousel-item v-for="item in carousel" :key="item">
          <img :src="item" alt="" style="width: 100%; height: 600px; border-radius: 10px">
        </el-carousel-item>
      </el-carousel>
      <div></div>
      <div>
        <div style="margin: 20px 0 0 0; width: 130px; background-color: #f16f44; height: 30px; line-height: 30px; text-align: center; font-size: 18px; color: white; font-weight: bold; border-radius: 20px">Our Clubs</div>
        <div style="margin-top: 15px">
          <el-row :gutter="20">
            <el-col :span="5" v-for="item in departmentData">
              <img :src="item.logo" alt="" style="width: 100%; height: 196px; border-radius: 50%" @click = "navTo('/front/departmentDetail?id=' + item.id)">
              <div style="text-align: center; margin-top: 10px; font-size: 16px">{{ item.clubName }}</div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    </div>


</template>

<script>

export default {

  data() {
    return {
      carousel: [
        require('@/assets/imgs/carou1.png'),
        require('@/assets/imgs/carou2.png'),
        require('@/assets/imgs/carou3.png'),
      ],
      departmentData: []
    }
  },
  mounted() {
    this.loadDepartment()
  },

  methods: {
    loadDepartment(){
      this.$request.get('/department/selectAll').then(res => {
        if (res.code === '200') {
          this.departmentData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    navTo(url){
      location.href = url
    }
  }
}
</script>
<style scoped>
.el-col-5{
  width: 20%;
  max-width: 20%;
  padding: 10px 10px;
}
</style>
