package com.example.controller;

import com.example.common.Result;
import com.example.entity.Application;
import com.example.service.ApplicationService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Resource
    private ApplicationService applicationService;

    @PostMapping("/add")
    public Result add(@RequestBody Application application) {
        applicationService.add(application);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        applicationService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        applicationService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Application application) {
        applicationService.updateById(application);
        return Result.success();
    }


    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Application application = applicationService.selectById(id);
        return Result.success(application);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Application application ) {
        List<Application> list = applicationService.selectAll(application);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Application application,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Application> page = applicationService.selectPage(application, pageNum, pageSize);
        return Result.success(page);
    }

}