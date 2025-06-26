package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rating")
public class Rating {
	


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String moodysRating;
	String sandPRating;
	String fitchRating;
	Integer orderNumber;
}
