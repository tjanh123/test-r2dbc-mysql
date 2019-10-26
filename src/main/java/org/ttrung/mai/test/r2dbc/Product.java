package org.ttrung.mai.test.r2dbc;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	private Long id;
	private String label;
	private String gender;
	private String type;
	
}