package com.xxxx.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CusDevPlanMapper;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
/*
    @Resource
    private SaleChanceService saleChanceService;*/


    /**
     * 多条件查询计划项列表
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String,Object> queryCusDevPlansByParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> map = new HashMap<String,Object>();
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        PageInfo<CusDevPlan> pageInfo =
                new PageInfo<CusDevPlan>(selectByParams(cusDevPlanQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return  map;
    }
    /**
     * 插入新的计划项数据
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveDevPlan(CusDevPlan cusDevPlan){
        checkParams(cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate(),cusDevPlan.getSaleChanceId());
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        cusDevPlan.setIsValid(1);

        AssertUtil.isTrue(insertSelective(cusDevPlan)<1,"计划项添加失败");
    }

    /**
     * 判断待更新计划项的内容是否为空
     * @param planItem
     * @param planDate
     * @param saleChanceId
     */
    private void checkParams(String planItem, Date planDate, Integer saleChanceId) {
        AssertUtil.isTrue(StringUtils.isBlank(planItem),"请输入计划项内容");
        AssertUtil.isTrue(null == planDate,"请输入计划时间");
        AssertUtil.isTrue(saleChanceId == 0,"请设置营销机会id");
    }


    /**
     * 计划项内容更新
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePlan(CusDevPlan cusDevPlan){
        AssertUtil.isTrue(null == cusDevPlan.getSaleChanceId() || null == selectByPrimaryKey(cusDevPlan.getSaleChanceId()),"待更新记录不存在");

        checkParams(cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate(),cusDevPlan.getSaleChanceId());

        cusDevPlan.setUpdateDate(new Date());

        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)<1,"数据更新失败");
    }


    /**
     * 计划项删除
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCusDevPlan(Integer id){
        CusDevPlan cusDevPlan = selectByPrimaryKey(id);
        AssertUtil.isTrue(cusDevPlan == null || id ==null,"记录不存在");

        cusDevPlan.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划项内容删除失败");

    }


    /**
     * 营销机会状态更新
     */
    /*@Transactional(propagation = Propagation.REQUIRED)
    public void updateDevResult(Integer id){
        CusDevPlan cusDevPlan = selectByPrimaryKey(id);
        AssertUtil.isTrue(null == id || cusDevPlan==null,"数据不存在");
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(cusDevPlan.getSaleChanceId());
        saleChance.setDevResult(2);
        AssertUtil.isTrue(saleChanceService.updateByPrimaryKeySelective(saleChance)<1,"计划状态更新失败");

    }*/


}
