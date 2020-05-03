package io.mellouk.view;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.mellouk.common.domain.CurrentMusicDataState;
import io.mellouk.common.domain.Music;
import io.mellouk.view.domain.GetCurrentPlayingMusicUseCase;
import io.reactivex.Observable;
import io.reactivex.Single;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

public class MainViewModelTest extends BaseTest {
    @InjectMocks
    MainViewModel viewModel;
    @Mock
    GetCurrentPlayingMusicUseCase getCurrentPlayingMusicUseCase;
    @Mock
    ViewStateMapper viewStateMapper;
    @Mock
    Music music;
    private final CurrentMusicDataState.Successful givenCurrentSuccessfulDataState =
            new CurrentMusicDataState.Successful(music);

    @Test
    public void onInitCommand_ShouldInitializeCorrectly() {
        final ViewState.INITIAL initialViewState =
                new ViewState.INITIAL(givenCurrentSuccessfulDataState.getMusic());
        final Observable<CurrentMusicDataState> observable = Single.just((CurrentMusicDataState) givenCurrentSuccessfulDataState).toObservable();
        when(getCurrentPlayingMusicUseCase.buildObservable())
                .thenReturn(observable);

        when(viewStateMapper.map(givenCurrentSuccessfulDataState))
                .thenReturn(initialViewState);

        viewModel.onCommand(Command.INIT);
        then(getCurrentPlayingMusicUseCase).should(once).buildObservable();
        then(viewStateMapper).should(once).map(givenCurrentSuccessfulDataState);
    }
}
