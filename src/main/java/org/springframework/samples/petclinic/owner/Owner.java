package org.springframework.samples.petclinic.owner;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;

//このクラスは所有者と人物情報が含まれている

@Entity
//このクラスはデータベースのテーブルとマッピングしている。
@Table(name = "owners")
//エンティティがマッピングしているデータテーブルの名前を指定している。
//ownersテーブルとマッピングしている。
public class Owner extends Person {


	//@Column: データベーステーブルのカラム名とマッピングしている。
	//@NotBlank: 値がないはNGという意味。
	//@Digits: バリデーションアノテーションになる、整数桁と少数桁を指定できる。
		//fraction: 少数桁  integer: 整数桁
	//@OneToMany: 1対多の関連を示すアノテーション, ownerとpetの間に、飼い主が複数のペットを持つ関係があるよという意味。
		//cascade: ownerに変更が加えられた場合に、petにも変更がされる。
		//fetch: ownerを読み込む際にpetも一緒に読み込む
	//@JoinColumn: ownerとpetの関連するカラムをjoinする。
	//@OrderBy: リストをソートする
	@Column(name = "address")
	@NotBlank
	private String address;

	@Column(name = "city")
	@NotBlank
	private String city;

	@Column(name = "telephone")
	@NotBlank
	@Digits(fraction = 0, integer = 10)
	private String telephone;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "owner_id")
	@OrderBy("name")


	//ownerが所有するペットのリストを表すフィールド。
	private List<Pet> pets = new ArrayList<>();

	//下記フィールはownerエンティティの操作を定義するメソッド
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<Pet> getPets() {
		return this.pets;
	}


	//新しいペットをリストに追加するメソッド。
	public void addPet(Pet pet) {
		if (pet.isNew()) {
		//pet.isNew(): Petオブジェクトが新しいものかを確認。
			getPets().add(pet);
			//新しい場合は追加する
		}
	}

	//ペットの名前を指定して、取得する。
	public Pet getPet(String name) {
		return getPet(name, false);
		//getPet(String name, boolean ignoreNew) を呼び出し、ignoreNewをfalseで渡す。
	}

	//ペットのIDを指定してペットを取得する
	public Pet getPet(Integer id) {
		//すべてのペットを取得する。処理内容は下記の通りになる
		for (Pet pet : getPets()) {
			if (!pet.isNew()) {
				//ペットが新しいものではないかを確認
				Integer compId = pet.getId();
				if (compId.equals(id)) {
					return pet;
					//ペットのIDが引数で指定されたIDと一致する場合、そのペットを返す。
				}
			}
		}
		return null;
	}
	//ペットの名前と新しいペットを無視するかどうかを指定してペットを取得する
	public Pet getPet(String name, boolean ignoreNew) {
		// name と ignoreNew を受けとるよ
		name = name.toLowerCase();
		//toLowerCase: nameを小文字に変換する。
		for (Pet pet : getPets()) {
			String compName = pet.getName();
			if (compName != null && compName.equalsIgnoreCase(name)) {
				//ペットの名前が指定された名前と一致するか判定。
				if (!ignoreNew || !pet.isNew()) {
					//ignoreNew が false であるか、ペットが新しいものではない場合、そのペットを返すよ。
					return pet;
				}
			}
		}
		return null;
		//一致しない場合はnullで返す。
	}

	@Override
	//toStringメソッド: 数値型などをString型の文字列に変更するために使う。
	//下記のフィールドの情報を連結して、文字列として扱う。
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId())
			//ToStringCreatorを使って、下記のフィールドを文字列を追加する。
			.append("new", this.isNew())
			.append("lastName", this.getLastName())
			.append("firstName", this.getFirstName())
			.append("address", this.address)
			.append("city", this.city)
			.append("telephone", this.telephone)
			.toString();
	}


	//所有者が特定のペットに新しい訪問情報を追加するメソッド。 メソッドの引数：ペットのID、新しい訪問情報
	public void addVisit(Integer petId, Visit visit) {

		//Assert.notNull: 引数がnullではないか確認するために使用。nullなら下記のエラーメッセージ表示
		Assert.notNull(petId, "Pet identifier must not be null!");
		Assert.notNull(visit, "Visit must not be null!");

		//指定されたペットのIDに対応するペットを取得する。
		Pet pet = getPet(petId);

		//Assert.notNull: 引数がnullではないか確認するために使用。
		Assert.notNull(pet, "Invalid Pet identifier!");

		pet.addVisit(visit);
	}
}
