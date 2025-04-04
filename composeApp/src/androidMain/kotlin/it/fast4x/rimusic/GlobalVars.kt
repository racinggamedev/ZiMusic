package it.fast4x.rimusic

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import it.fast4x.rimusic.enums.AudioQualityFormat
import it.fast4x.rimusic.enums.ColorPaletteMode
import it.fast4x.rimusic.enums.DnsOverHttpsType
import it.fast4x.rimusic.enums.UiType
import it.fast4x.rimusic.enums.ViewType
import it.fast4x.rimusic.ui.styling.LocalAppearance
import it.fast4x.rimusic.utils.UiTypeKey
import it.fast4x.rimusic.utils.audioQualityFormatKey
import it.fast4x.rimusic.utils.autosyncKey
import it.fast4x.rimusic.utils.bassboostEnabledKey
import it.fast4x.rimusic.utils.colorPaletteModeKey
import it.fast4x.rimusic.utils.dnsOverHttpsTypeKey
import it.fast4x.rimusic.utils.enablePreCacheKey
import it.fast4x.rimusic.utils.getEnum
import it.fast4x.rimusic.utils.handleAudioFocusEnabledKey
import it.fast4x.rimusic.utils.isConnectionMetered
import it.fast4x.rimusic.utils.isConnectionMeteredEnabledKey
import it.fast4x.rimusic.utils.logDebugEnabledKey
import it.fast4x.rimusic.utils.parentalControlEnabledKey
import it.fast4x.rimusic.utils.preferences
import it.fast4x.rimusic.utils.rememberPreference
import it.fast4x.rimusic.utils.showButtonPlayerVideoKey
import it.fast4x.rimusic.utils.showSearchTabKey
import it.fast4x.rimusic.utils.showStatsInNavbarKey
import it.fast4x.rimusic.utils.useYtLoginOnlyForBrowseKey
import it.fast4x.rimusic.utils.viewTypeKey
import it.fast4x.rimusic.utils.ytAccountNameKey
import it.fast4x.rimusic.utils.ytAccountThumbnailKey

@Composable
fun typography() = LocalAppearance.current.typography

@Composable
@ReadOnlyComposable
fun colorPalette() = LocalAppearance.current.colorPalette

@Composable
fun thumbnailShape() = LocalAppearance.current.thumbnailShape

@Composable
fun showSearchIconInNav() = rememberPreference( showSearchTabKey, false ).value

@Composable
fun showStatsIconInNav() = rememberPreference( showStatsInNavbarKey, false ).value

@Composable
fun binder() = LocalPlayerServiceBinder.current?.service

fun appContext(): Context = Dependencies.application.applicationContext
fun context(): Context = Dependencies.application

fun getColorTheme() = appContext().preferences.getEnum(colorPaletteModeKey, ColorPaletteMode.Dark)
fun getAudioQualityFormat() = appContext().preferences.getEnum(audioQualityFormatKey, AudioQualityFormat.Auto)
fun getViewType() = appContext().preferences.getEnum(viewTypeKey, ViewType.Grid)
fun getDnsOverHttpsType() = appContext().preferences.getEnum(dnsOverHttpsTypeKey, DnsOverHttpsType.Google)
fun getUiType() = appContext().preferences.getEnum(UiTypeKey, UiType.RiMusic)

fun ytAccountName() = appContext().preferences.getString(ytAccountNameKey, "")
fun ytAccountThumbnail() = appContext().preferences.getString(ytAccountThumbnailKey, "")
fun useYtLoginOnlyForBrowse() = appContext().preferences.getBoolean(useYtLoginOnlyForBrowseKey, true)

fun isVideoEnabled() = appContext().preferences.getBoolean(showButtonPlayerVideoKey, false)
fun isConnectionMetered() = appContext().isConnectionMetered()
fun isConnectionMeteredEnabled() = appContext().preferences.getBoolean(isConnectionMeteredEnabledKey, true)
fun isAutoSyncEnabled() = appContext().preferences.getBoolean(autosyncKey, false)
fun isHandleAudioFocusEnabled() = appContext().preferences.getBoolean(handleAudioFocusEnabledKey, true)
fun isBassBoostEnabled() = appContext().preferences.getBoolean(bassboostEnabledKey, false)
fun isDebugModeEnabled() = appContext().preferences.getBoolean(logDebugEnabledKey, false)
fun isParentalControlEnabled() = appContext().preferences.getBoolean(parentalControlEnabledKey, false)
fun isPreCacheEnabled() = appContext().preferences.getBoolean(enablePreCacheKey, false)
