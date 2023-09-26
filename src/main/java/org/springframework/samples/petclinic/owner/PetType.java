package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


//エンティティがマッピングしているデータテーブルの名前を指定している。
//typesテーブルとマッピングしている。
@Entity
@Table(name = "types")
public class PetType extends NamedEntity {

}
