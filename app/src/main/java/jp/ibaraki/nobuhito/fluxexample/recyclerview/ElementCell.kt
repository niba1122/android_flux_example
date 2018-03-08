package jp.ibaraki.nobuhito.fluxexample.recyclerview

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import jp.ibaraki.nobuhito.fluxexample.model.Element
import kotlinx.android.synthetic.main.view_element_cell.view.*

class ElementCell @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    fun build(element: Element) {
        textView.text = element.number.toString()
    }
}