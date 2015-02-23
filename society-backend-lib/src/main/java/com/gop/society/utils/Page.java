package com.gop.society.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page {

    public Page(Integer page, Integer size){
        this.page = (page==null) ? 0 : page;
        this.size = (size==null) ? 100 : size;
    }

    private Integer page;
    private Integer size;
}
