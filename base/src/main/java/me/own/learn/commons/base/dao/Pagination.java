package me.own.learn.commons.base.dao;

public class Pagination {
    private int iDisplayStart;
    private int iDisplayLength;
    private long iTotalCount;

    public Pagination(){

    }

    public Pagination(int iDisplayStart,int iDisplayLength,int TotalCount){
        this.iDisplayStart = iDisplayStart;
        this.iDisplayLength = iDisplayLength;
        this.iTotalCount = TotalCount;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public long getiTotalCount() {
        return iTotalCount;
    }

    public void setiTotalCount(long iTotalCount) {
        this.iTotalCount = iTotalCount;
    }


}
