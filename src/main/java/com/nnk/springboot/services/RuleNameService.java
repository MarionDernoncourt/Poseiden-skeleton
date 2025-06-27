package com.nnk.springboot.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;

@Service
public class RuleNameService {
	private final static Logger logger = LoggerFactory.getLogger(RuleNameService.class);

	private final RuleNameRepository ruleNameRepository;

	public RuleNameService(RuleNameRepository ruleNameRepository) {
		this.ruleNameRepository = ruleNameRepository;
	}

	public List<RuleName> getAllRuleNames() {
		logger.info("Tentative de récupération de tous les RuleNames");
		List<RuleName> ruleNames;
		try {
			ruleNames = ruleNameRepository.findAll();
			logger.info("Nombre de rulenames trouvés : {} ", ruleNames.size());
		} catch (Exception e) {
			logger.warn("Erreur lors de la récupération des Rulenames");
			throw e;
		}
		return ruleNames;
	}

	public RuleName saveRuleName(RuleName ruleName) {
		logger.info("Tentative d'ajout d'un ruleName");

		List<RuleName> existingRuleNames = ruleNameRepository.findAll();

		boolean alreadyExists = existingRuleNames.stream()
				.anyMatch(existing -> existing.getName().equals(ruleName.getName())
						&& existing.getDescription().equals(ruleName.getDescription())
						&& existing.getJson().equals(ruleName.getJson())
						&& existing.getTemplate().equals(ruleName.getTemplate())
						&& existing.getSqlStr().equals(ruleName.getSqlStr())
						&& existing.getSqlPart().equals(ruleName.getSqlPart()));

		if (alreadyExists) {
			logger.warn("Erreur le ruleName existe déjà");
			throw new IllegalArgumentException("Ce RuleName existe déjà.");
		}

		RuleName newRuleName = ruleNameRepository.save(ruleName);
		logger.info("Le RuleName a bien été ajouté");
		return newRuleName;
	}

	public RuleName getRuleNameById(Integer id) {
		logger.info("Tentative de récupération du RuleName id {}", id);
		RuleName ruleName = ruleNameRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Aucun ruleName trouvé"));

		logger.info("RuleName id {} récupéré", id);
		return ruleName;
	}

	public RuleName updateRuleNameById(Integer id, RuleName ruleName) {
		logger.info("Tentative de mise à jour du rulename id {}", id);

		RuleName ruleNameToUpdate = ruleNameRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Aucun rulename avec cet id " + id));

		ruleNameToUpdate.setName(ruleName.getName());
		ruleNameToUpdate.setDescription(ruleName.getDescription());
		ruleNameToUpdate.setJson(ruleName.getJson());
		ruleNameToUpdate.setTemplate(ruleName.getTemplate());
		ruleNameToUpdate.setSqlStr(ruleName.getSqlStr());
		ruleNameToUpdate.setSqlPart(ruleName.getSqlPart());

		ruleNameRepository.save(ruleNameToUpdate);
		logger.info("Rulename id {} bien mis à jour", id);

		return ruleNameToUpdate;
	}
	
	public void deleteRuleNameById(Integer id) {
		logger.info("Tentative de suppression du ruleName id {}", id);
		
		RuleName ruleNameToDelete = ruleNameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Aucun ruleName trouvé avec cet id"));
		
		ruleNameRepository.delete(ruleNameToDelete);
		logger.info("RuleName id {} supprimée avec succès", id);
	}

}
