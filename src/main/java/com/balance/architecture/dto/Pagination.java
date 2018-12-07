package com.balance.architecture.dto;


import java.util.List;

public class Pagination {
    private int pageNum = 0;//当前页码
    private int pageSize=10;//每页条目数
    private int startRow = 0;//查询起始行
    private int totalRecordNumber;// 总共的记录条数
    private int totalPageNumber;// 总共的页数，通过总共的记录条数以及每页大小计算而得
    private List<?> objectList;

    
    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        int temp = (pageNum - 1) < 0 ? 0 : (pageNum - 1);
        this.startRow = temp * pageSize;
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getTotalRecordNumber() {
        return totalRecordNumber;
    }

    public void setTotalRecordNumber(int totalRecordNumber) {
        this.totalRecordNumber = totalRecordNumber;
    }

    public int getTotalPageNumber() {
        return totalPageNumber;
    }

    public void setTotalPageNumber(int totalPageNumber) {
        this.totalPageNumber = totalPageNumber;
    }

    public List<?> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<?> objectList) {
        this.objectList = objectList;
    }
}
