package com.heycar.model.search;

import java.util.List;

import com.heycar.model.GSONUtil;

public class BaseSearch {

    private Long id;
    private Integer start;
    private Integer length;
    private Integer startingId;
    private Integer top;
    private String orderBy;
    private String sortOrder;
    private List<Long> idInList;
    private List<Long> idNotInList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getStartingId() {
        return startingId;
    }

    public void setStartingId(Integer startingId) {
        this.startingId = startingId;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setOrderByDesc() {
        this.sortOrder = "DESC";
    }

    public void setOrderByAsc() {
        this.sortOrder = "ASC";
    }

    public List<Long> getIdInList() {
        return idInList;
    }

    public void setIdInList(List<Long> idInList) {
        this.idInList = idInList;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<Long> getIdNotInList() {
        return idNotInList;
    }

    public void setIdNotInList(List<Long> idNotInList) {
        this.idNotInList = idNotInList;
    }

    public void setIdNotIn(Long id) {
        if (id != null) {
            this.idNotInList = List.of(id);
        }
    }

    @Override
    public String toString() {
        return GSONUtil.DEFAULT_GSON.toJson(this);
    }

}
