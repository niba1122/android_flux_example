package jp.ibaraki.nobuhito.fluxexample

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import jp.ibaraki.nobuhito.fluxexample.model.Element
import jp.ibaraki.nobuhito.fluxexample.utils.DisposableMapper
import jp.ibaraki.nobuhito.fluxexample.utils.ImmutableVariable
import jp.ibaraki.nobuhito.fluxexample.utils.Variable

class ListReducer(action: Observable<ListActionType>) : DisposableMapper() {
    init {
        action.ofType(ListActionType.StartInitialLoad::class.java)
                .subscribe {
                    mIsInitialLoading.value = true
                }.let { disposables.add(it) }

        action.ofType(ListActionType.SuccessInitialLoad::class.java)
                .subscribe {
                    mIsInitialLoading.value = false
                    mElements.value = it.elements
                }.let { disposables.add(it) }

        action.ofType(ListActionType.StartNextLoad::class.java)
                .subscribe {
                    mIsNextLoading.value = true
                }.let { disposables.add(it) }

        action.ofType(ListActionType.SuccessNextLoad::class.java)
                .subscribe {
                    mIsNextLoading.value = false
                    mElements.value += it.elements
                    page++
                }.let { disposables.add(it) }

        action.ofType(ListActionType.Error::class.java)
                .subscribe {
                    mIsInitialLoading.value = false
                    mIsNextLoading.value = false
                    mError.onNext(it.error)
                }.let { disposables.add(it) }
    }

    var page: Int = 1
        private set

    private val mElements: Variable<List<Element>> = Variable(listOf())
    val elements: ImmutableVariable<List<Element>>
        get() = mElements

    private val mIsInitialLoading: Variable<Boolean> = Variable(false)
    val isInitialLoading: ImmutableVariable<Boolean>
        get() = mIsInitialLoading

    private val mIsNextLoading: Variable<Boolean> = Variable(false)
    val isNextLoading: ImmutableVariable<Boolean>
        get() = mIsNextLoading

    private val mError: PublishSubject<Throwable> = PublishSubject.create()
    val error: Observable<Throwable>
        get() = mError
}
