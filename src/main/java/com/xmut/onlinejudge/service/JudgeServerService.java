package com.xmut.onlinejudge.service;

import com.mybatisflex.core.service.IService;
import com.xmut.onlinejudge.entity.JudgeServer;

/**
 * 服务层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface JudgeServerService extends IService<JudgeServer> {

    Long countNormalJudgeServers();

    JudgeServer getByHostnameAndIp(String hostname, String ip);

    void deleteByHostnameAndIp(String hostname, String ip);

}
