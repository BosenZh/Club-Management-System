package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.example.common.enums.ApplicationEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Account;
import com.example.entity.Student;
import com.example.entity.Department;
import com.example.entity.Application;
import com.example.exception.CustomException;
import com.example.mapper.StudentMapper;
import com.example.mapper.DepartmentMapper;
import com.example.mapper.ApplicationMapper;
import com.example.utils.TokenUtils;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.LevelEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApplicationService {

    @Resource
    private ApplicationMapper applicationMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private DepartmentMapper departmentMapper;

    public void add(Application application) {
        List<Application> list = applicationMapper.selectByStatus(application.getStudentId(), application.getDepartmentId());
        if (CollectionUtil.isNotEmpty(list)) {
            throw new CustomException(ResultCodeEnum.APPLICATION_ALREADY_EXIST);
        }
        application.setProcess(ApplicationEnum.APPLIED.status);
        application.setStatus(ApplicationEnum.IN_PROGRESS.status);
        applicationMapper.insert(application);
    }

    public void deleteById(Integer id) {
        applicationMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            applicationMapper.deleteById(id);
        }
    }

    public void updateById(Application application) {
        applicationMapper.updateById(application);
    }

    public Application selectById(Integer id) {
        return applicationMapper.selectById(id);
    }


    public List<Application> selectAll(Application application) {
        return applicationMapper.selectAll(application);
    }

    public PageInfo<Application> selectPage(Application application, Integer pageNum, Integer pageSize) {
        Account currentStudent = TokenUtils.getCurrentUser();
        if (RoleEnum.STUDENT.name().equals(currentStudent.getRole())) {
            Student student = studentMapper.selectById(currentStudent.getId());
            if (LevelEnum.ClubRepresentative.title.equals(student.getTitle())) {
                Department department = departmentMapper.selectByClubRepid(student.getId());
                if (ObjectUtil.isNotEmpty(department)) {
                    application.setDepartmentId(department.getId());
                }
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Application> list = applicationMapper.selectAll(application);
        return PageInfo.of(list);
    }

}