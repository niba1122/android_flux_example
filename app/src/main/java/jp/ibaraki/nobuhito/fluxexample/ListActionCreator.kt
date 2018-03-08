package jp.ibaraki.nobuhito.fluxexample

import jp.ibaraki.nobuhito.fluxexample.repository.Repository
import jp.ibaraki.nobuhito.fluxexample.utils.DisposableMapper

class ListActionCreator(private val dispatch: (ListActionType) -> Unit, private val reducer: ListReducer, private val repository: Repository) : DisposableMapper() {
    fun onInitialize() {
        dispatch(ListActionType.StartInitialLoad())
        repository.list(1)
                .subscribe({
                    dispatch(ListActionType.SuccessInitialLoad(it))
                }, {
                    dispatch(ListActionType.Error(it))
                }).let { disposables.add(it) }
    }

    fun onScrollToLast() {
        if (reducer.isNextLoading.value) return
        dispatch(ListActionType.StartNextLoad())
        repository.list(reducer.page + 1)
                .subscribe({
                    dispatch(ListActionType.SuccessNextLoad(it))
                }, {
                    dispatch(ListActionType.Error(it))
                }).let { disposables.add(it) }
    }
}