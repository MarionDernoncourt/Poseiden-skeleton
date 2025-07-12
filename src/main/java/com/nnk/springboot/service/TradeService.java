package com.nnk.springboot.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;

@Service
public class TradeService {

	private static final Logger logger = LoggerFactory.getLogger(TradeService.class);

	private final TradeRepository tradeRepository;

	public TradeService(TradeRepository tradeRepository) {
		this.tradeRepository = tradeRepository;
	}

	public List<Trade> getAllTrades() {
		logger.info("Tentative de récupération de tous les trades");

		List<Trade> trades = tradeRepository.findAll();

		logger.info("Nombre de trades trouvés : {}", trades.size());
		return trades;
	}
	
	public Trade saveTrade(Trade trade) {
		logger.info("Tentative de sauvegarde du trade {}", trade);
		
		Trade savedTrade = tradeRepository.save(trade);
		logger.info("Le trade {} a bien été sauvegardé", trade);
		return savedTrade;
	}
	
	public Trade getTradeById(Integer id) {
		logger.info("Tentative de récupération du trade id {}", id);
		
		Trade trade = tradeRepository.findById(id).orElseThrow(() -> {
			logger.warn("Aucun trade trouvé avec l'id {}", id);
			return new IllegalArgumentException("Aucun trade trouvé");
		});
		
		return trade;
	}
	
	public Trade updateTradeById(Integer id, Trade trade) {
		logger.info("Tentative de mise à jour du trade id {}", id);
		
		Trade tradeToUpdate = tradeRepository.findById(id).orElseThrow(() -> {
			logger.warn("Aucun trade trouvé avec l'id {}", id);
			return new IllegalArgumentException("Aucun trade trouvé");
		});
		
		tradeToUpdate.setAccount(trade.getAccount());
		tradeToUpdate.setType(trade.getType());
		tradeToUpdate.setBuyQuantity(trade.getBuyQuantity());
		
		Trade tradeUpdated = tradeRepository.save(tradeToUpdate);
		logger.info("Le trade id {} a bién été mis à jour", id);
		
		return tradeUpdated;
	}
	
	public void deleteTradeById(Integer id)  {
		logger.info("Tentative de suppression de trade id {}", id);
		
		Trade tradeToDelete = tradeRepository.findById(id).orElseThrow(() -> {
			logger.warn("Echec lors de la suppression du trade id {}", id);
			return new IllegalArgumentException("Aucun trade trouvé");
		});
		
		tradeRepository.delete(tradeToDelete);
		logger.info("Le trade id {} a bien été supprimé", id);
	}

}
