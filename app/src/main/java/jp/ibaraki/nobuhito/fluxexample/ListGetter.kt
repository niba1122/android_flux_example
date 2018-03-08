package jp.ibaraki.nobuhito.fluxexample

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import jp.ibaraki.nobuhito.fluxexample.recyclerview.ListItemType
import jp.ibaraki.nobuhito.fluxexample.utils.DisposableMapper

class ListGetter(reducer: ListReducer) : DisposableMapper() {

    val listItems: Observable<List<ListItemType>> =
            Observable.combineLatest(
                    reducer.elements.observable,
                    reducer.isNextLoading.observable,
                    BiFunction { elements, isNextLoading ->
                        var listItems: List<ListItemType> = elements.map { ListItemType.Element(it) }
                        if (isNextLoading) {
                            listItems += ListItemType.Loading()
                        }

                        listItems
                    })

    val isShownPageLoading = reducer.isInitialLoading

    val errorSnackbar = reducer.error
}