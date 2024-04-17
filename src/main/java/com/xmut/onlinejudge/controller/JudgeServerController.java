package com.xmut.onlinejudge.controller;

import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.JudgeServer;
import com.xmut.onlinejudge.service.JudgeServerService;
import com.xmut.onlinejudge.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


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
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<JudgeServer> list() {
        System.out.println(judgeServerService.list());
        return judgeServerService.list();
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
    public Result judgeServerHeartbeat(@RequestBody Map<String, Object> response) {
        JudgeServer judgeServer = new JudgeServer();
        judgeServer.setId(1);//当前系统只设置一台判题机
        judgeServer.setHostname(response.get("hostname").toString());
        judgeServer.setCpuCore(Integer.parseInt(response.get("cpu_core").toString()));
        judgeServer.setMemory(Double.parseDouble(response.get("memory").toString()));
        judgeServer.setCpu(Double.parseDouble(response.get("cpu").toString()));
        judgeServer.setJudgerVersion(response.get("judger_version").toString());
        judgeServer.setLastHeartbeat(DateUtil.getCurrTime());
        judgeServerService.updateById(judgeServer);
        return null;
    }

}
