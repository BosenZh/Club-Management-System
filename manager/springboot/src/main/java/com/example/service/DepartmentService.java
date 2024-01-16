package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Account;
import com.example.entity.Department;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.DepartmentMapper;
import com.example.mapper.StudentMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private StudentMapper studentMapper;

    public void add(Department department) {
        if(ObjectUtil.isNotEmpty(department.getClubRepid())){
            Department dbDepartment = departmentMapper.selectByClubRepid(department.getClubRepid());
            if(ObjectUtil.isNotEmpty(dbDepartment)){
                throw new CustomException(ResultCodeEnum.REP_ALREADY_EXIST);
            }
        }
        departmentMapper.insert(department);
    }

    public void deleteById(Integer id) {
        departmentMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            departmentMapper.deleteById(id);
        }
    }

    public void updateById(Department department) {
        if(ObjectUtil.isNotEmpty(department.getClubRepid())){
            Department dbDepartment = departmentMapper.selectByClubRepid(department.getClubRepid());
            if(ObjectUtil.isNotEmpty(dbDepartment)){
                Student student = studentMapper.selectById(department.getClubRepid());
                if(!dbDepartment.getClubName().equals(department.getClubName())){
                    throw new CustomException(ResultCodeEnum.REP_ALREADY_EXIST);
                }

            }
        }
        departmentMapper.updateById(department);
    }

    public Department selectById(Integer id) {
        return departmentMapper.selectById(id);
    }

    public List<Department> selectAll(Department department) {
        return departmentMapper.selectAll(department);
    }

    public PageInfo<Department> selectPage(Department department, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser.getRole().equals("STUDENT")) {
            Student student = studentMapper.selectById(currentUser.getId());
            if (student.getTitle().equals("ClubRepresentative")) {
                department.setClubRepid(student.getId());
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Department> list = departmentMapper.selectAll(department);
        return PageInfo.of(list);
    }

}