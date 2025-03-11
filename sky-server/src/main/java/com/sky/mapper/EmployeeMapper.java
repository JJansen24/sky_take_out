package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    Employee getByUsername(String username);

    void insert(Employee employee);

    /**
     * 查询数据库操作
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新操作
     * @param employee
     */
    void updateStatus(Employee employee);

    /**
     * 数据库获取人员
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id}")
    Employee getUserById(Long id);
}
