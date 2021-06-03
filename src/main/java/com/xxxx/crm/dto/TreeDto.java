package com.xxxx.crm.dto;

public class TreeDto {
    private Integer id;
    private String name;
    private Integer pId;
    private Boolean Checked;

    public Boolean getChecked() {
        return Checked;
    }

    public void setChecked(Boolean checked) {
        Checked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }
}
