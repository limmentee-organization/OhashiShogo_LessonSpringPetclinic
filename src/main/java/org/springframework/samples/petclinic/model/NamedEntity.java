package org.springframework.samples.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

//@MappedSuperclass: エンティティクラスではなく、他のエンティティクラスから継承される抽象クラスのこと。
//このクラスはデータベースにマッピングするテーブルはないが他のエンティティクラスから継承している。そのため、共通のメソッドを再利用できる。
@MappedSuperclass
public class NamedEntity extends BaseEntity {

	//@Column: データベースのカラムとマッピングされていることを示している。
	@Column(name = "name")
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	//toString():オブジェクトの文字列表現を表している。実装では、エンティティの名前を文字列として返すため、
	//toString()が呼び出されると、その名前が返されます。
	public String toString() {
		return this.getName();
	}

}
