package org.springframework.samples.petclinic.owner;


import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;



//ペットオブジェクトに関するバリデーション機能。
public class PetValidator implements Validator {

	private static final String REQUIRED = "required";

	//下記はバリデーションを定義している。
	@Override
	public void validate(Object obj, Errors errors) {
		Pet pet = (Pet) obj;
		String name = pet.getName();
		//nameが空白ではないか判定する
		if (!StringUtils.hasText(name)) {
			errors.rejectValue("name", REQUIRED, REQUIRED);
		}
		//isNew()はtrueであり、nullの場合エラーメッセージを表示
		if (pet.isNew() && pet.getType() == null) {
			errors.rejectValue("type", REQUIRED, REQUIRED);
		}
		//birthDateはnullだとエラーメッセージを表示する。
		if (pet.getBirthDate() == null) {
			errors.rejectValue("birthDate", REQUIRED, REQUIRED);
		}
	}

	//対象のオブジェクトがこのクラスが対象とするクラスかをチェックする。
	//これの場合は、petクラスが対象クラス。
	@Override
	public boolean supports(Class<?> clazz) {
		return Pet.class.isAssignableFrom(clazz);
	}

}
