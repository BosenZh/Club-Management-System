<template>
  <div>
    <div class="search">
      <el-select v-model="dbStatus" placeholder="Please Select Application Status" style="width: 200px">
        <el-option label="In Progress" value="In Progress"></el-option>
        <el-option label="Approved" value="Approved"></el-option>
        <el-option label="Declined" value="Declined"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">Search</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">Reset</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column prop="id" label="id" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="studentName" label="Student Name" show-overflow-tooltip></el-table-column>
        <el-table-column prop="departmentName" label="Club Name" show-overflow-tooltip></el-table-column>
        <el-table-column prop="description" label="Application Description"></el-table-column>
        <el-table-column prop="process" label="Process"></el-table-column>
        <el-table-column prop="status" label="Status"></el-table-column>
        <el-table-column prop="note" label="Note"></el-table-column>
        <el-table-column label="Modify" width="180" align="center" v-if="student.role === 'STUDENT'">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">Edit</el-button>
            <el-button plain type="danger" size="mini" @click=del(scope.row.id)>Delete</el-button>
            <el-button plain type="primary" :disabled="scope.row.status !== 'Pending'" @click="handleEdit(scope.row)" size="mini">View</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>


    <el-dialog title="Application Info" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="status" label="Application Status">
          <el-select v-model="status" placeholder="Please Select" style="width: 100%">
            <el-option label="Approved" value="Approved"></el-option>
            <el-option label="Declined" value="Declined"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="note" label="Note">
          <el-input v-model="form.note" autocomplete="off"></el-input>
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
import student from "@/views/manager/Student.vue";

export default {
  name: "Application",
  computed: {
    student() {
      return student
    }
  },
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      dbStatus: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        status: [
          {required: true, message: 'Please Selection Application Status', trigger: 'blur'},
        ],
      },
      ids: [],
      status: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.fromVisible = true
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.status = this.status
          this.form.process = 'Completed'
          this.$request({
            url: this.form.id ? '/application/update' : '/application/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('Success')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/application/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          status: this.dbStatus,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.dbStatus = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>
