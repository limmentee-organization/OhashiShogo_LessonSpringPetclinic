package org.springframework.samples.petclinic.owner;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

//エンティティがマッピングしているデータテーブルの名前を指定している。
//visitsテーブルとマッピングしている。
@Entity
@Table(name = "visits")
public class Visit extends BaseEntity {

	//@DateTimeFormat: 日付のフォーマットを指定
	//@Column: データベーステーブルのカラム名とマッピングしている。
	//@NotBlank: 値がないはNGという意味。
	@Column(name = "visit_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@NotBlank
	private String description;

	public Visit() {
		this.date = LocalDate.now();
	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
