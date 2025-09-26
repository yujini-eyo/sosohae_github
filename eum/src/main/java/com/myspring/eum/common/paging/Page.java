package com.myspring.eum.common.paging;

import java.util.List;

public class Page<T> {
    private List<T> rows;
    private int page;
    private int size;
    private long total;

    public Page() {}
    public Page(List<T> rows, int page, int size, long total) {
        this.rows = rows;
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public List<T> getRows() { return rows; }
    public void setRows(List<T> rows) { this.rows = rows; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public long getTotalPages() { return size > 0 ? (total + size - 1) / size : 0; }
}
