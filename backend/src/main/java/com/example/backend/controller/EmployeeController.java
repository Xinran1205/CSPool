package com.example.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.BaseContext;
import com.example.backend.common.RestResult;
import com.example.backend.domain.Employee;
import com.example.backend.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/2/26 16:37
 */

@RestController
@CrossOrigin
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /***
    * @description 员工登录
    * @param request
     * @param employee
    * @return
    * @author
    * @date
    */
    @PostMapping("/login")
    public RestResult<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //因为我们是要接收json格式的参数，所以得使用RequestBody注解
        //将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //如果没有查询到则返回登录失败结果
        if(emp == null){
            return RestResult.error("登录失败",0);
        }

        //密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return RestResult.error("登录失败",0);
        }

        //查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return RestResult.error("账号已禁用",0);
        }

        //登录成功，将员工id存入Session并返回登录成功结果
        //会话跟踪技术，向http会话保存数据
        request.getSession().setAttribute("employee",emp.getId());
        return RestResult.success(emp,"success");
    }

    /***
    * @description 员工退出
    * @param request
    * @return
    * @author
    * @date
    */
    @PostMapping("/logout")
    public RestResult<String> logout(HttpServletRequest request){
        //清理Session中保存的当前登录员工的id
        //页面跳转在前端做
        request.getSession().removeAttribute("employee");
        return RestResult.success("退出成功","success");
    }
    
    /**
    * @description 新增员工
    * @param request 
     * @param employee 
    * @return 
    * @author 
    * @date  
    */
    //不需要相对路径，就在/employee里
    @PostMapping
    public RestResult<String> save(HttpServletRequest request,@RequestBody Employee employee){

        //获得当前已登录用户的id，前面我们登录成功返回了登陆人的session
        //创建这个用户的人（我）
        if(BaseContext.getCurrentId().toString()!="我的id"){
            return RestResult.error("不是管理员不能添加员工，谢谢",0);
        }
        log.info("新增员工，员工信息：{}",employee.toString());

        //这里手动设的值（时间，创建人）都是前端他设置不了的，所以我们手动设置，本身employee传过来的时候就带了很多值了
        //status默认就是1
        //设置初始密码123456，需要进行md5加密处理,后期员工可以用这个密码登录进来修改自己的密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //这些注释掉的是我们需要多次使用，所以抽出来写个自动填充，在元数据对象处理器配置相应的值
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return RestResult.success("新增员工成功","成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    //第一次进页面，前后翻页，点查询名字图标，这些前端都会发请求查询
    public RestResult<Page> page(int page, int pageSize, String name){
        //Page是mp封装的属性，这边前端page默认（我一开始点进去）是1，pageSize是10
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        //like是前后百分号，挺好的
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return RestResult.success(pageInfo,"成功");
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public RestResult<String> update(HttpServletRequest request,@RequestBody Employee employee){
        //employee前端传过来的更新过的，比如禁用按钮，status变成0，那么我们肯定数据库中status也要变，所以前端把这个status变为0
        //的employee对象发给这个函数进行数据库更新，同时这个函数不仅是更新status，其他的都一样，一个道理，前端改了什么，employee也随之改变。
        log.info(employee.toString());

        //注释原因同理上面，自动填充
        //empId是更新人（当前用户），所以要从session中获取
//        Long empId = (Long)request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        //注意，前端发的id可能会存在错误，因为JS精度问题导致后3位不一样，所以我们给前端的数据long类型都转成string类型再给前端
        employeeService.updateById(employee);

        return RestResult.success("员工信息修改成功","成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public RestResult<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return RestResult.success(employee,"成功查询");
        }
        return RestResult.error("没有查询到对应员工信息",0);
    }

}
