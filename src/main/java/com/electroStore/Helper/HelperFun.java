package com.electroStore.Helper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.electroStore.DTOs.PagableResponse;

public class HelperFun {


    // Generic reusable function
    public static <U, V> PagableResponse<V> getPageResponse(Page<U> page, Class<V> type, ModelMapper modelMapper) {
        
        // Entities to DTOs
        List<V> dtos = page.getContent()
                .stream()
                .map(object -> modelMapper.map(object, type))
                .collect(Collectors.toList());

        // Fill response
        PagableResponse<V> resp = new PagableResponse<>();
        resp.setContent(dtos);
        resp.setPageNum(page.getNumber());
        resp.setPageSize(page.getSize());
        resp.setTotalElement(page.getTotalElements());
        resp.setTotalPage(page.getTotalPages());
        resp.setLastpage(page.isLast());

        return resp;
    }
}
