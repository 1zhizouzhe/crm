package com.xxxx.crm.controller;

import com.xxxx.crm.annotation.RequirePermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private UserService userService;

    /**
     * 根据参数查询SaleChance
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    @RequirePermission(code = "101001")
    public Map<String,Object> querySaleChanceParams(SaleChanceQuery query,Integer flag,HttpServletRequest req){
        if(null != flag && flag == 1){
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
            query.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(query);
    }

    @RequestMapping("index")
    @RequirePermission(code = "10")
    public String index(){
        return "saleChance/sale_chance";
    }


    /**
     * 营销机会数据添加
     * @param req
     * @param saleChance
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    @RequirePermission(code ="101002")
    public ResultInfo saveSaleChance(HttpServletRequest req, SaleChance saleChance){

        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);

        String trueName = userService.selectByPrimaryKey(userId).getTrueName();

        saleChance.setCreateMan(trueName);

        saleChanceService.saveSaleChance(saleChance);

        return success("营销机会数据添加成功");
    }

    @RequestMapping("addOrUpdateSaleChancePage")
    @RequirePermission(code = "1010")
    public String addOrUpdateSaleChancePage(Integer id, Model model){

        if(null != id){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            model.addAttribute("saleChance",saleChance);
        }

        return "saleChance/add_update";
    }

    /**
     * 更新营销机会数据
     * @param request
     * @param saleChance
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    @RequirePermission(code = "101002")
    public ResultInfo updateSaleChance(HttpServletRequest request, SaleChance saleChance){
        // 更新营销机会的数据
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功！");
    }

    @RequestMapping("addOrUpdateChancePage")
    @RequirePermission(code = "1010")
    public String addOrUpdateChancePage(Integer id,Model model){

        if(null != id){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);

            model.addAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }

    /**
     * 营销机会删除
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    @RequirePermission(code = "101003")
    public ResultInfo deleteSaleChance(Integer[] ids){
        saleChanceService.deleteSaleChance(ids);
        return success("营销机会数据删除成功");
    }



    @RequestMapping("toCusDevPlanDataPage")
    @RequirePermission(code = "1020")
    public String toCusDevPlanDataPage(Model model,Integer sid){
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(sid);
        model.addAttribute("saleChance",saleChance);
        return "cusDev/cus_data";
    }

    /**
     * 更新营销机会的开发状态
     * @param id
     * @param devResult
     * @return
     */
    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody@RequirePermission(code = "1010")
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("开发状态更新成功！");
    }

}
