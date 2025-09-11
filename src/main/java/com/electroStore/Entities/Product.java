package com.electroStore.Entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	private String id;
	private String title;
	private String description;
	private int price;
	private int dicountPric;
	private int quantity;
	private Date addedDate;
	private boolean live;
	private boolean stock;
	

}
