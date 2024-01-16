<template>
  <div>
    <div class="search">
      <el-input placeholder="Please enter club name" style="width: 200px" v-model="clubName"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">Search</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">Reset</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">Insert</el-button>
      <el-button type="danger" plain @click="delBatch">Delete</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="id" width="80" align="center" sortable></el-table-column>
        <el-table-column label="logo">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 40px; height: 40px; border-radius: 50%" v-if="scope.row.logo"
                        :src="scope.row.logo" :preview-src-list="[scope.row.logo]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="clubName" label="Name" show-overflow-tooltip></el-table-column>
        <<el-table-column prop="description" label="Club Description" show-overflow-tooltip>
        <template v-slot="scope">
          <el-button type="success" @click="viewEditor(scope.row.description)">View Description</el-button>
        </template>
      </el-table-column>
        <el-table-column prop="studentName" label="ClubRepresentative"></el-table-column>

        <el-table-column label="Modify" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">Edit</el-button>
            <el-button plain type="danger" size="mini" @click=del(scope.row.id)>Delete</el-button>
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


    <el-dialog title="info" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false" destroy-on-close @close="cancel">
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item label="logo">
          <el-upload
              class="logo-uploader"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleLogoSuccess"
          >
            <el-button type="primary">upload your club logo</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="clubName" label="Club Name">
          <el-input v-model="form.clubName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="description" label="Description">
          <div id = "editor"></div>
        </el-form-item>
        <el-form-item prop="clubRepid" label="Club Rep">
<!--          <el-select v-model="form.clubRepid" placeholder = "Please select your club rep" style = "width: 100%">-->
<!--            <el-option v-for = "item in repData" :label = "item.name" :value = "item.id"></el-option>-->
<!--          </el-select>-->
          <el-input v-model="form.clubRepid" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Apply</el-button>
      </div>
    </el-dialog>
    <el-dialog title="Club Description" :visible.sync="editorVisible" width="50%">
      <div v-html="this.viewData" class="w-e-text"></div>
    </el-dialog>


  </div>
</template>

<script>
import Editor from 'wangeditor'

let editor
function initWangEditor(content) {	setTimeout(() => {
  if (!editor) {
    editor = new Editor('#editor')
    editor.config.placeholder = 'Content'
    editor.config.uploadFileName = 'file'
    editor.config.uploadImgServer = 'http://localhost:9090/files/wang/upload'
    editor.create()
  }
  editor.txt.html(content)
}, 0)
}
export default {
  name: "Department",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      clubName: null,
      editorVisible: false,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        clubName: [
          {required: true, message: 'Please enter club name', trigger: 'blur'},
        ],
        clubRepid: [
          {required: true, message: 'Please enter your club rep', trigger: 'blur'},
        ]
      },
      ids: [],
      repData: [],
      viewData: null,
    }
  },
  created() {
    this.load(1)
    this.loadReps()
  },
  methods: {
    viewEditor(description) {
      this.viewData = description
      this.editorVisible = true
    },
    loadReps(){
      this.$request.get("/student/selectAll").then(res=> {
        if (res.code === '200'){
          this.repData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {
      this.form = {}
      initWangEditor('')
      this.fromVisible = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      initWangEditor(this.form.description || '')
      this.fromVisible = true
    },
    cancel() {
      this.fromVisible = false
      location.href = '/department'
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.description = editor.txt.html()
          this.$request({
            url: this.form.id ? '/department/update' : '/department/add',
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
    del(id) {
      this.$confirm('Confirm Delete', 'Delete', {type: "warning"}).then(response => {
        this.$request.delete('/department/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('Success')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('Select Data')
        return
      }
      this.$confirm('Please confirm delete', 'Delete', {type: "warning"}).then(response => {
        this.$request.delete('/department/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {
            this.$message.success('Success')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/department/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          clubName: this.clubName,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.clubName = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleLogoSuccess(response, file, fileList) {
      this.form.logo = response.data
    },
  }
}
</script>

<style scoped>

</style>
