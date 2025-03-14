package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;
    @Resource
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


    /**
     * 新增员工
     *
     * @return
     *
     */
    @PostMapping
   public Result save(@RequestBody EmployeeDTO employeeDTO){
        System.out.println("拦截器里的线程id"+Thread.currentThread().getId());
        employeeService.save(employeeDTO);
       return Result.success();
   }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
   @GetMapping("/page")
   public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
       log.info("员工分页查询，参数为{}",employeePageQueryDTO);
       PageResult pageResult= employeeService.pageQuery(employeePageQueryDTO);

        return Result.success(pageResult);

   }
   @PostMapping("/status/{status}")
   public Result updateStatus(@PathVariable Integer status,@RequestParam Long id){
       employeeService.updateStatus(status,id);
       return Result.success();
   }

    /**
     * 根据id拿到编辑对象
     * @param id
     * @return
     */
    @GetMapping("/{id}")
   public Result<Employee> getUserById(@PathVariable Long id){
        Employee employee = employeeService.getUserById(id);
        return Result.success(employee);
    }

    /**
     * 修改员工信息
     * @param employeeDTO
     * @return
     */
    @PutMapping
    public Result editPerson(@RequestBody EmployeeDTO employeeDTO){
        employeeService.editPerson(employeeDTO);
        return Result.success();
    }
}
