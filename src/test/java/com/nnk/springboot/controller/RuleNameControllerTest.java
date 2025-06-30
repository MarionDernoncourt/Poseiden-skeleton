package com.nnk.springboot.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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

import com.nnk.springboot.controllers.RuleNameController;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RuleNameController.class)
public class RuleNameControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RuleNameService ruleNameService;

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testGetAllRuleNames_WhenSuccess() throws Exception {
		List<RuleName> ruleNames = List
				.of(new RuleName(1, "Rule Name", "Description", "Json", "Template", "SQL", "SQL Part"));

		when(ruleNameService.getAllRuleNames()).thenReturn(ruleNames);

		mockMvc.perform(get("/ruleName/list")).andExpect(status().isOk()).andExpect(view().name("ruleName/list"))
				.andExpect(model().attributeExists("ruleNames")).andExpect(model().attribute("ruleNames", ruleNames));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testGetAllRuleNames_WhenException() throws Exception {
		when(ruleNameService.getAllRuleNames())
				.thenThrow(new RuntimeException("Erreur lors de la récupération des rulenames"));

		mockMvc.perform(get("/ruleName/list")).andExpect(status().isOk()).andExpect(view().name("ruleName/list"))
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur lors de la récupération des rulenames"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testSaveRuleName_WhenSuccess() throws Exception {
		RuleName newRuleName = new RuleName(1, "Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
		when(ruleNameService.saveRuleName(any(RuleName.class))).thenReturn(newRuleName);

		mockMvc.perform(post("/ruleName/validate").param("name", "Rule Name").param("description", "Description")
				.param("json", "Json").param("template", "Template").param("sqlStr", "SQL").param("sqlPart", "SQL Part")
				.with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/ruleName/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testSaveRuleName_WhenException() throws Exception {
		when(ruleNameService.saveRuleName(any(RuleName.class)))
				.thenThrow(new RuntimeException("Erreur lors de la sauvegarde"));

		mockMvc.perform(post("/ruleName/validate").param("name", "Rule Name").param("description", "Description")
				.param("json", "Json").param("template", "Template").param("sqlStr", "SQL").param("sqlPart", "SQL Part")
				.with(csrf())).andExpect(status().isOk()).andExpect(view().name("ruleName/add"))
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur lors de la sauvegarde"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testShowUpdateForm_WhenSuccess() throws Exception {
		RuleName ruleName = new RuleName(1, "Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

		when(ruleNameService.getRuleNameById(anyInt())).thenReturn(ruleName);

		mockMvc.perform(get("/ruleName/update/1")).andExpect(status().isOk()).andExpect(view().name("ruleName/update"))
				.andExpect(model().attributeExists("ruleName")).andExpect(model().attribute("ruleName", ruleName));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testShowUpdateForm_WhenException() throws Exception {
		when(ruleNameService.getRuleNameById(anyInt()))
				.thenThrow(new IllegalArgumentException("Aucun ruleName avec cet id"));

		mockMvc.perform(get("/ruleName/update/1")).andExpect(status().isOk()).andExpect(view().name("ruleName/list"))
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Aucun ruleName avec cet id"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testUpdateRuleName_WhenSuccess() throws Exception {
		RuleName ruleNameUpdated = new RuleName();
		ruleNameUpdated.setId(1);
		ruleNameUpdated.setDescription("new description");

		when(ruleNameService.updateRuleNameById(eq(1), any(RuleName.class))).thenReturn(ruleNameUpdated);

		mockMvc.perform(post("/ruleName/update/1").param("description", "new descriptions").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/ruleName/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testUpdateRuleName_WhenException() throws Exception {
		when(ruleNameService.updateRuleNameById(eq(1), any(RuleName.class)))
				.thenThrow(new RuntimeException("Erreur lors de la mise à jour"));

		mockMvc.perform(post("/ruleName/update/1").with(csrf())).andExpect(status().isOk())
				.andExpect(view().name("ruleName/update")).andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur lors de la mise à jour"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testDeleteRuleNameById_WhenSuccess() throws Exception {

		doNothing().when(ruleNameService).deleteRuleNameById(anyInt());

		mockMvc.perform(get("/ruleName/delete/1").with(csrf())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/ruleName/list"));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testDeleteRuleNameById_WhenException () throws Exception {
		doThrow(new RuntimeException("Erreur lors de la suppression du ruleName")).when(ruleNameService).deleteRuleNameById(anyInt());
		
		mockMvc.perform(get("/ruleName/delete/1").with(csrf())).andExpect(status().isOk()).andExpect(view().name("ruleName/list"))
		.andExpect(model().attributeExists("error"))
		.andExpect(model().attribute("error", "Erreur lors de la suppression du ruleName"));
		}
}
