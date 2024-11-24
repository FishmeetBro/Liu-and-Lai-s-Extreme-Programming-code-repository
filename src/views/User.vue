<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>
      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      
      <el-upload :action="'http://localhost:9090/user/import'" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>

    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="用户名" width="140"></el-table-column>
      <el-table-column label="用户组名称">
        <template v-slot="scope">
          <span v-if="scope.row.role">{{ roles.find(v => v.flag === scope.row.role) ? roles.find(v => v.flag === scope.row.role).name : ''  }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>

      <el-table-column label="邮箱">
        <template slot-scope="scope">
          <el-tag v-for="item in scope.row.emailList" :key="item">邮箱：{{ item }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="电话">
        <template slot-scope="scope">
          <el-tag v-for="item in scope.row.phoneList" :key="item">电话：{{ item }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="地址">
        <template slot-scope="scope">
          <el-tag v-for="item in scope.row.addressList" :key="item">地址：{{ item }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="社交">
        <template slot-scope="scope">
          <el-tag v-for="item in scope.row.socialList" :key="item">社交：{{ item }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="是否收藏">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.collectFlag">已收藏</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作"  width="280" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-button v-if="!scope.row.collectFlag"   @click="handleCollect(scope.row)" type="primary" plain>收藏 <i class="el-icon-connection"></i></el-button>
          <el-button v-if="scope.row.collectFlag"   @click="handleCollect2(scope.row)" type="warning" plain>取消收藏 <i class="el-icon-connection"></i></el-button>


          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>


          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户组">
          <el-select clearable v-model="form.role" placeholder="请选择用户组" style="width: 100%">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>


        <el-form-item
    v-for="(domain, index) in dynamicValidateForm.phoneList"
    :label="'电话'"
    :key="domain.key"
   
  >
    <el-input v-model="domain.value" style="width: 280px;"></el-input>
    &nbsp;
    <el-button @click.prevent="removeDomainPhone(domain)" ><i class="el-icon-minus" style="color: red;"></i></el-button>
    <el-button @click.prevent="addDomainPhone"><i class="el-icon-plus" style="color: cornflowerblue;"></i></el-button>
    
  </el-form-item>



        <el-form-item
    v-for="(domain, index) in dynamicValidateForm.emailList"
    :label="'邮箱'"
    :key="domain.key"
   
  >
    <el-input v-model="domain.value" style="width: 280px;"></el-input>
    &nbsp;
    <el-button @click.prevent="removeDomainEmail(domain)" ><i class="el-icon-minus" style="color: red;"></i></el-button>
    <el-button @click.prevent="addDomainEmail"><i class="el-icon-plus" style="color: cornflowerblue;"></i></el-button>
    
  </el-form-item>


  <el-form-item
    v-for="(domain, index) in dynamicValidateForm.socialList"
    :label="'社交'"
    :key="domain.key"
   
  >
    <el-input v-model="domain.value" style="width: 280px;"></el-input>
    &nbsp;
    <el-button @click.prevent="removeDomainSocialList(domain)" ><i class="el-icon-minus" style="color: red;"></i></el-button>
    <el-button @click.prevent="addDomainSocialList"><i class="el-icon-plus" style="color: cornflowerblue;"></i></el-button>
    
  </el-form-item>


  <el-form-item
    v-for="(domain, index) in dynamicValidateForm.addressList"
    :label="'地址'"
    :key="domain.key"
   
  >
    <el-input v-model="domain.value" style="width: 280px;"></el-input>
    &nbsp;
    <el-button @click.prevent="removeDomainAddressList(domain)" ><i class="el-icon-minus" style="color: red;"></i></el-button>
    <el-button @click.prevent="addDomainAddressList"><i class="el-icon-plus" style="color: cornflowerblue;"></i></el-button>
    
  </el-form-item>




        <!-- <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>

                <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item> -->

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      email: "",
      address: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      roles: [],
      dynamicValidateForm: {

id:'',

 phoneList: [{
   value: ''
 }],
 emailList: [{
   value: ''
 }],
 addressList: [{
   value: ''
 }],
 socialList: [{
   value: ''
 }],
},
    }
  },
  created() {
    this.load()
  },
  methods: {

    handleCollect(row){
      this.request.get("/user/collect/"+row.id).then(res => {
        //this.roles = res.data
        //收藏成功
        this.$message.success("收藏成功")
        this.load();
      })
    },

    handleCollect2(row){
      this.request.get("/user/cancelcollect/"+row.id).then(res => {
        //this.roles = res.data
        //收藏成功
        this.$message.success("取消收藏成功")
        this.load();
      })
    },

    
    removeDomainPhone(item) {
        var index = this.dynamicValidateForm.phoneList.indexOf(item)
        if (index !== 0) {
          this.dynamicValidateForm.phoneList.splice(index, 1)
        }
      },
      addDomainPhone() {
        for(var i = 0; i < this.dynamicValidateForm.phoneList.length; i++){
          if(this.dynamicValidateForm.phoneList[i].value === null || this.dynamicValidateForm.phoneList[i].value === ''){
            this.$message.error('请输入电话')
            return;
          }
        }
        
        this.dynamicValidateForm.phoneList.push({
          value: '',
          key: Date.now()
        });
      },

    removeDomainEmail(item) {
        var index = this.dynamicValidateForm.emailList.indexOf(item)
        if (index !== 0) {
          this.dynamicValidateForm.emailList.splice(index, 1)
        }
      },
      addDomainEmail() {
        for(var i = 0; i < this.dynamicValidateForm.emailList.length; i++){
          if(this.dynamicValidateForm.emailList[i].value === null || this.dynamicValidateForm.emailList[i].value === ''){
            this.$message.error('请输入邮箱')
            return;
          }
        }
        
        this.dynamicValidateForm.emailList.push({
          value: '',
          key: Date.now()
        });
      },

      removeDomainSocialList(item) {
        var index = this.dynamicValidateForm.socialList.indexOf(item)
        if (index !== 0) {
          this.dynamicValidateForm.socialList.splice(index, 1)
        }
      },
      addDomainSocialList() {
        for(var i = 0; i < this.dynamicValidateForm.socialList.length; i++){
          if(this.dynamicValidateForm.socialList[i].value === null || this.dynamicValidateForm.socialList[i].value === ''){
            this.$message.error('请输入社交账号')
            return;
          }
        }
        
        this.dynamicValidateForm.socialList.push({
          value: '',
          key: Date.now()
        });
      },

      removeDomainAddressList(item) {
        var index = this.dynamicValidateForm.addressList.indexOf(item)
        if (index !== 0) {
          this.dynamicValidateForm.addressList.splice(index, 1)
        }
      },
      addDomainAddressList() {
        for(var i = 0; i < this.dynamicValidateForm.addressList.length; i++){
          if(this.dynamicValidateForm.addressList[i].value === null || this.dynamicValidateForm.addressList[i].value === ''){
            this.$message.error('请输入地址')
            return;
          }
        }
        
        this.dynamicValidateForm.addressList.push({
          value: '',
          key: Date.now()
        });
      },

    load() {
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          email: this.email,
          address: this.address,
        }
      }).then(res => {

        this.tableData = res.data.records
        this.total = res.data.total

      })

      this.request.get("/role").then(res => {
        this.roles = res.data
      })
    },
    save() {

      //将数据取出来，放到list中，然后发送给后端
      var phoneList = [];
      var emailList = [];
      var addressList = [];
      var socialList = [];
      for(var i = 0; i < this.dynamicValidateForm.phoneList.length; i++){
        phoneList.push(this.dynamicValidateForm.phoneList[i].value);
      }
      for(var i = 0; i < this.dynamicValidateForm.emailList.length; i++){
        emailList.push(this.dynamicValidateForm.emailList[i].value);
      }
      for(var i = 0; i < this.dynamicValidateForm.addressList.length; i++){
        addressList.push(this.dynamicValidateForm.addressList[i].value);
      }

      for(var i = 0; i < this.dynamicValidateForm.socialList.length; i++){
        socialList.push(this.dynamicValidateForm.socialList[i].value);
      }

      this.form.phoneList = phoneList;
      this.form.emailList = emailList;
      this.form.addressList = addressList;
      this.form.socialList = socialList;

      console.log("111111111")
      console.log(this.form)
      
      this.request.post("/user", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      console.log("编辑");//这里打印出来的是对象
      console.log(this.form);

      //初始化
      this.dynamicValidateForm.phoneList = [];
      this.dynamicValidateForm.emailList = [];
      this.dynamicValidateForm.addressList = [];
      this.dynamicValidateForm.socialList = [];

      if(this.form.phoneList != null && this.form.phoneList.length > 0){
        for(var i = 0; i < this.form.phoneList.length; i++){
        this.dynamicValidateForm.phoneList.push({value: this.form.phoneList[i]});
      }
      }else{
        this.dynamicValidateForm.phoneList.push({value: ''});
      }

     
      if(this.form.emailList != null && this.form.emailList.length > 0){
        for(var i = 0; i < this.form.emailList.length; i++){
        this.dynamicValidateForm.emailList.push({value: this.form.emailList[i]});
      }
      }else{
        this.dynamicValidateForm.emailList.push({value: ''});
      }


      if(this.form.addressList != null && this.form.addressList.length > 0)
      {

        for(var i = 0; i < this.form.addressList.length; i++){
        this.dynamicValidateForm.addressList.push({value: this.form.addressList[i]});
      }
      }else{
        this.dynamicValidateForm.addressList.push({value: ''});
      }

      if(this.form.socialList != null && this.form.socialList.length > 0)
      {
        for(var i = 0; i < this.form.socialList.length; i++){
        this.dynamicValidateForm.socialList.push({value: this.form.socialList[i]});
      }
      }else{
        this.dynamicValidateForm.socialList.push({value: ''});
      }



      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/user/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/user/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.username = ""
      this.email = ""
      this.address = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    exp() {
      window.open("http://localhost:9090/user/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
