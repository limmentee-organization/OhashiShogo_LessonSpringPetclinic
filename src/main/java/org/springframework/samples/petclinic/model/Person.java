package org.springframework.samples.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

//@MappedSuperclass: エンティティクラスではなく、他のエンティティクラスから継承される抽象クラスのこと。
//このクラスはデータベースにマッピングするテーブルはないが他のエンティティクラスから継承している。そのため、共通のメソッドを再利用できる。
@MappedSuperclass
public class Person extends BaseEntity {

	@Column(name = "first_name")
	@NotNull
	private String firstName;

	@Column(name = "last_name")
	@NotNull
	private String lastName;

	public String getFirstName() {
		return this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
