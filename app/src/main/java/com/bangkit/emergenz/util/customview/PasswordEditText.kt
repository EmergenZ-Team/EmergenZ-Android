package com.bangkit.emergenz.util.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bangkit.emergenz.R

class PasswordEditText : AppCompatEditText{
    private lateinit var warningButtonImage: Drawable
    private lateinit var borderRed: Drawable
    private lateinit var borderBlack: Drawable
    constructor(context : Context) : super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        setupBorder()
        warningButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_warning_amber_24) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                background = borderBlack
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) showWarningEmpty() else if(s.toString().length < 8) showWarningInvalid() else hideWarning()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun setupBorder(){
        borderRed =  ContextCompat.getDrawable(context, R.drawable.border_red) as Drawable
        borderBlack =  ContextCompat.getDrawable(context, R.drawable.border_black) as Drawable
    }

    private fun showWarningInvalid() {
        setButtonDrawables(endOfText = warningButtonImage)
        setError(context.getString(R.string.pw_warning), null)
        background = borderRed
    }

    private fun showWarningEmpty() {
        setButtonDrawables(endOfText = warningButtonImage)
        setError(context.getString(R.string.pw_empty), null)
        background = borderRed
    }

    private fun hideWarning() {
        setButtonDrawables()
        setError(null , null)
    }

    private fun setButtonDrawables(
        startOfText: Drawable? = null,
        topOfText: Drawable? = null,
        endOfText: Drawable? = null,
        bottomOfText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfText,
            topOfText,
            endOfText,
            bottomOfText
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.pw_hint)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}