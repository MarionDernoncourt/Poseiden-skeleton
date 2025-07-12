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

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;

@ExtendWith(MockitoExtension.class)
public class RuleNameTest {

	@Mock
	private RuleNameRepository ruleNameRepository;

	@InjectMocks
	private RuleNameService ruleNameService;

	private RuleName rule;

	@BeforeEach
	void setUp() {
		rule = new RuleName();
		rule.setId(1);
		rule.setName("RuleName");
		rule.setDescription("Description");

	}

	@Test
	public void testGetAllRuleName_WhenSuccess() {
		List<RuleName> expectedRuleNames = new ArrayList<>();

		expectedRuleNames.add(rule);

		when(ruleNameRepository.findAll()).thenReturn(expectedRuleNames);

		List<RuleName> actualRuleNames = ruleNameService.getAllRuleNames();

		assertEquals(expectedRuleNames, actualRuleNames);
		assertEquals(expectedRuleNames.size(), actualRuleNames.size());
	}

	@Test
	public void testSaveRuleName_WhenSuccess() {
		when(ruleNameRepository.save(any(RuleName.class))).thenReturn(rule);

		RuleName savedRuleName = ruleNameService.saveRuleName(rule);

		assertEquals(rule.getDescription(), savedRuleName.getDescription());
		verify(ruleNameRepository, times(1)).save(rule);
	}

	@Test
	public void testSaveRuleName_WhenException() {
		doThrow(new IllegalArgumentException("Erreur lors de la sauvegarde")).when(ruleNameRepository)
				.save(any(RuleName.class));

		assertThrows(IllegalArgumentException.class, () -> {
			ruleNameService.saveRuleName(rule);
		});
		verify(ruleNameRepository, times(1)).save(any(RuleName.class));
	}

	@Test
	public void testGetRuleNameById_WhenSuccess() {
		when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(rule));

		RuleName foundRule = ruleNameService.getRuleNameById(1);

		assertEquals(rule.getId(), foundRule.getId());
		assertEquals(rule.getDescription(), foundRule.getDescription());
		verify(ruleNameRepository, times(1)).findById(anyInt());
	}

	@Test
	public void testGetRuleNameById_WhenException() {
		when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> {
			ruleNameService.getRuleNameById(5);
		});

		verify(ruleNameRepository, times(1)).findById(anyInt());
	}

	@Test
	public void testUpdateRuleName_WhenSuccess() {
		when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(rule));
		
		RuleName newRuleData = new RuleName();
		newRuleData.setDescription("newDescription");
		newRuleData.setName("newName");
		
		RuleName expectedSavedRule = new RuleName();
		expectedSavedRule.setId(rule.getId());
		expectedSavedRule.setDescription(newRuleData.getDescription());
		expectedSavedRule.setName(newRuleData.getName());
		
		when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(rule));
		
		RuleName actualUpdateRule = ruleNameService.updateRuleNameById(rule.getId(), newRuleData);
		
		assertEquals(expectedSavedRule, actualUpdateRule);
		
		verify(ruleNameRepository, times(1)).findById(anyInt());
		verify(ruleNameRepository, times(1)).save(any(RuleName.class));
	}
	
	@Test
	public void testUpdateRuleName_WhenExceptionDuringSave() {
		when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(rule));
		
		RuleName newRuleData = new RuleName();
		newRuleData.setDescription("newDescription");
		newRuleData.setName("newName");
		
		doThrow(new RuntimeException("Erreur lors de la sauvegarde de la mise à jour")).when(ruleNameRepository).save(any(RuleName.class));
		
		assertThrows(RuntimeException.class, () -> {
			ruleNameService.updateRuleNameById(rule.getId(), newRuleData);
		});
		
		verify(ruleNameRepository, times(1)).findById(anyInt());
		verify(ruleNameRepository, times(1)).save(any(RuleName.class));
	}
	
	@Test
	public void testUpdateRuleName_WhenNotFound() {
		when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		RuleName newRuleData = new RuleName();
		newRuleData.setDescription("newDescription");
		newRuleData.setName("newName");
		
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			ruleNameService.updateRuleNameById(rule.getId(), newRuleData);
		});
		
		assertEquals("Aucun rulename avec cet id", thrown.getMessage());
		verify(ruleNameRepository, times(1)).findById(anyInt());
		verify(ruleNameRepository, never()).save(any(RuleName.class));
	}
	
	@Test
	public void testDeleteWhenSuccess() {
		when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(rule));
		
		ruleNameService.deleteRuleNameById(rule.getId());;
		
		verify(ruleNameRepository,times(1)).delete(any(RuleName.class));
		}
	
	@Test
	public void testDeleteRuleName_WhenNotFound() {
		when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			ruleNameService.deleteRuleNameById(rule.getId());
		});
		verify(ruleNameRepository, times(1)).findById(anyInt());
		verify(ruleNameRepository, never()).delete(any(RuleName.class));
	}
	
	 @Test
	    public void testRuleNameGettersAndSetters() {
	        RuleName ruleName = new RuleName();

	        ruleName.setId(1);
	        ruleName.setName("TestName");
	        ruleName.setDescription("TestDescription");
	        ruleName.setJson("TestJson");
	        ruleName.setTemplate("TestTemplate");
	        ruleName.setSqlStr("TestSqlStr");
	        ruleName.setSqlPart("TestSqlPart");

	        assertEquals(1, ruleName.getId());
	        assertEquals("TestName", ruleName.getName());
	        assertEquals("TestDescription", ruleName.getDescription());
	        assertEquals("TestJson", ruleName.getJson());
	        assertEquals("TestTemplate", ruleName.getTemplate());
	        assertEquals("TestSqlStr", ruleName.getSqlStr());
	        assertEquals("TestSqlPart", ruleName.getSqlPart());
	    }
	 
	 @Test
	    public void testRuleNameEqualsAndHashCode() {
	        RuleName rule1 = new RuleName(1, "Name", "Desc", "Json", "Template", "SqlStr", "SqlPart");
	        RuleName rule2 = new RuleName(1, "Name", "Desc", "Json", "Template", "SqlStr", "SqlPart");
	        RuleName rule3 = new RuleName(2, "Name", "Desc", "Json", "Template", "SqlStr", "SqlPart"); // ID différent

	        assertEquals(rule1, rule2);
	        assertNotEquals(rule1, rule3);

	        assertEquals(rule1.hashCode(), rule2.hashCode());
	        assertNotEquals(rule1.hashCode(), rule3.hashCode());
	    }

}