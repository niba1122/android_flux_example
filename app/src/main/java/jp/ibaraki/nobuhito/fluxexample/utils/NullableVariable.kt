package jp.ibaraki.nobuhito.fluxexample.utils

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

interface ImmutableNullableVariable<T> {
    val value: T?
    val observable: Observable<Wrapper<T>>
}

@Suppress("unused")
sealed class Wrapper<T> {
    class Some<T>(val value: T) : Wrapper<T>()
    class None<T> : Wrapper<T>()

    fun unwrap(): T? = when (this) {
        is Some -> this.value
        is None -> null
    }
}

class NullableVariable<T>(initialValue: T?) : ImmutableNullableVariable<T> {
    private val subject = BehaviorSubject.createDefault<Wrapper<T>>(
            if (initialValue != null) {
                Wrapper.Some(initialValue)
            } else {
                Wrapper.None()
            })

    override var value: T?
        get() = subject.value.let {
            when (it) {
                is Wrapper.Some -> it.value
                is Wrapper.None -> null
            }
        }
        set(value) {
            if (value != null) {
                subject.toSerialized().onNext(Wrapper.Some(value))
            } else {
                subject.toSerialized().onNext(Wrapper.None())
            }
        }

    override val observable: Observable<Wrapper<T>>
        get() = subject.toSerialized().distinctUntilChanged()
}
