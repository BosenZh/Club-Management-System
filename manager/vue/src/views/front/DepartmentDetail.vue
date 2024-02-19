<template>
  <div class="main-content">
    <div style="width: 50%; margin: 20px auto">
      <div style="color: #333333; font-size: 20px; font-weight: 700">{{ departmentData.clubName }}：Club Intro
        <el-button type="primary" @click="init" >Apply</el-button>
      </div>
      <div style="margin-top: 10px; color: #767474">Publish Date：{{ departmentData.time }}</div>
      <div style="margin-top: 30px" v-html="departmentData.description"></div>
    </div>
    <el-dialog title="Your Information" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px">
        <el-form-item prop="description" label="Description">
          <el-input v-model="description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Save</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

export default {

  data() {
    let departmentId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      departmentData: {},
      departmentId: departmentId,
      fromVisible: false,
      description: null,
    }
  },
  mounted() {
    this.loadDepartment()
  },
  methods: {
    loadDepartment() {
      this.$request.get('/department/selectById/' + this.departmentId).then(res => {
        if (res.code === '200') {
          this.departmentData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    init() {
      this.fromVisible = true
    },
    save() {
      let data = {
        studentId: this.user.id,
        departmentId: this.departmentId,
        description: this.description
      }
      this.$request.post('/application/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('Success, Waiting for Approval')
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>
