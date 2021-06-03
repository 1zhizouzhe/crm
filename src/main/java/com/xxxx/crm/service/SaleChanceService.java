package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.enums.DevResult;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;


    /**
     * 按照给定的三个条件进行分页查找
     * @param query
     * @return
     */
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery query){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(),query.getLimit());
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(query));

        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());

        return map;
    }


    /**
     * 增加营销数据
     * @param saleChance
     */
    public void saveSaleChance(SaleChance saleChance){
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        saleChance.setState(StateStatus.UNSTATE.getType());
        saleChance.setDevResult(DevResult.UNDEV.getStatus());

        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
            saleChance.setAssignTime(new Date());
        }
        saleChance.setIsValid(1);
        saleChance.setUpdateDate(new Date());
        saleChance.setCreateDate(new Date());


        AssertUtil.isTrue(insertSelective(saleChance)<1,"营销数据增加失败");
    }


    /**
     * 判断三个三个参数是否为空，手机号是否符合格式
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入联系电话");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"手机号格式不正确");
    }


    /**
     * 更新销售机会
     * @param saleChance
     */
    public void updateSaleChance(SaleChance saleChance){
        //参数校验
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        //判断是否为空
        AssertUtil.isTrue(null == temp,"待更新记录不存在");
        //校验基础参数
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //设置相关参数值
        saleChance.setUpdateDate(new Date());
        if(StringUtils.isBlank(temp.getAssignMan()) && StringUtils.isNotBlank(saleChance.getAssignMan())){
            //原始记录未分配的改成已分配
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }else if(StringUtils.isNotBlank(temp.getAssignMan()) && StringUtils.isBlank(saleChance.getAssignMan())){
            //原始记录已经分配的，改成未分配
            saleChance.setAssignMan("");
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"更新失败");
    }

    /**
     * 营销机会删除
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){

        AssertUtil.isTrue(null == ids || ids.length ==0,"请选择需要删除的数据");

        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)<0,"营销机会数据删除失败");
    }

    /**
     * 更新营销机会的状态
     *      成功 = 2
     *      失败 = 3
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        AssertUtil.isTrue( null ==id,"待更新记录不存在!");
        SaleChance temp =selectByPrimaryKey(id);
        AssertUtil.isTrue( null ==temp,"待更新记录不存在!");
        temp.setDevResult(devResult);
        AssertUtil.isTrue(updateByPrimaryKeySelective(temp)<1,"机会数据更新失败!");
    }

}
