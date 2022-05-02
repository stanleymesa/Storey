package com.example.storey.ui.customview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import com.example.storey.R
import com.example.storey.data.local.statics.StaticData
import com.example.storey.helper.getColorFromAttr
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class CustomPasswordEditText : TextInputLayout, TextWatcher {

    private lateinit var inputEditText: TextInputEditText
    private lateinit var colorStroke: ColorStateList
    private var colorPrimary: Int = 0
    private var colorPrimaryVariant: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        init()
    }

    private fun init() {
        inputEditText = TextInputEditText(context)
        inputEditText.inputType = InputType.TYPE_CLASS_TEXT
        addView(inputEditText)
        endIconMode = END_ICON_PASSWORD_TOGGLE
        errorIconDrawable = null
        colorPrimaryVariant = context.getColorFromAttr(R.attr.colorPrimaryVariant)
        colorPrimary = context.getColorFromAttr(R.attr.colorPrimary)
        colorStroke = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_focused),
                intArrayOf(android.R.attr.state_focused),
            ),

            intArrayOf(
                colorPrimaryVariant,
                colorPrimary
            )
        )

        inputEditText.addTextChangedListener(this)


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setEndIconTintList(colorStroke)
        defaultHintTextColor = ColorStateList.valueOf(colorPrimaryVariant)
        hintTextColor = ColorStateList.valueOf(colorPrimary)
        setBoxStrokeColorStateList(colorStroke)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, textLength: Int) {
        error = when {
            text!!.length < StaticData.MINIMUM_PASSWORD_CHARS -> resources.getString(R.string.password_error)
            else -> null
        }
    }

    override fun afterTextChanged(p0: Editable?) {}

}