package org.springframework.samples.petclinic.vet;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

//@XmlRootElement: このクラスがXMLルートであることを示します
//javaオブジェクトをXML形式に変換する際、ルートのこのアノテーションで指定された名前になる。
//XML: データの構造化やデータ交換などで利用して、いろんなことに使えるテキスト文書
@XmlRootElement
public class Vets {
	private List<Vet> vets;

	//@XmlElement: このメソッドが返すデータがXML形式で出力される際に、XMLで該当することを示す。
	// これを利用するとHTMLでタグとして利用もできたりする。
	@XmlElement
	//vetsリストを取得。
	public List<Vet> getVetList() {
		//リストがまだ作成されていない場合、新しいArrayListインスタンスが作成して、vetsフィールドへの参照が返すよ。
		if (vets == null) {
			vets = new ArrayList<>();
		}
		return vets;
	}
}
