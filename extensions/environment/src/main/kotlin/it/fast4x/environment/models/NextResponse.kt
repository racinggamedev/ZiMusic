package it.fast4x.environment.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class NextResponse(
    val contents: Contents?,
    val continuationContents: ContinuationContents?,
    val currentVideoEndpoint: NavigationEndpoint?,
) {
    @Serializable
    data class MusicQueueRenderer(
        val content: Content?
    ) {
        @Serializable
        data class Content(
            @JsonNames("playlistPanelContinuation")
            val playlistPanelRenderer: PlaylistPanelRenderer?
        ) {
            @Serializable
            data class PlaylistPanelRenderer(
                val contents: List<Content>?,
                val continuations: List<Continuation>?,
            ) {
                @Serializable
                data class Content(
                    val playlistPanelVideoRenderer: PlaylistPanelVideoRenderer?,
                    val automixPreviewVideoRenderer: AutomixPreviewVideoRenderer?,
                ) {

                    @Serializable
                    data class AutomixPreviewVideoRenderer(
                        val content: Content?
                    ) {
                        @Serializable
                        data class Content(
                            val automixPlaylistVideoRenderer: AutomixPlaylistVideoRenderer?
                        ) {
                            @Serializable
                            data class AutomixPlaylistVideoRenderer(
                                val navigationEndpoint: NavigationEndpoint?
                            )
                        }
                    }
                }
            }
        }
    }

    @Serializable
    data class Contents(
        val singleColumnMusicWatchNextResultsRenderer: SingleColumnMusicWatchNextResultsRenderer?,
        val twoColumnWatchNextResults: YouTubeDataPage.Contents.TwoColumnWatchNextResults?,
    ) {
        @Serializable
        data class SingleColumnMusicWatchNextResultsRenderer(
            val tabbedRenderer: TabbedRenderer?
        ) {
            @Serializable
            data class TabbedRenderer(
                val watchNextTabbedResultsRenderer: WatchNextTabbedResultsRenderer?
            ) {
                @Serializable
                data class WatchNextTabbedResultsRenderer(
                    val tabs: List<Tab>?
                ) {
                    @Serializable
                    data class Tab(
                        val tabRenderer: TabRenderer?
                    ) {
                        @Serializable
                        data class TabRenderer(
                            val content: Content?,
                            val endpoint: NavigationEndpoint?,
                            val title: String?
                        ) {
                            @Serializable
                            data class Content(
                                val musicQueueRenderer: MusicQueueRenderer?
                            )
                        }
                    }
                }
            }
        }
    }

    @Serializable
    data class ContinuationContents(
        val playlistPanelContinuation: MusicQueueRenderer.Content.PlaylistPanelRenderer?,
        val sectionListContinuation: BrowseResponse.ContinuationContents.SectionListContinuation?,
        val musicPlaylistShelfContinuation: BrowseResponse.ContinuationContents.MusicPlaylistShelfContinuation?,
    )


}
