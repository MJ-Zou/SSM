package com.jd.pojo;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable {
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private List<T> list;
    private int totalPage;
    private int page;
}
