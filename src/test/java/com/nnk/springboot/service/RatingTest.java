package com.nnk.springboot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;

@ExtendWith(MockitoExtension.class)
public class RatingTest {

	@Mock
	private RatingRepository ratingRepository;

	@InjectMocks
	private RatingService ratingService;

	private Rating rating;

	@BeforeEach
	void setUp() {
		rating = new Rating();
		rating.setId(1);
		rating.setFitchRating("Fitch Rating");
		rating.setMoodysRating("Moodys Rating");
		rating.setSandPRating("S&P Rating");
	}

	@Test
	public void testGetAllRatings_WhenSuccess() {
		List<Rating> ratings = new ArrayList<>();

		ratings.add(rating);

		when(ratingRepository.findAll()).thenReturn(ratings);

		List<Rating> actualRatings = ratingService.getAllRatings();

		assertEquals(ratings.size(), actualRatings.size());
		assertEquals(ratings, actualRatings);
		verify(ratingRepository, times(1)).findAll();
	}

	@Test
	public void testSaveRating_WhenSuccess() {
		when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

		Rating savedRating = ratingService.saveRating(rating);

		assertEquals(rating.getMoodysRating(), savedRating.getMoodysRating());
		verify(ratingRepository, times(1)).save(any(Rating.class));
	}

	@Test
	public void testSaveRating_WhenException() {
		doThrow(new RuntimeException("Erreur lors de la sauvegarde")).when(ratingRepository).save(any(Rating.class));

		assertThrows(RuntimeException.class, () -> {
			ratingService.saveRating(rating);
		});
	}

	@Test
	public void testGetRatingById_WhenSuccess() {
		when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(rating));

		Rating foundRating = ratingService.getRatingById(1);

		assertEquals(1, foundRating.getId());
		assertEquals("Moodys Rating", foundRating.getMoodysRating());

		verify(ratingRepository, times(1)).findById(anyInt());
	}

	@Test
	public void testGetRatingById_WhenException() {
		when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());

		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			ratingService.getRatingById(15);
		});

		assertEquals("No rating with this ID", thrown.getMessage());
		verify(ratingRepository, times(1)).findById(anyInt());
	}

	@Test
	public void testUpdateRating_WhenSuccess() {
		when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(rating));

		Rating newRatingData = new Rating();
		newRatingData.setMoodysRating("New Moodys");
		newRatingData.setOrderNumber(12);

		Rating expectedSavedRating = new Rating();
		expectedSavedRating.setId(rating.getId());
		expectedSavedRating.setMoodysRating(newRatingData.getMoodysRating());
		expectedSavedRating.setOrderNumber(newRatingData.getOrderNumber());

		when(ratingRepository.save(any(Rating.class))).thenReturn(expectedSavedRating);

		Rating actualUpdateRating = ratingService.updateRating(rating.getId(), newRatingData);

		assertEquals(expectedSavedRating.getOrderNumber(), actualUpdateRating.getOrderNumber());
		assertEquals(expectedSavedRating.getMoodysRating(), actualUpdateRating.getMoodysRating());

		verify(ratingRepository, times(1)).findById(anyInt());
		verify(ratingRepository, times(1)).save(any(Rating.class));
	}

	@Test
	public void testUpdateRating_whenExceptionDuringSave() {
		when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(rating));

		Rating newRatingData = new Rating();
		newRatingData.setMoodysRating("New Moodys");
		newRatingData.setOrderNumber(12);

		doThrow(new RuntimeException("Erreur lors de la sauvegarde de la mise à jour")).when(ratingRepository)
				.save(any(Rating.class));

		assertThrows(RuntimeException.class, () -> {
			ratingService.updateRating(rating.getId(), newRatingData);
		});

		verify(ratingRepository, times(1)).findById(anyInt());
		verify(ratingRepository, times(1)).save(any(Rating.class));
	}

	@Test
	public void testUpdateRating_WhenNotFound() {
		when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());

		Rating newRatingData = new Rating();
		newRatingData.setMoodysRating("New Moodys");
		newRatingData.setOrderNumber(12);

		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			ratingService.updateRating(1, newRatingData);
		});

		assertEquals("Aucun rating existant", thrown.getMessage());
		verify(ratingRepository, times(1)).findById(anyInt());
		verify(ratingRepository, never()).save(any(Rating.class));
	}

	@Test
	public void testDeleteRating_WhenSuccess() {
		when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(rating));

		ratingService.deleteRatingById(rating.getId());

		verify(ratingRepository, times(1)).findById(anyInt());
		verify(ratingRepository, times(1)).deleteById(anyInt());
	}

	@Test
	public void testDeleteRating_WhenNotFound() {
		when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());

		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			ratingService.deleteRatingById(2);
		});
		verify(ratingRepository, times(1)).findById(anyInt());
		verify(ratingRepository, never()).delete(any(Rating.class));
	}
	
	@Test
    public void testRatingGettersAndSetters() {
        Rating rating2 = new Rating();

        rating2.setId(1);
        rating2.setMoodysRating("Moodys Rating Test");
        rating2.setSandPRating("S&P Rating Test");
        rating2.setFitchRating("Fitch Rating Test");
        rating2.setOrderNumber(10);

        assertEquals(1, rating2.getId());
        assertEquals("Moodys Rating Test", rating2.getMoodysRating());
        assertEquals("S&P Rating Test", rating2.getSandPRating());
        assertEquals("Fitch Rating Test", rating2.getFitchRating());
        assertEquals(10, rating2.getOrderNumber());
    }
	
	 @Test
	    public void testRatingEqualsAndHashCode() {
	        Rating rating1 = new Rating(1, "Moodys", "S&P", "Fitch", 10);
	        Rating rating2 = new Rating(1, "Moodys", "S&P", "Fitch", 10);
	        Rating rating3 = new Rating(2, "Moodys", "S&P", "Fitch", 10); // ID différent

	        assertEquals(rating1, rating2);
	        assertNotEquals(rating1, rating3);

	        assertEquals(rating1.hashCode(), rating2.hashCode());
	        assertNotEquals(rating1.hashCode(), rating3.hashCode());
	    }
}
