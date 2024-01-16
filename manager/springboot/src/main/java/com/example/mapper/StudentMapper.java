package com.example.mapper;

import com.example.entity.Student;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {

    int insert(Student student);

    int deleteById(Integer id);

    int updateById(Student student);

    Student selectById(Integer id);

    List<Student> selectAll(Student student);

    @Select("select * from studentinfo where username = #{username}")
    Student selectByUsername(String username);

    @Select("select * from studentinfo where title = #{title}")
    List<Student> selectByTitle(String Title);
}