package com.volcode.coreui.views

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView

class CenteringToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialToolbar(context, attrs, defStyleAttr) {

    companion object {
        const val TITLE_TEXT_VIEW_TAG = "title_text_view_tag"
    }

    private var titleTextView: MaterialTextView? = null

    override fun setTitle(resId: Int) {
        title = resources.getText(resId)
    }

    override fun setTitle(title: CharSequence?) {
        if (title.isNullOrEmpty()) {
            titleTextView?.text = title
            return
        }
        if (titleTextView == null) {
            titleTextView = MaterialTextView(context, null).apply {
                setSingleLine()
                ellipsize = TextUtils.TruncateAt.END
                text = title
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                ).also {
                    it.gravity = Gravity.CENTER
                }
                gravity = Gravity.CENTER
                tag = TITLE_TEXT_VIEW_TAG
            }
            addView(titleTextView)
        } else {
            requireNotNull(titleTextView).text = title
        }
    }
}
