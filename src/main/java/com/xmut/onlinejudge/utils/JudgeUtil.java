package com.xmut.onlinejudge.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmut.onlinejudge.entity.OptionsSysoptions;
import com.xmut.onlinejudge.entity.Problem;
import com.xmut.onlinejudge.entity.Submission;
import com.xmut.onlinejudge.service.OptionsSysoptionsService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private OptionsSysoptionsService optionsSysoptionsService;

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

    public static void main(String[] argc) throws JsonProcessingException {
        JudgeUtil judgeUtil = new JudgeUtil();
        Submission submission = new Submission();
//        judgeUtil.handleJudgeTask(submission);
//        judgeService.ping ("http://192.168.214.134:808/ping");
    }

    public JSONObject getLanguageConfig(String language) {
        JSONArray data;
        if (redisUtil.hasKey("languages")) {
            data = (JSONArray) redisUtil.get("languages");
        } else {
            OptionsSysoptions languages = optionsSysoptionsService.getValue("languages");
            redisUtil.set("languages", languages.getValue(), 60 * 60);
            data = (JSONArray) languages.getValue();
        }
        JSONObject languageConfig = null;
        for (Object item : data) {
            languageConfig = (JSONObject) item;
            if (languageConfig.getString("name").equals(language)) {
                break;
            }
        }
        JSONObject config = languageConfig.getJSONObject("config");
        return config;
    }

    @Async
    public void judge(Submission submission, Problem problem) throws JsonProcessingException {
        // 处理判题任务的逻辑，包括编译、运行等步骤
        // 通过异步队列或者消息队列发送判题任务
        // 可以在这里调用判题服务的接口或者方法
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyPort", "8888");

        String judgeUrl = "http://192.168.214.134:8080/judge";
        //向判题服务器发送post请求
        RestTemplate restTemplate = new RestTemplate();

        //创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Judge-Server-Token", sha256(token));

        JSONObject taskInfo = new JSONObject();
        taskInfo.put("language_config", getLanguageConfig(submission.getLanguage()));
        taskInfo.put("src", submission.getCode());
        taskInfo.put("max_cpu_time", problem.getTimeLimit());
        taskInfo.put("max_memory", problem.getMemoryLimit());
        taskInfo.put("test_case_id", problem.getTestCaseId());
        taskInfo.put("spj_version", problem.getSpjVersion());
        taskInfo.put("spj_config", null);
        taskInfo.put("spj_compile_config", null);
        taskInfo.put("spj_src", problem.getSpjCode());
        taskInfo.put("output", true);
        //创建请求体
        HttpEntity<String> entity = new HttpEntity<>(taskInfo.toString(), headers);
        //发送请求
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(judgeUrl, entity, String.class);
        String result = responseEntity.getBody();
        System.out.println(result);
    }
}
