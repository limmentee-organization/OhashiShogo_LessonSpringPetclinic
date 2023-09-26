package org.springframework.samples.petclinic.vet;

import java.util.Collection;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

//extends Repository<Vet, Integer>: Repositoryを継承している。
//Vetとそのプライマリキーの型（Integer）を指定している。
public interface VetRepository extends Repository<Vet, Integer> {

	//@Transactional: トランザクション内で実行している。readOnly=trueとして設定されているため、読み取り専用トランザクションで実行されます。
	@Transactional(readOnly = true)
	//@Cacheable("vets"): メソッドの結果をキャッシュするための指定,キャッシュ名はvetsになる。
	//同じ引数で再度呼び出されたらキャッシュから取得可能
	@Cacheable("vets")
	//すべての獣医情報を含むコレクションを返します。
	Collection<Vet> findAll() throws DataAccessException;

	//@Transactional: トランザクション内で実行している。readOnly=trueとして設定されているため、読み取り専用トランザクションで実行されます。
	@Transactional(readOnly = true)
	//@Cacheable("vets"): メソッドの結果をキャッシュするための指定,キャッシュ名はvetsになる。
	//同じ引数で再度呼び出されたらキャッシュから取得可能
	@Cacheable("Vets")
	//Pageオブジェクトを返して、獣医情報をページ単位で取得します。
	Page<Vet> findAll(Pageable pageable) throws DataAccessException;

}
