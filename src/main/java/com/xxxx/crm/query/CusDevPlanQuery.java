package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;
import org.springframework.web.util.pattern.PathPattern;

public class CusDevPlanQuery extends BaseQuery {

    private Integer sid;

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }
}
