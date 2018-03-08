package jp.ibaraki.nobuhito.fluxexample

import jp.ibaraki.nobuhito.fluxexample.model.Element


sealed class ListActionType {
    class StartInitialLoad : ListActionType()
    class SuccessInitialLoad(val elements: List<Element>) : ListActionType()
    class StartNextLoad : ListActionType()
    class SuccessNextLoad(val elements: List<Element>) : ListActionType()
    class Error(val error: Throwable) : ListActionType()
}