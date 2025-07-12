package com.nnk.springboot.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;

@Service
public class BidListService {

	private final static Logger logger = LoggerFactory.getLogger(BidListService.class);

	private final BidListRepository bidListRepository;

	public BidListService(BidListRepository bidListRepository) {
		this.bidListRepository = bidListRepository;
	}

	public List<BidList> getAllBidList() {
		logger.info("Tentative de récupération de la liste des bidList");

		List<BidList> bidList = bidListRepository.findAll();

		logger.info("Nombe de bidList trouvée :", bidList.size());
		return bidList;
	}

	public BidList saveBidList(BidList bid) {
		logger.info("Tentative de sauvegarde du bid {}", bid.getId());

		BidList savedBid = bidListRepository.save(bid);
		logger.info("Bid bien sauvegarder");

		return savedBid;
	}

	public BidList getBidListById(Integer id) {
		logger.info("Tentative de récupération du bid id {}", id);

		BidList bid = bidListRepository.findById(id).orElseThrow(() -> {
			logger.warn("Aucun bid trouvé avec l'id {}", id);
			return new IllegalArgumentException("Aucun bid trouvé");
		});
		logger.info("Bid id {} trouvé", id);
		return bid;

	}
	
	public BidList updateBidListById(Integer id, BidList bid) {
		logger.info("Tentative de mise à jour du bidList id {}", id);
		
		BidList existingBid = bidListRepository.findById(id).orElseThrow(() -> {
			logger.warn("Aucun bid avec cet id {}", id);
			return new IllegalArgumentException("Aucun bid trouvé");
		}); 
		
		existingBid.setAccount(bid.getAccount());
		existingBid.setType(bid.getType());
		existingBid.setBidQuantity(bid.getBidQuantity());
		
		BidList updatedBid = bidListRepository.save(existingBid);
		logger.info("Le bid id {} a bien été modifié", id);
		return updatedBid;
	}
	
	public void deleteById(Integer id) {
		logger.info("Tentative de suppression du bid id {}", id);
		
		BidList bid = bidListRepository.findById(id).orElseThrow(() -> {
			logger.warn("Aucun bid avec cet id {}", id);
			return new IllegalArgumentException("Aucun bid trouvé");
		});
		
		bidListRepository.delete(bid);
		logger.info("Le bid id {} a bien été supprimé", id);
	}
}
