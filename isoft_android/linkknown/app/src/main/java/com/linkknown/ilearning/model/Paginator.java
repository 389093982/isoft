package com.linkknown.ilearning.model;

import java.util.List;

import lombok.Data;

@Data
public class Paginator {

    private int currpage;
    private int firstpage;
    private int lastpage;
    private List<Integer> pages;
    private int pagesize;
    private int totalcount;
    private int totalpages;
}