package com.balance.architecture.dto;


public class Pagination<T> {
    private int pageNum = 0;//当前页码
    private int pageSize = 10;//每页条目数
    private int startRow = 0;//查询起始行
    private int totalRecordNumber;// 总共的记录条数
    private int totalPageNumber;// 总共的页数，通过总共的记录条数以及每页大小计算而得

    private String startTime;
    private String endTime;
    private T param;

    public Pagination() {
    }

    public Pagination(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}
