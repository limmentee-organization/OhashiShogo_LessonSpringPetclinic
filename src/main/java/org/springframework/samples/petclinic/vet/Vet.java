package org.springframework.samples.petclinic.vet;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.samples.petclinic.model.Person;

//@Entity: エンティティクラスである。これによって、このクラスはデータベースとマッピングされている。
@Entity
//@Table: エンティティクラスがマッピングされているテーブルのデータベースを指定できる。
@Table(name = "vets")
public class Vet extends Person {

	//@ManyToMany: 多対多の関係を表します。そのため下記のspecialtyとvetが関連づけれている。
		//fetch = FetchType.EAGER: specialtyとvetが特定の情報を引き出す際に全ての情報を取得してくれる。Join関係で必要。
	@ManyToMany(fetch = FetchType.EAGER)
	//@JoinTable: 中間テーブルvet_specialtiesを指定して、vet_idとspecialty_idをジョインしている。
	//複合キーのみの登録ができる。
	@JoinTable(name = "vet_specialties", joinColumns =  @JoinColumn(name = "vet_id"),
		inverseJoinColumns = @JoinColumn(name = "specialty_id"))
	private Set<Specialty> specialties;

	//specialtiesフィールドの内部セットを返します。セットが存在しない場合、新しく作成します
	protected Set<Specialty> getSpecialtiesInternal() {
		if(this.specialties == null) {
			this.specialties = new HashSet<>();
		}
		return this.specialties;
	}

	//specialties フィールドを設定。
	protected void setSpecialtiesInternal(Set<Specialty> specialties){
		this.specialties = specialties;
	}

	//@XmlElement: XML要素として表示。HTML内でタグとして利用もできる。
	@XmlElement
	//XML要素(HTML)として表現される List<Specialty> オブジェクトを返します
	public List<Specialty> getSpecialties() {
		//リストのソートと変換を行なっている。
		List<Specialty> sortedSpecs = new ArrayList<>(getSpecialtiesInternal());
		//sortedSpecsでリストを変更不可リストに変換し外部から変更されないようにしている。
		PropertyComparator.sort(sortedSpecs, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedSpecs);
	}

	//獣医の数を返している。
	public int getNrOfSpecialties() {
		return getSpecialtiesInternal().size();
	}

	//獣医の新しく追加する。
	public void addSpecialty(Specialty specialty) {
		getSpecialtiesInternal().add(specialty);
	}
}
