package org.springframework.samples.petclinic.system;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.MutableConfiguration;

//キャッシュを高速にするためのクラスになる。

//@Configuration: クラスで@Beanを付与したメソッドがあるときに使う。
//proxyBeanMethods = false: メソッドのプロキシ化を無効にして、パフォーマンスを向上する
//プロキシ化: 代理人。利用者とオブジェクトの間に代理人(プロキシ）が入っていて利用者がオブジェクトのメソッドを呼び出しているように見えるが、
//プロキシが事前処理を実行後にオブジェクトの同じメソッドを呼び出して、事後処理として利用者に返す。
@Configuration(proxyBeanMethods = false)
//@EnableCaching: Springのキャッシュ機能を有効にする。同じ引数を呼び出された場合は高速に結果を返す。
@EnableCaching
class CacheConfiguration {

	//@Bean: 個人情報など一時的にデータ保管する機能のこと。定義されたクラスはデフォルトでシングルトンとしてDIコンテナに格納される。
	@Bean
	//JCacheManagerCustomizer: キャッシュ設定をカスタマイズできるメソッド。
	public JCacheManagerCustomizer petclinicCacheConfigurationCustomizer() {
	//ラムダ式内で、cm（JCacheManagerのカスタマイズ可能なインスタンス）を受け取る。createCacheメソッドを呼び出して新しいキャッシュを作成する
	//キャッシュ名はvets、cacheConfiguration() メソッドを呼び出して設定している。
		return cm -> cm.createCache("vets", cacheConfiguration());
	}

	//cacheConfiguration:  キャッシュの設定を提供するメソッド。javax.cache.configuration.Configuration オブジェクトを生成し、
	// キャッシュの統計情報を有効にします
	private javax.cache.configuration.Configuration<Object, Object> cacheConfiguration() {
		return new MutableConfiguration<>().setStatisticsEnabled(true);
	}

}
