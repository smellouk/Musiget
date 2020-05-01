package io.mellouk.repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.mellouk.repository.disk.MusicFetcher;
import io.mellouk.repository.entity.MusicEntity;
import io.mellouk.repository.repos.MusicRepositoryImpl;
import io.reactivex.Single;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class MusicRepositoryTest extends BaseTest {
    private final MusicEntity givenEntity = new MusicEntity(-1, "", "", "", "");
    private final List<MusicEntity> givenMusicEntityList = new ArrayList<>();

    @Mock
    MusicFetcher musicFetcher;

    @InjectMocks
    MusicRepositoryImpl musicRepository;

    @Before
    @Override
    public void setup() {
        super.setup();
        givenMusicEntityList.add(givenEntity);
    }

    @Test
    public void getMusic_ShouldReturnSuccessfulListOfMusic() {
        given(musicFetcher.getMusic()).willReturn(Single.just(givenMusicEntityList));

        musicRepository.getMusicList()
                .test()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(value -> !value.isEmpty() && value.get(0).equals(givenEntity));

        then(musicFetcher).should(once).getMusic();
    }
}
