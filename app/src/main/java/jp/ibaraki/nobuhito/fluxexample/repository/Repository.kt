package jp.ibaraki.nobuhito.fluxexample.repository

import io.reactivex.Single
import jp.ibaraki.nobuhito.fluxexample.model.Element
import java.util.concurrent.TimeUnit

class Repository {
    companion object {
        const val LIMIT = 12
    }

    fun list(page: Int): Single<List<Element>> =
            Single.just(Array(LIMIT) { Element(LIMIT * (page - 1) + it) }.toList()).delay(1, TimeUnit.SECONDS)
}

