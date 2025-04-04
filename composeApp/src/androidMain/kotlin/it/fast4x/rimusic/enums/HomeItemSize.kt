package it.fast4x.rimusic.enums

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.fast4x.rimusic.R
import it.fast4x.rimusic.ui.styling.px

enum class HomeItemSize ( val textId: Int, val size: Int ) {
    SMALL( R.string.small, 104 ),
    MEDIUM( R.string.medium,132 ),
    BIG( R.string.big, 162 );

    val iconId = R.drawable.arrow_forward

    val dp: Dp = this.size.dp
    val px: Int
        @Composable
        get() = this.dp.px
}