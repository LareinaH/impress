package com.conton.base.model;

import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class BaseModel implements Serializable {
	private static final long serialVersionUID = -6332620263145558954L;
	public static final String ID = "id";
	public static final String CREATED_AT = "createdAt";

	public BaseModel() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedAtString(){
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateformat.format(this.createdAt);
	}
}
