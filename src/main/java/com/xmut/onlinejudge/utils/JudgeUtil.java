package com.xmut.onlinejudge.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmut.onlinejudge.config.LanguageConfig;
import com.xmut.onlinejudge.entity.Problem;
import com.xmut.onlinejudge.entity.Submission;
import com.xmut.onlinejudge.entity.TaskCase;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Component

public class JudgeUtil {

    private static String token = "TOKEN";
    private String serverBaseUrl;

    JudgeUtil() {
        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");
    }


    private static String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String request(String url, String data) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Judge-Server-Token", sha256(token));
        //创建请求体
        HttpEntity<String> entity = new HttpEntity<>(data, headers);
        //发送请求
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        JSONObject result = JSON.parseObject(responseEntity.getBody());
        System.out.println(result);
        System.out.println(responseEntity.getBody());
        return result.getString("data");
    }

    //    public static JudgeServer ping(String url) {
//        String response=request(url,null);
//        System.out.println(response);
//        JSONObject result = JSON.parseObject(response);
//        JSONObject data = JSON.parseObject(result.getString("data"));
//        JudgeServer judgeServer = new JudgeServer(data);
//        judgeServer.setServiceUrl(url);
//        System.out.println(judgeServer);
//        return judgeServer;
//    }
    public static void judge(Submission submission, Problem problem) {
        JSONObject task = new JSONObject();
        task.put("language_config", LanguageConfig.getConfig(submission.getLanguage()));
        task.put("src", submission.getCode());
        //未设置
        task.put("max_cpu_time", 1000);
        task.put("max_memory", 128 * 1024 * 1024);
        task.put("test_case_id", null);
        TaskCase[] taskCases = new TaskCase[]{
                new TaskCase("1", "1"),
                new TaskCase("2", "2"),
                new TaskCase("3", "3"),
                new TaskCase("4", "4"),
                new TaskCase("5", "5"),
        };
        task.put("test_case", taskCases);
        //通过问题内容设置
//        task.put("max_cpu_time", problem.getTimeLimit());
//        task.put("max_memory", problem.getMemoryLimit());
//        task.put("test_case_id", problem.getTestCaseId());

        task.put("spj_version", null);
        task.put("spj_config", null);
        task.put("spj_compile_config", null);
        task.put("spj_src", null);
        task.put("output", true);
        request("http://192.168.214.134:8080/judge", task.toString());
    }

    public static void main(String[] argc) throws JsonProcessingException {
        JudgeUtil judgeUtil = new JudgeUtil();
        Submission submission = new Submission();
        judgeUtil.handleJudgeTask(submission);
//        judgeService.ping ("http://192.168.214.134:808/ping");
    }

    public String dataToJson(Object data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return data == null ? null : objectMapper.writeValueAsString(data);
    }

    @Async
    public void handleJudgeTask(Submission task) throws JsonProcessingException {
        // 处理判题任务的逻辑，包括编译、运行等步骤
        // 通过异步队列或者消息队列发送判题任务
        // 可以在这里调用判题服务的接口或者方法
        String judgeUrl = "http://192.168.214.134:8080/judge";
        //向判题服务器发送post请求
        RestTemplate restTemplate = new RestTemplate();

        //创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Judge-Server-Token", sha256(token));

        JSONObject taskInfo = new JSONObject();
        taskInfo.put("language_config", LanguageConfig.getConfig("c"));
        taskInfo.put("src", "\n    #include <stdio.h>\n    int main() {\n        int a;\n        scanf(\"%d\", &a);\n        printf(\"%d\\n\", a);\n        return 0;\n    }\n");
        taskInfo.put("max_cpu_time", 1000);
        taskInfo.put("max_memory", 128 * 1024 * 1024);
        taskInfo.put("test_case_id", null);
        TaskCase[] taskCases = new TaskCase[]{
                new TaskCase("1", "1"),
                new TaskCase("2", "2"),
                new TaskCase("3", "3"),
                new TaskCase("4", "4"),
                new TaskCase("5", "5"),
        };
        taskInfo.put("test_case", taskCases);
        taskInfo.put("spj_version", null);
        taskInfo.put("spj_config", null);
        taskInfo.put("spj_compile_config", null);
        taskInfo.put("spj_src", null);
        taskInfo.put("output", true);
        //创建请求体
        HttpEntity<String> entity = new HttpEntity<>(taskInfo.toString(), headers);
        //发送请求
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(judgeUrl, entity, String.class);
        String result = responseEntity.getBody();
        System.out.println(result);
    }
}
