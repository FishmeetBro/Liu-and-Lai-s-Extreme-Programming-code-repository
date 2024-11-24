<template>
  <el-dialog :title="title" :visible.sync="visible" :close-on-click-modal="false" @opened="openFun">
    <el-form :model="dynamicValidateForm" ref="dynamicValidateForm" label-width="100px" class="demo-dynamic">

  <el-form-item
    v-for="(domain, index) in dynamicValidateForm.phoneList"
    :label="'电话' + index"
    :key="domain.key"
    :prop="'phoneList.' + index + '.value'"
    :rules="{
      required: true, message: '电话不能为空', trigger: 'blur'
    }"
  >
    <el-input v-model="domain.value"></el-input><el-button @click.prevent="removeDomainPhone(domain)">删除</el-button>
    <el-button @click.prevent="addDomainPhone">新增</el-button>
    
  </el-form-item>

  <el-form-item
    v-for="(domain, index) in dynamicValidateForm.emailList"
    :label="'邮箱' + index"
    :key="domain.key"
    :prop="'emailList.' + index + '.value'"
    :rules="{
      required: true, message: '邮箱不能为空', trigger: 'blur'
    }"
  >
    <el-input v-model="domain.value"></el-input><el-button @click.prevent="removeDomainEmail(domain)">删除</el-button>
    <el-button @click.prevent="addDomainEmail">新增</el-button>
    
  </el-form-item>

  <el-form-item
    v-for="(domain, index) in dynamicValidateForm.socialList"
    :label="'社交账号' + index"
    :key="domain.key"
    :prop="'socialList.' + index + '.value'"
    :rules="{
      required: true, message: '社交账号不能为空', trigger: 'blur'
    }"
  >
    <el-input v-model="domain.value"></el-input><el-button @click.prevent="removeDomainSocial(domain)">删除</el-button>
    <el-button @click.prevent="addDomainSocial">新增</el-button>
    
  </el-form-item>

  <el-form-item
    v-for="(domain, index) in dynamicValidateForm.addressList"
    :label="'地址' + index"
    :key="domain.key"
    :prop="'addressList.' + index + '.value'"
    :rules="{
      required: true, message: '地址不能为空', trigger: 'blur'
    }"
  >
    <el-input v-model="domain.value"></el-input><el-button @click.prevent="removeDomainAddress(domain)">删除</el-button>
    <el-button @click.prevent="addDomainAddress">新增</el-button>
    
  </el-form-item>


</el-form>
    <span slot="footer">
      <el-button type="primary" @click="submitForm('dynamicValidateForm')">提交</el-button>
    <el-button @click="resetForm('dynamicValidateForm')">重置</el-button>
    </span>
  </el-dialog>
</template>

<script>


export default {
  name: "relevance",
  props: {
    dialogVisible: {
      type: Boolean,
      require: true,
      default: false
    },
    userObj: Object
  },
  computed: {
    visible: {
      get: function () {
        return this.dialogVisible
      },
      set: function (val) {
        this.$emit('update:dialogVisible', val)
      }
    }
  },
  data(){
    return{
      title: '新增关联信息',
      isLoading: false,

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

        dynamicValidateForm2: {

id:'',

 phoneList: [],
 emailList: [],
 addressList: [],
 socialList: [],
},



    }
  },
  methods: {

    openFun(){
      console.log("打开")
      this.dynamicValidateForm.id = this.userObj.id;
      console.log(this.userObj)

      //遍历赋值
      if(this.userObj.infosDTO.phoneList){
        
        this.dynamicValidateForm.phoneList = []
        for(var i = 0; i < this.userObj.infosDTO.phoneList.length; i++){
          this.dynamicValidateForm.phoneList.push({value:this.userObj.infosDTO.phoneList[i]});
        }
      }

      if(this.userObj.infosDTO.emailList){
        this.dynamicValidateForm.emailList = []
        for(var i2 = 0; i2 < this.userObj.infosDTO.emailList.length; i2++){
          this.dynamicValidateForm.emailList.push({value:this.userObj.infosDTO.emailList[i2]});
        }
      }

      if(this.userObj.infosDTO.addressList){
        this.dynamicValidateForm.addressList = []
        for(var i3 = 0; i3 < this.userObj.infosDTO.addressList.length; i3++){
          this.dynamicValidateForm.addressList.push({value:this.userObj.infosDTO.addressList[i3]});
        }
      }

      if(this.userObj.infosDTO.socialList){
        this.dynamicValidateForm.socialList = []
        for(var i4 = 0; i4 < this.userObj.infosDTO.socialList.length; i4++){
          this.dynamicValidateForm.socialList.push({value:this.userObj.infosDTO.socialList[i4]});
        }
      }
      

      if (this.userObj.id){

        this.title = '编辑关联信息'
        this.userForm = this.userObj
        //this.userForm.roleIds = this.userForm.roleIds[0].split(',')
        //  将roleIds中的字符串数字转化为数字
        //this.userForm.roleIds = this.userForm.roleIds.map(Number)
      }
    },

    submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!');
            console.log("提交")
            console.log(this.dynamicValidateForm)

            //把数据都遍历到dynamicValidateForm2
            this.dynamicValidateForm2.id = this.dynamicValidateForm.id

            this.dynamicValidateForm2.phoneList = []
            this.dynamicValidateForm2.emailList = []
            this.dynamicValidateForm2.addressList = []
            this.dynamicValidateForm2.socialList = []

            for(var i = 0; i < this.dynamicValidateForm.phoneList.length; i++){
              this.dynamicValidateForm2.phoneList.push(this.dynamicValidateForm.phoneList[i].value);
        
              }

              for(var i2 = 0; i2 < this.dynamicValidateForm.emailList.length; i2++){
              this.dynamicValidateForm2.emailList.push(this.dynamicValidateForm.emailList[i2].value);
        
              }

              for(var i3 = 0; i3 < this.dynamicValidateForm.addressList.length; i3++){
              this.dynamicValidateForm2.addressList.push(this.dynamicValidateForm.addressList[i3].value);
        
              }

              for(var i4 = 0; i4 < this.dynamicValidateForm.socialList.length; i4++){
              this.dynamicValidateForm2.socialList.push(this.dynamicValidateForm.socialList[i4].value);
        
              }

              console.log("最好提交是的：")
            console.log(this.dynamicValidateForm2)

           

            editInfos(this.dynamicValidateForm2).then(res => {
              console.log(res)
              this.resetForm('dynamicValidateForm')
            if (res.success){
              successMsg(res.data)
              this.visible = false
              this.$emit('get-list')
            } else {
              errorMsg(res.msg)
            }
            this.isLoading = false
          })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      removeDomainPhone(item) {
        var index = this.dynamicValidateForm.phoneList.indexOf(item)


        if (index !== -1) {
          this.dynamicValidateForm.phoneList.splice(index, 1)
        }
      },
      addDomainPhone() {

        //判断是否为空为空不能再添加，全部遍历
        for(var i = 0; i < this.dynamicValidateForm.phoneList.length; i++){
          if(this.dynamicValidateForm.phoneList[i].value === null || this.dynamicValidateForm.phoneList[i].value === ''){
            this.$message.error('请输入手机号')
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
        if (index !== -1) {
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

      removeDomainAddress(item) {
        var index = this.dynamicValidateForm.addressList.indexOf(item)
        if (index !== -1) {
          this.dynamicValidateForm.addressList.splice(index, 1)
        }
      },

      addDomainAddress() {

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

      removeDomainSocial(item) {
        var index = this.dynamicValidateForm.socialList.indexOf(item)
        if (index !== -1) {
          this.dynamicValidateForm.socialList.splice(index, 1)
        }
      },
      addDomainSocial() {

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
  }
}
</script>

<style scoped>
 ::v-deep .vue-treeselect__control{
  height: 28px;
}
 ::v-deep .el-form-item__content{
   line-height: 28px;
   font-size: 12px;
 }
</style>