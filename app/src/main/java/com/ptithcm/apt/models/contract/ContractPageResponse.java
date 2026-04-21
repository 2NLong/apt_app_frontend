package com.ptithcm.apt.models.contract;


import com.ptithcm.apt.models.contract.ContractResponse;

import java.util.List;

public class ContractPageResponse {
    private List<ContractResponse> content;
    private int totalPages;
    private int number;

    public List<ContractResponse> getContent() { return content; }
    public int getTotalPages() { return totalPages; }
    public int getNumber() { return number; }
}