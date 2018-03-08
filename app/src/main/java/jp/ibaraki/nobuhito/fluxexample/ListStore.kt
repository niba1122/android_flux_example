package jp.ibaraki.nobuhito.fluxexample

import io.reactivex.Observable
import jp.ibaraki.nobuhito.fluxexample.repository.Repository
import jp.ibaraki.nobuhito.fluxexample.utils.Store

class ListStore(private val repository: Repository) : Store<ListActionType, ListActionCreator, ListReducer, ListGetter>() {
    override fun createActionCreator(dispatch: (ListActionType) -> Unit, reducer: ListReducer): ListActionCreator = ListActionCreator(dispatch, reducer, repository)

    override fun createReducer(action: Observable<ListActionType>): ListReducer = ListReducer(action)

    override fun createGetter(reducer: ListReducer): ListGetter = ListGetter(reducer)
}
