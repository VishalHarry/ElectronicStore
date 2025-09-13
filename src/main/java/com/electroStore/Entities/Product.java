package com.electroStore.Entities;

import java.util.Date;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	private String image;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_Id")
	private Category category;
	

}
