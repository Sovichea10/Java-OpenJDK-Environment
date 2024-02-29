package com.camcyber.shares;

import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class PaginationUtil<T> {
    private Page<T> page;
    private PageInfo pageInfo;

    public PaginationUtil(Page<T> page, PageInfo pageInfo1) {
        this.page=page;
        this.pageInfo=pageInfo1;
    }

    public PaginationUtil<T> calculatePageInfo(@NotNull Page<T> page) {
       if (page.getContent().isEmpty()){
           return new  PaginationUtil<T>();
       }
       PageInfo pageInfo1 = new PageInfo();
       pageInfo1.setCurrentPage(page.getNumber()+1);
       pageInfo1.setCurrentSize(page.getContent().size());
       pageInfo1.setTotalPages(page.getTotalPages());
       pageInfo1.setTotalSize(pageInfo1.getTotalSize());
       return new PaginationUtil<T>(page,pageInfo1);
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
