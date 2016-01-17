package com.xl.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class ResultFilter<T> implements Serializable{

	private static final long serialVersionUID = 5472321653620726832L;

	private final static int DEFAULT_NAVIGATOR_SIZE = 5;

	//当前页
    private int currentPage = 1;
    //每页显示数量
    private int pageSize = 5;
    
    //总条数
    private int totalCount;

    private boolean havaNextPage;

    private boolean havePrePage;

    private int navigatorSize;
    
    //存放查询结果用的list
    private List<T> items;
    
    public ResultFilter(){
    	
    }
    
    public ResultFilter(int totalCount, int pageSize, int currentPage) {
        this(totalCount, pageSize, currentPage, DEFAULT_NAVIGATOR_SIZE);
    }

    public ResultFilter(int totalCount, int pageSize, int currentPage,
                        int navigatorSize) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.navigatorSize = navigatorSize;
    }
    
    public int getPageCount() {
        int pageCount = 0;
        if (pageSize != 0) {
            pageCount = totalCount / pageSize;
            if (totalCount % pageSize != 0)
                pageCount++;
        }

        return pageCount;
    }

    public int getCurrentPage() {
        currentPage = currentPage < getPageCount() ? currentPage :
                      getPageCount();
        currentPage = currentPage < 1 ? 1 : currentPage;

        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }


    public boolean isHaveNextPage() {
        havaNextPage = false;
        if ((getPageCount() > 1) && (getPageCount() > getCurrentPage()))
            havaNextPage = true;
        return havaNextPage;
    }

    public boolean isHavePrePage() {
        havePrePage = false;
        if ((getPageCount() > 1) && (currentPage > 1))
            havePrePage = true;
        return havePrePage;
    }

    private int getNavigatorIndex(boolean isBegin) {
        int beginNavigatorIndex = getCurrentPage() - navigatorSize / 2;
        int endNavigatorIndex = getCurrentPage() + navigatorSize / 2;
        beginNavigatorIndex = beginNavigatorIndex < 1 ? 1 : beginNavigatorIndex;
        endNavigatorIndex = endNavigatorIndex < getPageCount() ?
                            endNavigatorIndex :
                            getPageCount();
        while ((endNavigatorIndex - beginNavigatorIndex) < navigatorSize &&
               (beginNavigatorIndex != 1 || endNavigatorIndex != getPageCount())) {
            if (beginNavigatorIndex > 1)
                beginNavigatorIndex--;
            else if (endNavigatorIndex < getPageCount())
                endNavigatorIndex++;
        }

        if(isBegin)
            return beginNavigatorIndex;
        else
            return endNavigatorIndex;
    }

    public int getBeginNavigatorIndex() {
        return getNavigatorIndex(true);
    }

    public int getEndNavigatorIndex() {
        return getNavigatorIndex(false);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
    
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
