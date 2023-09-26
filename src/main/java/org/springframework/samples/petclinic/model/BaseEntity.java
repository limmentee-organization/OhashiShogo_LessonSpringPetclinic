package org.springframework.samples.petclinic.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;

//@MappedSuperclass: エンティティクラスではなく、他のエンティティクラスから継承される抽象クラスのこと。
//このクラスはデータベースにマッピングするテーブルはないが他のエンティティクラスから継承している。そのため、共通のメソッドを再利用できる。
@MappedSuperclass
public class BaseEntity implements Serializable {

	//@Id: エンティティの主キーを表す。
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY): IDを自動生成する。
	//GenerationType.IDENTITYはデータベースの自動増分フィールドを使用してIDを自動的に生成して挿入してくれる。
	//例えば、すでに1,2,3の番号が使用されていたら４から作成してくれる。
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	//isNew(): エンティティが新しいものであるかどうかを判定。
	//IDがまだ設定されていない場合（nullである場合)新しいものとみなされる。
	//使う場合、既存を更新するか新しく挿入するのかを判断する際に役に立つ。
	public boolean isNew() {
		return this.id == null;
	}

}
