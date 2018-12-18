package com.lmj.base.entity;


public class Page {
    
    private Integer pageNo;
    
    private Integer pageSize;

    public Page(int pageNo, int pageSize) {
        this.pageNo = (pageNo - 1) * pageSize;
        this.pageSize = pageSize;
    }
    
    public Page() {
        
    }
    
    public Integer getPageNo() {
        return pageNo;
    }

    
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    
    public Integer getPageSize() {
        return pageSize;
    }

    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
