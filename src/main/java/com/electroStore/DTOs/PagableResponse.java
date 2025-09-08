package com.electroStore.DTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagableResponse<T> {
	
	private List<T> content;
	private int pageNum;
	private int pageSize;
	private Long totalElement;
	private int totalPage;
	private boolean lastpage;
	
	
	

}
