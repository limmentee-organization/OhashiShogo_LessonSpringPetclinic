package org.springframework.samples.petclinic.owner;


import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
//クエストのベースURLを指定。全てのリクエストは/owners/{ownerId}がつく。
@RequestMapping("/owners/{ownerId}")
class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final OwnerRepository owners;

	public PetController(OwnerRepository owners) {
		this.owners = owners;
	}

	//@ModelAttribute: 共通するオブジェクトを作成する。model.addAttributeを共通化することができる。

	//ペットの種類を取得する。types属性で設定。
	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.owners.findPetTypes();
	}

	//所有者情報を取得する。owner属性で設定。
	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable("ownerId") int ownerId) {

		Owner owner = this.owners.findById(ownerId);
		if (owner == null) {
			throw new IllegalArgumentException("Owner ID not found: " + ownerId);
		}
		return owner;
	}

	//ペット情報を取得する。pet属性で設定。
	@ModelAttribute("pet")
	public Pet findPet(@PathVariable("ownerId") int ownerId,
					   @PathVariable(name = "petId", required = false) Integer petId) {

		Owner owner = this.owners.findById(ownerId);
		if (owner == null) {
			throw new IllegalArgumentException("Owner ID not found: " + ownerId);
		}
		return petId == null ? new Pet() : owner.getPet(petId);
	}



	//@InitBinder フォームデータのバリデーションができる。許可拒否という意味

	//所有者情報でIDはデータバインドから拒否
	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	//ペット情報からPetValidatorをバリデーションとして設定。
	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
	}

	//ペット追加画面表示。所有者に紐づけられたペット情報を表示する。
	@GetMapping("/pets/new")
	public String initCreationForm(Owner owner, ModelMap model) {
		Pet pet = new Pet();
		owner.addPet(pet);
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	//ペット情報を受け取り、バリデーションを実行する。エラーがあったら、エラーメッセージを表示する。
	@PostMapping("/pets/new")
	public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, ModelMap model) {

		//名前が重複していないかチェックしている。result.rejectValue()でエラー情報を設定できる。
		if (StringUtils.hasText(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null) {
			result.rejectValue("name", "duplicate", "already exists");
		}

		// ペットの誕生日が未来の日付でないかをチェック。isAfterで未来判定する。result.rejectValue()でエラー情報を設定できる。
		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}
		//バリデーションエラーチェック
		//result.hasErrors() を使用して、バリデーションエラーがあるかどうかを確認。
		owner.addPet(pet);
		if (result.hasErrors()) {
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		//変更をデータベースに保存
		this.owners.save(owner);
		return "redirect:/owners/{ownerId}";
	}


	//ペット更新画面を表示する。
	@GetMapping("/pets/{petId}/edit")
	public String initUpdateForm(Owner owner, @PathVariable("petId") int petId, ModelMap model) {
		Pet pet = owner.getPet(petId);
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	//ペット更新処理。バリデーションを実行する。エラーがあったら、エラーメッセージを表示する。
	@PostMapping("/pets/{petId}/edit")
	public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner, ModelMap model) {

		String petName = pet.getName();

		//ペット名の重複チェック result.rejectValueでエラーメッセージを編集。
		if (StringUtils.hasText(petName)) {
			Pet existingPet = owner.getPet(petName.toLowerCase(), false);
			if (existingPet != null && existingPet.getId() != pet.getId()) {
				result.rejectValue("name", "duplicate", "already exists");
			}
		}

		// ペットの誕生日が未来の日付でないかをチェック。isAfterで未来判定する。result.rejectValue()でエラー情報を設定できる。
		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		//バリデーションエラーチェック
		//result.hasErrors() を使用して、バリデーションエラーがあるかどうかを確認。
		if (result.hasErrors()) {
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		//変更をデータベースに保存
		owner.addPet(pet);
		this.owners.save(owner);
		return "redirect:/owners/{ownerId}";
	}
}
