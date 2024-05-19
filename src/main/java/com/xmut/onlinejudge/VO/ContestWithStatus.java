package com.xmut.onlinejudge.VO;

import com.mybatisflex.annotation.Table;
import com.xmut.onlinejudge.entity.Contest;
import lombok.Data;

@Data
@Table(value = "contest_with_status")
public class ContestWithStatus extends Contest {

    private Integer status;

    private String creator;
}
