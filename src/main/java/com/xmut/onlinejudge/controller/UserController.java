package com.xmut.onlinejudge.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.User;
import com.xmut.onlinejudge.service.OptionsSysoptionsService;
import com.xmut.onlinejudge.service.UserService;
import com.xmut.onlinejudge.utils.DateUtil;
import com.xmut.onlinejudge.utils.EmailUtil;
import com.xmut.onlinejudge.utils.JwtUtil;
import com.xmut.onlinejudge.utils.RedisUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 控制层。
 *
 * @author Zi
 * @since 2024-03-03
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OptionsSysoptionsService optionsSysoptionsService;



    @PostMapping("login")
    public Result<String> login(@RequestBody User user) {
        Result<String> result = new Result<String>();
        User getter = userService.getByUsernameWithPassword(user.getUsername());
        if (getter != null) {
            if (getter.getPassword().equals(user.getPassword())) {
                if (getter.getIsDisabled()) {
                    result.error("你的账号已被禁用");
                } else {
                    result.success(JwtUtil.generateToken(getter), "登录成功");
                }
            } else {
                result.error("密码错误");
            }
        } else {
            result.error("用户名不存在");
        }
        return result;
    }

    @GetMapping("logout")
    public Result<String> logout() {
        Result<String> result = new Result<String>();
        String token = request.getHeader("token");
        if (JwtUtil.verifyToken(token)) {
            Integer userId = JwtUtil.getUserId(token);
            User user = userService.getById(userId);
            user.setLastLogin(DateUtil.getCurrTime());
            userService.updateById(user);
        }
        result.success(null, "登出成功");
        return result;
    }

    @PostMapping("register")
    public Result<User> register(@RequestBody String data) {
        Result<User> result = new Result<User>();
        //将data中的user提取出来
        User user = JSON.parseObject(data, User.class);
        JSONObject jsonObject = JSONObject.parseObject(data);
        //将data中的captcha提取出来
        String captcha = jsonObject.getString("captcha");
        if (redisUtil.hasKey(user.getEmail()) && redisUtil.get(user.getEmail()).equals(captcha)) {
            //设置创建时间
            user.setCreateTime(DateUtil.getCurrTime());
            //保存用户
            userService.save(user);
            //并创建用户档案
            result.success(null, "注册成功");
        } else {
            result.error("验证码错误");
        }
        return result;
    }

    //生成邮箱验证码
    @GetMapping("captcha")
    public Result<String> sendEmailCode(@RequestParam String email) throws Exception {
        Result<String> result = new Result<String>();
        if (redisUtil.get(email) != null) {
            result.error("验证码尚未超时,请勿频繁发送");
        } else {
            //生成6位String类型验证码
            String captcha = RandomStringUtils.randomAlphanumeric(6);
            JSONObject smtpConfig = (JSONObject) optionsSysoptionsService.getValue("smtp_config");
            //发送邮件
            emailUtil.registerEmail(smtpConfig.getString("email"), email, captcha);
            redisUtil.set(email, captcha, 60 * 5);
            result.success(null, "发送验证码成功");
        }
        return result;
    }

    @PostMapping("check_username_or_email")
    public Result<JSONObject> checkUsernameOrEmail(@RequestBody JSONObject data) {
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject judge = new JSONObject();
        String username = data.getString("username");
        String email = data.getString("email");
        judge.put("username", false);
        judge.put("email", false);
        if (username != null && userService.isUsernameExist(username)) {
            //用户名重复
            judge.replace("username", true);
        } else if (email != null && userService.isEmailExist(email)) {
            //邮箱重复
            judge.replace("email", true);
        }
        result.success(judge, "判断用户名或邮箱是否重复");
        return result;
    }

    @GetMapping("list")
    public Result<Page<User>> page(Integer limit, Integer page, @RequestParam(required = false) String keyword) {
        Result<Page<User>> result = new Result<>();
        Page<User> submissionPage = userService.page(page, limit, keyword);
        result.success(submissionPage, "查询成功");
        return result;
    }

    @GetMapping("")
    public Result<User> getById(Integer id) {
        Result<User> result = new Result<>();
        User user = userService.getById(id);
        if (user != null) {
            result.success(user, "查询成功");
        } else {
            result.error("该用户不存在");
        }
        return result;
    }

    @PostMapping("")
    public Result<User> addUserList(@RequestBody List<User> users) {
        Result<User> result = new Result<>();
        try {
            userService.saveBatch(users);
            result.success(null, "新增成功");
        } catch (Exception e) {
            result.error("新增失败,请检查用户名或者邮箱是否重复");
        }
        return result;
    }

    @DeleteMapping("")
    public Result<User> removeByIds(@RequestParam("id") String ids) {
        Result<User> result = new Result<>();
        //ids的格式为id,id,id,id...
        List<Integer> idList = Arrays.stream(ids.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        userService.getMapper().deleteBatchByIds(idList);
        result.success(null, "删除成功");
        return result;
    }

    @PutMapping("")
    public Result<User> updateById(@RequestBody User data) {
        Result<User> result = new Result<>();
        User user = userService.getById(data.getId());
        if (user == null) {
            result.error("该用户不存在");
            return result;
        }
        if (userService.isUsernameExist(data.getUsername(), data.getId())) {
            result.error("用户名已存在");
            return result;
        }
        if (userService.isEmailExist(data.getEmail(), data.getId())) {
            result.error("邮箱已存在");
            return result;
        }
        userService.updateById(data);
        result.success(null, "更新成功");
        return result;
    }

    @PostMapping("generate_user")
    public Result<String> generateUser(@RequestBody JSONObject limit) {
        Integer from = limit.getInteger("number_from");
        Integer to = limit.getInteger("number_to");
        Integer length = limit.getInteger("password_length");
        String prefix = limit.getString("prefix");
        String suffix = limit.getString("suffix");
        Result<String> result = new Result<>();
        List<User> userList = new ArrayList<>();
        String fileId = RandomStringUtils.randomAlphanumeric(8);
        try (FileOutputStream fos = new FileOutputStream("data/tmp/" + fileId + ".xlsx")) {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Users");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Username");
            headerRow.createCell(1).setCellValue("Password");
            int rowNum = 1;
            for (int i = from; i <= to; i++) {
                //根据密码长度生成随机密码
                String rawPassword = RandomStringUtils.randomAlphanumeric(length);
                //对密码进行md5加密两次
                String encryptPassword = DigestUtils.md5Hex(DigestUtils.md5Hex(rawPassword));
                String username = prefix + i + suffix;
                User user = new User(username, encryptPassword);
                userList.add(user);
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(username);
                row.createCell(1).setCellValue(rawPassword);
            }
            workbook.write(fos);
            workbook.close();
            userService.saveBatch(userList);
            result.success(fileId, "生成成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.error("生成失败,用户名可能存在重复");
        }
        return result;
    }

    @GetMapping("generate_user")
    public Object generateUser(String fileId) throws IOException {
        Result<String> result = new Result<>();
        if (fileId == null || fileId.isEmpty()) {
            result.error("Invalid Parameter, file_id is required");
            return result;
        }
        if (!fileId.matches("^[a-zA-Z0-9]+$")) {
            result.error("Illegal file_id");
            return result;
        }
        String filePath = "data/tmp/" + fileId + ".xlsx";
        File file = new File(filePath);
        if (!file.exists()) {
            result.error("File not found");
            return result;
        }
        Path path = Paths.get(filePath);
        byte[] raw_data = Files.readAllBytes(path);
        Files.deleteIfExists(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "users.xlsx");
        return new ResponseEntity<>(raw_data, headers, HttpStatus.OK);
    }


}
