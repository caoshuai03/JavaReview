package com.hmdp.controller;


import com.hmdp.dto.Result;
import com.hmdp.service.IFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author caoshuai
 * @version 1.1
 */
@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private IFollowService followService;

    @PutMapping("/{id}/{isFollow}")
    public Result follow(@PathVariable("id") Long folloewUserId, @PathVariable("isFollow") Boolean isFollow) {
        return followService.follow(folloewUserId, isFollow);
    }

    @PutMapping("/or/not/{id}")
    public Result follow(@PathVariable("id") Long folloewUserId) {
        return followService.isFollow(folloewUserId);

    }

    @GetMapping("/common/{id}")
    public Result followCommons(@PathVariable("id") Long id) {
        return followService.followCommons(id);
    }


}
