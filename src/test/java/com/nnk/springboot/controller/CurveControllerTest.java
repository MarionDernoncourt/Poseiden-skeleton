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

import com.nnk.springboot.controllers.CurveController;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CurveController.class)
public class CurveControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CurvePointService curvePointService;

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testGetAllCurvepoints_WhenSuccess() throws Exception {
		List<CurvePoint> curvePoints = List.of(new CurvePoint(1, null, null, null, null, null));

		when(curvePointService.getAllCurvePoints()).thenReturn(curvePoints);

		mockMvc.perform(get("/curvePoint/list")).andExpect(status().isOk()).andExpect(view().name("curvePoint/list"))
				.andExpect(model().attributeExists("curvePoints"))
				.andExpect(model().attribute("curvePoints", curvePoints));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testGetAllCurvePoints_WhenException() throws Exception {
		when(curvePointService.getAllCurvePoints())
				.thenThrow(new RuntimeException("Erreur lors de la récupération des curvepoints"));

		mockMvc.perform(get("/curvePoint/list")).andExpect(status().isOk()).andExpect(view().name("curvePoint/list"))
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur lors de la récupération des curvepoints"));

	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testSaveCurvePoint_WhenSuccess() throws Exception {
		CurvePoint newCurvePoint = new CurvePoint(1, null, null, null, null, null);

		when(curvePointService.saveCurvePoint(any(CurvePoint.class))).thenReturn(newCurvePoint);

		mockMvc.perform(post("/curvePoint/validate").with(csrf()).param("term", "12").param("value", "3"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/curvePoint/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testSaveCurvePoint_WhenException() throws Exception {
		when(curvePointService.saveCurvePoint(any(CurvePoint.class)))
				.thenThrow(new RuntimeException("Erreur lors de la sauvegarde"));

		mockMvc.perform(post("/curvePoint/validate").with(csrf()).param("term", "12").param("value", "3"))
				.andExpect(status().isOk()).andExpect(view().name("curvePoint/add"))
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur lors de la sauvegarde"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testShowUpdateCurvePointForm_WhenSuccess() throws Exception {
		CurvePoint curvePoint = new CurvePoint(1, null, null, null, null, null);

		when(curvePointService.getCurvePointById(anyInt())).thenReturn(curvePoint);

		mockMvc.perform(get("/curvePoint/update/1")).andExpect(status().isOk())
				.andExpect(model().attributeExists("curvePoint"))
				.andExpect(model().attribute("curvePoint", curvePoint));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testShowUpdateCurvePointForm_WhenException() throws Exception {
		when(curvePointService.getCurvePointById(anyInt()))
				.thenThrow(new RuntimeException("Erreur pour récupérer le curvepoint"));

		mockMvc.perform(get("/curvePoint/update/1")).andExpect(status().isOk())
				.andExpect(view().name("curvePoint/list")).andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur pour récupérer le curvepoint"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testUpdateCurvePointById_WhenSuccess() throws Exception {
		CurvePoint newCurvePoint = new CurvePoint(1, 2, null, 20.00, 15.50, null);

		when(curvePointService.updateCurvePoint(anyInt(), any(CurvePoint.class))).thenReturn(newCurvePoint);

		mockMvc.perform(post("/curvePoint/update/1").with(csrf()).param("curveId", "2").param("term", "20.00")
				.param("value", "15.50")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/curvePoint/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testUpdateCurvePointById_WhenException() throws Exception {
		when(curvePointService.updateCurvePoint(anyInt(), any(CurvePoint.class)))
				.thenThrow(new RuntimeException("Erreur lors de la mise à jour"));

		mockMvc.perform(post("/curvePoint/update/1").with(csrf()).param("curveId", "2").param("term", "20.00")
				.param("value", "15.50")).andExpect(status().isOk()).andExpect(view().name("curvePoint/update"))
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur lors de la mise à jour"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testDeleteCurvePointById_WhenSuccess() throws Exception {

		doNothing().when(curvePointService).deleteCurvePointById(anyInt());

		mockMvc.perform(get("/curvePoint/delete/1")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/curvePoint/list"));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testDeleteCurvePointById_WhenException() throws Exception {
		
		doThrow(new RuntimeException("Erreur lors de la suppression")).when(curvePointService).deleteCurvePointById(anyInt());
		
		mockMvc.perform(get("/curvePoint/delete/1")).andExpect(status().isOk()).andExpect(view().name("curvePoint/list"))
		.andExpect(model().attributeExists("error")).andExpect(model().attribute("error", "Erreur lors de la suppression"));
	}
}
