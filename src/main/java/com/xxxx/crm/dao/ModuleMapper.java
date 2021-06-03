package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.dto.TreeDto;
import com.xxxx.crm.vo.Module;

import java.util.List;
import java.util.Map;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    public List<TreeDto> queryAllModules();

    public List<Module> queryModules();

    Module queryModuleByGradeAndModuleName(Integer grade, String moduleName);

    Module queryModuleByGradeAndUrl(Integer grade, String url);

    Module queryModuleByOptValue(String optValue);

    List<Map<String,Object>> selectModuleByGrade (Integer grade);

    int countSubModuleByParentId(Integer mid);
}