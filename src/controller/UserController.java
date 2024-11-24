package com.farmland.intel.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.farmland.intel.common.Constants;
import com.farmland.intel.common.Result;
import com.farmland.intel.config.interceptor.AuthAccess;
import com.farmland.intel.controller.dto.UserDTO;
import com.farmland.intel.controller.dto.UserPasswordDTO;
import com.farmland.intel.entity.User;
import com.farmland.intel.entity.UserExcel;
import com.farmland.intel.exception.ServiceException;
import com.farmland.intel.service.IUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${files.upload.path}")
    private String filesUploadPath;

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        UserDTO dto = userService.login(userDTO);
        return Result.success(dto);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        userDTO.setNickname(userDTO.getUsername());
        return Result.success(userService.register(userDTO));
    }

     // 新增或者更新
    @PostMapping
    public Result save(@RequestBody User user) {

        String email = JSON.toJSONString(user.getEmailList());
        user.setEmail(email);

        String phone = JSON.toJSONString(user.getPhoneList());
        user.setPhone(phone);

        String address = JSON.toJSONString(user.getAddressList());
        user.setAddress(address);

        String social = JSON.toJSONString(user.getSocialList());
        user.setSocial(social);

        String username = user.getUsername();
        if (StrUtil.isBlank(username)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        if (StrUtil.isBlank(user.getNickname())) {
            user.setNickname(username);
        }
        if (user.getId() != null) {
            user.setPassword(null);
        } else {
            if (user.getPassword() == null) {
                user.setPassword("123");
            }
        }
        return Result.success(userService.saveOrUpdate(user));
    }
    /**
     * 修改密码
     *
     * @param userPasswordDTO
     * @return
     */
    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.updatePassword(userPasswordDTO);
        return Result.success();
    }

    @AuthAccess
    @PutMapping("/reset")
    public Result reset(@RequestBody UserPasswordDTO userPasswordDTO) {
        if (StrUtil.isBlank(userPasswordDTO.getUsername()) || StrUtil.isBlank(userPasswordDTO.getPhone())) {
            throw new ServiceException("-1", "参数异常");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userPasswordDTO.getUsername());
        queryWrapper.eq("phone", userPasswordDTO.getPhone());
        List<User> list = userService.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            User user = list.get(0);
            user.setPassword("123");
            userService.updateById(user);
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String address) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            queryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
            queryWrapper.like("address", address);
        }

        Page<User> page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<User> records = page.getRecords();
        for (User record : records) {
            record.setEmailList(JSON.parseArray(record.getEmail(),String.class));
            record.setPhoneList(JSON.parseArray(record.getPhone(),String.class));
            record.setSocialList(JSON.parseArray(record.getSocial(),String.class));
            record.setAddressList(JSON.parseArray(record.getAddress(),String.class));
        }

        System.out.println("1111111");
        System.out.println(records);


        return Result.success(page);
    }

    @GetMapping("/page2")
    public Result findPages2(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String address) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            queryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
            queryWrapper.like("address", address);
        }

        queryWrapper.eq("collect_flag", true);

        Page<User> page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<User> records = page.getRecords();
        for (User record : records) {
            record.setEmailList(JSON.parseArray(record.getEmail(),String.class));
            record.setPhoneList(JSON.parseArray(record.getPhone(),String.class));
            record.setSocialList(JSON.parseArray(record.getSocial(),String.class));
            record.setAddressList(JSON.parseArray(record.getAddress(),String.class));
        }

        System.out.println("1111111");
        System.out.println(records);


        return Result.success(page);


    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        List<UserExcel> list2 = new ArrayList<>();
        for (User user : list) {
            UserExcel userExcel = new UserExcel();
            userExcel.setId(user.getId());
            userExcel.setUsername(user.getUsername());
            userExcel.setPassword(user.getPassword());
            userExcel.setNickname(user.getNickname());
            userExcel.setEmail(user.getEmail());
            userExcel.setPhone(user.getPhone());
            userExcel.setSocial(user.getSocial());
            userExcel.setAddress(user.getAddress());
            userExcel.setCreateTime(user.getCreateTime());
            userExcel.setAvatarUrl(user.getAvatarUrl());
            userExcel.setRole(user.getRole());
            userExcel.setCollectFlag(user.getCollectFlag());

            list2.add(userExcel);
        }



        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id", "ID");
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("social", "社交");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像");
        writer.addHeaderAlias("role", "角色");
        writer.addHeaderAlias("collectFlag", "是否收藏");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list2, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户通讯录信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     * excel 导入
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容

        //过滤如果名称重复不再添加
        List<User> listt = userService.list();
        HashMap<String,String> hashMap = new HashMap<>();
        for (User user : listt) {
            hashMap.put(user.getUsername(),user.getUsername());
        }

        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            if(hashMap.containsKey(row.get(1).toString())){
                continue;
            }
            User user = new User();
            user.setUsername(row.get(1).toString());
            user.setPassword(row.get(2).toString());
            user.setNickname(row.get(3).toString());
            user.setEmail(row.get(4).toString());
            user.setPhone(row.get(5).toString());
            user.setSocial(row.get(6).toString());
            user.setAddress(row.get(7).toString());
            user.setAvatarUrl(row.get(9).toString());
            user.setRole(row.get(10).toString());
            String f = row.get(11).toString();
            if(f==null || "".equals(f) || f.equals("false")){
                user.setCollectFlag(false);
            }else {
                user.setCollectFlag(true);
            }

            users.add(user);
        }

        userService.saveBatch(users);
        return Result.success(true);
    }


    /**
     * 导出接口
     */
    @GetMapping("/export2")
    public void export2(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        List<UserExcel> list2 = new ArrayList<>();
        for (User user : list) {
            if(user.getCollectFlag() != null && !"".equals(user.getCollectFlag()) && user.getCollectFlag()){
                UserExcel userExcel = new UserExcel();
                userExcel.setId(user.getId());
                userExcel.setUsername(user.getUsername());
                userExcel.setPassword(user.getPassword());
                userExcel.setNickname(user.getNickname());
                userExcel.setEmail(user.getEmail());
                userExcel.setPhone(user.getPhone());
                userExcel.setSocial(user.getSocial());
                userExcel.setAddress(user.getAddress());
                userExcel.setCreateTime(user.getCreateTime());
                userExcel.setAvatarUrl(user.getAvatarUrl());
                userExcel.setRole(user.getRole());
                userExcel.setCollectFlag(user.getCollectFlag());
                list2.add(userExcel);
            }

        }



        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id", "ID");
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("social", "社交");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像");
        writer.addHeaderAlias("role", "角色");
        writer.addHeaderAlias("collectFlag", "是否收藏");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list2, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户通讯录信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     * excel 导入
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/import2")
    public Result imp2(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容

        //过滤如果名称重复不再添加
        List<User> listt = userService.list();
        HashMap<String,String> hashMap = new HashMap<>();
        for (User user : listt) {
            hashMap.put(user.getUsername(),user.getUsername());
        }

        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            String f = row.get(11).toString();
            if(f==null || "".equals(f) || f.equals("false")){
                user.setCollectFlag(false);
            }else {
                user.setCollectFlag(true);
            }
            if(hashMap.containsKey(row.get(1).toString()) || !user.getCollectFlag()){
                continue;
            }

            user.setUsername(row.get(1).toString());
            user.setPassword(row.get(2).toString());
            user.setNickname(row.get(3).toString());
            user.setEmail(row.get(4).toString());
            user.setPhone(row.get(5).toString());
            user.setSocial(row.get(6).toString());
            user.setAddress(row.get(7).toString());
            user.setAvatarUrl(row.get(9).toString());
            user.setRole(row.get(10).toString());


            users.add(user);
        }

        userService.saveBatch(users);
        return Result.success(true);
    }

    //收藏
    @GetMapping("/collect/{id}")
    public Result collect(@PathVariable Integer id) {


        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(User::getCollectFlag,true)
                .eq(User::getId,id);

        userService.update(null,updateWrapper);

        return Result.success(null);
    }

    //取消收藏
    @GetMapping("/cancelcollect/{id}")
    public Result cancelcollect(@PathVariable Integer id) {

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(User::getCollectFlag,false)
                .eq(User::getId,id);

        userService.update(null,updateWrapper);

        return Result.success(null);
    }
}

