package net.jiaobaowang.visitor.entity;

import java.util.List;

/**
 * Created by rocka on 2018/1/16.
 */

public class ListData {
    private int totalRow;
    private int pageInt;
    private boolean lastPage;
    private boolean firstPage;
    private int totalPage;
    private int pageSize;
    private List<VisitRecord> list;

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public int getPageInt() {
        return pageInt;
    }

    public void setPageInt(int pageInt) {
        this.pageInt = pageInt;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<VisitRecord> getList() {
        return list;
    }

    public void setList(List<VisitRecord> list) {
        this.list = list;
    }
}
