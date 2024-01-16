package com.example.mapper;

import com.example.entity.Department;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作department相关数据接口
*/
public interface DepartmentMapper {

    int insert(Department department);

    int deleteById(Integer id);

    int updateById(Department department);

    Department selectById(Integer id);

    List<Department> selectAll(Department department);

    @Select("select * from department where ClubRepid = #{ClubRepid}")
    Department selectByClubRepid(Integer ClubRepid);

}