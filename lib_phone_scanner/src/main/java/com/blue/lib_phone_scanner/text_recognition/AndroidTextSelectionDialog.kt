package com.blue.lib_phone_scanner.text_recognition



import android.app.Dialog
import android.content.Context
import android.content.ClipboardManager
import android.os.Bundle
import android.view.Window
import android.widget.*
import com.blue.lib_phone_scanner.R
import com.blue.lib_phone_scanner.ext.getTextValue

/**
 * 结果选择对话框
 */
class AndroidTextSelectionDialog(
    context: Context,
    private val text: String,
    private val onCopy: (String) -> Unit,
) : Dialog(context) {

    private lateinit var textView: EditText
    private var selectedText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_text_selection)

        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setDimAmount(0.7f)

        textView = findViewById(R.id.tvRecognizedText)
        textView.setText(text)
        textView.setTextIsSelectable(true)

        textView.customSelectionActionModeCallback = object : android.view.ActionMode.Callback {
            override fun onCreateActionMode(
                mode: android.view.ActionMode?,
                menu: android.view.Menu?
            ): Boolean {
                return true
            }

            override fun onPrepareActionMode(
                mode: android.view.ActionMode?,
                menu: android.view.Menu?
            ): Boolean {
                // 清除系统菜单
                menu?.clear()
                return true
            }

            override fun onActionItemClicked(
                mode: android.view.ActionMode?,
                item: android.view.MenuItem?
            ): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: android.view.ActionMode?) {
            }
        }

        // 设置按钮点击事件
        findViewById<TextView>(R.id.btnSelectAll).setOnClickListener {
            textView.selectAll();
        }

        findViewById<TextView>(R.id.btnCopy).setOnClickListener {
            copySelectedText()
        }

        findViewById<TextView>(R.id.btnClose).setOnClickListener {
            dismiss()
        }
    }




    private fun getSelectText(){
        val start = textView.selectionStart
        val end = textView.selectionEnd
        if (start < 0) {
            return
        }
        selectedText = textView.getTextValue().subSequence(start, end).toString()
    }

    private fun copySelectedText() {
        getSelectText()
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(android.content.ClipData.newPlainText("text", selectedText))
        onCopy(selectedText)
        Toast.makeText(context, "已复制到剪贴板[${selectedText}]", Toast.LENGTH_SHORT)
            .show()
        dismiss()
    }



    companion object {
        fun show(
            context: Context,
            text: String,
            onCopy: (String) -> Unit,
        ) {
            AndroidTextSelectionDialog(
                context = context,
                text = text,
                onCopy = onCopy,
            ).show()
        }
    }
}
