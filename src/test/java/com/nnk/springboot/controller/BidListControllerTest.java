package com.nnk.springboot.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
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

import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BidListController.class)
public class BidListControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BidListService bidListService;

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testGetAllBidLists_WhenSuccess() throws Exception {
		BidList bid = new BidList();
		bid.setId(1);
		bid.setAccount("account1");
		bid.setType("type1");
		bid.setBidQuantity(123.45);

		List<BidList> mockBidLists = List.of(bid);

		when(bidListService.getAllBidList()).thenReturn(mockBidLists);

		mockMvc.perform(get("/bidList/list")).andExpect(status().isOk()).andExpect(view().name("bidList/list"))
				.andExpect(model().attributeExists("bidLists")).andExpect(model().attribute("bidLists", mockBidLists));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testGetAllBidList_WhenException() throws Exception {
		when(bidListService.getAllBidList()).thenThrow(new RuntimeException("Erreur lors de la récupération des bid"));

		mockMvc.perform(get("/bidList/list")).andExpect(status().isOk()).andExpect(view().name("bidList/list"))
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur lors de la récupération des bid"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testSaveBidList_WhenSuccess() throws Exception {
		BidList newBid = new BidList();
		newBid.setId(1);
		newBid.setAccount("account1");
		newBid.setType("type1");
		newBid.setBidQuantity(123.45);

		when(bidListService.saveBidList(any(BidList.class))).thenReturn(newBid);

		mockMvc.perform(post("/bidList/validate").with(csrf()).param("account", "account1").param("type", "type1")
				.param("bidQuantity", "123.45")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/bidList/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testSaveBidList_WhenException() throws Exception {
		when(bidListService.saveBidList(any(BidList.class)))
				.thenThrow(new RuntimeException("Erreur lors de la sauvegarde"));

		mockMvc.perform(post("/bidList/validate").with(csrf()).param("account", "account1").param("type", "type1")
				.param("bidQuantity", "123.45")).andExpect(status().isOk()).andExpect(view().name("bidList/add"))
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur lors de la sauvegarde"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testShowUpdateForm_WhenSuccess() throws Exception {
		BidList bid = new BidList();
		bid.setId(1);
		bid.setAccount("account1");
		bid.setType("type1");
		bid.setBidQuantity(123.45);

		when(bidListService.getBidListById(anyInt())).thenReturn(bid);

		mockMvc.perform(get("/bidList/update/1").with(csrf())).andExpect(status().isOk())
				.andExpect(view().name("bidList/update")).andExpect(model().attributeExists("bidList"))
				.andExpect(model().attribute("bidList", bid));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testShowUpdateForm_WhenException() throws Exception {
		when(bidListService.getBidListById(anyInt()))
				.thenThrow(new RuntimeException("Erreur pour charger le formulaire"));

		mockMvc.perform(get("/bidList/update/1").with(csrf())).andExpect(status().isOk())
				.andExpect(view().name("bidList/list")).andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur pour charger le formulaire"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testUpdateBid_WhenSuccess() throws Exception {
		BidList updatedBid = new BidList();
		updatedBid.setId(1);
		updatedBid.setAccount("account1");
		updatedBid.setType("type1");
		updatedBid.setBidQuantity(123.45);

		when(bidListService.updateBidListById(anyInt(), any(BidList.class))).thenReturn(updatedBid);

		mockMvc.perform(post("/bidList/update/1").with(csrf()).param("account", "account1").param("type", "type1")
				.param("bidQuantity", "123.45")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/bidList/list"));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testUpdateBid_WhenException() throws Exception {
		when(bidListService.updateBidListById(anyInt(), any(BidList.class))).thenThrow(new RuntimeException("Erreur lors de la mise à jour"));
		
		mockMvc.perform(post("/bidList/update/1").with(csrf()).param("account", "account1").param("type", "type1").param("bidQuantity", "123.45")).andExpect(status().isOk())
		.andExpect(view().name("bidList/update")).andExpect(model().attributeExists("error")).andExpect(model().attribute("error", "Erreur lors de la mise à jour"));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testDeleteBidById_WhenSuccess() throws Exception {
		BidList bid = new BidList();
		bid.setId(1);
		bid.setAccount("account1");
		bid.setType("type1");
		bid.setBidQuantity(123.45);
		
		doNothing().when(bidListService).deleteById(anyInt());
		
		mockMvc.perform(delete("/bidList/delete/1").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/bidList/list"));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testDeleteBidById_WhenException() throws Exception {
		doThrow(new RuntimeException("Erreur lors de la suppression")).when(bidListService).deleteById(anyInt());
		
		mockMvc.perform(delete("/bidList/delete/1").with(csrf())).andExpect(status().isOk()).andExpect(view().name("bidList/list"))
		.andExpect(model().attributeExists("error")).andExpect(model().attribute("error", "Erreur lors de la suppression"));
	}
}
