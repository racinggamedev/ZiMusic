package it.fast4x.rimusic.ui.items

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.fast4x.innertube.Innertube
import it.fast4x.rimusic.Database
import it.fast4x.rimusic.R
import it.fast4x.rimusic.models.PlaylistPreview
import it.fast4x.rimusic.ui.components.themed.TextPlaceholder
import it.fast4x.rimusic.ui.screens.home.PINNED_PREFIX
import it.fast4x.rimusic.ui.styling.LocalAppearance
import it.fast4x.rimusic.ui.styling.onOverlay
import it.fast4x.rimusic.ui.styling.overlay
import it.fast4x.rimusic.ui.styling.shimmer
import it.fast4x.rimusic.utils.MONTHLY_PREFIX
import it.fast4x.rimusic.utils.color
import it.fast4x.rimusic.utils.getTitleMonthlyPlaylist
import it.fast4x.rimusic.utils.medium
import it.fast4x.rimusic.utils.secondary
import it.fast4x.rimusic.utils.semiBold
import it.fast4x.rimusic.utils.thumbnail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import timber.log.Timber

@Composable
fun PlaylistItem(
    @DrawableRes icon: Int,
    colorTint: Color,
    name: String?,
    songCount: Int?,
    thumbnailSizeDp: Dp,
    modifier: Modifier = Modifier,
    alternative: Boolean = false,
    showName: Boolean = true,
    iconSize: Dp = 34.dp
) {
    PlaylistItem(
        thumbnailContent = {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorTint),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(iconSize)
            )
        },
        songCount = songCount,
        name = name,
        channelName = null,
        thumbnailSizeDp = thumbnailSizeDp,
        modifier = modifier,
        alternative = alternative,
        showName = showName
    )
}

@Composable
fun PlaylistItem(
    playlist: PlaylistPreview,
    thumbnailSizePx: Int,
    thumbnailSizeDp: Dp,
    modifier: Modifier = Modifier,
    alternative: Boolean = false,
    showName: Boolean = true
) {
    val thumbnails by remember {
        Database.playlistThumbnailUrls(playlist.playlist.id).distinctUntilChanged().map {
            it.map { url ->
                url.thumbnail(thumbnailSizePx / 2)
            }
        }
    }.collectAsState(initial = emptyList(), context = Dispatchers.IO)

    PlaylistItem(
        thumbnailContent = {
            if (thumbnails.toSet().size == 1) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(thumbnails.first())
                        .setHeader("User-Agent", "Mozilla/5.0")
                        .build(), //thumbnails.first().thumbnail(thumbnailSizePx),
                    onError = {error ->
                        Timber.e(error.result.throwable.message)
                    },
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = it
                )
            } else {
                Box(
                    modifier = it
                        .fillMaxSize()
                ) {
                    listOf(
                        Alignment.TopStart,
                        Alignment.TopEnd,
                        Alignment.BottomStart,
                        Alignment.BottomEnd
                    ).forEachIndexed { index, alignment ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(thumbnails.getOrNull(index))
                                .setHeader("User-Agent", "Mozilla/5.0")
                                .build(),
                            onError = {error ->
                                Timber.e(error.result.throwable.message)
                            },
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .align(alignment)
                                .size(thumbnailSizeDp / 2)
                        )
                    }
                }
            }
        },
        songCount = playlist.songCount,
        name = playlist.playlist.name,
        channelName = null,
        thumbnailSizeDp = thumbnailSizeDp,
        modifier = modifier,
        alternative = alternative,
        showName = showName
    )
}

@Composable
fun PlaylistItem(
    playlist: Innertube.PlaylistItem,
    thumbnailSizePx: Int,
    thumbnailSizeDp: Dp,
    modifier: Modifier = Modifier,
    alternative: Boolean = false,
    showSongsCount: Boolean = true
) {
    PlaylistItem(
        thumbnailUrl = playlist.thumbnail?.url,
        songCount = playlist.songCount,
        showSongsCount = showSongsCount,
        name = playlist.info?.name,
        channelName = playlist.channel?.name,
        thumbnailSizePx = thumbnailSizePx,
        thumbnailSizeDp = thumbnailSizeDp,
        modifier = modifier,
        alternative = alternative
    )
}

@Composable
fun PlaylistItem(
    thumbnailUrl: String?,
    songCount: Int?,
    name: String?,
    channelName: String?,
    thumbnailSizePx: Int,
    thumbnailSizeDp: Dp,
    modifier: Modifier = Modifier,
    alternative: Boolean = false,
    showSongsCount: Boolean = true
) {
    PlaylistItem(
        thumbnailContent = {
            AsyncImage(
                model = thumbnailUrl?.thumbnail(thumbnailSizePx),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = it
            )
        },
        songCount = songCount,
        showSongsCount = showSongsCount,
        name = name,
        channelName = channelName,
        thumbnailSizeDp = thumbnailSizeDp,
        modifier = modifier,
        alternative = alternative,
    )
}

@Composable
fun PlaylistItem(
    thumbnailContent: @Composable BoxScope.(modifier: Modifier) -> Unit,
    songCount: Int?,
    name: String?,
    channelName: String?,
    thumbnailSizeDp: Dp,
    modifier: Modifier = Modifier,
    alternative: Boolean = false,
    showName: Boolean = true,
    showSongsCount: Boolean = true
) {
    val (colorPalette, typography, thumbnailShape) = LocalAppearance.current

    ItemContainer(
        alternative = alternative,
        thumbnailSizeDp = thumbnailSizeDp,
        modifier = modifier
    ) { centeredModifier ->
        Box(
            modifier = centeredModifier
                .clip(thumbnailShape)
                .background(color = colorPalette.background4)
                .requiredSize(thumbnailSizeDp)
        ) {
            thumbnailContent(
                modifier = Modifier
                    .fillMaxSize()
            )

            name?.let {
                if (it.startsWith(PINNED_PREFIX,0,true)) {
                    Image(
                        painter = painterResource(R.drawable.pin),
                        colorFilter = ColorFilter.tint(colorPalette.accent),
                        modifier = Modifier
                            .size(40.dp)
                            .padding(all = 5.dp),
                        contentDescription = "Background Image",
                        contentScale = ContentScale.Fit
                    )
                }
                if (it.startsWith(MONTHLY_PREFIX,0,true)) {
                    Image(
                        painter = painterResource(R.drawable.stat_month),
                        colorFilter = ColorFilter.tint(colorPalette.accent),
                        modifier = Modifier
                            .size(40.dp)
                            .padding(all = 5.dp),
                        contentDescription = "Background Image",
                        contentScale = ContentScale.Fit
                    )
                }
            }


            if (showSongsCount)
                songCount?.let {
                    BasicText(
                        text = "$songCount",
                        style = typography.xxs.medium.color(colorPalette.onOverlay),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .background(color = colorPalette.overlay, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 6.dp)
                            .align(Alignment.BottomEnd)
                    )
                }

        }


        ItemInfoContainer(
            horizontalAlignment = if (alternative && channelName == null) Alignment.CenterHorizontally else Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (showName)
                if (name != null) {
                    BasicText(
                        //text = name.substringAfter(PINNED_PREFIX) ?: "",
                        text = if (name.startsWith(PINNED_PREFIX,0,true))
                            name.substringAfter(PINNED_PREFIX) else
                            if (name.startsWith(MONTHLY_PREFIX,0,true))
                                getTitleMonthlyPlaylist(name.substringAfter(MONTHLY_PREFIX)) else name,
                        style = typography.xs.semiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

            channelName?.let {
                BasicText(
                    text = channelName,
                    style = typography.xs.semiBold.secondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun PlaylistItemPlaceholder(
    thumbnailSizeDp: Dp,
    modifier: Modifier = Modifier,
    alternative: Boolean = false,
) {
    val (colorPalette, _, thumbnailShape) = LocalAppearance.current

    ItemContainer(
        alternative = alternative,
        thumbnailSizeDp = thumbnailSizeDp,
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .background(color = colorPalette.shimmer, shape = thumbnailShape)
                .size(thumbnailSizeDp)
        )

        ItemInfoContainer(
            horizontalAlignment = if (alternative) Alignment.CenterHorizontally else Alignment.Start,
        ) {
            TextPlaceholder()
            TextPlaceholder()
        }
    }
}
