package org.springframework.samples.petclinic.owner;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;

//エンティティがマッピングしているデータテーブルの名前を指定している。
//petsテーブルとマッピングしている。
@Entity
@Table(name = "pets")
public class Pet extends NamedEntity {

	//@Column: データベーステーブルのカラム名とマッピングしている。
	//@NotBlank: 値がないはNGという意味。
	//@Digits: バリデーションアノテーションになる、整数桁と少数桁を指定できる。
		//fraction: 少数桁  integer: 整数桁
	//@OneToMany: 1対多の関連を示すアノテーション, ownerとpetの間に、飼い主が複数のペットを持つ関係があるよという意味。
		//cascade: ownerに変更が加えられた場合に、petにも変更がされる。
		//fetch: ownerを読み込む際にpetも一緒に読み込む
	//@ManyToOne: 多対1 のリレーションシップを持つ
	//@JoinColumn: ownerとpetの関連するカラムをjoinする。
	//@OrderBy: リストをソートする
	//@DateTimeFormat: 日付のフォーマットを指定

	@Column(name = "birth_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType type;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "pet_id")
	@OrderBy("visit_date ASC")
	private Set<Visit> visits = new LinkedHashSet<>();

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getBirthDate() {
		return this.birthDate;
	}

	public PetType getType() {
		return this.type;
	}

	public void setType(PetType type) {
		this.type = type;
	}

	public Collection<Visit> getVisits() {
		return this.visits;
	}

	public void addVisit(Visit visit) {
		getVisits().add(visit);
	}

}
