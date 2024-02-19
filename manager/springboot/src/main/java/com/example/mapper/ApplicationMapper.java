package com.example.mapper;

import com.example.entity.Application;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;


public interface ApplicationMapper {
    
    int insert(Application application);


    int deleteById(Integer id);

    int updateById(Application application);

    Application selectById(Integer id);

    List<Application> selectAll(Application application);

    @Select("select * from application where student_id = #{studentId} and department_id = #{departmentId} and (status = 'Application Received' or status = 'Application In Progress')")
    List<Application> selectByStatus(@Param("studentId") Integer studentId, @Param("departmentId") Integer departmentId);

}