package com.nnk.springboot.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

import com.nnk.springboot.controllers.TradeController;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TradeController.class)
public class TradeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TradeService tradeService;
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testGetAllTrades_WhenSuccess() throws Exception {
		List<Trade> trades = List.of(new Trade(1, "account1", "type1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
		
		when(tradeService.getAllTrades()).thenReturn(trades);
		
		mockMvc.perform(get("/trade/list")).andExpect(status().isOk()).andExpect(view().name("trade/list"))
		.andExpect(model().attributeExists("trades")).andExpect(model().attribute("trades", trades));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testGetAllTrades_WhenException() throws Exception {
		when(tradeService.getAllTrades()).thenThrow(new RuntimeException("Erreur lors de la récupération des trade"));
		
		mockMvc.perform(get("/trade/list")).andExpect(status().isOk()).andExpect(view().name("trade/list"))
		.andExpect(model().attributeExists("error")).andExpect(model().attribute("error", "Erreur lors de la récupération des trade"));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testShowUpdateForm_WhenSuccess() throws Exception {
		Trade tradeToUpdate = new Trade(1, "account1", "type1", 2.00, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		
		when(tradeService.getTradeById(anyInt())).thenReturn(tradeToUpdate);
		
		mockMvc.perform(get("/trade/update/1").with(csrf()).param("account", "account1").param("type", "type1").param("buyQuantity", "2.00"))
		.andExpect(status().isOk()).andExpect(view().name("trade/update"))
		.andExpect(model().attributeExists("trade")).andExpect(model().attribute("trade", tradeToUpdate));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testShowUpdateForm_WhenException() throws Exception {
	when(tradeService.getTradeById(anyInt())).thenThrow(new RuntimeException("Erreur lors de l'affichage du formulaire"));
	
	mockMvc.perform(get("/trade/update/1")).andExpect(status().isOk()).andExpect(view().name("trade/list"))
	.andExpect(model().attributeExists("error")).andExpect(model().attribute("error", "Erreur lors de l'affichage du formulaire"));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testUpdateTradeById_WhenSuccess() throws Exception {
		Trade tradeUpdated = new Trade(1, "account1", "type1", 2.00, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

		when(tradeService.updateTradeById(anyInt(), any(Trade.class))).thenReturn(tradeUpdated);
		
		mockMvc.perform(post("/trade/update/1").with(csrf()).param("account", "account1").param("type", "type1").param("buyQuantity", "2.00"))
		.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/trade/list"));
	}

	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testUpdateTradeById_WhenException() throws Exception {
		when(tradeService.updateTradeById(anyInt(), any(Trade.class))).thenThrow(new RuntimeException("Erreur lors de la mise à jour"));
		
		mockMvc.perform(post("/trade/update/1").with(csrf()).param("account", "account1").param("type", "type1").param("buyQuantity", "2.00"))
		.andExpect(status().isOk()).andExpect(view().name("trade/update"))
		.andExpect(model().attributeExists("error")).andExpect(model().attribute("error", "Erreur lors de la mise à jour"));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testDeleteTradeById_WhenSuccess() throws Exception {
		doNothing().when(tradeService).deleteTradeById(anyInt());
		
		mockMvc.perform(get("/trade/delete/1")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/trade/list"));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testDeleteTradeById_WhenException() throws Exception {
		doThrow(new RuntimeException("Erreur lors de la suppression")).when(tradeService).deleteTradeById(anyInt());
		
		mockMvc.perform(get("/trade/delete/1")).andExpect(status().isOk()).andExpect(view().name("trade/list"))
		.andExpect(model().attributeExists("error")).andExpect(model().attribute("error", "Erreur lors de la suppression"));
	}
	

}
