package org.springframework.samples.petclinic.vet;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.samples.petclinic.model.NamedEntity;

//@Entity: エンティティクラスである。これによって、このクラスはデータベースとマッピングされている。
@Entity
//@Table: エンティティクラスがマッピングされているテーブルのデータベースを指定できる。
@Table(name = "specialties")
public class Specialty extends NamedEntity {

}
