package com.OmenKi.shortlink.admin.controller;

import com.OmenKi.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Masin_Zhu
 * @Date: 2024/12/16
 * @Description: 短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
}
