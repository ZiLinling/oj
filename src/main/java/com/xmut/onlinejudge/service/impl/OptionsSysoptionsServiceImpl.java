package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.OptionsSysoptions;
import com.xmut.onlinejudge.mapper.OptionsSysoptionsMapper;
import com.xmut.onlinejudge.service.OptionsSysoptionsService;
import org.springframework.stereotype.Service;

import static com.xmut.onlinejudge.entity.table.OptionsSysoptionsTableDef.OPTIONS_SYSOPTIONS;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class OptionsSysoptionsServiceImpl extends ServiceImpl<OptionsSysoptionsMapper, OptionsSysoptions> implements OptionsSysoptionsService {

    @Override
    public OptionsSysoptions getValue(String key) {
        return this.getOne(OPTIONS_SYSOPTIONS.KEY.eq(key));
    }

}
