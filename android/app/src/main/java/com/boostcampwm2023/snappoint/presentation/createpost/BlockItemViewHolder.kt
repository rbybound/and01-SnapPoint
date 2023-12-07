package com.boostcampwm2023.snappoint.presentation.createpost

import android.graphics.Bitmap
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.boostcampwm2023.snappoint.databinding.ItemImageBlockBinding
import com.boostcampwm2023.snappoint.databinding.ItemTextBlockBinding
import com.boostcampwm2023.snappoint.presentation.model.PostBlockCreationState
import com.boostcampwm2023.snappoint.presentation.util.resizeBitmap
import com.google.android.material.card.MaterialCardView

sealed class BlockItemViewHolder(
    binding: ViewDataBinding,
    blockItemEvent: BlockItemEventListener,
) : RecyclerView.ViewHolder(binding.root) {

    val textWatcher = EditTextWatcher(blockItemEvent.onTextChange)

    abstract fun attachTextWatcherToEditText()
    abstract fun detachTextWatcherFromEditText()

    class TextBlockViewHolder(
        private val binding: ItemTextBlockBinding,
        private val blockItemEvent: BlockItemEventListener,
    ) : BlockItemViewHolder(binding, blockItemEvent) {

        fun bind(block: PostBlockCreationState.TEXT, index: Int) {
            with(binding) {
                tilText.editText?.setText(block.content)
                btnDeleteBlock.setOnClickListener { blockItemEvent.onDeleteButtonClick(index) }
                btnEditBlock.setOnClickListener {
                    itemView.rootView.clearFocus()
                    blockItemEvent.onEditButtonClick(index)
                }
                btnEditComplete.setOnClickListener { blockItemEvent.onCheckButtonClick(index) }
                btnUp.setOnClickListener {
                    itemView.rootView.clearFocus()
                    blockItemEvent.onUpButtonClick(index)
                }
                btnDown.setOnClickListener {
                    itemView.rootView.clearFocus()
                    blockItemEvent.onDownButtonClick(index)
                }
                editMode = block.isEditMode
            }
            textWatcher.updatePosition(index)
        }

        override fun attachTextWatcherToEditText() {
            binding.tilText.editText?.addTextChangedListener(textWatcher)
        }

        override fun detachTextWatcherFromEditText() {
            binding.tilText.editText?.removeTextChangedListener(textWatcher)
        }
    }

    class ImageBlockViewHolder(
        private val binding: ItemImageBlockBinding,
        private val blockItemEvent: BlockItemEventListener,
    ) : BlockItemViewHolder(binding, blockItemEvent) {

        fun bind(block: PostBlockCreationState.IMAGE, index: Int) {
            with(binding) {
                tilDescription.editText?.setText(block.description)
                tilAddress.editText?.setText(block.address)
                btnDeleteBlock.setOnClickListener { blockItemEvent.onDeleteButtonClick(index) }
                btnEditBlock.setOnClickListener {
                    itemView.rootView.clearFocus()
                    blockItemEvent.onEditButtonClick(index)
                }
                tilAddress.setEndIconOnClickListener { blockItemEvent.onAddressIconClick(index) }
                btnEditComplete.setOnClickListener { blockItemEvent.onCheckButtonClick(index) }
                btnUp.setOnClickListener {
                    itemView.rootView.clearFocus()
                    blockItemEvent.onUpButtonClick(index)
                }
                btnDown.setOnClickListener {
                    itemView.rootView.clearFocus()
                    blockItemEvent.onDownButtonClick(index)
                }
                editMode = block.isEditMode
                bitmap = block.bitmap
            }
            textWatcher.updatePosition(index)
        }

        override fun attachTextWatcherToEditText() {
            binding.tilDescription.editText?.addTextChangedListener(textWatcher)
        }

        override fun detachTextWatcherFromEditText() {
            binding.tilDescription.editText?.removeTextChangedListener(textWatcher)
        }
    }
}


class EditTextWatcher(private val listener: (Int, String) -> Unit) : TextWatcher {

    private var position = 0

    fun updatePosition(position: Int) {
        this.position = position
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(changedText: Editable?) {
        listener(position, changedText.toString())
    }
}


@BindingAdapter("bitmap")
fun ImageView.bindBitmap(bitmap: Bitmap) {
    load(resizeBitmap(bitmap, width))
}

@BindingAdapter("editMode")
fun MaterialCardView.isEditMode(editMode: Boolean) {
    val value = TypedValue()
    if (editMode) {
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorSecondary, value, true)
        strokeWidth = 4
    } else {
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOutline, value, true)
        strokeWidth = 1
    }
    strokeColor = value.data
}