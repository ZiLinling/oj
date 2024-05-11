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
    public Object getValue(String key) {
        OptionsSysoptions optionsSysoptions = this.getOne(OPTIONS_SYSOPTIONS.KEY.eq(key));
        return optionsSysoptions.getValue();
    }

    @Override
    public void updateValue(String key, Object value) {
        OptionsSysoptions option = new OptionsSysoptions();
        option.setValue(value);
        this.update(option, OPTIONS_SYSOPTIONS.KEY.eq(key));
    }
}
