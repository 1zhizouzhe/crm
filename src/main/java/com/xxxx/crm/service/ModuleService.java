package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.dto.TreeDto;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Resource

    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    public List<TreeDto> queryAllModules(){
        return moduleMapper.queryAllModules();
    }

    public List<TreeDto> queryAllModules02(Integer roleId){
        List<TreeDto> treeDtos = moduleMapper.queryAllModules();
        List<Integer> roleHasMids = permissionMapper.queryRoleHasAllModuleIdsByRoleId(roleId);
        if(null!=roleHasMids && roleHasMids.size()>0){
            treeDtos.forEach(treeDto -> {
                if(roleHasMids.contains(treeDto.getId())){
                    treeDto.setChecked(true);
                }
            });
        }

        return treeDtos;
    }

    public Map<String,Object> moduleList(){
        Map<String,Object> result = new HashMap<String,Object>();
        List<Module> modules = moduleMapper.queryModules();
        result.put("count", modules.size());
        result.put("data",modules);
        result.put("code",0);
        result.put("msg","");
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module){
        AssertUtil.isTrue(null == module.getId() || null == selectByPrimaryKey(module.getId()),"待更新记录不存在");
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"请指定菜单名");
        Integer grade = module.getGrade();
        AssertUtil.isTrue(null == grade || !(grade==0||grade==1||grade==2),"菜单层级不合法");
        Module temp = moduleMapper.queryModuleByGradeAndModuleName(grade,module.getModuleName());
        if(null != temp){
            AssertUtil.isTrue(!(temp.getId().equals(module.getId())),"该层级下菜单已存在");
        }

        if(grade == 1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"请指定二级菜单url值");
            temp = moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl());
            if(null != temp){
                AssertUtil.isTrue(!(temp.getId().equals(module.getId())),"该层级下url已存在");
            }
        }

        if(grade !=0){
            Integer parentId = module.getParentId();
            AssertUtil.isTrue(null == parentId || null == selectByPrimaryKey(parentId),"请指定上级菜单");

        }

        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"请输入权限码");

        temp = moduleMapper.queryModuleByOptValue(module.getOptValue());

        if(null != temp){
            AssertUtil.isTrue(!(temp.getId().equals(module.getId())),"权限已存在");
        }

        module.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(module)<1,"菜单更新失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveModule(Module module){
        module.setIsValid((byte) 1);
        AssertUtil.isTrue(moduleMapper.insertSelective(module)<1,"插入失败");
    }


    public List<Map<String, Object>> queryAllModulesByGrade(Integer grade) {

        return moduleMapper.selectModuleByGrade(grade);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModuleById(Integer mid){
        Module temp = selectByPrimaryKey(mid);
        AssertUtil.isTrue(null == mid || null == temp,"待删除记录不存在");

        int count = moduleMapper.countSubModuleByParentId(mid);
        AssertUtil.isTrue(count>0,"存在子菜单，不支持删除操作！");

        count = permissionMapper.countPermissionByRoleId(mid);
        if(count>0){
            AssertUtil.isTrue(permissionMapper.deletePermissionByModuleId(mid)<count,"菜单删除失败！");
        }
        temp.setIsValid((byte)0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(temp)<1,"菜单删除失败");
    }
}
