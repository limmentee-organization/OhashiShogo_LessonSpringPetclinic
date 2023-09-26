package org.springframework.samples.petclinic.vet;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VetController {

	private final VetRepository vetRepository;

	//コンストラクタ VetRepositoryインターフェイスの実装を受け入れてコントローラー内で使用できる。
	public VetController(VetRepository clinicService) {
		this.vetRepository = clinicService;
	}

	// vet.htmlにGETリクエストを受け入れるエンドポイントを定義している。
	@GetMapping("/vet.html")
	//@RequestParam: pageパラメータを受け入れている。デフォルトが１。
	//Model: Modelオブジェクトを受け取り、ビューへデータを渡している。
	public String shortVetList(@RequestParam(defaultValue = "1") int page, Model model) {
		//獣医のオブジェクトを作成。このオブジェクトに獣医のリストを格納する。
		Vets vets = new Vets();
		//findPaginatedを呼び出してページネーションを行い、結果をvetsに追加する。
		Page<Vet> paginated = findPaginated(page);
		vets.getVetList().addAll(paginated.toList());
		return addPaginationModel(page, paginated, model);
	}

	//ページ情報をモデルに追加して、vets/vetListへ返す。
	private String addPaginationModel(int page, Page<Vet> paginated, Model model) {
		//下記はページネーション機能に使用している、
		List<Vet> listVets = paginated.getContent();
		//現在のページについて
		model.addAttribute("currentPage", page);
		//合計ページについて
		model.addAttribute("totalPages", paginated.getTotalPages());
		//合計のアイテム数ついて
		model.addAttribute("totalItems", paginated.getTotalElements());
		//獣医リストについて
		model.addAttribute("listVets", listVets);
		return "vets/vetList";
	}

	//このメソッドは、指定されたページ番号をもとに獣医リストを取得。
	private Page<Vet> findPaginated(int page) {
		//ページサイズは５で固定。
		int pageSize = 5;
		//ページネーションの設定を行なっている。PageRequest.ofは（ページ番号、ページサイズ）で作成。
		//page-1は、1ページ目が0から始まるため、指定されたページ番号から1を引いています。
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		//findAll使用して、指定されたページのデータを取得。ページネーションの設定に従ってデータが切りとって
		//指定されたページに表示される獣医のリストが返されます。
		return vetRepository.findAll(pageable);
	}

	//vetsのGETリクエストを受け取っている。
	@GetMapping({"/vets"})
	//@ResponseBody: オブジェクトをHTTPレスポンスのボディに直接変換すること。
	//つまり、javaオブジェクトをJSONなどの形式に変更してクライアントに返す。
	public @ResponseBody Vets showResourcesVetList() {
		//vetsオブジェクト作成。
		Vets vets = new Vets();
		//全ての獣医情報をデータベースから取得、vetsオブジェクト内のリストへ追加している。
		vets.getVetList().addAll(this.vetRepository.findAll());
		return vets;
	}
}
