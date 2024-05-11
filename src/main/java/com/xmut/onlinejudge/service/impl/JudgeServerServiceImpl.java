package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.JudgeServer;
import com.xmut.onlinejudge.mapper.JudgeServerMapper;
import com.xmut.onlinejudge.service.JudgeServerService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xmut.onlinejudge.entity.table.JudgeServerTableDef.JUDGE_SERVER;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class JudgeServerServiceImpl extends ServiceImpl<JudgeServerMapper, JudgeServer> implements JudgeServerService {

    @Override
    public Long countNormalJudgeServers() {
        List<JudgeServer> list = this.mapper.selectAll();
        return list.stream()
                .filter(judgeServer -> "normal".equals(judgeServer.getStatus()))
                .count();
    }

    @Override
    public JudgeServer getByHostnameAndIp(String hostname, String ip) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(JUDGE_SERVER.HOSTNAME.eq(hostname)).and(JUDGE_SERVER.IP.eq(ip));
        return this.mapper.selectOneByQuery(queryWrapper);
    }

    @Override
    public void deleteByHostnameAndIp(String hostname, String ip) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(JUDGE_SERVER.HOSTNAME.eq(hostname)).and(JUDGE_SERVER.IP.eq(ip));
        this.mapper.deleteByQuery(queryWrapper);
    }
}
