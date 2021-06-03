package com.xxxx.crm.controller;

import com.xxxx.crm.annotation.RequirePermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.service.CusDevPlanService;
import com.xxxx.crm.vo.CusDevPlan;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;


@Controller
@RequestMapping("cusDevPlan")
public class CusDevPlanController extends BaseController {

    @Resource
    private CusDevPlanService cusDevPlanService;

    /**
     * 点击客户开发计划，实现页面的跳转
     * @return
     */
    @RequestMapping("plan")
    @RequirePermission(code = "1020")
    public String plan(){
        return "cusDev/plan";
    }

    /**
     * 查询营销机会的计划项数据列表
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    //@RequirePermission(code = "1020")
    public Map<String, Object> queryCusDevPlanByParams (CusDevPlanQuery query) {
        return cusDevPlanService.queryByParamsForTable(query);
    }

    @RequestMapping("save")
    @ResponseBody
    //@RequirePermission(code = "1020")
    public ResultInfo saveCusDevPlan(CusDevPlan cusDevPlan){

        cusDevPlanService.saveDevPlan(cusDevPlan);
        return success("插入成功");
    }

    @RequestMapping("update")
    @ResponseBody
    //@RequirePermission(code = "1020")
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){

        cusDevPlanService.updatePlan(cusDevPlan);
        return success("计划项目内容更新成功");
    }

    @RequestMapping("addOrUpdateCusDevPlanPage")
    //@RequirePermission(code = "1020")
    public String addOrUpdateCusDevPlanPage(Integer sid, Integer id, Model model){
        model.addAttribute("cusDevPlan",cusDevPlanService.selectByPrimaryKey(id));
        model.addAttribute("sid",sid);
        return "cusDev/add_update";
    }

    /**
     * 删除计划项
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    //@RequirePermission(code = "1020")
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("删除成功");
    }

    /**
     * 更新开发状态
     * @param id
     * @return
     */
    /*@RequestMapping("updateDevResult")
    @ResponseBody
    public ResultInfo updateDevResult(Integer id){

        cusDevPlanService.updateDevResult(id);
        return success("更新成功");
    }*/
}
