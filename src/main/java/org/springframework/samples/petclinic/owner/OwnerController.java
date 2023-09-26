package org.springframework.samples.petclinic.owner;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	//OwnerRepository 所有者情報をアクセスできる。
	private final OwnerRepository owners;

	public OwnerController(OwnerRepository clinicService) {
		this.owners = clinicService;
	}

	//@InitBinder フォームデータのバリデーションができる。許可拒否という意味
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		//setDisallowedFields("id") は、フィールド名のidがフィールドへバインド拒否する。
	}

	@ModelAttribute("owner")
	//モデル属性を設定できる。
	public Owner findOwner(@PathVariable(name = "ownerId", required = false) Integer ownerId) {
		return ownerId == null ? new Owner() : this.owners.findById(ownerId);
	//ownerIdが指定されていない場合、新しいOwnerオブジェクトが作成され、モデルに追加されます。
	//ownerIdが指定されている場合、対応する所有者情報がOwnerRepository
	//を通じて取得され、モデルに追加されます。
	}

	//新しい所有者を作成する画面表示するためのメソッド
	@GetMapping("/owners/new")
	public String initCreationForm(Map<String, Object> model) {
		Owner owner = new Owner();
		model.put("owner", owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	//新しい所有者情報を保存するためのメソッド
	@PostMapping("/owners/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}
		this.owners.save(owner);
		return "redirect:/owners/" + owner.getId();
	}

	//所有者を検索する画面表示するためのメソッド
	@GetMapping("/owners/find")
	public String initFindForm() {
		return "owners/findOwners";
	}

	//所有者を検索の処理するためのメソッド
	@GetMapping("/owners")
	//@RequestParam(defaultValue = "1"): ページ番号をつけている。
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Owner owner, BindingResult result,
								  Model model) {
		//所有者の姓で検索する処理。
		if (owner.getLastName() == null) {
			owner.setLastName("");
		}

		//下記はページネーション処理になる。
		//①findPaginatedForOwnersLastName: ページネーションでして検索する。
		//②addPaginationModel: 処理結果を返す。
		Page<Owner> ownersResults = findPaginatedForOwnersLastName(page, owner.getLastName());
		if (ownersResults.isEmpty()) {

			result.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		}

		if (ownersResults.getTotalElements() == 1) {
			owner = ownersResults.iterator().next();
			return "redirect:/owners/" + owner.getId();
		}

		return addPaginationModel(page, model, ownersResults);
	}

	//①ページネーション情報をモデルに追加するためのメソッド
	private String addPaginationModel(int page, Model model, Page<Owner> paginated) {
		List<Owner> listOwners = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listOwners", listOwners);
		return "owners/ownersList";
	}


	//②姓をもとに所有者情報をページネーションして検索するためのメソッド
	private Page<Owner> findPaginatedForOwnersLastName(int page, String lastname) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return owners.findByLastName(lastname, pageable);
	}

	//所有者情報の編集または更新画面を表示するメソッド
	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
		Owner owner = this.owners.findById(ownerId);
		model.addAttribute(owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	//所有者情報の編集または更新処理するメソッド
	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result,
										 @PathVariable("ownerId") int ownerId) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}

		owner.setId(ownerId);
		this.owners.save(owner);
		return "redirect:/owners/{ownerId}";
	}

	//所有者情報の詳細を表示するメソッド。
	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		Owner owner = this.owners.findById(ownerId);
		mav.addObject(owner);
		return mav;
	}

}
