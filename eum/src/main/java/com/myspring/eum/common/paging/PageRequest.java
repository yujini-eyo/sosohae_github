package com.myspring.eum.common.paging;

public class PageRequest {

    private int page = 1;   // 1-base
    private int size = 10;  // rows per page

    private String field;    // 검색 필드 (id, name, email, title 등)
    private String keyword;  // 검색어
    private String status;   // 상태 (ACTIVE/INACTIVE, OPEN/ANSWERED 등)
    private String sort;     // 정렬 키 (created_at, last_login 등)
    private String order = "DESC"; // ASC/DESC

    public PageRequest() {}
    public PageRequest(int page, int size) {
        setPage(page);
        setSize(size);
    }

    public int getOffset() { return (Math.max(1,page) - 1) * Math.max(1,size); }
    public int getLimit()  { return Math.max(1, size); }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = (page < 1) ? 1 : page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = (size < 1) ? 10 : size; }

    public String getField() { return nz(field); }
    public void setField(String field) { this.field = trimOrNull(field); }

    public String getKeyword() { return nz(keyword); }
    public void setKeyword(String keyword) { this.keyword = trimOrNull(keyword); }

    public String getStatus() { return nz(status); }
    public void setStatus(String status) { this.status = trimOrNull(status); }

    public String getSort() { return nz(sort); }
    public void setSort(String sort) { this.sort = trimOrNull(sort); }

    public String getOrder() {
        String v = order == null ? "" : order.trim();
        if ("ASC".equalsIgnoreCase(v)) return "ASC";
        return "DESC";
    }
    public void setOrder(String order) { this.order = order == null ? "DESC" : order.trim(); }

    // 매퍼 호환용 별칭
    public String getOrderBy() { return getSort(); }
    public String getOrderDir() { return "ASC".equals(getOrder()) ? "asc" : "desc"; }

    private static String trimOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.length() == 0 ? null : t;
    }
    private static String nz(String s) { return s == null ? null : (s.trim().length()==0? null : s.trim()); }

    public String toString() {
        return "PageRequest{page=" + page + ", size=" + size +
                ", field='" + field + "', keyword='" + keyword +
                "', status='" + status + "', sort='" + sort +
                "', order='" + getOrder() + "'}";
    }
}
