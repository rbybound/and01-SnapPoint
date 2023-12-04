package com.boostcampwm2023.snappoint.presentation.main

import androidx.lifecycle.ViewModel
import com.boostcampwm2023.snappoint.data.repository.PostRepository
import com.boostcampwm2023.snappoint.presentation.main.search.SearchViewUiState
import com.boostcampwm2023.snappoint.presentation.model.PositionState
import com.boostcampwm2023.snappoint.presentation.model.PostBlockState
import com.boostcampwm2023.snappoint.presentation.model.PostSummaryState
import com.boostcampwm2023.snappoint.presentation.model.SnapPointTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val postRepository: PostRepository
) :ViewModel(){

    private val _postState: MutableStateFlow<List<PostSummaryState>> = MutableStateFlow(emptyList())
    val postState: StateFlow<List<PostSummaryState>> = _postState.asStateFlow()

    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _searchViewUiState: MutableStateFlow<SearchViewUiState> = MutableStateFlow(
        SearchViewUiState(onAutoCompleteItemClicked = { index ->
            moveCameraToAddress(index)
        })
    )
    val searchViewUiState: StateFlow<SearchViewUiState> = _searchViewUiState.asStateFlow()

    private val _event: MutableSharedFlow<MainActivityEvent> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event: SharedFlow<MainActivityEvent> = _event.asSharedFlow()

    var bottomSheetHeight: Int = 0

    init {
        loadPosts()
    }

    private fun loadPosts() {
        _postState.update {
            listOf(
                PostSummaryState(
                    title = "하이",
                    author = "원승빈",
                    timeStamp = "2 Days Ago",
                    postBlocks = listOf(
                        PostBlockState.TEXT(
                            content = "안녕하세요, 하하"
                        ),
                        PostBlockState.IMAGE(
                            content = "https://health.chosun.com/site/data/img_dir/2023/07/17/2023071701753_0.jpg",
                            position = PositionState(37.421793077676774, -122.09180117366115),
                            description = "고양이입니다.",
                            address = "null"
                        ),
                        PostBlockState.IMAGE(
                            content = "https://pds.joongang.co.kr/news/component/htmlphoto_mmdata/201901/20/28017477-0365-4a43-b546-008b603da621.jpg",
                            position = PositionState(37.41887606344049, -122.0879954078449),
                            description = "강아징입니다.",
                            address = "null"
                        ),
                        PostBlockState.TEXT(
                            content = "ㅎㅇ염"
                        ),
                        PostBlockState.TEXT(
                            content = "동물원갔다왔슴다 ㅋ"
                        ),
                        PostBlockState.IMAGE(
                            content = "https://i.namu.wiki/i/Nvsy3_i1lyInOB79UBbcDeR6MocJ4C8TBN8NjepPwqTnojCbb3Xwge9gQXfAGgW74ZA3c3i16odhBLE0bSwgFA.webp",
                            position = PositionState(37.42155682099068, -122.08342886715077),
                            description = "이것은 악어~",
                            address = "null"
                        ),
                        PostBlockState.IMAGE(
                            content = "https://upload.wikimedia.org/wikipedia/commons/4/41/Siberischer_tiger_de_edit02.jpg",
                            position = PositionState(37.4227919844394, -122.08028507548029),
                            description = "어흥",
                            address = "null"
                        ),
                    )
                ),
                PostSummaryState(
                    title = "여름 철새 구경",
                    author = "익명",
                    timeStamp = "3 Weeks Ago",
                    postBlocks = listOf(
                        PostBlockState.TEXT(
                            content = "123"
                        ),
                        PostBlockState.IMAGE(
                            content = "https://upload.wikimedia.org/wikipedia/commons/8/85/Columbina_passerina.jpg",
                            position = PositionState(37.40837052881207, -122.10293026989889),
                            description = "비둘기야 먹자 구구구구",
                            address = "address"
                        ),
                        PostBlockState.IMAGE(
                            content = "https://upload.wikimedia.org/wikipedia/commons/f/f0/Nipponia_nippon_20091230131054.png",
                            position = PositionState(37.40272239693208, -122.06577824456974),
                            description = "떴따 오기",
                            address = "address"
                        ),
                        PostBlockState.IMAGE(
                            content = "https://upload.wikimedia.org/wikipedia/commons/8/82/Watercock_%28Gallicrex_cinerea%29.jpg",
                            position = PositionState(37.414911773823924, -122.0536102126485),
                            description = "뜸 부기",
                            address = "address"
                        ),
                    )
                ),
            )
        }
    }

    // TODO DataStore 확인을 위한 임시 코드
    fun clearPosts() {
        _postState.update {
            listOf()
        }
    }

    fun drawerIconClicked() {
        _event.tryEmit(MainActivityEvent.OpenDrawer)
    }

    fun appbarBackIconClicked() {
        _event.tryEmit(MainActivityEvent.NavigatePrev)
    }

    fun appbarCloseIconClicked() {
        _event.tryEmit(MainActivityEvent.NavigateClose)
    }

    fun previewButtonClicked(index: Int) {
        updateSelectedIndex(index = index)
        _event.tryEmit(MainActivityEvent.NavigatePreview(index))
    }

    fun onPreviewFragmentShowing() {
        _uiState.update {
            it.copy(
                isPreviewFragmentShowing = true
            )
        }
    }

    private fun updateSelectedIndex(index: Int){
        _uiState.update {
            it.copy(
                selectedIndex = index,
                focusedIndex = 0
            )
        }
    }

    fun onPreviewFragmentClosing() {
        _uiState.update {
            it.copy(
                isPreviewFragmentShowing = false,
                selectedIndex = -1,
                focusedIndex = -1
            )
        }
    }

    fun onBottomSheetChanged(isExpanded: Boolean) {
        _uiState.update {
            it.copy(isBottomSheetExpanded = isExpanded)
        }
    }

    fun onMarkerClicked(tag: SnapPointTag) {
        updateClickedSnapPoint(tag.postIndex, tag.snapPointIndex)
        _event.tryEmit(MainActivityEvent.NavigatePreview(tag.postIndex))
    }

    fun focusOfImageMoved(imageIndex: Int) {
        updateClickedSnapPoint(_uiState.value.selectedIndex, imageIndex)
    }

    private fun updateClickedSnapPoint(postIndex: Int, snapPointIndex: Int) {
        _uiState.update {
            it.copy(
                selectedIndex = postIndex,
                focusedIndex = snapPointIndex)
        }
    }

    fun updateAutoCompleteTexts(texts: List<String>) {
        _searchViewUiState.update {
            it.copy(texts = texts)
        }
    }

    private fun moveCameraToAddress(index: Int) {
        _event.tryEmit(MainActivityEvent.MoveCameraToAddress(index))
    }

    fun navigateSignIn() {
        _event.tryEmit(MainActivityEvent.NavigateSignIn)
    }

    fun onMapReady() {
        _event.tryEmit(MainActivityEvent.CheckPermissionAndMoveCameraToUserLocation)
    }
}