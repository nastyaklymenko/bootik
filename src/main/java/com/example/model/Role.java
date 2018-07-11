package com.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "role")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "role_id")
  private int id;

  @Column(name = "role")
  private String role;

}
