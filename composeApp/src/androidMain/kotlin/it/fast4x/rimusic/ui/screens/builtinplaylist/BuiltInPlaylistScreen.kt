package it.fast4x.rimusic.ui.screens.builtinplaylist

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import it.fast4x.compose.persist.PersistMapCleanup
import it.fast4x.rimusic.R
import it.fast4x.rimusic.enums.BuiltInPlaylist
import it.fast4x.rimusic.enums.DeviceLists
import it.fast4x.rimusic.enums.MaxTopPlaylistItems
import it.fast4x.rimusic.enums.NavRoutes
import it.fast4x.rimusic.ui.screens.ondevice.DeviceListSongs
import it.fast4x.rimusic.utils.MaxTopPlaylistItemsKey
import it.fast4x.rimusic.utils.rememberPreference
import it.fast4x.rimusic.utils.showCachedPlaylistKey
import it.fast4x.rimusic.utils.showDownloadedPlaylistKey
import it.fast4x.rimusic.utils.showFavoritesPlaylistKey
import it.fast4x.rimusic.utils.showMyTopPlaylistKey
import it.fast4x.rimusic.utils.showOnDevicePlaylistKey
import it.fast4x.rimusic.ui.components.Skeleton

@ExperimentalMaterialApi
@ExperimentalTextApi
@SuppressLint("SuspiciousIndentation")
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@UnstableApi
@Composable
fun BuiltInPlaylistScreen(
    navController: NavController,
    builtInPlaylist: BuiltInPlaylist,
    miniPlayer: @Composable () -> Unit = {},
) {
    val saveableStateHolder = rememberSaveableStateHolder()

    val (tabIndex, onTabIndexChanged) = rememberSaveable {
        mutableStateOf(when (builtInPlaylist) {
            BuiltInPlaylist.Favorites -> 0
            BuiltInPlaylist.Offline -> 1
            BuiltInPlaylist.Downloaded -> 2
            BuiltInPlaylist.Top -> 3
            BuiltInPlaylist.OnDevice -> 4
            else -> 5
        })
    }

    val maxTopPlaylistItems by rememberPreference(
        MaxTopPlaylistItemsKey,
        MaxTopPlaylistItems.`10`
    )
    val showFavoritesPlaylist by rememberPreference(showFavoritesPlaylistKey, true)
    val showCachedPlaylist by rememberPreference(showCachedPlaylistKey, true)
    val showMyTopPlaylist by rememberPreference(showMyTopPlaylistKey, true)
    val showDownloadedPlaylist by rememberPreference(showDownloadedPlaylistKey, true)
    val showOnDevicePlaylist by rememberPreference(showOnDevicePlaylistKey, true)

    PersistMapCleanup(tagPrefix = "${builtInPlaylist.name}/")

            Skeleton(
                navController,
                tabIndex,
                onTabIndexChanged,
                miniPlayer,
                navBarContent = { item ->
                    if(showFavoritesPlaylist)
                        item(0, stringResource(R.string.favorites), R.drawable.heart)
                    if(showCachedPlaylist)
                        item(1, stringResource(R.string.cached), R.drawable.sync)
                    if(showDownloadedPlaylist)
                        item(2, stringResource(R.string.downloaded), R.drawable.downloaded)
                    if(showMyTopPlaylist)
                        item(3, stringResource(R.string.my_playlist_top).format(maxTopPlaylistItems.number) , R.drawable.trending)
                    if(showOnDevicePlaylist)
                        item(4, stringResource(R.string.on_device), R.drawable.musical_notes)
                }
            ) { currentTabIndex ->
                saveableStateHolder.SaveableStateProvider(key = currentTabIndex) {
                    val playlist: BuiltInPlaylist =
                        when( currentTabIndex ) {
                            0 -> BuiltInPlaylist.Favorites
                            1 -> BuiltInPlaylist.Offline
                            2 -> BuiltInPlaylist.Downloaded
                            3 -> BuiltInPlaylist.Top
                            else -> BuiltInPlaylist.OnDevice
                        }

                    if( playlist == BuiltInPlaylist.OnDevice )
                        DeviceListSongs(
                            navController = navController,
                            deviceLists = DeviceLists.LocalSongs,
                            onSearchClick = { navController.navigate(NavRoutes.search.name) }
                        )
                    else
                        BuiltInPlaylistSongs(
                            navController = navController,
                            builtInPlaylist = playlist,
                            onSearchClick = { navController.navigate(NavRoutes.search.name) }
                        )
                }
            }

}
