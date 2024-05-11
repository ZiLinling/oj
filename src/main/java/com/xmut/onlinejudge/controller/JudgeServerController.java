package com.xmut.onlinejudge.controller;

import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.JudgeServer;
import com.xmut.onlinejudge.service.JudgeServerService;
import com.xmut.onlinejudge.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;


/**
 * 控制层。
 *
 * @author Zi
 * @since 2024-03-05
 */
@RestController
@RequestMapping("/judgeServer")
public class JudgeServerController {

    @Autowired
    private JudgeServerService judgeServerService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 添加。
     *
     * @param url
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("add")
    public Result<JudgeServer> save(@RequestBody String url) {
        Result<JudgeServer> result = new Result<>();
//        JudgeServer judgeServer= JudgeUtil.ping(url);
//        judgeServer.setCreateTime(DateUtil.getCurrTime());
//        if (judgeServerService.save(judgeServer)) {
//            result.success("添加成功");
//            result.setData(judgeServer);
//        } else {
//            result.fail("添加失败");
//        }
        return result;
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return judgeServerService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param judgeServer
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody JudgeServer judgeServer) {
        return judgeServerService.updateById(judgeServer);
    }


    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public JudgeServer getInfo(@PathVariable Serializable id) {
        return judgeServerService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<JudgeServer> page(Page<JudgeServer> page) {
        return judgeServerService.page(page);
    }

    @PostMapping("/judge_server_heartbeat")
    public void judgeServerHeartbeat(@RequestBody JSONObject response) {
        JudgeServer getter = judgeServerService.getByHostnameAndIp(response.getString("hostname"), request.getRemoteAddr());
        JudgeServer judgeServer = new JudgeServer(null, request.getRemoteAddr(), response.getString("hostname"),
                response.getString("judger_version"), response.getInteger("cpu_core"),
                response.getDouble("memory"), response.getDouble("cpu"), DateUtil.getCurrTime(),
                DateUtil.getCurrTime(), 0, response.getString("service_url"),
                false, null);
        if (getter != null) {
            judgeServer.setId(getter.getId());
            judgeServer.setTaskNumber(getter.getTaskNumber());
            judgeServer.setCreateTime(getter.getCreateTime());
            judgeServer.setIsDisabled(getter.getIsDisabled());
        }
        judgeServerService.saveOrUpdate(judgeServer);
    }

    @GetMapping("")
    public Result<List<JudgeServer>> list() {
        Result<List<JudgeServer>> result = new Result<>();
        List<JudgeServer> serverList = judgeServerService.list();
        result.success(serverList, "查询成功");
        return result;
    }

    @PutMapping("")
    public Result<JudgeServer> updateById(@RequestBody JudgeServer server) {
        Result<JudgeServer> result = new Result<>();
        if (judgeServerService.updateById(server)) {
            result.success(null, "更新成功");
        } else {
            result.error("更新失败");
        }
        return result;
    }

    @DeleteMapping("")
    public Result<JudgeServer> removeByHostnameAndIp(String hostname, String ip) {
        Result<JudgeServer> result = new Result<>();
        judgeServerService.deleteByHostnameAndIp(hostname, ip);
        result.success(null, "删除成功");
        return result;
    }

}
