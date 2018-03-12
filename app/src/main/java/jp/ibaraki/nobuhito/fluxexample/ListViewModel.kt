package jp.ibaraki.nobuhito.fluxexample

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import jp.ibaraki.nobuhito.fluxexample.recyclerview.ListItemType
import jp.ibaraki.nobuhito.fluxexample.repository.Repository

class ListViewModel : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val store = ListStore(Repository())

    val listItems: MutableLiveData<List<ListItemType>> = MutableLiveData()
    val isShownPageLoading: MutableLiveData<Boolean> = MutableLiveData()
    var errorSnackbarBlock: (Throwable) -> Unit = {}

    init {
        store.getter.listItems
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listItems.value = it
                }, {
                }).let { disposables.add(it) }

        store.getter.isShownPageLoading.observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isShownPageLoading.value = it
                }, {}).let { disposables.add(it) }

        store.getter.errorSnackbar
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    errorSnackbarBlock(it)
                }, {}).let { disposables.add(it) }
    }

    override fun onCleared() {
        disposables.clear()
        store.clearDisposables()
    }

    fun onInitialize() {
        store.actionCreator.onInitialize()
    }

    fun onScrollToLast() {
        store.actionCreator.onScrollToLast()
    }
}
