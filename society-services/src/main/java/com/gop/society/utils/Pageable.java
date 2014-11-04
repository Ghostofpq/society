package com.gop.society.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Pageable<E> {

    private List<E> data;
    private Integer page;
    private Integer size;
    private Long totalCount;

    public Pageable() {
    }

    public Pageable(Integer page, Integer size, Long totalCount, List<E> data) {
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
        this.data = data;
    }

    public static boolean validatePageAndSize(int size, int page) {
        if (size < 0 || page < 0) {
            return false;
        }
        return true;
    }

}
