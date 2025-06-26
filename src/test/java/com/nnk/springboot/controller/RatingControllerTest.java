package com.nnk.springboot.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.nnk.springboot.controllers.RatingController;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RatingController.class)
public class RatingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RatingService ratingService;

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testGetAllRatings_WhenSuccess() throws Exception {

		List<Rating> mockRatings = List.of(new Rating(1, "Moody", "S&P", "Fitch", 1));

		when(ratingService.getAllRatings()).thenReturn(mockRatings);

		mockMvc.perform(get("/rating/list")).andExpect(status().isOk()).andExpect(model().attributeExists("ratings"))
				.andExpect(view().name("rating/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = "{USER}")
	public void testGetAllRatings_WhenException() throws Exception {
		when(ratingService.getAllRatings()).thenThrow(new RuntimeException("Erreur du service"));

		mockMvc.perform(get("/rating/list")).andExpect(status().isOk()).andExpect(view().name("rating/list"))
				.andExpect(model().attributeExists("error")).andExpect(model().attribute("error", "Erreur du service"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testAddRatingForm_WhenSuccess() throws Exception {
		mockMvc.perform(get("/rating/add")).andExpect(status().isOk()).andExpect(view().name("rating/add"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testSaveRating_WhenSuccess() throws Exception {
		Rating rating = new Rating(1, "Moody", "S&P", "Fitch", 1);
		when(ratingService.saveRating(rating)).thenReturn(rating);

		mockMvc.perform(post("/rating/validate").param("moodysRating", "Moody").param("sandPRating", "S&P")
				.param("fitchRating", "Fitch").param("orderNumber", "1").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/rating/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testSaveRating_WhenException() throws Exception {
		when(ratingService.saveRating(any(Rating.class))).thenThrow(new RuntimeException("Erreur interne"));

		mockMvc.perform(post("/rating/validate").param("moodysRating", "Moody").param("sandPRating", "S&P")
				.param("fitchRating", "Fitch").param("orderNumber", "1").with(csrf())).andExpect(status().isOk())
				.andExpect(view().name("rating/add")).andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur interne"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testShowUpdateForm_WhenSuccess() throws Exception {
		Rating rating = new Rating();
		rating.setId(1);
		rating.setMoodysRating("Moody");
		when(ratingService.getRatingById(1)).thenReturn(rating);

		mockMvc.perform(get("/rating/update/1")).andExpect(status().isOk()).andExpect(view().name("rating/update"))
				.andExpect(model().attributeExists("rating")).andExpect(model().attribute("rating", rating));
	}

	@Test
	@WithMockUser(username = "username", roles = { "USER" })
	public void testShowUpdateForm_WhenException() throws Exception {
		when(ratingService.getRatingById(anyInt())).thenThrow(new RuntimeException("Erreur interne"));

		mockMvc.perform(get("/rating/update/1")).andExpect(status().isOk()).andExpect(view().name("rating/list"))
				.andExpect(model().attributeExists("error")).andExpect(model().attribute("error", "Erreur interne"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testDeleteRatingById_WhenSuccess() throws Exception {
		Rating rating = new Rating(1, "Moody", "S&P", "Fitch", 1);
		when(ratingService.getRatingById(1)).thenReturn(rating);

		mockMvc.perform(delete("/rating/delete/1").with(csrf())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/rating/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testDeleteRatingById_WhenException() throws Exception {
		doThrow(new RuntimeException("Aucun rating trouvé")).when(ratingService).deleteRatingById(anyInt());;
		List<Rating> ratings = List.of(new Rating(2, "moodys", "S&P", "fitch", 8));

		when(ratingService.getAllRatings()).thenReturn(ratings);
		mockMvc.perform(delete("/rating/delete/1").with(csrf())).andExpect(status().isOk())
				.andExpect(view().name("rating/list")).andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Aucun rating trouvé"))
				.andExpect(model().attributeExists("ratings")).andExpect(model().attribute("ratings", ratings));
	}

}