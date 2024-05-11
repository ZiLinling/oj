package com.xmut.onlinejudge.controller;

import com.xmut.onlinejudge.service.ProblemTagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制层。
 *
 * @author Zi
 * @since 2024-03-05
 */
@RestController
@RequestMapping("/problemTags")
public class ProblemTagsController {

    @Autowired
    private ProblemTagsService problemTagsService;


}
