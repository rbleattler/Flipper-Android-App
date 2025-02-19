package com.flipperdevices.screenstreaming.impl.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.flipperdevices.protobuf.screen.InputKey
import com.flipperdevices.screenstreaming.impl.R
import com.flipperdevices.screenstreaming.impl.model.ButtonAnimEnum

enum class ButtonEnum(
    @DrawableRes val icon: Int,
    @StringRes val description: Int,
    val key: InputKey,
    val animEnum: ButtonAnimEnum
) {
    LEFT(R.drawable.ic_control_left, R.string.control_left, InputKey.LEFT, ButtonAnimEnum.LEFT),
    RIGHT(
        R.drawable.ic_control_right,
        R.string.control_right,
        InputKey.RIGHT,
        ButtonAnimEnum.RIGHT
    ),
    UP(R.drawable.ic_control_up, R.string.control_up, InputKey.UP, ButtonAnimEnum.UP),
    DOWN(R.drawable.ic_control_down, R.string.control_down, InputKey.DOWN, ButtonAnimEnum.DOWN),
    OK(R.drawable.ic_control_ok, R.string.control_ok, InputKey.OK, ButtonAnimEnum.OK),
    BACK(R.drawable.ic_control_back, R.string.control_back, InputKey.BACK, ButtonAnimEnum.BACK)
}
