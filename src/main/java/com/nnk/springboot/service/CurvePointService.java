package com.nnk.springboot.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;

@Service
public class CurvePointService {

	private final static Logger logger = LoggerFactory.getLogger(CurvePointService.class);

	private final CurvePointRepository curvePointRepository;

	public CurvePointService(CurvePointRepository curvePointRepository) {
		this.curvePointRepository = curvePointRepository;
	}

	public List<CurvePoint> getAllCurvePoints() {
		logger.info("Tentative de récupération de tous les curvepoints");
		List<CurvePoint> curvePoints = curvePointRepository.findAll();

		try {
			logger.info("Nombes de curvePoint trouvés : {}", curvePoints.size());
			return curvePoints;
		} catch (Exception e) {
			logger.info("Erreur lors de la récupération des curvepoints");
			throw e;
		}
	}

	public CurvePoint saveCurvePoint(CurvePoint curvePoint) {
		logger.info("Tentative d'ajout du curvePoint : {}", curvePoint);
		try {
			CurvePoint savedCurvePoint = curvePointRepository.save(curvePoint);
			logger.info("Le curvePoint {} a bien été ajouté", curvePoint);
			return savedCurvePoint;
		} catch (Exception e) {
			logger.warn("Echec de la sauvegarde du curvePoint {}", curvePoint);
			throw e;
		}
	}

	public CurvePoint getCurvePointById(Integer id) {
		logger.info("Tentative d'afficher le formulaire de mise à jour pour le curvePoint id {}", id);
		CurvePoint curvePointToUpdate = curvePointRepository.findById(id).orElseThrow(() -> {
			logger.warn("Pas de curvePoint trouvé avec l'id {}", id);
			return new IllegalArgumentException("Aucun curvePoint trouvé");
		});
		return curvePointToUpdate;
	}
	
	public CurvePoint updateCurvePoint(Integer id, CurvePoint curvePoint) {
		logger.info("Tentative de mise à jour du curvepoint {}", curvePoint);
		
		CurvePoint existingCurvePoint = curvePointRepository.findById(id).orElseThrow(() -> {
			logger.warn("Aucun curvePoint trouvé avec cet id {}", id);
			return new IllegalArgumentException("Aucun curvePoint trouvé");
		});
		
		existingCurvePoint.setTerm(curvePoint.getTerm());
		existingCurvePoint.setValue(curvePoint.getValue());
		existingCurvePoint.setCurveId(curvePoint.getCurveId());
		
		CurvePoint updatedCurvePoint = curvePointRepository.save(existingCurvePoint);
		logger.info("Le curvePoint id {} a bien été mis à jour.", id);
		return updatedCurvePoint;
		
	}
	
	public void deleteCurvePointById(Integer id) {
		logger.info("Tentative de suppression du curvepoint id {}", id);
		
		CurvePoint curvePointToDelete = curvePointRepository.findById(id).orElseThrow(() -> {
			logger.warn("Aucun curvepoint avec l'id {} trouvé", id);
			return new IllegalArgumentException("Aucun curvepoint trouvé");
		});
		
		curvePointRepository.delete(curvePointToDelete);
		logger.info("Le curvepoint id {} a bien été supprimée", id);;
	}
}
