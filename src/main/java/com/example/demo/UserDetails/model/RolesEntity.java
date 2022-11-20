package com.example.demo.UserDetails.model;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class RolesEntity {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @Enumerated(EnumType.STRING)
	  private RolesEnum name;

	  
	  public Long getId() {
	    return id;
	  }

	  public void setId(Long id) {
	    this.id = id;
	  }

	  public RolesEnum getName() {
	    return name;
	  }

	  public void setName(RolesEnum name) {
	    this.name = name;
	  }
}
