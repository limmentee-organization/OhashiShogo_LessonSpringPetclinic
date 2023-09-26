package org.springframework.samples.petclinic.owner;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;


//ペットの種類をテキストからオブジェクトに変換している。逆にオブジェクトをテキストに変換するフォーマットをしている。

//@Component: アプリ内で再利用できるオブジェクトと設定している。
@Component
//つまり、別クラスで任意のタイミングで、PetTypeFormatterのインスタンス注入や生成ができる。

public class PetTypeFormatter implements Formatter<PetType> {

	private final OwnerRepository owners;

	@Autowired
	public PetTypeFormatter(OwnerRepository owners) {
		this.owners = owners;
	}

	//ペット種類のオブジェクトを文字列にフォーマットにしている。つまりPetTypeから名前を取り出して文字列で返している。
	@Override
	public String print(PetType petType, Locale locale) {
		return petType.getName();
	}

	//parseメソッドのテキストをPetTypeのオブジェクトに変換している。
	//具体的にいうと、parseメソッドの取得したペットの種類がownersで検索後に一致しているか確認している。
	@Override
	public PetType parse(String text, Locale locale) throws ParseException {
		Collection<PetType> findPetTypes = this.owners.findPetTypes();
		for (PetType type : findPetTypes) {
			if (type.getName().equals(text)) {
				return type;
			}
		}
		throw new ParseException("type not found: " + text, 0);
	}
}

